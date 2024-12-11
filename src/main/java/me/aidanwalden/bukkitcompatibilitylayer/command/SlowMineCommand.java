package me.aidanwalden.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import me.aidanwalden.bukkitcompatibilitylayer.StateSaverAndLoader;
import me.aidanwalden.bukkitcompatibilitylayer.command.suggestions.FlagOperationSuggestion;
import me.aidanwalden.bukkitcompatibilitylayer.mixin.EntityAttributeAccessor;
import me.aidanwalden.bukkitcompatibilitylayer.util.FlagOperationOption;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class SlowMineCommand {
    private static final double decrementBy = 0.01;
    private static final FlagOperationSuggestion SUGGESTION_PROVIDER = new FlagOperationSuggestion();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("slowmine")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("mode", StringArgumentType.string()).suggests(SUGGESTION_PROVIDER)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(SlowMineCommand::execute))));
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        String mode = StringArgumentType.getString(context, "mode");

        FlagOperationOption option = FlagOperationOption.byName(mode);

        if (option == null) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Invalid mode: " + mode), false);
            return 0;
        }

        if (option == FlagOperationOption.SET) {
            StateSaverAndLoader.getPlayerState(player).doSlowMine = true;

            BukkitCompatibilityLayer.playersSlowMine.put(player.getUuid(), true);
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "The slow burn on mining speed starts for " + Objects.requireNonNull(player.getDisplayName()).getString() + "..."), false);
        } else if (option == FlagOperationOption.CLEAR) {
            BukkitCompatibilityLayer.playersSlowMine.remove(player.getUuid());
            StateSaverAndLoader.getPlayerState(player).doSlowMine = false;
            clear(player);
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Mining speed modifiers cleared for " + Objects.requireNonNull(player.getDisplayName()).getString()), false);
        }

        return 1;
    }

    public static void decrement(ServerPlayerEntity player) {
        double currentMineSpeed = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)).getValue();
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)).setBaseValue(currentMineSpeed - decrementBy);
    }

    public static void clear(ServerPlayerEntity player) {
        double fallback = ((EntityAttributeAccessor) EntityAttributes.PLAYER_BLOCK_BREAK_SPEED.value()).getFallback();
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED)).setBaseValue(fallback);
    }
}
