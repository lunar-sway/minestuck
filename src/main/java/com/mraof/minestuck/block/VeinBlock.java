package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class VeinBlock extends DirectionalBlock	//TODO duplicate code between this and VeinCornerBlock. Do something about that
{

	protected VeinBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public void playerDestroy(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
		super.playerDestroy(world, player, pos, state, tileEntity, stack);
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			if (world.dimensionType().ultraWarm())
			{
				world.removeBlock(pos, false);
				return;
			}
			
			Material mater = world.getBlockState(pos.below()).getMaterial();
			if (mater.blocksMotion() || mater.isLiquid())
				world.setBlockAndUpdate(pos, MSBlocks.BLOOD.defaultBlockState());
		}
	}
	
	@Override
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
	{
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{/*Does not work as it is written because the vein and vein corner isn't using the same facing type. Fix this if you want to use the vein block in the future.
		BlockState state = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));

	   if(state.getBlock() == MSBlocks.VEIN || state.getBlock() == MSBlocks.VEIN_CORNER)
		{
			Direction direction = state.getValue(FACING);

			if (direction == context.getClickedFace())
				return this.defaultBlockState().setValue(FACING, direction.getOpposite());
		}*/

		return this.defaultBlockState().setValue(FACING, context.getClickedFace());
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}
}