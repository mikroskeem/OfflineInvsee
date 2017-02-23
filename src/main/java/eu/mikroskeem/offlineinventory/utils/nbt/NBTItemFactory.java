package eu.mikroskeem.offlineinventory.utils.nbt;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.*;


public class NBTItemFactory {
    @SuppressWarnings("unchecked")
    public static INBTItem getItem(CompoundTag nbt){
        CompoundMap nbtMap = nbt.getValue();
        if(nbtMap.containsKey("tag")){
            Tag rawTag = nbtMap.get("tag");
            assert rawTag.getType() == TagType.TAG_COMPOUND : "Property 'tag' is not type of TAG_COMPOUND!";
            CompoundMap tag = (CompoundMap)rawTag.getValue();

            /* Book & Quill/Written book */
            if(tag.containsKey("pages")){
                return new NBTBookAndQuill(nbt);
            }
            if(tag.containsKey("BlockEntityTag")){
                Tag beT = tag.get("BlockEntityTag");
                assert beT.getType() == TagType.TAG_COMPOUND : "Property 'tag.BlockEntityTag' is not type of TAG_COMPOUND!";
                CompoundMap blockEntityTag = (CompoundMap)beT.getValue();
                if(blockEntityTag.containsKey("Items")){
                    /* Shulker */
                    return new NBTShulkerBox(nbt);
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
