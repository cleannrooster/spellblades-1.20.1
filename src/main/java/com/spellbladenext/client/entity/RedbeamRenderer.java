package com.spellbladenext.client.entity;

import com.spellbladenext.entity.CycloneEntity;
import com.spellbladenext.entity.RedLaserEntity;
import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class RedbeamRenderer<T extends RedLaserEntity> extends GeoEntityRenderer<RedLaserEntity> {

    public RedbeamRenderer(EntityRendererFactory.Context context) {
        super(context, new RedLaserModel<>());
        this.scaleHeight = 1.1F;
        this.scaleWidth = 1.1F;
    }

}