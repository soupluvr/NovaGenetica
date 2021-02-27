package me.orangemonkey68.novagenetica;

import com.mojang.serialization.Lifecycle;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.abilities.AbilityEatGrass;
import me.orangemonkey68.novagenetica.abilities.RegistrationHelper;
import me.orangemonkey68.novagenetica.commands.GiveAbilityCommand;
import me.orangemonkey68.novagenetica.item.FilledSyringeItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;

public class NovaGenetica implements ModInitializer {
    public static final String MOD_ID = "novagenetica";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final RegistryKey<Registry<Ability>> ABILITY_KEY = RegistryKey.ofRegistry(new Identifier(MOD_ID, "ability"));
    public static final Registry<Ability> ABILITY_REGISTRY = new SimpleRegistry<>(ABILITY_KEY, Lifecycle.stable());

    public static ItemGroup ITEM_GROUP;
    public static final Item EMPTY_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "empty_syringe"), new Item(new Item.Settings().maxCount(16)));
    public static final Item FILLED_SYRINGE_ITEM = Registry.register(Registry.ITEM, new Identifier(MOD_ID, "filled_syringe"), new FilledSyringeItem(new Item.Settings().maxCount(1)));

    private static final RegistrationHelper REGISTRATION_HELPER = new RegistrationHelper(new Identifier(MOD_ID, "item_group"));
    @Override
    public void onInitialize() {
        REGISTRATION_HELPER.addItemToGroup(new ItemStack(EMPTY_SYRINGE_ITEM));

        REGISTRATION_HELPER.register(
                new AbilityEatGrass(),
                new Identifier(MOD_ID, "eat_grass"),
                new HashSet<>(Arrays.asList(EntityType.SHEEP)),
                0xFFFFFF
        );

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            CompoundTag tag = stack.getTag();
            if(tag == null) return 0xFFFFFF;
            if(!tag.contains("color")) return 0xFFFFFF;
            return tag.getInt("color");
        }, FILLED_SYRINGE_ITEM);

        //Loops over all abilities, and runs their onRegistry() logic
        ABILITY_REGISTRY.forEach(Ability::onRegistryServer);

        //Register /giveability command
        //TODO: Fix /giveability command
        CommandRegistrationCallback.EVENT.register(new GiveAbilityCommand());

        ITEM_GROUP = REGISTRATION_HELPER.buildGroup(FilledSyringeItem.stackOf(new Identifier(MOD_ID, "none"), 0xed382b));
    }
}
