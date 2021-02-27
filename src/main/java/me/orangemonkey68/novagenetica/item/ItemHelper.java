package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.RegistrationHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    private ItemHelper(){

    }

    /**
     * Generates an {@link NovaGenetica#MOB_FLAKES} ItemStack with the Identifier or the EntityType and entityColor
     * @param entityType The {@link EntityType} that this flake dropped from
     * @return an {@link NovaGenetica#MOB_FLAKES} with the correct NBT tags.
     */
    @NotNull
    public static ItemStack stackWithEntityType(EntityType<?> entityType, Item item){
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("entityType", Registry.ENTITY_TYPE.getId(entityType).toString());

        tag.putInt("color", RegistrationHelper.ENTITY_TYPE_COLOR_MAP.get(entityType));
        stack.setTag(tag);
        return stack;
    }

    @NotNull
    public static ItemStack stackWithAbility(Identifier abilityId, Item item){
        ItemStack stack = new ItemStack(item);
        CompoundTag tag = stack.getOrCreateTag();
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityId);
        tag.putString("ability", abilityId.toString());
        if(ability != null){
            tag.putInt("color", ability.getColor());
        }
        stack.setTag(tag);
        return stack;
    }
}
