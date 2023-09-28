package com.spellbladenext.effect;

import com.spellbladenext.Spellblades;
import com.spellbladenext.entity.Magister;
import com.spellbladenext.invasions.attackevent;
import com.spellbladenext.invasions.piglinsummon;
import com.spellbladenext.items.Spellblade;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.spell_engine.api.spell.Spell;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.spellbladenext.Spellblades.*;

public class Hex extends StatusEffect {
    public Hex(StatusEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int i) {
        if(livingEntity instanceof PlayerEntity player && player.getWorld() instanceof ServerWorld level){

            Magister reaver1 = player.getWorld().getClosestEntity(Magister.class, TargetPredicate.createNonAttackable(),player,player.getX(),player.getY(),player.getZ(),player.getBoundingBox().expand(50,50,50));
            Magister reaver = new Magister(Spellblades.REAVER, player.getWorld());
            reaver.isScout = true;
            reaver.nemesis = player;
            BlockPos pos = piglinsummon.getSafePositionAroundPlayer(player.getWorld(), player.getSteppingPos(), 50);

            if (pos != null) {
                boolean bool = StreamSupport.stream(level.iterateEntities().spliterator(),true).toList().stream().noneMatch(entity -> entity instanceof Magister reaver2 && reaver2.isScout() && reaver2.nemesis == player);
                if(bool) {
                    reaver.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    reaver.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(player.getPos(), 2, 2));
                    player.getWorld().spawnEntity(reaver);
                }

            }
            if(reaver1 != null && reaver1.isScout() &&  reaver.nemesis == player && !reaver1.returninghome && !player.hasStatusEffect(StatusEffects.INVISIBILITY)){
                reaver1.getBrain().remember(MemoryModuleType.WALK_TARGET,new WalkTarget(player,1.4F,1));
            }

        }
        super.applyUpdateEffect(livingEntity, i);
    }

    @Override
    public boolean canApplyUpdateEffect(int i, int j) {
        return true;
    }


    @Override
    public void onRemoved(LivingEntity livingEntity, AttributeContainer attributeMap, int i) {

        super.onRemoved(livingEntity, attributeMap, i);

        if(livingEntity instanceof PlayerEntity player && !player.getWorld().isClient()){
            Optional<BlockPos> pos = BlockPos.findClosest(player.getBlockPos(),64,128,
                    blockPos -> player.getWorld().getBlockState(blockPos).getBlock().equals(HEXBLADE));
            if(pos.isPresent()){
                return;
            }
            if(player.getInventory().containsAny(itemStack -> itemStack.isOf(HEXBLADEITEM))) {
                return;
            }
            if(!player.getInventory().containsAny(itemStack -> itemStack.isOf(Spellblades.OFFERING))){
                attackeventArrayList.add(new attackevent(player.getWorld(),player));
            }
            else{
                player.sendMessage(Text.translatable("Your patronage has saved you. For now."));
                player.addStatusEffect(new StatusEffectInstance(Spellblades.MAGISTERFRIEND,20*60*5,0));

                if(player instanceof ServerPlayerEntity player1) {
                    player1.getStatHandler().setStat(player1, Stats.CUSTOM.getOrCreateStat(SINCELASTHEX), 0);
                }
                if(player.getStackInHand(Hand.MAIN_HAND).isOf(Spellblades.OFFERING)) {
                    ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                    stack.decrement(1);
                    if (stack.isEmpty()) {
                        player.getInventory().removeOne(stack);

                    }
                }
                else if(player.getStackInHand(Hand.OFF_HAND).isOf(Spellblades.OFFERING)) {
                    ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
                    stack.decrement(1);


                }
                else {
                    for (int ii = 0; ii < player.getInventory().size(); ++ii) {
                        ItemStack stack = player.getInventory().getStack(ii);
                        if (stack.isOf(Spellblades.OFFERING)) {
                            stack.decrement(1);
                            if (stack.isEmpty()) {
                                player.getInventory().removeOne(stack);
                            }
                            break;
                        }
                    }

                }
            }
        }
    }
}
