package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import lombok.Getter;

@Getter
public class NBTPotion extends NBTBasicItem {
    private String potionEffect = null;

    public NBTPotion(CompoundTag nbtTag){
        super(nbtTag);
        potionEffect = NBTPath.getPath(nbtTag, "tag.Potion", StringTag.class).getValue();
    }
}
