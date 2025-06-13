package com.spellbladenext.effect;

import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.AnimationHelper;

import java.util.List;

import static com.spellbladenext.Spellblades.MOD_ID;

public class Spellstrike extends StatusEffect {
    public Spellstrike(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration == 1;
    }



    @Override
    public void applyUpdateEffect(LivingEntity player, int amplifier) {

        if(player instanceof PlayerDamageInterface playerDamageInterface && player instanceof SpellCasterEntity caster){
            int cooldown = (int)(SpellHelper.getCooldownDuration(player,SpellRegistry.getSpell(Identifier.of(MOD_ID,"spellstrike")))*20);
            caster.getCooldownManager().set(Identifier.of(MOD_ID,"spellstrike"),cooldown);

        }
        if(player instanceof PlayerEntity  && !player.getWorld().isClient()){
            AnimationHelper.sendAnimation((PlayerEntity) player, PlayerLookup.tracking(player), SpellCast.Animation.RELEASE, SpellRegistry.getSpell(Identifier.of(MOD_ID, "spellstrike2")).release.animation, 1F);
            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                AnimationHelper.sendAnimation((PlayerEntity) player, List.of(serverPlayerEntity), SpellCast.Animation.RELEASE, SpellRegistry.getSpell(Identifier.of(MOD_ID, "spellstrike2")).release.animation, 1F);
            }
        }
    }


}
