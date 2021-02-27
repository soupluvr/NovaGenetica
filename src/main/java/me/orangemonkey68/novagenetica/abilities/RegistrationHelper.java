package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.item.ItemHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegistrationHelper {
    private static final Registry<Ability> ABILITY_REGISTRY = NovaGenetica.ABILITY_REGISTRY;

    private final FabricItemGroupBuilder itemGroupBuilder;
    private final Map<Subsection, List<ItemStack>> itemMap = new HashMap<>();


    public RegistrationHelper (Identifier itemGroupId) {
        this.itemGroupBuilder = FabricItemGroupBuilder.create(itemGroupId);
        //Set up map
        itemMap.put(Subsection.START, new ArrayList<>());
        itemMap.put(Subsection.SYRINGE, new ArrayList<>());
        itemMap.put(Subsection.GENE, new ArrayList<>());
        itemMap.put(Subsection.MOB_FLAKES, new ArrayList<>());
        itemMap.put(Subsection.END, new ArrayList<>());
    }

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

        //Create and put syringe in itemMap
        ItemStack syringe = ItemHelper.stackOf(abilityId, color, NovaGenetica.FILLED_SYRINGE_ITEM);
        addItemToGroup(Subsection.SYRINGE, syringe);

        //Create and put gene in itemMap
        ItemStack gene = ItemHelper.stackOf(abilityId, color, NovaGenetica.GENE_ITEM);
        addItemToGroup(Subsection.GENE, gene);

        //Create and put flake in itemMap
        ItemStack flake = ItemHelper.stackOf(abilityId, color, NovaGenetica.MOB_FLAKES);
        addItemToGroup(Subsection.MOB_FLAKES, flake);

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
    public void addItemToGroup(Subsection section, ItemStack stack){
        itemMap.get(section).add(stack);
    }

    /**
     *
     * @param groupIcon the {@link ItemStack} to show on the tab in the creative inventory
     * @return the completed ItemGroup
     */
    public ItemGroup buildGroup(ItemStack groupIcon){
        List<ItemStack> totalList = Stream.of(
                itemMap.get(Subsection.START),
                itemMap.get(Subsection.SYRINGE),
                itemMap.get(Subsection.GENE),
                itemMap.get(Subsection.MOB_FLAKES),
                itemMap.get(Subsection.END)
        ).flatMap(Collection::stream).collect(Collectors.toList());

        return itemGroupBuilder.icon(() -> groupIcon)
                .appendItems(list -> list.addAll(totalList))
                .build();
    }

    public enum Subsection {
        START,
        SYRINGE,
        GENE,
        MOB_FLAKES,
        END
    }
}
