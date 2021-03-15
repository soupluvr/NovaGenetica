package me.orangemonkey68.novagenetica;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.item.helper.ItemHelper;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class LootTableManager {
    public static final RegistryKey<Registry<LootTable>> LOOT_TABLE_REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier(NovaGenetica.MOD_ID, "loot_table"));
    public static final Registry<LootTable> LOOT_TABLE_REGISTRY = new SimpleRegistry<>(LOOT_TABLE_REGISTRY_KEY, Lifecycle.stable());

    private static final Map<Identifier, FabricLootPoolBuilder> PRE_ENTITY_LOOT_POOL_MAP = new HashMap<>();

    //NOTE: I'm 99% sure FabricLootPoolBuilder is mutable
    public static void register(Identifier entityTypeId, Ability ability){
        //add new builder if key doesn't exist
        PRE_ENTITY_LOOT_POOL_MAP.putIfAbsent(entityTypeId, getBuilder());

        FabricLootPoolBuilder builder = PRE_ENTITY_LOOT_POOL_MAP.get(entityTypeId);
        //Adds an entry of novagenetica:gene_item

        CompoundTag nbt = ItemHelper.getGene(entityTypeId, null, false, false).getTag();

        //Adds item entry
        builder.withEntry(
                ItemEntry.builder(NovaGenetica.GENE_ITEM) // creates builder
                .weight(ability.getLootTableWeight()) // set weight to ability weight
                .apply(SetNbtLootFunction.builder(nbt)) // adds a function to set the nbt to the correct values
                .build()
        );
    }

    private static FabricLootPoolBuilder getBuilder(){
        return FabricLootPoolBuilder.builder().rolls(ConstantLootTableRange.create(1));
    }
}
