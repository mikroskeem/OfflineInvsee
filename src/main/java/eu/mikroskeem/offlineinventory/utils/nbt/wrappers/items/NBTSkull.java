package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
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
        if(skullType == SkullType.PLAYER) {
            CompoundMap skullOwner = NBTPath.getPath(nbtTag, "tag.SkullOwner", CompoundTag.class).getValue();
            List<TextureData> textures = null;
            if (skullOwner.containsKey("Properties")) {
                CompoundMap properties = NBTPath.getPath(skullOwner, "Properties", CompoundTag.class).getValue();
                if (properties.containsKey("textures")) {
                    List<CompoundTag> texturesList = NBTPath.getPath(properties, "textures", ListTag.class).getValue();
                    textures = texturesList.stream().map(TextureData::parse).collect(Collectors.toList());
                }
            }
            this.skullOwner = new SkullOwner(
                    UUID.fromString(NBTPath.getPath(skullOwner, "Id", StringTag.class).getValue()),
                    NBTPath.getPath(skullOwner, "Name", StringTag.class).getValue(),
                    textures
            );
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
            return new TextureData(
                    NBTPath.getPath(nbt, "Signature", StringTag.class).getValue(),
                    NBTPath.getPath(nbt, "Value", StringTag.class).getValue()
            );
        }
    }
}