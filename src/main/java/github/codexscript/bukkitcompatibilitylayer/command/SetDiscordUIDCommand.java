package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.StateSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class SetDiscordUIDCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("setdiscord")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("id", greedyString())
                        .executes(github.codexscript.bukkitcompatibilitylayer.command.SetDiscordUIDCommand::execute))));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");
        String newID = StringArgumentType.getString(source, "id");
        // DiscordUIDData.setUID((IEntityDataSaver) player, newID);
        StateSaverAndLoader.getPlayerState(player).discordUID = newID;
        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Set Discord UID of " + player.getName().getString() + " to " + newID), false);
        return 1;
    }
}
