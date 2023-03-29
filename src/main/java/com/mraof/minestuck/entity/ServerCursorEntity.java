package com.mraof.minestuck.entity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerCursorPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.Collections;

public class ServerCursorEntity extends LivingEntity implements IAnimatable, IEntityAdditionalSpawnData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final AnimationFactory factory = new AnimationFactory(this);
	
	//These two are only used server-side to make sure cursors are removed properly.
	private int despawnTimer = 0;
	private boolean removalFlag = false;
	
	// Specifies the current animation phase
	@Nonnull
	private Animation animation = Animation.CLICK;
	
	protected ServerCursorEntity(EntityType<? extends ServerCursorEntity> type, Level level)
	{
		super(type, level);
		
		noPhysics = true;
		setNoGravity(true);
		setInvulnerable(true);
		
		setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
	}
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		if(!waitForFinish(event))
			event.getController().setAnimation(new AnimationBuilder().addAnimation(animation.animationName, animation.looping ? ILoopType.EDefaultLoopTypes.LOOP : ILoopType.EDefaultLoopTypes.PLAY_ONCE));
		
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
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		
		compound.putInt("DespawnTimer", despawnTimer);
		compound.putInt("Animation", animation.ordinal());
		if(removalFlag)
			compound.putBoolean("RemovalFlag", true);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
		if(compound.contains("DespawnTimer", Tag.TAG_ANY_NUMERIC))
			despawnTimer = compound.getInt("DespawnTimer");
		if(compound.contains("Animation", Tag.TAG_ANY_NUMERIC))
			animation = Animation.values()[compound.getInt("Animation")];
		if(compound.contains("RemovalFlag"))
			removalFlag = compound.getBoolean("RemovalFlag");
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeInt(animation.ordinal());
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		animation = Animation.values()[additionalData.readInt()];
	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		
		if(!level.isClientSide)
		{
			if(!animation.looping || removalFlag)
				despawnTimer += 1;
			
			if(!removalFlag)
			{
				if(despawnTimer >= MinestuckConfig.SERVER.cursorDespawnTime.get())
					this.remove(RemovalReason.DISCARDED); //After cursorDespawnTime ticks of the cursor not receiving updates, the cursor will be automatically removed.
			} else
			{
				if(despawnTimer >= animation.length + MinestuckConfig.SERVER.cursorRemovalPadding.get() + 1)
					this.remove(RemovalReason.DISCARDED); //cursorRemovalPadding ticks after the animation is done (+1 tick so the client can receive the animation), remove the cursor.
			}
		}
	}
	
	//Ensures that the head and body always face the same way as the root Y rotation.
	@Override
	protected float tickHeadTurn(float pYRot, float pAnimStep)
	{
		this.yBodyRot = this.getYRot();
		this.yHeadRot = this.getYRot();
		return pAnimStep;
	}
	
	//Remove linear interpolation so that the position is more immediately clear.
	@Override
	public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport)
	{
		this.setPos(pX, pY, pZ);
		this.setRot(pYaw, pPitch);
		this.yBodyRot = this.getYRot();
		this.yHeadRot = this.getYRot();
	}
	
	@Override
	protected MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
	@Override
	public Iterable<ItemStack> getArmorSlots()
	{
		return Collections.emptyList();
	}
	
	@Override
	public ItemStack getItemBySlot(EquipmentSlot pSlot)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack)
	{
	}
	
	@Override
	public HumanoidArm getMainArm()
	{
		return HumanoidArm.RIGHT;
	}
	
	public void setAnimation(Animation animation)
	{
		if(!level.isClientSide)
		{
			this.animation = animation;
			if(!removalFlag)
				this.despawnTimer = 0;
			ServerCursorPacket packet = ServerCursorPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
			MSPacketHandler.sendToTracking(packet, this);
		} else
			setAnimationFromPacket(animation);
	}
	
	public void setAnimationFromPacket(ServerCursorEntity.Animation animation)
	{
		if(level.isClientSide) //allows client-side effects tied to server-side events
		{
			this.animation = animation;
		}
	}
	
	public void queueRemoval(Animation removalAnimation)
	{
		if(!level.isClientSide)
		{
			setAnimation(removalAnimation);
			this.removalFlag = true;
		} else
			throw new IllegalStateException("queueRemoval() was accessed from the client-side! It should only be accessed remotely.");
	}
	
	private boolean waitForFinish(AnimationEvent event)
	{
		if(event.getController().getCurrentAnimation() == null)
			return false;
		
		if(event.getController().getCurrentAnimation().loop == ILoopType.EDefaultLoopTypes.PLAY_ONCE && !removalFlag)
			return event.getController().getAnimationState() == AnimationState.Running;
		else
			return false; //Do not wait if animation is looping or if the cursor is going to be removed, so that the removal animation can be played.
	}
	
	public enum Animation //animationName set in assets/minestuck/animations/server_cursor.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("animation.ServerCursorModel.idle", true, 40), //2 sec idle animation * 20 ticks/sec = 40
		CLICK("animation.ServerCursorModel.click", false, 4), //0.2 sec click animation * 20 ticks/sec = 4
		REJECTED("animation.ServerCursorModel.rejected", false, 4), //0.2 sec reject animation * 20 ticks/sec = 4
		LOADING("animation.ServerCursorModel.loading", true, 20); //1 sec loading animation * 20 ticks/sec = 20
		
		private final String animationName;
		private final boolean looping;
		private final int length;
		
		Animation(String animationName, boolean looping, int length)
		{
			this.animationName = animationName;
			this.looping = looping;
			this.length = length;
		}
	}
}
