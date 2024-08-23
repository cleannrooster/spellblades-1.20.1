package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import com.spellbladenext.SpellbladesClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.trinket.SpellBookTrinketItem;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.spell.*;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.mixin.ItemStackMixin;
import net.spell_engine.spellbinding.SpellBindingScreen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.spell_engine.internals.SpellContainerHelper.containerFromItemStack;

public class RandomSpellOil extends Item {
    public RandomSpellOil(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        SpellContainer tabulaContainer = SpellContainerHelper.containerFromItemStack(stack);
        if(tabulaContainer == null){
            return Text.translatable("spellblades.unidentified").append(super.getName(stack));
        }
        else{
            if(tabulaContainer.spell_ids.get(0) != null) {
                return Text.translatable(SpellTooltip.spellTranslationKey(new Identifier(tabulaContainer.spell_ids.get(0))))
                        .append(Text.of(" "))
                        .append( super.getName(stack));
            }

        }

        return super.getName(stack);
    }

    public static boolean matches(String subject, String nullableRegex) {
        if (subject == null) {
            return false;
        }
        if (nullableRegex == null || nullableRegex.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(nullableRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(subject);
        return matcher.find();
    }
    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (matches(Registries.ITEM.getId(slot.getStack().getItem()).toString(), Spellblades.config.blacklist_spell_oil_regex)) {
            player.sendMessage(Text.translatable("This item is blacklisted by the server."));
            return true;
        }
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
            spells.add(new Identifier(Spellblades.MOD_ID,"smite"));
            spells.add(new Identifier(Spellblades.MOD_ID,"whirlwind"));
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
