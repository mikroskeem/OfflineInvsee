package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class NBTSkull extends NBTBasicItem {
    private SkullType skullType = SkullType.PLAYER;
    private SkullOwner skullOwner = null;

    public NBTSkull(CompoundTag nbtTag){
        super(nbtTag);
        for(SkullType type : SkullType.values()){
            if(type.getNMSValue() == this.getDamage()){
                this.skullType = type;
                break;
            }
        }
        CompoundMap nbtMap = super.getCompoundMap();
        if(nbtMap.containsKey("tag") && skullType == SkullType.PLAYER) {
            Tag rawTag = nbtMap.get("tag");
            assert rawTag.getType() == TagType.TAG_COMPOUND : "Property 'tag' is not type of TAG_COMPOUND!";
            CompoundMap tag = (CompoundMap) rawTag.getValue();
            if(tag.containsKey("SkullOwner")){
                Tag skOwner = tag.get("SkullOwner");
                assert skOwner.getType() == TagType.TAG_COMPOUND : "Property 'tag.SkullOwner' is not type of TAG_COMPOUND!";
                CompoundMap skullOwner = (CompoundMap)skOwner.getValue();
                List<TextureData> textures = null;
                if(skullOwner.containsKey("Properties")){
                    Tag propertiesTag = skullOwner.get("Properties");
                    assert propertiesTag.getType() == TagType.TAG_COMPOUND : "Property 'tag.SkullOwner.Properties' is not type of TAG_COMPOUND!";
                    CompoundMap properties = (CompoundMap)propertiesTag.getValue();
                    if(properties.containsKey("textures")){
                        Tag texturesTag = properties.get("textures");
                        assert texturesTag.getType() == TagType.TAG_LIST : "Property 'tag.SkullOwner.Properties.textures' is not type of TAG_LIST!";
                        List<CompoundTag> texturesList = ((ListTag<CompoundTag>)texturesTag).getValue();
                        textures = texturesList.stream().map(TextureData::parse).collect(Collectors.toList());
                    }
                }
                this.skullOwner = new SkullOwner(
                        UUID.fromString((String)skullOwner.get("Id").getValue()),
                        (String)skullOwner.get("Name").getValue(),
                        textures
                );
            }
        }
    }

    @RequiredArgsConstructor
    public enum SkullType {
        SKELETON(0),
        WITHER_SKELETON(1),
        ZOMBIE(2),
        PLAYER(3),
        CREEPER(4),
        DRAGON(5);

        @Getter private final int NMSValue;
    }

    @RequiredArgsConstructor
    @Getter
    public static class SkullOwner {
        private final UUID id;
        private final String name;
        private final List<TextureData> textures;
    }

    @RequiredArgsConstructor
    @Getter
    public static class TextureData {
        private final String signature;
        private final String value;

        public static TextureData parse(CompoundTag nbt){
            CompoundMap map = nbt.getValue();
            return new TextureData(
                    (String)map.get("Signature").getValue(),
                    (String)map.get("Value").getValue()
            );
        }
    }
}