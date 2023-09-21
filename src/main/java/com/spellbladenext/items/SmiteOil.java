package com.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.world.World;
import net.spell_engine.internals.SpellContainerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.spell_engine.internals.SpellContainerHelper.containerFromItemStack;

public class SmiteOil extends Item {
    public SmiteOil(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if(clickType == ClickType.RIGHT && slot.getStack() != null && slot.getStack().getItem() instanceof ToolItem item && !(SpellContainerHelper.containerFromItemStack(slot.getStack()) != null && SpellContainerHelper.containerFromItemStack(slot.getStack()).spell_ids.contains("spellbladenext:smite"))){
            ItemStack stack1 = slot.getStack();
            NbtCompound compound = stack1.getOrCreateNbt();
            List<String> stringlist = List.of();
            if(containerFromItemStack(stack1) != null) {
                stringlist = containerFromItemStack(stack1).spell_ids;
            }
            NbtList list = new NbtList();

            for(String string : stringlist){
                list.add(NbtString.of(string));
            }
            list.add(NbtString.of("spellbladenext:smite"));
            NbtCompound compound1 = stack1.getOrCreateNbt().getCompound("spell_container");
            compound1.putBoolean("is_proxy",true);
            compound1.put("spell_ids", list);
            compound.remove("spell_container");

            compound.put("spell_container",compound1);

            stack.increment(-1);
            return true;

        }
        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("Adds Smite to a tool, and turns it into a spell container, if it isn't already."));
        tooltip.add(Text.translatable("Drag over an item and right click to use. IRREVERSIBLE. USE CAREFULLY."));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
