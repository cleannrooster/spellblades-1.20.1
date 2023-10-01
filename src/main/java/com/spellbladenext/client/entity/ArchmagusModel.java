package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Archmagus;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class ArchmagusModel<T extends ArchmagusModel> extends GeoModel<Archmagus> implements ModelWithArms {
    private static final Identifier DEFAULT_LOCATION = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_fire.png");
    private static final Identifier FIRE = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_fire.png");
    private static final Identifier FROST = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_frost.png");
    private static final Identifier ARCANE = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_arcane.png");


    @Override
    public Identifier getModelResource(Archmagus reaver) {
        return new Identifier(Spellblades.MOD_ID,"geo/archmagus.geo.json");
    }
    public Identifier getTextureResource(Archmagus p_114891_) {
        switch(p_114891_.getMagicSchool()){
            case ARCANE -> {
                return ARCANE;
            }
            case FIRE -> {
                return FIRE;
            }
            case FROST -> {
                return FROST;
            }
        }
        return DEFAULT_LOCATION;
    }

    @Override
    public Identifier getAnimationResource(Archmagus reaver) {
        return new Identifier(Spellblades.MOD_ID,"animations/magus.animation.json");
    }
    public void setArmAngle(Arm humanoidArm, MatrixStack poseStack) {
        this.translateAndRotate(poseStack);
    }
    public void translateAndRotate(MatrixStack arg) {
        arg.translate((double)(1), (double)(0 / 16.0F), (double)(0 / 16.0F));
        arg.scale(2, 2, 2);



    }
}
