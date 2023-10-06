package com.spellbladenext.mixin;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.spell_engine.entity.SpellProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.minecraft.util.math.MathHelper.sqrt;

@Mixin(PlayerEntity.class)
public class PlayerMixin implements PlayerDamageInterface {
    public float damageMultipler = 1F;
    public Entity lastAttacked;
    public int lasthurt;
    public float damageAbsorbed;

    public int repeats = 0;
    public boolean overrideDamageMultiplier = false;
    public boolean shouldUnFortify = false;
    public int timesincefirsthurt = 0;
    public boolean offhand = false;
    public List<LivingEntity> list = new ArrayList<>();
    public void repeat(){
        repeats++;
    }
    public int getRepeats(){
        return repeats;
    }

    public int getLasthurt() {
        return lasthurt;
    }

    @Override
    public float getDamageAbsorbed() {
        return damageAbsorbed;
    }

    @Override
    public void resetDamageAbsorbed() {
        damageAbsorbed = 0;
        this.lasthurt = ((PlayerEntity) (Object) this).age;
    }

    @Override
    public void absorbDamage(float i) {
        damageAbsorbed = damageAbsorbed + i;
    }

    public void setLasthurt(int lasthurt) {
        this.lasthurt = lasthurt;
    }

    @Override
    public void resetRepeats() {
        repeats = 0;
    }

    @Override
    public void setDamageMultiplier(float f) {
        this.damageMultipler = f;
    }

    @Override
    public void listAdd(LivingEntity entity) {
        list.add(entity);
    }

    @Override
    public void listRefresh() {
        list = new ArrayList<>();
    }

    @Override
    public boolean listContains(LivingEntity entity) {
        return list.contains(entity);
    }

    @Override
    public List<LivingEntity> getList() {
        return list;
    }

    @Inject(at = @At("HEAD"), method = "applyDamage", cancellable = true)
    protected void applyDamageMixinSpellblade(DamageSource source, float amount, CallbackInfo info) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!player.isInvulnerableTo(source)) {
            this.setLasthurt(player.age);
            this.resetDamageAbsorbed();
        }
    }


        @Override
    public void override(boolean bool) {
        overrideDamageMultiplier = bool;
    }

    @Override
    public void setLastAttacked(Entity entity) {
        this.lastAttacked = entity;
    }

    @Override
    public Entity getLastAttacked() {
        return lastAttacked;
    }

    @Inject(at = @At("HEAD"), method = "getAttackCooldownProgress", cancellable = true)
    private void armor(float f, CallbackInfoReturnable<Float> info) {
        if (this.overrideDamageMultiplier) {
            info.setReturnValue(sqrt(max(0.01F,(this.damageMultipler - 0.2F) / 0.8F)));
        }
    }

    public void shouldUnfortify(boolean bool) {
        this.shouldUnFortify = bool;
    }


}