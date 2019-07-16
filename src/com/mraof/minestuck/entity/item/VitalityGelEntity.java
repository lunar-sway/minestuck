package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.ModEntityTypes;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class VitalityGelEntity extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;
	
	public int age = 0;
	private int healAmount = 1;
	private int health = 5;
	
	private PlayerEntity closestPlayer;

	private int targetCycle;
	
	public float animationOffset;
	
	public VitalityGelEntity(World world, double x, double y, double z, int healAmount)
	{
		this(ModEntityTypes.VITALITY_GEL, world, x, y, z, healAmount);
	}
	
	protected VitalityGelEntity(EntityType<? extends VitalityGelEntity> type, World world, double x, double y, double z, int healAmount)
	{
		super(type, world);
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.setMotion(world.rand.nextGaussian() * 0.2D - 0.1D, world.rand.nextGaussian() * 0.2D, world.rand.nextGaussian() * 0.2D - 0.1D);
		
		this.healAmount = healAmount;
	}

	public VitalityGelEntity(EntityType<? extends VitalityGelEntity> type, World world)
	{
		super(type, world);
		animationOffset = (float) (Math.random() * Math.PI * 2.0D);
	}
	
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
			this.health = (int)((float)this.health - amount);
			
			if (this.health <= 0)
			{
				this.remove();
			}
			
			return false;
		}
	}
	
	@Override
	protected void registerData()
	{
	}
	
	
	@OnlyIn(Dist.CLIENT)
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

	/**
	 * Called to update the entity's position/logic.
	 */
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
		
		double d0 = this.getSize(Pose.STANDING).width * 2.0D;
		
		if (this.targetCycle < this.cycle - 20 + this.getEntityId() % 100)
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
		++this.age;

		if (this.age >= 6000)
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
		return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
	}*/
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.putShort("health", (short)((byte)this.health));
		compound.putShort("age", (short)this.age);
		compound.putShort("amount", (short)this.healAmount);
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.health = compound.getShort("health") & 255;
		this.age = compound.getShort("age");
		if(compound.contains("amount", 99))
			this.healAmount = compound.getShort("amount");
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(PlayerEntity player)
	{
		if(this.world.isRemote?ClientEditHandler.isActive():ServerEditHandler.getData(player) != null)
			return;
		
		if (!this.world.isRemote)
		{
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
			player.heal(healAmount);
			this.remove();
		}
		else  
			this.remove();
	}
	
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
	
	@Override
	public EntitySize getSize(Pose poseIn)
	{
		return super.getSize(poseIn).scale(healAmount);
	}

	public float getSizeByValue() {	return (float)this.healAmount / 4.0F; }

	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(this.healAmount);
	}
	
	@Override
	public void readSpawnData(PacketBuffer data)
	{
		this.healAmount = data.readInt();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}