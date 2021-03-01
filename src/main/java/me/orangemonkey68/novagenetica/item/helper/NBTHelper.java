package me.orangemonkey68.novagenetica.item.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NBTHelper {
    private NBTHelper(){}

    public static Set<Ability> getAbilitySetFromTag(CompoundTag abilitiesTag){
        Set<Ability> abilities = new HashSet<>();
        for (int i = 0; abilitiesTag.contains(String.valueOf(i)); i++) {
            Identifier id = new Identifier(abilitiesTag.getString(String.valueOf(i)));
            Ability ability = NovaGenetica.ABILITY_REGISTRY.get(id);

            if(ability != null){
                abilities.add(ability);
            }
        }

        return abilities;
    }

    public static CompoundTag getAbilitiesTag(NovaGeneticaPlayer player){
        CompoundTag tag = new CompoundTag();
        List<Ability> abilities = new ArrayList<>(player.getAbilities());

        for (int i = 0; i < abilities.size(); i++) {
            Identifier id = NovaGenetica.ABILITY_REGISTRY.getId(abilities.get(i));
            if(id != null){
                tag.putString(String.valueOf(i), id.toString());
            }
        }

        return tag;
    }
}
