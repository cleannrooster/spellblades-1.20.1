package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static com.spellbladenext.Spellblades.SINCELASTHEX;


public class DebugNetherPortal extends Item {
    public DebugNetherPortal(Settings p_41383_) {
        super(p_41383_);
    }

    @Override
    public TypedActionResult<ItemStack> use(World p_41432_, PlayerEntity p_41433_, Hand p_41434_) {
        if(p_41433_.isSneaking()){
            p_41433_.increaseStat(SINCELASTHEX,1);
            System.out.println(p_41433_.getWorld().getRegistryKey().equals(World.OVERWORLD) && p_41433_.getWorld().isSkyVisible(p_41433_.getSteppingPos().up().up()));
            if(p_41433_ instanceof ServerPlayerEntity) {
                System.out.println(0.01 * Math.pow((1.02930223664), ((ServerPlayerEntity) p_41433_).getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(SINCELASTHEX))));
            }

        }
        else {
            if(!p_41432_.isClient()) {
                p_41433_.addStatusEffect(new StatusEffectInstance(Spellblades.HEXED,20*60*3,0));
            }
        }

        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public boolean onClicked(ItemStack itemStack, ItemStack itemStack2, Slot slot, ClickType clickAction, PlayerEntity player, StackReference slotAccess) {
        return super.onClicked(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }
}
