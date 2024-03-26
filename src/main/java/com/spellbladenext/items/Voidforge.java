package com.spellbladenext.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static com.extraspellattributes.ReabsorptionInit.*;

public class Voidforge extends Starforge implements ConfigurableAttributes {
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public Voidforge(ToolMaterial material, Settings settings, int damage, float speed, MagicSchool school) {
        super(material, settings,damage, speed, MagicSchool.ARCANE);
        this.school = school;
    }

    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        builder.putAll(attributes);
        builder.put(CONVERTTOFIRE,new EntityAttributeModifier(UUID.fromString("a1d444d8-5ca1-47bb-bfb2-709cf1e44476"),"tofire",Spellblades.config.voidforge, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(CONVERTTOFROST,new EntityAttributeModifier(UUID.fromString("a1d444d8-5ca1-47bb-bfb2-709cf1e44476"),"tofrost",Spellblades.config.voidforge, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        builder.put(CONVERTTOARCANE,new EntityAttributeModifier(UUID.fromString("a1d444d8-5ca1-47bb-bfb2-709cf1e44476"),"toarcane",Spellblades.config.voidforge, EntityAttributeModifier.Operation.MULTIPLY_BASE));

        this.attributes = builder.build();
    }



    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {

        if (this.attributes == null) {
            return super.getAttributeModifiers(slot);
        } else {
            return slot == EquipmentSlot.MAINHAND ? this.attributes : super.getAttributeModifiers(slot);
        }
    }
    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {

        super.inventoryTick(itemStack, level, entity, i, bl);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

            return super.postHit(stack,target,attacker);
    }

    MagicSchool school = MagicSchool.PHYSICAL_MELEE;

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }


    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner, (e) -> {
                e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World level, List<Text> list, TooltipContext tooltipFlag) {
       list.add(Text.translatable("Triggers Elemental Novas on hit, with a 1 second base cooldown and a 0.8 coefficient."));
        list.add(Text.translatable("Requires runes of the right type, or Spell Infinity."));
        list.add(Text.translatable("A weapon born of nothingness,").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        list.add(Text.translatable("can only create more nothingness.").formatted(Formatting.RED).formatted(Formatting.ITALIC));

        super.appendTooltip(itemStack,level,list,tooltipFlag);
    }
}
