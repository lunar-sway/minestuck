package com.mraof.minestuck.entity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.LotusFlowerPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.Collections;

public class LotusFlowerEntity extends LivingEntity implements IAnimatable, IEntityAdditionalSpawnData
{
	// Animation lengths
	private static final int OPENING_LENGTH = 120;        //6 sec open animation * 20 ticks/sec = 120
	private static final int OPEN_IDLE_LENGTH = 320;    //4 sec idle animation * 4 loops * 20 ticks/sec = 320
	private static final int VANISHING_LENGTH = 13;        //0.65 sec vanish animation * 20 ticks/sec = 13
	
	// Animation start times
	private static final int IDLE_TIME = -1;
	private static final int OPEN_START = 0;
	private static final int OPEN_IDLE_START = OPEN_START + OPENING_LENGTH;
	private static final int VANISH_START = OPEN_IDLE_START + OPEN_IDLE_LENGTH;
	private static final int ANIMATION_END = VANISH_START + VANISHING_LENGTH;
	
	private final AnimationFactory factory = new AnimationFactory(this);
	
	//Only used server-side. Used to track the flower state and the progression of the animation
	private int eventTimer = IDLE_TIME;
	
	public static final String REGROW = "minestuck.lotusflowerentity.regrow";
	
	// Specifies the current animation phase
	@Nonnull
	private Animation animation = Animation.IDLE;
	
	protected LotusFlowerEntity(EntityType<? extends LotusFlowerEntity> type, World worldIn)
	{
		super(type, worldIn);
		
		setInvulnerable(true);
	}
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation(animation.animationName, true));
		
		return PlayState.CONTINUE;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	
	@Override
	public ActionResultType interact(PlayerEntity player, Hand hand)
	{
		if(isAlive() && !player.isShiftKeyDown() && animation == Animation.IDLE)
		{
			startLotusAnimation();
			return ActionResultType.SUCCESS;
		}
		if(isAlive() && animation == Animation.EMPTY)
		{
			ItemStack itemstack = player.getItemInHand(hand);
			
			if(player.distanceToSqr(this) < 36 && itemstack.getItem() == Items.BONE_MEAL && player.isCreative())
			{
				restoreFromBonemeal();
			} else if(level.isClientSide && player.distanceToSqr(this) < 36)
			{
				player.sendMessage(new TranslationTextComponent(REGROW), Util.NIL_UUID);
			}
			
			return ActionResultType.SUCCESS;
		} else
			return super.interact(player, hand);
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		
		if(!level.isClientSide)
		{
			if(animation != Animation.IDLE)
				setEventTimer(eventTimer + 1);
			
			if(eventTimer == OPEN_IDLE_START)
				spawnLoot();
			else if(eventTimer >= MinestuckConfig.SERVER.lotusRestorationTime.get() * 20) //600(default) seconds from animation start to flower restoration
				setEventTimer(IDLE_TIME);
		}
	}
	
	private void startLotusAnimation()
	{
		if(!level.isClientSide)
		{
			setEventTimer(OPEN_START);
			
			Vector3d posVec = position();
			level.playSound(null, posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_OPEN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		}
	}
	
	private void restoreFromBonemeal()
	{
		if(!level.isClientSide)
			setEventTimer(IDLE_TIME);
		
		Vector3d posVec = position();
		for(int i = 0; i < 10; i++)
			this.level.addParticle(ParticleTypes.COMPOSTER, posVec.x, posVec.y + 0.5, posVec.z, 0.5 - random.nextDouble(), 0.5 - random.nextDouble(), 0.5 - random.nextDouble());
	}
	
	private void setEventTimer(int time)
	{
		if(level.isClientSide)
			throw new IllegalStateException("Shouldn't call setEventTimer client-side!");
		
		eventTimer = time;
		
		Animation newAnimation = animationFromEventTimer();
		if(newAnimation != animation)
			updateAndSendAnimation(newAnimation);
	}
	
	private Animation animationFromEventTimer()
	{
		if(eventTimer >= ANIMATION_END)
			return Animation.EMPTY;
		else if(eventTimer >= VANISH_START)
			return Animation.VANISH; //TODO Lotus Flower petals spazz out underneath the petals for just a tick here
		else if(eventTimer >= OPEN_IDLE_START)
			return Animation.OPEN_IDLE;
		else if(eventTimer >= OPEN_START)
			return Animation.OPEN;
		else
			return Animation.IDLE; //TODO relevant to all animation steps but looping of animations is choppy
	}
	
	protected void updateAndSendAnimation(Animation animation)
	{
		this.animation = animation;
		LotusFlowerPacket packet = LotusFlowerPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
		MSPacketHandler.sendToTracking(packet, this);
	}
	
	protected void spawnLoot()
	{
		World worldIn = this.level;
		Vector3d posVec = this.position();
		
		ItemEntity unpoweredComputerItemEntity = new ItemEntity(worldIn, posVec.x(), posVec.y() + 1D, posVec.z(), new ItemStack(MSItems.COMPUTER_PARTS, 1));
		worldIn.addFreshEntity(unpoweredComputerItemEntity);
		
		ItemEntity sburbCodeItemEntity = new ItemEntity(worldIn, posVec.x(), posVec.y() + 1D, posVec.z(), new ItemStack(MSItems.SBURB_CODE, 1));
		worldIn.addFreshEntity(sburbCodeItemEntity);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		
		compound.putInt("EventTimer", eventTimer);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains("EventTimer", Constants.NBT.TAG_ANY_NUMERIC))
		{
			eventTimer = compound.getInt("EventTimer");
			animation = animationFromEventTimer();
		}
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeInt(animation.ordinal());
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		animation = Animation.values()[additionalData.readInt()];
	}
	
	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public void setAnimationFromPacket(Animation newAnimation)
	{
		if(level.isClientSide) //allows client-side effects tied to server-side events
		{
			animation = newAnimation;
			if(animation == Animation.IDLE)
				addRestoreEffects();
			if(animation == Animation.OPEN_IDLE)
				addLootSpawnEffects();
		}
	}
	
	protected void addRestoreEffects()
	{
		Vector3d posVec = this.position();
		this.level.addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
		this.level.playLocalSound(posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_RESTORE, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
	}
	
	protected void addLootSpawnEffects()
	{
		Vector3d posVec = this.position();
		this.level.addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
		this.level.playLocalSound(posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_LOOT_SPAWN, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
	}
	
	@Override
	protected boolean isMovementNoisy()
	{
		return false;
	}
	
	@Override
	public void move(MoverType typeIn, Vector3d pos)
	{
	}
	
	@Override
	public Iterable<ItemStack> getArmorSlots()
	{
		return Collections.emptyList();
	}
	
	@Override
	public ItemStack getItemBySlot(EquipmentSlotType slotIn)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack)
	{
	}
	
	@Override
	public HandSide getMainArm()
	{
		return HandSide.RIGHT;
	}
	
	public enum Animation //animationName set in assets/minestuck/animations/lotus_flower.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("lotus.idle"),
		OPEN("lotus.open"),
		OPEN_IDLE("lotus.open.idle"),
		VANISH("lotus.vanish"),
		EMPTY("lotus.empty");
		
		private final String animationName;
		
		Animation(String animationName)
		{
			this.animationName = animationName;
		}
	}
}