package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;

public class ConsortReputationDataPacket implements MSPacket.PlayToClient
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
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeInt(count);
    }
    
    public static ConsortReputationDataPacket decode(FriendlyByteBuf buffer)
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
