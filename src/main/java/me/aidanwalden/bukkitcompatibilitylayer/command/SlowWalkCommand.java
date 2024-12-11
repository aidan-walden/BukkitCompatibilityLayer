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

public class SlowWalkCommand {
    private static final double decrementBy = 0.01;
    private static final FlagOperationSuggestion SUGGESTION_PROVIDER = new FlagOperationSuggestion();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("slowwalk")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("mode", StringArgumentType.string()).suggests(SUGGESTION_PROVIDER)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(SlowWalkCommand::execute))));
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
            StateSaverAndLoader.getPlayerState(player).doSlowWalk = true;

            BukkitCompatibilityLayer.playersSlowWalk.put(player.getUuid(), true);
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "The slow burn on walking speed starts for " + Objects.requireNonNull(player.getDisplayName()).getString() + "..."), false);
        } else if (option == FlagOperationOption.CLEAR) {
            BukkitCompatibilityLayer.playersSlowWalk.remove(player.getUuid());
            StateSaverAndLoader.getPlayerState(player).doSlowWalk = false;
            clear(player);
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Walking speed modifiers cleared for " + Objects.requireNonNull(player.getDisplayName()).getString()), false);

        }

        return 1;
    }

    public static void decrement(ServerPlayerEntity player) {
        double currentWalkSpeed = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).getValue();
        double newSpeed = currentWalkSpeed - decrementBy;

        if (newSpeed < 0) {
            return;
        }

        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(newSpeed);
        StateSaverAndLoader.getPlayerState(player).walkSpeed = newSpeed;
    }

    public static void clear(ServerPlayerEntity player) {
        double fallback = player.getAbilities().getWalkSpeed();
        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(fallback);
        StateSaverAndLoader.getPlayerState(player).walkSpeed = fallback;
    }
}
