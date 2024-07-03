package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.BlockUtil;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.network.block.BlockTeleporterSettingsPacket;
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockTeleporterBlockEntity extends BlockEntity
{
	@Nonnull
	private BlockPos teleportOffset = new BlockPos(0, 0, 0);
	
	public BlockTeleporterBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.BLOCK_TELEPORTER.get(), pos, state);
	}
	
	public void handleTeleports()
	{
		if(level == null)
			return;
		
		Direction facingDirection = this.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		BlockPos abovePos = this.getBlockPos().above();
		BlockState aboveState = level.getBlockState(abovePos);
		
		if(aboveState.isAir())
			return;
		
		PushReaction pushReaction = aboveState.getPistonPushReaction();
		boolean badPushReaction = pushReaction == PushReaction.BLOCK || pushReaction == PushReaction.IGNORE;
		float defaultDestroyTime = aboveState.getBlock().defaultDestroyTime();
		boolean hardToDestroy = defaultDestroyTime < 0 || defaultDestroyTime > 30;
		
		//TODO consider adding functionality for transferring block entities
		if(badPushReaction || hardToDestroy || aboveState.hasBlockEntity())
			return;
		
		BlockPos offsetMod = teleportOffset.rotate(MSRotationUtil.rotationBetween(Direction.EAST, facingDirection));
		BlockPos destinationPos = new BlockPos(this.getBlockPos().offset(offsetMod));
		
		BlockState destinationState = level.getBlockState(destinationPos);
		if(!BlockUtil.isReplaceable(destinationState))
			return;
		
		level.playSound(null, destinationPos, MSSoundEvents.TRANSPORTALIZER_TELEPORT.get(), SoundSource.BLOCKS, 1, 1);
		level.setBlock(destinationPos, aboveState, Block.UPDATE_ALL);
		
		level.playSound(null, abovePos, MSSoundEvents.TRANSPORTALIZER_TELEPORT.get(), SoundSource.BLOCKS, 1, 1);
		level.removeBlock(abovePos, true); //TODO consider whether isMoving = true is appropriate
	}
	
	@Nonnull
	public BlockPos getTeleportOffset()
	{
		return this.teleportOffset;
	}
	
	public void handleSettingsPacket(BlockTeleporterSettingsPacket packet)
	{
		this.teleportOffset = packet.offsetPos();
		
		setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		int offsetX = compound.getInt("offsetX");
		int offsetY = compound.getInt("offsetY");
		int offsetZ = compound.getInt("offsetZ");
		this.teleportOffset = clampPos(offsetX, offsetY, offsetZ);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("offsetX", teleportOffset.getX());
		compound.putInt("offsetY", teleportOffset.getY());
		compound.putInt("offsetZ", teleportOffset.getZ());
	}
	
	public static BlockPos clampPos(int x, int y, int z)
	{
		return new BlockPos(Mth.clamp(x, -32, 32), Mth.clamp(y, -32, 32), Mth.clamp(z, -32, 32));
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
