package me.orangemonkey68.novagenetica;

import me.orangemonkey68.novagenetica.abilities.Ability;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = NovaGenetica.MOD_ID)
public class NovaGeneticaConfig implements ConfigData {
    public boolean loseAbilitiesOnDeath = true;

    @ConfigEntry.Gui.CollapsibleObject
    public AbilitiesConfig abilitiesConfig = new AbilitiesConfig();

    public static class AbilitiesConfig {
        public boolean eat_grass = true;
        public boolean resistance = true;
        public boolean scare_creepers = true;
    }
}
