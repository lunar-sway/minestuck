package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;

public class GateTileEntity extends OnCollisionTeleporterTileEntity<ServerPlayerEntity>
{
	//Only used client-side
	public int color;
	private boolean hasCollisions = false;
	
	public GateHandler.Type gateType;
	
	public GateTileEntity()
	{
		super(MSTileEntityTypes.GATE, ServerPlayerEntity.class);
	}
	
	@Override
	protected AxisAlignedBB getTeleportField()
	{
		if(getBlockState().getBlock() == MSBlocks.RETURN_NODE)
			return new AxisAlignedBB(pos.getX() - 1, pos.getY() + 7D / 16, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 9D / 16, pos.getZ() + 1);
		else
			return new AxisAlignedBB(pos.getX(), pos.getY() + 7D / 16, pos.getZ(), pos.getX() + 1, pos.getY() + 9D / 16, pos.getZ() + 1);
	}
	
	@Override
	protected void teleport(ServerPlayerEntity player)
	{
		if(getBlockState().getBlock() == MSBlocks.RETURN_NODE)
		{
			BlockPos pos = world.getDimension().findSpawn(0, 0, false);
			if(pos == null)
				return;
			Teleport.teleportEntity(player, (ServerWorld) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			player.timeUntilPortal = player.getPortalCooldown();
			player.setMotion(Vec3d.ZERO);
			player.fallDistance = 0;
		} else
		{
			GateHandler.teleport(gateType, (ServerWorld) world, player);
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
		if(compound.contains("gate_type"))
			this.gateType = GateHandler.Type.fromString(compound.getString("gate_type"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		if(this.gateType != null)
			compound.putString("gate_type", gateType.toString());
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putInt("color", ColorHandler.getColorForDimension((ServerWorld) world));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		this.color = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	public boolean isGate()
	{
		return this.world != null ? this.world.getBlockState(this.getPos()).getBlock() != MSBlocks.RETURN_NODE : this.gateType != null;
	}
	
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return isGate() ? 65536.0D : 4096.0D;
	}
	
}