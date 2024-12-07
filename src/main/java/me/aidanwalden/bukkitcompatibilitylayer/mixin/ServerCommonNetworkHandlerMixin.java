package me.aidanwalden.bukkitcompatibilitylayer.mixin;

import com.mojang.authlib.GameProfile;
import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerCommonNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method = "sendPacket", cancellable = true)
    public void sendPacket(Packet<?> packet, CallbackInfo ci) {
        GameProfile profile = ((ServerCommonNetworkHandlerInvoker) this).getProfile();
        if (BukkitCompatibilityLayer.playersGhosting.get(profile.getId()) != null && BukkitCompatibilityLayer.playersGhosting.get(profile.getId())) {
            ci.cancel();
        }
    }
}
