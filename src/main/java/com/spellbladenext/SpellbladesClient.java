package com.spellbladenext;

import com.spellbladenext.client.entity.*;
import com.spellbladenext.config.ConfigSync;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.spell_engine.api.event.CombatEvents;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.casting.SpellCasterClient;
import net.spell_engine.internals.casting.SpellCasterEntity;
import net.spell_power.api.SpellPowerMechanics;

import java.util.Objects;

import static com.spellbladenext.Spellblades.*;

public class SpellbladesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Spellblades.REAVER, MagisterRenderer::new);
        EntityRendererRegistry.register(HEXBLADEPORTAL, HexbladePortalRenderer::new);
        EntityRendererRegistry.register(Spellblades.ARCHMAGUS, MagusRenderer::new);
        EntityRendererRegistry.register(Spellblades.RIFLEPROJECTILE, ArrowEntityRenderer::new);
        EntityRendererRegistry.register(Spellblades.CYCLONEENTITY, CycloneRenderer::new);
        EntityRendererRegistry.register(Spellblades.REDLASERENTITY, RedbeamRenderer::new);
        EntityRendererRegistry.register(Spellblades.SMITELIGHTNING, LightningEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(ConfigSync.ID, (client, handler, buf, responseSender) -> {
            var config = ConfigSync.read(buf);
            Spellblades.config = config;
        });
        ClientTickEvents.START_CLIENT_TICK.register(server -> {
                    PlayerEntity player = server.player;
                    World level = server.world;
            if (player != null && level != null) {

                if (player instanceof SpellCasterEntity caster) {

                    if (SpellRegistry.getSpell(Identifier.of(MOD_ID, "overpower")) != null) {
                        if (Objects.equals(caster.getCurrentSpell(), SpellRegistry.getSpell(Identifier.of(MOD_ID, "overpower")))) {

                            double speed = player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 4;
                            BlockHitResult result = level.raycast(new RaycastContext(player.getPos(), player.getPos().add(0, -2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player));
                            if (player.isSneaking()) {
                                speed *= 0;
                            }
                            double modifier = 0;
                            if (result.getType() == HitResult.Type.BLOCK) {
                                modifier = 1;
                            }

                            Spell spell = SpellRegistry.getSpell(Identifier.of(MOD_ID, "overpower"));


                            player.setVelocity(player.getRotationVec(1).subtract(0, player.getRotationVec(1).y, 0).normalize().multiply(speed, speed * modifier, speed).add(0, player.getVelocity().y, 0));
                        }
                    }
                }
            }
        });

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}