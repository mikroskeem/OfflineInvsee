package eu.mikroskeem.offlineinventory.utils.nbt.translator;


import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items.INBTItem;

public interface Translator {
    Object getTranslatedItem(INBTItem nbtItem);
}
