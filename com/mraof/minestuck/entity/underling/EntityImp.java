package com.mraof.minestuck.entity.underling;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.mraof.minestuck.entity.ai.EntityAIAttackOnCollideWithRate;
import com.mraof.minestuck.entity.item.EntityGrist;

public class EntityImp extends EntityUnderling
{
	//Some of this class will belong in EntityUnderling once I create that
	public static enum Type implements IType
	{
		SHALE("Shale", 0),
		MERCURY("Mercury", 1),
		COBALT("Cobalt", 2),
		AMBER("Amber", 0),
		RUST("Rust", 1),
		CHALK("Chalk", 0),
		MARBLE("Marble", 1),
		QUARTZ("Quartz", 1),
		URANIUM("Uranium", 5);

		final String typeString;
		final int strength;
		Type(String typeString, int strength)
		{
			this.typeString = typeString;
			this.strength = strength;
		}
		@Override
		public Type getTypeFromString(String string) 
		{
			for(Type current : this.values())
				if(current.typeString.equals(string))
					return current;
			return null;
		}
		@Override
		public String getTypeString() 
		{
			return typeString;
		}
		@Override
		public int getStrength() 
		{
			return strength;
		}
	}
	private EntityAIAttackOnCollideWithRate entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);

	public EntityImp(World world) 
	{
		this(world, Type.values()[randStatic.nextInt(Type.values().length)]);
	}
	public EntityImp(World world, Type type) 
	{
		super(world, type, "Imp");
		setSize(0.5F, 1.0F);
		this.experienceValue = 3 * type.strength + 1;
		this.maxHealth = 5 * (type.strength + 1);
		this.health = this.maxHealth;
	}


	@Override
	public void onLivingUpdate() 
	{
		if(Type.URANIUM == this.type && this.rand.nextDouble() < .0001)
		{
			this.motionX += rand.nextInt(33) - 16;
			this.motionZ += rand.nextInt(33) - 16;
		}
		super.onLivingUpdate();
	}
	@Override
	protected void onDeathUpdate() 
	{
		super.onDeathUpdate();
		if(this.deathTime == 20 && !this.worldObj.isRemote)
		{
			//For spawning all grist in debugging
			//			for(String gristType : EntityGrist.gristTypes)
			//				for(int i = 0; i < rand.nextInt(5) + 1; i++)
			//				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * 10 - 5, this.posY, this.posZ + this.rand.nextDouble() * 10 - 5, gristType, rand.nextInt(32) * rand.nextInt(32) + 1));
			for(String gristType : this.getGristSpoils())
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, gristType, rand.nextInt(4 + type.getStrength()) + 2));
		}
	}

	@Override
	public String[] getGristSpoils()
	{
		switch((Type)this.type)
		{
		case SHALE:
			return new String[] {"Build", "Shale", "Tar"};
		case MERCURY:
			return new String[] {"Build", "Mercury"};
		case COBALT:
			return new String[] {"Build", "Cobalt"};
		case AMBER:
			return new String[] {"Build", "Amber"};
		case RUST:
			return new String[] {"Build", "Rust"};
		case CHALK:
			return new String[] {"Build", "Chalk"};
		case MARBLE:
			return new String[] {"Build", "Marble"};
		case QUARTZ:
			return new String[] {"Quartz"};
		case URANIUM:
			return new String[] {"Uranium"};
		default:
			return new String[] {"Build"};
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) 
	{
		boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int) Math.ceil((double)(this.type.getStrength() + 1) / 2));
		return flag;
	}
	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackOnCollideWithRate == null)
			entityAIAttackOnCollideWithRate = new EntityAIAttackOnCollideWithRate(this, .4F, 20, false);
		this.tasks.removeTask(this.entityAIAttackOnCollideWithRate);
		this.tasks.addTask(4, entityAIAttackOnCollideWithRate);
	}
	@Override
	protected float getWanderSpeed() 
	{
		return .3F;
	}

}
