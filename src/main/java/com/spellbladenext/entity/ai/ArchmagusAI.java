package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.spellbladenext.entity.Archmagus;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

import static com.spellbladenext.entity.Archmagus.DOWN2;


public class ArchmagusAI {
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

        public ArchmagusAI() {
        }
        public static Brain<?> makeBrain(Archmagus Archmagus, Brain<Archmagus> brain) {
            initCoreActivity(Archmagus, brain);
            initIdleActivity(Archmagus, brain);
            initFightActivity(Archmagus, brain);
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.resetPossibleActivities();
            return brain;
        }

        protected static void initMemories(Archmagus Archmagus) {
            GlobalPos globalPos = GlobalPos.create(Archmagus.getWorld().getRegistryKey(), Archmagus.getBlockPos());
            Archmagus.getBrain().remember(MemoryModuleType.HOME, globalPos);
        }

        private static void initCoreActivity(Archmagus Archmagus, Brain<Archmagus> brain) {
            brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90), new WanderAroundTask(),   OpenDoorsTask.create(),  ForgetAngryAtTargetTask.create()));
        }

        private static void initIdleActivity(Archmagus Archmagus1, Brain<Archmagus> brain) {
            brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(UpdateAttackTargetTask.create(magus -> !Archmagus1.getDataTracker().get(DOWN2),ArchmagusAI::findNearestValidAttackTarget), new WaitTask(30, 60), StrollTask.create(0.6F),  FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
        }


        private static void initFightActivity(Archmagus Archmagus1, Brain<Archmagus> brain) {

            brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of( ForgetAttackTargetTask.create((livingEntity) -> {
                return !isNearestValidAttackTarget(Archmagus1, (LivingEntity) livingEntity);
            }),new ArchmagusJumpBack<Archmagus>(4.5D,1F), RangedApproachTask.create(archmagus -> {
                if (!Archmagus1.getDataTracker().get(DOWN2)  && Archmagus1.getDataTracker().get(Archmagus.BIDED)){
                    return 1.3F;
                }
                else if (!Archmagus1.getDataTracker().get(DOWN2)){
                    return 1.0F;
                }
                else{
                    return 0F;
                }
            }), new MeleeAttack(20)), MemoryModuleType.ATTACK_TARGET);
        }



        public static void updateActivity(Archmagus Archmagus) {
            Brain<?> brain = Archmagus.getBrain();
            Activity activity = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
            brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
            Activity activity2 = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
            if (activity != activity2) {
                playActivitySound(Archmagus);
            }

            Archmagus.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
        }

        private static boolean isNearestValidAttackTarget(Archmagus Archmagus, LivingEntity livingEntity) {

            return findNearestValidAttackTarget(Archmagus).filter((livingEntity2) -> {
                return livingEntity2 == livingEntity;
            }).isPresent();
        }
    private static final TargetPredicate ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetPredicate.createAttackable().setBaseMaxDistance(40).ignoreVisibility().ignoreDistanceScalingFactor();
    private static final TargetPredicate ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetPredicate.createAttackable().setBaseMaxDistance(40).ignoreVisibility();

    public static boolean isEntityAttackableIgnoringLineOfSight(LivingEntity livingEntity, LivingEntity livingEntity2) {
        return livingEntity.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ATTACK_TARGET, livingEntity2) ? ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.test(livingEntity, livingEntity2) :
                ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.test(livingEntity, livingEntity2);
    }
        private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Archmagus Archmagus) {


            Optional<LivingEntity> optional = LookTargetUtil.getEntity(Archmagus, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && isEntityAttackableIgnoringLineOfSight(Archmagus, (LivingEntity)optional.get())) {
                return optional;
            } else {
                Optional<? extends LivingEntity> optional2 = getTargetIfWithinRange(Archmagus, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
                return optional2.isPresent() ? optional2 : Archmagus.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            }
        }

        private static Optional<? extends LivingEntity> getTargetIfWithinRange(Archmagus Archmagus, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
            return Archmagus.getBrain().getOptionalMemory(memoryModuleType).filter((livingEntity) -> {
                return livingEntity.isInRange(Archmagus, 36);
            });
        }






        private static void playActivitySound(Archmagus Archmagus) {
            Archmagus.getBrain().getFirstPossibleNonCoreActivity().ifPresent((activity) -> {


            });
        }


}
