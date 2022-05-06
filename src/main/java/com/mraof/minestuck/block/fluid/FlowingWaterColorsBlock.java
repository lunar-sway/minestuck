package com.mraof.minestuck.block.fluid;

import com.mraof.minestuck.fluid.IMSFog;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

/**
 * specifically for implementing fluid blocks that change fluid fog color on movement
 */
public class FlowingWaterColorsBlock extends FlowingFluidBlock implements IMSFog
{
	private final float fogDensity;
	
	public FlowingWaterColorsBlock(Supplier<FlowingFluid> fluid, Properties properties)
	{
		this(fluid, 0 ,properties);
	}
	
	public FlowingWaterColorsBlock(Supplier<? extends FlowingFluid> fluid, float fogDensity, Properties properties)
	{
		super(fluid, properties);
		this.fogDensity = fogDensity;
	}
	
	@Override
	public Vector3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vector3d originalColor, float partialTicks)
	{
		Vector3d newColor = new Vector3d(0.0, 20.0, 30.0);
		newColor = newColor.yRot((float) (entity.getX() / 2.0));
		newColor = newColor.xRot((float) (entity.getZ() / 2.0));
		newColor = newColor.yRot((float) (entity.getY()));
		newColor = newColor.normalize();
		newColor = new Vector3d(newColor.x % 1.0, newColor.y % 1.0, newColor.z % 1.0);
		
		return newColor;
	}
	
	@Override
	public float getMSFogDensity()
	{
		return fogDensity;
	}
	
	@Override
	public Vector3d getMSFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vector3d originalColor, float partialTicks)
	{
		return this.getFogColor(state, world, pos, entity, originalColor, partialTicks);
	}
}
