package eu.mikroskeem.offlineinventory.utils.nbt.bukkit;

import com.flowpowered.nbt.*;
import com.flowpowered.nbt.stream.NBTInputStream;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Parse Player data file
 * Documentation: http://wiki.vg/Map_Format#.5Bplayeruuid.5D.dat
 */
public class BukkitPlayerDataFile extends File {
    @Getter private final CompoundTag rootTag;

    public BukkitPlayerDataFile(World world, OfflinePlayer player) throws IOException {
        super(world.getWorldFolder(), String.format("playerdata/%s.dat", player.getUniqueId().toString()));
        if(!this.exists()){
            throw new FileNotFoundException(String.format("Could not open file %s", this.getAbsolutePath()));
        }
        if(player.isOnline()){
            player.getPlayer().getServer().getLogger()
                    .warning(String.format(
                            "Player %s is online, it isn't good idea to touch NBT right now!",
                            player.getName()));
        }
        try(NBTInputStream nis = new NBTInputStream(new FileInputStream(this), true)){
            Tag tag = nis.readTag().clone();
            assert tag.getType() == TagType.TAG_COMPOUND : "Root tag is not type of TAG_COMPOUND!";
            this.rootTag = (CompoundTag)tag;
        }
    }

    @SuppressWarnings("unchecked")
    public List<CompoundTag> getRawInventory(){
        CompoundMap map = rootTag.getValue();
        Tag inventoryTag = map.get("Inventory");
        assert inventoryTag.getType() == TagType.TAG_LIST : "Inventory tag is not type of TAG_LIST!";
        return ((ListTag<CompoundTag>) inventoryTag).getValue();
    }
}
