package eu.mikroskeem.offlineinventory.utils.nbt.wrappers.items;

import com.flowpowered.nbt.*;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTColor;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTEnchantment;
import eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTHideFlags;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static eu.mikroskeem.offlineinventory.utils.nbt.wrappers.misc.NBTColor.parseColor;

/**
 * Parse item based off from
 * http://minecraft.gamepedia.com/Player.dat_format#Item_structure
 */
@Getter
public class NBTBasicItem implements INBTItem {
    private final CompoundMap compoundMap;

    private final byte count;
    private final byte slot;
    private final short damage;
    private final String id;

    private String name = null;
    private List<String> lore = null;
    private List<NBTEnchantment> enchantments = null;
    private int repairCost = -1;
    private boolean unbreakable = false;
    private NBTColor color = null;
    private String locName = null;
    private List<NBTHideFlags> hideFlags = null;

    @SuppressWarnings("unchecked")
    public NBTBasicItem(CompoundTag itemTag){
        CompoundMap nbt = this.compoundMap = itemTag.clone().getValue();

        /* Set base stuff */
        this.count = (Byte)nbt.get("Count").getValue();
        this.slot = (Byte)nbt.get("Slot").getValue();
        this.damage = (Short)nbt.get("Damage").getValue();
        this.id = (String)nbt.get("id").getValue();

        /* Read optional tags */
        if(nbt.containsKey("tag")){
            assert nbt.get("tag").getType() == TagType.TAG_COMPOUND : "Property 'tag' is not type of TAG_COMPOUND!";
            CompoundMap tag = (CompoundMap) nbt.get("tag").getValue();

            /* Parse display tags */
            if(tag.containsKey("display")){
                assert tag.get("display").getType() == TagType.TAG_COMPOUND: "Property 'tag.display' is not type of TAG_COMPOUND!";
                CompoundMap display = (CompoundMap) nbt.get("display").getValue();

                if(display.containsKey("color")){
                    assert tag.get("color").getType() == TagType.TAG_INT : "Property 'tag.display.color' is not type of TAG_INT!";
                    color = parseColor((Integer)tag.get("color").getValue());
                }
                if(display.containsKey("LocName")){
                    assert tag.get("LocName").getType() == TagType.TAG_INT : "Property 'tag.display.LocName' is not type of TAG_STRING!";
                    locName = (String) tag.get("LocName").getValue();
                }
                if(display.containsKey("Name")) {
                    assert tag.get("Name").getType() == TagType.TAG_STRING : "Property 'tag.display.Name' is not type of TAG_STRING!";
                    name = (String) tag.get("Name").getValue();
                }
                if(display.containsKey("Lore")) {
                    assert tag.get("Lore").getType() == TagType.TAG_LIST : "Property 'tag.display.Lore' is not type of TAG_LIST!";
                    ListTag<StringTag> nbtLore = (ListTag<StringTag>) display.get("Lore");
                    lore = nbtLore.getValue().stream().map(StringTag::getValue).collect(Collectors.toList());
                }
            }
            /* Get hidden flags */
            hideFlags = NBTHideFlags.getAllFromItem(this);

            /* Get enchantments */
            enchantments = NBTEnchantment.getAllFromItem(this);
            if(enchantments != null){
                repairCost = (Integer)tag.get("RepairCost").getValue();
            }

            /* Parse misc tags */
            if(tag.containsKey("Unbreakable")){
                assert tag.get("Unbreakable").getType() == TagType.TAG_BYTE: "Property 'tag.Unbreakable' is not type of TAG_BYTE!";
                unbreakable = ((Byte)tag.get("Unbreakable").getValue()) == 1;
            }
        }
    }
}
