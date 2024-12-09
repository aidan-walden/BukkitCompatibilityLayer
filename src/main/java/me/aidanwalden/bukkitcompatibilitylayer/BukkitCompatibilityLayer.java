package me.aidanwalden.bukkitcompatibilitylayer;

import com.mojang.brigadier.context.CommandContext;
import me.aidanwalden.bukkitcompatibilitylayer.command.*;
import me.aidanwalden.bukkitcompatibilitylayer.events.PlayerLeaveListener;
import me.aidanwalden.bukkitcompatibilitylayer.events.UseBlockListener;
import me.aidanwalden.bukkitcompatibilitylayer.networking.NetworkingMessages;
import me.aidanwalden.bukkitcompatibilitylayer.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitCompatibilityLayer implements ModInitializer {
    public static final String MOD_NAME = "BukkitCompatibilityLayer";
    public static final String MOD_COLOR = "§3"; // §3 = dark aqua

    public static final String CHAT_PREFIX = "§r" + BukkitCompatibilityLayer.MOD_COLOR + "[" + BukkitCompatibilityLayer.MOD_NAME + "]§r ";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME.toLowerCase());

    public static final GameRules.Key<GameRules.BooleanRule> DISABLE_END = GameRuleRegistry.register("disableEnd", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

    public static ConcurrentHashMap<Object, Boolean> playersGhosting = new ConcurrentHashMap<>();
    public static Set<UUID> playersModInstalled = ConcurrentHashMap.newKeySet();
    public static ConcurrentHashMap<UUID, CommandContext<ServerCommandSource>> osStatRequests = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, CommandContext<ServerCommandSource>> evalRequests = new ConcurrentHashMap<>();

    // public static boolean hasLuckPerms = false;

    @Override
    public void onInitialize() {
        // hasLuckPerms = FabricLoader.getInstance().isModLoaded("luckperms");
        UseBlockCallback.EVENT.register(UseBlockListener::onUseBlock);

        ModSounds.registerSounds();

        NetworkingMessages.registerServersidePackets();
        ServerPlayConnectionEvents.DISCONNECT.register(PlayerLeaveListener::onPlayerLeave);

        // Command registration
        CommandRegistrationCallback.EVENT.register(ScareCommand::register);
        CommandRegistrationCallback.EVENT.register(ModListCommand::register);
        CommandRegistrationCallback.EVENT.register(GetposCommand::register);
        CommandRegistrationCallback.EVENT.register(GhostKickCommand::register);
        CommandRegistrationCallback.EVENT.register(GetDiscordUIDCommand::register);
        CommandRegistrationCallback.EVENT.register(SetDiscordUIDCommand::register);
        CommandRegistrationCallback.EVENT.register(PinCPUCommand::register);
        CommandRegistrationCallback.EVENT.register(LastSeenCommand::register);
        CommandRegistrationCallback.EVENT.register(OsStatCommand::register);
        CommandRegistrationCallback.EVENT.register(EvalCommand::register);
        CommandRegistrationCallback.EVENT.register(ShuffleSettingsCommand::register);
        CommandRegistrationCallback.EVENT.register(IgniteCommand::register);
    }
}
