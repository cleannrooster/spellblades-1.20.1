package com.spellbladenext.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.spellbladenext.entity.Magus;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

import static com.spellbladenext.entity.Magus.DOWN2;


public class MagusAI {
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

        public MagusAI() {
        }
        public static Brain<?> makeBrain(Magus Magus, Brain<Magus> brain) {
            initCoreActivity(Magus, brain);
            initIdleActivity(Magus, brain);
            initFightActivity(Magus, brain);
            brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
            brain.setDefaultActivity(Activity.IDLE);
            brain.resetPossibleActivities();
            return brain;
        }

        protected static void initMemories(Magus Magus) {
            GlobalPos globalPos = GlobalPos.create(Magus.getWorld().getRegistryKey(), Magus.getBlockPos());
            Magus.getBrain().remember(MemoryModuleType.HOME, globalPos);
        }

        private static void initCoreActivity(Magus Magus, Brain<Magus> brain) {
            brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90),FindWalkTargetTask.create(0.6F), new WanderAroundTask(),   OpenDoorsTask.create(),  ForgetAngryAtTargetTask.create()));
        }

        private static void initIdleActivity(Magus Magus1, Brain<Magus> brain) {
            brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(UpdateAttackTargetTask.create(magus -> !Magus1.getDataTracker().get(DOWN2), MagusAI::findNearestValidAttackTarget), new WaitTask(30, 60), StrollTask.create(0.6F),  FindInteractionTargetTask.create(EntityType.PLAYER, 4)));
        }


        private static void initFightActivity(Magus Magus1, Brain<Magus> brain) {

            brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of( ForgetAttackTargetTask.create((livingEntity) -> {
                return !isNearestValidAttackTarget(Magus1, (LivingEntity) livingEntity);
            }),new MagusJumpBack<Magus>(4.5D,1F), RangedApproachTask.create(archmagus -> {
                if (!Magus1.getDataTracker().get(DOWN2)  && Magus1.getDataTracker().get(Magus.BIDED)){
                    return 1.3F;
                }
                else if (!Magus1.getDataTracker().get(DOWN2)){
                    return 1.0F;
                }
                else{
                    return 0F;
                }
            }), new MeleeAttack(20)), MemoryModuleType.ATTACK_TARGET);
        }



        public static void updateActivity(Magus Magus) {
            Brain<?> brain = Magus.getBrain();
            Activity activity = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
            brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
            Activity activity2 = (Activity)brain.getFirstPossibleNonCoreActivity().orElse((Activity) null);
            if (activity != activity2) {
                playActivitySound(Magus);
            }

            Magus.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
        }

        private static boolean isNearestValidAttackTarget(Magus Magus, LivingEntity livingEntity) {

            return findNearestValidAttackTarget(Magus).filter((livingEntity2) -> {
                return livingEntity2 == livingEntity;
            }).isPresent();
        }
    private static final TargetPredicate ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetPredicate.createAttackable().setBaseMaxDistance(40).ignoreVisibility().ignoreDistanceScalingFactor();
    private static final TargetPredicate ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetPredicate.createAttackable().setBaseMaxDistance(40).ignoreVisibility();

    public static boolean isEntityAttackableIgnoringLineOfSight(LivingEntity livingEntity, LivingEntity livingEntity2) {
        return livingEntity.getBrain().hasMemoryModuleWithValue(MemoryModuleType.ATTACK_TARGET, livingEntity2) ? ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.test(livingEntity, livingEntity2) :
                ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.test(livingEntity, livingEntity2);
    }
        private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Magus Magus) {


            Optional<LivingEntity> optional = LookTargetUtil.getEntity(Magus, MemoryModuleType.ANGRY_AT);
            if (optional.isPresent() && isEntityAttackableIgnoringLineOfSight(Magus, (LivingEntity)optional.get())) {
                return optional;
            } else {
                Optional<? extends LivingEntity> optional2 = getTargetIfWithinRange(Magus, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
                return optional2.isPresent() ? optional2 : Magus.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            }
        }

        private static Optional<? extends LivingEntity> getTargetIfWithinRange(Magus Magus, MemoryModuleType<? extends LivingEntity> memoryModuleType) {
            return Magus.getBrain().getOptionalMemory(memoryModuleType).filter((livingEntity) -> {
                return livingEntity.isInRange(Magus, 36);
            });
        }






        private static void playActivitySound(Magus Magus) {
            Magus.getBrain().getFirstPossibleNonCoreActivity().ifPresent((activity) -> {


            });
        }


}
