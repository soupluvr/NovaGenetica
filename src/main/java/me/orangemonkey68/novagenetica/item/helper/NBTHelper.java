package me.orangemonkey68.novagenetica.item.helper;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

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

    public static int getIntOrDefault(@NotNull CompoundTag tag, String key, int defaultVal){
        if(tag.contains(key)){
            return tag.getInt(key);
        }
        return defaultVal;
    }

    public static String getStringOrDefault(@NotNull CompoundTag tag, String key, String defaultVal){
        if(tag.contains(key)){
            return tag.getString(key);
        }
        return defaultVal;
    }

    public static float getFloatOrDefault(@NotNull CompoundTag tag, String key, float defaultVal){
        if(tag.contains(key)){
            return tag.getFloat(key);
        }
        return defaultVal;
    }

    public static double getDoubleOrDefault(@NotNull CompoundTag tag, String key, double defaultVal){
        if(tag.contains(key)){
            return tag.getDouble(key);
        }
        return defaultVal;
    }

    public static boolean getBooleanOrDefault(@NotNull CompoundTag tag, @NotNull String key, boolean defaultVal){
        if(tag.contains(key)){
            return tag.getBoolean(key);
        }
        return defaultVal;
    }
}
