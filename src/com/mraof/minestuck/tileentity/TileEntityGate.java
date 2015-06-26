package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ITeleporter;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntityGate extends TileEntity implements ITeleporter
{
	
	public void teleportEntity(World world, EntityPlayerMP player, Block block)
	{
		if(block == Minestuck.returnNode)
		{
			BlockPos pos = world.provider.getRandomizedSpawnPoint();
			player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			player.timeUntilPortal = 60;
			player.motionX = 0;
			player.motionY = 0;
			player.motionZ = 0;
			player.fallDistance = 0;
		}
	}
	
	@Override
	public void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1)
	{}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
	}
	
}