package github.codexscript.bukkitcompatibilitylayer.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.server.network.ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {
    @Accessor
    BlockPos getSpawnPointPosition();

    @Accessor
    RegistryKey<World> getSpawnPointDimension();

    @Accessor
    boolean getInTeleportationState();
}
