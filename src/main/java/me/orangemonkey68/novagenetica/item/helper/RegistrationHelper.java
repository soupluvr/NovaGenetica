package me.orangemonkey68.novagenetica.item.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityValidator;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
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

    /**
     * A map of each EntityTypee and the set of abilities they can drop
     */
    public static final Map<EntityType<?>, Set<Ability>> ENTITY_TYPE_ABILITY_MAP = new HashMap<>();
    /**
     * A map of each EntityType and their color
     */
    public static final Map<EntityType<?>, Integer> ENTITY_TYPE_COLOR_MAP = new HashMap<>();

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
     * @param entityTypeColorMap a map of all the entityTypes that can drop this ability, and the colors their {@code Mob Flakes} should be
     */
    public void register(Ability ability, Identifier abilityId, Map<EntityType<?>, Integer> entityTypeColorMap){
        if(!AbilityValidator.validate(ability)) {
            NovaGenetica.LOGGER.error("Ability: \"{}\" has failed a check. Submit a bug report to the mod authors!", abilityId);
            return;
        }
        //Register ability to ABILITY_REGISTRY
        Registry.register(ABILITY_REGISTRY, abilityId, ability);

        //Create and put syringe in itemMap
        ItemStack syringe = ItemHelper.getSyringe(abilityId);
        addItemToGroup(Subsection.SYRINGE, syringe);

        //Genes need all of the Abilities to be registered, so they're registered in the build function
        //Loops over given EntityTypes and registers their colors and maps their colors, and registers a Mob Flake
        entityTypeColorMap.forEach((type, mobColor) -> {
            if(!ENTITY_TYPE_ABILITY_MAP.containsKey(type))
                ENTITY_TYPE_ABILITY_MAP.put(type, new HashSet<>());
            ENTITY_TYPE_ABILITY_MAP.get(type).add(ability);

            if(!ENTITY_TYPE_COLOR_MAP.containsKey(type)){
                ENTITY_TYPE_COLOR_MAP.put(type, mobColor);
                addItemToGroup(Subsection.INCOMPLETE_GENE, ItemHelper.getGene(Registry.ENTITY_TYPE.getId(type), null, false, false));
                addItemToGroup(Subsection.MOB_FLAKES, ItemHelper.getMobFlakes(Registry.ENTITY_TYPE.getId(type)));
            }

        });

        addItemToGroup(Subsection.COMPLETE_GENE, ItemHelper.getGene(null, abilityId, true, true));

        //Adds to Ability.ABILITY_ENTITY_MAP
        Ability.ABILITY_ENTITY_MAP.put(ability, entityTypeColorMap.keySet());

        //Register abilities to entity
        entityTypeColorMap.forEach((type, entityColor) -> ((NovaGeneticaEntityType)type).registerAbility(ability));
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

    /**
     * Generates a map of {@link EntityType}s and colors for use in {@link #register(Ability, Identifier, Map)}
     * @param types a {@link List} of {@link EntityType}s
     * @param colors a {@link List} of colors, represented in {@code 0xRRGGBB} format
     * @return a {@link HashMap} of each EntityType : int pair
     */
    public Map<EntityType<?>, Integer> generateEntityTypeColorMap (List<EntityType<?>> types, List<Integer> colors) {
        if(types.size() != colors.size()) throw new IllegalArgumentException("The EntityType and Color list must be the same length.");

        HashMap<EntityType<?>, Integer> entityTypeColorMap = new HashMap<>();

        for (int i = 0; i < types.size(); i++) {
            entityTypeColorMap.put(types.get(i), colors.get(i));
        }

        return entityTypeColorMap;
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
