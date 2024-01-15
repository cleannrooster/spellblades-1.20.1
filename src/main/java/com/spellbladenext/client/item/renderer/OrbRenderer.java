package com.spellbladenext.client.item.renderer;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.items.Orb;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.util.Identifier;
import net.spell_power.api.MagicSchool;
import org.spongepowered.asm.mixin.Dynamic;
public class OrbRenderer extends GeoItemRenderer<Orb> {


    public OrbRenderer() {
        super(new OrbModel());

    }
}
