package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.PacketBuffer;

public class ConsortReputationDataPacket implements PlayToClientPacket
{
    private final int count;
    
    private ConsortReputationDataPacket(int count)
    {
        this.count = count;
    }
    
    public static ConsortReputationDataPacket create(int count)
    {
        return new ConsortReputationDataPacket(count);
    }
    
    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeInt(count);
    }
    
    public static ConsortReputationDataPacket decode(PacketBuffer buffer)
    {
        int count = buffer.readInt();
        return create(count);
    }
    
    @Override
    public void execute()
    {
        ClientPlayerData.handleDataPacket(this);
    }
    
    public int getCount()
    {
        return count;
    }
}
