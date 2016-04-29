package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.BlockTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPrimedTNT extends BlockTNT
{
	
	public BlockPrimedTNT()
	{
		super();
		setCreativeTab(Minestuck.tabMinestuck);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("primedTnt");
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
	{
		this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.valueOf(true)), playerIn);
		worldIn.setBlockToAir(pos);
	}
}