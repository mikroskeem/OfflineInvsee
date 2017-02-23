package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

public interface INBTItem {
    byte getCount();
    byte getSlot();
    short getDamage();
    String getId();
}
