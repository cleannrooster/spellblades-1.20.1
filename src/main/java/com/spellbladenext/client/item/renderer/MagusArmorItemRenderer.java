package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.MagisterArmorItemModel;
import com.spellbladenext.client.item.model.MagusArmorItemModel;
import com.spellbladenext.items.armor.MagisterArmor;
import com.spellbladenext.items.armor.MagusArmor;
import mod.azure.azurelib.renderer.GeoItemRenderer;

public class MagusArmorItemRenderer extends GeoItemRenderer<MagusArmor> {

    public MagusArmorItemRenderer() {
        super(new MagusArmorItemModel());

    }
}
