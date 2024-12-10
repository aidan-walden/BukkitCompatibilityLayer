package me.aidanwalden.bukkitcompatibilitylayer;

import me.aidanwalden.bukkitcompatibilitylayer.events.ClientPlayerJoinListener;
import me.aidanwalden.bukkitcompatibilitylayer.networking.ClientNetworkingMessages;
import me.aidanwalden.bukkitcompatibilitylayer.sound.ModSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class BukkitCompatibilityLayerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetworkingMessages.registerClientsidePackets();
        ModSounds.registerSounds();
        ClientPlayConnectionEvents.JOIN.register(ClientPlayerJoinListener::onPlayerJoin);
    }
}
