package eu.mikroskeem.offlineinventory.utils.nbt;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.*;


public class NBTItemFactory {
    public static INBTItem getItem(CompoundTag nbt){
        System.out.println(nbt);
        CompoundMap nbtMap = nbt.getValue();
        if(nbtMap.containsKey("tag")){
            CompoundMap tag = NBTPath.getPath(nbt, "tag", CompoundTag.class).getValue();

            /* Potions/potion arrows */
            if(tag.containsKey("Potion")){
                return new NBTPotion(nbt);
            }

            /* Book & Quill/Written book */
            if(tag.containsKey("pages")){
                return new NBTBookAndQuill(nbt);
            }
            if(tag.containsKey("BlockEntityTag")){
                CompoundMap blockEntityTag = NBTPath.getPath(tag, "BlockEntityTag", CompoundTag.class).getValue();
                if(blockEntityTag.containsKey("Items")){
                    /* Shulker */
                    return new NBTShulkerBox(nbt);
                }
            }

            if(tag.containsKey("EntityTag")){
                CompoundMap entityTag = NBTPath.getPath(tag, "EntityTag", CompoundTag.class).getValue();
                if(entityTag.containsKey("id")){
                    return new NBTSpawnEgg(nbt);
                }
            }

            /* Skull */
            if(tag.containsKey("SkullOwner")){
                return new NBTSkull(nbt);
            }
        }

        return new NBTBasicItem(nbt);
    }
}
