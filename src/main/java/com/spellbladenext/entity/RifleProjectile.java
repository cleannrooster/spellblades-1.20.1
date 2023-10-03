package com.spellbladenext.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RifleProjectile extends ArrowEntity {
    private double damage = 2;

    public RifleProjectile(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void setDamage(double damage) {
        super.setDamage(damage);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public double getDamage() {
        return super.getDamage();
    }

    @Override
    public Vec3d getVelocity() {
        return new Vec3d(0,0,3);
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
    }
}
