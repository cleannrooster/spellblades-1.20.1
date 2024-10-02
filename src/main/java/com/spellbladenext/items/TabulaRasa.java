package com.spellbladenext.items;

import com.spellbladenext.Spellblades;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.item.trinket.SpellBookTrinketItem;
import net.spell_engine.internals.SpellContainerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabulaRasa extends SpellBookTrinketItem {
    public TabulaRasa(Identifier poolId, Settings settings) {
        super(poolId, settings);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.translatable("item.spellbladenext.tabula_rasa.desc").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        tooltip.add(Text.translatable("item.spellbladenext.tabula_rasa.desc2").formatted(Formatting.GRAY));
        if(!Spellblades.config.tab){
            tooltip.add(Text.translatable("DISABLED via server config").formatted(Formatting.RED).formatted(Formatting.BOLD));

        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
