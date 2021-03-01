package me.orangemonkey68.novagenetica;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityEatGrass;
import me.orangemonkey68.novagenetica.abilities.AbilityResistance;
import me.orangemonkey68.novagenetica.abilities.AbilityScareCreepers;
import me.orangemonkey68.novagenetica.commands.GiveAbilityCommand;
import me.orangemonkey68.novagenetica.item.*;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class NovaGenetica implements ModInitializer {
    public static final String MOD_ID = "novagenetica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    public static ItemGroup ITEM_GROUP;
    public static final Item EMPTY_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "empty_syringe"), new EmptySyringeItem(new Item.Settings().maxCount(16)));
    public static final Item FILLED_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "filled_syringe"), new FilledSyringeItem(new Item.Settings().maxCount(1)));
    public static final Item GENE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "gene"), new GeneItem(new Item.Settings().maxCount(1)));
    public static final Item MOB_FLAKES = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mob_flakes"), new MobFlakesItem(new Item.Settings().maxCount(64)));
    public static final Item COMPLETE_GENE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "complete_gene"), new CompleteGeneItem(new Item.Settings().maxCount(1)));

    private static final RegistrationHelper REGISTRATION_HELPER = new RegistrationHelper(new Identifier(MOD_ID, "item_group"));
    @Override
    public void onInitialize() {
        AutoConfig.register(NovaGeneticaConfig.class, Toml4jConfigSerializer::new);

        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.START, new ItemStack(EMPTY_SYRINGE_ITEM));

        registerAbilities();

        //Register colorproviders
        ColorProviderRegistry.ITEM.register(new NovaGeneticaItemColorProvider(), FILLED_SYRINGE_ITEM);
        ColorProviderRegistry.ITEM.register(new NovaGeneticaItemColorProvider(), GENE_ITEM);
        ColorProviderRegistry.ITEM.register(new NovaGeneticaItemColorProvider(), MOB_FLAKES);
        ColorProviderRegistry.ITEM.register(new NovaGeneticaItemColorProvider(), COMPLETE_GENE_ITEM);

        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(Ability::onRegistryServer);

        //Register /giveability command
        //TODO: Fix /giveability command
        CommandRegistrationCallback.EVENT.register(new GiveAbilityCommand());

        ITEM_GROUP = REGISTRATION_HELPER.buildGroup(ItemHelper.getCompleteGene(new Identifier(MOD_ID, "none")));
    }

    void registerAbilities(){
        REGISTRATION_HELPER.register(
                new AbilityEatGrass(),
                new Identifier(MOD_ID, "eat_grass"),
                REGISTRATION_HELPER.generateEntityTypeColorMap(
                        Arrays.asList(EntityType.SHEEP),
                        Arrays.asList(0xFFFFFF)
                )
        );

        REGISTRATION_HELPER.register(
                new AbilityResistance(),
                new Identifier(MOD_ID, "resistance"),
                REGISTRATION_HELPER.generateEntityTypeColorMap(
                        Arrays.asList(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER),
                        Arrays.asList(0x276339, 0x276339)
                )
        );

        REGISTRATION_HELPER.register(
                new AbilityScareCreepers(),
                new Identifier(MOD_ID, "scare_creepers"),
                REGISTRATION_HELPER.generateEntityTypeColorMap(
                        Arrays.asList(EntityType.OCELOT, EntityType.CAT),
                        Arrays.asList(0xf2c26d, 0xf2ad35)
                )
        );
    }

    public static NovaGeneticaConfig getConfig(){
        return AutoConfig.getConfigHolder(NovaGeneticaConfig.class).get();
    }
}
