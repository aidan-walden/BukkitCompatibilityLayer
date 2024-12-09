package me.aidanwalden.bukkitcompatibilitylayer.networking.routines;

import me.aidanwalden.bukkitcompatibilitylayer.sound.StalkerScreamSoundInstance;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class StalkerScareRoutine {

    public static void execute(ClientPlayNetworking.Context context) {
        context.client().getSoundManager().play(new StalkerScreamSoundInstance(context.player()));
    }
}
