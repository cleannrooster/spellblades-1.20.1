package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.renderer.OrbRenderer;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.spell_power.api.MagicSchool;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Arc2D;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Orb extends SpellWeaponItem implements GeoItem {
    public Orb(ToolMaterial material, Settings settings, MagicSchool school) {
        super(material, settings);
        this.school = school;

    }
    MagicSchool school = MagicSchool.PHYSICAL_MELEE;
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private OrbRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (renderer == null) return new OrbRenderer();
                return this.renderer;
            }
        });
    }
    private AnimatableInstanceCache factory = AzureLibUtil.createInstanceCache(this);
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    public MagicSchool getSchool() {
        return school;
    }
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return state.isIn(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    public boolean isSuitableFor(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Orb>(this,"idle",
                0,animationState -> {
                animationState.getController().setAnimation(RawAnimation.begin().then(
                        "idle", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;})
        );
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("Orbweaver's Grace").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("Move normally while casting").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
}
