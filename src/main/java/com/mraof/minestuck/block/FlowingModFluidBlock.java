package com.mraof.minestuck.block;

import com.mraof.minestuck.fluid.IMSFog;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

/**
 * This class allows for implementing most fluid blocks in the game
 */
public class FlowingModFluidBlock extends FlowingFluidBlock implements IMSFog
{
	protected Vec3d fogColor;
	protected float fogDensity;
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Properties properties)
	{
		this(fluid, null, 0, properties);
	}
	
	public FlowingModFluidBlock(Supplier<? extends FlowingFluid> fluid, Vec3d fogColor, float fogDensity, Properties properties)
	{
		super(fluid, properties);
		this.fogColor = fogColor;
		this.fogDensity = fogDensity;
	}
	
	@Override
	public Vec3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		return fogColor != null ? fogColor : super.getFogColor(state, world, pos, entity, originalColor, partialTicks);
	}
	
	@Override
	public float getMSFogDensity()
	{
		return fogDensity;
	}
	
	@Override
	public Vec3d getMSFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		return fogColor;
	}
}