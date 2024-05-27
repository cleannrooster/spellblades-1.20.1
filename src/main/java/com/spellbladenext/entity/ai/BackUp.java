package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableMap;
import com.spellbladenext.entity.Magister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.spell_power.api.SpellSchools;

public class BackUp<E extends MobEntity> extends MultiTickTask<E> {
    private final int tooCloseDistance;
    private final float strafeSpeed;

    public BackUp(int i, float f) {
        super(ImmutableMap.of( MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
        this.tooCloseDistance = i;
        this.strafeSpeed = f;
    }
    protected boolean shouldRun(ServerWorld serverLevel, E mob) {
        if(this.getTarget(mob) instanceof PlayerEntity player &&( player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) > 40 ||
                player.getAttributeValue(SpellSchools.ARCANE.attribute) > mob.getMaxHealth()/2 ||
                player.getAttributeValue(SpellSchools.FROST.attribute) > mob.getMaxHealth()/2 ||
                player.getAttributeValue(SpellSchools.FIRE.attribute) > mob.getMaxHealth()/2 ||
                player.getAttributeValue(SpellSchools.HEALING.attribute) > mob.getMaxHealth()/2)){
            return true;
        }
        return (/*mob instanceof Magus ||*/ (mob instanceof Magister reaver && reaver.isCaster())) && this.isTargetVisible(mob) && this.isTargetTooClose(mob);
    }


    public void run(ServerWorld serverLevel, E mob, long l) {
        //System.out.println("backing up!");
        mob.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(this.getTarget(mob), true));
        mob.getMoveControl().strafeTo(-this.strafeSpeed, 0.0F);
        mob.setYaw(MathHelper.clampAngle(mob.getYaw(), mob.headYaw, 0.0F));
    }


    @Override
    public String getName() {
        return null;
    }

    private boolean isTargetVisible(E mob) {
        return ((LivingTargetCache)mob.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).get()).contains(this.getTarget(mob));
    }

    private boolean isTargetTooClose(E mob) {
        return this.getTarget(mob).isInRange(mob, (double)this.tooCloseDistance);
    }

    private LivingEntity getTarget(E mob) {
        return (LivingEntity)mob.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
