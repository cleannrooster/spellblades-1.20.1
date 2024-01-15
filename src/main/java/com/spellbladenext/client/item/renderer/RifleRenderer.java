package com.spellbladenext.client.item.renderer;

import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.client.item.model.RifleModel;
import com.spellbladenext.items.Orb;
import com.spellbladenext.items.Rifle;
import mod.azure.azurelib.renderer.GeoItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class RifleRenderer extends GeoItemRenderer<Rifle> {


    public RifleRenderer() {
        super(new RifleModel());

    }
}
