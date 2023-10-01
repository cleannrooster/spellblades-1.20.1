package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Archmagus;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.renderer.DynamicGeoEntityRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.spell_power.SpellPowerMod;
import net.spell_power.api.MagicSchool;

public class ArchmagusRenderer<T extends Archmagus, M extends BipedEntityModel<T>> extends DynamicGeoEntityRenderer<Archmagus> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_fire.png");
    private static final Identifier FIRE = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_fire.png");
    private static final Identifier FROST = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_frost.png");
    private static final Identifier ARCANE = new Identifier(Spellblades.MOD_ID,"textures/mob/archmagus_arcane.png");


    public ArchmagusRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ArchmagusModel<>());
        addRenderLayer(new RenderLayerItemArchmagus(this));

        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,renderManager.getItemInHandRenderer()));
    }



    public Identifier getTextureLocation(Archmagus p_114891_) {
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


}