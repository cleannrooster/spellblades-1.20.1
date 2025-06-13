package com.spellbladenext.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;

import static com.spellbladenext.Spellblades.MOD_ID;

public class Inexorable extends CustomEffect{
    public Inexorable(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }


    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        ParticleHelper.play(entity.getWorld(),entity, SpellRegistry.getSpell(Identifier.of(MOD_ID,"particlesholy")).release.target.cloud.client_data.particles);
    }
}
