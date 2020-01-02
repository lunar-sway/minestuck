package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;

import java.util.function.Supplier;

public class FlowingWaterColorsBlock extends FlowingFluidBlock
{
	public FlowingWaterColorsBlock(Supplier<FlowingFluid> fluid, Properties properties)
	{
		super(fluid, properties);
	}
	
	@Override
	public Vec3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor, float partialTicks)
	{
		Vec3d newColor = new Vec3d(0.0, 20.0, 30.0);
		newColor = newColor.rotateYaw((float) (entity.posX / 2.0));
		newColor = newColor.rotatePitch((float) (entity.posZ / 2.0));
		newColor = newColor.rotateYaw((float) (entity.posY));
		newColor = newColor.normalize();
		newColor = new Vec3d(newColor.x % 1.0, newColor.y % 1.0, newColor.z % 1.0);
		
		return newColor;
	}
}