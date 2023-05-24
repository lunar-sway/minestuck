package com.mraof.minestuck.block.plant;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.world.gen.feature.tree.RainbowTree;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RainbowSaplingBlock extends BushBlock implements BonemealableBlock
{
	public static final BooleanProperty RED = MSProperties.RED;
	public static final BooleanProperty GREEN = MSProperties.GREEN;
	public static final BooleanProperty BLUE = MSProperties.BLUE;
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	
	private final AbstractTreeGrower tree = new RainbowTree();
	
	public RainbowSaplingBlock(Properties properties)
	{
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(RED, false).setValue(GREEN, false).setValue(BLUE, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		super.tick(state, level, pos, random);
		if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (level.getMaxLocalRawBrightness(pos.above()) >= 9 && random.nextInt(3) == 0)
			this.performBonemeal(level, random, pos, state);
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state)
	{
		return level.random.nextFloat() < 0.45F;
	}
	
	@Override
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state)
	{
		if(state.getValue(RED) && state.getValue(GREEN) && state.getValue(BLUE))
		{
			tree.growTree(level, level.getChunkSource().getGenerator(), pos, state, rand);
		} else
		{
			float f = rand.nextFloat();
			BooleanProperty property;
			if(f < 1/3F)
				property = RED;
			else if(f < 2/3F)
				property = GREEN;
			else property = BLUE;
			if(!state.getValue(property))
				level.setBlock(pos, state.setValue(property, true), Block.UPDATE_CLIENTS);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(RED, GREEN, BLUE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		BlockState soil = level.getBlockState(pos.below());
		if(soil.is(Blocks.GRASS_BLOCK))
		{
			state = state.setValue(GREEN, true);
		} else if(soil.is(Blocks.BLUE_WOOL))
		{
			state = state.setValue(BLUE, true);
		} else if(soil.is(Blocks.CYAN_WOOL))
		{
			if(level.random.nextFloat() < 0.5)
				state = state.setValue(GREEN, true);
			else state = state.setValue(BLUE, true);
		} else if(soil.is(Blocks.GREEN_WOOL))
		{
			state = state.setValue(GREEN, true);
		} else if(soil.is(Blocks.LIGHT_BLUE_WOOL))
		{
			state = state.setValue(BLUE, true);
		} else if(soil.is(Blocks.LIME_WOOL))
		{
			if(level.random.nextFloat() < 0.25)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		} else if(soil.is(Blocks.MAGENTA_WOOL))
		{
			if(level.random.nextFloat() < 0.5)
			state = state.setValue(RED, true);
		else state = state.setValue(BLUE, true);
		} else if(soil.is(Blocks.PURPLE_WOOL))
		{
			if(level.random.nextFloat() < 0.25)
				state = state.setValue(RED, true);
			else state = state.setValue(BLUE, true);
		} else if(soil.is(Blocks.ORANGE_WOOL))
		{
			if(level.random.nextFloat() < 0.75)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		} else if(soil.is(Blocks.PINK_WOOL))
		{
			state = state.setValue(RED, true);
		} else if(soil.is(Blocks.RED_WOOL))
		{
			state = state.setValue(RED, true);
		} else if(soil.is(Blocks.YELLOW_WOOL))
		{
			if(level.random.nextFloat() < 0.5)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		}
		level.setBlockAndUpdate(pos, state);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(stack.is(Items.LAPIS_LAZULI))
		{
			state = state.setValue(BLUE, true);
		} else if(stack.is(Items.CYAN_DYE))
		{
			if(level.random.nextFloat() < 0.5)
				state = state.setValue(GREEN, true);
			else state = state.setValue(BLUE, true);
		} else if(stack.is(Items.GREEN_DYE))
		{
			state = state.setValue(GREEN, true);
		} else if(stack.is(Items.LIGHT_BLUE_DYE))
		{
			state = state.setValue(BLUE, true);
		} else if(stack.is(Items.LIME_DYE))
		{
			if(level.random.nextFloat() < 0.25)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		} else if(stack.is(Items.MAGENTA_DYE))
		{
			if(level.random.nextFloat() < 0.5)
				state = state.setValue(RED, true);
			else state = state.setValue(BLUE, true);
		} else if(stack.is(Items.PURPLE_DYE))
		{
			if(level.random.nextFloat() < 0.25)
				state = state.setValue(RED, true);
			else state = state.setValue(BLUE, true);
		} else if(stack.is(Items.ORANGE_DYE))
		{
			if(level.random.nextFloat() < 0.75)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		} else if(stack.is(Items.PINK_DYE))
		{
			state = state.setValue(RED, true);
		} else if(stack.is(Items.RED_DYE))
		{
			state = state.setValue(RED, true);
		} else if(stack.is(Items.YELLOW_DYE))
		{
			if(level.random.nextFloat() < 0.5)
				state = state.setValue(RED, true);
			else state = state.setValue(GREEN, true);
		} else return InteractionResult.PASS;
		
		if(!level.isClientSide)
		{
			level.setBlockAndUpdate(pos, state);
			stack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos)
	{
		return state.is(BlockTags.WOOL) || super.mayPlaceOn(state, worldIn, pos);
	}
}