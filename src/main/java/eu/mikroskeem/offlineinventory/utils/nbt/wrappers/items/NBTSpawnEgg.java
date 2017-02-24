package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.StringTag;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import lombok.Getter;

@Getter
public class NBTSpawnEgg extends NBTBasicItem {
    private String entityId = null;

    public NBTSpawnEgg(CompoundTag nbt){
        super(nbt);
        entityId = NBTPath.getPath(nbt, "tag.EntityTag.id", StringTag.class).getValue();
    }
}
