package github.codexscript.bukkitcompatibilitylayer.events;

import github.codexscript.bukkitcompatibilitylayer.BukkitCompatibilityLayer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.Optional;

public class UseBlockListener {
    private static final Block END_PORTAL_FRAME = Block.getBlockFromItem(net.minecraft.item.Items.END_PORTAL_FRAME);

    public static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        MinecraftServer server = world.getServer();

        if (server == null) {
            return ActionResult.PASS;
        }

        if (!server.getGameRules().getBoolean(BukkitCompatibilityLayer.DISABLE_END)) {
            return ActionResult.PASS;
        }

        Block block = world.getBlockState(hitResult.getBlockPos()).getBlock();
        if (block.equals(END_PORTAL_FRAME) && player.getStackInHand(hand).isOf(Items.ENDER_EYE)) {
            MutableText message = Text.empty();
            Optional<TextColor> color = TextColor.parse("red").result();
            assert color.isPresent();
            message.setStyle(message.getStyle().withColor(color.get()));
            message.append(Text.of("End portal frames are disabled in this world!"));
            player.sendMessage(message, false);
            player.swingHand(hand);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
