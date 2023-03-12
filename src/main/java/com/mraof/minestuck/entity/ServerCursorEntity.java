package com.mraof.minestuck.entity;

import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ServerCursorPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
	
	// Specifies the current animation phase
	@Nonnull
	private ServerCursorEntity.Animation animation = ServerCursorEntity.Animation.CLICK;
	
	private boolean canSwitchAnimation = false;
	
	protected ServerCursorEntity(EntityType<? extends ServerCursorEntity> type, Level level)
	{
		super(type, level);
		
		noPhysics = true;
		setNoGravity(true);
		setInvulnerable(true);
	}
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		canSwitchAnimation = !waitForFinish(event);
		
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
	public AnimationFactory getFactory() { return this.factory; }
	
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
	public Packet<?> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
	
	//Ensures that the head and body always face the same way as the root Y rotation.
	@Override
	protected float tickHeadTurn(float pYRot, float pAnimStep) {
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
	public Iterable<ItemStack> getArmorSlots() { return Collections.emptyList(); }
	
	@Override
	public ItemStack getItemBySlot(EquipmentSlot pSlot) { return ItemStack.EMPTY; }
	
	@Override
	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack)
	{
	}
	
	@Override
	public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
	
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		ServerCursorPacket packet = ServerCursorPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
		MSPacketHandler.sendToTracking(packet, this);
	}
	
	public void setAnimationFromPacket(ServerCursorEntity.Animation animation)
	{
		if(level.isClientSide) //allows client-side effects tied to server-side events
		{
			this.animation = animation;
		}
	}
	
	private boolean waitForFinish(AnimationEvent event)
	{
		if(event.getController().getCurrentAnimation() == null)
			return false;
		
		if(event.getController().getCurrentAnimation().loop == ILoopType.EDefaultLoopTypes.PLAY_ONCE)
			return event.getController().getAnimationState() == AnimationState.Running;
		else
			return false;
	}
	
	public enum Animation //animationName set in assets/minestuck/animations/lotus_flower.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("animation.ServerCursorModel.idle", true),
		CLICK("animation.ServerCursorModel.click", false),
		REJECTED("animation.ServerCursorModel.rejected", false),
		LOADING("animation.ServerCursorModel.loading", true);
		
		private final String animationName;
		private final boolean looping;
		
		Animation(String animationName, boolean looping)
		{
			this.animationName = animationName;
			this.looping = looping;
		}
	}
}
