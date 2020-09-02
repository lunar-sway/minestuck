package com.mraof.minestuck.block;

import com.mraof.minestuck.world.gen.feature.tree.RainbowTree;
import net.minecraft.block.*;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class RainbowSaplingBlock extends BushBlock implements IGrowable
{
	public static final BooleanProperty RED = MSProperties.RED;
	public static final BooleanProperty GREEN = MSProperties.GREEN;
	public static final BooleanProperty BLUE = MSProperties.BLUE;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	private final Tree tree = new RainbowTree();
	
	protected RainbowSaplingBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(RED, false).with(GREEN, false).with(BLUE, false));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		super.tick(state, worldIn, pos, random);
		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLight(pos.up()) >= 9 && random.nextInt(3) == 0)
			this.grow(worldIn, random, pos, state);
	}
	
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return (double)worldIn.rand.nextFloat() < 0.45D;
	}
	
	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
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
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(RED, GREEN, BLUE);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		BlockState soil = worldIn.getBlockState(pos.down());
		if(soil.getBlock() == Blocks.GRASS_BLOCK)
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
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
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
		} else if(stack.getItem() == Items.GREEN_DYE)
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
		} else if(stack.getItem() == Items.RED_DYE)
		{
			state = state.with(RED, true);
		} else if(stack.getItem() == Items.YELLOW_DYE)
		{
			if(worldIn.rand.nextFloat() < 0.5)
				state = state.with(RED, true);
			else state = state.with(GREEN, true);
		} else return ActionResultType.PASS;
		
		if(!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, state);
			stack.shrink(1);
		}
		
		return ActionResultType.SUCCESS;
	}
	
	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return BlockTags.WOOL.contains(state.getBlock()) || super.isValidGround(state, worldIn, pos);
	}
	
	private void generateTree(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand)
	{
		if(!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
			return;
		tree.place(worldIn, worldIn.getChunkProvider().getChunkGenerator(), pos, state, rand);
	}
}