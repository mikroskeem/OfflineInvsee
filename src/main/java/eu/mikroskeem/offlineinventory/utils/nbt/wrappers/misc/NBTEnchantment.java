package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
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
            CompoundMap tag = NBTPath.getPath(nbt, "tag", CompoundTag.class).getValue();
            if(tag.containsKey("ench")){
                ListTag<CompoundTag> enchantments = NBTPath.getPath(tag, "ench", ListTag.class);
                return enchantments.getValue().stream()
                        .map(NBTEnchantment::new).collect(Collectors.toList());
            }
        }
        return null;
    }
}
