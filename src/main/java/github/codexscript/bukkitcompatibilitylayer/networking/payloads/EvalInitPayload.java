package github.codexscript.bukkitcompatibilitylayer.networking.payloads;

import github.codexscript.bukkitcompatibilitylayer.networking.NetworkingMessages;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record EvalInitPayload(String requestId, boolean visible, String command) implements CustomPayload {
    public static final CustomPayload.Id<EvalInitPayload> ID = new CustomPayload.Id<>(NetworkingMessages.EVAL_INIT_ID);
    public static final PacketCodec<RegistryByteBuf, EvalInitPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, EvalInitPayload::requestId, PacketCodecs.BOOL, EvalInitPayload::visible, PacketCodecs.STRING, EvalInitPayload::command, EvalInitPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
