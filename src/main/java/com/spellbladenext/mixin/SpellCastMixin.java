package com.spellbladenext.mixin;

import com.mojang.brigadier.ParseResults;
import com.spellbladenext.Spellblades;
import com.spellbladenext.invasions.attackevent;
import com.spellbladenext.items.Claymore;
import com.spellbladenext.items.Spellblade;
import com.spellbladenext.items.Starforge;
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
import net.spell_engine.internals.casting.SpellCast;
import net.spell_engine.internals.casting.SpellCasterEntity;
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

import static com.spellbladenext.Spellblades.HEXRAID;
import static com.spellbladenext.Spellblades.SINCELASTHEX;

@Mixin(SpellHelper.class)
public class SpellCastMixin {
   
    @Inject(at = @At("HEAD"), method = "performSpell", cancellable = true)
    private static void performSpellBlades(World world, PlayerEntity player, Identifier spellId, List<Entity> targets, SpellCast.Action action, float progress, CallbackInfo callbackInfo) {
        if (!player.isSpectator()) {
            Spell spell = SpellRegistry.getSpell(spellId);
            if (spell != null) {
                SpellInfo spellInfo = new SpellInfo(spell, spellId);
                ItemStack itemStack = player.getMainHandStack();
                SpellSchool actualSchool = SpellRegistry.getSpell(spellId).school;

                if (actualSchool != ExternalSpellSchools.PHYSICAL_MELEE && action == SpellCast.Action.RELEASE && (itemStack.getItem() instanceof Spellblade || itemStack.getItem() instanceof Claymore || Objects.equals(spellId, new Identifier(Spellblades.MOD_ID, "smite")))) {

                    SpellPower.Result power2 = SpellPower.getSpellPower(actualSchool, (LivingEntity) player);
                    int amp = Math.min(49, (int) power2.randomValue() / 4 - 1);
                    if (amp >= 0) {

                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, (int) (SpellHelper.getCooldownDuration(player, SpellRegistry.getSpell(spellId)) * 20 * progress), amp));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, (int) (SpellHelper.getCooldownDuration(player, SpellRegistry.getSpell(spellId)) * 20 * progress), amp));

                    }
                }
                if (Spellblades.config.horde && spell.school != ExternalSpellSchools.PHYSICAL_RANGED && spell.school != ExternalSpellSchools.PHYSICAL_MELEE && spell.school != SpellSchools.HEALING) {
                    attackevent.horde(player,false);
                }
            }
        }
    }
}
