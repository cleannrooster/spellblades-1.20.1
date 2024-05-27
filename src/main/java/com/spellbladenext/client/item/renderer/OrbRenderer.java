package com.spellbladenext.client.item.renderer;

import com.spellbladenext.Spellblades;
import com.spellbladenext.client.item.model.OrbModel;
import com.spellbladenext.items.Orb;
import mod.azure.azurelib.renderer.GeoItemRenderer;
public class OrbRenderer extends GeoItemRenderer<Orb> {


    public OrbRenderer() {
        super(new OrbModel());

    }
}
