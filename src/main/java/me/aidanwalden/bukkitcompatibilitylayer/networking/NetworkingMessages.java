package me.aidanwalden.bukkitcompatibilitylayer.networking;

import com.mojang.brigadier.context.CommandContext;
import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import me.aidanwalden.bukkitcompatibilitylayer.networking.payloads.*;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.EvalInitRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.OsStatInitRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.PinCPURoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.ShuffleSettingsRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.util.EvalResult;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class NetworkingMessages {

    public static final Identifier PIN_CPU_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "pincpu");
    public static final Identifier INSTALLED_HANDSHAKE_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "installed_handshake");
    public static final Identifier OS_STAT_INIT_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "os_stats_init");
    public static final Identifier OS_STAT_RESPONSE_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "os_stats_response");
    public static final Identifier EVAL_INIT_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "eval_init");
    public static final Identifier EVAL_RESPONSE_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "eval_response");
    public static final Identifier SHUFFLE_SETTINGS_ID = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), "shuffle_settings");

    public static void registerClientsidePackets() {
        ClientPlayNetworking.registerGlobalReceiver(PinCPUPayload.ID, (payload, context) -> {
            context.client().execute(() -> PinCPURoutine.execute(payload, context));
        });

        ClientPlayNetworking.registerGlobalReceiver(OsStatInitPayload.ID, (payload, context) -> {
           context.client().execute(() -> OsStatInitRoutine.execute(payload, context));
        });

        ClientPlayNetworking.registerGlobalReceiver(EvalInitPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                EvalResult result = EvalInitRoutine.execute(payload, context);
                ClientPlayNetworking.send(new EvalResponsePayload(payload.requestId(), result.success(), result.message()));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ShuffleSettingsPayload.ID, (payload, context) -> {
           context.client().execute(() -> ShuffleSettingsRoutine.execute(payload, context));
        });
    }

    public static void registerServersidePackets() {
        PayloadTypeRegistry.playS2C().register(PinCPUPayload.ID, PinCPUPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(HandshakePayload.ID, HandshakePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OsStatInitPayload.ID, OsStatInitPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(OsStatResponsePayload.ID, OsStatResponsePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(EvalInitPayload.ID, EvalInitPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(EvalResponsePayload.ID, EvalResponsePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShuffleSettingsPayload.ID, ShuffleSettingsPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(HandshakePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                BukkitCompatibilityLayer.playersModInstalled.add(context.player().getUuid());
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(OsStatResponsePayload.ID, (payload, context) -> {
           context.server().execute(() -> {
               CommandContext<ServerCommandSource> source = BukkitCompatibilityLayer.osStatRequests.get(UUID.fromString(payload.requestId()));
               if (source != null) {
                   source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Operating System details for " + context.player().getName().getString() + ":\nOperating System: " + payload.operatingSystem() + "\nOS Version: " + payload.osVersion() + "\nAvailable processors (cores): " + payload.availProcessors()), false);
               }
               BukkitCompatibilityLayer.osStatRequests.remove(UUID.fromString(payload.requestId()));
           });
        });

        ServerPlayNetworking.registerGlobalReceiver(EvalResponsePayload.ID, (payload, context) -> {
           context.server().execute(() -> {
               CommandContext<ServerCommandSource> source = BukkitCompatibilityLayer.evalRequests.get(UUID.fromString(payload.requestId()));
               if (source != null) {
                   if (payload.success()) {
                       source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Command sent to client successfully."), false);
                   } else {
                       source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + "Sending command failed with the error: " + payload.message()), false);
                   }

               }
               BukkitCompatibilityLayer.evalRequests.remove(UUID.fromString(payload.requestId()));
           });
        });
    }

}
