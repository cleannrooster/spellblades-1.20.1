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
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class TheAvatar extends TrinketItem {
    public TheAvatar(Settings settings) {
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
    public Text getName(ItemStack stack) {
        int var01 = 0;
        int var02 = 0;
        int var03 = 0;
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;
        if(!Objects.equals(stack.getOrCreateNbt().getString("1"), "")) {
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            SpellSchool school = SpellSchools.ARCANE;
            double finalvar = 0;

            if (var1 >= var2 && var1 >= var3) {
                school = SpellSchools.ARCANE;
                finalvar = var1;
            }
            if (var2 >= var3 && var2 >= var1) {
                school = SpellSchools.FROST;
                finalvar = var2;
            }
            if (var3 >= var2 && var3 >= var1) {
                school = SpellSchools.FIRE;
                finalvar = var3;
            }
            String string = school.id.getPath();
            return Text.translatable("spellblades.avatarof").append(" ").append(StringUtils.capitalize(string));
        }
        return getName();

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if( stack.getOrCreateNbt().get("1") == null && stack.getOrCreateNbt().get("2") == null&& stack.getOrCreateNbt().get("3") == null){

            tooltip.add(Text.translatable("Unidentified."));
            tooltip.add(Text.translatable("The stats of the ").append(Text.translatable("item.spellbladenext.ashes")).append(" used to create this item determines its stats."));

        }
        int var01 = 0;
        int var02 = 0;
        int var03 = 0;
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;
        if(!Objects.equals(stack.getOrCreateNbt().getString("1"), "")) {
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            SpellSchool school = SpellSchools.ARCANE;
            double finalvar = 0;

            if (var1 >= var2 && var1 >= var3) {
                school = SpellSchools.ARCANE;
                finalvar = var1;
            }
            if (var2 >= var3 && var2 >= var1) {
                school = SpellSchools.FROST;
                finalvar = var2;
            }
            if (var3 >= var2 && var3 >= var1) {
                school = SpellSchools.FIRE;
                finalvar = var3;
            }

            if (school == SpellSchools.ARCANE) {
                tooltip.add(Text.translatable("Night's promises are dark, for even the stars must be harvested.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
            }
            if (school == SpellSchools.FROST) {
                tooltip.add(Text.translatable("Icy fangs seek tender hulls to sink themselves into.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
            }
            if (school == SpellSchools.FIRE) {
                tooltip.add(Text.translatable("In my dreams I see a great warrior, his skin scorched black, his fists aflame.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {

        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        int var01 = 0;
        int var02 = 0;
        int var03 = 0;
        double var1 = 0;
        double var2 = 0;
        double var3 = 0;
        if (!Objects.equals(stack.getOrCreateNbt().getString("1"), "")) {
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("1"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("1modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("2"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("2modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "arcane")) {
                var01 = var01 + 1;
                var1 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "frost")) {
                var02 = var02 + 1;
                var2 += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            if (Objects.equals(stack.getOrCreateNbt().getString("3"), "fire")) {
                var03 = var03+ 1;
                var3  += (stack.getOrCreateNbt().getInt("3modifier") * 0.01);

            }
            SpellSchool school = SpellSchools.ARCANE;
            double finalvar = 0;

            if (var1 >= var2 && var1 >= var3) {
                school = SpellSchools.ARCANE;
                finalvar = var1;
            }
            if (var2 >= var3 && var2 >= var1) {
                school = SpellSchools.FROST;
                finalvar = var2;
            }
            if (var3 >= var2 && var3 >= var1) {
                school = SpellSchools.FIRE;
                finalvar = var3;
            }

            if (SpellSchools.getSchool(school.id.toString()) != null) {
                EntityAttribute attribute = SpellSchools.getSchool(school.id.toString()).attribute;

                modifiers.put(attribute, new EntityAttributeModifier(uuid, "spellblades:avatar", (finalvar) / 3F, EntityAttributeModifier.Operation.MULTIPLY_BASE));
            }
        }

            // If the player has access to ring slots, this will give them an extra one
            return modifiers;
        }
    }

