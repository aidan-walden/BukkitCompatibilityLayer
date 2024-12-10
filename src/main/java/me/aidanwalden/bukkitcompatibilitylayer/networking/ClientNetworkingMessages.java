package me.aidanwalden.bukkitcompatibilitylayer.networking;

import me.aidanwalden.bukkitcompatibilitylayer.networking.payloads.*;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.EvalInitRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.OsStatInitRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.PinCPURoutine;
import me.aidanwalden.bukkitcompatibilitylayer.networking.routines.ShuffleSettingsRoutine;
import me.aidanwalden.bukkitcompatibilitylayer.sound.FollowingSoundInstance;
import me.aidanwalden.bukkitcompatibilitylayer.sound.ModSounds;
import me.aidanwalden.bukkitcompatibilitylayer.util.EvalResult;
import me.aidanwalden.bukkitcompatibilitylayer.util.ScareOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
public class ClientNetworkingMessages {
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

        ClientPlayNetworking.registerGlobalReceiver(ScarePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                int id = payload.mode();
                ScareOption mode = ScareOption.byId(id);
                if (mode == null) {
                    return;
                } else if (mode == ScareOption.STALKER) {
                    context.client().getSoundManager().play(new FollowingSoundInstance(ModSounds.STALKER_SCREAM, context.player()));
                } else if (mode == ScareOption.DISCORD) {
                    context.client().getSoundManager().play(new FollowingSoundInstance(ModSounds.DISCORD, context.player()));
                }
            });
        });
    }
}
