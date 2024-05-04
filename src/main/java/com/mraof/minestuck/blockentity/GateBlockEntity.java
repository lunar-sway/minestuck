package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GateBlockEntity extends OnCollisionTeleporterBlockEntity<ServerPlayer>
{
	//Only used client-side
	public int color;
	
	public GateHandler.Type gateType;
	
	public GateBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.GATE.get(), pos, state, ServerPlayer.class);
	}
	
	@Override
	protected AABB getTeleportField()
	{
		return new AABB(worldPosition.getX(), worldPosition.getY() + 7D / 16, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 9D / 16, worldPosition.getZ() + 1);
	}
	
	@Override
	protected void teleport(ServerPlayer player)
	{
		if(level instanceof ServerLevel serverLevel)
			GateHandler.teleport(gateType, serverLevel, player);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		if(nbt.contains("gate_type"))
			this.gateType = GateHandler.Type.fromString(nbt.getString("gate_type"));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		if(this.gateType != null)
			compound.putString("gate_type", gateType.toString());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = super.getUpdateTag();
		nbt.putInt("color", ColorHandler.getColorForDimension((ServerLevel) level));
		return nbt;
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag)
	{
		this.color = tag.getInt("color");
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		handleUpdateTag(pkt.getTag());
	}
	
}