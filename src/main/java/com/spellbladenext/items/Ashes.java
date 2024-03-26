package com.spellbladenext.items;

import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static com.extraspellattributes.ReabsorptionInit.*;

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

            stack.getOrCreateNbt().putInt("1modifier", entity.getWorld().getRandom().nextBetween(Spellblades.config.ashes_lower_bound, Spellblades.config.ashes_upper_bound));
            stack.getOrCreateNbt().putInt("2modifier", entity.getWorld().getRandom().nextBetween(Spellblades.config.ashes_lower_bound,Spellblades.config.ashes_upper_bound));
            stack.getOrCreateNbt().putInt("3modifier", entity.getWorld().getRandom().nextBetween(Spellblades.config.ashes_lower_bound,Spellblades.config.ashes_upper_bound));

        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if( stack.getOrCreateNbt().get("1") == null && stack.getOrCreateNbt().get("2") == null&& stack.getOrCreateNbt().get("3") == null){

            tooltip.add(Text.translatable("Unidentified."));
        }
        tooltip.add(Text.translatable("A single facet contains the wisdom").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("of countless disintegrated worlds.").formatted(Formatting.RED).formatted(Formatting.ITALIC));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {

        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "fire")) {
            modifiers.put(CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromfire1", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("1modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "frost")) {
            modifiers.put(CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromfrost1", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("1modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("1") != null && stack.getOrCreateNbt().getString("1").equals( "arcane")) {
            modifiers.put(CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("1e10b1f5-66ed-4e64-b222-c1106273d080"), "spellblades:convertfromarcane1", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("1modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "fire")) {
            modifiers.put(CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromfire2", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("2modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "frost")) {
            modifiers.put(CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromfrost2", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("2modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("2") != null && stack.getOrCreateNbt().getString("2").equals( "arcane")) {
            modifiers.put(CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("c73054bc-46f0-45de-b042-7ddf9ecef1bc"), "spellblades:convertfromarcane2", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("2modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "fire")) {
            modifiers.put(CONVERTFROMFIRE, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromfire3", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("3modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "frost")) {
            modifiers.put(CONVERTFROMFROST, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromfrost3", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("3modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        if(stack.getOrCreateNbt().getString("3") != null && stack.getOrCreateNbt().getString("3").equals( "arcane")) {
            modifiers.put(CONVERTFROMARCANE, new EntityAttributeModifier(UUID.fromString("c05273f4-9302-4545-8880-307d331ffaf0"), "spellblades:convertfromarcane3", Math.max(Spellblades.config.ashes_lower_bound,Math.min(Spellblades.config.ashes_upper_bound,stack.getOrCreateNbt().getInt("3modifier")))*0.01, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        }
        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
