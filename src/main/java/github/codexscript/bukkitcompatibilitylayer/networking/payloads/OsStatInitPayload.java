package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OsStatInitPayload(String requestId) implements CustomPayload {
    public static final CustomPayload.Id<OsStatInitPayload> ID = new CustomPayload.Id<>(NetworkingMessages.OS_STAT_INIT_ID);
    public static final PacketCodec<RegistryByteBuf, OsStatInitPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, OsStatInitPayload::requestId, OsStatInitPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}