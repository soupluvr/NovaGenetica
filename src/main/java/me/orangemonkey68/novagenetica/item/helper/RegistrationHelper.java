package me.orangemonkey68.novagenetica.item.helper;

import me.orangemonkey68.novagenetica.LootTableManager;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityValidator;
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

    /**
     * A map of each EntityTypee and the set of abilities they can drop
     */
    public static final Map<EntityType<?>, HashMap<Ability, Integer>> ENTITY_TYPE_ABILITY_WEIGHT_MAP = new HashMap<>();
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
            //put the submap if it doesn't exist
            ENTITY_TYPE_ABILITY_WEIGHT_MAP.putIfAbsent(type, new HashMap<>());
            //put the ability and weight
            ENTITY_TYPE_ABILITY_WEIGHT_MAP.get(type).put(ability, ability.getLootTableWeight());

            Identifier typeId = Registry.ENTITY_TYPE.getId(type);

            LootTableManager.register(Registry.ENTITY_TYPE.getId(type), ability);

            ItemStack incompleteGene = ItemHelper.getGene(typeId, null, false, false);

            if(!itemMap.get(Subsection.INCOMPLETE_GENE).contains(incompleteGene)){
                itemMap.get(Subsection.INCOMPLETE_GENE).add(incompleteGene);
            }

            ItemStack mobFlake = ItemHelper.getMobFlakes(typeId);
            if(!itemMap.get(Subsection.MOB_FLAKES).contains(mobFlake)){
                itemMap.get(Subsection.MOB_FLAKES).add(mobFlake);
            }
        });

        addItemToGroup(Subsection.COMPLETE_GENE, ItemHelper.getGene(null, abilityId, true, true));

        //Adds to Ability.ABILITY_ENTITY_MAP
        Ability.ABILITY_ENTITY_MAP.put(ability, ENTITY_TYPE_COLOR_MAP.keySet());

        //Register abilities to entity
        ENTITY_TYPE_COLOR_MAP.forEach((type, entityColor) -> ((NovaGeneticaEntityType)type).registerAbility(ability));
    }

    public void registerEntityColor(int color, EntityType<?> entityType){
        ENTITY_TYPE_COLOR_MAP.put(entityType, color);
    }

    /**
     * Gets the color of any entity registered with {@link #registerEntityColor(int, EntityType)}
     * @param entityType the {@link EntityType} to query
     * @return the color if registerred, otherwise it will return {@code 0xFFFFFF}
     */
    public static int getEntityColor(EntityType<?> entityType){
        return ENTITY_TYPE_COLOR_MAP.getOrDefault(entityType, 0xFFFFFF);
    }

    /**
     * Gets the color of any entity registered with {@link #registerEntityColor(int, EntityType)}
     * @param id the Identifier of the {@link EntityType}
     * @return the color if registerred, otheriwse it will return {@code 0xFFFFFF}
     */
    public static int getEntityColor(Identifier id){
        if(Registry.ENTITY_TYPE.containsId(id) && ENTITY_TYPE_COLOR_MAP.containsKey(Registry.ENTITY_TYPE.get(id))){
            return ENTITY_TYPE_COLOR_MAP.get(Registry.ENTITY_TYPE.get(id));
        }
        
        return 0xFFFFFF;
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
     * Generates a map of {@link EntityType}s and colors for use in {@link #register(Ability, Identifier)}
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
