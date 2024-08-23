package com.spellbladenext.client.item.renderer;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.model.MagisterArmorModel;
import com.spellbladenext.client.item.model.MagusArmorModel;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.renderer.GeoArmorRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

public class MagusArmorRenderer extends GeoArmorRenderer<MagusArmor> {

    public MagusArmorRenderer() {
        super(new MagusArmorModel());

    }

    @Override
    public Identifier getTextureLocation(MagusArmor animatable) {
        if(this.getCurrentEntity() instanceof PlayerEntity player) {
            double arcane = SpellPower.getSpellPower(SpellSchools.ARCANE,player).baseValue();
            double fire = SpellPower.getSpellPower(SpellSchools.FIRE,player).baseValue();
            double frost = SpellPower.getSpellPower(SpellSchools.FROST,player).baseValue();
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
