package me.orangemonkey68.novagenetica.abilities;

import me.orangemonkey68.novagenetica.NovaGenetica;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class AbilityScareCreepers implements Ability {

    @Override
    public String getTranslationKey() {
        return "ability.novagenetica.scare_creepers";
    }

    @Override
    public int getRarity() {
        return 2;
    }

    @Override
    public int genesNeededToComplete() {
        return 4;
    }

    @Override
    public boolean isEnabled() {
        return NovaGenetica.getConfig().abilitiesConfig.scare_creepers;
    }

    @Override
    public void onRegistryServer() {

    }

    @Override
    public void onRegistryClient() {

    }

    @Override
    public void onInjection(ServerPlayerEntity player) {
        player.sendMessage(new TranslatableText("message.novagenetica.ability.scare_creepers"), false);
    }

    @Override
    public void onTick(ServerPlayerEntity player) {

    }

    @Override
    public int getColor() {
        return 0xf2ad35;
    }
}