package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class GristEntity extends Entity implements IEntityAdditionalSpawnData
{
	//TODO Perhaps use a data manager for grist type in the same way as the underling entity?
	public int cycle;
	
	public int gristAge = 0;
	
	private int gristHealth = 5;
	//Type of grist
	private GristType gristType = GristTypes.BUILD.get();
	private long gristValue = 1;
	
	private Player closestPlayer;
	
	private int targetCycle;
	
	public static GristEntity create(EntityType<? extends GristEntity> type, Level level)
	{
		return new GristEntity(type, level);
	}
	
	public GristEntity(Level level, double x, double y, double z, GristAmount gristData)
	{
		super(MSEntityTypes.GRIST.get(), level);
		this.gristValue = gristData.getAmount();
		//this.yOffset = this.height / 2.0F;
		this.setPos(x, y, z);
		this.setYRot((float) (Math.random() * 360.0D));
		this.setDeltaMovement(level.random.nextGaussian() * 0.2D - 0.1D, level.random.nextGaussian() * 0.2D, level.random.nextGaussian() * 0.2D - 0.1D);
		
		this.gristType = gristData.getType();
	}
	
	public GristEntity(EntityType<? extends GristEntity> type, Level level)
	{
		super(type, level);
	}
	
	@Override
	protected void defineSynchedData()
	{
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
			this.gristHealth = (int) ((float) this.gristHealth - amount);
			
			if(this.gristHealth <= 0)
			{
				this.discard();
			}
			
			return false;
		}
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.setDeltaMovement(this.getDeltaMovement().add(0, -0.03D, 0));
		
		if(this.level.getBlockState(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ()))).getMaterial() == Material.LAVA)
		{
			this.setDeltaMovement(0.2D, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
		}
		
		//this.setPosition(this.getPosX(), (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.getPosZ());
		double d0 = this.getDimensions(Pose.STANDING).width * 2.0D;
		
		if(this.targetCycle < this.cycle - 20 + this.getId() % 100) //Why should I care about the entityId
		{
			if(this.closestPlayer == null || this.closestPlayer.distanceToSqr(this) > d0 * d0)
			{
				this.closestPlayer = this.level.getNearestPlayer(this, d0);
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
		
		if(this.onGround)
		{
			BlockPos pos = new BlockPos(this.getX(), this.getBoundingBox().minY - 1, this.getZ());
			f = this.level.getBlockState(pos).getFriction(level, pos, this) * 0.98F;
		}
		
		this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.98D, f));
		
		if(this.onGround)
		{
			this.setDeltaMovement(this.getDeltaMovement().multiply(1, -0.9D, 1));
		}
		
		++this.cycle;
		++this.gristAge;
		
		if(this.gristAge >= 6000)
		{
			this.discard();
		}
		
	}
	
	@Override
	public void checkDespawn()
	{
		if(this.gristValue <= 0)
			discard();
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
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.putShort("Health", (short) this.gristHealth);
		compound.putShort("Age", (short) this.gristAge);
		compound.putLong("Value", (short) this.gristValue);
		compound.putString("Type", gristType.getRegistryName().toString());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		this.gristHealth = compound.getShort("Health") & 255;
		this.gristAge = compound.getShort("Age");
		if(compound.contains("Value", Tag.TAG_ANY_NUMERIC))
			this.gristValue = compound.getLong("Value");
		if(compound.contains("Type", Tag.TAG_STRING))
			this.gristType = GristType.read(compound, "Type");
	}
	
	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void playerTouch(Player entityIn)
	{
		if(this.level.isClientSide ? ClientEditHandler.isActive() : ServerEditHandler.getData(entityIn) != null)
			return;
		
		if(!this.level.isClientSide && !(entityIn instanceof FakePlayer))
		{
			consumeGrist(IdentifierHandler.encode(entityIn), true);
		}
	}
	
	public void consumeGrist(PlayerIdentifier identifier, boolean sound)
	{
		if(this.level.isClientSide)
			throw new IllegalStateException("Grist entities shouldn't be consumed client-side.");
		if(sound)
			this.playSound(SoundEvents.ITEM_PICKUP, 0.1F, 0.5F * ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.8F));
		GristHelper.increase(level, identifier, new GristSet(gristType, gristValue));
		GristHelper.notify(level.getServer(), identifier, new GristSet(gristType, gristValue));
		this.discard();
	}
	
	@Override
	public boolean isAttackable()
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
	
	@Override
	public EntityDimensions getDimensions(Pose poseIn)
	{
		return super.getDimensions(poseIn).scale((float) Math.pow(gristValue, .25));
	}
	
	public float getSizeByValue()
	{
		return (float) (Math.pow((double) this.gristValue, 0.25D) / 3.0D);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeRegistryId(gristType);
		buffer.writeLong(gristValue);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf data)
	{
		gristType = data.readRegistryIdSafe(GristType.class);
		gristValue = data.readLong();
	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}