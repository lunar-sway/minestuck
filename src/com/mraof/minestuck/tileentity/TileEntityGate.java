package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.world.GateHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGate extends TileEntity
{
	
	@SideOnly(Side.CLIENT)
	public int colorIndex;
	
	public int gateCount;
	
	public void teleportEntity(World world, EntityPlayerMP player, Block block)
	{
		if(block == MinestuckBlocks.returnNode)
		{
			BlockPos pos = world.provider.getRandomizedSpawnPoint();
			player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			player.timeUntilPortal = player.getPortalCooldown();
			player.motionX = 0;
			player.motionY = 0;
			player.motionZ = 0;
			player.fallDistance = 0;
			player.addStat(MinestuckAchievementHandler.returnNode);
		} else
		{
			GateHandler.teleport(gateCount, worldObj.provider.getDimension(), player);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if(compound.hasKey("gateCount"))
			this.gateCount = compound.getInteger("gateCount");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if(this.gateCount != 0)
			compound.setInteger("gateCount", gateCount);
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInteger("color", SburbHandler.getColorForDimension(this.worldObj.provider.getDimension()));
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("color", SburbHandler.getColorForDimension(this.worldObj.provider.getDimension()));
		return new SPacketUpdateTileEntity(this.pos, 0, nbt);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.colorIndex = tag.getInteger("color");
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public boolean isGate()
	{
		return this.worldObj != null ? this.worldObj.getBlockState(this.getPos()).getBlock() != MinestuckBlocks.returnNode : this.gateCount != 0;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return isGate() ? 65536.0D : 4096.0D;
	}
	
}