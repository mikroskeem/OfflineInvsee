package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.TagType;
import eu.mikroskeem.offlineinventory.utils.nbt.NBTItemFactory;
import lombok.Getter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NBTShulkerBox extends NBTBasicItem implements INBTContainer {
    private List<INBTItem> contents = null;

    @SuppressWarnings("unchecked")
    public NBTShulkerBox(CompoundTag nbtTag){
        super(nbtTag);
        CompoundMap nbt = super.getCompoundMap();
        /* TODO: read other tags as well */
        if(nbt.containsKey("tag")) {
            assert nbt.get("tag").getType() == TagType.TAG_COMPOUND : "Property 'tag' is not type of TAG_COMPOUND!";
            CompoundMap tag = (CompoundMap) nbt.get("tag").getValue();
            if (tag.containsKey("BlockEntityTag")) {
                assert tag.get("BlockEntityTag").getType() == TagType.TAG_COMPOUND : "Property 'tag.BlockEntityTag' is not type of TAG_COMPOUND!";
                CompoundMap blockEntityTag = (CompoundMap) tag.get("BlockEntityTag").getValue();
                if (blockEntityTag.containsKey("Items")) {
                    assert blockEntityTag.get("Items").getType() == TagType.TAG_LIST : "Property 'tag.BlockEntityTag.Items' is not type of TAG_LIST";
                    ListTag<CompoundTag> nbtItems = (ListTag<CompoundTag>) blockEntityTag.get("Items");
                    this.contents = nbtItems.getValue().stream().map(NBTItemFactory::getItem).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    public Iterator<INBTItem> iterator() {
        if(contents != null) {
            return contents.iterator();
        } else {
            return Collections.<INBTItem>emptyList().iterator();
        }
    }
}
