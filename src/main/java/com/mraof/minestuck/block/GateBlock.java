package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.blockentity.GateBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.OnCollisionTeleporterBlockEntity;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class GateBlock extends AbstractGateBlock
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GateBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	protected final boolean isValid(BlockPos mainPos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(mainPos.offset(x, 0, z));
					if(!block.is(MSBlocks.GATE.get()))
						return false;
				}
		
		return true;
	}
	
	@Nullable
	protected BlockPos findMainComponent(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.is(MSBlocks.GATE_MAIN.get()))
						return pos.offset(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected final void removePortal(BlockPos mainPos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(level.getBlockState(mainPos.offset(x, 0, z)).is(this))
					level.removeBlock(mainPos.offset(x, 0, z), false);
	}
	
	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
	{
		if(MinestuckConfig.SERVER.canBreakGates.get())
			return super.getExplosionResistance(state, level, pos, explosion);
		else return 3600000.0F;
	}
	
	public static class Main extends GateBlock implements EntityBlock
	{
		public Main(Properties properties)
		{
			super(properties);
		}
		
		@Nullable
		@Override
		public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
		{
			return new GateBlockEntity(pos, state);
		}
		
		@Nullable
		@Override
		public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
		{
			return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSBlockEntityTypes.GATE.get(), OnCollisionTeleporterBlockEntity::serverTick) : null;
		}
		
		@Nullable
		@Override
		protected BlockPos findMainComponent(BlockPos pos, Level level)
		{
			return pos;
		}
		
		@Override
		@SuppressWarnings("deprecation")
		public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
		{
			if(entityIn instanceof ServerPlayer player)
			{
				BlockEntity blockEntity = level.getBlockEntity(pos);
				if(blockEntity instanceof GateBlockEntity gate)
					gate.onCollision(player);
			}
		}
	}
	
	public static void placeGate(CommonLevelAccessor level, BlockPos centerPos, GateHandler.Type type, int blockFlag)
	{
		for(int offsetX = -1; offsetX <= 1; offsetX++)
		{
			for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
			{
				if(offsetX == 0 && offsetZ == 0)
				{
					level.setBlock(centerPos, MSBlocks.GATE_MAIN.get().defaultBlockState(), blockFlag);
					BlockEntity blockEntity = level.getBlockEntity(centerPos);
					if(blockEntity instanceof GateBlockEntity gate)
						gate.gateType = type;
					else
						LOGGER.error("Expected a gate block entity after placing a gate block, but got {}!", blockEntity);
				} else
					level.setBlock(centerPos.offset(offsetX, 0, offsetZ), MSBlocks.GATE.get().defaultBlockState(), blockFlag);
			}
		}
	}
}