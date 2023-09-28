package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.HexbladePortal;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class PortalModel<T extends HexbladePortal> extends GeoModel<HexbladePortal> {

    @Override
    public Identifier getModelResource(HexbladePortal reaver) {

        return new Identifier(Spellblades.MOD_ID,"geo/portal.geo.json");
    }
    @Override
    public Identifier getTextureResource(HexbladePortal reaver) {
        return new Identifier(Spellblades.MOD_ID,"textures/mob/ender_eye.png");
    }

    @Override
    public Identifier getAnimationResource(HexbladePortal reaver) {
        return new Identifier(Spellblades.MOD_ID,"animations/portal.animation.json");
    }

}
