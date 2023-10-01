package com.spellbladenext.mixin;

import com.spellbladenext.Spellblades;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public class NaturalSpawnerMixin {
    @Inject(method = "spawnEntitiesInChunk",
            at = @At("HEAD"),
            cancellable = true)
    private static void spawnEtiesInChunk(SpawnGroup mobCategory, ServerWorld world, WorldChunk levelChunk, SpawnHelper.Checker spawnPredicate, SpawnHelper.Runner afterSpawnCallback, CallbackInfo ci) {

        if (
                world.getRegistryKey().equals(Spellblades.DIMENSIONKEY))
        {
            ci.cancel();
        }
    }
}
