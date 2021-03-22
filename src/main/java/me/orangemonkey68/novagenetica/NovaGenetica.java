package me.orangemonkey68.novagenetica;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.abilities.*;
import me.orangemonkey68.novagenetica.block.NovaGeneticaMachineBlock;
import me.orangemonkey68.novagenetica.blockentity.BaseMachineBlockEntity;
import me.orangemonkey68.novagenetica.blockentity.GeneAnalyzerBlockEntity;
import me.orangemonkey68.novagenetica.blockentity.GeneDecryptorBlockEntity;
import me.orangemonkey68.novagenetica.blockentity.GeneExtractorBlockEntity;
import me.orangemonkey68.novagenetica.gui.Generic1x1GuiDescription;
import me.orangemonkey68.novagenetica.helper.TextureHelper;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import me.orangemonkey68.novagenetica.helper.registration.LootTableHelper;
import me.orangemonkey68.novagenetica.helper.registration.RegistrationHelper;
import me.orangemonkey68.novagenetica.item.*;
import me.orangemonkey68.novagenetica.networking.NetworkHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.reborn.energy.Energy;

public class NovaGenetica implements ModInitializer {
    public static final String MOD_ID = "novagenetica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static MinecraftServer SERVER_INSTANCE;

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    public static final ScreenHandlerType<Generic1x1GuiDescription> GENERIC_1X1_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID, "generic_one_by_one"), ((syncId, inventory) -> new Generic1x1GuiDescription(syncId, inventory,ScreenHandlerContext.EMPTY)));

    public static final Identifier GENE_EXTRACTOR_ID = new Identifier(MOD_ID, "gene_extractor");
    public static NovaGeneticaMachineBlock GENE_EXTRACTOR_BLOCK;
    public static BlockEntityType<GeneExtractorBlockEntity> GENE_EXTRACTOR_BLOCK_ENTITY_TYPE;
    public static BlockItem GENE_EXTRACTOR_ITEM;

    public static final Identifier GENE_ANALYZER_ID = new Identifier(MOD_ID, "gene_analyzer");
    public static NovaGeneticaMachineBlock GENE_ANALYZER_BLOCK;
    public static BlockEntityType<GeneAnalyzerBlockEntity> GENE_ANALYZER_BLOCK_ENTITY_TYPE;
    public static BlockItem GENE_ANALYZER_ITEM;

    public static final Identifier GENE_DECRYPTOR_ID = new Identifier(MOD_ID, "gene_decryptor");
    public static NovaGeneticaMachineBlock GENE_DECRYPTOR_BLOCK;
    public static BlockEntityType<GeneDecryptorBlockEntity> GENE_DECRYPTOR_BLOCK_ENTITY_TYPE;
    public static BlockItem GENE_DECRYPTOR_ITEM;

    public static ItemGroup ITEM_GROUP;
    public static final Item EMPTY_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "empty_syringe"), new EmptySyringeItem(new Item.Settings().maxCount(16)));
    public static final Item FILLED_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "filled_syringe"), new FilledSyringeItem(new Item.Settings().maxCount(1)));
    public static final Item GENE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "gene"), new GeneItem(new Item.Settings().maxCount(1)));
    public static final Item MOB_FLAKES = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "mob_flakes"), new MobFlakesItem(new Item.Settings().maxCount(64)));

    public static final MobScraperItem MOB_SCRAPER_ITEM = Registry.register(
            Registry.ITEM,
            new Identifier(MOD_ID, "mob_scraper"),
            new MobScraperItem(
                    new ItemMaterialImpl(100, 1, 3, 0, 0, new Lazy<>(() -> Ingredient.ofItems(Items.IRON_BARS))),
                    new Item.Settings().maxCount(1)
            )
    );

    private static final RegistrationHelper REGISTRATION_HELPER = new RegistrationHelper(new Identifier(MOD_ID, "item_group"));

    @Override
    public void onInitialize() {
        AutoConfig.register(NovaGeneticaConfig.class, Toml4jConfigSerializer::new);
        NovaGenetica.LOGGER.info("Config registered");

        NetworkHandler.initServer();

        registerBlocks();

        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.START, new ItemStack(MOB_SCRAPER_ITEM));
        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.START, new ItemStack(EMPTY_SYRINGE_ITEM));

        ServerLifecycleEvents.SERVER_STARTING.register(server -> SERVER_INSTANCE = server);

        registerAbilities();

        registerColorProviders();

        LootTableHelper.init();

        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(Ability::onRegistryServer);

        ITEM_GROUP = REGISTRATION_HELPER.buildGroup(ItemHelper.getGene(null, new Identifier(MOD_ID, "none"), true, false, 0xFFFFFF));
        LOGGER.info("ItemGroup built");

        LOGGER.info("Done initializing");
    }

    @SuppressWarnings("ConstantConditions")
    void registerColorProviders(){
        // Gene
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if(tag != null){
                if(tag.contains("color")){
                    int color = tag.getInt("color") == TextureHelper.BAD_RETURN ? 0xFFFFFF : tag.getInt("color");
                    return tintIndex == 1 ? color : -1;
                }else if (tag.contains("ability")){
                    Identifier id = new Identifier(tag.getString("ability"));
                    if(NovaGenetica.ABILITY_REGISTRY.containsId(id)){
                        return tintIndex == 1 ? NovaGenetica.ABILITY_REGISTRY.get(id).getColor() : -1;
                    }
                }
            }

            return tintIndex == 1 ? 0xFFFFFF : -1;
        }, GENE_ITEM);

        //Filled Syringe
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if(tag != null){
                if (tag.contains("ability")){
                    Identifier id = new Identifier(tag.getString("ability"));
                    if(NovaGenetica.ABILITY_REGISTRY.containsId(id)){
                        return tintIndex == 0 ? NovaGenetica.ABILITY_REGISTRY.get(id).getColor() : -1;
                    }
                } else if (tag.contains("uuid")){
                    return tintIndex == 0 ? 0xe83c1a : -1;
                }
            }
            return tintIndex == 0 ? 0xFFFFFF : -1;
        }, FILLED_SYRINGE_ITEM);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if(tag != null) {
                if(tag.contains("color") && tag.getInt("color") != TextureHelper.BAD_RETURN){
                    return tag.getInt("color");
                }
            }
            return 0xFFFFFF;
        }, MOB_FLAKES);

        LOGGER.info("Color providers registered");
    }

    void registerBlocks() {
        Energy.registerHolder(BaseMachineBlockEntity.class, (object) -> new GeneExtractorBlockEntity());

        Item.Settings itemSettings = new Item.Settings().fireproof();

        NovaGeneticaMachineBlock geneExtractorBlock = new NovaGeneticaMachineBlock();
        geneExtractorBlock.setBlockEntity(GeneExtractorBlockEntity::new);
        GENE_EXTRACTOR_BLOCK = Registry.register(Registry.BLOCK, GENE_EXTRACTOR_ID, geneExtractorBlock);
        GENE_EXTRACTOR_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, GENE_EXTRACTOR_ID, BlockEntityType.Builder.create(GeneExtractorBlockEntity::new, GENE_EXTRACTOR_BLOCK).build(null));
        GENE_EXTRACTOR_ITEM = Registry.register(Registry.ITEM, GENE_EXTRACTOR_ID, new BlockItem(GENE_EXTRACTOR_BLOCK, itemSettings));
        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.MACHINE, new ItemStack(GENE_EXTRACTOR_ITEM));

        NovaGeneticaMachineBlock geneAnalyzerBlock = new NovaGeneticaMachineBlock();
        geneAnalyzerBlock.setBlockEntity(GeneAnalyzerBlockEntity::new);
        GENE_ANALYZER_BLOCK = Registry.register(Registry.BLOCK, GENE_ANALYZER_ID, geneAnalyzerBlock);
        GENE_ANALYZER_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, GENE_ANALYZER_ID, BlockEntityType.Builder.create(GeneAnalyzerBlockEntity::new, GENE_ANALYZER_BLOCK).build(null));
        GENE_ANALYZER_ITEM = Registry.register(Registry.ITEM, GENE_ANALYZER_ID, new BlockItem(GENE_ANALYZER_BLOCK, itemSettings));
        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.MACHINE, new ItemStack(GENE_ANALYZER_ITEM));

        NovaGeneticaMachineBlock geneDecryptorBlock = new NovaGeneticaMachineBlock();
        geneDecryptorBlock.setBlockEntity(GeneDecryptorBlockEntity::new);
        GENE_DECRYPTOR_BLOCK = Registry.register(Registry.BLOCK, GENE_DECRYPTOR_ID, geneDecryptorBlock);
        GENE_DECRYPTOR_BLOCK_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, GENE_DECRYPTOR_ID, BlockEntityType.Builder.create(GeneDecryptorBlockEntity::new, GENE_DECRYPTOR_BLOCK).build(null));
        GENE_DECRYPTOR_ITEM = Registry.register(Registry.ITEM, GENE_DECRYPTOR_ID, new BlockItem(GENE_DECRYPTOR_BLOCK, itemSettings));
        REGISTRATION_HELPER.addItemToGroup(RegistrationHelper.Subsection.MACHINE, new ItemStack(GENE_DECRYPTOR_ITEM));

        LOGGER.info("Machines registered");
    }

    void registerAbilities() {
        REGISTRATION_HELPER.register(new AbilityEatGrass(), new Identifier(MOD_ID, "eat_grass"));
        REGISTRATION_HELPER.register(new AbilityResistance(), new Identifier(MOD_ID, "resistance"));
        REGISTRATION_HELPER.register(new AbilityScareCreepers(), new Identifier(MOD_ID, "scare_creepers"));
        REGISTRATION_HELPER.register(new AbilityNone(), new Identifier(MOD_ID, "none"));

        LOGGER.info("Abilities registered");
    }

    public static NovaGeneticaConfig getConfig() {
        return AutoConfig.getConfigHolder(NovaGeneticaConfig.class).get();
    }
}
