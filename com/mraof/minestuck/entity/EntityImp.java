package com.mraof.minestuck.entity;

import java.util.Random;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.mraof.minestuck.entity.item.EntityGrist;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityImp extends EntityMob implements IMob, IEntityAdditionalSpawnData
{
	//Some of this class will belong in EntityUnderling once I create that
	public static enum Type
	{
		SHALE("Shale", 0),
		COBALT("Cobalt", 1),
		AMBER("Amber", 1),
		CHALK("Chalk", 1),
		URANIUM("Uranium", 3);
		
		final String typeString;
		final int strength;
		Type(String typeString, int strength)
		{
			this.typeString = typeString;
			this.strength = strength;
		}
		public static Type getTypeFromString(String string) 
		{
			switch(string)
			{
			default:
			case "Shale":
				return SHALE;
			case "Cobalt":
				return COBALT;
			case "Amber":
				return AMBER;
			case "Uranium":
				return URANIUM;
			}
		}
	}
	private static Random randStatic = new Random();
	protected int maxHealth = 1;
	protected Type type;
	public EntityImp(World world) 
	{
		this(world, Type.values()[randStatic.nextInt(Type.values().length)]);
	}
	public EntityImp(World world, Type type) 
	{
		super(world);
		moveSpeed = 2.5F;
		setSize(0.5F, 1.0F);
		this.experienceValue = 3;
		this.type = type;
		this.maxHealth = 5 * (type.strength + 1);
		this.health = this.maxHealth;
		texture = "/mods/Minestuck/textures/mobs/" + type.typeString + "Imp.png";
	}

	@Override
	public int getMaxHealth() 
	{
		return maxHealth;
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
				this.worldObj.spawnEntityInWorld(new EntityGrist(worldObj, this.posX + this.rand.nextDouble() * this.width - this.width / 2, this.posY, this.posZ + this.rand.nextDouble() * this.width - this.width / 2, gristType, rand.nextInt(4 + type.strength) + 2));
		}
	}
	public String[] getGristSpoils()
	{
		switch(this.type)
		{
		default:
		case SHALE:
			return new String[] {"Build", "Shale", "Tar"};
		case COBALT:
			return new String[] {"Build", "Cobalt"};
		case AMBER:
			return new String[] {"Build", "Amber"};
		case CHALK:
			return new String[] {"Build", "Chalk"};
		case URANIUM:
			return new String[] {"Build", "Uranium"};
		}
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setString("Type", this.type.typeString);
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.readEntityFromNBT(par1nbtTagCompound);
		this.type = Type.getTypeFromString(par1nbtTagCompound.getString("Type"));
	}
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) 
	{
		data.writeInt(type.ordinal());
	}
	@Override
	public void readSpawnData(ByteArrayDataInput data) 
	{
		this.type = Type.values()[data.readInt()];
		texture = "/mods/Minestuck/textures/mobs/" + type.typeString + "Imp.png";
	}

}
