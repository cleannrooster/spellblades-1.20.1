package com.spellbladenext.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.armor.Armor;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.mixin.ItemStackMixin;

import java.util.EnumMap;
import java.util.UUID;

import static com.extraspellattributes.ReabsorptionInit.WARDING;

public class RunicArmor extends CustomArmor {
    public RunicArmor(Armor.CustomMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }
    private static final EnumMap<Type, UUID> MODIFIERS = (EnumMap) Util.make(new EnumMap(ArmorItem.Type.class), (uuidMap) -> {
        uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;


    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (attributes == null) {
            return super.getAttributeModifiers(slot);
        }
        return slot == this.type.getEquipmentSlot() ? this.attributes : super.getAttributeModifiers(slot);
    }

    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        builder.putAll(attributes);
        UUID uuid = (UUID)MODIFIERS.get(this.type);
        builder.put(WARDING,new EntityAttributeModifier(uuid,"warding",4, EntityAttributeModifier.Operation.ADDITION));

        this.attributes = builder.build();

    }

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        if(user instanceof PlayerDamageInterface damageInterface){
            damageInterface.resetDamageAbsorbed();
        }
        return super.equipAndSwap(item, world, user, hand);
    }


    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
       /* if(!level.isClient()) {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;

                int amount = 0;
                if(player.getInventory().getArmorStack(0).getItem() instanceof RunicArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(1).getItem() instanceof RunicArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(2).getItem() instanceof RunicArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(3).getItem() instanceof RunicArmor){
                    amount++;
                }
                int ii = 0;
                if(player.hasStatusEffect(Spellblades.RunicAbsorption)){
                    ii = player.getStatusEffect(Spellblades.RunicAbsorption).getAmplifier();
                }
                if(amount > 0 && !player.hasStatusEffect(Spellblades.RunicAbsorption) && player.getAbsorptionAmount() <= 0)
                    player.addStatusEffect(new StatusEffectInstance(Spellblades.RunicAbsorption,20*5,amount, false, false));

            }
        }*/
        super.inventoryTick(itemStack, level, entity, i, bl);
    }
}
