package github.codexscript.bukkitcompatibilitylayer.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerCommonNetworkHandler.class)
public interface ServerCommonNetworkHandlerInvoker {
    @Invoker("getProfile")
    GameProfile getProfile();
}
