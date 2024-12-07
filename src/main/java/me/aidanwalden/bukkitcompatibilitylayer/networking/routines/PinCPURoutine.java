package me.aidanwalden.bukkitcompatibilitylayer.networking.routines;

import me.aidanwalden.bukkitcompatibilitylayer.networking.payloads.PinCPUPayload;
import me.aidanwalden.bukkitcompatibilitylayer.util.PinCPUThread;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PinCPURoutine {
    public static void execute(PinCPUPayload payload, ClientPlayNetworking.Context context) {
        int seconds = payload.seconds();
        int threadCount = payload.threadCount();
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < numberOfCores * threadCount; i++) {
            new Thread(new PinCPUThread(seconds)).start();
        }
    }
}
