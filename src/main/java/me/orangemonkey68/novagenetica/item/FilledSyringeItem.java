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

public class FilledSyringeItem extends Item {
    private final Identifier abilityId;

    public FilledSyringeItem(Settings settings, Identifier abilityId) {
        super(settings);
        this.abilityId = abilityId;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(!world.isClient){
            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer)user;
            CompoundTag tag = stack.getTag();

            if(tag == null) return TypedActionResult.fail(stack);
            String idString = tag.getString("action");
            if (idString == null) return TypedActionResult.fail(stack);

            ngPlayer.giveAbility(new Identifier(idString));

            return TypedActionResult.success(stack);
        }
        return TypedActionResult.fail(stack);
    }

    public Ability getAbility() {
        return NovaGenetica.ABILITY_REGISTRY.get(abilityId);
    }

    public Identifier getAbilityId(){
        return abilityId;
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("ability", abilityId.toString());
        stack.setTag(tag);
        return stack;
    }
}
