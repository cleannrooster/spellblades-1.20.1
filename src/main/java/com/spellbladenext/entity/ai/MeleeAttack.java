package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class MeleeAttack  extends MultiTickTask<MobEntity> {
    private final int cooldownBetweenAttacks;

    public MeleeAttack(int i) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleState.VALUE_ABSENT));
        this.cooldownBetweenAttacks = i;
    }

    protected boolean shouldRun(ServerWorld serverLevel, MobEntity mob) {
        LivingEntity livingEntity = this.getAttackTarget(mob);

        return !( mob.hasCustomName() &&mob.getCustomName().equals(Text.translatable("Caster"))) && LookTargetUtil.isVisibleInMemory(mob, livingEntity) && mob.isInAttackRange(livingEntity);
    }

    private boolean isHoldingUsableProjectileWeapon(MobEntity mob) {
        return mob.isHolding((itemStack) -> {
            Item item = itemStack.getItem();
            return item instanceof RangedWeaponItem && mob.canUseRangedWeapon((RangedWeaponItem)item);
        });
    }

    protected void run(ServerWorld serverLevel, MobEntity mob, long l) {
        LivingEntity livingEntity = this.getAttackTarget(mob);
        mob.lookAtEntity(livingEntity,90,90);
/*
        if(!(mob instanceof Archmagus archmagus && archmagus.getDataTracker().get(Archmagus.BIDED))) {
*/
            mob.swingHand(Hand.MAIN_HAND);
        mob.tryAttack(livingEntity);
        mob.getBrain().remember(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long)this.cooldownBetweenAttacks);
    }

    private LivingEntity getAttackTarget(MobEntity mob) {
        return (LivingEntity)mob.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
