package github.codexscript.bukkitcompatibilitylayer.mixin;

import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.server.network.ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    // Effectively throw out all received updates from client if player ghosted
    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void tick(CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null && !BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) && !player.isDisconnected()) {
            ((ServerCommonNetworkHandlerAccessor)(Object)this).getConnection().tryDisableAutoRead();
            BukkitCompatibilityLayer.playersGhosting.put(player.getUuid(), true);
        }
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }

    }

    // Literally stop all communications from server if player ghosted
//    @Inject(at = @At("HEAD"), method = "", cancellable = true)
//    private void sendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
//        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
//        if (BukkitCompatibilityLayer.playersGhosting.contains(player.getUuid())) {
//            ci.cancel();
//        }
//    }

    // For some reason cancelling tick is not enough to stop movement
    @Inject(at = @At("HEAD"), method = "onPlayerMove", cancellable = true)
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerAction", cancellable = true)
    private void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerInteractEntity", cancellable = true)
    private void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerInteractBlock", cancellable = true)
    private void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onPlayerInteractItem", cancellable = true)
    private void onPlayerInteractItem(PlayerInteractItemC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

//    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/server/network/ConnectedClientData;)V")
//    public void ServerPlayNetworkHandler(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
//        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
//            if (!player.isDisconnected()) {
//                player.networkHandler.disconnect(Text.of("Connection refused: no further information"));
//            }
//        }
//    }

    @Inject(at = @At("HEAD"), method = "onChatMessage", cancellable = true)
    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        if (BukkitCompatibilityLayer.playersGhosting.get(player.getUuid()) != null) {
            ci.cancel();
        }
    }

}
