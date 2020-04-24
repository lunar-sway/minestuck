package com.mraof.minestuck.network;

import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.network.PacketBuffer;

public class ConsortReputationDataPacket implements PlayToClientPacket
{
    private final long count;
    
    private ConsortReputationDataPacket(long count)
    {
        this.count = count;
    }
    
    public static ConsortReputationDataPacket create(long count)
    {
        return new ConsortReputationDataPacket(count);
    }
    
    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeLong(count);
    }
    
    public static ConsortReputationDataPacket decode(PacketBuffer buffer)
    {
        long count = buffer.readLong();
        return create(count);
    }
    
    @Override
    public void execute()
    {
        ClientPlayerData.consortReputation = count;
    }
}
