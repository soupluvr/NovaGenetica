package me.orangemonkey68.novagenetica.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.orangemonkey68.novagenetica.NovaGenetica;
import me.orangemonkey68.novagenetica.NovaGeneticaPlayer;
import me.orangemonkey68.novagenetica.abilities.Ability;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.command.argument.IdentifierArgumentType.identifier;
import static net.minecraft.command.argument.IdentifierArgumentType.getIdentifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class GiveAbilityCommand implements CommandRegistrationCallback {
    private final static SuggestionProvider<ServerCommandSource> ABILITY_SUGGESTIONS = (context, builder) -> CommandSource.suggestMatching(collectAbilityIdentifiers(NovaGenetica.ABILITY_REGISTRY), builder);


    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(literal("giveability")
            .requires(source -> source.hasPermissionLevel(4))
                .then(
                        argument("identifier", identifier())
                        .suggests(ABILITY_SUGGESTIONS)
                )
                    .executes(context -> giveAbility(context.getSource(), getIdentifier(context, "identifier"))));
    }

    public int giveAbility(ServerCommandSource source, Identifier identifier) {
//            NovaGeneticaPlayer ngPlayer = (NovaGeneticaPlayer) source.getPlayer();
//
//            if(!NovaGenetica.ABILITY_REGISTRY.containsId(identifier)) throw new SimpleCommandExceptionType(new TranslatableText("novagenetica.command.id_doesnt_exist", identifier.toString())).create();
//
//            if(ngPlayer.hasAbility(identifier)) throw new SimpleCommandExceptionType(new TranslatableText("novagenetica.command.already_has_ability", identifier.toString())).create();
//
//            ngPlayer.giveAbility(identifier);
        return 15;
    }

    private static String[] collectAbilityIdentifiers(Registry<Ability> registry){
        List<String> list = new ArrayList<>();
        registry.forEach(ability -> {
            Identifier id = registry.getId(ability);
            if(id != null){
                list.add(id.toString());
            }
        });
        return list.toArray(new String[0]);
    }
}
