package com.spellbladenext.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.items.Items;
import com.spellbladenext.items.SpellbladeItems;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class HexbladePortal extends LivingEntity implements GeoEntity {
    public PlayerEntity owner;
    public boolean spawn = true;
    public boolean firstPiglin = true;
    public boolean ishome = false;
    public int hometicks = 0;
    public boolean goinghome = false;
    public BlockPos origin = BlockPos.ORIGIN;
    public int experiencePoints = 160;

    public HexbladePortal(EntityType<HexbladePortal> entityType, World world) {
        super(Spellblades.HEXBLADEPORTAL, world);
    }
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    public static final RawAnimation FLYINGANIM = RawAnimation.begin().thenPlay("animation.model.dilate");
    public static final RawAnimation IDLEANIM = RawAnimation.begin().thenPlayAndHold("animation.model.idle");

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean canTakeDamage() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }


    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        return super.interact(player, hand);
    }

    @Override
    public boolean isCollidable() {
        return false;
    }



    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getXpToDrop() {
        return experiencePoints;
    }

    @Override
    public void equipStack(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }
    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,1);
    }    @Override
    public void tick() {
        if(firstUpdate){
            SoundEvent soundEvent = SoundEvents.ENTITY_BLAZE_SHOOT;
            this.playSound(soundEvent, 1F, 0.5F);
        }
        for(int i = 0; i < 8; ++i) {
            double d = (double)this.getX() + random.nextDouble();
            double e = (double)this.getY() + random.nextDouble();
            double f = (double)this.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;

            f = (double)this.getZ() + 0.5D + 0.25D * (double)k;
            j = (double)(random.nextFloat() * 2.0F * (float)k);

            getWorld().addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }
        for(int i = 0; i < 8; ++i) {
            double d = (double)this.getX() + random.nextDouble();
            double e = (double)this.getY() + random.nextDouble();
            double f = (double)this.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;
            int k = random.nextInt(2) * 2 - 1;

            d = (double)this.getX() + 0.5D + 0.25D * (double)k;
            g = (double)(random.nextFloat() * 2.0F * (float)k);

            getWorld().addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }

        this.setNoGravity(true);
        this.noClip = true;
        if (this.spawn && this.age < 100 && this.age % 10 == 5 && !this.ishome) {
            Magister piglin = new Magister(Spellblades.REAVER, this.getWorld());
            piglin.tryEquip(new ItemStack(List.of(Items.arcane_blade.item(),Items.frost_blade.item(),Items.fire_blade.item()).get(getRandom().nextInt(3))));
            piglin.setPosition(this.getPos());
            if (firstPiglin) {
                piglin.isleader = true;
            }
            if (this.random.nextInt(5) < 2) {
                piglin.isCaster = true;
            }


            this.getWorld().spawnEntity(piglin);
            firstPiglin = false;
        }
        if (age > 240) {
            this.getWorld().playSound((PlayerEntity)null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            if( !this.getWorld().isClient()) {
                this.discard();
            }
        }




        super.tick();
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }



    @Override
    public void takeKnockback(double d, double e, double f) {
    }

    private <E extends GeoAnimatable> PlayState predicate2(AnimationState<E> event) {

        if(firstUpdate) {
            return event.setAndContinue(FLYINGANIM);
        }

        return PlayState.CONTINUE;

    }
    private <E extends GeoAnimatable> PlayState predicate3(AnimationState<E> event) {

        return event.setAndContinue(IDLEANIM);


    }
        @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
            animationData.add(new AnimationController<>(this, "fly",
                    0, this::predicate2)
            );
            animationData.add(new AnimationController<>(this, "idle",
                    0, this::predicate3)
            );
        }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

}
