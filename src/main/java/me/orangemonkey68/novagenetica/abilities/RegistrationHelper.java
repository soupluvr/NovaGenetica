package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.item.FilledSyringeItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RegistrationHelper {
    private final FabricItemGroupBuilder itemGroupBuilder;

    public RegistrationHelper (Identifier itemGroupId) {
        this.itemGroupBuilder = FabricItemGroupBuilder.create(itemGroupId);
    }

    private static final Registry<Ability> ABILITY_REGISTRY = NovaGenetica.ABILITY_REGISTRY;
    private final List<ItemStack> itemList = new ArrayList<>();

    /**
     * Registers all the genes, abilities, syringes, and other items for you.
     * @param ability the {@link Ability} to register.
     * @param abilityId the {@link Identifier} of the ability.
     * @param entityTypes the {@link Set} of entities that can produce drops that create this ability
     * @param color the {@link Integer} representing the color of the syringe. Represented in {@code 0xRRGGBB} format.
     */
    public void register(Ability ability, Identifier abilityId, Set<EntityType<?>> entityTypes, int color){
        if(!AbilityValidator.validate(ability)) {
            NovaGenetica.LOGGER.error("Ability: \"{}\" has failed a check. Submit a bug report to the mod authors!", abilityId);
            return;
        }
        //Register ability to ABILITY_REGISTRY
        Registry.register(ABILITY_REGISTRY, abilityId, ability);

        //Add item to SYRINGE_LIST so that it can be registered in finishRegister()
        ItemStack syringe = FilledSyringeItem.stackOf(abilityId, color);
        itemList.add(syringe);

        //Adds to Ability.ABILITY_ENTITY_MAP
        Ability.ABILITY_ENTITY_MAP.put(ability, entityTypes);

        //Register abilities to entity
        entityTypes.forEach(type -> ((NovaGeneticaEntityType)type).registerAbility(ability));
    }

    /**
     * Adds an {@link ItemStack} to the {@link ItemGroup} returned in {@link #buildGroup(ItemStack)}
     * <b>NOTE:</b> the order in which you add items here will be the same order they appear in the {@link ItemGroup}
     * @param stack the {@link ItemStack} to add
     */
    public void addItemToGroup(ItemStack stack){
        itemList.add(stack);
    }

    /**
     *
     * @param groupIcon the {@link ItemStack} to show on the tab in the creative inventory
     * @return the completed ItemGroup
     */
    public ItemGroup buildGroup(ItemStack groupIcon){
        return itemGroupBuilder.icon(() -> groupIcon).appendItems(list -> list.addAll(itemList)).build();
    }
}
