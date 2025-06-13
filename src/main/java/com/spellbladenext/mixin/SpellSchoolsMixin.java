package com.spellbladenext.mixin;

import com.spellbladenext.CustomSpellSchools;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpellSchools.class)

public class SpellSchoolsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void static_taiSB(CallbackInfo ci) {
        if (CustomSpellSchools.ARMOR.attribute != null) {
            SpellSchools.register(CustomSpellSchools.ARMOR); // Trigger registration
        }

    }

}
