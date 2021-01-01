package com.mraof.minestuck.block;

import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.util.CustomVoxelShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class FrogEggsBlock extends DecorBlock
{
	public FrogEggsBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
	{
		if(random.nextDouble() < 0.1)
		{
			worldIn.removeBlock(pos, false);
			Entity entity =  createFrog(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 0);
			worldIn.addEntity(entity);
		}
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