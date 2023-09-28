package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableMap;
import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Magister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.entity.SpellProjectile;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.utils.SoundHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import net.spell_power.api.attributes.SpellAttributes;

import static net.spell_engine.internals.SpellHelper.impactTargetingMode;
import static net.spell_engine.internals.SpellHelper.launchPoint;

public class SpellAttack<E extends MobEntity, T extends LivingEntity> extends MultiTickTask<E> {
    private static final int TIMEOUT = 1200;
    private int attackDelay = 20;
    private CrossbowState crossbowState;
    public SpellAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT), 1200);
        this.crossbowState = CrossbowState.UNCHARGED;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, E entity) {
        return  entity instanceof Magister reaver && reaver.isCaster();
    }

    protected boolean checkExtraStartConditions(ServerWorld serverLevel, E mob) {

        LivingEntity livingEntity = getAttackTarget(mob);
        if(livingEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) > 40 ||
                livingEntity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute) > mob.getMaxHealth()/2 ||
                livingEntity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FROST).attribute) > mob.getMaxHealth()/2 ||
                livingEntity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute) > mob.getMaxHealth()/2 ||
                livingEntity.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute) > mob.getMaxHealth()/2){
            return false;
        }
        return  mob instanceof Magister reaver && reaver.isCaster() && LookTargetUtil.isVisibleInMemory(mob, livingEntity) && mob.distanceTo(livingEntity) < 32;
    }

    protected boolean shouldKeepRunning(ServerWorld serverLevel, E mob, long l) {
        return mob.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel, mob);
    }

    protected void run(ServerWorld serverLevel, E mob, long l) {
        LivingEntity livingEntity = getAttackTarget(mob);
        //this.lookAtTarget(mob, livingEntity);
        this.crossbowAttack(mob, livingEntity);
    }

    protected void finishRunning(ServerWorld serverLevel, E mob, long l) {
        if (mob.isUsingItem()) {
            mob.clearActiveItem();
        }


    }

    private void crossbowAttack(E mob, LivingEntity target) {
        if(attackDelay <= 0) {
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == MagicSchool.ARCANE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "arcane_missile"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(MagicSchool.ARCANE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.8, 0, 1), impactTargetingMode(spell));
                Vec3d launchPoint = launchPoint(mob);
                SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, spell, target, context,   new Spell.ProjectileData().perks);
                Spell.ProjectileData projectileData = spell.release.target.projectile;
                projectileData.homing_angle = 15;
                float velocity = projectileData.velocity;
                float divergence = projectileData.divergence;
                SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
                Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                projectile.setVelocity(0, 1, 0, velocity, divergence);

                projectile.range = spell.range;
                projectile.getPitch(mob.getPitch());
                projectile.setYaw(mob.getYaw());
                mob.getWorld().spawnEntity(projectile);
                attackDelay = 20;
            }
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == MagicSchool.FROST) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "frostbolt"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(MagicSchool.FROST, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*0.6, 0, 1), impactTargetingMode(spell));
                Vec3d launchPoint = launchPoint(mob);
                SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, spell, target, context,   new Spell.ProjectileData().perks);
                Spell.ProjectileData projectileData = spell.release.target.projectile;
                float additional = 7.5F*reaver.getRandom().nextFloat();
                projectileData.homing_angle = 7.5F+additional;
                float velocity = projectileData.velocity;
                float divergence = projectileData.divergence;
                SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1.2F);
                Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                projectile.setVelocity(0, 1, 0, velocity, divergence);

                projectile.range = spell.range;
                projectile.getPitch(mob.getPitch());
                projectile.setYaw(mob.getYaw());
                mob.getWorld().spawnEntity(projectile);
                attackDelay = 10;
            }
            if(mob instanceof Magister reaver && reaver.getMagicSchool() == MagicSchool.FIRE) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "fireball"));
                SpellHelper.ImpactContext context = new SpellHelper.ImpactContext(1, 1.0F, (Vec3d) null, new SpellPower.Result(MagicSchool.FIRE, mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)*1, 0, 1), impactTargetingMode(spell));
                Vec3d launchPoint = launchPoint(mob);
                SpellProjectile projectile = new SpellProjectile(mob.getWorld(), mob, launchPoint.getX(), launchPoint.getY(), launchPoint.getZ(), SpellProjectile.Behaviour.FLY, spell, target, context,   new Spell.ProjectileData().perks);
                Spell.ProjectileData projectileData = spell.release.target.projectile;
                projectileData.homing_angle = 15;
                float velocity = projectileData.velocity;
                float divergence = projectileData.divergence;
                SoundHelper.playSoundEvent(mob.getWorld(), mob, SoundEvents.ENTITY_BLAZE_SHOOT, 1, 1);
                Vec3d look = target.getBoundingBox().getCenter().subtract(launchPoint).normalize();
                projectile.setVelocity(0, 1, 0, velocity, divergence);

                projectile.range = spell.range;
                projectile.getPitch(mob.getPitch());
                projectile.setYaw(mob.getYaw());
                mob.getWorld().spawnEntity(projectile);
                attackDelay = 40;
            }
        }
        else{
            attackDelay--;
        }
    }

    private void lookAtTarget(MobEntity mob, LivingEntity livingEntity) {
        mob.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(livingEntity, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity) {
        return (LivingEntity)livingEntity.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    static enum CrossbowState {
        UNCHARGED,
        CHARGING,
        CHARGED,
        READY_TO_ATTACK;

        private CrossbowState() {
        }
    }
}

