package com.spellbladenext.invasions;

import com.spellbladenext.entity.HexbladePortal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spellbladenext.Spellblades.*;

public class attackevent implements piglinsummon {
    public int tickCount;
    public boolean done = false;
    World level;
    PlayerEntity player;
    boolean firstTick;

    public attackevent(World world, PlayerEntity serverPlayer) {
        this.tickCount = 0;
        this.level = world;
        this.player = serverPlayer;
        this.firstTick = true;
    }

    @Override
    public void tick() {
        if(this.tickCount % 5 == 0 && !level.isClient()){
            Optional<HexbladePortal> frame = piglinsummon.summonNetherPortal(this.level,this.player,false);
            if(frame.isPresent()){
                this.level.spawnEntity(frame.get());
                this.done = true;
            }
        }

        if(this.tickCount % 80 == 0) {
        }
        this.firstTick = false;
        this.tickCount++;

    }

    public static void horde(PlayerEntity player, boolean multiply) {
        List<ServerPlayerEntity> list = new ArrayList<>();
        List<ServerPlayerEntity> list2 = new ArrayList<>();

        double b = 0;
        double c = 0;
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            b = serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(HEXRAID));
            c = serverPlayerEntity.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX));
            list2.add(serverPlayerEntity);
        }
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            list = serverWorld.getPlayers();

        }
        for (ServerPlayerEntity entity : list) {
            if (entity != player && entity.distanceTo(player) < 64) {
                b += entity.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(HEXRAID));
                c += entity.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX));
                list2.add(entity);
            }
        }
        double a = 0.01 * (b / 100F) * Math.pow((1.02930223664), c);
        if(multiply){
            a*=10;
        }
        a *= config.spawnmodifier;
        int k = (24 + player.getRandom().nextInt(24)) * (player.getRandom().nextBoolean() ? -1 : 1);
        int l = (24 + player.getRandom().nextInt(24)) * (player.getRandom().nextBoolean() ? -1 : 1);
        BlockPos.Mutable blockpos$mutableblockpos = player.getBlockPos().mutableCopy().move(k, 0, l);
        int i1 = 10;
        if (player.getRandom().nextFloat() < a * 0.1) {
            if (!player.getWorld().isRegionLoaded(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)) {

            } else {
                RegistryEntry<Biome> holder = player.getWorld().getBiome(blockpos$mutableblockpos);
                if (holder.isIn(BiomeTags.WITHOUT_PATROL_SPAWNS)) {

                } else {
                    int j1 = 0;
                    int k1 = 10;

                    blockpos$mutableblockpos.setY(player.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()));
                    ServerCommandSource commandSource = player.getServer().getCommandSource();

                    player.getServer().getCommandManager().executeWithPrefix(commandSource, "execute positioned " + blockpos$mutableblockpos.getX() + " " + blockpos$mutableblockpos.getY() + " " + blockpos$mutableblockpos.getZ() +
                            " run horde spawn");
                    player.getWorld().playSound((PlayerEntity) null, player.getBlockPos(), SoundEvents.AMBIENT_CAVE.value(), SoundCategory.MASTER, 1.2F, 1.0F);
                    for (ServerPlayerEntity playerEntity : list2) {
                        playerEntity.sendMessage(Text.literal("They're coming."));
                        playerEntity.getStatHandler().setStat(playerEntity, Stats.CUSTOM.getOrCreateStat(SINCELASTHEX), 0);
                        playerEntity.getStatHandler().setStat(playerEntity, Stats.CUSTOM.getOrCreateStat(HEXRAID), 0);

                    }

                }
            }
        }
    }
}
