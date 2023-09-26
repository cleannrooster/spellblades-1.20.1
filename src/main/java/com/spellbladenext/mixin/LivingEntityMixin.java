package com.spellbladenext.mixin;

import com.spellbladenext.items.Orb;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.internals.SpellCasterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.spellbladenext.Spellblades.MOD_ID;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(at = @At("HEAD"), method = "applyMovementInput", index = 1)
    public Vec3d applyInputMIX(Vec3d vec3d) {
        LivingEntity living = ((LivingEntity) (Object) this);

        if(living instanceof PlayerEntity player && player instanceof SpellCasterEntity entity && entity.getCurrentSpellId() != null && player.getActiveItem().getItem() instanceof Orb) {
            return vec3d.multiply(6);
        }
        else{
            return vec3d;
        }
    }
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void hurtreal(final DamageSource player, float f, final CallbackInfoReturnable<Boolean> info) {
        LivingEntity player2 = ((LivingEntity) (Object) this);
        if (player2 instanceof SpellCasterEntity entity && (entity.getCurrentSpellId() != null && (entity.getCurrentSpellId().equals(new Identifier(MOD_ID, "eviscerate")) || entity.getCurrentSpellId().equals(new Identifier(MOD_ID, "monkeyslam"))))) {
            info.setReturnValue(false);
        }
    }
}
