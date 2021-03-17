package me.orangemonkey68.novagenetica.item;

import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.helper.item.NBTHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GeneItem extends Item {
    public GeneItem(Settings settings) {
        super(settings);
    }

    //TODO: refactor other Items to use this system to get name
    @Override
    public Text getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        //Check it isn't null
        if(tag != null){
            Identifier abilityId = new Identifier(NBTHelper.getStringOrDefault(tag, "ability", ""));
            Identifier entityTypeId = new Identifier(NBTHelper.getStringOrDefault(tag, "entityType", ""));

            boolean abilityExists = NovaGenetica.ABILITY_REGISTRY.containsId(abilityId);
            boolean entityTypeExists = Registry.ENTITY_TYPE.containsId(entityTypeId);

            boolean complete = NBTHelper.getBooleanOrDefault(tag, "complete", false);
            boolean identified = NBTHelper.getBooleanOrDefault(tag, "identified", false);

            if(complete && identified && abilityExists){ //If it's complete, identified, and the ability exists, return complete name
                return new TranslatableText("item.novagenetica.complete_gene", new TranslatableText(NovaGenetica.ABILITY_REGISTRY.get(abilityId).getTranslationKey()));
            }

            if(abilityExists && identified){ // If it's incomplete, but identified and has a real ability, return "[ability name] gene"
                return new TranslatableText("item.novagenetica.gene", new TranslatableText(NovaGenetica.ABILITY_REGISTRY.get(abilityId).getTranslationKey()).formatted(Formatting.RESET));
            }

            if(abilityExists && !identified){ // Ability exists, but it's unidentified
                return new TranslatableText("item.novagenetica.obfuscated_gene", new TranslatableText(NovaGenetica.ABILITY_REGISTRY.get(abilityId).getTranslationKey()).formatted(Formatting.OBFUSCATED));
            }

            if(entityTypeExists){ // Entity type exists
                return new TranslatableText("item.novagenetica.gene", new TranslatableText(Registry.ENTITY_TYPE.get(entityTypeId).getTranslationKey()));
            }
        }
        // Entity type doesn't exist :pensive:
        return new TranslatableText("item.novagenetica.unknown_gene");
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null){
            if(tag.contains("complete")){
                return tag.getBoolean("complete");
            }
        }
        return false;
    }
}
