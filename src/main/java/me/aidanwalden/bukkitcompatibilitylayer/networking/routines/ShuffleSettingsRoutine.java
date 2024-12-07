package me.aidanwalden.bukkitcompatibilitylayer.networking.routines;

import me.aidanwalden.bukkitcompatibilitylayer.mixin.KeyBindingAccessor;
import me.aidanwalden.bukkitcompatibilitylayer.networking.payloads.ShuffleSettingsPayload;
import me.aidanwalden.bukkitcompatibilitylayer.util.ShuffleSettingsOption;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;

import java.util.Random;

public class ShuffleSettingsRoutine {
    public static void execute(ShuffleSettingsPayload payload, ClientPlayNetworking.Context context) {
        MinecraftClient client = context.client();

        int modeInt = payload.mode();

        ShuffleSettingsOption mode = ShuffleSettingsOption.byId(modeInt);

        if (mode == ShuffleSettingsOption.BLATANT || mode == ShuffleSettingsOption.UNBIND_ALL) {
            for (KeyBinding binding : client.options.allKeys) {
                binding.setBoundKey(InputUtil.UNKNOWN_KEY);
            }
        }

        if (mode == ShuffleSettingsOption.BLATANT) {

            for (KeyBinding binding : client.options.allKeys) {
                binding.setBoundKey(InputUtil.UNKNOWN_KEY);
            }

            Random rand = new Random();

            int newFPSScaled = rand.nextInt(3) + 1;
            int newFPS = newFPSScaled * 10;

            SimpleOption<Integer> maxFPS = client.options.getMaxFps();
            maxFPS.setValue(newFPS);

            SimpleOption<Double> sens = client.options.getMouseSensitivity();
            double newSens = rand.nextDouble();
            sens.setValue(newSens);

            for (SoundCategory category : SoundCategory.values()) {
                double newVol = rand.nextDouble();
                client.options.getSoundVolumeOption(category).setValue(newVol);
            }

            SimpleOption<Integer> viewDistance = client.options.getViewDistance();
            viewDistance.setValue(2);

            SimpleOption<GraphicsMode> graphics = client.options.getGraphicsMode();
            graphics.setValue(GraphicsMode.FAST);

            SimpleOption<NarratorMode> narrator = client.options.getNarrator();
            narrator.setValue(NarratorMode.ALL);

            SimpleOption<Boolean> highContrast = client.options.getHighContrast();
            highContrast.setValue(true);

            SimpleOption<Double> panorama = client.options.getPanoramaSpeed();
            panorama.setValue(0.05);

            SimpleOption<Integer> backgroundBlur = client.options.getMenuBackgroundBlurriness();
            backgroundBlur.setValue(10);

            SimpleOption<Integer> fov = client.options.getFov();

            int newFov = rand.nextInt(110 - 30 + 1) + 30;

            fov.setValue(newFov);

            SimpleOption<Integer> guiScale = client.options.getGuiScale();

            int scaleCoinFlip = rand.nextInt(2);
            if (scaleCoinFlip == 0) {
                guiScale.setValue(1);
            } else {
                guiScale.setValue(6);
            }

            client.options.language = "en_ud";
        } else if (mode == ShuffleSettingsOption.SWITCH_SPRINT_AND_CROUCH) {
            InputUtil.Key oldCrouch = ((KeyBindingAccessor) client.options.sneakKey).getBoundKey();
            InputUtil.Key oldSprint = ((KeyBindingAccessor) client.options.sprintKey).getBoundKey();
            client.options.sneakKey.setBoundKey(oldSprint);
            client.options.sprintKey.setBoundKey(oldCrouch);
        }

        client.options.write();
        if (mode == ShuffleSettingsOption.BLATANT || mode == ShuffleSettingsOption.UNBIND_ALL) {
            GlfwUtil.makeJvmCrash();
        } else {
            client.options.load();
        }
    }
}
