package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;

public class WyrmEntity extends UnderlingEntity implements IEntityMultiPart
{
	public ArrayList<Entity> parts = new ArrayList<>();
	public ArrayList<Integer> partIds = new ArrayList<>();
	
	public WyrmEntity(EntityType<? extends WyrmEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	public World getWorld()
	{
		return this.world;
	}

	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage)
	{
		return false;
	}

	@Override
	public void updatePartPositions()
	{
	}

	@Override
	public void addPart(Entity entityPart, int id)
	{
	}

	@Override
	public void onPartDeath(Entity entityPart, int id)
	{
	}

	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 87);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return 0;
	}
}
