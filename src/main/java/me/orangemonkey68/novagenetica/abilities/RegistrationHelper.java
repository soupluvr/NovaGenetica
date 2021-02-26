package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaEntityType;
import me.orangemonkey68.novagenetica.item.FilledSyringeItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Set;

public class RegistrationHelper {
    private static Registry<Ability> ABILITY_REGISTRY = NovaGenetica.ABILITY_REGISTRY;

    public static final ItemGroup SYRINGE_ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(NovaGenetica.MOD_ID, "syringe_item_group"),
            () -> new ItemStack(Items.COBBLESTONE));

    public static void register(Ability ability, Identifier abilityId, Identifier itemId, Set<EntityType<?>> entityTypes, int color){
        if(!AbilityValidator.validate(ability)) {
            NovaGenetica.LOGGER.error("Ability: \"{}\" has failed a check. Submit a bug report to the mod authors!", abilityId);
        }

        FilledSyringeItem syringe = new FilledSyringeItem(new Item.Settings().group(SYRINGE_ITEM_GROUP), abilityId);

        //Register ability to ABILITY_REGISTRY
        Registry.register(ABILITY_REGISTRY, abilityId, ability);
        //Register item to item registry
        Registry.register(Registry.ITEM, itemId, syringe);

        //Adds to Ability.ABILITY_ENTITY_MAP
        Ability.ABILITY_ENTITY_MAP.put(ability, entityTypes);

        //Register color provider
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> color, syringe);
        //Register abilities to entity
        entityTypes.forEach(type -> ((NovaGeneticaEntityType)type).registerAbility(ability));

    }
}
