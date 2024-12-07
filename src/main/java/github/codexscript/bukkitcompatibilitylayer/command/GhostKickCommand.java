package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.StateSaverAndLoader;
import github.codexscript.bukkitcompatibilitylayer.util.GhostKickThread;
import github.codexscript.bukkitcompatibilitylayer.util.ResponseHandler;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class GhostKickCommand {

    private static void commenceKick(CommandContext<ServerCommandSource> source, ServerPlayerEntity player) {
        BukkitCompatibilityLayer.LOGGER.info("Commencing ghost kick");
        // PlayerManager pm = source.getSource().getServer().getPlayerManager();
        // pm.remove(player);
        BukkitCompatibilityLayer.playersGhosting.put(player.getUuid(), false);


        Runnable r = new GhostKickThread(player);
        new Thread(r).start();
    }

    public static HttpClient client = HttpClient.newHttpClient();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("ghostkick")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(GhostKickCommand::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {

        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");

        if (player.getServer() == null || player.getServer().isSingleplayer()) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "You can't use that command in this server environment."), false);
            return -1;
        }

        if (source.getSource().getPlayer() == null) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " was ghost kicked by " + source.getSource().getName() + "." ), true);
        } else {
            String sourcePlayerName = Objects.requireNonNull(source.getSource().getPlayer().getDisplayName()).getString();
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " was ghost kicked by " + sourcePlayerName + "." ), true);
        }

        BukkitCompatibilityLayer.LOGGER.info("Ghost kicking {}", Objects.requireNonNull(player.getDisplayName()).getString());

        //String id = ((IEntityDataSaver) player).getPersistentData().getString("discordUID");
        String id = StateSaverAndLoader.getPlayerState(player).discordUID;
        if (id == null || id.isEmpty()) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Discord UID for " + player.getDisplayName().getString() + " is not set."), false);
            BukkitCompatibilityLayer.LOGGER.info("Discord UID not set for {}.", player.getDisplayName().getString());
            commenceKick(source, player);
        } else {
            makeRequest(GhostKickCommand::processResponse, player, source);
        }

        return 1;
    }

    private static void makeRequest(ResponseHandler<HttpResponse<String>, ServerPlayerEntity, CommandContext<ServerCommandSource>> callback, ServerPlayerEntity player, CommandContext<ServerCommandSource> source) {
        try {
            // String id = ((IEntityDataSaver) player).getPersistentData().getString("discordUID");
            String id = StateSaverAndLoader.getPlayerState(player).discordUID;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://192.168.1.50:3000/kick/" + id)).build();
            CompletableFuture<HttpResponse<String>> responseFuture =
                    client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            responseFuture.thenAccept(response -> callback.accept(response, player, source))
                    .exceptionally(e -> {
                        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Request to Discord bot endpoint failed. Ghost kick will not continue."), false);
                        BukkitCompatibilityLayer.LOGGER.error("Error during Discord bot request: {}", e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            BukkitCompatibilityLayer.LOGGER.error(e.getMessage());
        }
    }

    private static void processResponse(HttpResponse<String> response, ServerPlayerEntity player, CommandContext<ServerCommandSource> source) {
        if (response.statusCode() == 200) {
            commenceKick(source, player);
        } else {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Request to Discord bot endpoint failed. Ghost kick will not continue."), false);
            BukkitCompatibilityLayer.LOGGER.error("Received status code {} from Discord bot API.", response.statusCode());
        }
    }
}
