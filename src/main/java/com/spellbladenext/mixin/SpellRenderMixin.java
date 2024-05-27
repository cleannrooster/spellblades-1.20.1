package com.spellbladenext.mixin;

import com.spellbladenext.Spellblades;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.spell_engine.client.util.SpellRender;
import net.spell_engine.internals.SpellContainerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.spellbladenext.Spellblades.MOD_ID;


@Mixin(SpellRender.class)
public class SpellRenderMixin {
    @Inject(at = @At("HEAD"), method = "iconTexture", cancellable = true)
    private static void iconTextureReplaceSpellblade(Identifier spellId, CallbackInfoReturnable<Identifier> identifier) {
        if(MinecraftClient.getInstance() != null) {

            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && SpellContainerHelper.getEquipped(player.getMainHandStack(),player) != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids.contains("spellbladenext:echoes")) {

                if (spellId.getPath().equals("echoes")) {
                    if(player.getStatusEffect(Spellblades.UNLEASH) != null) {
                        identifier.setReturnValue(new Identifier(MOD_ID, "textures/spell/" + "echoes" + (player.getStatusEffect(Spellblades.UNLEASH).getAmplifier()+1) + ".png"));
                    }
                }

            }
        }
    }
}
