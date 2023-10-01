package com.spellbladenext.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.spellbladenext.Spellblades.DIMENSIONKEY;

@Mixin(ItemEntity.class)
abstract  class ItemEntityMixin {
    @Inject(at = @At("HEAD"), method = "applyWaterBuoyancy", cancellable = true)
    private void setBounding( CallbackInfo info) {
        ItemEntity itenEntity = (ItemEntity) (Object) this;
        if (itenEntity.getWorld().getRegistryKey().equals(DIMENSIONKEY)) {

            Vec3d vec3d = itenEntity.getVelocity();
            itenEntity.setVelocity(vec3d.x * 0.9900000095367432D, vec3d.y + 0.01, vec3d.z * 0.9900000095367432D);

        }

    }

}
