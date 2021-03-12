package me.orangemonkey68.novagenetica;

import blue.endless.jankson.Comment;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

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
        @Comment("Backup config used in case the specific machine's can't be found")
        @ConfigEntry.Gui.Excluded
        public PoweredMachineConfig defaultConfig = new PoweredMachineConfig();

        @ConfigEntry.Gui.CollapsibleObject
        public PoweredMachineConfig geneExtractorConfig = new PoweredMachineConfig();
    }

    public static class PoweredMachineConfig {
        public int maxInputPerTick = 1000;
        public int powerDrawPerTick = 80;
        public int maxStoredPower = 50000;
        public int processingTime = 200;
    }
}
