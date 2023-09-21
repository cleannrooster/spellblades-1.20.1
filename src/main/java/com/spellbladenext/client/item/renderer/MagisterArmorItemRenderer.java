package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.MagisterArmorItemModel;
import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.items.armor.MagisterArmor;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MagisterArmorItemRenderer extends GeoItemRenderer<MagisterArmor> {

    public MagisterArmorItemRenderer() {
        super(new MagisterArmorItemModel());

    }
}
