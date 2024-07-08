package com.spellbladenext.mixin;


import com.spellbladenext.entity.Magus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class RenderMixin<E extends Entity>{

    @Shadow
    private EntityRenderDispatcher entityRenderDispatcher;
@Shadow
private  BufferBuilderStorage bufferBuilders;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"), method = "render", cancellable = true)
    public void renderCleann(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo info) {
        WorldRenderer renderer = (WorldRenderer) (Object) this;
        if (MinecraftClient.getInstance().world != null) {
                for(Entity entity : MinecraftClient.getInstance().world.getEntities()) {
                    if (entity instanceof Magus magus) {


                        for (int ii = 0; ii < magus.getPositions().size(); ii++) {
                            if (ii % 2 == 0) {
                                Vec3d vec3d = magus.getPositions().get(ii);

                                    double j = vec3d.getX() - camera.getPos().getX();
                                    double k = vec3d.getY() - camera.getPos().getY();
                                    double l = vec3d.getZ() - camera.getPos().getZ();
                                    float g = entity.getYaw(tickDelta);
                                matrices.push();
                                matrices.scale(1.05F,1.05F,1.05F);
                                    entityRenderDispatcher.render(entity, j, k, l, g, tickDelta, matrices, bufferBuilders.getEntityVertexConsumers(), entityRenderDispatcher.getLight(entity,tickDelta)/(2*(4-(ii/2))));
                                matrices.pop();
                            }
                        }
                    }
                }
        }

    }
}
