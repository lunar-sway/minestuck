package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SessionHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGate extends TileEntity
{
	
	@SideOnly(Side.CLIENT)
	public int colorIndex;
	
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
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("color", SessionHandler.getColorForDimension(this.worldObj.provider.getDimensionId()));
		return new S35PacketUpdateTileEntity(this.pos, 0, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.colorIndex = pkt.getNbtCompound().getInteger("color");
	}
	
}