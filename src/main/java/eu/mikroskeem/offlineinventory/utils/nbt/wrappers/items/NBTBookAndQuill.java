package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class NBTBookAndQuill extends NBTBasicItem {
    private byte resolved;

    private Generation generation = Generation.ORIGINAL;
    private String author = null;
    private String title = null;
    private List<String> pages = null;

    @SuppressWarnings("unchecked")
    public NBTBookAndQuill(CompoundTag nbtTag){
        super(nbtTag);
        CompoundMap nbt = super.getCompoundMap();
        CompoundMap tag = NBTPath.getPath(nbt, "tag", CompoundTag.class).getValue();
        if(tag.containsKey("pages")){
            List<StringTag> pages = NBTPath.getPath(nbt, "pages", ListTag.class).getValue();
            this.pages = pages.stream().map(StringTag::getValue).collect(Collectors.toList());
        }
        /* Written book tags */
        if(tag.containsKey("author")){
            author = NBTPath.getPath(tag, "author", StringTag.class).getValue();
        }
        if(tag.containsKey("title")){
            title = NBTPath.getPath(tag, "title", StringTag.class).getValue();
        }
        if(tag.containsKey("generation")){
            int gen = NBTPath.getPath(tag, "generation", IntTag.class).getValue();
            for(Generation value : Generation.values()){
                if(value.getNBTValue() == gen) {
                    generation = value;
                    break;
                }
            }
        }
        if(tag.containsKey("resolved")){
            resolved = NBTPath.getPath(tag, "resolved", ByteTag.class).getValue();
        }
    }

    public boolean isWrittenBook(){
        try {
            checkNotNull(author);
            checkNotNull(title);
            return true;
        } catch (NullPointerException e){
            return false;
        }
    }

    @RequiredArgsConstructor
    @Getter
    public enum Generation {
        ORIGINAL(0),
        COPY_OF_ORIGINAL(1),
        COPY_OF_COPY(2),
        TATTERED(3);

        private final int NBTValue;
    }
}
