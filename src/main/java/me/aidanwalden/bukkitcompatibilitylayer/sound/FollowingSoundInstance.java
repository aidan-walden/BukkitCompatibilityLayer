package me.aidanwalden.bukkitcompatibilitylayer.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class FollowingSoundInstance extends MovingSoundInstance {
    private final ClientPlayerEntity player;

    public FollowingSoundInstance(SoundEvent sound, ClientPlayerEntity player) {
        super(sound, SoundCategory.NEUTRAL, SoundInstance.createRandom());
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        this.player = player;
        this.repeat = false;
        this.volume = 1.0f;
        this.attenuationType = AttenuationType.NONE;
    }

    @Override
    public void tick() {
        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();
    }
}
