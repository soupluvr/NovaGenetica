package me.orangemonkey68.novagenetica.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public class ItemMaterialImpl implements ToolMaterial {
    final int durability;
    final float multiplier;
    final float attackDamage;
    final int miningLevel;
    final int enchantability;
    final Lazy<Ingredient> repairIngredient;

    public ItemMaterialImpl(int durability, float multiplier, float attackDamage, int miningLevel, int enchantability, Lazy<Ingredient> repairIngredient){
        this.durability = durability;
        this.multiplier = multiplier;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return multiplier;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return miningLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }


}
