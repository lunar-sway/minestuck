package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.ai.AnimatedMoveTowardsRestrictionGoal;
import com.mraof.minestuck.entity.ai.AnimatedPanicGoal;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ConsortPacket;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class TutleEntity extends CreatureEntity/* implements IAnimatable*/
{
	/*private boolean shouldLoop;
	
	//Only used server-side. Used to track the flower state and the progression of the animation
	//private int eventTimer = IDLE_TIME;
	
	@Nonnull
	private TutleEntity.Animation animation = TutleEntity.Animation.IDLE;
	
	private final AnimationFactory factory = new AnimationFactory(this);
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation(animation.animationName, shouldLoop));
		
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
	
	public enum Animation //animationName set in assets/minestuck/animations/lotus_flower.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("turtle.walkarms"),
		WALK("turtle.walk"),
		//WALK_ARMS("turtle.walkarms"),
		TALK("turtle.talk"),
		PANIC("turtle.panic"),
		PANIC_RUN("turtle.panic.run"),
		DIE("turtle.die"),
		ARMFIX("turtle.armfix");
		
		private final String animationName;
		
		Animation(String animationName)
		{
			this.animationName = animationName;
		}
	}
	
	private boolean hasHadMessage = false;
	ConsortDialogue.DialogueWrapper message;
	int messageTicksLeft;
	private CompoundNBT messageData;
	private final Set<PlayerIdentifier> talkRepPlayerList = new HashSet<>();
	public EnumConsort.MerchantType merchantType = EnumConsort.MerchantType.NONE;
	DimensionType homeDimension;
	boolean visitedSkaia;
	MessageType.DelayMessage updatingMessage; //TODO Change to an interface/array if more message components need tick updates
	public ConsortMerchantInventory stocks;
	private int eventTimer = -1;	//TODO use the interface mentioned in the todo above to implement consort explosion instead
	
	
	
	*/public TutleEntity(EntityType<? extends TutleEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.experienceValue = 1;
		setInvulnerable(true);
	}
	/*
	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
	}
	
	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new AnimatedPanicGoal(this, 1.4D));
		goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.6F));
		goalSelector.addGoal(4, new AnimatedMoveTowardsRestrictionGoal(this, 0.6F));
		goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.addGoal(7, new LookRandomlyGoal(this));
		goalSelector.addGoal(4, new AvoidEntityGoal<>(this, PlayerEntity.class, 16F, 1.0D, 1.4D, this::shouldFleeFrom));
	}
	
	private boolean shouldFleeFrom(LivingEntity entity)
	{
		return entity instanceof ServerPlayerEntity && EntityPredicates.CAN_AI_TARGET.test(entity) && PlayerSavedData.getData((ServerPlayerEntity) entity).getConsortReputation(homeDimension) <= -1000;
	}
	
	protected void applyAdditionalAITasks()
	{
		if(!this.detachHome() || getMaximumHomeDistance() > 1)
			goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5F));
	}
	
	@Override
	public boolean isWithinHomeDistanceCurrentPosition()
	{
		return homeDimension != this.dimension || super.isWithinHomeDistanceCurrentPosition();
	}
	
	public void setAnimation(@Nonnull Animation animation)
	{
		this.animation = animation;
	}
	
	public void setAnimationFromPacket(TutleEntity.Animation newAnimation)
	{
		if(world.isRemote) //allows client-side effects tied to server-side events
		{
			animation = newAnimation;
			if(animation == TutleEntity.Animation.IDLE)
			{
			
			}
				//addRestoreEffects();
		}
	}
	
	public void updateAndSendAnimation(TutleEntity.Animation animation, boolean shouldLoop)
	{
		this.animation = animation;
		this.shouldLoop = shouldLoop;
		ConsortPacket packet = ConsortPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
		MSPacketHandler.sendToTracking(packet, this);
	}*/
}
