package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Magus;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagusModel<T extends MagusModel> extends GeoModel<Magus> implements ModelWithArms {
    private static final Identifier DEFAULT_LOCATION = new Identifier(Spellblades.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = new Identifier(Spellblades.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = new Identifier(Spellblades.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = new Identifier(Spellblades.MOD_ID,"textures/mob/magus_arcane.png");


    @Override
    public Identifier getModelResource(Magus reaver) {
        return new Identifier(Spellblades.MOD_ID,"geo/archmagus.geo.json");
    }
    public Identifier getTextureResource(Magus p_114891_) {
        if(p_114891_.getMainHandStack().getItem() instanceof Spellblade spellblade){
            if(spellblade.getSchool().equals(SpellSchools.FIRE)){
                return FIRE;
            }
            if(spellblade.getSchool().equals(SpellSchools.FROST)){
                return FROST;
            }
            if(spellblade.getSchool().equals(SpellSchools.ARCANE)){
                return ARCANE;
            }
        }
        return DEFAULT_LOCATION;
    }

    @Override
    public Identifier getAnimationResource(Magus reaver) {
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
