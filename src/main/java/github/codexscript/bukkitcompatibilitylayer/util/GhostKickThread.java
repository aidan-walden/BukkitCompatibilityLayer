package github.codexscript.bukkitcompatibilitylayer.util;

import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.mixin.ServerPlayNetworkHandlerMixin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.concurrent.TimeUnit;

public class GhostKickThread implements Runnable {
    private final ServerPlayerEntity player;

    public GhostKickThread(ServerPlayerEntity player) {
        this.player = player;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);

            // handleDisconnection() called twice warning will be outputted to console by Minecraft here
            // This is because of a known Mojang bug
            // MC-248335
            if (!player.isDisconnected()) {
                player.networkHandler.disconnect(Text.of("Internal Exception: java.IOException: An established connection was aborted by the software in your host machine"));
                BukkitCompatibilityLayer.playersGhosting.put(player.getUuid(), false);
            }
            TimeUnit.SECONDS.sleep(10);
            BukkitCompatibilityLayer.playersGhosting.remove(player.getUuid());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
