package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.tileentity.redstone.PlatformGeneratorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class PlatformGeneratorBlock extends MSDirectionalBlock
{
	
	public PlatformGeneratorBlock(Properties properties)
	{
		super(properties);
		setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));
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
		return new PlatformGeneratorTileEntity();
	}
	
	/*@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isSneaking() && !CreativeShockEffect.doesCreativeShockLimit(player, 1, 4))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof PlatformGeneratorTileEntity)
			{
				PlatformGeneratorTileEntity te = (PlatformGeneratorTileEntity) tileEntity;
			}
		}
		
		return ActionResultType.SUCCESS;
	}*/
}
