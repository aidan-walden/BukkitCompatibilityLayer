package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record PinCPUPayload(int seconds, int threadCount) implements CustomPayload {
    public static final CustomPayload.Id<PinCPUPayload> ID = new CustomPayload.Id<>(NetworkingMessages.PIN_CPU_ID);
    public static final PacketCodec<RegistryByteBuf, PinCPUPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, PinCPUPayload::seconds, PacketCodecs.INTEGER, PinCPUPayload::threadCount, PinCPUPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
