package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.RegistrationHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemHelper {
    private ItemHelper(){

    }

    public static ItemStack getSyringe(Identifier abilityId){
        ItemStack stack = new ItemStack(NovaGenetica.FILLED_SYRINGE_ITEM);
        CompoundTag tag = new CompoundTag();
        tag.putString("ability", abilityId.toString());
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityId);
        tag.putInt("color", ability != null ? ability.getColor() : 0xFFFFFF);
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getCompleteGene(Identifier abilityId){
        ItemStack stack = new ItemStack(NovaGenetica.COMPLETE_GENE_ITEM);
        CompoundTag tag = new CompoundTag();
        tag.putString("ability", abilityId.toString());
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityId);
        tag.putInt("color", ability != null ? ability.getColor() : 0xFFFFFF);
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getGene(Identifier entityTypeId){
        ItemStack stack = new ItemStack(NovaGenetica.GENE_ITEM);
        CompoundTag tag = new CompoundTag();
        tag.putString("entityType", entityTypeId.toString());
        if(Registry.ENTITY_TYPE.containsId(entityTypeId)){
            EntityType<?> type = Registry.ENTITY_TYPE.get(entityTypeId);
            tag.putInt("color", RegistrationHelper.ENTITY_TYPE_COLOR_MAP.getOrDefault(type, 0xFFFFFF));
        } else {
            tag.putInt("color", 0xFFFFFF);
        }
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getMobFlakes(EntityType<?> entityType){
        ItemStack stack = new ItemStack(NovaGenetica.MOB_FLAKES);
        CompoundTag tag = new CompoundTag();
        tag.putInt("color", RegistrationHelper.ENTITY_TYPE_COLOR_MAP.getOrDefault(entityType, 0xFFFFFF));
        tag.putString("entityType", Registry.ENTITY_TYPE.getId(entityType).toString());
        stack.setTag(tag);

        return stack;
    }
}
