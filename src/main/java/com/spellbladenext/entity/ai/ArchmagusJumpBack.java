package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableMap;
import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Archmagus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchools;

import java.util.List;
import java.util.Optional;

import static com.spellbladenext.entity.Archmagus.*;


public class ArchmagusJumpBack<E extends Archmagus> extends MultiTickTask<E> {
    private final double tooCloseDistance;
    private final float strafeSpeed;
    float time = 0;
    boolean bool = true;

    public ArchmagusJumpBack(double i, float f) {
        super(ImmutableMap.of( MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT));
        this.tooCloseDistance = i;
        this.strafeSpeed = f;
    }

    protected boolean checkExtraStartConditions(ServerWorld serverLevel, E mob) {
        float health = mob.getMaxHealth()/10;
        return this.isTargetVisible(mob) && mob.getMaxHealth() - health*(mob.getDataTracker().get(TIER)+1) >= mob.getHealth() ;
    }
    @Override
    public boolean shouldRun(ServerWorld serverLevel, E mob) {
        //System.out.println("backing up!");

        if(this.getTarget(mob).isPresent() ) {
            mob.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(this.getTarget(mob).get(), true));
            bool = serverLevel.random.nextBoolean();
            return checkExtraStartConditions(serverLevel,mob);

        }
        else{
            return false;
        }
        //mob.getMoveControl().strafe(-this.strafeSpeed, 0.0F);
        //mob.setYRot(Mth.rotateIfNecessary(mob.getYRot(), mob.yHeadRot, 0.0F));
    }


    @Override
    protected boolean shouldKeepRunning(ServerWorld serverWorld, E livingEntity, long l) {
        return this.time <=40;
    }

    @Override
    public void finishRunning(ServerWorld serverLevel, E livingEntity, long l) {
        this.time = 0;
        livingEntity.getDataTracker().set(DOWN2,false);

        if(this.getTarget(livingEntity).isPresent()) {

            Vec3d vec31 = new Vec3d(-this.getTarget(livingEntity).get().getX()+livingEntity.getX(),0,-this.getTarget(livingEntity).get().getZ()+livingEntity.getZ());
            Vec3d vec3 = new Vec3d(vec31.normalize().x*1, 0.5,vec31.normalize().z*1 );
            livingEntity.setPosition(livingEntity.getPos().add(0,0.2,0));
            livingEntity.setOnGround(false);
            livingEntity.setVelocity(vec3);
            livingEntity.isthinking = true;
            livingEntity.thinktime = 0;
            livingEntity.damagetakensincelastthink = 0;
            livingEntity.casting = true;

            livingEntity.getDataTracker().set(JUMPING,true);
            livingEntity.getDataTracker().set(TIER,livingEntity.getDataTracker().get(TIER)+1);

        }

        super.finishRunning(serverLevel, livingEntity, l);
    }

    @Override
    public void keepRunning(ServerWorld serverLevel, E livingEntity, long l) {
        super.keepRunning(serverLevel, livingEntity, l);
            livingEntity.getDataTracker().set(DOWN2,true);

        int i = 1;
        if(bool){
            i = -1;
        }
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,5,9,false,false));

        if(this.getTarget(livingEntity).isPresent()) {
            int ii = 1;
            if(this.isTargetTooClose(livingEntity)){
             ii = -1;
            }
            livingEntity.lookAtEntity(this.getTarget(livingEntity).get(),999,999);
            livingEntity.getBrain().forget(MemoryModuleType.WALK_TARGET);

        }
        if(time % 10 == 0) {
            if (livingEntity.getMagicSchool() == SpellSchools.ARCANE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "arcaneoverdrive"));
                if (!serverLevel.isClient()) {
                    ParticleHelper.sendBatches(livingEntity, spell.release.particles);
                    SoundHelper.playSound(serverLevel,livingEntity,spell.release.sound);

                }

                List<Entity> entities = serverLevel.getEntitiesByClass(Entity.class, livingEntity.getBoundingBox().expand(4, 2, 4), entity -> entity != livingEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(SpellSchools.ARCANE, livingEntity), (float) livingEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);
                }
            }
            if (livingEntity.getMagicSchool() == SpellSchools.FIRE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "fireoverdrive"));
                if (!serverLevel.isClient()) {
                    ParticleHelper.sendBatches(livingEntity, spell.release.particles);
                    SoundHelper.playSound(serverLevel,livingEntity,spell.release.sound);
                }

                List<Entity> entities = serverLevel.getEntitiesByClass(Entity.class, livingEntity.getBoundingBox().expand(4, 2, 4), entity -> entity != livingEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(SpellSchools.FIRE, livingEntity), (float) livingEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);

                }
            }
            if (livingEntity.getMagicSchool() == SpellSchools.FROST) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "frostoverdrive"));
                if (!serverLevel.isClient()) {
                    ParticleHelper.sendBatches(livingEntity, spell.release.particles);
                    SoundHelper.playSound(serverLevel,livingEntity,spell.release.sound);

                }

                List<Entity> entities = serverLevel.getEntitiesByClass(Entity.class, livingEntity.getBoundingBox().expand(4, 2, 4), entity -> entity != livingEntity);
                for (Entity entity : entities) {
                    entity.damage(SpellDamageSource.mob(SpellSchools.FROST, livingEntity), (float) livingEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * 0.2F);
                }
            }
        }

        time++;
    }

    private boolean isTargetVisible(E mob) {
        if(this.getTarget(mob).isPresent()) {

            return ((LivingTargetCache) mob.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).get()).contains(this.getTarget(mob).get());
        }
        return false;
    }

    private boolean isTargetTooClose(E mob) {
        if(this.getTarget(mob).isPresent()) {
        return this.getTarget(mob).get().isInRange(mob, (double)this.tooCloseDistance);
        }
        return false;
    }

    private Optional<LivingEntity> getTarget(E mob) {
        return mob.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
