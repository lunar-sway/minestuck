package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class GristEntity extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;

	public int gristAge = 0;

	private int gristHealth = 5;
	//Type of grist
	private GristType gristType = GristType.BUILD;
	private long gristValue = 1;

	private PlayerEntity closestPlayer;

	private int targetCycle;
	
	public static GristEntity create(EntityType<? extends GristEntity> type, World world)
	{
		return new GristEntity(type, world);
	}
	
	public GristEntity(World world, double x, double y, double z, GristAmount gristData)
	{
		super(MSEntityTypes.GRIST, world);
		this.gristValue = gristData.getAmount();
//		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.setMotion(world.rand.nextGaussian() * 0.2D - 0.1D, world.rand.nextGaussian() * 0.2D, world.rand.nextGaussian() * 0.2D - 0.1D);
		
		this.gristType = gristData.getType();
	}

	public GristEntity(EntityType<? extends GristEntity> type, World world)
	{
		super(type, world);
	}
	
	@Override
	protected void registerData()
	{}
	
	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isInvulnerableTo(source))
		{
			return false;
		} else
		{
			this.markVelocityChanged();
			this.gristHealth = (int)((float)this.gristHealth - amount);
			
			if (this.gristHealth <= 0)
			{
				this.remove();
			}
			
			return false;
		}
	}
	
	@Override
	public int getBrightnessForRender()
	{
		float f1 = 0.5F;

		int i = super.getBrightnessForRender();
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f1 * 15.0F * 16.0F);

		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}
	
	@Override
	public void tick()
	{
		super.tick();

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.setMotion(this.getMotion().add(0, -0.03D, 0));

		if (this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ))).getMaterial() == Material.LAVA)
		{
			this.setMotion(0.2D, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		//this.setPosition(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
		double d0 = this.getSize(Pose.STANDING).width * 2.0D;

		if (this.targetCycle < this.cycle - 20 + this.getEntityId() % 100) //Why should I care about the entityId
		{
			if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > d0 * d0)
			{
				this.closestPlayer = this.world.getClosestPlayer(this, d0);
			}

			this.targetCycle = this.cycle;
		}

		if (this.closestPlayer != null)
		{
			double d1 = (this.closestPlayer.posX - this.posX) / d0;
			double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
			double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = this.getSize(Pose.STANDING).width * 2.0D - d4;

			if (d5 > 0.0D)
			{
				this.setMotion(this.getMotion().add(d1 / d4 * d5 * 0.1D, d2 / d4 * d5 * 0.1D, d3 / d4 * d5 * 0.1D));
			}
		}
		
		this.move(MoverType.SELF, this.getMotion());
		float f = 0.98F;
		
		if(this.onGround)
		{
			BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
			f = this.world.getBlockState(pos).getSlipperiness(world, pos, this) * 0.98F;
		}
		
		this.setMotion(this.getMotion().mul(f, 0.98D, f));

		if (this.onGround)
		{
			this.setMotion(this.getMotion().mul(1, -0.9D, 1));
		}

		++this.cycle;
		++this.gristAge;

		if (this.gristAge >= 6000)
		{
			this.remove();
		}
		
	}

	/*
	 * Returns if this entity is in water and will end up adding the waters velocity to the entity
	 */
	/*@Override
	public boolean handleWaterMovement()
	{
		return this.world.handleMaterialAcceleration(this.getBoundingBox(), Material.WATER, this);
	}*/
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.putShort("Health", (short)this.gristHealth);
		compound.putShort("Age", (short)this.gristAge);
		compound.putLong("Value", (short)this.gristValue);
		compound.putString("Type", GristType.REGISTRY.getKey(gristType).toString());
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.gristHealth = compound.getShort("Health") & 255;
		this.gristAge = compound.getShort("Age");
		if(compound.contains("Value", 99))
			this.gristValue = compound.getLong("Value");
		if(compound.contains("Type", 8))
			this.gristType = GristType.getTypeFromString(compound.getString("Type"));
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(PlayerEntity entityIn)
	{
		if(this.world.isRemote?ClientEditHandler.isActive():ServerEditHandler.getData(entityIn) != null)
			return;
		
		if (!this.world.isRemote && !(entityIn instanceof FakePlayer))
		{
			consumeGrist(IdentifierHandler.encode(entityIn), true);
		}
	}
	
	public void consumeGrist(IdentifierHandler.PlayerIdentifier identifier, boolean sound)
	{
		if(this.world.isRemote)
			throw new IllegalStateException("Grist entities shouldn't be consumed client-side.");
		if(sound)
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
		if(GristHelper.increase(world, identifier, new GristSet(gristType, gristValue)))
		{
			PlayerTracker.updateGristCache(this.getServer(), identifier);
			this.remove();
		}
	}
	
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
	
	public GristType getGristType()
	{
		return gristType;
	}
	
	public GristAmount getAmount()
	{
		return new GristAmount(gristType, gristValue);
	}
	
	public static int typeInt(GristType type)
	{
		return type == null ? -1 : type.getId();
	
	}
	
	@Override
	public EntitySize getSize(Pose poseIn)
	{
		return super.getSize(poseIn).scale((float) Math.pow(gristValue, .25));
	}

	public float getSizeByValue() {
		return (float)(Math.pow((double)this.gristValue, 0.25D) / 3.0D);
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		if(typeInt(this.gristType) < 0)
		{
			this.remove();
		}
		buffer.writeInt(typeInt(this.gristType));
		buffer.writeLong(this.gristValue);
	}
	
	@Override
	public void readSpawnData(PacketBuffer data)
	{
		int typeOffset = data.readInt();
		if(typeOffset < 0)
		{
			this.remove();
			return;
		}
		this.gristType = GristType.REGISTRY.getValue(typeOffset);
		this.gristValue = data.readLong();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}