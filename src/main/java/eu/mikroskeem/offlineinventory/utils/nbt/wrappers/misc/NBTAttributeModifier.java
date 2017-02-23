package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import com.flowpowered.nbt.CompoundMap;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.NBTBasicItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class NBTAttributeModifier {
    private final String attributeName;
    private final String modifierName;
    private final NBTSlot slot;
    private final int operation;
    private double amount;
    private long UUIDMost;
    private long UUIDLeast;

    public static List<NBTAttributeModifier> getAllFromItem(NBTBasicItem item){
        CompoundMap nbt = item.getCompoundMap();
        if(nbt.containsKey("tag")){

        }
        return Collections.emptyList();
    }
}
