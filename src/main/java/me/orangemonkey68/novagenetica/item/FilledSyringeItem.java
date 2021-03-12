package me.orangemonkey68.novagenetica.item;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import me.orangemonkey68.novagenetica.item.helper.NBTHelper;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class FilledSyringeItem extends Item {

    private static final TranslatableText UNKNOWN_NAME = new TranslatableText("item.novagenetica.filled_syringe.unknown");

    public FilledSyringeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient){
            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer)user;
            ItemStack stack = user.getStackInHand(hand);

            CompoundTag tag = stack.getTag();
            if (tag == null) return TypedActionResult.fail(stack);

            if(tag.contains("playerAbilities")){
                return handlePlayerSyringe(user, stack, hand);
            } else if (tag.contains("ability")){
                return handleAbilitySyringe(user, stack, hand);
            } else {
                return TypedActionResult.fail(stack);
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    //Handles a syringe that gives the player one ability
    TypedActionResult<ItemStack> handleAbilitySyringe(PlayerEntity user, ItemStack stack, Hand hand){
        CompoundTag tag = stack.getTag();
        if (tag == null) return TypedActionResult.fail(stack);

        NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer) user;
        Identifier abilityId = new Identifier(tag.getString("ability"));

        if(ngPlayer.hasAbility(abilityId)){
            user.sendMessage(new TranslatableText("message.novagenetica.has_ability"), false);

            return TypedActionResult.fail(user.getStackInHand(hand));
        } else if (!NovaGenetica.ABILITY_REGISTRY.containsId(abilityId)){
            user.sendMessage(new TranslatableText("message.novagenetica.doesnt_exist"), false);
            user.setStackInHand(hand, new ItemStack(NovaGenetica.EMPTY_SYRINGE_ITEM));

            return TypedActionResult.fail(user.getStackInHand(hand));
        } else {
            ngPlayer.giveAbility(abilityId);

            ItemStack emptySyringe =  new ItemStack(NovaGenetica.EMPTY_SYRINGE_ITEM);
            user.setStackInHand(hand, emptySyringe);

            return TypedActionResult.success(emptySyringe);
        }
    }

    //Handles a syringe filled with a player's blood
    TypedActionResult<ItemStack> handlePlayerSyringe(PlayerEntity user, ItemStack stack, Hand hand){
        NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer)user;

        CompoundTag tag = stack.getTag();
        if(tag == null) return TypedActionResult.fail(stack);
        UUID uuid = tag.getUuid("uuid");
        CompoundTag subTag = (CompoundTag) tag.get("playerAbilities");
        ItemStack emptySyringe = new ItemStack(NovaGenetica.EMPTY_SYRINGE_ITEM);

        if(uuid.equals(user.getUuid()) && subTag != null){
            Set<Ability> abilities = NBTHelper.getAbilitySetFromTag(subTag);
            abilities.forEach(ngPlayer::giveAbility);
        } else {
            user.sendMessage(new TranslatableText("message.novagenetica.filled_syringe.bad_idea"), false);
            user.damage(DamageSource.MAGIC, 5);
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 10*20, 1));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 15*20, 0));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20*20, 2));
        }

        user.setStackInHand(hand, emptySyringe);
        return TypedActionResult.success(emptySyringe, true);
    }

    @Nullable
    public static Ability getAbility(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) return null;

        String abilityString = tag.getString("ability");
        if(abilityString == null) return null;

        Identifier id = new Identifier(abilityString);
        if(!NovaGenetica.ABILITY_REGISTRY.containsId(id)) return null;

        return NovaGenetica.ABILITY_REGISTRY.get(id);
    }

    @Nullable
    public static Identifier getAbilityId(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null) return null;

        String abilityString = tag.getString("ability");
        if(abilityString == null) return null;

        return new Identifier(abilityString);
    }

//    @Override
//    public String getTranslationKey(ItemStack stack) {
//        CompoundTag tag = stack.getTag();
//        //Return if tag is null
//        if(tag == null) return UNKNOWN_TRANSLATION_KEY;
//        String id = tag.getString("ability");
//        //Return if id is null
//        if(id == null) return UNKNOWN_TRANSLATION_KEY;
//        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(new Identifier(id));
//        //return if ability is null
//        if(ability == null) return UNKNOWN_TRANSLATION_KEY;
//        String[] splitId = id.split(":");
//
//        return "item.filled_syringe." + id;
//    }


    @Override
    public Text getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) return UNKNOWN_NAME;

        if(tag.contains("playerName")){
            String name = tag.getString("playerName");
            if(name != null){
                return new TranslatableText("item.novagenetica.filled_syringe.player", new LiteralText(name));
            }
        } else if (tag.contains("ability")){
            Identifier id = new Identifier(tag.getString("ability"));
            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(id);
            if(ability != null){
                return new TranslatableText(getTranslationKey(), ability.getTranslationKey());
            }
        }

        return UNKNOWN_NAME;
    }
}

