package com.spellbladenext.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.ai.MagisterAI;
import com.spellbladenext.items.Orb;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.*;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Predicate;

public class Magister extends PathAwareEntity implements InventoryOwner, GeoEntity, Merchant {
    public PlayerEntity nemesis;
    public boolean isthinking = false;
    public boolean isScout = false;
    private boolean hasntthrownitems = true;
    private boolean firstattack = false;
    private boolean secondattack = false;
    private boolean isstopped = false;
    public boolean isCaster = false;
    private PlayerEntity tradingplayer;
    float damagetakensincelastthink = 0;
    public int experiencePoints = 20;

    public Magister(EntityType<? extends Magister> p_34652_, World p_34653_) {
        super(p_34652_, p_34653_);
    }

    private final SimpleInventory inventory = new SimpleInventory(8);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of(Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public boolean returninghome = false;
    public boolean isleader = false;
    public int homecount = 0;
    public int homecount2 = 0;
    public PlayerEntity hero = null;
    public boolean canGiveGifts = false;
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    public static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.hexblade.new");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("animation.hexblade.new2");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.hexblade.walk");
    public static final RawAnimation WALK2 = RawAnimation.begin().thenLoop("animation.hexblade.walk2");
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    public static final RawAnimation IDLE1 = RawAnimation.begin().thenPlay("idle");
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION,MemoryModuleType.NEAREST_ATTACKABLE, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT);
    protected static final ImmutableList<SensorType<? extends Sensor<? super Magister>>> SENSOR_TYPES = ImmutableList.of(MagisterAI.MAGISTER_SENSOR_SENSOR_TYPE,SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);

    @Override
    public void setEquipmentDropChance(EquipmentSlot equipmentSlot, float f) {
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
    }

    @Override
    protected float getDropChance(EquipmentSlot equipmentSlot) {
        return 0;
    }

    public boolean isCaster() {
        return this.isCaster;
    }

    public boolean isScout() {
        return this.isScout;
    }

    public boolean canGiveGifts() {
        return this.canGiveGifts;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Magister piglin) {
        Brain<?> brain = piglin.getBrain();
        Optional<LivingEntity> optional = LookTargetUtil.getEntity(piglin, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && optional.get().distanceTo(piglin) < 32) {
            return optional;
        } else {
            Optional optional2;


            optional2 = brain.getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
            return optional2;

        }
    }

    @Override
    public Brain<Magister> getBrain() {
        return (Brain<Magister>) super.getBrain();
    }

    static boolean isNearestValidAttackTarget(Magister piglin, LivingEntity livingEntity) {
        return findNearestValidAttackTarget(piglin).filter((livingEntity2) -> {
            return livingEntity2 == livingEntity;
        }).isPresent();
    }

    public void writeCustomDataToNbt(NbtCompound compoundTag) {


        if (this.isCaster) {
            compoundTag.putBoolean("Caster", true);
        } else {
            compoundTag.putBoolean("Caster", false);

        }
        if (this.isScout) {
            compoundTag.putBoolean("Scout", true);
        } else {
            compoundTag.putBoolean("Scout", false);

        }
        super.writeCustomDataToNbt(compoundTag);

    }


    public void readCustomDataFromNbt(NbtCompound compoundTag) {

        this.isCaster = compoundTag.getBoolean("Caster");
        this.isScout = compoundTag.getBoolean("Scout");
        super.readCustomDataFromNbt(compoundTag);

    }

    @Override
    public int getMaxLookYawChange() {
        return 9999999;
    }

    protected boolean isImmuneToZombification() {
        return true;
    }

    public boolean attacking = false;
    public boolean justattacked = false;
    public int attackTicker = 0;
    public int attackTime = 0;

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public void tickMovement() {
        tickHandSwing();

        super.tickMovement();
    }

    @Override
    public void tick() {
        super.tick();

        if(this.getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntitiesByType(Spellblades.REAVER,LivingEntity::isAlive).size()>32){
            this.playSoundIfNotSilent(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            this.discard();
        }


        if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            this.lookAtEntity(this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get(), 999, 999);
        }
        if (age > 1000 && !this.getWorld().isClient()) {
            this.playSoundIfNotSilent(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            this.discard();
        }


        if (this.handSwingTicks == 12) {
            SoundHelper.playSoundEvent(this.getWorld(), this, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP);
            Spell.Release.Target.Area area = new Spell.Release.Target.Area();
            area.angle_degrees = 180;
            Predicate<Entity> selectionPredicate = (target) -> {
                return !(target instanceof Magister);
            };
            List<Entity> list = TargetHelper.targetsFromArea(this, this.getBoundingBox().getCenter(), 2.5F, area, selectionPredicate);
            for (Entity entity : list) {
                if (entity.damage(SpellDamageSource.mob(getMagicSchool(), this), (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2)) {
                    entity.timeUntilRegen = 0;
                    entity.damage(this.getWorld().getDamageSources().mobAttack(this), (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 2);
                }
            }
        }
        this.attackTime++;
        if (this.attackTime > 18) {
            this.justattacked = false;
            this.attackTime = 0;
            this.firstattack = false;
            this.secondattack = false;
        }




    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.ENTITY_PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
    }

    protected void playAngrySound() {
    }
    public MagicSchool getMagicSchool(){
        if(this.getMainHandStack().getItem() instanceof Spellblade spellblade){
            return spellblade.getSchool();
        }
        return MagicSchool.ARCANE;
    }

    @Override
    protected boolean shouldDropLoot() {

        return !( this.getRandom().nextFloat() > 0.25 && this.getWorld().getRegistryKey() == Spellblades.DIMENSIONKEY);

    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean bl) {
        if(damageSource.getAttacker() instanceof PlayerEntity player && player.hasStatusEffect(Spellblades.HEXED)){
            player.removeStatusEffect(Spellblades.HEXED);
        }
        if(damageSource.getAttacker() instanceof PlayerEntity player && player.hasStatusEffect(Spellblades.MAGISTERFRIEND)){
            player.removeStatusEffect(Spellblades.MAGISTERFRIEND);
        }
        super.dropLoot(damageSource, bl);
    }

    @Override
    public int getXpToDrop() {
        return experiencePoints;
    }

    @Override
    public boolean canSpawn(WorldAccess levelAccessor, SpawnReason mobSpawnType) {
        return super.canSpawn(levelAccessor, mobSpawnType);
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        if(damageSource.getSource() instanceof PlayerEntity player && this.isScout() && this.getHealth()/this.getMaxHealth() <= 0.5 && this.getMainHandStack().isEmpty()){
            this.tryEquip(new ItemStack(com.spellbladenext.items.Items.arcane_blade.item()));

        }
        if(damageSource.getSource() instanceof PlayerEntity player && damageSource.getAttacker() instanceof PlayerEntity && player.hasStatusEffect(Spellblades.HEXED) && this.getHealth()/this.getMaxHealth() <= 0.5){
            player.removeStatusEffect(Spellblades.HEXED);
        }
        if(damageSource.getSource() instanceof PlayerEntity player && damageSource.getAttacker() instanceof PlayerEntity && player.hasStatusEffect(Spellblades.MAGISTERFRIEND) && this.getHealth()/this.getMaxHealth() <= 0.5){
            player.removeStatusEffect(Spellblades.MAGISTERFRIEND);
        }
        return super.damage(damageSource, f);

    }


    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0.5);
    }
    protected void tickHandSwing() {
        int i = 18;

        if (this.handSwinging) {
            ++this.handSwingTicks;
            if (this.handSwingTicks >= i) {
                this.handSwingTicks = 0;
                this.handSwinging = false;
            }
        } else {
            this.handSwingTicks = 0;
        }

        this.handSwingProgress = (float)this.handSwingTicks / (float)i;
    }
    @Override
    public void swingHand(Hand interactionHand, boolean bl) {
        if (!this.handSwinging || this.handSwingTicks >= 18 || this.handSwingTicks < 0) {

            this.handSwingTicks = -1;
            this.handSwinging = true;
            this.preferredHand = interactionHand;
            if (this.getWorld() instanceof ServerWorld) {
                EntityAnimationS2CPacket clientboundAnimatePacket = new EntityAnimationS2CPacket(this, interactionHand == Hand.MAIN_HAND ? 0 : 3);
                ServerChunkManager serverChunkCache = ((ServerWorld)this.getWorld()).getChunkManager();
                if (bl) {
                    serverChunkCache.sendToNearbyPlayers(this, clientboundAnimatePacket);
                } else {
                    serverChunkCache.sendToOtherNearbyPlayers(this, clientboundAnimatePacket);
                }
            }
        }

    }
    @Override
    public boolean tryAttack(Entity entity) {
        return false;
    }

    protected static boolean canHarvest(Magister piglin){
        return piglin.getMainHandStack().getItem() instanceof HoeItem;
    }



    protected void loot(ItemEntity p_35467_) {


    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return MagisterAI.makeBrain(this,createBrainProfile().deserialize(dynamic));
    }
    protected Brain.Profile<Magister> createBrainProfile() {
        return Brain.createProfile(MEMORY_TYPES, SENSOR_TYPES);
    }
    protected void mobTick() {
        this.getWorld().getProfiler().push("MagisterBrain");
        this.getBrain().tick((ServerWorld)this.getWorld(), this);
        this.getWorld().getProfiler().pop();
        MagisterAI.updateActivity(this);
        super.mobTick();
    }
    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    private PlayState predicate(AnimationState<Magister> state) {
        boolean second = this.random.nextBoolean();
        if(this.handSwinging && !second) {
            state.getController().forceAnimationReset();
            this.secondattack = true;
            this.handSwinging = false;
            return state.setAndContinue(ATTACK);
        }
        if(this.handSwinging) {
            state.getController().forceAnimationReset();

            this.secondattack = false;
            this.handSwinging = false;
            return state.setAndContinue(ATTACK2);
        }

        return PlayState.CONTINUE;

    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand interactionHand) {
        if (this.getOffers().isEmpty() || !this.getMainHandStack().isEmpty()) {
            return ActionResult.FAIL;
        } else {
                this.setCustomer(player);
                this.sendOffers(player, Text.translatable("Protection comes at a price"), 1);

            return ActionResult.SUCCESS;
        }
    }

    private PlayState predicate2(AnimationState<Magister> state) {
        if(state.isMoving()){
            if(this.isAttacking()){
                return state.setAndContinue(WALK2);


            }
            return state.setAndContinue(WALK);


        }
        return state.setAndContinue(IDLE);
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        animationData.add(new AnimationController<Magister>(this,"walk",
                0,this::predicate2)
        );
        animationData.add(new AnimationController<Magister>(this,"attack",
                0,this::predicate)
        );

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public Text getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity player) {
        this.tradingplayer = player;
    }

    @Nullable
    @Override
    public PlayerEntity getCustomer() {
        return this.tradingplayer;
    }

    @Override
    public TradeOfferList getOffers() {
        TradeOfferList offers = new TradeOfferList();
        if(this.getMainHandStack().isEmpty()) {
            ItemStack offering = new ItemStack(Spellblades.OFFERING);
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEBLAZE, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEGLEAM, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEFROST, 2),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(com.spellbladenext.items.Items.arcane_blade.item(), 1),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(com.spellbladenext.items.Items.fire_blade.item(), 1),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(com.spellbladenext.items.Items.frost_blade.item(), 1),
                    offering, 10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEGLEAM, 2),
                    new ItemStack(Spellblades.finalstrikeoil, 1),
                    10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEBLAZE, 2),
                    new ItemStack(Spellblades.flickerstrikeoil, 1),
                    10, 8, 1F));
            offers.add(new TradeOffer(
                    new ItemStack(Spellblades.RUNEFROST, 2),
                    new ItemStack(Spellblades.eviscerateoil, 1),
                    10, 8, 1F));
        }
        return offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList merchantOffers) {

    }

    @Override
    public void trade(TradeOffer merchantOffer) {

    }

    @Override
    public void onSellingItem(ItemStack itemStack) {

    }

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int i) {

    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public SoundEvent getYesSound() {
        return null;
    }

    public void sendOffers(PlayerEntity player, Text component, int i) {
        OptionalInt optionalInt = player.openHandledScreen(new SimpleNamedScreenHandlerFactory((ix, inventory, playerx) -> {
            return new MerchantScreenHandler(ix, inventory, this);
        }, component));
        if (optionalInt.isPresent() && this.getMainHandStack().isEmpty()) {
            TradeOfferList merchantOffers = this.getOffers();
            if (!merchantOffers.isEmpty()) {
                player.sendTradeOffers(optionalInt.getAsInt(), merchantOffers, i, this.getExperience(), this.isLeveledMerchant(), this.canRefreshTrades());
            }
        }

    }

    @Override
    public boolean isClient() {
        return false;
    }
}

