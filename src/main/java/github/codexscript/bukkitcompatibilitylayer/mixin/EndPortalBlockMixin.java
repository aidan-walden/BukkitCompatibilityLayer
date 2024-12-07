package github.codexscript.bukkitcompatibilitylayer.mixin;

import com.mojang.serialization.DataResult;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(net.minecraft.block.EndPortalBlock.class)
public abstract class EndPortalBlockMixin {
    // On Entity Collision (End Portal)
    @Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
    private void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info) {
        MinecraftServer server = world.getServer();

        if (server == null) {
            return;
        }

        if (server.getGameRules().getBoolean(BukkitCompatibilityLayer.DISABLE_END)) { // If our game rule is set to true
            if (!entity.isPlayer()) {
                return;
            }

            PlayerEntity player = (PlayerEntity) entity;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerPlayerEntityAccessor accessor = (ServerPlayerEntityAccessor) serverPlayer;
            if (accessor.getInTeleportationState()) { // If the player is in the teleportation state (e.g. we are teleporting them)
                info.cancel(); // Cancel the event
                return;
            }
            MutableText message = Text.empty();
            Optional<TextColor> color = TextColor.parse("red").result();
            assert color.isPresent();
            message.setStyle(message.getStyle().withColor(color.get()));
            message.append(Text.of("The End is disabled in this world! You have been teleported to your spawn point."));

            BlockPos respawnPosition = accessor.getSpawnPointPosition();
            RegistryKey<World> respawnDimension = ((ServerPlayerEntityAccessor) serverPlayer).getSpawnPointDimension();
            ServerWorld respawnWorld = serverPlayer.getServer().getWorld(respawnDimension);
            if (respawnPosition != null && respawnDimension != null) { // If the player has a spawn point set
                serverPlayer.teleport(respawnWorld, respawnPosition.getX(), respawnPosition.getY(), respawnPosition.getZ(), null, 0, 0);
            } else { // If the player does not have a spawn point set
                assert respawnWorld != null;
                BlockPos worldSpawnPos = respawnWorld.getSpawnPos();
                serverPlayer.teleport(respawnWorld, worldSpawnPos.getX(), worldSpawnPos.getY(), worldSpawnPos.getZ(), null, 0, 0);
            }
            player.sendMessage(message, false);
            info.cancel(); // Cancel the vanilla event
        }
    }
}
