package com.mraof.minestuck.entity.underling;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.mraof.minestuck.entity.EntityMinestuck;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityUnderling extends EntityMinestuck implements IEntityAdditionalSpawnData, IMob
{
	//The type of the imp
	protected GristType type;
	//Name of underling, used in getting the texture and actually naming it
	public String underlingName;
	//random used in randomly choosing a type of creature
	protected static Random randStatic = new Random();
	
	public EntityUnderling(World par1World, GristType type, String underlingName) 
	{
		super(par1World, type, underlingName);
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, this.entityAINearestAttackableTargetWithHeight());
		this.tasks.addTask(5, new EntityAIWander(this, this.getWanderSpeed()));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

		if (par1World != null && !par1World.isRemote)
		{
			this.setCombatTask();
		}

	}
	@Override
	protected void setCustomStartingVariables(Object[] objects) 
	{
		this.type = (GristType)objects[0];
		this.underlingName = (String)objects[1];
	}
	//used when getting how much grist should be dropped on death
	public abstract GristSet getGristSpoils();

	protected abstract void setCombatTask();
	
	protected abstract float getMaxHealth();

	protected abstract float getWanderSpeed();
	@Override
	public String getTexture() 
	{
		return "textures/mobs/" + type.getName() + underlingName + ".png";
	}
	//Gives each type of underling a unique name, so instead of all types being called entity.underlingName.name they are called entity.typeString.underlingName.name
	@Override
	public String getEntityName() 
	{
		String s = type.getName() + "." + underlingName;
		return StatCollector.translateToLocal("entity." + s + ".name");
	}
	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight()
	{
		return new EntityAINearestAttackableTargetWithHeight(this, EntityPlayer.class, 128.0F, 2, true, false);
	}
	@Override
	public void moveEntity(double par1, double par3, double par5) 
	{
		super.moveEntity(par1, par3, par5);
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setString("Type", this.type.getName());
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) 
	{
		super.readEntityFromNBT(par1nbtTagCompound);
		this.type = type.getTypeFromString(par1nbtTagCompound.getString("Type"));
	}
	@Override
	public void writeSpawnData(ByteArrayDataOutput data) 
	{
		data.writeInt(((Enum) type).ordinal());
	}
	@Override
	public void readSpawnData(ByteArrayDataInput data) 
	{
		this.type = type.getClass().getEnumConstants()[data.readInt()];
		this.textureResource = new ResourceLocation("minestuck:textures/mobs/" + type.getName() + underlingName + ".png");
	}


}
