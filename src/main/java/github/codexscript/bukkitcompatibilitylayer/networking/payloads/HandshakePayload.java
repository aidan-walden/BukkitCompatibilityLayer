package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record HandshakePayload() implements CustomPayload {
    public static final CustomPayload.Id<HandshakePayload> ID = new CustomPayload.Id<>(NetworkingMessages.INSTALLED_HANDSHAKE_ID);
    public static final PacketCodec<RegistryByteBuf, HandshakePayload> CODEC = PacketCodec.unit(new HandshakePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
