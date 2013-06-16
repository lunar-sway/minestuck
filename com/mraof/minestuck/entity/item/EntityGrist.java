package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet22Collect;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityGrist extends Entity
{
	/**
	 * A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow)
	 */
	public static final String[] gristTypes = {"Build", "Shale"};
	public int xpColor;

	/** The age of the XP orb in ticks. */
	public int xpOrbAge = 0;

	/** The health of this XP orb. */
	private int xpOrbHealth = 5;
	//Type of grist
	private String gristType;
	/** This is how much XP this orb has. */
	private int gristValue;

	/** The closest EntityPlayer to this orb. */
	private EntityPlayer closestPlayer;

	/** Threshold color for tracking players */
	private int xpTargetColor;

	public EntityGrist(World world, double x, double y, double z, String type, int value)
	{
		super(world);
		this.setSize(0.5F, 0.5F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
		this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
		this.gristValue = value;

		this.gristType = type;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	public EntityGrist(World par1World)
	{
		super(par1World);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.gristType = "Build";
	}

	protected void entityInit() {}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float par1)
	{
		float f1 = 0.5F;

		int i = super.getBrightnessForRender(par1);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f1 * 15.0F * 16.0F);

		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.029999999329447746D;

		if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava)
		{
			this.motionY = 0.20000000298023224D;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		double d0 = 8.0D;

		if (this.xpTargetColor < this.xpColor - 20 + this.entityId % 100)
		{
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
			{
				this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
			}

			this.xpTargetColor = this.xpColor;
		}

		if (this.closestPlayer != null)
		{
			double d1 = (this.closestPlayer.posX - this.posX) / d0;
			double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
			double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = 1.0D - d4;

			if (d5 > 0.0D)
			{
				d5 *= d5;
				this.motionX += d1 / d4 * d5 * 0.1D;
				this.motionY += d2 / d4 * d5 * 0.1D;
				this.motionZ += d3 / d4 * d5 * 0.1D;
			}
		}

		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f = 0.98F;

		if (this.onGround)
		{
			f = 0.58800006F;
			int i = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));

			if (i > 0)
			{
				f = Block.blocksList[i].slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)f;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= (double)f;

		if (this.onGround)
		{
			this.motionY *= -0.8999999761581421D;
		}

		++this.xpColor;
		++this.xpOrbAge;

		if (this.xpOrbAge >= 6000)
		{
			this.setDead();
		}
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters velocity to the entity
	 */
	public boolean handleWaterMovement()
	{
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	/**
	 * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
	 * amountDamage
	 */
	protected void dealFireDamage(int par1)
	{
		this.attackEntityFrom(DamageSource.inFire, par1);
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
	{
		if (this.isEntityInvulnerable())
		{
			return false;
		}
		else
		{
			this.setBeenAttacked();
			this.xpOrbHealth -= par2;

			if (this.xpOrbHealth <= 0)
			{
				this.setDead();
			}

			return false;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("Health", (short)((byte)this.xpOrbHealth));
		par1NBTTagCompound.setShort("Age", (short)this.xpOrbAge);
		par1NBTTagCompound.setShort("Value", (short)this.gristValue);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.xpOrbHealth = par1NBTTagCompound.getShort("Health") & 255;
		this.xpOrbAge = par1NBTTagCompound.getShort("Age");
		this.gristValue = par1NBTTagCompound.getShort("Value");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote)
		{
				this.playSound("random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
				par1EntityPlayer.onItemPickup(this, 1);
				if(par1EntityPlayer.getEntityData().getCompoundTag("Grist").getTags().size() == 0)
					par1EntityPlayer.getEntityData().setCompoundTag("Grist", new NBTTagCompound("Grist"));
				this.addGrist(par1EntityPlayer);
//				System.out.println(par1EntityPlayer.getEntityData().getCompoundTag("Grist").getInteger("Build"));
				this.setDead();
		}
		else 
			this.setDead();
	}
	public void addGrist(EntityPlayer entityPlayer)
	{
		int oldValue = entityPlayer.getEntityData().getCompoundTag("Grist").getInteger(this.gristType);
		entityPlayer.getEntityData().getCompoundTag("Grist").setInteger(this.gristType, oldValue + gristValue);
        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "Minestuck";
        packet.data = MinestuckPacket.makePacket(Type.GRIST, typeInt(this.gristType), oldValue + gristValue, this.entityId	);
        packet.length = packet.data.length;
        ((EntityPlayerMP)entityPlayer).playerNetServerHandler.sendPacketToPlayer(packet);
	}

	/**
	 * Returns the XP value of this XP orb.
	 */

	@SideOnly(Side.CLIENT)

	/**
	 * Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
	 * what texture to use.
	 */
	public int getTextureByXP()
	{
		return this.gristValue >= 2477 ? 10 : (this.gristValue >= 1237 ? 9 : (this.gristValue >= 617 ? 8 : (this.gristValue >= 307 ? 7 : (this.gristValue >= 149 ? 6 : (this.gristValue >= 73 ? 5 : (this.gristValue >= 37 ? 4 : (this.gristValue >= 17 ? 3 : (this.gristValue >= 7 ? 2 : (this.gristValue >= 3 ? 1 : 0)))))))));
	}

	/**
	 * Get xp split rate (Is called until the xp drop code in EntityLiving.onEntityUpdate is complete)
	 */
//	public static int getXPSplit(int par0)
//	{
//		return par0 >= 2477 ? 2477 : (par0 >= 1237 ? 1237 : (par0 >= 617 ? 617 : (par0 >= 307 ? 307 : (par0 >= 149 ? 149 : (par0 >= 73 ? 73 : (par0 >= 37 ? 37 : (par0 >= 17 ? 17 : (par0 >= 7 ? 7 : (par0 >= 3 ? 3 : 1)))))))));
//	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem()
	{
		return false;
	}
	public static int typeInt(String type)
	{
		for(int index = 0; index < gristTypes.length; index++)
			if(type.equals(gristTypes[index]))return index;
		return -1;
	}
}
