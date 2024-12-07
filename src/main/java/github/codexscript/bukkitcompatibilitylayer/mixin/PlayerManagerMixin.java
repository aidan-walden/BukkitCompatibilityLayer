package github.codexscript.bukkitcompatibilitylayer.mixin;

import com.mojang.authlib.GameProfile;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At("HEAD"), method = "checkCanJoin", cancellable = true)
    public void checkCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
        if (BukkitCompatibilityLayer.playersGhosting.get(profile.getId()) != null) {
            cir.setReturnValue(Text.of("Connection refused: no further information"));
        }
    }
}
