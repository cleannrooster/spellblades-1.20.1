package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.HeraldArmorModel;
import com.spellbladenext.client.item.model.MagisterArmorModel;
import com.spellbladenext.items.armor.HeraldArmor;
import com.spellbladenext.items.armor.MagisterArmor;
import mod.azure.azurelib.renderer.GeoArmorRenderer;

public class HeraldArmorRenderer  extends GeoArmorRenderer<HeraldArmor> {

    public HeraldArmorRenderer() {
        super(new HeraldArmorModel());

    }
}
