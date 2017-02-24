package eu.mikroskeem.offlineinventory.utils.nbt.utils;

import com.flowpowered.nbt.*;

public class NBTPath {
    public static <T> T getPath(CompoundMap base, String path, Class<T> type){
        String[] paths = path.split("\\.");
        CompoundMap last = base;
        for(int i=0; i<=(paths.length-1); i++){
            String p = paths[i];
            Tag tag;
            if((paths.length-1) == i){
                tag = last.get(p);
                assert tag.getType().getClass() == type;
                return (T)tag;
            } else {
                tag = last.get(p);
                if(tag == null){
                    return null;
                }
                assert tag.getType() == TagType.TAG_COMPOUND;
                last = (CompoundMap)tag.getValue();
            }
        }
        return null;
    }

    public static <T> T getPath(CompoundTag base, String path, Class<T> type){
        return getPath(base.getValue(), path, type);
    }
}
