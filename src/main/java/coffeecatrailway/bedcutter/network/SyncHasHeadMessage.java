package coffeecatrailway.bedcutter.network;

import coffeecatrailway.bedcutter.network.handler.ICutterClientPlayHandler;
import io.github.ocelot.sonar.common.network.message.SonarMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 23/10/2020
 */
public class SyncHasHeadMessage implements SonarMessage<ICutterClientPlayHandler>
{
    private boolean hasHead;
    private int owner;

    public SyncHasHeadMessage()
    {
    }

    public SyncHasHeadMessage(boolean hasHead, int owner)
    {
        this.hasHead = hasHead;
        this.owner = owner;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        this.hasHead = buf.readBoolean();
        this.owner = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeBoolean(this.hasHead);
        buf.writeInt(this.owner);
    }

    @Override
    public void processPacket(ICutterClientPlayHandler handler, NetworkEvent.Context ctx)
    {
        handler.handleSyncHasCutterMessage(this, ctx);
    }

    public boolean hasHead()
    {
        return hasHead;
    }

    public int getOwner()
    {
        return owner;
    }
}
