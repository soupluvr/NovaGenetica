package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class AbilityResistance implements Ability{

    @Override
    public String getTranslationKey() {
        return "ability.novagenetica.resistance";
    }

    @Override
    public int getRarity() {
        return 23;
    }

    @Override
    public int genesNeededToComplete() {
        return 12;
    }

    @Override
    public boolean isEnabled() {
        return NovaGenetica.getConfig().abilitiesConfig.resistance;
    }

    @Override
    public void onRegistryServer() {

    }

    @Override
    public void onRegistryClient() {

    }

    @Override
    public void onInjection(ServerPlayerEntity player) {

    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, true, true));
    }

    /**
     * @return the color of this Ability's items in 0xRRGGBB format.
     */
    @Override
    public int getColor() {
        return 0x276339;
    }
}
