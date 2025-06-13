package com.spellbladenext.mixin;

import com.mojang.brigadier.ParseResults;
import com.spellbladenext.Spellblades;
import com.spellbladenext.invasions.attackevent;
import com.spellbladenext.items.Claymore;
import com.spellbladenext.items.Spellblade;
import com.spellbladenext.items.Starforge;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.listener.GameEventListener;
import net.spell_engine.SpellEngineMod;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellInfo;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchool;
import net.spell_power.api.SpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.spellbladenext.Spellblades.*;

@Mixin(SpellHelper.class)
public class SpellCastMixin {

    @Inject(at = @At("TAIL"), method = "performSpell", cancellable = true)
    private static void performSpellBladesEchp(World world, PlayerEntity player, Identifier spellId, List<Entity> targets, SpellCast.Action action, float progress, CallbackInfo callbackInfo) {
        if (!player.isSpectator()) {

            if(!(spellId.equals(Identifier.of(MOD_ID,"blastarcane")) ||
                    spellId.equals(Identifier.of(MOD_ID,"blastfrost")) ||
                    spellId.equals(Identifier.of(MOD_ID,"blastfire")) ||
                    spellId.equals(Identifier.of(MOD_ID,"blastlightning")) ||
                    spellId.equals(Identifier.of(MOD_ID,"blastsoul"))) && !spellId.equals(Identifier.of(MOD_ID,"spellstrike"))&&!action.equals(SpellCast.Action.CHANNEL) &&!(targets.isEmpty() && SpellRegistry.getSpell(spellId).release.target.type.equals(Spell.Release.Target.Type.CURSOR)) && player.hasStatusEffect(Spellblades.UNLEASH) && player instanceof SpellCasterEntity caster && player instanceof PlayerDamageInterface playerDamageInterface){
                int repeats = player.getStatusEffect(Spellblades.UNLEASH).getAmplifier()+1;
                player.removeStatusEffect(Spellblades.UNLEASH);
                playerDamageInterface.resetDiebeamStack();
                for(int i = 0; i < repeats; i++){
                    ((WorldScheduler)player.getWorld()).schedule((i+1)*4, () -> {
                        caster.getCooldownManager().set(spellId,0,true);
                        player.removeStatusEffect(Spellblades.UNLEASH);
                        playerDamageInterface.resetDiebeamStack();

                        SpellHelper.performSpell(world,player,spellId,targets,action,progress);
                    });
                }

            }
        }
    }
}
