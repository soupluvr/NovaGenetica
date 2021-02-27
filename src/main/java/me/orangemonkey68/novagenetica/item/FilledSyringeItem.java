package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilledSyringeItem extends Item {

    private static final String UNKNOWN_TRANSLATION_KEY = "item.novagenetica.filled_syringe.unknown";

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

            Identifier abilityId = new Identifier(tag.getString("ability"));

            if(NovaGenetica.ABILITY_REGISTRY.containsId(abilityId) && !ngPlayer.hasAbility(abilityId)){
                ngPlayer.giveAbility(abilityId);

                ItemStack emptySyringe =  new ItemStack(NovaGenetica.EMPTY_SYRINGE_ITEM);
                user.setStackInHand(hand, emptySyringe);

                return TypedActionResult.success(emptySyringe);
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
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

    @NotNull
    public static ItemStack stackOf(Identifier abilityId, int color){
        ItemStack stack = new ItemStack(NovaGenetica.FILLED_SYRINGE_ITEM);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("ability", abilityId.toString());
        tag.putInt("color", color);
        stack.setTag(tag);
        return stack;
    }

    //God I hate lang files
    @Override
    public String getTranslationKey(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        //Return if tag is null
        if(tag == null) return UNKNOWN_TRANSLATION_KEY;
        String id = tag.getString("ability");
        //Return if id is null
        if(id == null) return UNKNOWN_TRANSLATION_KEY;
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(new Identifier(id));
        //return if ability is null
        if(ability == null) return UNKNOWN_TRANSLATION_KEY;
        String[] splitId = id.split(":");

        return "item." + splitId[0] + ".filled_syringe." + splitId[1];
    }
}

