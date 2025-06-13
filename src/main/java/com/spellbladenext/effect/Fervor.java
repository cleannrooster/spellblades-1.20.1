package com.spellbladenext.effect;

import com.spellbladenext.Spellblades;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;

public class Fervor extends CustomEffect{
    public Fervor(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        ParticleHelper.play(entity.getWorld(),entity, SpellRegistry.getSpell(Identifier.of(Spellblades.MOD_ID,"particlesholy")).release.target.cloud.client_data.particles);
    }
}