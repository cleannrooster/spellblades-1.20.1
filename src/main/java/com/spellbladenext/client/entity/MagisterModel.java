package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Magister;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.SpellSchools;

public class MagisterModel<T extends Magister> extends GeoModel<Magister> implements ModelWithArms {
    private static final Identifier DEFAULT_LOCATION = new Identifier(Spellblades.MOD_ID,"textures/mob/arcanehexblade.png");
    private static final Identifier FIRE = new Identifier(Spellblades.MOD_ID,"textures/mob/firehexblade.png");
    private static final Identifier FROST = new Identifier(Spellblades.MOD_ID,"textures/mob/frosthexblade.png");
    private static final Identifier ARCANE = new Identifier(Spellblades.MOD_ID,"textures/mob/arcanehexblade.png");

    @Override
    public Identifier getModelResource(Magister reaver) {
        if(reaver.getMainHandStack().getItem() instanceof Spellblade spellblade){
            if(spellblade.getSchool().equals(SpellSchools.FIRE)){
                return new Identifier(Spellblades.MOD_ID,"geo/firehexblade.json");
            }
            if(spellblade.getSchool().equals(SpellSchools.FROST)){
                return new Identifier(Spellblades.MOD_ID,"geo/frosthexblade.geo.json");
            }
            if(spellblade.getSchool().equals(SpellSchools.ARCANE)){
                return new Identifier(Spellblades.MOD_ID,"geo/arcanehexblade.geo.json");
            }
        }
        return new Identifier(Spellblades.MOD_ID,"geo/arcanehexblade.geo.json");
    }
    @Override
    public Identifier getTextureResource(Magister reaver) {
        if(reaver.getMainHandStack().getItem() instanceof Spellblade spellblade){
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
    public Identifier getAnimationResource(Magister reaver) {
        return new Identifier(Spellblades.MOD_ID,"animations/hexblade.animation.json");
    }
    public void setArmAngle(Arm humanoidArm, MatrixStack poseStack) {
        this.translateAndRotate(poseStack);
    }
    public void translateAndRotate(MatrixStack arg) {
        arg.translate((double)(1), (double)(0 / 16.0F), (double)(0 / 16.0F));
        arg.scale(2, 2, 2);



    }
}
