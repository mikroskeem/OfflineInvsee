package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import java.util.List;

public interface INBTContainer extends Iterable<INBTItem> {
    List<INBTItem> getContents();
}
