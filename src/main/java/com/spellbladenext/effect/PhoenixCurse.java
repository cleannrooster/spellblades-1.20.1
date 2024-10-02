package com.spellbladenext.effect;

import com.extraspellattributes.api.SpellStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_power.api.SpellDamageSource;

public class PhoenixCurse extends SpellStatusEffect {
    public PhoenixCurse(StatusEffectCategory category, int color, Spell spell) {
        super(category, color, spell);
    }

    @Override
    public boolean canApplySpellEffect(int duration, int amplifier){
        if(duration % 10 == 1){
            return true;
        }
        return super.canApplyUpdateEffect(duration, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applySpellEffect(LivingEntity entity,LivingEntity owner, int amplifier, float spellPower, Spell spell) {

        if(owner != null && owner instanceof PlayerEntity player  &&  spell != null && spellPower > 0){
            entity.hurtTime = 0;
            entity.timeUntilRegen = 0;
            entity.damage(SpellDamageSource.player(spell.school,player),spellPower*spell.impact[0].action.damage.spell_power_coefficient);
            entity.hurtTime = 0;
            entity.timeUntilRegen = 0;
        }
    }
}
