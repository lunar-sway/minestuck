package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEndGrass extends Block
{
	protected BlockEndGrass()
	{
		super(Material.ROCK, MapColor.PURPLE);
		this.setUnlocalizedName("endGrass");
		this.setDefaultState(this.blockState.getBaseState());
		this.setTickRandomly(true);
		setHarvestLevel("pickaxe", 0);
		setHardness(3.0F);
		setResistance(15.0F);	//Values used for end stone
		this.setCreativeTab(TabMinestuck.instance);
	}

	/**
	 * Get the actual Block state of this Block at the given position. This applies properties not visible in the
	 * metadata, such as fence connections.
	 */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state;
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2)
			{
				worldIn.setBlockState(pos, Blocks.END_STONE.getDefaultState());
			}
			else
			{
				if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(3) - 1, rand.nextInt(5) - 3);	//End grass grows faster north-ways because magnets
						IBlockState iblockstate = worldIn.getBlockState(blockpos);
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos.up());

						if (iblockstate.getBlock() == Blocks.END_STONE && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate1.getLightOpacity(worldIn, blockpos.up()) <= 2)
						{
							worldIn.setBlockState(blockpos, this.getDefaultState());
						}
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		super.randomDisplayTick(stateIn, worldIn, pos, rand);

		if (rand.nextInt(10) == 0)
		{
			worldIn.spawnParticle(EnumParticleTypes.PORTAL, (double)((float)pos.getX() + rand.nextFloat()), (double)((float)pos.getY() + 1.1F), (double)((float)pos.getZ() + rand.nextFloat()), 0.0D, 0.0D, 0.0D);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Blocks.END_STONE.getItemDropped(Blocks.END_STONE.getDefaultState(), rand, fortune);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {});
	}
}
