package me.aidanwalden.bukkitcompatibilitylayer.networking.payloads;

import me.aidanwalden.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ScarePayload(int mode) implements CustomPayload {
    public static final CustomPayload.Id<ScarePayload> ID = new CustomPayload.Id<>(NetworkingMessages.SCARE_ID);
    public static final PacketCodec<RegistryByteBuf, ScarePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, ScarePayload::mode, ScarePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}