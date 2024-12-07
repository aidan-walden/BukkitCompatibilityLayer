package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.command.suggestions.ShuffleSettingsSuggestion;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.ShuffleSettingsPayload;
import github.codexscript.bukkitcompatibilitylayer.util.ShuffleSettingsOption;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ShuffleSettingsCommand {

    private static final ShuffleSettingsSuggestion SUGGESTION_PROVIDER = new ShuffleSettingsSuggestion();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("shufflesettings")
        .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes((source) -> execute(source, ShuffleSettingsOption.SWITCH_SPRINT_AND_CROUCH.asString()))
                .then(CommandManager.argument("mode", StringArgumentType.string()).suggests(SUGGESTION_PROVIDER).executes((source) -> execute(source, StringArgumentType.getString(source, "mode"))))));
    }

    public static int execute(CommandContext<ServerCommandSource> context, String mode) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

        if(!BukkitCompatibilityLayer.playersModInstalled.contains(player.getUuid())) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " does not have the mod installed."), false);
            return 0;
        }

        ShuffleSettingsOption option = ShuffleSettingsOption.byName(mode);

        if (option == null) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Invalid shuffle settings mode: " + mode), false);
            return 0;
        } else if (option == ShuffleSettingsOption.SWITCH_SPRINT_AND_CROUCH) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Successfully switched around bindings for sprint and crouch for " + player.getName().getString()), false);
        } else if (option == ShuffleSettingsOption.UNBIND_ALL) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " should crash! When they restart, all of their keys will be unbound!"), false);
        } else if (option == ShuffleSettingsOption.BLATANT) {
            context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " should crash! When they restart, their game will be fucked!"), false);
        }

        ServerPlayNetworking.send(player, new ShuffleSettingsPayload(option.getId()));
        return 0;
    }
}
