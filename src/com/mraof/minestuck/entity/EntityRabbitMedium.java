package com.mraof.minestuck.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.mraof.minestuck.entity.EntityFrog.isAreaClearOfEntityType;

public class EntityRabbitMedium extends EntityRabbit
{
	public EntityRabbitMedium(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected boolean canDespawn()
	{
		return true;
	}

	@Override
	public boolean getCanSpawnHere()
	{
		// Gotta override all the way down :/
		IBlockState iblockstate = this.world.getBlockState((new BlockPos(this)).down());
		return iblockstate.canEntitySpawn(this);
	}
}
