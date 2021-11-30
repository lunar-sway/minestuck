package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.ItemMagnetBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ItemMagnetTileEntity extends TileEntity implements ITickableTileEntity
{
	private int gatherLength;
	
	public ItemMagnetTileEntity()
	{
		super(MSTileEntityTypes.ITEM_MAGNET.get());
	}
	
	@Override
	public void tick()
	{
		if(world == null || !world.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks
		
		sendUpdate();
	}
	
	private void sendUpdate()
	{
		if(world != null/* && !world.isRemote*/)
		{
			int powerIn = world.getRedstonePowerFromNeighbors(pos);
			gatherLength = powerIn;
			
			if(powerIn > 0)
			{
				Direction blockFacing = world.getBlockState(pos).get(ItemMagnetBlock.FACING);
				BlockPos offsetPosClose = pos.offset(blockFacing);
				BlockPos offsetPosFar = offsetPosClose.offset(blockFacing, gatherLength);
				
				AxisAlignedBB axisalignedbb = new AxisAlignedBB(
						offsetPosClose.getX() + 0.5, offsetPosClose.getY() + 0.5, offsetPosClose.getZ() + 0.5,
						offsetPosFar.getX() + 0.5, offsetPosFar.getY() + 0.5, offsetPosFar.getZ() + 0.5).grow(0.5);
				List<ItemEntity> list = world.getEntitiesWithinAABB(ItemEntity.class, axisalignedbb);
				if(!list.isEmpty())
				{
					for(ItemEntity itemEntity : list)
					{
						Vec3d motionVec3d = new Vec3d(blockFacing.getOpposite().getDirectionVec());
						motionVec3d.add(itemEntity.getMotion());
						itemEntity.setMotion(motionVec3d.scale(0.3));
					}
				}
			}
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		gatherLength = compound.getInt("gatherLength");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("gatherLength", gatherLength);
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}