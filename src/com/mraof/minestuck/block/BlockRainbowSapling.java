package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.world.gen.feature.RainbowTree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.trees.AbstractTree;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockRainbowSapling extends BlockBush implements IGrowable
{
	public static final BooleanProperty RED = MinestuckProperties.RED;
	public static final BooleanProperty GREEN = MinestuckProperties.GREEN;
	public static final BooleanProperty BLUE = MinestuckProperties.BLUE;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	protected BlockRainbowSapling(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(RED, false).with(GREEN, false).with(BLUE, false));
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return SHAPE;
	}
	
	@Override
	public void tick(IBlockState state, World worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLight(pos.up()) >= 9 && random.nextInt(3) == 0)
			this.grow(worldIn, random, pos, state);
	}
	
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}
	
	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		if(state.get(RED) && state.get(GREEN) && state.get(BLUE))
		{
			generateTree(worldIn, pos, state, rand);
		} else
		{
			float f = rand.nextFloat();
			BooleanProperty property;
			if(f < 1/3F)
				property = RED;
			else if(f < 2/3F)
				property = GREEN;
			else property = BLUE;
			if(!state.get(property))
				worldIn.setBlockState(pos, state.with(property, true), 2);
		}
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
	{
		builder.add(RED, GREEN, BLUE);
	}
	
	@Override
	public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		if(soil.getBlock() == Blocks.GRASS)
		{
			state = state.with(GREEN, true);
		} else if(soil.getBlock() == Blocks.BLUE_WOOL)
		{
			state = state.with(BLUE, true);
		} else if(soil.getBlock() == Blocks.CYAN_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(GREEN, true);
			else state = state.with(BLUE, true);
		} else if(soil.getBlock() == Blocks.GREEN_WOOL)
		{
			state = state.with(GREEN, true);
		} else if(soil.getBlock() == Blocks.LIGHT_BLUE_WOOL)
		{
			state = state.with(BLUE, true);
		} else if(soil.getBlock() == Blocks.LIME_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.25)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else if(soil.getBlock() == Blocks.MAGENTA_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.5)
			state = state.with(RED, true);
		else state = state.with(BLUE, true);
		} else if(soil.getBlock() == Blocks.PURPLE_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.25)
				state = state.with(RED, true);
			else state = state.with(BLUE, true);
		} else if(soil.getBlock() == Blocks.ORANGE_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.75)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else if(soil.getBlock() == Blocks.PINK_WOOL)
		{
			state = state.with(RED, true);
		} else if(soil.getBlock() == Blocks.RED_WOOL)
		{
			state = state.with(RED, true);
		} else if(soil.getBlock() == Blocks.YELLOW_WOOL)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		}
		worldIn.setBlockState(pos, state);
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() == Items.LAPIS_LAZULI)
		{
			state = state.with(BLUE, true);
		} else if(stack.getItem() == Items.CYAN_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(GREEN, true);
			else state = state.with(BLUE, true);
		} else if(stack.getItem() == Items.CACTUS_GREEN)
		{
			state = state.with(GREEN, true);
		} else if(stack.getItem() == Items.LIGHT_BLUE_DYE)
		{
			state = state.with(BLUE, true);
		} else if(stack.getItem() == Items.LIME_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.25)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else if(stack.getItem() == Items.MAGENTA_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(RED, true);
			else state = state.with(BLUE, true);
		} else if(stack.getItem() == Items.PURPLE_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.25)
				state = state.with(RED, true);
			else state = state.with(BLUE, true);
		} else if(stack.getItem() == Items.ORANGE_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.75)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else if(stack.getItem() == Items.PINK_DYE)
		{
			state = state.with(RED, true);
		} else if(stack.getItem() == Items.ROSE_RED)
		{
			state = state.with(RED, true);
		} else if(stack.getItem() == Items.DANDELION_YELLOW)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else return false;
		
		if(!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, state);
			stack.shrink(1);
		}
		
		return true;
	}
	
	@Override
	protected boolean isValidGround(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return BlockTags.WOOL.contains(state.getBlock()) || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND;
	}
	
	private static void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
			return;
		//AbstractTree tree = new RainbowTree(true);
		//tree.spawn(worldIn, pos, state, rand);
	}
}