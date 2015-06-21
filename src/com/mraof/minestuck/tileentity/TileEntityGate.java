package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.util.ITeleporter;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;

public class TileEntityGate extends TileEntity implements ITeleporter
{
	
	@Override
	public void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1)
	{
		
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
	}
	
}