package com.spellbladenext.client.entity;

import com.spellbladenext.entity.CycloneEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class CycloneRenderer<T extends CycloneEntity> extends GeoEntityRenderer<CycloneEntity> {

    public CycloneRenderer(EntityRendererFactory.Context context) {
        super(context, new CycloneModel<>());

    }

    @Override
    public void preRender(MatrixStack poseStack, CycloneEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(animatable.getOwner() != null ){

            if(animatable.getColor() != 5){
                this.scaleWidth = animatable.getOwner().getHeight() * 1.75F;
                this.scaleHeight = animatable.getOwner().getHeight() * 1.5F;

                poseStack.translate(-animatable.lastRenderX,-animatable.lastRenderY,-animatable.lastRenderZ);
                poseStack.translate(animatable.getOwner().lastRenderX,animatable.getOwner().lastRenderY,animatable.getOwner().lastRenderZ);

            }
            else{
                this.scaleWidth = 2 * 1.75F;
                this.scaleHeight = 2 * 1.5F;

            }

        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
    @Override
    public boolean shouldRender(CycloneEntity entity, Frustum frustum, double x, double y, double z) {
        if(entity.getColor() != 5 && MinecraftClient.getInstance() != null && MinecraftClient.getInstance().cameraEntity != null &&  entity.getOwner() != null && MinecraftClient.getInstance().cameraEntity.equals(entity.getOwner()) && MinecraftClient.getInstance().options.getPerspective().isFirstPerson()){
            return false;
        }
        else{
            return super.shouldRender(entity,frustum,x,y,z);
        }
    }
}