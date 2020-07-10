package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

public class FlowingModFluidBlock extends FlowingFluidBlock
{
	protected final Vec3d fogColor;
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties)
	{
		this(fluid, null, properties);
	}
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Vec3d fogColor, Properties properties)
	{
		super(fluid, properties);
		this.fogColor = fogColor;
	}
	
	@Override
	public Vec3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		return fogColor != null ? fogColor : super.getFogColor(state, world, pos, entity, originalColor, partialTicks);
	}
}