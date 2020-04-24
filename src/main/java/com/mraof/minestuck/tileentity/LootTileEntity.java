package com.mraof.minestuck.tileentity;

import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class LootTileEntity extends TileEntity
{

    public LootTileEntity()
    {
        super(MSTileEntityTypes.LOOT_BLOCK);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

}
