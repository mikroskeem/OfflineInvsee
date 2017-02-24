package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTColor;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTEnchantment;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTHideFlags;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTColor.parseColor;

/**
 * Parse item based off from
 * http://minecraft.gamepedia.com/Player.dat_format#Item_structure
 */
@Getter
public class NBTBasicItem implements INBTItem {
    private final CompoundMap compoundMap;

    private final byte count;
    private final byte slot;
    private final short damage;
    private final String id;

    private String name = null;
    private List<String> lore = null;
    private List<NBTEnchantment> enchantments = null;
    private int repairCost = -1;
    private boolean unbreakable = false;
    private NBTColor color = null;
    private String locName = null;
    private List<NBTHideFlags> hideFlags = null;

    @SuppressWarnings("unchecked")
    public NBTBasicItem(CompoundTag itemTag){
        CompoundMap nbt = this.compoundMap = itemTag.clone().getValue();

        /* Set base stuff */
        this.count = NBTPath.getPath(itemTag, "Count", ByteTag.class).getValue();
        this.slot = NBTPath.getPath(itemTag, "Slot", ByteTag.class).getValue();
        this.damage = NBTPath.getPath(itemTag, "Damage", ShortTag.class).getValue();
        this.id = NBTPath.getPath(itemTag, "id", StringTag.class).getValue();

        /* Read optional tags */
        if(nbt.containsKey("tag")){
            CompoundMap tag = NBTPath.getPath(itemTag, "tag", CompoundTag.class).getValue();
            /* Parse display tags */
            if(tag.containsKey("display")){
                CompoundMap display = NBTPath.getPath(tag, "display", CompoundTag.class).getValue();
                if(display.containsKey("color")){
                    color = parseColor(NBTPath.getPath(display, "color", IntTag.class).getValue());
                }
                if(display.containsKey("LocName")){
                    locName = NBTPath.getPath(display, "LocName", StringTag.class).getValue();
                }
                if(display.containsKey("Name")) {
                    name = NBTPath.getPath(display, "Name", StringTag.class).getValue();
                }
                if(display.containsKey("Lore")) {
                    ListTag<StringTag> nbtLore = NBTPath.getPath(display, "Lore", ListTag.class);
                    lore = nbtLore.getValue().stream().map(StringTag::getValue).collect(Collectors.toList());
                }
            }
            /* Get hidden flags */
            hideFlags = NBTHideFlags.getAllFromItem(this);

            /* Get enchantments */
            enchantments = NBTEnchantment.getAllFromItem(this);
            if(enchantments != null){
                repairCost = NBTPath.getPath(tag, "RepairCost", IntTag.class).getValue();
            }

            /* Parse misc tags */
            if(tag.containsKey("Unbreakable")){
                unbreakable = (NBTPath.getPath(tag, "Unbreakable", ByteTag.class).getValue()) == 1;
            }
        }
    }
}