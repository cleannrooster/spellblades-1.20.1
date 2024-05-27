package com.spellbladenext.items.armor;

import com.spellbladenext.Spellblades;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class Armors {
    private static final Supplier<Ingredient> WOOL_INGREDIENTS = () -> { return Ingredient.ofItems(
            Items.WHITE_WOOL,
            Items.ORANGE_WOOL,
            Items.MAGENTA_WOOL,
            Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL,
            Items.LIME_WOOL,
            Items.PINK_WOOL,
            Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL,
            Items.CYAN_WOOL,
            Items.PURPLE_WOOL,
            Items.BLUE_WOOL,
            Items.BROWN_WOOL,
            Items.GREEN_WOOL,
            Items.RED_WOOL,
            Items.BLACK_WOOL);
    };

    public static final ArrayList<Armor.Entry> entries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> runeentries = new ArrayList<>();
    public static final ArrayList<Armor.Entry> magisterentries = new ArrayList<>();

    private static Armor.Entry create(Armor.CustomMaterial material, ItemConfig.ArmorSet defaults) {
        return new Armor.Entry(material, null, defaults);
    }



    private static final float specializedRobeSpellPower = 0.25F;
    private static final float specializedRobeCritDamage = 0.1F;
    private static final float specializedRobeCritChance = 0.02F;
    private static final float specializedRobeHaste = 0.03F;

    public static final Armor.Set runegleaming =
            create(
                    new Armor.CustomMaterial(
                            "runegleaming",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEGLEAM)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE.id), 1F)
                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE.id), 1F)
                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE.id), 1F)
                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE.id), 1F)
                                    ))
                    ))   .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new RunicArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
                    ))
                    .put(entries).armorSet();;

    public static final Armor.Set runeblazing =
            create(
                    new Armor.CustomMaterial(
                            "runeblazing",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEBLAZE)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE.id), 1F)
                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE.id), 1F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE.id), 1F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 1F)

                                    ))
                    ))   .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new RunicArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
                    ))
                    .put(entries)
                    .armorSet();;

    public static final Armor.Set runefrosted =
            create(
                    new Armor.CustomMaterial(
                            "runefrosted",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEFROST)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 1F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 1F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST.id), 1F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(1)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST.id), 1F)


                                    ))
                    ))
                    .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new RunicArmor(material, ArmorItem.Type.HELMET, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.CHESTPLATE, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings()),
                            new RunicArmor(material, ArmorItem.Type.BOOTS, new Item.Settings())
                    ))
                    .put(entries)
                    .armorSet();;
    public static final Armor.Set aetherfire =
            create(
                    new Armor.CustomMaterial(
                            "gleaming",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEGLEAM)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.4F),
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.HASTE.id, 0.12F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)


                                    ))
                    ))   .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new MagisterArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.ARCANE)),
                            new MagisterArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.ARCANE)),
                            new MagisterArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.ARCANE)),
                            new MagisterArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.ARCANE))
                    ))
                    .put(entries)
                   .armorSet();
    public static final Armor.Set rimeblaze =
            create(
                    new Armor.CustomMaterial(
                            "blazing",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEBLAZE)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_CHANCE.id, 0.08F),
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.HASTE.id, 0.12F)


                                    )),                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE.id), 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F)


                                    ))
                    ))   .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new MagisterArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.FIRE)),
                            new MagisterArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.FIRE)),
                            new MagisterArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.FIRE)),
                            new MagisterArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.FIRE))
                    ))
                    .put(entries)
                    .armorSet();;;
    public static final Armor.Set deathchill =
            create(
                    new Armor.CustomMaterial(
                            "frozen",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEFROST)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_CHANCE.id, 0.08F),
                                            ItemConfig.Attribute.multiply(SpellPowerMechanics.CRITICAL_DAMAGE.id, 0.4F)

                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST.id), 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST.id), 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 2F)


                                    ))
                    )) .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new MagisterArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.FROST)),
                            new MagisterArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.FROST)),
                            new MagisterArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.FROST)),
                            new MagisterArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.FROST))
                    ))
                    .put(entries)
                    .armorSet();;
    public static final Armor.Set ashherald =
            create(
                    new Armor.CustomMaterial(
                            "ashherald",
                            35,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEBLAZENUGGET)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    )),
                            new ItemConfig.ArmorSet.Piece(8)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    ))
                    )) .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new HeraldArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.FIRE)),
                            new HeraldArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.FIRE)),
                            new HeraldArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.FIRE)),
                            new HeraldArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.FIRE))
                    ))
                    .put(entries)
                    .armorSet();;
    public static final Armor.Set arcaneherald =
            create(
                    new Armor.CustomMaterial(
                            "arcaneherald",
                            35,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEGLEAMNUGGET)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    )),
                            new ItemConfig.ArmorSet.Piece(8)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    ))
                    )) .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new HeraldArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.ARCANE)),
                            new HeraldArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.ARCANE)),
                            new HeraldArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.ARCANE)),
                            new HeraldArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.ARCANE))
                    ))
                    .put(entries)
                    .armorSet();;
    public static final Armor.Set frosherald =
            create(
                    new Armor.CustomMaterial(
                            "frostherald",
                            35,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
                            () -> Ingredient.ofItems(Spellblades.RUNEFROSTNUGGET)
                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    )),
                            new ItemConfig.ArmorSet.Piece(8)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(


                                    )),
                            new ItemConfig.ArmorSet.Piece(3)
                                    .addAll(List.of(

                                    ))
                    )) .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new HeraldArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),List.of(SpellSchools.FROST)),
                            new HeraldArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), List.of(SpellSchools.FROST)),
                            new HeraldArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),List.of(SpellSchools.FROST)),
                            new HeraldArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), List.of(SpellSchools.FROST))
                    ))
                    .put(entries)
                    .armorSet();;

    public static final Armor.Set magus =
            create(
                    new Armor.CustomMaterial(
                            "magus",
                            20,
                            10,
                            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
                            () -> { return Ingredient.ofItems(
                                    Spellblades.THREAD,Spellblades.RUNEBLAZENUGGET,Spellblades.RUNEFROSTNUGGET,Spellblades.RUNEGLEAMNUGGET);
                            }

                    ),
                    ItemConfig.ArmorSet.with(
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)


                                    )),
                            new ItemConfig.ArmorSet.Piece(6)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)



                                    )),
                            new ItemConfig.ArmorSet.Piece(4)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)



                                    )),
                            new ItemConfig.ArmorSet.Piece(2)
                                    .addAll(List.of(
                                            ItemConfig.Attribute.bonus((SpellSchools.FROST).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.FIRE).id, 2F),
                                            ItemConfig.Attribute.bonus((SpellSchools.ARCANE).id, 2F)



                                    ))
                    )) .bundle(material -> new Armor.Set(Spellblades.MOD_ID,
                            new MagusArmor(material, ArmorItem.Type.HELMET, new Item.Settings(),SpellSchools.ARCANE),
                            new MagusArmor(material, ArmorItem.Type.CHESTPLATE,new Item.Settings(), SpellSchools.ARCANE),
                            new MagusArmor(material, ArmorItem.Type.LEGGINGS, new Item.Settings(),SpellSchools.ARCANE),
                            new MagusArmor(material, ArmorItem.Type.BOOTS,new Item.Settings(), SpellSchools.ARCANE)
                    ))
                    .put(entries)
                    .armorSet();;


    public static void register(Map<String, ItemConfig.ArmorSet> configs) {
        Armor.register(configs, entries,Spellblades.KEY);
       /* for (Armor.Entry entry : entries){
            for(Object pieces : entry.armorSet().pieces()) {
                if(pieces instanceof List list){
                    for(Object item : list){
                        if(item instanceof ArmorItem item1){
                            if(item1 instanceof MagisterArmor armor){
                                UUID uuid = (UUID) MagisterArmor.MODIFIERS.get(armor.getType());
                                System.out.println("Registering magister");
                                armor.getAttributeModifiers(armor.getSlotType())
                                        .put(Spellblades.WARDING,new EntityAttributeModifier(uuid,"warding",3, EntityAttributeModifier.Operation.ADDITION));
                            }
                            if(item1 instanceof RunicArmor armor){
                                UUID uuid = (UUID) MagisterArmor.MODIFIERS.get(armor.getType());
                                System.out.println("Registering runic");

                                armor.getAttributeModifiers(armor.getSlotType())
                                        .put(Spellblades.WARDING,new EntityAttributeModifier(uuid,"warding",3, EntityAttributeModifier.Operation.ADDITION));
                            }
                            if(item1 instanceof MagusArmor armor){
                                UUID uuid = (UUID) MagisterArmor.MODIFIERS.get(armor.getType());
                                System.out.println("Registering Magus");

                                armor.getAttributeModifiers(armor.getSlotType())
                                        .put(Spellblades.WARDING,new EntityAttributeModifier(uuid,"warding",3, EntityAttributeModifier.Operation.ADDITION));
                            }
                        }
                    }
                }
            }
        }*/

    }
}