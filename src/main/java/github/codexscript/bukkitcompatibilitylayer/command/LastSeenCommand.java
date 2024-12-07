package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.StateSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class LastSeenCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("lastseen")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                        .executes(LastSeenCommand::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        Collection<GameProfile> players = GameProfileArgumentType.getProfileArgument(source, "player");
        GameProfile player = players.iterator().next();

        ServerPlayerEntity playerEntity = source.getSource().getServer().getPlayerManager().getPlayer(player.getId());

        long lastSeenEpoch = StateSaverAndLoader.getPlayerState(player.getId(), source.getSource().getServer()).lastSeen;
        if (playerEntity != null && !playerEntity.isDisconnected()) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName() + " is on the server now."), false);
            return 1;
        } else if (lastSeenEpoch == 0) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName() + " has never been seen before."), false);
            return 1;
        }

        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = Instant.ofEpochSecond(lastSeenEpoch);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd yyyy h:mm a")
                .withZone(zoneId);
        String formatted = formatter.format(instant);
        String timeZone = zoneId.getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.US);
        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName() + " was last seen: " + formatted + " " + timeZone), false);
        return 1;

    }
}
