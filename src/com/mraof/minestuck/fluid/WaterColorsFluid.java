package com.mraof.minestuck.fluid;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class WaterColorsFluid extends FlowingFluid
{
	//Textures are not loaded right now, but that should be solved with https://github.com/MinecraftForge/MinecraftForge/pull/6110
	private static final ResourceLocation STILL_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/water_colors_still");
	private static final ResourceLocation FLOWING_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "block/water_colors_flowing");
	
	@Override
	public Fluid getFlowingFluid()
	{
		return MSFluids.FLOWING_WATER_COLORS;
	}
	
	@Override
	public Fluid getStillFluid()
	{
		return MSFluids.WATER_COLORS;
	}
	
	@Override
	protected boolean canSourcesMultiply()
	{
		return true;
	}
	
	@Override
	protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state)
	{
		TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
		Block.spawnDrops(state, worldIn.getWorld(), pos, tileentity);
	}
	
	@Override
	protected int getSlopeFindDistance(IWorldReader worldIn)
	{
		return 4;
	}
	
	@Override
	protected int getLevelDecreasePerBlock(IWorldReader worldIn)
	{
		return 1;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public Item getFilledBucket()
	{
		return MSItems.WATER_COLORS_BUCKET;
	}
	
	@Override
	protected boolean canDisplace(IFluidState state, IBlockReader worldIn, BlockPos pos, Fluid fluid, Direction direction)
	{
		return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
	}
	
	@Override
	public int getTickRate(IWorldReader worldIn)
	{
		return 5;
	}
	
	@Override
	protected float getExplosionResistance()
	{
		return 100F;
	}
	
	@Override
	protected BlockState getBlockState(IFluidState state)
	{
		return MSBlocks.WATER_COLORS.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
	}
	
	@Override
	public boolean isEquivalentTo(Fluid otherFluid)
	{
		return otherFluid == MSFluids.FLOWING_WATER_COLORS || otherFluid == MSFluids.WATER_COLORS;
	}
	
	@Override
	protected FluidAttributes createAttributes()
	{
		return FluidAttributes.builder(STILL_TEXTURE, FLOWING_TEXTURE).build(this);
	}
	
	public static class Flowing extends WaterColorsFluid
	{
		public Flowing()
		{
			setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
		}
		
		@Override
		protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
		{
			super.fillStateContainer(builder);
			builder.add(LEVEL_1_8);
		}
		
		@Override
		public boolean isSource(IFluidState state)
		{
			return false;
		}
		
		@Override
		public int getLevel(IFluidState state)
		{
			return state.get(LEVEL_1_8);
		}
	}
	
	public static class Source extends WaterColorsFluid
	{
		@Override
		public boolean isSource(IFluidState state)
		{
			return true;
		}
		
		@Override
		public int getLevel(IFluidState state)
		{
			return 8;
		}
	}
}