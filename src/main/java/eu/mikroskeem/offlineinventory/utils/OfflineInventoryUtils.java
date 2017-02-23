package eu.mikroskeem.offlineinventory.utils;

import com.flowpowered.nbt.CompoundTag;
import eu.mikroskeem.offlineinventory.utils.nbt.NBTItemFactory;
import eu.mikroskeem.offlineinventory.utils.nbt.bukkit.BukkitPlayerDataFile;
import eu.mikroskeem.offlineinventory.utils.nbt.translator.CBTranslator;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.INBTItem;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OfflineInventoryUtils {
    private static CBTranslator cbTranslator;
    public static List<ItemStack> getOfflineInventoryContents(OfflinePlayer player, World world)
            throws IOException, AssertionError {
        BukkitPlayerDataFile playerDataFile = new BukkitPlayerDataFile(world, player);
        List<CompoundTag> nbtInventory = playerDataFile.getRawInventory();
        if(cbTranslator == null){
            cbTranslator = new CBTranslator();
        }
        return nbtInventory.stream().map(itemNbt -> {
            INBTItem nbtItem = NBTItemFactory.getItem(itemNbt);
            return cbTranslator.getBukkitItemStack(nbtItem);
        }).collect(Collectors.toList());
    }
}
