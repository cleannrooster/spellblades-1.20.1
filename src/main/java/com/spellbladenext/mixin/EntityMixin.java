package com.spellbladenext.mixin;

import com.spellbladenext.Spellblades;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "extinguish", cancellable = true)

    public void extinguishSB(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof LivingEntity living && living.hasStatusEffect(Spellblades.PHOENIXCURSE)){
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "extinguishWithSound", cancellable = true)

    public void extinguish2SB(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof LivingEntity living && living.hasStatusEffect(Spellblades.PHOENIXCURSE)){
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "isOnFire", cancellable = true)

    public void isOnFireSB(CallbackInfoReturnable<Boolean> ci) {
        Entity entity = (Entity) (Object) this;
        if(entity instanceof LivingEntity living && living.hasStatusEffect(Spellblades.PHOENIXCURSE)){
            ci.setReturnValue(true);
        }
    }
}