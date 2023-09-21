package com.spellbladenext.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RunicAbsorption extends StatusEffect {
    public RunicAbsorption(StatusEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }
    public void applyUpdateEffect(LivingEntity livingEntity, int i) {


    }
    public boolean canApplyUpdateEffect(int i, int j) {
        return true;
    }
    public void onRemoved(LivingEntity p_19417_, AttributeContainer p_19418_, int p_19419_) {
        //p_19417_.setAbsorptionAmount(p_19417_.getAbsorptionAmount() - (float)(1 * (p_19419_)));
        if(p_19417_.getAbsorptionAmount() < p_19419_*4) {
            p_19417_.setAbsorptionAmount((float) ((p_19419_*4)));
        }
        super.onRemoved(p_19417_, p_19418_, p_19419_);
    }

    public void onApplied(LivingEntity p_19421_, AttributeContainer p_19422_, int p_19423_) {

        super.onApplied(p_19421_, p_19422_, p_19423_);
    }
}
