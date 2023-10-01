package com.spellbladenext.items.armor;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.renderer.MagisterArmorItemRenderer;
import com.spellbladenext.client.item.renderer.MagisterArmorRenderer;
import com.spellbladenext.client.item.renderer.MagusArmorItemRenderer;
import com.spellbladenext.client.item.renderer.MagusArmorRenderer;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MagusArmor extends CustomArmor implements GeoItem {

    public MagusArmor(Armor.CustomMaterial material, Type type, Settings settings, List<MagicSchool> magicSchool) {
        super(material, type, settings);
        this.magicschool.addAll(magicSchool);

    }
    private final List<MagicSchool> magicschool = new ArrayList<>();

    // MARK: GeoItem
    public List<MagicSchool> getMagicschool() {
        return magicschool;
    }
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private MagusArmorRenderer renderer;
            private MagusArmorItemRenderer renderer1;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new MagusArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (renderer1 == null) return new MagusArmorItemRenderer();
                return this.renderer1;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
    @Override
    public void inventoryTick(ItemStack itemStack, World level, Entity entity, int i, boolean bl) {
        if(!level.isClient()) {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;

                int amount = 0;
                if(player.getInventory().getArmorStack(0).getItem() instanceof MagusArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(1).getItem() instanceof MagusArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(2).getItem() instanceof MagusArmor){
                    amount++;
                }
                if(player.getInventory().getArmorStack(3).getItem() instanceof MagusArmor){
                    amount++;
                }
                int ii = 0;
                if(player.hasStatusEffect(Spellblades.RunicAbsorption)){
                    ii = player.getStatusEffect(Spellblades.RunicAbsorption).getAmplifier();
                }
                if(amount > 0 && !player.hasStatusEffect(Spellblades.RunicAbsorption) && player.getAbsorptionAmount() <= 0)
                    player.addStatusEffect(new StatusEffectInstance(Spellblades.RunicAbsorption,20*5,amount+1, false, false));

            }
        }
        super.inventoryTick(itemStack, level, entity, i, bl);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
