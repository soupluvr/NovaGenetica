package me.orangemonkey68.novagenetica.helper.registration;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.helper.TextureHelper;
import me.orangemonkey68.novagenetica.helper.item.ItemHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.HashMap;
import java.util.Map;

public class LootTableHelper {
    public static final RegistryKey<Registry<LootTable>> LOOT_TABLE_REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(NovaGenetica.MOD_ID, "loot_table"));
    public static Registry<LootTable> LOOT_TABLE_REGISTRY = null;

    private static final Map<Identifier, LootPool.Builder> PRE_ENTITY_LOOT_POOL_MAP = new HashMap<>();

    public static void init(){
        ServerWorldEvents.LOAD.register((server, world) -> LootTableHelper.build());
        ServerWorldEvents.UNLOAD.register((server, world) -> LootTableHelper.LOOT_TABLE_REGISTRY = null);
        NovaGenetica.LOGGER.info("LootTable callbacks initiated");
    }

    /**
     * Registers a loot entry for any given entity type and ability.
     * @param entityTypeId the ID of the entity type
     * @param ability the ability to register
     */
    public static void registerLootEntry(Identifier entityTypeId, Ability ability){
        NovaGenetica.LOGGER.info("Adding ability entry \"{}\" to loot table \"{}\"", NovaGenetica.ABILITY_REGISTRY.getId(ability), entityTypeId.toString());

        //add new builder if key doesn't exist
        PRE_ENTITY_LOOT_POOL_MAP.putIfAbsent(entityTypeId, LootPool.builder());
        //get loot pool builder we just added
        LootPool.Builder poolBuilder = PRE_ENTITY_LOOT_POOL_MAP.get(entityTypeId);

        //get the NBT of the correct ItemStack
        CompoundTag nbt = ItemHelper.getGene(null, NovaGenetica.ABILITY_REGISTRY.getId(ability), false, false, TextureHelper.BAD_RETURN).getTag();
        //Makes an ItemEntryBuilder of a GeneItem
        ItemEntry.Builder entryBuilder = ItemEntry.builder(NovaGenetica.GENE_ITEM);
        //adds a "set nbt data" function to the table
        entryBuilder.apply(SetNbtLootFunction.builder(nbt));

        //set entry weight to that of the ability
        //TODO: Research how these tables work a bit more, and see if I need to rebalance these weights on compile
        entryBuilder.weight(ability.getLootTableWeight());

        //Adds item entry
        poolBuilder.rolls(ConstantLootTableRange.create(1));
        //Adds entry to pool
        poolBuilder.with(entryBuilder);
    }

    /**
     * Builds the LOOT_TABLE_REGISTRY on world load.
     */
    public static void build(){
        //we only wanna run this once
        if(LOOT_TABLE_REGISTRY != null) return;

        LOOT_TABLE_REGISTRY = new SimpleRegistry<>(LOOT_TABLE_REGISTRY_KEY, Lifecycle.stable());

        PRE_ENTITY_LOOT_POOL_MAP.forEach((id, poolBuilder) -> {
            LootTable.Builder tableBuilder = LootTable.builder();
            tableBuilder.pool(poolBuilder);
            Registry.register(LOOT_TABLE_REGISTRY, id, tableBuilder.build());
        });

        NovaGenetica.LOGGER.info("LootTables built");
    }
}
