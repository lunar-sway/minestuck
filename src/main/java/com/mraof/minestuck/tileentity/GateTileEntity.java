package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

public class GateTileEntity extends OnCollisionTeleporterTileEntity<ServerPlayerEntity>
{
	//Only used client-side
	public int color;
	
	public GateHandler.Type gateType;
	
	public GateTileEntity()
	{
		super(MSTileEntityTypes.GATE.get(), ServerPlayerEntity.class);
	}
	
	@Override
	protected AxisAlignedBB getTeleportField()
	{
		if(getBlockState().getBlock() == MSBlocks.RETURN_NODE)
			return new AxisAlignedBB(worldPosition.getX() - 1, worldPosition.getY() + 7D / 16, worldPosition.getZ() - 1, worldPosition.getX() + 1, worldPosition.getY() + 9D / 16, worldPosition.getZ() + 1);
		else
			return new AxisAlignedBB(worldPosition.getX(), worldPosition.getY() + 7D / 16, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 9D / 16, worldPosition.getZ() + 1);
	}
	
	@Override
	protected void teleport(ServerPlayerEntity player)
	{
		if(getBlockState().getBlock() == MSBlocks.RETURN_NODE)
		{
			if (level instanceof ServerWorld)
			{
				BlockPos pos = level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, ((ServerWorld) level).getSharedSpawnPos());
				
				Teleport.teleportEntity(player, (ServerWorld) level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
				player.setPortalCooldown();
				player.setDeltaMovement(Vector3d.ZERO);
				player.fallDistance = 0;
			}
		} else
		{
			GateHandler.teleport(gateType, (ServerWorld) level, player);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB(this.getBlockPos().offset(-1, 0, -1), this.getBlockPos().offset(1, 1, 1));
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		if(nbt.contains("gate_type"))
			this.gateType = GateHandler.Type.fromString(nbt.getString("gate_type"));
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		if(this.gateType != null)
			compound.putString("gate_type", gateType.toString());
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbt = super.getUpdateTag();
		nbt.putInt("color", ColorHandler.getColorForDimension((ServerWorld) level));
		return nbt;
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.worldPosition, 0, getUpdateTag());
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag)
	{
		this.color = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(getBlockState(), pkt.getTag());
	}
	
	public boolean isGate()
	{
		return this.level != null ? this.level.getBlockState(this.getBlockPos()).getBlock() != MSBlocks.RETURN_NODE : this.gateType != null;
	}
	
	@Override
	public double getViewDistance()
	{
		return isGate() ? 65536.0D : 4096.0D;
	}
	
}