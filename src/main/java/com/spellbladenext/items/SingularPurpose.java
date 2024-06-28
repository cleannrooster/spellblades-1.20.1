package com.spellbladenext.items;

import com.extraspellattributes.ReabsorptionInit;
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
import static com.spellbladenext.Spellblades.PURPOSE;

public class SingularPurpose extends TrinketItem {
    public SingularPurpose(Settings settings) {
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
            tooltip.add(Text.translatable("The stats of the ").append(Text.translatable("item.spellbladenext.omni")).append(" used to create this item determines its stats."));

        }
        tooltip.add(Text.translatable("Champion that which you love.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("He who fights for nothing - dies for nothing.").formatted(Formatting.RED).formatted(Formatting.ITALIC));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {

        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        double var1 =  (stack.getOrCreateNbt().getInt("1modifier")*0.01);
        double var2 =  (stack.getOrCreateNbt().getInt("2modifier")*0.01);
        double var3 =  (stack.getOrCreateNbt().getInt("3modifier")*0.01);
        modifiers.put(PURPOSE, new EntityAttributeModifier(uuid, "spellblades:purpose", (var1+var2+var3)/3F, EntityAttributeModifier.Operation.MULTIPLY_BASE));

        // If the player has access to ring slots, this will give them an extra one
        return modifiers;
    }

}
