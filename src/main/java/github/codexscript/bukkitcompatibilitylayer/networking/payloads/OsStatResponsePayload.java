package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record OsStatResponsePayload(String requestId, String operatingSystem, String osVersion, int availProcessors) implements CustomPayload {
    public static final CustomPayload.Id<OsStatResponsePayload> ID = new CustomPayload.Id<>(NetworkingMessages.OS_STAT_RESPONSE_ID);
    public static final PacketCodec<RegistryByteBuf, OsStatResponsePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, OsStatResponsePayload::requestId, PacketCodecs.STRING, OsStatResponsePayload::operatingSystem, PacketCodecs.STRING, OsStatResponsePayload::osVersion, PacketCodecs.INTEGER, OsStatResponsePayload::availProcessors, OsStatResponsePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
