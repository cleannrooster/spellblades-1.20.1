package com.spellbladenext.client.entity;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Magus;
import com.spellbladenext.items.Spellblade;
import mod.azure.azurelib.renderer.DynamicGeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagusRenderer<T extends Magus, M extends BipedEntityModel<T>> extends DynamicGeoEntityRenderer<Magus> {

    private static final Identifier DEFAULT_LOCATION = new Identifier(Spellblades.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FIRE = new Identifier(Spellblades.MOD_ID,"textures/mob/magus.png");
    private static final Identifier FROST = new Identifier(Spellblades.MOD_ID,"textures/mob/magus_frost.png");
    private static final Identifier ARCANE = new Identifier(Spellblades.MOD_ID,"textures/mob/magus_arcane.png");


    public MagusRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MagusModel<>());
        addRenderLayer(new RenderLayerItemMagus(this));

        //this.layerRenderers.add((GeoLayerRenderer<Reaver>) new GeoitemInHand<T,M>((IGeoRenderer<T>) this,renderManager.getItemInHandRenderer()));
    }



    public Identifier getTextureLocation(Magus p_114891_) {
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


}