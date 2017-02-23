package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.NBTBasicItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ToString
public class NBTEnchantment {
    @Getter private final short id;
    @Getter private final short level;

    public NBTEnchantment(CompoundTag nbtTag){
        CompoundMap nbt = nbtTag.getValue();
        this.id = (Short) nbt.get("id").getValue();
        this.level = (Short) nbt.get("lvl").getValue();
    }

    public static List<NBTEnchantment> getAllFromItem(NBTBasicItem item){
        CompoundMap nbt = item.getCompoundMap();
        if(nbt.containsKey("tag")){
            assert nbt.get("tag").getType() == TagType.TAG_COMPOUND : "Property 'tag' is not type of TAG_COMPOUND!";
            CompoundMap tag = (CompoundMap) nbt.get("tag").getValue();
            if(tag.containsKey("ench")){
                Tag ench = tag.get("ench");
                assert ench.getType() == TagType.TAG_LIST : "Property 'tag.ench' is not type of TAG_LIST!";
                return ((ListTag<CompoundTag>) ench).getValue().stream()
                        .map(NBTEnchantment::new).collect(Collectors.toList());
            }
        }
        return null;
    }
}
