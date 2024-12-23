package me.aidanwalden.bukkitcompatibilitylayer.sound;

import me.aidanwalden.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModSounds {

    public static final SoundEvent STALKER_SCREAM = registerSoundEvent("stalker_scream");
    public static final SoundEvent DISCORD = registerSoundEvent("discord_ping");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(BukkitCompatibilityLayer.MOD_NAME.toLowerCase(), name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        BukkitCompatibilityLayer.LOGGER.info("Registering sounds for " + BukkitCompatibilityLayer.MOD_NAME);
    }
}
