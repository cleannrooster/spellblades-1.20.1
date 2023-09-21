package com.spellbladenext.items.attacks;

import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class AttackAll {
    public static void attackAll(LivingEntity user, List<Entity> targets, float multiplier){
        if(user instanceof PlayerEntity player && player instanceof PlayerDamageInterface damager && !targets.isEmpty()){
            damager.override(true);
            damager.setDamageMultiplier(multiplier);
            for(Entity entity : targets){
                if(entity instanceof LivingEntity living){
                    living.timeUntilRegen = 0;

                }
                player.attack(entity);

            }
            damager.override(false);
        }
    }
}
