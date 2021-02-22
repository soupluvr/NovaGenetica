package me.orangemonkey68.novageneticae.abilities;

import me.orangemonkey68.novageneticae.NovaGeneticae;
import net.minecraft.text.TranslatableText;

public interface Ability {
    /**
     *
     * @return The translation key string to pass into {@link #getName()}
     */
    String getTranslationKey();

    /**
     *
     * @return The {@link TranslatableText} which is the text used to show in game
     */
    default TranslatableText getName() {
        return new TranslatableText(getTranslationKey());
    }

    /**
     * The rarity is used to determine how hard it is to get an ability. The higher this value, the lower the chance is to get the ability.
     * @return The rarity score
     */
    int getRarity();

    /**
     * The number of genes needed to create one "Completed Gene".
     * This number should <b>always</b> be even. If it isn't, an {@link IllegalStateException} will be logged and it will be removed from the registry.
     * @return
     */
    int getBreedingState();

    /**
     * It's recommended to check a config here.
     * @return Whether or not the player should be allowed to get this Ability or the items to make it.
     */
    boolean isAllowed();

    /**
     * This code is run in {@link NovaGeneticae#onInitialize()}, and it's recommended that register any event callbacks in this function.
     */
    void onRegistry();
}
