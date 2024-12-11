package me.aidanwalden.bukkitcompatibilitylayer.events;

import me.aidanwalden.bukkitcompatibilitylayer.PlayerData;
import me.aidanwalden.bukkitcompatibilitylayer.StateSaverAndLoader;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class PlayerJoinListener {
    public static void onPlayerJoin(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
        boolean doSlowWalk = playerState.doSlowWalk;

        if (!doSlowWalk) {
            return;
        }

        Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).setBaseValue(playerState.walkSpeed);
    }
}
