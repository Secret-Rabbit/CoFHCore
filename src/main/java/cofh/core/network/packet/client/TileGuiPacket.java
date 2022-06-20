package cofh.core.network.packet.client;

import cofh.core.CoFHCore;
import cofh.lib.content.block.entity.IPacketHandlerTile;
import cofh.core.network.packet.IPacketClient;
import cofh.core.network.packet.PacketBase;
import cofh.core.util.ProxyUtils;
import cofh.core.util.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import static cofh.lib.util.Constants.PACKET_GUI;

public class TileGuiPacket extends PacketBase implements IPacketClient {

    protected BlockPos pos;
    protected FriendlyByteBuf buffer;

    public TileGuiPacket() {

        super(PACKET_GUI, CoFHCore.PACKET_HANDLER);
    }

    @Override
    public void handleClient() {

        Level world = ProxyUtils.getClientWorld();
        if (world == null) {
            CoFHCore.LOG.error("Client world is null! (Is this being called on the server?)");
            return;
        }
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof IPacketHandlerTile) {
            ((IPacketHandlerTile) tile).handleGuiPacket(buffer);
        }
    }

    @Override
    public void write(FriendlyByteBuf buf) {

        buf.writeBlockPos(pos);
        buf.writeBytes(buffer);
    }

    @Override
    public void read(FriendlyByteBuf buf) {

        buffer = buf;
        pos = buffer.readBlockPos();
    }

    public static void sendToClient(IPacketHandlerTile tile, ServerPlayer player) {

        if (tile.world() == null || Utils.isClientWorld(tile.world())) {
            return;
        }
        TileGuiPacket packet = new TileGuiPacket();
        packet.pos = tile.pos();
        packet.buffer = tile.getGuiPacket(new FriendlyByteBuf(Unpooled.buffer()));
        packet.sendToPlayer(player);
    }

}
