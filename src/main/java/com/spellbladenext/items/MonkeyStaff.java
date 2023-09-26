package com.spellbladenext.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonkeyStaff extends AxeItem {
    public MonkeyStaff(float f, float g, Settings properties) {
        super(ModTiers.WOOD, f, g, properties);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext useOnContext) {
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World level, List<Text> list, TooltipContext tooltipFlag) {
        super.appendTooltip(itemStack, level, list, tooltipFlag);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity livingEntity, LivingEntity attacker) {
        stack.damage(1, attacker, (e) -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        return true;
    }
}
