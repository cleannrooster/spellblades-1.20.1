package com.spellbladenext.invasions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

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
            /*Optional<HexbladePortal> frame = piglinsummon.summonNetherPortal(this.level,this.player,false);
            if(frame.isPresent()){
                this.level.spawnEntity(frame.get());
                this.done = true;
            }*/
        }

        if(this.tickCount % 80 == 0) {
        }
        this.firstTick = false;
        this.tickCount++;

    }
}
