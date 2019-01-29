package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.world.gen.feature.WorldGenEndTree;
import com.mraof.minestuck.world.gen.feature.WorldGenRainbowTree;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockEndSapling extends BlockBush implements IGrowable
{
	public static final PropertyBool ALPHA = PropertyBool.create("alpha");
	public static final PropertyBool OMEGA = PropertyBool.create("omega");
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	protected BlockEndSapling()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(ALPHA, false).withProperty(OMEGA, false));
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("endSapling");
		this.setSoundType(SoundType.PLANT);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SAPLING_AABB;
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return worldIn.getMoonPhase()!=4;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}
	
	/**
	 * Randomly selects one of the two internal booleans, alpha and omega, and toggles it.
	 * If Alpha is true and omega is false, then the tree will generate.
	 */
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if(worldIn.isRemote || !canGrow(worldIn, pos, state, false))
		{
			return;
		}
		boolean alpha = state.getValue(ALPHA);
		boolean omega = state.getValue(OMEGA);
		
		if(rand.nextFloat() < 0.5)
		{
			alpha = !alpha;
			state = state.withProperty(ALPHA, alpha);
		} else
		{
			omega = !omega;
			state = state.withProperty(OMEGA, omega);
		}
		
		if(alpha && !omega)
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			worldIn.setBlockState(pos, state);
		}
	}
	
	private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.isRemote || !net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
		WorldGenerator worldgenerator= new WorldGenEndTree(true);
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if (!worldgenerator.generate(worldIn, rand, pos))
		{
			worldIn.setBlockState(pos, state, 4);
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		items.add(new ItemStack(this, 1, 0));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ALPHA, meta%2==1).withProperty(OMEGA, meta>1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(ALPHA)? 1:0) + (state.getValue(OMEGA) ? 2:0);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {ALPHA, OMEGA});
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		boolean out = super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this); 
		if(!out && (soil.getBlock()==Blocks.END_STONE || soil.getBlock()==MinestuckBlocks.coarseEndStone || soil.getBlock()==MinestuckBlocks.endGrass))
			out = true;
		return out;
	}
	
	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.END_STONE || state.getBlock() == MinestuckBlocks.coarseEndStone || state.getBlock()==MinestuckBlocks.endGrass;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			super.updateTick(worldIn, pos, state, rand);
			
			if (canGrow(worldIn, pos, state, false) && rand.nextInt(7) == 0)	//The world is not remote, therefore the side is not client.
			{
				this.grow(worldIn, rand, pos, state);
			}
		}
	}
	
}
