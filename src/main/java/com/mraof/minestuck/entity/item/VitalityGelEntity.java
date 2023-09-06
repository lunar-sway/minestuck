package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class VitalityGelEntity extends Entity implements IEntityAdditionalSpawnData
{
	public int cycle;
	
	public int age = 0;
	private int healAmount = 1;
	private int health = 5;
	
	private Player closestPlayer;
	
	private int targetCycle;
	
	public float animationOffset;
	
	public VitalityGelEntity(Level level, double x, double y, double z, int healAmount)
	{
		this(MSEntityTypes.VITALITY_GEL.get(), level, x, y, z, healAmount);
	}
	
	protected VitalityGelEntity(EntityType<? extends VitalityGelEntity> type, Level level, double x, double y, double z, int healAmount)
	{
		super(type, level);
		this.setPos(x, y, z);
		this.setYRot((float) (Math.random() * 360.0D));
		this.setDeltaMovement(level.random.nextGaussian() * 0.2D - 0.1D, level.random.nextGaussian() * 0.2D, level.random.nextGaussian() * 0.2D - 0.1D);
		
		this.healAmount = healAmount;
	}
	
	public VitalityGelEntity(EntityType<? extends VitalityGelEntity> type, Level level)
	{
		super(type, level);
		animationOffset = (float) (Math.random() * Math.PI * 2.0D);
	}
	
	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if(this.isInvulnerableTo(source))
		{
			return false;
		} else
		{
			this.markHurt();
			this.health = (int) ((float) this.health - amount);
			
			if(this.health <= 0)
			{
				this.discard();
			}
			
			return false;
		}
	}
	
	@Override
	protected void defineSynchedData()
	{
	}

//	@Override
//	public int getBrightnessForRender()
//	{
//		float f1 = 0.5F;
//
//		int i = super.getBrightnessForRender();
//		int j = i & 255;
//		int k = i >> 16 & 255;
//		j += (int)(f1 * 15.0F * 16.0F);
//
//		if (j > 240)
//		{
//			j = 240;
//		}
//
//		return j | k << 16;
//	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick()
	{
		super.tick();
		
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.setDeltaMovement(this.getDeltaMovement().add(0, -0.03D, 0));
		
		if(this.isInLava())
		{
			this.setDeltaMovement(0.2D, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
		}
		
		double d0 = this.getDimensions(Pose.STANDING).width * 2.0D;
		
		if(this.targetCycle < this.cycle - 20 + this.getId() % 100)
		{
			if(this.closestPlayer == null || this.closestPlayer.distanceToSqr(this) > d0 * d0)
			{
				this.closestPlayer = this.level().getNearestPlayer(this, d0);
			}
			
			this.targetCycle = this.cycle;
		}
		
		if(this.closestPlayer != null)
		{
			double d1 = (this.closestPlayer.getX() - this.getX()) / d0;
			double d2 = (this.closestPlayer.getY() + (double) this.closestPlayer.getEyeHeight() - this.getY()) / d0;
			double d3 = (this.closestPlayer.getZ() - this.getZ()) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = this.getDimensions(Pose.STANDING).width * 2.0D - d4;
			
			if(d5 > 0.0D)
			{
				this.setDeltaMovement(this.getDeltaMovement().add(d1 / d4 * d5 * 0.1D, d2 / d4 * d5 * 0.1D, d3 / d4 * d5 * 0.1D));
			}
		}
		
		this.move(MoverType.SELF, this.getDeltaMovement());
		float f = 0.98F;
		
		if(this.onGround())
		{
			BlockPos pos = new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getBoundingBox().minY) - 1, Mth.floor(this.getZ()));
			f = this.level().getBlockState(pos).getFriction(level(), pos, this) * 0.98F;
		}
		
		this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.98D, f));
		
		if(this.onGround())
		{
			this.setDeltaMovement(this.getDeltaMovement().multiply(1, -0.9D, 1));
		}
		
		++this.cycle;
		++this.age;
		
		if(this.age >= 6000)
		{
			this.discard();
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
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.putShort("health", (short) ((byte) this.health));
		compound.putShort("age", (short) this.age);
		compound.putShort("amount", (short) this.healAmount);
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		this.health = compound.getShort("health") & 255;
		this.age = compound.getShort("age");
		if(compound.contains("amount", Tag.TAG_ANY_NUMERIC))
			this.healAmount = compound.getShort("amount");
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void playerTouch(Player player)
	{
		if(this.level().isClientSide ? ClientEditHandler.isActive() : ServerEditHandler.getData(player) != null)
			return;
		
		if(!this.level().isClientSide)
		{
			this.playSound(SoundEvents.ITEM_PICKUP, 0.1F, 0.5F * ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.8F));
			player.heal(healAmount);
		}
		this.discard();
	}
	
	@Override
	public boolean isAttackable()
	{
		return false;
	}
	
	@Override
	public EntityDimensions getDimensions(Pose poseIn)
	{
		return super.getDimensions(poseIn).scale(healAmount);
	}
	
	public float getSizeByValue()
	{
		return (float) this.healAmount / 4.0F;
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.healAmount);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf data)
	{
		this.healAmount = data.readInt();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}