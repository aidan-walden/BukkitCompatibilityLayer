package me.aidanwalden.bukkitcompatibilitylayer.events;

import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import me.aidanwalden.bukkitcompatibilitylayer.StateSaverAndLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerLeaveListener {
    public static void onPlayerLeave(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        if (player != null) {
            StateSaverAndLoader.getPlayerState(player).lastSeen = System.currentTimeMillis() / 1000L;
            BukkitCompatibilityLayer.playersModInstalled.remove(player.getUuid());
        }
    }
}
