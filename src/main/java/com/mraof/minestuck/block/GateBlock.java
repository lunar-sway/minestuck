package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.tileentity.OnCollisionTeleporterTileEntity;
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

public class GateBlock extends AbstractGateBlock implements EntityBlock
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public GateBlock(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return state.getValue(MAIN) ? new GateTileEntity(pos, state) : null;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> placedType)
	{
		return !level.isClientSide ? BlockUtil.checkTypeForTicker(placedType, MSTileEntityTypes.GATE.get(), OnCollisionTeleporterTileEntity::serverTick) : null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn)
	{
		if(state.getValue(MAIN) && entityIn instanceof ServerPlayer player)
		{
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity instanceof GateTileEntity gate)
				gate.onCollision(player);
		}
	}
	
	@Override
	protected boolean isValid(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.getBlock() != this || block.getValue(MAIN))
						return false;
				}
		
		return true;
	}
	
	@Nullable
	@Override
	protected BlockPos findMainComponent(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					BlockState block = level.getBlockState(pos.offset(x, 0, z));
					if(block.is(this) && block.getValue(MAIN))
						return pos.offset(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	protected void removePortal(BlockPos pos, Level level)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(level.getBlockState(pos.offset(x, 0, z)).is(this))
					level.removeBlock(pos.offset(x, 0, z), false);
	}
	
	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion)
	{
		if(MinestuckConfig.SERVER.canBreakGates.get())
			return super.getExplosionResistance(state, level, pos, explosion);
		else return 3600000.0F;
	}
	
	public static void placeGate(CommonLevelAccessor level, BlockPos centerPos, GateHandler.Type type, int blockFlag)
	{
		for(int offsetX = -1; offsetX <= 1; offsetX++)
		{
			for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
			{
				if(offsetX == 0 && offsetZ == 0)
				{
					level.setBlock(centerPos, MSBlocks.GATE.get().defaultBlockState().setValue(GateBlock.MAIN, true), blockFlag);
					BlockEntity tileEntity = level.getBlockEntity(centerPos);
					if(tileEntity instanceof GateTileEntity gate)
						gate.gateType = type;
					else
						LOGGER.error("Expected a gate tile entity after placing a gate block, but got {}!", tileEntity);
				} else
					level.setBlock(centerPos.offset(offsetX, 0, offsetZ), MSBlocks.GATE.get().defaultBlockState(), blockFlag);
			}
		}
	}
}