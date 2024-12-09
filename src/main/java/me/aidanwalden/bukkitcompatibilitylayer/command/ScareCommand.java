package me.aidanwalden.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.aidanwalden.bukkitcompatibilitylayer.command.suggestions.ScareSuggestion;
import me.aidanwalden.bukkitcompatibilitylayer.networking.payloads.StalkerScreamPayload;
import me.aidanwalden.bukkitcompatibilitylayer.sound.ModSounds;
import me.aidanwalden.bukkitcompatibilitylayer.util.ScareOption;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;

public class ScareCommand {

    private static final ScareSuggestion SUGGESTION_PROVIDER = new ScareSuggestion();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("scare")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("mode", StringArgumentType.string()).suggests(SUGGESTION_PROVIDER)
                                .executes(ScareCommand::execute))));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(source, "player");
        String mode = StringArgumentType.getString(source, "mode");


        ScareOption option = ScareOption.byName(mode);

        if (option == null) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Invalid scare mode: " + mode), false);
            return 0;
        }

        int id = option.getId();

        if (id == 0) {
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1.0f, 1.0f);
        } else if (id == 1) {
            if (!BukkitCompatibilityLayer.playersModInstalled.contains(player.getUuid())) {
                source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + player.getName().getString() + " does not have the mod installed."), false);
                return 0;
            }
            ServerPlayNetworking.send(player, new StalkerScreamPayload());
        } else {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Invalid scare mode: " + mode), false);
            return 0;
        }

        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Scared " + player.getName().getString() + "!"), false);

        return 1;
    }
}
