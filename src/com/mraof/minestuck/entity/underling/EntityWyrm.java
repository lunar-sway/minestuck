package com.mraof.minestuck.entity.underling;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.util.GristHelper;
import com.mraof.minestuck.util.GristSet;

public class EntityWyrm extends EntityUnderling implements IEntityMultiPart
{
	public ArrayList<EntityLiving> parts = new ArrayList<EntityLiving>();
	public ArrayList<Integer> partIds = new ArrayList<Integer>();
	public EntityWyrm(World world)
	{
		super(world, "Wyrm");
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		super.writeSpawnData(buffer);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		super.readSpawnData(buffer);
	}

	@Override
	public World getWorld()
	{
		return this.worldObj;
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
		return GristHelper.getRandomDrop(type, 87);
	}

	@Override
	protected void setCombatTask()
	{
	}

	@Override
	protected float getMaximumHealth()
	{
		return 20; //TODO make real value
	}

	@Override
	protected float getKnockbackResistance()
	{
		return 0;
	}

	@Override
	protected double getWanderSpeed()
	{
		return 0;
	}
	
	@Override
	protected double getAttackDamage()
	{
		return 0;
	}
}
