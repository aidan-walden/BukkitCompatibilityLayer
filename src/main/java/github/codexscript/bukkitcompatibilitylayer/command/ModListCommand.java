package github.codexscript.bukkitcompatibilitylayer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ModListCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("mods")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(ModListCommand::execute));
    }

    private static int execute(CommandContext<ServerCommandSource> source) {
        StringBuilder modList = new StringBuilder("Loaded mods: §2");
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            ModMetadata metadata = mod.getMetadata();
            String id = metadata.getId();
            String type = metadata.getType();
            // if statement lifted from source code of Mod Menu by Prospector
            if (type.equals("builtin") || id.startsWith("fabric") && (id.equals("fabricloader") || metadata.getProvides().contains("fabricloader") || id.equals("fabric") || id.equals("fabric-api") || metadata.getProvides().contains("fabric") || metadata.getProvides().contains("fabric-api"))) {
                continue;
            }
            modList.append(mod.getMetadata().getName()).append(", ");
        }


        source.getSource().sendFeedback(() -> Text.literal(BukkitCompatibilityLayer.CHAT_PREFIX + modList + "§r"), false);
        return 1;
    }
}
