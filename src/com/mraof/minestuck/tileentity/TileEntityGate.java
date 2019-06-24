package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.PositionTeleporter;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGate extends TileEntity
{
	
	@OnlyIn(Dist.CLIENT)
	public int colorIndex;
	
	public int gateCount;
	
	public TileEntityGate()
	{
		super(MinestuckTiles.GATE);
	}
	
	public void teleportEntity(World world, EntityPlayerMP player, Block block)
	{
		if(block == MinestuckBlocks.RETURN_NODE)
		{
			BlockPos pos = world.getSpawnPoint();
			PositionTeleporter.moveEntity(player, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			player.timeUntilPortal = player.getPortalCooldown();
			player.motionX = 0;
			player.motionY = 0;
			player.motionZ = 0;
			player.fallDistance = 0;
			//player.addStat(MinestuckAchievementHandler.returnNode);
		} else
		{
			GateHandler.teleport(gateCount, world.getDimension().getType(), player);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getPos().add(-1, 0, -1), this.getPos().add(1, 1, 1));
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		if(compound.hasKey("gateCount"))
			this.gateCount = compound.getInt("gateCount");
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		if(this.gateCount != 0)
			compound.setInt("gateCount", gateCount);
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInt("color", SburbHandler.getColorForDimension(world));
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInt("color", SburbHandler.getColorForDimension(world));
		return new SPacketUpdateTileEntity(this.pos, 0, nbt);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		this.colorIndex = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public boolean isGate()
	{
		return this.world != null ? this.world.getBlockState(this.getPos()).getBlock() != MinestuckBlocks.RETURN_NODE : this.gateCount != 0;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return isGate() ? 65536.0D : 4096.0D;
	}
	
}