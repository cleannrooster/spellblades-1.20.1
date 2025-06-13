package com.spellbladenext.mixin;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.utils.AnimationHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(AnimationHelper.class)
public class AnimationHelperMixin {
    @Inject(at = @At("HEAD"), method = "sendAnimation", cancellable = true)
    private static  void sendAnimationSpellblades(PlayerEntity animatedPlayer, Collection<ServerPlayerEntity> trackingPlayers, SpellCast.Animation type, String name, float speed, CallbackInfo info) {
        if(name != null && animatedPlayer instanceof PlayerDamageInterface playerInterface&&  name.equals( Identifier.of(Spellblades.MOD_ID,"sword_swing_first").toString())){
            if(playerInterface.isSecondSwing()) {
                AnimationHelper.sendAnimation(animatedPlayer, trackingPlayers, type, Identifier.of(Spellblades.MOD_ID, "sword_swing_second").toString(), speed);
                info.cancel();
            }
            playerInterface.nextSwing();
        }

    }
}
