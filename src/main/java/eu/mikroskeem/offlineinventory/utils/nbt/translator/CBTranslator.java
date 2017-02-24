package eu.mikroskeem.offlineinventory.utils.nbt.translator;


import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.*;
import eu.mikroskeem.utils.reflect.Reflect;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static eu.mikroskeem.utils.bukkit.ServerUtils.getNmsVersion;

/**
 * Converts raw NBT data to NMS or Bukkit ItemStack using reflection.
 * Retains metadata
 */
public class CBTranslator implements Translator {
    private Class<?> cbItemStack;
    private Class<?> NMSItem;
    private Class<?> NMSItemStack;
    private Class<?> NMSChatSerializer;
    private Constructor<?> newItemStack;
    private Method NMSSetRepairCost;
    private Method NMSGetItem;
    private Method cBAsBukkitCopy;

    public CBTranslator(){
        cbItemStack = checkNotNull(
                Reflect.getClass(String.format("org.bukkit.craftbukkit.%s.inventory.CraftItemStack", getNmsVersion())),
                String.format("Could not get class 'org.bukkit.craftbukkit.%s.inventory.CraftItemStack'", getNmsVersion())
        );
        NMSItem = checkNotNull(
                Reflect.getClass(String.format("net.minecraft.server.%s.Item", getNmsVersion())),
                String.format("Could not get class 'net.minecraft.server.%s.Item'", getNmsVersion())
        );
        NMSItemStack = checkNotNull(
                Reflect.getClass(String.format("net.minecraft.server.%s.ItemStack", getNmsVersion())),
                String.format("Could not get class 'net.minecraft.server.%s.ItemStack'", getNmsVersion())
        );
        NMSChatSerializer = checkNotNull(
                Reflect.getClass(String.format("net.minecraft.server.%s.IChatBaseComponent$ChatSerializer", getNmsVersion())),
                String.format("Could not get class 'net.minecraft.server.%s.IChatBaseComponent$ChatSerializer'", getNmsVersion())
        );
        NMSSetRepairCost = checkNotNull(
                Reflect.getMethod(NMSItemStack, "setRepairCost", int.class),
                String.format("Could not get static method 'setRepairCost' from '%s'", NMSItemStack.getName())
        );
        NMSGetItem = checkNotNull(
                Reflect.getMethod(NMSItem, "b", String.class),
                String.format("Could not get static method 'b' from '%s'", NMSItem.getName())
        );
        cBAsBukkitCopy = checkNotNull(
                Reflect.getMethod(cbItemStack, "asBukkitCopy", NMSItemStack),
                String.format("Could not get static method 'asBukkitCopy' from '%s'", cbItemStack.getName())
        );
        try {
            newItemStack = NMSItemStack.getConstructor(NMSItem, int.class, int.class, boolean.class);
        } catch (Exception e){
            throw new NullPointerException(String.format("Could not get constructor [Item, int, int, boolean] from 'net.minecraft.server.%s.ItemStack'", getNmsVersion()));
        }
    }

    /* Translates to net.minecraft.server.ItemStack */
    @Override
    public Object getTranslatedItem(INBTItem nbtItem) {
        return getNMSItemStack(nbtItem);
    }


    /* Helper methods */
    public ItemStack getBukkitItemStack(INBTItem inbtItem){
        try {
            ItemStack is = (ItemStack)Reflect.invokeMethod(cBAsBukkitCopy, null, checkNotNull(
                    getNMSItemStack(inbtItem),
                    "getNMSItemStack returned null!"
            ));
            if(is == null){
                return new ItemStack(Material.AIR);
            }
            ItemMeta im = is.getItemMeta();
            /* Set enchantments and other meta options */
            if(inbtItem instanceof NBTBasicItem){
                NBTBasicItem item = (NBTBasicItem) inbtItem;
                if(item.getLore() != null){
                    im.setLore(item.getLore());
                }
                if(item.getHideFlags() != null){
                    item.getHideFlags().forEach(flag->im.addItemFlags(
                            ItemHideFlagsMap.valueOf(flag.name()).bukkitFlag
                    ));
                }
                im.setUnbreakable(item.isUnbreakable());
                /* Book & Quill/Written book */
                if(inbtItem instanceof NBTBookAndQuill){
                    NBTBookAndQuill book = (NBTBookAndQuill)inbtItem;
                    BookMeta bm = (BookMeta) im;
                    bm.setAuthor(book.getAuthor());
                    bm.setTitle(book.getTitle());
                    bm.setPages(book.getPages().stream().map(this::deserializeJson).collect(Collectors.toList()));
                }
                if(inbtItem instanceof NBTShulkerBox){
                    NBTShulkerBox shulkerBox = (NBTShulkerBox)inbtItem;
                    BlockStateMeta bsm = (BlockStateMeta)im;
                    ShulkerBox sb = (ShulkerBox)bsm.getBlockState();
                    List<ItemStack> contents = shulkerBox.getContents().stream()
                            .map(this::getBukkitItemStack).collect(Collectors.toList());
                    sb.getInventory().setContents(contents.toArray(new ItemStack[0]));
                    bsm.setBlockState(sb);
                }
                if(inbtItem instanceof NBTSkull){
                    NBTSkull skull = (NBTSkull)inbtItem;
                    SkullMeta sm = (SkullMeta)im;
                    /* This sucks :c */
                    sm.setOwner(skull.getSkullOwner().getName());
                }
                if(inbtItem instanceof NBTPotion){
                    NBTPotion potion = (NBTPotion)inbtItem;
                    PotionMeta pm = (PotionMeta)im;
                    //pm.setBasePotionData(null);
                    /* TODO: finish this */
                }
                if(inbtItem instanceof NBTSpawnEgg){
                    NBTSpawnEgg spawnEgg = (NBTSpawnEgg)inbtItem;
                    SpawnEggMeta sem = (SpawnEggMeta)im;
                    sem.setSpawnedType(EntityType.fromName(spawnEgg.getEntityId().split(":")[1]));
                }
                is.setItemMeta(im);

                /* Apply enchantments after item meta, apparently enchantments won't be applied otherwise */
                if(item.getEnchantments() != null){
                    item.getEnchantments().forEach(ench->{
                        is.addUnsafeEnchantment(
                                new EnchantmentWrapper(ench.getId()),
                                ench.getLevel()
                        );
                    });
                }
            }
            return is;
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            return new ItemStack(Material.AIR);
        }
    }

    private Object getNMSItemById(String id){
        return Reflect.invokeMethod(NMSGetItem, null, id);
    }

    private Object getNMSItemStack(INBTItem nbtItem){
        try {
            Object NMSItemStack = newItemStack.newInstance(
                    getNMSItemById(nbtItem.getId()),
                    nbtItem.getCount(),
                    nbtItem.getDamage(),
                    false);
            if(nbtItem instanceof NBTBasicItem) {
                Reflect.invokeMethod(NMSSetRepairCost, NMSItemStack, ((NBTBasicItem)nbtItem).getRepairCost());
            }
            /* TODO: apply meta */
            return NMSItemStack;
        } catch (Exception e){
            return null;
        }
    }

    private String deserializeJson(String raw){
        return new JSONObject(raw).getString("text");
    }

    @RequiredArgsConstructor
    enum ItemHideFlagsMap {
        HIDE_ENCHANTMENTS(ItemFlag.HIDE_ENCHANTS),
        HIDE_ATTRIBUTE_MODIFIERS(ItemFlag.HIDE_ATTRIBUTES),
        HIDE_UNBREAKABLE(ItemFlag.HIDE_UNBREAKABLE),
        HIDE_CAN_DESTROY(ItemFlag.HIDE_DESTROYS),
        HIDE_CAN_PLACE_ON(ItemFlag.HIDE_PLACED_ON),
        HIDE_POTION_EFFECTS(ItemFlag.HIDE_POTION_EFFECTS);

        private final ItemFlag bukkitFlag;
    }
}
