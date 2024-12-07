package github.codexscript.bukkitcompatibilitylayer.networking.routines;

import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.OsStatInitPayload;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.OsStatResponsePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class OsStatInitRoutine {

    public static void execute(OsStatInitPayload payload, ClientPlayNetworking.Context context) {
        String requestId = payload.requestId();
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ClientPlayNetworking.send(new OsStatResponsePayload(requestId, osName, osVersion, availableProcessors));
    }
}
