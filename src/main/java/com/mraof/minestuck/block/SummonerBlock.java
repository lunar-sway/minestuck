package com.mraof.minestuck.block;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SummonerBlock extends Block
{
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	
	public SummonerBlock(Properties properties)
	{
		super(properties);
		setDefaultState(getDefaultState().with(TRIGGERED, false));
	}
	
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
	{
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		boolean blockPowered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		//Debug.debugf("blockPowered = %s, triggered = %s", blockPowered, state.get(TRIGGERED));
		if(blockPowered && !state.get(TRIGGERED))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			Debug.debugf("summonerTileEntity = %s", tileEntity instanceof SummonerTileEntity);
			if(tileEntity instanceof SummonerTileEntity)
			{
				SummonerTileEntity summonerTE = (SummonerTileEntity) tileEntity;
				
				summonerTE.summonEntity(worldIn, state, pos, true);
			}
			//worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
			worldIn.setBlockState(pos, state.with(TRIGGERED, true), 4);
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new SummonerTileEntity();
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(TRIGGERED);
	}
}
