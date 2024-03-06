package com.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.item.weapon.StaffItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TheSpark extends StaffItem {
    public TheSpark(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> list, TooltipContext context) {
        list.add(Text.translatable("Spells of non-Arcane Magic Schools").formatted(Formatting.LIGHT_PURPLE));
        list.add(Text.translatable("use your Arcane Power instead.").formatted(Formatting.LIGHT_PURPLE));
        list.add(Text.translatable("Pure arcana, woven like fabric,").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        list.add(Text.translatable("scalding or freezing to the touch.").formatted(Formatting.RED).formatted(Formatting.ITALIC));
        super.appendTooltip(stack, world, list, context);
    }
}
