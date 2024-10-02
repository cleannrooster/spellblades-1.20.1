package com.spellbladenext.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.ai.*;
import com.spellbladenext.invasions.piglinsummon;
import com.spellbladenext.items.DebugNetherPortal;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.SoundHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import net.spell_power.mixin.DamageSourcesAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static com.extraspellattributes.ReabsorptionInit.WARDING;
import static com.spellbladenext.Spellblades.DIMENSIONKEY;
import static java.lang.Math.*;
import static net.minecraft.entity.attribute.EntityAttributes.GENERIC_FOLLOW_RANGE;
import static net.spell_power.api.SpellSchools.*;

public class Magus extends HostileEntity implements InventoryOwner, GeoEntity {
    public PlayerEntity nemesis;
    public boolean isthinking = false;
    public UUID attackUUID = UUID.fromString("d25869f9-efad-494a-8b3a-777bff9443c5");
    public UUID healthUUID = UUID.fromString("9288df06-ae54-488e-8a43-a127b7922783");
    public boolean isScout = false;
    private boolean hasntthrownitems = true;
    private boolean firstattack = false;
    private boolean secondattack = false;
    private boolean isstopped = false;
    public static TrackedData<Integer> modifier;
    boolean isCaster = false;
    private PlayerEntity tradingplayer;
    private final ServerBossBar bossBar;

    public float damagetakensincelastthink = 0;
    int invisibletime = 0;
    public Vec3d speed = Vec3d.ZERO;
    private final SimpleInventory inventory = new SimpleInventory(8);
    private static final Set<Item> WANTED_ITEMS = ImmutableSet.of( Items.POTATO, Items.CARROT, Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT, Items.BEETROOT_SEEDS);
    public boolean returninghome = false;
    public boolean isleader = false;
    public boolean biding = false;
    public int bidingtime = 0;
    public int homecount = 0;
    public int homecount2 = 0;
    public PlayerEntity hero = null;
    public boolean casting = false;
    public boolean canGiveGifts = false;


    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    public static final RawAnimation ATTACK =  RawAnimation.begin().thenPlay("animation.hexblade.new");
    public static final RawAnimation SLASHONE =  RawAnimation.begin().then("animation.unknown.slashone", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation SLASHTWO =  RawAnimation.begin().then("animation.unknown.slashtwo", Animation.LoopType.PLAY_ONCE);

    public static final RawAnimation SLASHTHREE =  RawAnimation.begin().then("animation.unknown.slashthree", Animation.LoopType.PLAY_ONCE);

    public static final RawAnimation THROWONE =  RawAnimation.begin().then("animation.unknown.throwone", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation THROWTWO =  RawAnimation.begin().then("animation.unknown.throwtwo", Animation.LoopType.PLAY_ONCE);
    public static final RawAnimation RYUENJIN =  RawAnimation.begin().then("animation.unknown.ryuenjin", Animation.LoopType.PLAY_ONCE);

    public static final RawAnimation ATTACK2 =  RawAnimation.begin().thenPlay("animation.hexblade.new2");
    public static final RawAnimation WALK =  RawAnimation.begin().thenLoop("animation.unknown.walk");
    public static final RawAnimation WALK2 =  RawAnimation.begin().thenLoop("animation.unknown.walk");
    public static final RawAnimation FLYINGANIM =  RawAnimation.begin().thenLoop("animation.hexblade.dash");
    public static final RawAnimation FLOATINGANIM =  RawAnimation.begin().thenLoop("animation.model.floattowards");
    public static final RawAnimation RAISINGANIM =  RawAnimation.begin().thenLoop("animation.model.raise");
    public static final RawAnimation JUMPINGANIM =  RawAnimation.begin().thenLoop("animation.model.jump");
    public static final RawAnimation SPIN =  RawAnimation.begin().thenLoop("animation.model.floattowards2");
    public static final RawAnimation SMASH = RawAnimation.begin().thenLoop("animation.hexblade.whack");
    public List<Vec3d> positions = new ArrayList<>();

    public  List<Vec3d> getPositions(){
        return this.positions;
    }


    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.unknown.idle");
    public static final RawAnimation IDLE1 = RawAnimation.begin().thenLoop("animation.unknown.idle");
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY);
    protected static final ImmutableList<SensorType<? extends Sensor<? super Magus>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY);
    private boolean rising;
    private int risingtime = 0;
    private boolean dashing = false;
    public boolean spawnedfromitem = false;
    public int tier = 0;
    public static final TrackedData<Integer> TIER;
    public static final TrackedData<Integer> BIDINGTIME;

    public static final TrackedData<Boolean> FLOATING;
    public static final TrackedData<Boolean> FLYING;
    public static final TrackedData<Boolean> DOWN2;

    public static final TrackedData<Boolean> RAISING;
    public static final TrackedData<Boolean> JUMPING;
    public static final TrackedData<Boolean> BIDED;

    static {
        TIER = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.INTEGER);
        modifier = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.INTEGER);

        FLOATING = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);
        RAISING = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);
        FLYING = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);
        JUMPING = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);
        BIDED = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);
        BIDINGTIME = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.INTEGER);

        DOWN2 = DataTracker.registerData(Magus.class, TrackedDataHandlerRegistry.BOOLEAN);

    }

    private boolean specialattack = false;
    private int specialattacktime = 0;
    private boolean shotlightning = false;


    private int dashingtime = 0;

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TIER, 0);
        this.dataTracker.startTracking(BIDINGTIME, 0);

        this.dataTracker.startTracking(modifier, 0);

        this.dataTracker.startTracking(FLOATING, false);
        this.dataTracker.startTracking(FLYING, false);
        this.dataTracker.startTracking(RAISING, false);
        this.dataTracker.startTracking(JUMPING, false);
        this.dataTracker.startTracking(BIDED, false);
        this.dataTracker.startTracking(DOWN2, false);

    }
    public int experiencePoints = 500;

    @Override
    public int getXpToDrop() {
        return experiencePoints;
    }

    public void writeCustomDataToNbt(NbtCompound compoundTag) {


        compoundTag.putInt("Tier", (Integer) this.dataTracker.get(TIER));
        compoundTag.putInt("Bidingtime", (Integer) this.dataTracker.get(BIDINGTIME));

        compoundTag.putInt("Modifier", (Integer) this.dataTracker.get(modifier));

        compoundTag.putBoolean("Item1",this.spawnedfromitem);
        compoundTag.putBoolean("floating",this.dataTracker.get(FLOATING));
        compoundTag.putBoolean("flying",this.dataTracker.get(FLYING));

        compoundTag.putBoolean("raising",this.dataTracker.get(RAISING));
        compoundTag.putBoolean("jumping",this.dataTracker.get(JUMPING));
        compoundTag.putBoolean("biding",this.dataTracker.get(BIDED));
        compoundTag.putBoolean("down2",this.dataTracker.get(DOWN2));

        super.writeCustomDataToNbt(compoundTag);

    }


    public void readCustomDataFromNbt(NbtCompound compoundTag) {

        if (compoundTag.contains("Tier")) {
            this.dataTracker.set(TIER, compoundTag.getInt("Tier"));
        }
        if (compoundTag.contains("Bidingtime")) {
            this.dataTracker.set(BIDINGTIME, compoundTag.getInt("Bidingtime"));
        }
        if (compoundTag.contains("Modifier")) {
            this.dataTracker.set(modifier, compoundTag.getInt("Modifier"));
        }
        if (compoundTag.contains("flying")) {
            this.dataTracker.set(FLYING, compoundTag.getBoolean("flying"));
        }
        if (compoundTag.contains("raising")) {
            this.dataTracker.set(RAISING, compoundTag.getBoolean("raising"));
        }
        if (compoundTag.contains("floating")) {
            this.dataTracker.set(FLOATING, compoundTag.getBoolean("floating"));
        }
        if (compoundTag.contains("jumping")) {
            this.dataTracker.set(JUMPING, compoundTag.getBoolean("jumping"));
        }
        if (compoundTag.contains("biding")) {
            this.dataTracker.set(BIDED, compoundTag.getBoolean("biding"));
        }
        if (compoundTag.contains("down2")) {
            this.dataTracker.set(DOWN2, compoundTag.getBoolean("down2"));
        }
        if (compoundTag.contains("Item1")) {
            this.spawnedfromitem = compoundTag.getBoolean("Item1");
        }
        super.readCustomDataFromNbt(compoundTag);

    }
    @Override
    public void setEquipmentDropChance(EquipmentSlot equipmentSlot, float f) {
    }

    @Override
    public int getMaxLookYawChange() {
        return 9999999;
    }


    @Override
    protected float getDropChance(EquipmentSlot equipmentSlot) {
        return 0;
    }

    public int thinktime = 0;
    public Magus(EntityType<? extends Magus> p_34652_, World p_34653_) {
        super(p_34652_, p_34653_);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        double health = 600;
        double attackdamage = 8;

        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, health).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, attackdamage).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0.5).add(GENERIC_FOLLOW_RANGE,48);
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);

    }

    @Override
    public boolean isLeftHanded() {
        return true;
    }

    @Override
    public ItemStack getMainHandStack() {
        if(this.getDataTracker().get(TIER) % 3 == 0){
            return new ItemStack(com.spellbladenext.items.Items.arcane_blade.item());
        }
        if(this.getDataTracker().get(TIER) % 3 == 1){
            return new ItemStack(com.spellbladenext.items.Items.fire_blade.item());
        }
        if(this.getDataTracker().get(TIER) % 3 == 2){
            return new ItemStack(com.spellbladenext.items.Items.frost_blade.item());
        }
        return super.getMainHandStack();
    }

    @Nullable
    @Override
    public ItemEntity dropStack(ItemStack stack) {
        ItemEntity AIR = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), Items.AIR.getDefaultStack());

        if(this.getWorld().getRegistryKey() == Spellblades.DIMENSIONKEY) {
            return super.dropStack(stack);
        }
        else{
            if(stack.getItem()== com.spellbladenext.items.Items.STARFORGE.item()){
                return AIR;
            }
            else{
                if(this.getRandom().nextBoolean()){
                    return super.dropStack(stack);
                }
                    return AIR;
                }
        }
    }


    @Override
    public void applyDamage(DamageSource damageSource, float f) {
        if(damageSource.getAttacker() instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof DebugNetherPortal){
            super.applyDamage(damageSource, 999999);

            return;
        }

        if(this.age <= 10 && f < 999999){
            return;
        }
        if(damageSource.getAttacker() instanceof PlayerEntity player  ) {
            SpellSchool magicSchool = this.getMagicSchool();
            if (magicSchool.equals(FROST)) {
                this.dataTracker.set(modifier, this.dataTracker.get(modifier) + min((int) ceil(100 * SpellPower.getSpellPower(magicSchool,player).baseValue() / this.getMaxHealth()), (int) f));
            } else if (magicSchool.equals(FIRE)) {
                this.dataTracker.set(modifier, this.dataTracker.get(modifier) + min((int) ceil(100 * SpellPower.getSpellPower(magicSchool,player).baseValue() / this.getMaxHealth()), (int) f));
            } else if (magicSchool.equals(ARCANE)) {
                this.dataTracker.set(modifier, this.dataTracker.get(modifier) + min((int) ceil(100 * SpellPower.getSpellPower(magicSchool,player).baseValue() / this.getMaxHealth()), (int) f));
            }
            this.dataTracker.set(modifier, this.dataTracker.get(modifier) + min((int) ceil(100 *  ( player.getAttributeValue((SpellSchools.HEALING).attribute)) / this.getMaxHealth()),(int)f));

        }
        if(damageSource.getAttacker() instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(Enchantments.SMITE, living) > 0) {
            this.dataTracker.set(modifier, this.dataTracker.get(modifier)+EnchantmentHelper.getEquipmentLevel(Enchantments.SMITE, living));
        }

        double damagemodifier = min(1,0.05+(double)this.dataTracker.get(modifier)/100);
        super.applyDamage(damageSource, (float) (f*damagemodifier));
    }



    public SpellSchool getMagicSchool(){
        switch(this.getDataTracker().get(TIER) % 3){
            case 0 -> {
                return SpellSchools.ARCANE;
            }
            case 1 -> {
                return SpellSchools.FIRE;
            }
            case 2 -> {
                return SpellSchools.FROST;
            }
        }
        return SpellSchools.ARCANE;
    }

    @Override
    protected void updatePostDeath() {

        super.updatePostDeath();
    }
    public String getShortName(){
        return "Magus";
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.initCustomGoals();
    }
    protected void initCustomGoals() {

        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(0, new MagusAttackGoal<>(this, 1.0,false));
        this.goalSelector.add(1, new MagusThrowGoal<>(this, 1.0,false));
        this.goalSelector.add(2, new MagusDivebombGoal<>(this, 1.0,false));
        this.goalSelector.add(3, new MagusSwirlGoal<>(this, 1.0,false));

        this.goalSelector.add(10, new MagusFollowGoal(this,0.7));

        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, HostileEntity.class, true, entity -> !(entity instanceof Magus || entity instanceof Magister)));

    }
    @Override
    public void tick() {
        if(this.firstUpdate){
            if(this.getWorld().getRegistryKey() == Spellblades.DIMENSIONKEY) {
                boolean flag = false;
                if(this.getMaxHealth() == this.getHealth()){
                    flag = true;
                }
                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

                builder.put(EntityAttributes.GENERIC_MAX_HEALTH,new EntityAttributeModifier(healthUUID,"magushealth",0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,new EntityAttributeModifier(attackUUID,"magusattack",0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

                this.getAttributes().addTemporaryModifiers(builder.build());
                if(flag) {
                    this.setHealth(this.getMaxHealth());
                }
            }
        }
        super.tick();
        if(this.getTarget() != null && this.getTarget().isAlive()) {
        }
        this.positions.add(this.getPos());
        if(this.positions.size() > 8){
            this.positions.remove(0);
        }



       if (this.getWorld().getRegistryKey().equals(DIMENSIONKEY) && this.getY() < -32) {
            this.requestTeleport(this.getX(), 150, this.getZ());
        }
/*
        this.biding = this.getHealth() < this.getMaxHealth() / 2 && !this.getDataTracker().get(BIDED);
        if(this.getDataTracker().get(BIDED)){
            if(this.getServer() != null && this.getServer().getWorld(World.OVERWORLD) != null &&!this.getServer().getWorld(World.OVERWORLD).isThundering()){
                if(Spellblades.config.magusWeather) {

                    this.getServer().getWorld(World.OVERWORLD).setWeather(0, 12000, true, true);
                }

            }
        }
        if(this.getDataTracker().get(BIDINGTIME) >= 20*30){
            this.getDataTracker().set(BIDED,true);
            this.biding = false;
        }
        if(this.biding){
            if(this.getServer() != null && this.getServer().getWorld(World.OVERWORLD) != null &&!this.getServer().getWorld(World.OVERWORLD).isRaining()){
                if(Spellblades.config.magusWeather) {

                    this.getServer().getWorld(World.OVERWORLD).setWeather(0, 12000, true, false);
                }
            }
            this.getDataTracker().set(BIDINGTIME,this.getDataTracker().get(BIDINGTIME)+1);

            if(this.age % (20 * 5) == 0 && !this.getWorld().isClient()){
                Optional<HexbladePortal> frame = piglinsummon.summonNetherPortal(this.getWorld(),this,false);
                frame.ifPresent(hexbladePortal -> {
                    hexbladePortal.age = 60;
                    this.getWorld().spawnEntity(hexbladePortal);
                });
            }

        }
        if(this.specialattack){
            if(this.specialattacktime == 10 && !this.getWorld().isClient() && !this.shotlightning){
                this.shotlightning = true;
                LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT,this.getWorld());
                bolt.setPosition(this.getPos());
                this.getWorld().spawnEntity(bolt);
                this.getDataTracker().set(RAISING,false);
            }
            this.specialattacktime++;

        }
        if(this.isOnGround()){
            this.getDataTracker().set(JUMPING,false);
        }*/
        if(this.age % 5 == 0 && !this.getWorld().isClient){
            List<PlayerEntity> players = this.getWorld().getPlayers(TargetPredicate.createNonAttackable(),this,this.getBoundingBox().expand(32));

            players.forEach(player -> {
                player.addStatusEffect(new StatusEffectInstance(Spellblades.PORTALSICKNESS,160,0,false,false));
                String string = "null";
                Formatting chatFormatting = Formatting.GRAY;
                if(getMagicSchool() == SpellSchools.ARCANE){
                    string = "ARCANE";
                    chatFormatting = Formatting.DARK_PURPLE;

                }
                if(getMagicSchool() == SpellSchools.FROST){
                    string = "FROST";
                    chatFormatting = Formatting.AQUA;

                }
                if(getMagicSchool() == SpellSchools.FIRE){
                    string = "FIRE";
                    chatFormatting = Formatting.RED;

                }
                player.sendMessage(Text.translatable(getShortName()+"' Barrier is weak to " + string + " power. Barrier Strength: "+ max(0,(95-this.getDataTracker().get(modifier))) + "%.").formatted(chatFormatting), true);
            });
        }

        if (this.age % 5 == 0 && this.getWorld() instanceof ServerWorld level) {
            List<BoatEntity> boatEntities = this.getWorld().getEntitiesByClass(BoatEntity.class,this.getBoundingBox().expand(16), (asdf) -> true);
            for(BoatEntity boat: boatEntities){
                boat.getWorld().createExplosion(this,boat.getX(),boat.getY(),boat.getZ(),4, World.ExplosionSourceType.NONE);
                if(boat.isAlive()){
                    boat.discard();
                }
            }

        }
   /*
        if (isInvisible()) {
            if (invisibletime > 60) {
                if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                    Vec3d vec3 = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get().getPos().add(new Vec3d(1 - 2 * random.nextFloat(), 0, 1 - 2 * random.nextFloat()).normalize());
                    this.requestTeleport(vec3.x, vec3.y, vec3.z);
                } else if (this.getWorld().getClosestPlayer(this, 32) != null) {
                    Vec3d vec3 = this.getWorld().getClosestPlayer(this, 32).getPos().add(new Vec3d(1 - 2 * random.nextFloat(), 0, 1 - 2 * random.nextFloat()).normalize());
                    this.requestTeleport(vec3.x, vec3.y, vec3.z);
                }
            }
            invisibletime++;
        }
        if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
            this.lookAtEntity(this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get(), 999, 999);
            if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get().isInRange(this, 2)) {
                this.setInvisible(false);
                invisibletime = 0;
                if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get().isInRange(this, 1)) {
                    this.getDataTracker().set(FLYING,false);

                    this.dashing = false;
                    this.casting = false;
                    this.noClip = false;
                }
            }
        }

        if (this.casting && getMagicSchool() == SpellSchools.ARCANE && this.isOnGround()) {
            this.setInvisible(true);
            this.casting = false;
        }
        if (this.casting && getMagicSchool() == SpellSchools.FIRE && this.isOnGround() && !this.rising) {
            this.rising = true;
            this.setNoGravity(true);
            this.risingtime = 0;
            this.getDataTracker().set(RAISING,true);
        }
        if (this.rising) {

            if (this.risingtime < 20) {
                this.setVelocity(0, 0.3, 0);

            }
            if (this.risingtime == 20 && this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                this.speed = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get().getPos().subtract(this.getPos()).normalize();
                this.getDataTracker().set(RAISING,false);
                this.getDataTracker().set(FLOATING,true);

                this.setVelocity(speed);

            } else if (this.risingtime == 20 && this.getWorld().getClosestPlayer(this, 32) != null) {
                this.speed = this.getWorld().getClosestPlayer(this, 32).getPos().subtract(this.getPos()).normalize();
                this.getDataTracker().set(RAISING,false);
                this.getDataTracker().set(FLOATING,true);

                this.setVelocity(speed);
            }
            if (this.risingtime > 20) {
                this.setVelocity(speed);
            }
            if ((this.isOnGround() && this.risingtime > 20) || this.risingtime > 40) {
                Spell spell = SpellRegistry.getSpell(new Identifier(Spellblades.MOD_ID, "magus_firenova"));
                if(!this.getWorld().isClient()) {
                    ParticleHelper.sendBatches(this, spell.release.particles);
                }

                List<Entity> entities = this.getWorld().getEntitiesByClass(Entity.class,this.getBoundingBox().expand(4,2,4),entity -> entity != this);
                for(Entity entity : entities){
                    entity.damage(SpellDamageSource.mob(SpellSchools.FIRE,this),(float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                }
                this.casting = false;
                this.setNoGravity(false);
                this.rising = false;
                this.risingtime = 0;
                this.getDataTracker().set(RAISING,false);
                this.getDataTracker().set(FLOATING,false);

            }
            this.risingtime++;
        } else {
            this.setNoGravity(false);
        }
        if (this.casting && this.isOnGround() && this.getMagicSchool() == SpellSchools.FROST) {
            this.noClip = true;
            this.dashing = true;
        }
        if (this.dashing) {
            this.getDataTracker().set(FLYING,true);

            this.noClip = true;
            if (this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent()) {
                this.speed = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get().getBoundingBox().getCenter().subtract(this.getPos()).normalize();
            } else if (this.getWorld().getClosestPlayer(this, 32) != null) {
                this.speed = this.getWorld().getClosestPlayer(this, 32).getBoundingBox().getCenter().subtract(this.getPos()).normalize();

            }
            this.setVelocity(speed);
            this.dashingtime++;
        }
        if (this.dashingtime > 40) {
            this.getDataTracker().set(FLYING,false);

            this.dashing = false;
            this.casting = false;
            this.noClip = false;
            this.dashingtime = 0;
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
                if (entity.damage(SpellDamageSource.mob(getMagicSchool(), this), (float)((float) 2F* this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F))) {
                    entity.timeUntilRegen = 0;
                    entity.damage(this.getDamageSources().mobAttack(this), (float) ( 1F * this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F));
                }
            }
            this.specialattack = false;
            this.specialattacktime = 0;
        }
        if (this.isthinking) {
            thinktime++;
        }
        if (this.thinktime > 40) {
            this.isthinking = false;
            this.thinktime = 0;
        }*/
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        SpellSchool magicSchool = this.getMagicSchool();
        if (magicSchool.equals(ARCANE)) {
            this.bossBar.setColor(BossBar.Color.PURPLE);
        } else if (magicSchool.equals(FROST)) {
            this.bossBar.setColor(BossBar.Color.WHITE);
        } else if (magicSchool.equals(FIRE)) {
            this.bossBar.setColor(BossBar.Color.RED);
        }

    }


    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        // builder.putAll(super.getAttributeModifiers(this.slot));


    }


    @Override
    public boolean tryAttack(Entity entity) {

        if(this.getDataTracker().get(BIDED)){
            if (entity.damage(SpellDamageSource.mob(getMagicSchool(), this), (float)((float) 2F* this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F))) {
                entity.timeUntilRegen = 0;
                entity.damage(this.getWorld().getDamageSources().mobAttack(this), (float) ( 1F * this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 3F));
            }
            if(this.age % 160 < 80){
                this.setVelocity(this.getVelocity().multiply(-1));
            }
            else if (this.getRandom().nextBoolean()){
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT,1.0F,1.0F);
                this.teleportRandomly();
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT,1.0F,1.0F);

            }
        }
        return false;
    }
    protected void teleportRandomly() {
        if (!this.getWorld().isClient() && this.isAlive()) {
            int a = -1;
            int b = -1;
            int c = -1;
            if(this.getRandom().nextBoolean()){
                a = 1;
            }
            if(this.getRandom().nextBoolean()){
                b = 1;
            }
            if(this.getRandom().nextBoolean()){
                c = 1;
            }
            int posX = this.getBlockX()  + a*this.getRandom().nextInt(8 ) + a*4;
            int posY = this.getBlockY();
            int posZ = this.getBlockZ()  + c* this.getRandom().nextInt(8) +  c * 4;
            if(this.getWorld().isSkyVisible(new BlockPos(posX,posY,posZ).up())) {
                this.teleport(posX, posY + 1, posZ);
            }
        }

    }





    @Override
    public void tickMovement() {
        super.tickMovement();
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
            if(this.getRandom().nextInt(4)==0 && !this.specialattack && this.getDataTracker().get(BIDED)){
                this.specialattack = true;
                this.getDataTracker().set(RAISING,true);
                this.shotlightning = false;
                return;
            }

            this.handSwinging = true;
            this.preferredHand = interactionHand;
            if (this.getWorld() instanceof ServerWorld) {
                EntityAnimationS2CPacket clientboundAnimatePacket = new EntityAnimationS2CPacket(this, interactionHand == Hand.MAIN_HAND ? 0 : 3);
                ServerChunkManager serverChunkCache = ((ServerWorld) this.getWorld()).getChunkManager();
                if (bl) {
                    serverChunkCache.sendToNearbyPlayers(this, clientboundAnimatePacket);
                } else {
                    serverChunkCache.sendToOtherNearbyPlayers(this, clientboundAnimatePacket);
                }
            }
        }

    }
    @Override
    protected void mobTick() {
        if(!this.biding) {
            MagusAI.updateActivity(this);
        }
        super.mobTick();

    }



    private PlayState predicate2(mod.azure.azurelib.core.animation.AnimationState<Magus> state) {
        if (this.getDataTracker().get(FLYING)) {
            return state.setAndContinue(FLOATINGANIM);

        }

        if(state.isMoving()){
            if(this.isAttacking()){
                return state.setAndContinue(WALK2);


            }
            return state.setAndContinue(WALK);


        }
        if(this.getTarget() != null) {
            return state.setAndContinue(IDLE1);

        }
        return state.setAndContinue(IDLE);

    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<Magus>(this,"walk",0,this::predicate2)

        );
        controllers.add(
                new AnimationController<>(this, "slashone", event -> PlayState.CONTINUE)
                        .triggerableAnim("slashone", SLASHONE));
        controllers.add(
                new AnimationController<>(this, "slashtwo", event -> PlayState.CONTINUE)
                        .triggerableAnim("slashtwo", SLASHTWO));
        controllers.add(
                new AnimationController<>(this, "slashthree", event -> PlayState.CONTINUE)
                        .triggerableAnim("slashthree", SLASHTHREE));
        controllers.add(
                new AnimationController<>(this, "ryuenjin", event -> PlayState.CONTINUE)
                        .triggerableAnim("ryuenjin", RYUENJIN));
        controllers.add(
                new AnimationController<>(this, "throwone", event -> PlayState.CONTINUE)
                        .triggerableAnim("throwone", THROWONE));
        controllers.add(
                new AnimationController<>(this, "throwtwo", event -> PlayState.CONTINUE)
                        .triggerableAnim("throwtwo", THROWTWO));




    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
    public SimpleInventory getInventory() {
        return this.inventory;
    }

}
