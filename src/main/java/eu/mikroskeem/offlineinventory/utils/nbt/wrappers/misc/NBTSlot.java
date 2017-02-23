package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NBTSlot {
    MAIN_HAND("mainhand"),
    OFF_HAND("offhand"),
    BOOTS("feet"),
    LEGGINS("legs"),
    CHESTPLATE("chest"),
    HELMET("head");

    @Getter private final String NBTSlotName;
}
