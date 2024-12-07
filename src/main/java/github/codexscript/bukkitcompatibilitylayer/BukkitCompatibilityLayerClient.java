package github.codexscript.bukkitcompatibilitylayer;

import github.codexscript.bukkitcompatibilitylayer.events.ClientPlayerJoinListener;
import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class BukkitCompatibilityLayerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NetworkingMessages.registerClientsidePackets();
        ClientPlayConnectionEvents.JOIN.register(ClientPlayerJoinListener::onPlayerJoin);
    }
}
