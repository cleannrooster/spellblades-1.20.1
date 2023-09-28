package com.spellbladenext;

import com.spellbladenext.client.entity.HexbladePortalRenderer;
import com.spellbladenext.client.entity.MagisterRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellCasterEntity;
import net.spell_engine.internals.SpellRegistry;
import net.spell_power.api.attributes.SpellAttributes;

import java.util.Objects;

import static com.spellbladenext.Spellblades.HEXBLADEPORTAL;
import static com.spellbladenext.Spellblades.MOD_ID;

public class SpellbladesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Spellblades.REAVER, MagisterRenderer::new);
        EntityRendererRegistry.register(HEXBLADEPORTAL, HexbladePortalRenderer::new);

        ClientTickEvents.START_CLIENT_TICK.register(server -> {
                    PlayerEntity player = server.player;
                    World level = server.world;

                    if (player != null && level != null) {

                        if (SpellRegistry.getSpell(new Identifier(MOD_ID, "maelstrom")) != null) {
                            double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * player.getAttributeValue(SpellAttributes.HASTE.attribute) * 0.01 * 4;
                            BlockHitResult result = level.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
                            if (player.isSneaking()) {
                                speed *= 0;
                            }
                            double modifier = 0;
                            if (result.getType() == HitResult.Type.BLOCK) {
                                modifier = 1;
                            }

                            Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID, "maelstrom"));

                            if (player instanceof SpellCasterEntity caster) {

                                if (Objects.equals(caster.getCurrentSpellId(), new Identifier(MOD_ID, "maelstrom"))) {


                                    player.setVelocity(player.getRotationVec(1).subtract(0, player.getRotationVec(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0, player.getVelocity().y, 0));
                                }
                            }
                        }
                        if (SpellRegistry.getSpell(new Identifier(MOD_ID, "inferno")) != null) {
                            double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * player.getAttributeValue(SpellAttributes.HASTE.attribute) * 0.01 * 4;
                            BlockHitResult result = level.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
                            if (player.isSneaking()) {
                                speed *= 0;
                            }
                            double modifier = 0;
                            if (result.getType() == HitResult.Type.BLOCK) {
                                modifier = 1;
                            }

                            Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID, "inferno"));

                            if (player instanceof SpellCasterEntity caster) {

                                if (Objects.equals(caster.getCurrentSpellId(), new Identifier(MOD_ID, "inferno"))) {


                                    player.setVelocity(player.getRotationVec(1).subtract(0, player.getRotationVec(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0, player.getVelocity().y, 0));
                                }
                            }
                        }
                        if (SpellRegistry.getSpell(new Identifier(MOD_ID, "whirlwind")) != null) {
                            double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * player.getAttributeValue(SpellAttributes.HASTE.attribute) * 0.01 * 4;
                            BlockHitResult result = level.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
                            if (player.isSneaking()) {
                                speed *= 0;
                            }
                            double modifier = 0;
                            if (result.getType() == HitResult.Type.BLOCK) {
                                modifier = 1;
                            }

                            Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID, "whirlwind"));

                            if (player instanceof SpellCasterEntity caster) {

                                if (Objects.equals(caster.getCurrentSpellId(), new Identifier(MOD_ID, "whirlwind"))) {


                                    player.setVelocity(player.getRotationVec(1).subtract(0, player.getRotationVec(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0, player.getVelocity().y, 0));
                                }
                            }
                        }
                        if (SpellRegistry.getSpell(new Identifier(MOD_ID, "tempest")) != null) {
                            double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * player.getAttributeValue(SpellAttributes.HASTE.attribute) * 0.01 * 4;
                            BlockHitResult result = level.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
                            if (player.isSneaking()) {
                                speed *= 0;
                            }
                            double modifier = 0;
                            if (result.getType() == HitResult.Type.BLOCK) {
                                modifier = 1;
                            }

                            Spell spell = SpellRegistry.getSpell(new Identifier(MOD_ID, "tempest"));

                            if (player instanceof SpellCasterEntity caster) {

                                if (Objects.equals(caster.getCurrentSpellId(), new Identifier(MOD_ID, "tempest"))) {


                                    player.setVelocity(player.getRotationVec(1).subtract(0, player.getRotationVec(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0, player.getVelocity().y, 0));
                                }
                            }
                        }
                    }
                });
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}