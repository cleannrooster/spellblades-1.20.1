package com.spellbladenext.mixin;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.AnimationHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static com.spellbladenext.Spellblades.*;
import static net.spell_engine.internals.SpellHelper.intent;

@Mixin(ItemStack.class)
abstract  class ItemStackMixin {
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damage(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if(player instanceof SpellCasterEntity entity && entity.getCurrentSpell() != null){
            info.cancel();
        }

    }
    @Inject(at = @At("HEAD"), method = "postHit", cancellable = true)
    public void postDamageEntitySpellblades(LivingEntity target, PlayerEntity player, CallbackInfo callbackInfo) {
        if(player.hasStatusEffect(SPELLSTRIKE)&&
             SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids.contains("spellbladenext:spellstrike")){
            double arcane = SpellPower.getSpellPower(SpellSchools.ARCANE,player).baseValue();
            double fire = SpellPower.getSpellPower(SpellSchools.FIRE,player).baseValue();
            double frost = SpellPower.getSpellPower(SpellSchools.FROST,player).baseValue();
            double lightning = SpellPower.getSpellPower(SpellSchools.LIGHTNING,player).baseValue();
            Spell spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastarcane"));
            Identifier id = Identifier.of(MOD_ID,"blastarcane");
            if(arcane > fire && arcane > frost && arcane > lightning){
                spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastarcane"));
                id = Identifier.of(MOD_ID,"blastarcane");
            }
            else if(fire > arcane && fire > frost && fire > lightning){
                spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastfire"));
                id =Identifier.of(MOD_ID,"blastfire");
            }
            else if(frost > fire && frost > arcane && frost > lightning){
                spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastfrost"));
                id =Identifier.of(MOD_ID,"blastfrost");

            }
            else if(lightning > fire && lightning > frost && lightning > arcane){
                spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastlightning"));
                id =Identifier.of(MOD_ID,"blastlightning");

            }
            else{
                spell = SpellRegistry.getSpell(Identifier.of(MOD_ID,"blastarcane"));
                id = Identifier.of(MOD_ID,"blastarcane");

            }


            SpellHelper.performSpell(player.getWorld(), player, id, List.of(target), SpellCast.Action.RELEASE, 1.0F);
            AnimationHelper.sendAnimation((PlayerEntity) player, PlayerLookup.tracking(player), SpellCast.Animation.RELEASE, SpellRegistry.getSpell(Identifier.of(MOD_ID, "spellstrike")).release.animation, 1F);

        }

    }

}
