package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.spellbladenext.entity.Magister;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.Optional;

public class MagisterAI {
    private static final int ANGER_DURATION = 600;
    private static final int MELEE_ATTACK_COOLDOWN = 20;
    private static final double ACTIVITY_SOUND_LIKELIHOOD_PER_TICK = 0.0125D;
    private static final int MAX_LOOK_DIST = 8;
    private static final int INTERACTION_RANGE = 8;
    private static final double TARGETING_RANGE = 12.0D;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
    private static final int HOME_CLOSE_ENOUGH_DISTANCE = 2;
    private static final int HOME_TOO_FAR_DISTANCE = 100;
    private static final int HOME_STROLL_AROUND_DISTANCE = 5;

    public MagisterAI() {
    }
    public static Brain<?> makeBrain(Magister reaver, Brain<Magister> brain) {
        initCoreActivity(reaver, brain);
        initIdleActivity(reaver, brain);
        initFightActivity(reaver, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    protected static void initMemories(Magister reaver) {
        GlobalPos globalPos = GlobalPos.create(reaver.getWorld().getRegistryKey(), reaver.getBlockPos());
        reaver.getBrain().remember(MemoryModuleType.HOME, globalPos);
    }

    private static void initCoreActivity(Magister reaver, Brain<Magister> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90), new WanderAroundTask(),   OpenDoorsTask.create(),  ForgetAngryAtTargetTask.create()));
    }

    private static void initIdleActivity(Magister reaver, Brain<Magister> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(new WanderAroundTask(), UpdateAttackTargetTask.create(MagisterAI::findNearestValidAttackTarget), makeRandomFollowTask(),   FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
    }

    private static void initFightActivity(Magister reaver, Brain<Magister> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of( ForgetAttackTargetTask.create((livingEntity) -> {
            return !isNearestValidAttackTarget(reaver, (LivingEntity) livingEntity);
        }),new BackUp<Magister>(10, 0.75F),   RangedApproachTask.create(1.0F),  new MeleeAttack(20),new SpellAttack<Magister, LivingEntity>()), MemoryModuleType.ATTACK_TARGET);
    }


    private static RandomTask<LivingEntity> makeRandomFollowTask() {
        return new RandomTask(ImmutableList.builder().addAll(makeFollowTasks()).add(Pair.of(new WaitTask(30, 60), 1)).build());
    }
    private static ImmutableList<Pair<SingleTickTask<LivingEntity>, Integer>> makeFollowTasks() {
        return ImmutableList.of(Pair.of(LookAtMobTask.create(EntityType.PLAYER, 8.0F), 1), Pair.of(LookAtMobTask.create(EntityType.PIGLIN, 8.0F), 1), Pair.of(LookAtMobTask.create(8.0F), 1));
    }
    public static void updateActivity(Magister reaver) {
        Brain<?> brain = reaver.getBrain();
        Activity activity = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
        //System.out.println(activity);
        brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        Activity activity2 = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
        if (activity != activity2) {
            playActivitySound(reaver);
        }

        reaver.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean isNearestValidAttackTarget(Magister reaver, LivingEntity livingEntity) {
            if(reaver.isScout() && reaver.getMainHandStack().isEmpty()){
                return false;
            }
        return findNearestValidAttackTarget(reaver).filter((livingEntity2) -> {
            return livingEntity2 == livingEntity;
        }).isPresent();
    }

    static Optional<? extends LivingEntity> findNearestValidAttackTarget(Magister abstractPiglin) {
            if(abstractPiglin.isScout() && abstractPiglin.getMainHandStack().isEmpty()){
                return Optional.empty();
            }

        Optional<LivingEntity> optional = LookTargetUtil.getEntity(abstractPiglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.testAttackableTargetPredicateIgnoreVisibility(abstractPiglin, (LivingEntity)optional.get())) {
            return optional;
        } else {
            Optional<? extends LivingEntity> optional2 = getTargetIfWithinRange(abstractPiglin, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
            return optional2.isPresent() ? optional2 : abstractPiglin.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        }
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(Magister abstractPiglin, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
        return abstractPiglin.getBrain().getOptionalMemory(memoryModuleType).filter((livingEntity) -> {
            return livingEntity.isInRange(abstractPiglin, 36);
        });
    }

    protected static void wasHurtBy(Magister reaver, LivingEntity livingEntity) {
        if (!(livingEntity instanceof AbstractPiglinEntity)) {
            maybeRetaliate(reaver, livingEntity);
        }
    }
    protected static void maybeRetaliate(Magister abstractPiglin, LivingEntity livingEntity) {
        if (!abstractPiglin.getBrain().hasActivity(Activity.AVOID)) {
            if (Sensor.testAttackableTargetPredicateIgnoreVisibility(abstractPiglin, livingEntity)) {
                if (!LookTargetUtil.isNewTargetTooFar(abstractPiglin, livingEntity, 4.0D)) {
                    if (livingEntity.getType() == EntityType.PLAYER && abstractPiglin.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                        setAngerTargetToNearestTargetablePlayerIfFound(abstractPiglin, livingEntity);
                        broadcastUniversalAnger(abstractPiglin);
                    } else {
                        setAngerTarget(abstractPiglin, livingEntity);
                        broadcastAngerTarget(abstractPiglin, livingEntity);
                    }

                }
            }
        }
    }
    protected static void broadcastUniversalAnger(Magister abstractPiglin) {
        getAdultPiglins(abstractPiglin).forEach((abstractPiglinx) -> {
            getNearestVisibleTargetablePlayer(abstractPiglinx).ifPresent((player) -> {
                setAngerTarget(abstractPiglinx, player);
            });
        });
    }

    private static List<Magister> getAdultPiglins(Magister abstractPiglin) {
        return (List)abstractPiglin.getBrain().getOptionalMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }
    protected static void broadcastAngerTarget(Magister abstractPiglin, LivingEntity livingEntity) {
        getAdultPiglins(abstractPiglin).forEach((abstractPiglinx) -> {
                setAngerTargetIfCloserThanCurrent(abstractPiglinx, livingEntity);
        });
    }
    private static Optional<LivingEntity> getAngerTarget(Magister abstractPiglin) {
        return LookTargetUtil.getEntity(abstractPiglin, MemoryModuleType.ANGRY_AT);
    }
    private static void setAngerTargetIfCloserThanCurrent(Magister abstractPiglin, LivingEntity livingEntity) {
        Optional<LivingEntity> optional = getAngerTarget(abstractPiglin);
        LivingEntity livingEntity2 = LookTargetUtil.getCloserEntity(abstractPiglin, optional, livingEntity);
        if (!optional.isPresent() || optional.get() != livingEntity2) {
            setAngerTarget(abstractPiglin, livingEntity2);
        }
    }
    private static void setAngerTargetToNearestTargetablePlayerIfFound(Magister abstractPiglin, LivingEntity livingEntity) {
        Optional<PlayerEntity> optional = getNearestVisibleTargetablePlayer(abstractPiglin);
        if (optional.isPresent()) {
            setAngerTarget(abstractPiglin, (LivingEntity)optional.get());
        } else {
            setAngerTarget(abstractPiglin, livingEntity);
        }

    }
    public static Optional<PlayerEntity> getNearestVisibleTargetablePlayer(Magister abstractPiglin) {
        return abstractPiglin.getBrain().hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) ? abstractPiglin.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER) : Optional.empty();
    }
    protected static void setAngerTarget(Magister reaver, LivingEntity livingEntity) {
        reaver.getBrain().forget(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        reaver.getBrain().remember(MemoryModuleType.ANGRY_AT, livingEntity.getUuid(), 600L);
    }

    protected static void maybePlayActivitySound(Magister reaver) {
        if ((double)reaver.getWorld().random.nextFloat() < 0.0125D) {
            playActivitySound(reaver);
        }

    }

    private static void playActivitySound(Magister reaver) {
        reaver.getBrain().getFirstPossibleNonCoreActivity().ifPresent((activity) -> {


        });
    }
}


