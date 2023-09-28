package com.spellbladenext.items.armor;

import com.spellbladenext.client.item.renderer.MagisterArmorItemRenderer;
import com.spellbladenext.client.item.renderer.MagisterArmorRenderer;
import com.spellbladenext.client.item.renderer.OrbRenderer;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MagisterArmor extends CustomArmor implements GeoItem {

    public MagisterArmor(Armor.CustomMaterial material, ArmorItem.Type type, Item.Settings settings, List<MagicSchool> magicSchool) {
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
            private MagisterArmorRenderer renderer;
            private MagisterArmorItemRenderer renderer1;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new MagisterArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (renderer1 == null) return new MagisterArmorItemRenderer();
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
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
