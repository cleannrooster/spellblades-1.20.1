package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.CycloneEntity;
import com.spellbladenext.entity.HexbladePortal;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class CycloneModel<T extends CycloneEntity> extends GeoModel<CycloneEntity> {

    @Override
    public Identifier getModelResource(CycloneEntity reaver) {

        return new Identifier(Spellblades.MOD_ID,"geo/cyclone.json");
    }
    @Override
    public Identifier getTextureResource(CycloneEntity reaver) {
        if(reaver.getColor() == 1) {
            return new Identifier(Spellblades.MOD_ID, "textures/mob/whirlwind.png");
        }
        else if (reaver.getColor() == 2 || reaver.getColor() == 5){
            return new Identifier(Spellblades.MOD_ID, "textures/mob/maelstrom.png");
        }
        else if (reaver.getColor() == 3){
            return new Identifier(Spellblades.MOD_ID, "textures/mob/tempest.png");
        }
        else if (reaver.getColor() == 4){
            return new Identifier(Spellblades.MOD_ID, "textures/mob/inferno.png");
        }
        return new Identifier(Spellblades.MOD_ID, "textures/mob/whirlwind.png");

    }

    @Override
    public Identifier getAnimationResource(CycloneEntity reaver) {
        return new Identifier(Spellblades.MOD_ID,"animations/cyclone.animation.json");
    }

}
