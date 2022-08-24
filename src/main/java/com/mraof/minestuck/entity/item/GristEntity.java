package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.network.GristEntityPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class GristEntity extends Entity implements IEntityAdditionalSpawnData
{	//TODO Perhaps use a data manager for grist type in the same way as the underling entity?
	public int cycle;
	
	public int consumeDelay;

	public int gristAge = 0;

	private int gristHealth = 5;
	//Type of grist
	private GristType gristType = GristTypes.BUILD.get();
	private long gristValue = 1;

	private Player closestPlayer;

	private int targetCycle;
	
	private Animation animation = Animation.REJECT;
	
	public static GristEntity create(EntityType<? extends GristEntity> type, Level level)
	{
		return new GristEntity(type, level);
	}
	
	public GristEntity(Level level, double x, double y, double z, GristAmount gristData)
	{
		super(MSEntityTypes.GRIST, level);
		this.gristValue = gristData.getAmount();
//		this.yOffset = this.height / 2.0F;
		this.setPos(x, y, z);
		this.setYRot((float)(Math.random() * 360.0D));
		this.setDeltaMovement(level.random.nextGaussian() * 0.2D - 0.1D, level.random.nextGaussian() * 0.2D, level.random.nextGaussian() * 0.2D - 0.1D);
		
		this.gristType = gristData.getType();
	}

	public GristEntity(EntityType<? extends GristEntity> type, Level level)
	{
		super(type, level);
	}
	
	/**
	 * this is where we set up our consumedelay
	 */
	public GristEntity(Level level, double x, double y, double z, GristAmount gristData, int pickupDelay)
	{
		this(level, x, y, z, gristData);
		consumeDelay = pickupDelay;
		// Set the class's consume-delay variable to equal the pickupDelay value that got passed in.
	}
	
	
	
	@Override
	protected void defineSynchedData()
	{}
	
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
		}
		else
		{
			this.markHurt();
			this.gristHealth = (int)((float)this.gristHealth - amount);
			
			if (this.gristHealth <= 0)
			{
				this.discard();
			}
			
			return false;
		}
	}
	
	public void setAnimationFromPacket(Animation animation)
	{
		switch(animation)
		{
			case REJECT:
				shaderAlpha = 255;
				break;
		}
	}
	
	public enum Animation
	{
		REJECT
	}
	
	@Override
	
	public void tick()
	{
		if(this.level.isClientSide == true)
		{
			shaderAlpha = Math.max(shaderAlpha - 7, 0);
		}
		
		
		super.tick();

		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.setDeltaMovement(this.getDeltaMovement().add(0, -0.03D, 0));

		if (this.level.getBlockState(new BlockPos(Mth.floor(this.getX()), Mth.floor(this.getY()), Mth.floor(this.getZ()))).getMaterial() == Material.LAVA)
		{
			this.setDeltaMovement(0.2D, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
			this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
		}

		//this.setPosition(this.getPosX(), (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.getPosZ());
		double d0 = this.getDimensions(Pose.STANDING).width * 2.0D;

		if (this.targetCycle < this.cycle - 20 + this.getId() % 100  && shaderAlpha == 0) //Why should I care about the entityId
		{
			if (this.closestPlayer == null || this.closestPlayer.distanceToSqr(this) > d0 * d0)
			{
				this.closestPlayer = this.level.getNearestPlayer(this, d0);
			}

			this.targetCycle = this.cycle;
		}

		if (this.closestPlayer != null && shaderAlpha == 0)
		{
			double d1 = (this.closestPlayer.getX() - this.getX()) / d0;
			double d2 = (this.closestPlayer.getY() + (double)this.closestPlayer.getEyeHeight() - this.getY()) / d0;
			double d3 = (this.closestPlayer.getZ() - this.getZ()) / d0;
			double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
			double d5 = this.getDimensions(Pose.STANDING).width * 2.0D - d4;

			if (d5 > 0.0D)
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

		if (this.onGround)
		{
			this.setDeltaMovement(this.getDeltaMovement().multiply(1, -0.9D, 1));
		}

		++this.cycle;
		++this.gristAge;

		if (this.gristAge >= 6000)
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
		compound.putShort("Health", (short)this.gristHealth);
		compound.putShort("Age", (short)this.gristAge);
		compound.putLong("Value", (short)this.gristValue);
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
	
	public int shaderAlpha = 0;
	
	/**
	 * Called by a player entity when they collide with an entity
	 * dectects collision with player entity and
	 */
	@Override
	public void playerTouch(Player entityIn)
	{
		if(this.level.isClientSide ? ClientEditHandler.isActive() : ServerEditHandler.getData(entityIn) != null)
			return; //checks if player is in edit mode. returns nothing and doesn't allow the entities to touch.
		
		if(entityIn instanceof ServerPlayer)
		{
			Session playerSession = SessionHandler.get(level).
					getPlayerSession(IdentifierHandler.encode(entityIn));
			
			long playerGristAmount;
			long rung;
			
			playerGristAmount = PlayerSavedData.getData((ServerPlayer) entityIn).getGristCache().getGrist(gristType);
			rung = PlayerSavedData.getData((ServerPlayer) entityIn).getEcheladder().getRung();
			
			int gristCap = GristHelper.rungGrist[(int) rung];
			int gutterCap = 0;
			long gutterTotal = 0;
			
			if(playerSession != null)
			{
				gutterTotal = playerSession.getGristGutter().getGutterTotal();
				gutterCap = (int) playerSession.getGristGutter().getGutterCapacity();
			}
			
			long gutterRoom = gutterCap - gutterTotal;
			long cacheRoom = gristCap - playerGristAmount;
			
			boolean hasRoom = (gutterRoom + cacheRoom) >= (gristValue);
			
			if(hasRoom)
			{
				consumeGrist(IdentifierHandler.encode(entityIn), true);
			}
			else
			{
				this.animation = Animation.REJECT;
				GristEntityPacket packet = GristEntityPacket.createPacket(this, animation);
				MSPacketHandler.sendToTracking(packet, this);
				shaderAlpha = 255;
			}
		}
	}
	
	
	public void consumeGrist(PlayerIdentifier identifier, boolean sound)
	{
		
		if(this.level.isClientSide)
			throw new IllegalStateException("Grist entities shouldn't be consumed client-side.");
		if(sound)
			this.playSound(SoundEvents.ITEM_PICKUP, 0.1F, 0.5F *
					((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.8F));
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

	public float getSizeByValue() {
		return (float)(Math.pow((double)this.gristValue, 0.25D) / 3.0D);
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