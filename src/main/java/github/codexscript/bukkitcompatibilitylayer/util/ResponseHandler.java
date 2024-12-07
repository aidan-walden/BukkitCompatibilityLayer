package github.codexscript.bukkitcompatibilitylayer.util;

import com.mojang.brigadier.context.CommandContext;

import java.net.http.HttpResponse;

@FunctionalInterface
public interface ResponseHandler<HttpResponse, ServerPlayerEntity, CommandContext> {
    void accept(HttpResponse response, ServerPlayerEntity player, CommandContext source);
}
