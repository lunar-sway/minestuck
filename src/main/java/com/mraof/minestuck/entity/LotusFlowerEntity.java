package com.mraof.minestuck.entity;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.LotusFlowerPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class LotusFlowerEntity extends CreatureEntity implements IAnimatable, IEntityAdditionalSpawnData
{
	private final AnimationFactory factory = new AnimationFactory(this);
	
	private int eventTimer;
	
	public static final String REGROW = "minestuck.lotusflowerentity.regrow";
	
	public enum Animation
	{
		IDLE, OPEN, OPEN_IDLE, VANISH, EMPTY
	}
	
	private Animation animation;
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		if(animation == null)
		{
			animation = Animation.IDLE;
		}
		
		switch(animation)
		{
			case IDLE:
				event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.idle", true));
				break;
			
			case OPEN:
				event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open", true));
				break;
			
			case OPEN_IDLE:
				event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open.idle", true));
				break;
			
			case VANISH:
				event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.vanish", true));
				break;
			
			case EMPTY:
				event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.empty", true));
				break;
		}
		
		/*if(eventTimer == -1)
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.idle", true));
		if(eventTimer == 10000)
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open", true));
		if(eventTimer == 9880) //6 sec open animation * 20 ticks/sec, 10000 - 120 = 9880
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open.idle", true));
		if(eventTimer == 9560) //4 sec idle animation * 4 loops * 20 ticks/sec, 9880 - 320 = 9560
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.vanish", true));
		if(eventTimer == 9547) //0.65 sec vanish animation * 20 ticks/sec, 9560 - 13 = 9547
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.empty", true));*/
		
		return PlayState.CONTINUE;
	}
	
	public LotusFlowerEntity(EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand)
	{
		if(this.isAlive() && !player.isSneaking() && this.eventTimer < 0)
		{
			Vec3d posVec = getPositionVec();
			
			if(!world.isRemote)
			{
				setLotusActivatedTimer();
				world.playSound(null, posVec.getX(), posVec.getY(), posVec.getZ(), SoundEvents.BLOCK_COMPOSTER_READY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
			
			return true;
		}
		if(this.isAlive() && eventTimer > 2 && eventTimer <= 9547)
		{
			ItemStack itemstack = player.getHeldItem(hand);
			
			if(player.getDistanceSq(this) < 9.0D && itemstack.getItem() == Items.BONE_MEAL && player.isCreative())
			{
				Vec3d posVec = getPositionVec();
				if(!world.isRemote)
					eventTimer = 2;
				
				for(int i = 0; i < 10; i++)
				{
					this.world.addParticle(ParticleTypes.COMPOSTER, posVec.x, posVec.y + 0.5D, posVec.z, 0.5D - this.rand.nextDouble(), 0.5D - this.rand.nextDouble(), 0.5D - this.rand.nextDouble());
				}
			}
			
			if(!world.isRemote && itemstack.getItem() != Items.BONE_MEAL)
			{
				ITextComponent message = new TranslationTextComponent(REGROW);
				player.sendMessage(message);
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	protected void setLotusActivatedTimer()
	{
		this.eventTimer = 10001;
	}
	
	protected void sendPacketToTracking()
	{
		LotusFlowerPacket packet = LotusFlowerPacket.createPacket(this, animation);
		MSPacketHandler.sendToTracking(packet, this);
	}
	
	protected void spawnLoot()
	{
		World worldIn = this.world;
		Vec3d posVec = this.getPositionVec();
		
		ItemEntity unpoweredComputerItemEntity = new ItemEntity(worldIn, posVec.getX(), posVec.getY() + 1D, posVec.getZ(), new ItemStack(MSItems.COMPUTER_PARTS, 1));
		worldIn.addEntity(unpoweredComputerItemEntity);
		
		ItemEntity sburbCodeItemEntity = new ItemEntity(worldIn, posVec.getX(), posVec.getY() + 1D, posVec.getZ(), new ItemStack(MSItems.SBURB_CODE, 1));
		worldIn.addEntity(sburbCodeItemEntity);
		
		this.world.addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
	}
	
	protected void lotusResetEffects()
	{
		Vec3d posVec = this.getPositionVec();
		this.world.addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
		this.world.playSound(posVec.getX(), posVec.getY(), posVec.getZ(), SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
	}
	
	@Override
	public void livingTick()
	{
		super.livingTick();
		
		if(!world.isRemote)
		{
			if(eventTimer == -1)
			{
				animation = Animation.IDLE;
			}
			if(eventTimer == 10000)
			{
				animation = Animation.OPEN;
				sendPacketToTracking();
			}
			if(eventTimer == 9880) //6 sec open animation * 20 ticks/sec, 10000 - 120 = 9880
			{
				animation = Animation.OPEN_IDLE;
				sendPacketToTracking();
			}
			if(eventTimer == 9560) //4 sec idle animation * 4 loops * 20 ticks/sec, 9880 - 320 = 9560
			{
				animation = Animation.VANISH;
				sendPacketToTracking();
			}
			if(eventTimer == 9547) //0.65 sec vanish animation * 20 ticks/sec, 9560 - 13 = 9547
			{
				animation = Animation.EMPTY;
				sendPacketToTracking();
			}
			
			Debug.debugf("SERVERside: eventTimer = %s, animation = %s", eventTimer, animation);
		} else
			Debug.debugf("CLIENTside: eventTimer = %s, animation = %s", eventTimer, animation);
		
		if(this.eventTimer >= 0)
			this.eventTimer--;
		
		if(this.eventTimer == 9880)
			spawnLoot();
		
		if(this.eventTimer == 0)
			lotusResetEffects();
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer)
	{
		return false;
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		
		compound.putInt("EventTimer", eventTimer);
		compound.putInt("AnimationOrdinal", animation.ordinal());
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		
		eventTimer = compound.getInt("EventTimer");
		animation = LotusFlowerEntity.Animation.values()[compound.getInt("AnimationOrdinal")];
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(eventTimer);
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		eventTimer = additionalData.readInt();
	}
	
	public void timerSetPacketUpdate(Animation newAnimation)
	{
		if(world.isRemote)
			animation = newAnimation;
	}
	
	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		//Debug.debugf("onInitialSpawn");
		
		this.eventTimer = -1;
		this.animation = Animation.IDLE;
		
		this.entityCollisionReduction = 1F;
		this.setInvulnerable(true);
		this.setNoAI(true);
		this.enablePersistence();
		
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}
}