package com.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellPool;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.spell_engine.internals.SpellContainerHelper.containerFromItemStack;

public class RandomSpellOil extends Item {
    public RandomSpellOil(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if(clickType == ClickType.RIGHT && slot.getStack() != null && slot.getStack().getItem() instanceof TabulaRasa item && SpellContainerHelper.containerFromItemStack(stack) != null){
            SpellContainer tabulaContainer = SpellContainerHelper.containerFromItemStack(slot.getStack());
            if(tabulaContainer == null){
                tabulaContainer = new SpellContainer(false,"",6,new ArrayList<>());
            }
            if(tabulaContainer.spell_ids.size() < 6) {
                List<String> strings = new ArrayList<>(tabulaContainer.spell_ids);
                if(containerFromItemStack(stack) != null) {
                    strings.addAll(containerFromItemStack(stack).spell_ids);
                }
                tabulaContainer.spell_ids = strings;
                SpellContainerHelper.addContainerToItemStack(tabulaContainer,slot.getStack());
                stack.increment(-1);

                return true;
            }
            else{
                return false;
            }
        }
        if(clickType == ClickType.RIGHT && slot.getStack() != null && slot.getStack().getItem() instanceof ToolItem item && SpellContainerHelper.containerFromItemStack(stack) != null
                && SpellContainerHelper.containerFromItemStack(slot.getStack()) != null &&  containerFromItemStack(slot.getStack()).spell_ids.isEmpty()){
            SpellContainer container = containerFromItemStack(stack);
            container.is_proxy = true;
            SpellContainerHelper.addContainerToItemStack(container,slot.getStack());
            stack.increment(-1);

            return true;
        }

        return super.onStackClicked(stack, slot, clickType, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(SpellContainerHelper.containerFromItemStack(stack) == null ){

            List<SpellBookItem> books = SpellBooks.sorted();
            List<SpellPool> pools = new ArrayList<SpellPool>();
            List<Identifier> spells = new ArrayList<Identifier>();

            for (SpellBookItem book : books) {
                pools.add(SpellRegistry.spellPool(book.getPoolId()));
            }
            for (SpellPool pool : pools) {
                spells.addAll(pool.spellIds());
            }
            spells.remove(new Identifier("spellbladenext:thesis"));
            spells.removeIf(spell ->
                SpellRegistry.getSpell(spell).school.equals(ExternalSpellSchools.PHYSICAL_RANGED)
            );
            SpellContainer container = new SpellContainer(false,"",1,List.of((spells.toArray()[world.getRandom().nextInt(spells.size())]).toString()));

            SpellContainerHelper.addContainerToItemStack(container, stack);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.spellbladenext.spelloil.desc1"));
        tooltip.add(Text.translatable("item.spellbladenext.spelloil.desc2"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
