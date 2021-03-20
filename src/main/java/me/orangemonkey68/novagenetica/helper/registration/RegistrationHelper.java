package me.orangemonkey68.novagenetica.helper.registration;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityValidator;
import me.orangemonkey68.novagenetica.accessor.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.helper.TextureHelper;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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
        itemMap.put(Subsection.MACHINE, new ArrayList<>());
        itemMap.put(Subsection.SYRINGE, new ArrayList<>());
        itemMap.put(Subsection.COMPLETE_GENE, new ArrayList<>());
        itemMap.put(Subsection.INCOMPLETE_GENE, new ArrayList<>());
        itemMap.put(Subsection.MOB_FLAKES, new ArrayList<>());
        itemMap.put(Subsection.END, new ArrayList<>());
    }

    /**
     * Registers all the genes, abilities, syringes, and other items for you.
     * @param ability the {@link Ability} to register.
     * @param abilityId the {@link Identifier} of the ability.
     */
    public void register(Ability ability, Identifier abilityId){
        if(!AbilityValidator.validate(ability)) {
            NovaGenetica.LOGGER.error("Ability: \"{}\" has failed a check. Submit a bug report to the mod authors!", abilityId);
            return;
        }
        //Register ability to ABILITY_REGISTRY
        Registry.register(ABILITY_REGISTRY, abilityId, ability);

        //Create and put syringe in itemMap
        ItemStack syringe = ItemHelper.getSyringe(abilityId);
        addItemToGroup(Subsection.SYRINGE, syringe);

        ability.getEntityTypes().forEach(type -> {
            Identifier typeId = Registry.ENTITY_TYPE.getId(type);

            LootTableHelper.registerLootEntry(Registry.ENTITY_TYPE.getId(type), ability);

            ItemStack incompleteGene = ItemHelper.getGene(typeId, null, false, false, TextureHelper.BAD_RETURN);

            if(!itemMap.get(Subsection.INCOMPLETE_GENE).contains(incompleteGene)){
                itemMap.get(Subsection.INCOMPLETE_GENE).add(incompleteGene);
            }

            ItemStack mobFlake = ItemHelper.getMobFlakes(typeId, TextureHelper.BAD_RETURN);
            if(!itemMap.get(Subsection.MOB_FLAKES).contains(mobFlake)){
                itemMap.get(Subsection.MOB_FLAKES).add(mobFlake);
            }

//            NovaGenetica.LOGGER.info(typeId.toString());

            ((NovaGeneticaEntityType)type).registerAbility(ability);
            NovaGenetica.LOGGER.info(((NovaGeneticaEntityType)type).canGiveAbility(ability));
        });

        addItemToGroup(Subsection.COMPLETE_GENE, ItemHelper.getGene(null, abilityId, true, true, TextureHelper.BAD_RETURN));

        //Adds to Ability.ABILITY_ENTITY_MAP
        Ability.ABILITY_ENTITY_MAP.put(ability, ability.getEntityTypes());
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
                itemMap.get(Subsection.MACHINE),
                itemMap.get(Subsection.SYRINGE),
                itemMap.get(Subsection.COMPLETE_GENE),
                itemMap.get(Subsection.INCOMPLETE_GENE),
                itemMap.get(Subsection.MOB_FLAKES),
                itemMap.get(Subsection.END)
        ).flatMap(Collection::stream).collect(Collectors.toList());

        return itemGroupBuilder.icon(() -> groupIcon)
                .appendItems(list -> list.addAll(totalList))
                .build();
    }

    public enum Subsection {
        START,
        MACHINE,
        SYRINGE,
        COMPLETE_GENE,
        INCOMPLETE_GENE,
        MOB_FLAKES,
        END
    }
}
