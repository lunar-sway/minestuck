package com.mraof.minestuck.entity;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerCursorPacket;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ServerCursorEntity extends LivingEntity implements GeoEntity, IEntityAdditionalSpawnData
{
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
	//These are only used server-side to make sure cursors are removed properly.
	private int despawnTimer = 0;
	private boolean removalFlag = false;
	private final int CURSOR_DESPAWN_TICKS = 40; //The time, in ticks, that it takes for a Sburb editmode cursor to despawn after not receiving any updates.
	private final int CURSOR_REMOVAL_PADDING = 3; //The time added, in ticks, after the removal animation has finished, but before the cursor is removed.
	
	// Specifies the current animation phase
	@Nonnull
	private AnimationType animationType = AnimationType.CLICK;
	
	protected ServerCursorEntity(EntityType<? extends ServerCursorEntity> type, Level level)
	{
		super(type, level);
		
		noPhysics = true;
		setNoGravity(true);
		setInvulnerable(true);
		
		setBoundingBox(new AABB(0, 0, 0, 0, 0, 0));
	}
	
	private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state)
	{
		if(!waitForFinish(state))
			state.getController().setAnimation(animationType.animation);
		
		return PlayState.CONTINUE;
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return this.cache;
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeInt(animationType.ordinal());
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		animationType = AnimationType.values()[additionalData.readInt()];
	}
	
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		
		if(!level().isClientSide)
		{
			if(!animationType.looping || removalFlag)
				despawnTimer += 1;
			
			if(!removalFlag)
			{
				if(despawnTimer >= CURSOR_DESPAWN_TICKS)
					this.remove(RemovalReason.DISCARDED); //After cursorDespawnTime ticks of the cursor not receiving updates, the cursor will be automatically removed.
			} else
			{
				if(despawnTimer >= animationType.length + CURSOR_REMOVAL_PADDING + 1)
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
		this.yBodyRotO = this.getYRot();
		this.yHeadRotO = this.getYRot();
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
		return MovementEmission.NONE;
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
	
	public void setAnimation(AnimationType animation)
	{
		if(!level().isClientSide)
		{
			this.animationType = animation;
			if(!removalFlag)
				this.despawnTimer = 0;
			ServerCursorPacket packet = ServerCursorPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
			MSPacketHandler.sendToTracking(packet, this);
		} else
			setAnimationFromPacket(animation);
	}
	
	public void setAnimationFromPacket(AnimationType animation)
	{
		if(level().isClientSide) //allows client-side effects tied to server-side events
		{
			this.animationType = animation;
		}
	}
	
	public void queueRemoval(AnimationType removalAnimation)
	{
		if(!level().isClientSide)
		{
			setAnimation(removalAnimation);
			this.removalFlag = true;
		} else
			throw new IllegalStateException("queueRemoval() was accessed from the client-side! It should only be accessed remotely.");
	}
	
	private boolean waitForFinish(AnimationState<?> event)
	{
		if(event.getController().getCurrentAnimation() == null)
			return false;
		
		if(event.getController().getCurrentAnimation().loopType() == Animation.LoopType.PLAY_ONCE && !removalFlag)
			return event.getController().getAnimationState() == AnimationController.State.RUNNING;
		else
			return false; //Do not wait if animation is looping or if the cursor is going to be removed, so that the removal animation can be played.
	}
	
	public enum AnimationType //animationName set in assets/minestuck/animations/server_cursor.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("animation.ServerCursorModel.idle", true, 40), //2 sec idle animation * 20 ticks/sec = 40
		CLICK("animation.ServerCursorModel.click", false, 4), //0.2 sec click animation * 20 ticks/sec = 4
		REJECTED("animation.ServerCursorModel.rejected", false, 4), //0.2 sec reject animation * 20 ticks/sec = 4
		LOADING("animation.ServerCursorModel.loading", true, 20); //1 sec loading animation * 20 ticks/sec = 20
		
		private final RawAnimation animation;
		private final boolean looping;
		private final int length;
		
		AnimationType(String animationName, boolean looping, int length)
		{
			this.animation = RawAnimation.begin().then(animationName, looping ? Animation.LoopType.LOOP : Animation.LoopType.PLAY_ONCE);
			this.looping = looping;
			this.length = length;
		}
	}
}
