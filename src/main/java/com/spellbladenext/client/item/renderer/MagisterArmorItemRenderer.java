package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.MagisterArmorItemModel;
import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.renderer.GeoItemRenderer;

public class MagisterArmorItemRenderer extends GeoItemRenderer<MagisterArmor> {

    public MagisterArmorItemRenderer() {
        super(new MagisterArmorItemModel());

    }
}
