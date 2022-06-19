package com.mraof.minestuck.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class BlockHitResultUtil
{
	/**
	 * Based on the Item class function of the same name, without the FluidMode context
	 */
	public static BlockRayTraceResult getPlayerPOVHitResult(World world, PlayerEntity playerEntity)
	{
		float xRot = playerEntity.xRot;
		float yRot = playerEntity.yRot;
		Vector3d eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = MathHelper.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = MathHelper.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vector3d endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return world.clip(new RayTraceContext(eyeVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, playerEntity));
	}
	
	public static BlockState collidedBlockState(World world, PlayerEntity playerEntity)
	{
		BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, playerEntity);
		
		return collidedBlockState(playerEntity, rayTraceResult);
	}
	
	public static BlockState collidedBlockState(PlayerEntity playerEntity, BlockRayTraceResult rayTraceResult)
	{
		return playerEntity.level.getBlockState(rayTraceResult.getBlockPos());
	}
}
