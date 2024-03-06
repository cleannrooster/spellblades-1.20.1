package com.spellbladenext.items.attacks;

import com.spellbladenext.Spellblades;
import com.spellbladenext.config.ServerConfig;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.CustomSpellHandler;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;

import java.util.List;

public class Attacks {
    public static void attackAll(LivingEntity user, List<Entity> targets, float multiplier) {
        if (user instanceof PlayerEntity player && player instanceof PlayerDamageInterface damager && !targets.isEmpty()) {
            damager.override(true);
            damager.setDamageMultiplier(multiplier);
            for (Entity entity : targets) {
                if (entity instanceof LivingEntity living) {
                    living.timeUntilRegen = 0;

                }
                player.attack(entity);

            }
            damager.override(false);
        }
    }
    public static Vec3d rotate(double x, double y, double z, double pitch, double roll, double yaw) {
        double cosa = Math.cos(yaw);
        double sina = Math.sin(yaw);

        double cosb = Math.cos(pitch);
        double sinb = Math.sin(pitch);
        double cosc = Math.cos(roll);
        double sinc = Math.sin(roll);

        double Axx = cosa * cosb;
        double Axy = cosa * sinb * sinc - sina * cosc;
        double Axz = cosa * sinb * cosc + sina * sinc;

        double Ayx = sina * cosb;
        double Ayy = sina * sinb * sinc + cosa * cosc;
        double Ayz = sina * sinb * cosc - cosa * sinc;

        double Azx = -sinb;
        double Azy = cosb * sinc;
        double Azz = cosb * cosc;

        Vec3d vec3 = new Vec3d(Axx * x + Axy * y + Axz * z,Ayx * x + Ayy * y + Ayz * z,Azx * x + Azy * y + Azz * z);
        return vec3;
    }
    public static void eleWhirlwind(CustomSpellHandler.Data data1) {
        if(((SpellCasterEntity) data1.caster()).getCurrentSpell() != null){
        MagicSchool actualSchool = ((SpellCasterEntity) data1.caster()).getCurrentSpell().school;
        float modifier = ((SpellCasterEntity) data1.caster()).getCurrentSpell().impact[0].action.damage.spell_power_coefficient;
        modifier *= 0.4F + 0.6F / (float) data1.targets().size() + (0.6F - 0.6F / (float) data1.targets().size()) * Math.min(3, EnchantmentHelper.getEquipmentLevel(Enchantments.SWEEPING, data1.caster())) / 3;
        modifier *= 0.2;
        modifier *= data1.caster().getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED);
        modifier *= Spellblades.config.spin_attack_coeff;
        float modifier2 = ((SpellCasterEntity) data1.caster()).getCurrentSpell().impact[1].action.damage.spell_power_coefficient;
        modifier2 *= 0.4F + 0.6F / (float) data1.targets().size() + (0.6F - 0.6F / (float) data1.targets().size()) * Math.min(3, EnchantmentHelper.getEquipmentLevel(Enchantments.SWEEPING, data1.caster())) / 3;
        modifier2 *= 0.2;
            modifier2 *= Spellblades.config.spin_attack_coeff;

            modifier2 *= data1.caster().getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED);


        attackAll(data1.caster(), data1.targets(), (float) modifier);
        for (Entity entity : data1.targets()) {
            SpellPower.Result power = SpellPower.getSpellPower(actualSchool, (LivingEntity) data1.caster());
            SpellPower.Vulnerability vulnerability = SpellPower.Vulnerability.none;
            if (entity instanceof LivingEntity living) {
                vulnerability = SpellPower.getVulnerability(living, actualSchool);
            }
            double amount = modifier2 * power.randomValue(vulnerability);
            entity.timeUntilRegen = 0;

            entity.damage(SpellDamageSource.player(actualSchool, data1.caster()), (float) amount);
        }
        }
    }

    public static void flourish(CustomSpellHandler.Data data1) {

    }
}
