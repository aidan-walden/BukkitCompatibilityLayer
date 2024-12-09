package me.aidanwalden.bukkitcompatibilitylayer.networking.payloads;

import me.aidanwalden.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record StalkerScreamPayload() implements CustomPayload {
    public static final CustomPayload.Id<StalkerScreamPayload> ID = new CustomPayload.Id<>(NetworkingMessages.STALKER_SCREAM_ID);
    public static final PacketCodec<RegistryByteBuf, StalkerScreamPayload> CODEC = PacketCodec.unit(new StalkerScreamPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
