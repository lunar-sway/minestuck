package com.mraof.minestuck.item.block;

import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FrogBlockItem extends BlockItem
{
	public FrogBlockItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Nullable
	public static Entity createFrog(World worldIn, double x, double y, double z, int type)
	{
		FrogEntity frog = null;
		
		frog = new FrogEntity(worldIn);
		frog.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
		frog.rotationYawHead = frog.rotationYaw;
		frog.renderYawOffset = frog.rotationYaw;
		frog.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(new BlockPos(x,y,z)), null, null, null);
		
		frog.playAmbientSound();
		
		return frog;
	}
}