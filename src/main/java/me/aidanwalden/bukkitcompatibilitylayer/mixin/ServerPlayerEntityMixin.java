package me.aidanwalden.bukkitcompatibilitylayer.mixin;

import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import me.aidanwalden.bukkitcompatibilitylayer.StateSaverAndLoader;
import me.aidanwalden.bukkitcompatibilitylayer.command.SlowMineCommand;
import me.aidanwalden.bukkitcompatibilitylayer.command.SlowWalkCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Unique
    private static final Random RANDOM = new Random();


    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        Boolean shouldDoSlowMine = BukkitCompatibilityLayer.playersSlowMine.get(player.getUuid());
        Boolean shouldDoSlowWalk = BukkitCompatibilityLayer.playersSlowWalk.get(player.getUuid());

        if (shouldDoSlowMine == null) {
            shouldDoSlowMine = StateSaverAndLoader.getPlayerState(player).doSlowMine;
            BukkitCompatibilityLayer.playersSlowMine.put(player.getUuid(), shouldDoSlowMine);
        }

        if (shouldDoSlowWalk == null) {
            shouldDoSlowWalk = StateSaverAndLoader.getPlayerState(player).doSlowWalk;
            BukkitCompatibilityLayer.playersSlowWalk.put(player.getUuid(), shouldDoSlowWalk);
        }

        if (shouldDoSlowMine) {
            int roll = RANDOM.nextInt(10000);

            if (roll == 0) {
                SlowMineCommand.decrement(player);
            }
        }

        if (shouldDoSlowWalk) {
            int roll = RANDOM.nextInt(10000);

            if (roll == 0) {
                SlowWalkCommand.decrement(player);
            }
        }
    }
}
