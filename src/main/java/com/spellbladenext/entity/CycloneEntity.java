package com.spellbladenext.entity;

import com.google.common.base.Predicates;
import com.spellbladenext.Spellblades;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.TargetHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static com.spellbladenext.Spellblades.MOD_ID;

public class CycloneEntity extends Entity implements GeoEntity, Ownable {

    public static final RawAnimation SPINNING = RawAnimation.begin().thenLoop("animation.model.spin");
    private Entity owner;
    @Nullable
    private int ownerUuid;
    public Entity target;
    public SpellHelper.ImpactContext context;
    public CycloneEntity(EntityType<? extends CycloneEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
        this.noClip = true;
    }

    public static final TrackedData<Integer> COLOR;
    public static final TrackedData<Integer> OWNER;

    static {
        COLOR = DataTracker.registerData(CycloneEntity.class, TrackedDataHandlerRegistry.INTEGER);
        OWNER = DataTracker.registerData(CycloneEntity.class, TrackedDataHandlerRegistry.INTEGER);

    }


    protected void initDataTracker() {
        this.dataTracker.startTracking(COLOR, 1);
        this.dataTracker.startTracking(OWNER, -1);

    }

    public int getColor(){
        return this.dataTracker.get(COLOR);
    }
    public void setColor(int color){
         this.dataTracker.set(COLOR,color);
    }
    @Override
    public void tick() {
        if(this.getOwner() != null && !this.getWorld().isClient) {


        }
        else{
            if(!this.getWorld().isClient) {
                this.discard();
            }
        }
        super.tick();
        this.setPosition(this.getPos().add(this.getVelocity()));
        if(this.getOwner() instanceof SpellCasterEntity caster){
            if(this.getColor() != 5) {
                this.setPos(this.getOwner().getX(), this.getOwner().getY(), this.getOwner().getZ());
                if (caster.getCurrentSpell() != null && !caster.getCurrentSpell().equals(SpellRegistry.getSpell(new Identifier(MOD_ID, "whirlwind")))
                        && !caster.getCurrentSpell().equals(SpellRegistry.getSpell(new Identifier(MOD_ID, "reckoning")))
                        && !caster.getCurrentSpell().equals(SpellRegistry.getSpell(new Identifier(MOD_ID, "inferno")))
                        && !caster.getCurrentSpell().equals(SpellRegistry.getSpell(new Identifier(MOD_ID, "maelstrom")))
                        && !caster.getCurrentSpell().equals(SpellRegistry.getSpell(new Identifier(MOD_ID, "tempest")))) {
                    if (!this.getWorld().isClient()) {
                        this.discard();
                    }

                } else if (caster.getCurrentSpell() == null) {
                    if (!this.getWorld().isClient()) {
                        this.discard();
                    }
                }
            }
            else{
                if(this.target != null) {
                    Vec3d vec3d = this.target.getPos().subtract(this.getPos());
                    if (this.getWorld().isClient) {
                        this.lastRenderY = this.getY();
                    }
                    double d = 0.05D;
                    this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3d.normalize().multiply(d)));
                }
                if (this.age % 10 == 0 && this.getOwner() instanceof LivingEntity living) {

                    List<LivingEntity> list = this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox(), Entity::isAlive);
                    for (LivingEntity entity : list) {
                        Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID, "bladestorm"));
                        if (spell != null  && this.context != null) {
                            if(TargetHelper.actionAllowed(TargetHelper.TargetingMode.AREA, TargetHelper.Intent.HARMFUL,living,entity) || (this.target != null && this.target == entity)) {
                                SpellHelper.performImpacts(entity.getWorld(), living, entity, this.getOwner(), spell,
                                        this.context, false);
                            }
                        }
                    }
                }
                if(this.age > 160 && !this.getWorld().isClient()){
                    this.discard();
                }
            }
        }
    }

    @Override
    public boolean canHit() {
        return false;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        animationData.add(new AnimationController<>(this, "fly",
                0, this::predicate2)
        );
    }
    private <E extends GeoAnimatable> PlayState predicate2(AnimationState<E> event) {

            return event.setAndContinue(SPINNING);
    }
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getId();
            this.owner = entity;
            this.dataTracker.set(OWNER,entity.getId());
        }
    }
    @Nullable
    public Entity getOwner() {
        if(this.dataTracker.get(OWNER) != -1) {
            return this.getWorld().getEntityById(this.dataTracker.get(OWNER));
        }else{
            return null;
        }
    }
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Color", (Integer) this.dataTracker.get(COLOR));
            nbt.putInt("Owner", this.ownerUuid);


    }
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Color")) {
            this.dataTracker.set(COLOR, nbt.getInt("Color"));
        }

        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getInt("Owner");
            this.owner = null;
        }

    }
}
