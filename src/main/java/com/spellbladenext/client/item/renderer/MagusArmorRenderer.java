package com.spellbladenext.client.item.renderer;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.model.MagisterArmorModel;
import com.spellbladenext.client.item.model.MagusArmorModel;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.renderer.GeoArmorRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellSchools;

public class MagusArmorRenderer extends GeoArmorRenderer<MagusArmor> {

    public MagusArmorRenderer() {
        super(new MagusArmorModel());

    }

    @Override
    public Identifier getTextureLocation(MagusArmor animatable) {
        if(this.getCurrentEntity() instanceof PlayerEntity player) {
            double arcane = player.getAttributeValue((SpellSchools.ARCANE).attribute);
            double fire = player.getAttributeValue(  (SpellSchools.FIRE).attribute);
            double frost = player.getAttributeValue( (SpellSchools.FROST).attribute);
            if(arcane > fire && arcane > frost){
                return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_arcane.png");
            }
            if(fire > arcane && fire > frost){
                return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_fire.png");
            }
            if(frost > arcane && frost > fire){
                return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_frost.png");
            }
            if(arcane == fire && fire == frost){
                return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_default.png");

            }
        }

        return new Identifier(Spellblades.MOD_ID, "textures/armor/robestexture_default.png");
    }
}
