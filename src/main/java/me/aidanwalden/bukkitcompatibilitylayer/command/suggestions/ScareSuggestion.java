package me.aidanwalden.bukkitcompatibilitylayer.command.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScareSuggestion implements SuggestionProvider<ServerCommandSource> {
    private static final List<String> suggestions = List.of("creeper", "stalker", "discord");

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        CommandSource.suggestMatching(suggestions, builder);
        return builder.buildFuture();
    }
}
