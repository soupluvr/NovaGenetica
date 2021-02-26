package me.orangemonkey68.novagenetica.abilities;

/**
 * This class is used to validate properties of Abilities and their items
 */
public class AbilityValidator {
    public static boolean validate(Ability ability){
        return ability.genesNeededToComplete() % 2 == 0;
    }
}
