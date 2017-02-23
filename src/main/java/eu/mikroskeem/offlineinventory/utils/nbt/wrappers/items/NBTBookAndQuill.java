package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
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
        CompoundMap compoundMap = super.getCompoundMap();
        CompoundMap tag = (CompoundMap) compoundMap.get("tag").getValue();
        if(tag.containsKey("pages")){
            Tag pagesTag = tag.get("pages");
            assert pagesTag.getType() == TagType.TAG_LIST : "Property 'tag.pages' is not type of TAG_LIST!";
            List<StringTag> pages = ((ListTag<StringTag>) pagesTag).getValue();
            this.pages = pages.stream().map(StringTag::getValue).collect(Collectors.toList());
            System.out.println(this.pages);
        }
        /* Written book tags */
        if(tag.containsKey("author")){
            Tag authorTag = tag.get("author");
            assert authorTag.getType() == TagType.TAG_STRING : "Property 'tag.author' is not type of TAG_LIST!";
            author = (String)authorTag.getValue();
        }
        if(tag.containsKey("title")){
            Tag titleTag = tag.get("title");
            assert titleTag.getType() == TagType.TAG_STRING : "Property 'tag.title' is not type of TAG_LIST!";
            title = (String)titleTag.getValue();
        }
        if(tag.containsKey("generation")){
            Tag generationTag = tag.get("generation");
            assert generationTag.getType() == TagType.TAG_INT : "Property 'tag.generation' is not type of TAG_INT!";
            int gen = (Integer)generationTag.getValue();
            for(Generation value : Generation.values()){
                if(value.getNBTValue() == gen) {
                    generation = value;
                    break;
                }
            }
        }
        if(tag.containsKey("resolved")){
            Tag resolvedTag = tag.get("resolved");
            assert resolvedTag.getType() == TagType.TAG_BYTE : "Property 'tag.resolved' is not type of TAG_BYTE!";
            resolved = (Byte)resolvedTag.getValue();
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
