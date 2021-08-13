package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.StatStorerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class StatStorerBlock extends Block
{
	public StatStorerBlock(Properties properties)
	{
		super(properties);
	}
	
	/*@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand)
	{
		super.tick(state, worldIn, pos, rand);
		
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity instanceof StatStorerTileEntity && !worldIn.isRemote)
		{
			StatStorerTileEntity statStorerTileEntity = (StatStorerTileEntity) tileentity;
			
			LivingEntity livingEntity;
			livingEntity.;
			
			EnumKeyType tileEntityKeyType = interfaceTileEntity.getKey();
			
			Debug.debugf("DoorBlock activated. itemKeyType = %s, tileEntityKeyType = %s", itemKeyType, tileEntityKeyType);
			
		}
	}*/
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new StatStorerTileEntity();
	}
}