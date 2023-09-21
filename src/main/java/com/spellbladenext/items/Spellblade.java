package com.spellbladenext.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.spell_engine.api.item.weapon.SpellWeaponItem;
import net.spell_power.api.MagicSchool;

public class Spellblade extends SpellWeaponItem {
    MagicSchool school = MagicSchool.PHYSICAL_MELEE;
    public Spellblade(ToolMaterial material, Settings settings, int damage, float speed, MagicSchool school) {
        super(material, damage,speed, settings);
        this.school = school;
    }

    public MagicSchool getSchool() {
        return school;
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return state.isIn(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    public boolean isSuitableFor(BlockState state) {
        return state.isOf(Blocks.COBWEB);
    }

}
