package com.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.trinket.SpellBookTrinketItem;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.api.spell.SpellPool;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.spell_engine.api.item.trinket.SpellBooks.itemIdFor;

public class ThesisBook extends SpellBookTrinketItem implements SpellBookItem {
    private final Identifier poolId;

    public ThesisBook( Identifier poolId, Item.Settings settings) {
        super(poolId,settings);
        this.poolId = poolId;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public Identifier getPoolId() {
        return this.poolId;
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.spellbladenext.thesis_spell_book.desc").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(SpellContainerHelper.containerFromItemStack(stack).spell_ids.isEmpty()) {
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
            SpellContainerHelper.addSpell((Identifier) spells.toArray()[world.getRandom().nextInt(spells.size())], stack);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
