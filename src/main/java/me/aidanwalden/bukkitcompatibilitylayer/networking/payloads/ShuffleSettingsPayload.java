package me.aidanwalden.bukkitcompatibilitylayer.networking.payloads;

import me.aidanwalden.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ShuffleSettingsPayload(int mode) implements CustomPayload {
    public static final CustomPayload.Id<ShuffleSettingsPayload> ID = new CustomPayload.Id<>(NetworkingMessages.SHUFFLE_SETTINGS_ID);
    public static final PacketCodec<RegistryByteBuf, ShuffleSettingsPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, ShuffleSettingsPayload::mode, ShuffleSettingsPayload::new);


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
