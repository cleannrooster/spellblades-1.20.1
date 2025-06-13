package com.spellbladenext.effect;

import com.extraspellattributes.api.SpellStatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.TypeFilter;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.utils.TargetHelper;

import java.util.List;

public class Challenged extends SpellStatusEffect {
    public Challenged(StatusEffectCategory category, int color, Spell spell) {
        super(category, color, spell);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        List<LivingEntity> list2 = entity.getWorld().getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),entity.getBoundingBox().expand(16),entity2 -> TargetHelper.getRelation(entity,entity2).equals(TargetHelper.Relation.HOSTILE));
        list2.remove(entity);

        for(LivingEntity living : list2) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 2));
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 80, 2));
        }
        List<LivingEntity> list = entity.getWorld().getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class),entity.getBoundingBox().expand(16),entity2 -> TargetHelper.getRelation(entity2,entity2).equals(TargetHelper.Relation.SEMI_FRIENDLY) || TargetHelper.getRelation(entity2,entity2).equals(TargetHelper.Relation.FRIENDLY) );
        list.remove(entity);
        for(LivingEntity living: list) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 1));
        }

        super.onRemoved(entity, attributes, amplifier);
    }



    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.getLastAttacker() != null){
            if(entity instanceof HostileEntity hostile) {
                hostile.setTarget(hostile.getAttacker());
                if (entity.getBrain() != null) {
                    if (entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET)) {
                        entity.getBrain().remember(MemoryModuleType.ATTACK_TARGET, hostile.getAttacker());
                    }
                    if (entity.getBrain().hasMemoryModule(MemoryModuleType.ANGRY_AT)) {
                        entity.getBrain().remember(MemoryModuleType.ANGRY_AT, hostile.getAttacker().getUuid());
                    }
                }
            }
        }
    }
}
