package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.OnCollisionTeleporterBlockEntity;
import com.mraof.minestuck.blockentity.ReturnNodeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;

public class ReturnNodeBlock extends AbstractGateBlock
{
	public ReturnNodeBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
	{
		if(entityIn instanceof ServerPlayer player)
		{
			BlockPos mainPos = this.findMainComponent(pos, level);
			
			if(mainPos != null)
			{
				BlockEntity blockEntity = level.getBlockEntity(mainPos);
				if(blockEntity instanceof ReturnNodeBlockEntity gate)
					gate.onCollision(player);
			} else
				level.removeBlock(pos, false);
		}
	}
	
	@Override
	protected final boolean isValid(BlockPos mainPos, Level level)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(x != 0 || z != 0)
				{
					BlockState state = level.getBlockState(mainPos.offset(x, 0, z));
					if(!state.is(MSBlocks.RETURN_NODE.get()))
						return false;
				}
		
		return true;
	}
	
	@Nullable
	@Override
	protected BlockPos findMainComponent(BlockPos pos, Level level)
	{
		for(int x = 0; x <= 1; x++)
			for(int z = 0; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.is(MSBlocks.RETURN_NODE_MAIN.get()))
						return pos.offset(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected final void removePortal(BlockPos mainPos, Level level)
	{
		for(int x = -1; x <= 0; x++)
			for(int z = -1; z <= 0; z++)
				if(level.getBlockState(mainPos.offset(x, 0, z)).getBlock() == this)
					level.removeBlock(mainPos.offset(x, 0, z), false);
	}
	
	public static class Main extends ReturnNodeBlock implements EntityBlock
	{
		public Main(Properties properties)
		{
			super(properties);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new ReturnNodeBlockEntity(pos, state);
		}
		
		@Nullable
		@Override
		public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
		{
			return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.RETURN_NODE.get(), OnCollisionTeleporterBlockEntity::serverTick) : null;
		}
		
		@Override
		protected BlockPos findMainComponent(BlockPos pos, Level level)
		{
			return pos;
		}
	}
	
	public static void placeReturnNode(LevelAccessor level, BlockPos nodePos, @Nullable BoundingBox boundingBox)
	{
		for(int i = 0; i < 4; i++)
		{
			BlockPos pos = nodePos.offset(i % 2, 0, i/2);
			if(boundingBox == null || boundingBox.isInside(pos))
			{
				if(i == 3)
					level.setBlock(pos, MSBlocks.RETURN_NODE_MAIN.get().defaultBlockState(), Block.UPDATE_CLIENTS);
				else level.setBlock(pos, MSBlocks.RETURN_NODE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
			}
		}
	}
}