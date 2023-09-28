package com.spellbladenext.client.entity;

import com.spellbladenext.entity.HexbladePortal;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class HexbladePortalRenderer extends GeoEntityRenderer<HexbladePortal> {

    public HexbladePortalRenderer(EntityRendererFactory.Context context) {
        super(context, new PortalModel());
    }
}