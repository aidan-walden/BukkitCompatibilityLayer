package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record EvalResponsePayload(String requestId, boolean success, String message) implements CustomPayload {
    public static final CustomPayload.Id<EvalResponsePayload> ID = new CustomPayload.Id<>(NetworkingMessages.EVAL_RESPONSE_ID);
    public static final PacketCodec<RegistryByteBuf, EvalResponsePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, EvalResponsePayload::requestId, PacketCodecs.BOOL, EvalResponsePayload::success, PacketCodecs.STRING, EvalResponsePayload::message, EvalResponsePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
