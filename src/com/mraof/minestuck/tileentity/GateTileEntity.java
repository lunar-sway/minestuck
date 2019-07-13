package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.PositionTeleporter;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GateTileEntity extends TileEntity
{
	
	@OnlyIn(Dist.CLIENT)
	public int colorIndex;
	
	public int gateCount;
	
	public GateTileEntity()
	{
		super(MinestuckTiles.GATE);
	}
	
	public void teleportEntity(World world, ServerPlayerEntity player, Block block)
	{
		if(block == MinestuckBlocks.RETURN_NODE)
		{
			BlockPos pos = world.getSpawnPoint();
			PositionTeleporter.moveEntity(player, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			player.timeUntilPortal = player.getPortalCooldown();
			player.setMotion(Vec3d.ZERO);
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
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		if(compound.contains("gateCount"))
			this.gateCount = compound.getInt("gateCount");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		if(this.gateCount != 0)
			compound.putInt("gateCount", gateCount);
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putInt("color", SburbHandler.getColorForDimension(world));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("color", SburbHandler.getColorForDimension(world));
		return new SUpdateTileEntityPacket(this.pos, 0, nbt);
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		this.colorIndex = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
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