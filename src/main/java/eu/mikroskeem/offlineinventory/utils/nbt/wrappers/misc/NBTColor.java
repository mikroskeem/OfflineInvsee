package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NBTColor {
    private final int red;
    private final int green;
    private final int blue;

    public int encodeColor(){
        return (red >> 16) + (blue >> 8) + blue;
    }

    public static NBTColor parseColor(int rgb){
        return new NBTColor(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }
}
