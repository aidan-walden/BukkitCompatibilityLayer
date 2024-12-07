package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class IgniteCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("ignite")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(context -> ignite(context, 20))
                .then(CommandManager.argument("ticks", IntegerArgumentType.integer(1))
                        .executes(context -> ignite(context, IntegerArgumentType.getInteger(context, "ticks"))))));
    }

    private static int ignite(CommandContext<ServerCommandSource> context, int ticks) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        player.setOnFireForTicks(ticks);
        context.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Ignited " + player.getName().getString() + " for " + ticks + " ticks"), false);
        return 0;
    }
}
