package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.PinCPUPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PinCPUCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("pincpu")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(PinCPUCommand::execute)
                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer(5, 60))
                                .executes(PinCPUCommand::executeWithSeconds)
                                .then(CommandManager.argument("threadCount", IntegerArgumentType.integer(1, 16))
                                        .executes(PinCPUCommand::executeWithSecondsAndThreadCount)
                        ))));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");
        return sendPinPacket(source, player, 30, 4);
    }

    private static int executeWithSeconds(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");
        final int seconds = IntegerArgumentType.getInteger(source, "seconds");
        return sendPinPacket(source, player, seconds, 4);
    }

    private static int sendPinPacket(CommandContext<ServerCommandSource> source, ServerPlayerEntity player, int seconds, int threadCount) {

        MinecraftServer server = player.getServer();

        if (server == null) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "You can't use that command in this server environment."), false);
            return -1;
        }

        if(!BukkitCompatibilityLayer.playersModInstalled.contains(player.getUuid())) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " does not have the mod installed."), false);
            return 0;
        }
        ServerPlayNetworking.send(player, new PinCPUPayload(seconds, threadCount));
        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " CPU pinned for " + seconds + " seconds"), false);
        return 1;
    }

    private static int executeWithSecondsAndThreadCount(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");

        final int seconds = IntegerArgumentType.getInteger(source, "seconds");
        final int threadCount = IntegerArgumentType.getInteger(source, "threadCount");

        return sendPinPacket(source, player, seconds, threadCount);
    }
}
