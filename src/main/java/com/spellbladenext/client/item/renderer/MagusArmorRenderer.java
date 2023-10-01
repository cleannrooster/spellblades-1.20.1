package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.MagisterArmorModel;
import com.spellbladenext.client.item.model.MagusArmorModel;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.renderer.GeoArmorRenderer;

public class MagusArmorRenderer extends GeoArmorRenderer<MagusArmor> {

    public MagusArmorRenderer() {
        super(new MagusArmorModel());

    }
}
