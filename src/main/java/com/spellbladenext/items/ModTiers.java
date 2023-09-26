package com.spellbladenext.items;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum ModTiers implements ToolMaterial {
    WOOD(4, 2031, 9.0F, 0F, 15, () -> {
        return Ingredient.ofItems(Items.STRIPPED_MANGROVE_WOOD);
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Lazy<Ingredient> repairIngredient;

    private ModTiers(int j, int k, float f, float g, int l, Supplier<Ingredient> supplier) {
        this.level = j;
        this.uses = k;
        this.speed = f;
        this.damage = g;
        this.enchantmentValue = l;
        this.repairIngredient = new Lazy(supplier);
    }

    public int getDurability() {
        return this.uses;
    }

    public float getMiningSpeedMultiplier() {
        return this.speed;
    }

    public float getAttackDamage() {
        return this.damage;
    }

    public int getMiningLevel() {
        return this.level;
    }

    public int getEnchantability() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
