package com.spellbladenext.block;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class HexbladeBlockItem extends BlockItem {
    public HexbladeBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack itemStack, PlayerEntity player, LivingEntity livingEntity, Hand hand) {
        /*if(livingEntity instanceof HexbladePortal){
            livingEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT,1,1);
            if(player.getWorld().isClient()) {
                return ActionResult.success(player.getWorld().isClient());
            }
            else{
                livingEntity.discard();
                RegistryKey<World> resourceKey = player.world.getRegistryKey().equals(SpellbladeNext.DIMENSIONKEY) ? World.OVERWORLD : SpellbladeNext.DIMENSIONKEY;

                ServerWorld serverWorld = player.getEntityWorld().getServer().getWorld(resourceKey);
                if (serverWorld == null) {
                    return super.useOnEntity(itemStack, player, livingEntity, hand);
                }
                if (resourceKey == SpellbladeNext.DIMENSIONKEY) {
                    TeleportTarget target = new TeleportTarget(
                            new net.minecraft.util.math.Vec3d(0, 150, 0),
                            net.minecraft.util.math.Vec3d.ZERO,
                            0.0F,
                            0.0F
                    );
                    FabricDimensions.teleport(player, serverWorld, target);
                }
                if (player instanceof ServerPlayerEntity serverPlayer && resourceKey == World.OVERWORLD) {
                    TeleportTarget target;
                    if (serverPlayer.getSpawnPointPosition() != null) {
                        target = new TeleportTarget(
                                net.minecraft.util.math.Vec3d.ofCenter(serverPlayer.getSpawnPointPosition()),
                                net.minecraft.util.math.Vec3d.ZERO,
                                0.0F,
                                0.0F
                        );
                    } else {
                        target = new TeleportTarget(
                                net.minecraft.util.math.Vec3d.ofCenter(serverWorld.getSpawnPos()),
                                net.minecraft.util.math.Vec3d.ZERO,
                                0.0F,
                                0.0F
                        );
                    }
                    FabricDimensions.teleport(player, serverWorld, target);

                }
            }

        }*/
        return super.useOnEntity(itemStack, player, livingEntity, hand);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
    /*    HexbladePortal portal = new HexbladePortal(SpellbladesFabric.HEXBLADEPORTAL,world);
        portal.prevYaw = playerEntity.getYaw();
        portal.setYaw(playerEntity.getYaw());
        portal.headYaw = playerEntity.getYaw();
        portal.prevHeadYaw = playerEntity.getYaw();
        portal.prevBodyYaw = playerEntity.getYaw();

        portal.prevHeadYaw = playerEntity.headYaw;
        portal.setYaw(playerEntity.getYaw());
        portal.setPosition(playerEntity.getPos().add(playerEntity.getRotationVector().multiply(4)));
        portal.spawn = false;
        if(!world.isClient() && !playerEntity.getItemCooldownManager().isCoolingDown(this)){
            world.spawnEntity(portal);
            playerEntity.getItemCooldownManager().set(this,160);
            return super.use(world, playerEntity, hand);
        }
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        playerEntity.getItemCooldownManager().set(this,160);
*/
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        return TypedActionResult.success(itemStack,playerEntity.getWorld().isClient());

    }

}
