package com.spellbladenext.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.casting.SpellCasterEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.spellbladenext.Spellblades.DIMENSIONKEY;

@Mixin(ItemStack.class)
abstract  class ItemStackMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damage(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if(player instanceof SpellCasterEntity entity && entity.getCurrentSpell() != null){
            info.cancel();
        }

    }

}
