package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.entity.ModEntityTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EntityWyrm extends EntityUnderling implements IEntityMultiPart
{
	public ArrayList<EntityLiving> parts = new ArrayList<EntityLiving>();
	public ArrayList<Integer> partIds = new ArrayList<Integer>();
	public EntityWyrm(World world)
	{
		super(ModEntityTypes.UNDERLING_PART, world);
	}
	
	@Override
	protected String getUnderlingName()
	{
		return "wyrm";
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
		return GristHelper.getRandomDrop(type, 87);
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
	
	@Override
	protected int getVitalityGel()
	{
		return 0;
	}
}
