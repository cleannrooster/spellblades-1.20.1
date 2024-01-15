package com.spellbladenext.items;

import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import com.spellbladenext.config.ServerConfig;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class Ashes  extends TrinketItem {
    public Ashes(Settings settings) {
        super(settings);

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if( stack.getOrCreateNbt().get("1") == null && stack.getOrCreateNbt().get("2") == null&& stack.getOrCreateNbt().get("3") == null){
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> stack.getOrCreateNbt().putString("1", "fire");
                case 1 -> stack.getOrCreateNbt().putString("1", "frost");
                case 2 -> stack.getOrCreateNbt().putString("1", "arcane");

            }
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> stack.getOrCreateNbt().putString("2", "fire");
                case 1 -> stack.getOrCreateNbt().putString("2", "frost");
                case 2 -> stack.getOrCreateNbt().putString("2", "arcane");

            }
            switch (entity.getWorld().getRandom().nextInt(3)){
                case 0 -> stack.getOrCreateNbt().putString("3", "fire");
                case 1 -> stack.getOrCreateNbt().putString("3", "frost");
                case 2 -> stack.getOrCreateNbt().putString("3", "arcane");

            }

            stack.getOrCreateNbt().putInt("1modifier", entity.getWorld().getRandom().nextBetween(1, Spellblades.config.ashes_upper_bound));
            stack.getOrCreateNbt().putInt("2modifier", entity.getWorld().getRandom().nextBetween(1,Spellblades.config.ashes_upper_bound));
            stack.getOrCreateNbt().putInt("3modifier", entity.getWorld().getRandom().nextBetween(1,Spellblades.config.ashes_upper_bound));

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(stack.getOrCreateNbt().get("fire") == null && stack.getOrCreateNbt().get("arcane") == null && stack.getOrCreateNbt().get("frost") == null) {

            tooltip.add(Text.translatable("Unidentified."));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "fire")) {
            modifiers.put(Spellblades.CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromfire1", stack.getOrCreateNbt().getInt("1modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "frost")) {
            modifiers.put(Spellblades.CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromfrost1", stack.getOrCreateNbt().getInt("1modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "arcane")) {
            modifiers.put(Spellblades.CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromarcane1", stack.getOrCreateNbt().getInt("1modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "fire")) {
            modifiers.put(Spellblades.CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromfire2", stack.getOrCreateNbt().getInt("2modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "frost")) {
            modifiers.put(Spellblades.CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromfrost2", stack.getOrCreateNbt().getInt("2modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "arcane")) {
            modifiers.put(Spellblades.CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromarcane2", stack.getOrCreateNbt().getInt("2modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "fire")) {
            modifiers.put(Spellblades.CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromfire3", stack.getOrCreateNbt().getInt("3modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "frost")) {
            modifiers.put(Spellblades.CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromfrost3", stack.getOrCreateNbt().getInt("3modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "arcane")) {
            modifiers.put(Spellblades.CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromarcane3", stack.getOrCreateNbt().getInt("3modifier")*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
