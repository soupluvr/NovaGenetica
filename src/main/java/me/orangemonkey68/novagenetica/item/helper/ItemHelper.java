package me.orangemonkey68.novagenetica.item.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemHelper {
    private ItemHelper(){

    }

    public static ItemStack getSyringe(Identifier abilityId){
        ItemStack stack = new ItemStack(NovaGenetica.FILLED_SYRINGE_ITEM);
        CompoundTag tag = new CompoundTag();
        tag.putString("ability", abilityId.toString());
        Ability ability = NovaGenetica.ABILITY_REGISTRY.get(abilityId);
        tag.putInt("color", ability != null ? ability.getColor() : 0xFFFFFF);
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getGene(Identifier entityTypeId, Identifier abilityId, boolean complete, boolean identified){
        ItemStack stack = new ItemStack(NovaGenetica.GENE_ITEM);
        CompoundTag tag = new CompoundTag();

        if(NovaGenetica.ABILITY_REGISTRY.containsId(abilityId)){
                tag.putString("ability", abilityId.toString());
                tag.putInt("color", NovaGenetica.ABILITY_REGISTRY.get(abilityId).getColor());
        }

        if(Registry.ENTITY_TYPE.containsId(entityTypeId)){
            tag.putString("entityType", entityTypeId.toString());
            tag.putInt("color", RegistrationHelper.ENTITY_TYPE_COLOR_MAP.get(Registry.ENTITY_TYPE.get(entityTypeId)));
        }

        //complete?
        tag.putBoolean("complete", complete);

        //identified?
        tag.putBoolean("identified", identified);

        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getMobFlakes(Identifier entityTypeId){
        ItemStack stack = new ItemStack(NovaGenetica.MOB_FLAKES);
        CompoundTag tag = new CompoundTag();
        tag.putString("entityType", entityTypeId.toString());
        if(Registry.ENTITY_TYPE.containsId(entityTypeId)){
            EntityType<?> type = Registry.ENTITY_TYPE.get(entityTypeId);
            tag.putInt("color", RegistrationHelper.ENTITY_TYPE_COLOR_MAP.getOrDefault(type, 0xFFFFFF));
        } else {
            tag.putInt("color", 0xFFFFFF);
        }
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack getPlayerBackupSyringe(ServerPlayerEntity player){
        NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer) player;
        ItemStack stack = new ItemStack(NovaGenetica.FILLED_SYRINGE_ITEM);
        CompoundTag tag = new CompoundTag();
        tag.putInt("color", 0xe83c1a);
        tag.put("playerAbilities", NBTHelper.getAbilitiesTag(ngPlayer));
        tag.putUuid("uuid", player.getUuid());
        tag.putString("playerName", player.getName().asString());
        stack.setTag(tag);

        return stack;
    }
}
