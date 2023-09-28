package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.MagisterArmorModel;
import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.items.Orb;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.renderer.GeoArmorRenderer;

public class MagisterArmorRenderer  extends GeoArmorRenderer<MagisterArmor> {

    public MagisterArmorRenderer() {
        super(new MagisterArmorModel());

    }
}
