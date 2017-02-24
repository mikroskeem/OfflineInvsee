package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import eu.mikroskeem.offlineinventory.utils.nbt.NBTItemFactory;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
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
        /* TODO: read other tags as well */
        ListTag<CompoundTag> nbtItems = NBTPath.getPath(nbtTag, "tag.BlockEntityTag.Items", ListTag.class);
        this.contents = nbtItems.getValue().stream().map(NBTItemFactory::getItem).collect(Collectors.toList());
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
