package com.spellbladenext.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.renderer.MagisterArmorItemRenderer;
import com.spellbladenext.client.item.renderer.MagisterArmorRenderer;
import com.spellbladenext.client.item.renderer.OrbRenderer;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.spell_engine.api.item.ConfigurableAttributes;
import net.spell_engine.api.item.armor.Armor;
import net.spell_power.api.MagicSchool;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.extraspellattributes.ReabsorptionInit.WARDING;

public class MagisterArmor extends CustomArmor implements GeoItem {

    public MagisterArmor(Armor.CustomMaterial material, ArmorItem.Type type, Item.Settings settings, List<MagicSchool> magicSchool) {
        super(material, type, settings);
        this.magicschool.addAll(magicSchool);

    }
    private Multimap<EntityAttribute, EntityAttributeModifier> attributes;



    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (attributes == null) {
            return super.getAttributeModifiers(slot);
        }
        return slot == this.type.getEquipmentSlot() ? this.attributes : super.getAttributeModifiers(slot);
    }
    private static final EnumMap<Type, UUID> MODIFIERS = (EnumMap) Util.make(new EnumMap(ArmorItem.Type.class), (uuidMap) -> {
        uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    private final List<MagicSchool> magicschool = new ArrayList<>();

    @Override
    public void setAttributes(Multimap<EntityAttribute, EntityAttributeModifier> attributes) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        // builder.putAll(super.getAttributeModifiers(this.slot));
        builder.putAll(attributes);
        UUID uuid = (UUID)MODIFIERS.get(this.type);
        builder.put(WARDING,new EntityAttributeModifier(uuid,"warding",3, EntityAttributeModifier.Operation.ADDITION));
        this.attributes = builder.build();
    }

    // MARK: GeoItem
    public List<MagicSchool> getMagicschool() {
        return magicschool;
    }
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public TypedActionResult<ItemStack> equipAndSwap(Item item, World world, PlayerEntity user, Hand hand) {
        if(user instanceof PlayerDamageInterface damageInterface){
            damageInterface.resetDamageAbsorbed();
        }
        return super.equipAndSwap(item, world, user, hand);
    }

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
