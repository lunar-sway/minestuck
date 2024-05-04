package com.mraof.minestuck.network.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ConsortReputationDataPacket implements MSPacket.PlayToClient
{
    public static final ResourceLocation ID = Minestuck.id("consort_reputation_data");
    
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
    public ResourceLocation id()
    {
        return ID;
    }
    
    @Override
    public void write(FriendlyByteBuf buffer)
    {
        buffer.writeInt(count);
    }
    
    public static ConsortReputationDataPacket read(FriendlyByteBuf buffer)
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
