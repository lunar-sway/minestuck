package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.util.MinestuckRandom;
import com.mraof.minestuck.world.gen.feature.WorldGenRainbowTree;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockColored;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BlockRainbowSapling extends BlockBush implements IGrowable
{
	public static final PropertyBool GROWN_SOME = PropertyBool.create("growth");
	public static final PropertyBool RED = PropertyBool.create("red");
	public static final PropertyBool GREEN = PropertyBool.create("green");
	public static final PropertyBool BLUE = PropertyBool.create("blue");
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	protected BlockRainbowSapling()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(GROWN_SOME, false).withProperty(RED, false).withProperty(GREEN, false).withProperty(BLUE, false));
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("rainbowSapling");
		this.setSoundType(SoundType.PLANT);
	}
	
	/*
	 * Overridden from Block
	 */
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SAPLING_AABB;
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
		return this.getDefaultState().withProperty(GROWN_SOME, meta%2==1).withProperty(RED, meta>7).withProperty(GREEN, meta%8 > 3).withProperty(BLUE, meta%4>1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(GROWN_SOME)? 1:0) + (state.getValue(RED) ? 8:0) + (state.getValue(GREEN) ? 4:0) + (state.getValue(BLUE) ? 2:0);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {GROWN_SOME, RED, GREEN, BLUE});
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		if(soil.getBlock()==Blocks.GRASS && !state.getValue(GREEN))
		{
			state.cycleProperty(GREEN);
		} else if(soil.getBlock()==Blocks.WOOL)
		{
			Random rand = MinestuckRandom.getRandom();
			//A really long, drawn out way of saying that the sapling gets one growth flag set to true based on the color of the wool.
			//Black and brown don't grant this head-start, and gray has a chance of working proportional to the amount of white in it.
			switch(soil.getValue(BlockColored.COLOR))
			{
			case BLUE:
				state = state.withProperty(BLUE, true);
				break;
			case CYAN:
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GREEN, true);
				else
					state = state.withProperty(BLUE, true);
				break;
			case GRAY:
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case GREEN:
				state = state.withProperty(GREEN, true);
				break;
			case LIGHT_BLUE:
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				else
					state = state.withProperty(BLUE, true);
				break;
			case LIME:
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(RED, true);
				else
					state = state.withProperty(GREEN, true);
				break;
			case MAGENTA:
				if(rand.nextFloat() < 0.5)
				{
					if(rand.nextFloat() < 0.5)
						state = state.withProperty(GROWN_SOME, true);
					else
						state = state.withProperty(RED, true);
					break;
				}
				//Deliberate fall-through
			case PURPLE:
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(RED, true);
				else
					state = state.withProperty(BLUE, true);
				break;
			case ORANGE:
				if(rand.nextFloat() < 0.75)
					state = state.withProperty(RED, true);
				else
					state = state.withProperty(GREEN, true);
				break;
			case PINK:
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				else
					state = state.withProperty(RED, true);
				break;
			case RED:
				state = state.withProperty(RED, true);
				break;
			case SILVER:
				if(rand.nextFloat() < 0.75)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case WHITE:
				state = state.withProperty(GROWN_SOME, true);
				break;
			case YELLOW:
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(RED, true);
				else
					state = state.withProperty(GREEN, true);
				break;
			default:
				break;
			}
			worldIn.setBlockState(pos, state);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if(stack.getItem()==Items.DYE)
		{
			if(worldIn.isRemote)
			{
				return true;
			}
			
			Random rand = MinestuckRandom.getRandom();
			if(getMetaFromState(state)==15)
			{
				generateTree(worldIn, pos, state, rand);
				stack.shrink(1);
				return true;
			}
			switch(stack.getMetadata())
			{
			case 0:	//black
				break;
			case 1:	//red
				state = state.withProperty(RED, true);
				break;
			case 2:	//green
				state = state.withProperty(GREEN, true);
				break;
			case 3:	//brown
				break;
			case 4:	//blue
				state = state.withProperty(BLUE, true);
				break;
			case 5:	//purple
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(RED, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(BLUE, true);
				break;
			case 6:	//cyan
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(BLUE, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GREEN, true);
				break;
			case 7: //silver
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case 8: //gray
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case 9: //pink
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(RED, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case 10: //lime
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GREEN, true);
				break;
			case 11: //yellow
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(RED, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GREEN, true);
				break;
			case 12: //light blue
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(BLUE, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case 13: //magenta
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(BLUE, true);
				if(rand.nextFloat() < 0.5)
					state = state.withProperty(RED, true);
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(GROWN_SOME, true);
				break;
			case 14: //orange
				if(rand.nextFloat() < 0.75)
					state = state.withProperty(RED, true);
				if(rand.nextFloat() < 0.25)
					state = state.withProperty(GREEN, true);
				break;
			case 15: //white
				if(!state.getValue(GROWN_SOME))
					state = state.withProperty(GROWN_SOME, true);
				else
				{
					if(rand.nextFloat() < 0.25)
						state = state.withProperty(RED, true);
					if(rand.nextFloat() < 0.25)
						state = state.withProperty(GREEN, true);
					if(rand.nextFloat() < 0.25)
						state = state.withProperty(BLUE, true);
				}
				break;
			default:
				break;
			}
			worldIn.setBlockState(pos, state);
			stack.shrink(1);
			return true;
		}
		return false;
	}
	
	/*
	 * Overridden from IGrowable
	 */
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return false;
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if(worldIn.isRemote)
		{
			return;
		}
		int oldMeta = getMetaFromState(state);
		if(oldMeta==15)
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			boolean growthAchieved = false;
			while(!growthAchieved)
			{
				int i = 0;
				switch(rand.nextInt(4-i))
				{
				case 0:
					if(oldMeta%2 < 1)
					{
						state = state.withProperty(GROWN_SOME, true);
						growthAchieved=true;
						break;
					}
					//Deliberate fall-through
				case 1:
					if(oldMeta < 8)
					{
						state = state.withProperty(RED, true);
						growthAchieved=true;
						break;
					}
					//deliberate fall-through
				case 2:
					if(oldMeta%8 < 4)
					{
						state = state.withProperty(GREEN, true);
						growthAchieved=true;
						break;
					}
				case 3:
					if(oldMeta%4 < 2)
					{
						state = state.withProperty(BLUE, true);
						growthAchieved=true;
					}
					break;
				}
				i++;
			}
			worldIn.setBlockState(pos, state);
		}
	}
	
	/*
	 * Overrides from BlockBush
	 */
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		boolean out = super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this); 
		if(!out && soil.getBlock()==Blocks.WOOL)
			out = true;
		return out;
	}
	
	@Override
	protected boolean canSustainBush(IBlockState state)
	{
		return state.getBlock() == Blocks.WOOL || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			super.updateTick(worldIn, pos, state, rand);

			if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
			{
				this.grow(worldIn, rand, pos, state);
			}
		}
	}
	
	/*
	 * Private
	 */
	
	private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.isRemote || !net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
		WorldGenerator worldgenerator= new WorldGenRainbowTree(true);
		
		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
		
		if (!worldgenerator.generate(worldIn, rand, pos))
		{
			worldIn.setBlockState(pos, state, 4);
		}
	}
}