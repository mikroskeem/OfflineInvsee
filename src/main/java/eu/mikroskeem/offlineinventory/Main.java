package eu.mikroskeem.offlineinventory;

import eu.mikroskeem.offlineinventory.utils.OfflineInventoryUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Enabling");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equals("offlineinvsee")) return false;
        if(args.length == 1){
            List<ItemStack> items;
            try {
                items = OfflineInventoryUtils.getOfflineInventoryContents(
                        getServer().getOfflinePlayer(args[0]),
                        getServer().getWorld("world")
                );
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
            if(sender instanceof Player) {
                Player player = (Player) sender;
                Inventory inventory = getServer().createInventory(null, 54, "Temp inv");
                inventory.setContents(items.toArray(new ItemStack[0]));
                player.openInventory(inventory);
            } else {
                sender.sendMessage(items.toString());
            }
        }
        return true;
    }
}
