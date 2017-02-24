package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.IntTag;
import eu.mikroskeem.offlineinventory.utils.nbt.utils.NBTPath;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.NBTBasicItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum NBTHideFlags {
    HIDE_ENCHANTMENTS(1),
    HIDE_ATTRIBUTE_MODIFIERS(2),
    HIDE_UNBREAKABLE(4),
    HIDE_CAN_DESTROY(8),
    HIDE_CAN_PLACE_ON(16),
    HIDE_POTION_EFFECTS(32);


    private final int value;

    public static List<NBTHideFlags> getAllFromItem(NBTBasicItem item){
        CompoundMap nbt = item.getCompoundMap();
        IntTag hideFlagsTag = NBTPath.getPath(nbt, "tag.display.HideFlags", IntTag.class);
        if(hideFlagsTag == null){
            return null;
        }
        int hideFlags = hideFlagsTag.getValue();
        List<NBTHideFlags> parsedHideFlags = new ArrayList<>();
        NBTHideFlags[] flags = NBTHideFlags.values();
        for(NBTHideFlags flag : flags){
            if(hideFlags == 0)
                continue;
            if(flag.getValue() > hideFlags){
                System.out.println(String.format(
                        "WARNING: %s(%s) > %s!", flag.toString(), flag.getValue(), hideFlags
                ));
                continue;
            }
            hideFlags = hideFlags - flag.getValue();
            parsedHideFlags.add(flag);
        }
        return parsedHideFlags;
    }
}
