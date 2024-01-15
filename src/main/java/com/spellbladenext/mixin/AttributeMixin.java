package com.spellbladenext.mixin;


import com.google.common.collect.Maps;
import com.spellbladenext.Spellblades;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

import static com.spellbladenext.Spellblades.*;
@Mixin(value = AttributeContainer.class)
public class AttributeMixin {
    @Shadow
    private Map<EntityAttribute, EntityAttributeInstance> custom;
    @Shadow
    private DefaultAttributeContainer fallback;

    @Inject(at = @At("HEAD"), method = "getValue", cancellable = true)
    private void getAttributeValueSpellblades(EntityAttribute attribute, CallbackInfoReturnable<Double> info) {
        if(attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE){
        AttributeContainer container = (AttributeContainer) (Object) this;
        EntityAttributeInstance percentinstance1 = (EntityAttributeInstance)this.custom.get(CONVERTFROMFIRE);
        EntityAttributeInstance percentinstance2 = (EntityAttributeInstance)this.custom.get(CONVERTFROMFROST);
        EntityAttributeInstance percentinstance3 = (EntityAttributeInstance)this.custom.get(CONVERTFROMARCANE);
        double value1 = 100;
        double value2 = 100;
        double value3 = 100;
        if(percentinstance1 != null) {
            value1 = percentinstance1.getValue();
        }
            if(percentinstance2 != null) {
                value2 = percentinstance2.getValue();
            }
            if(percentinstance3 != null) {
                value3 = percentinstance3.getValue();
            }
        double percent1 = (double) (0.01*(value1-100));
        double percent2 = (double) (0.01*(value2-100));
        double percent3 = (double) (0.01*(value3-100));
        double added = 0;
        double added2 = 0;
        double added3 = 0;
        EntityAttributeInstance addedinstance1 = (EntityAttributeInstance)this.custom.get(SpellAttributes.POWER.get(MagicSchool.FIRE).attribute);
        EntityAttributeInstance addedinstance2 = (EntityAttributeInstance)this.custom.get(SpellAttributes.POWER.get(MagicSchool.FROST).attribute);
        EntityAttributeInstance addedinstance3 = (EntityAttributeInstance)this.custom.get(SpellAttributes.POWER.get(MagicSchool.ARCANE).attribute);
        if(percentinstance1 != null) {
            added = addedinstance1.getValue();
        }
        if(percentinstance2 != null) {
            added2 = addedinstance2.getValue();
        }
        if(percentinstance3  != null) {
            added3 = addedinstance3.getValue();
        }
        EntityAttributeInstance damageinstance = (EntityAttributeInstance)this.custom.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        double total = this.fallback.getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if(damageinstance != null){
            total = damageinstance.getValue();
        }
        if( percent1 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE){
            total += percent1*added;
        }
        if( percent2 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE){
            total +=percent2*added2;
        }
        if( percent3 > 0 && attribute == EntityAttributes.GENERIC_ATTACK_DAMAGE){
            total += percent3*added3;
        }
        if(percent1+percent2+percent3 > 0){
            info.setReturnValue(total);
        }
        }
    }
}
