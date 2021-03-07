package me.orangemonkey68.novagenetica;

import me.orangemonkey68.novagenetica.abilities.Ability;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.lwjgl.system.CallbackI;

@Config(name = NovaGenetica.MOD_ID)
public class NovaGeneticaConfig implements ConfigData {
    public boolean loseAbilitiesOnDeath = true;

    @ConfigEntry.Gui.CollapsibleObject
    public AbilitiesConfig abilitiesConfig = new AbilitiesConfig();

    @ConfigEntry.Gui.CollapsibleObject
    public MachinesConfig machinesConfig = new MachinesConfig();

    public static class AbilitiesConfig {
        public boolean eat_grass = true;
        public boolean resistance = true;
        public boolean scare_creepers = true;
    }

    public static class MachinesConfig {
        @ConfigEntry.Gui.CollapsibleObject
        public PoweredMachineConfig geneExtractorConfig = new PoweredMachineConfig();
    }

    public static class PoweredMachineConfig {
        public int maxInputPerTick = 1000;
        public int maxStoredPower = 20000;
    }
}
