package com.spellbladenext.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.armor.Armor;

public class RunicArmor extends CustomArmor implements ConfigurableAttributes {
    public RunicArmor(Armor.CustomMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        builder.putAll(attributes);
        this.attributes = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (attributes == null) {
            return super.getAttributeModifiers(slot);
        }
        return slot == this.type.getEquipmentSlot() ? this.attributes : super.getAttributeModifiers(slot);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
        if(!level.isClient()) {
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
        }
        super.inventoryTick(itemStack, level, entity, i, bl);
    }
}
