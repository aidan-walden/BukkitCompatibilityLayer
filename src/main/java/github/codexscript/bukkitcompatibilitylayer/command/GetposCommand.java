package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Optional;

public class GetposCommand {

    private static String getDimension(World world) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return "Overworld";
        } else if (world.getRegistryKey() == World.END) {
            return "End";
        } else if (world.getRegistryKey() == World.NETHER) {
            return "Nether";
        } else {
            return world.getDimensionEntry().getType().toString();
        }
    }

    private static String getDimension(ServerWorld world) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return "Overworld";
        } else if (world.getRegistryKey() == World.END) {
            return "End";
        } else if (world.getRegistryKey() == World.NETHER) {
            return "Nether";
        } else {
            return world.getDimensionEntry().getType().toString();
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("getpos")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                        .executes(GetposCommand::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> source) throws CommandSyntaxException {
        GameProfile requestedProfile = GameProfileArgumentType.getProfileArgument(source, "player").iterator().next();
        MinecraftServer minecraftServer = source.getSource().getServer();
        ServerPlayerEntity requestedPlayer = minecraftServer.getPlayerManager().getPlayer(requestedProfile.getName());

        BlockPos playerPos;
        String dimension;

        if (requestedPlayer == null) {
            requestedPlayer = minecraftServer.getPlayerManager().createPlayer(requestedProfile, SyncedClientOptions.createDefault());
            Optional<NbtCompound> compound = minecraftServer.getPlayerManager().loadPlayerData(requestedPlayer);
            if (compound.isPresent()) {
                ServerWorld world = minecraftServer.getWorld(
                        DimensionType.worldFromDimensionNbt(new Dynamic<>(NbtOps.INSTANCE, compound.get()))
                                .result().get());
                if (world != null) {
                    dimension = getDimension(world);
                } else {
                    dimension = "";
                }

                playerPos = requestedPlayer.getBlockPos();
            } else {
                dimension = "";
                playerPos = null;
            }
        } else {
            playerPos = requestedPlayer.getBlockPos();
            dimension = getDimension(requestedPlayer.getWorld());
        }

        if (playerPos == null) {
            source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Player not found."), false);
            return 0;
        }

        ServerPlayerEntity finalRequestedPlayer = requestedPlayer;
        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + finalRequestedPlayer.getName().getString() + " is at x: " + playerPos.getX() + " y: " + playerPos.getY() + " z: " + playerPos.getZ() + " in dimension " + dimension), false);
        return 1;
    }
}
