package me.aidanwalden.bukkitcompatibilitylayer;

import me.aidanwalden.bukkitcompatibilitylayer.events.ClientPlayerJoinListener;
import me.aidanwalden.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BukkitCompatibilityLayerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkingMessages.registerClientsidePackets();
        ClientPlayConnectionEvents.JOIN.register(ClientPlayerJoinListener::onPlayerJoin);
    }
}
