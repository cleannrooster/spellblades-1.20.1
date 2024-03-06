package com.spellbladenext.mixin;

import com.spellbladenext.items.TheSpark;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.spell_engine.internals.SpellHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellPower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.spell_power.api.SpellPower.getSpellPower;

@Mixin(SpellPower.class)
public class SpellCasterMixin {
    @ModifyVariable(method = "getSpellPower", at = @At(value = "HEAD"))
    private static MagicSchool getSpellPowerSpellblades(MagicSchool school, MagicSchool originalschool,LivingEntity entity) {
        if(entity instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof TheSpark && originalschool != MagicSchool.ARCANE) {
            return MagicSchool.ARCANE;
        }
        else{
            return school;
        }
    }

}
