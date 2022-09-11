package com.mraof.minestuck.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class BlockHitResultUtil
{
	/**
	 * Based on the Item class function of the same name, without the FluidMode context
	 */
	public static BlockHitResult getPlayerPOVHitResult(Level level, Player playerEntity)
	{
		float xRot = playerEntity.xRotO;
		float yRot = playerEntity.yRotO;
		Vec3 eyeVec = playerEntity.getEyePosition(1.0F);
		float f2 = (float) Math.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = (float) Math.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = (float) -Math.cos(-xRot * ((float) Math.PI / 180F));
		float yComponent = (float) Math.sin(-xRot * ((float) Math.PI / 180F));
		float xComponent = f3 * f4;
		float zComponent = f2 * f4;
		double reachDistance = playerEntity.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		Vec3 endVec = eyeVec.add((double) xComponent * reachDistance, (double) yComponent * reachDistance, (double) zComponent * reachDistance);
		return level.clip(new ClipContext(eyeVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, playerEntity));
	}
	
	public static BlockState collidedBlockState(Level level, Player playerEntity)
	{
		BlockHitResult rayTraceResult = getPlayerPOVHitResult(level, playerEntity);
		
		return collidedBlockState(playerEntity, rayTraceResult);
	}
	
	public static BlockState collidedBlockState(Player playerEntity, BlockHitResult rayTraceResult)
	{
		return playerEntity.level.getBlockState(rayTraceResult.getBlockPos());
	}
}
