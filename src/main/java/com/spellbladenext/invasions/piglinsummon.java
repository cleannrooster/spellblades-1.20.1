package com.spellbladenext.invasions;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.HexbladePortal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.spellbladenext.Spellblades.SINCELASTHEX;
import static java.lang.Math.sqrt;

public interface piglinsummon {
     default void tick(){
     }
     public static Optional<HexbladePortal> summonNetherPortal(World level, LivingEntity player, boolean home){
          double xRand = -1+level.getRandom().nextDouble()*2;
          double zRand = -1+level.getRandom().nextDouble()*2;
          double d0 = sqrt(xRand*xRand+zRand*zRand);
          if(player != null) {
               BlockHitResult result = level.raycast(new RaycastContext(player.getEyePos(), new Vec3d(player.getX() + 40 * xRand / d0, player.getY() - 40 * .2, player.getZ() + 40 * zRand / d0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player));
               if (result.getType() != HitResult.Type.MISS) {
                    BlockPos pos = result.getBlockPos();
                    if (result.getPos().subtract(player.getEyePos()).horizontalLength() > 2) {
                         boolean flag = level.getBlockState(pos.up()).shouldSuffocate(level, result.getBlockPos().up())
                                 || level.getBlockState(pos.up().up()).shouldSuffocate(level, pos.up().up())
                                 || level.getBlockState(pos.up().up().up()).shouldSuffocate(level, pos.up().up().up())
                                 || level.getBlockState(pos.up().up().up().up()).shouldSuffocate(level, pos.up().up().up().up());
                         int ii = 0;
                         boolean found = true;
                         while (flag) {
                              pos = pos.up();
                              flag = level.getBlockState(pos.up()).shouldSuffocate(level, result.getBlockPos().up())
                                      || level.getBlockState(pos.up().up()).shouldSuffocate(level, pos.up().up())
                                      || level.getBlockState(pos.up().up().up()).shouldSuffocate(level, pos.up().up().up())
                                      || level.getBlockState(pos.up().up().up().up()).shouldSuffocate(level, pos.up().up().up().up());
                              ii++;
                              if (ii > 10) {
                                   found = false;
                                   break;
                              }
                         }
                         if (found) {
                              boolean bool = level.getRandom().nextBoolean();
                              HexbladePortal portal = new HexbladePortal(Spellblades.HEXBLADEPORTAL,player.getWorld());
                              float yaw = 360*level.getRandom().nextFloat();
                              portal.setPos(pos.getX(),pos.getY()+1,pos.getZ());
                                   portal.setYaw(yaw);
                              portal.setBodyYaw(yaw);
                              portal.setHeadYaw(yaw);
                              portal.prevBodyYaw = yaw;
                              portal.prevHeadYaw = yaw;
                              portal.ishome = home;
                              portal.prevYaw = yaw;

                              if (player instanceof ServerPlayerEntity player1) {
                                   player1.getStatHandler().setStat(player1, Stats.CUSTOM.getOrCreateStat(SINCELASTHEX), 0);
                              }

                              return Optional.of(portal);

                         }
                    }
               }
          }
          return Optional.empty();
     }

     @Nullable
     public static BlockPos getSafePositionAroundPlayer(World level, BlockPos pos, int range)
     {
          if(range == 0)
          {
               return null;
          }
          BlockPos safestPos = null;
          for(int attempts = 0; attempts < 1; attempts++)
          {
               int a = -1;
               int b = -1;
               int c = -1;
               if(level.getRandom().nextBoolean()){
                    a = 1;
               }
               if(level.getRandom().nextBoolean()){
                    b = 1;
               }
               if(level.getRandom().nextBoolean()){
                    c = 1;
               }
               int posX = pos.getX()  + a*level.getRandom().nextInt(10 );
               int posY = pos.getY() + level.getRandom().nextInt(10) - 10 / 2;
               int posZ = pos.getZ()  + c* level.getRandom().nextInt(10);
               BlockPos testPos = findGround(level, new BlockPos(posX, posY, posZ));

               if(testPos != null && level.getFluidState(testPos).isEmpty() && level.getBlockState(testPos.down()).isOpaque())
               {
                    safestPos = testPos;
                    break;
               }
          }
          return safestPos;
     }
     @Nullable
     public static BlockPos getSafePositionAroundPlayer2(World level, BlockPos pos, int range)
     {
          if(range == 0)
          {
               return null;
          }
          BlockPos safestPos = null;
          for(int attempts = 0; attempts < 1; attempts++)
          {
               int a = -1;
               int b = -1;
               int c = -1;
               if(level.getRandom().nextBoolean()){
                    a = 1;
               }
               if(level.getRandom().nextBoolean()){
                    b = 1;
               }
               if(level.getRandom().nextBoolean()){
                    c = 1;
               }
               int posX = pos.getX()  + a*level.getRandom().nextInt(10 );
               int posY = pos.getY() + level.getRandom().nextInt(10) - 10 / 2;
               int posZ = pos.getZ()  + c* level.getRandom().nextInt(10);
               BlockPos testPos = findGround(level, new BlockPos(posX, posY, posZ));

               if(testPos != null)
               {
                    safestPos = testPos;
                    break;
               }
          }
          return safestPos;
     }


     @Nullable
     private static BlockPos findGround(World level, BlockPos pos)
     {
          if(level.getBlockState(pos).isAir())
          {
               BlockPos downPos = pos;
               while(World.isValid(downPos.down()) && level.getBlockState(downPos.down()).isAir() && downPos.down().isWithinDistance(pos, 20))
               {
                    downPos = downPos.down();
               }
               if(!level.getBlockState(downPos.down()).isAir())
               {
                    return downPos;
               }
          }
          else
          {
               BlockPos upPos = pos;
               while(World.isValid(upPos.up()) && !level.getBlockState(upPos.up()).isAir() && upPos.up().isWithinDistance(pos, 20))
               {
                    upPos = upPos.up();
               }
               if(!level.getBlockState(upPos.up()).isAir())
               {
                    return upPos;
               }
          }
          return null;
     }
}
