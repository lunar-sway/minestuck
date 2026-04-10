package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;
import java.util.EnumSet;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class KernelspriteEntity extends PathfinderMob implements DialogueEntity, FlyingAnimal
{
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(KernelspriteEntity.class, EntityDataSerializers.INT);
	
	private final DialogueComponent dialogueComponent = new DialogueComponent(this);
	private final RandomMoveGoal randomMoveGoal = new RandomMoveGoal(this);
	
	@Nullable
	private BlockPos boundOrigin;
	private PlayerIdentifier owner;
	
	public KernelspriteEntity(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level);
		setInvulnerable(true);
		setPersistenceRequired();
		this.goalSelector.addGoal(8, this.randomMoveGoal);
		
		this.moveControl = new KernelspriteMoveControl(this);
		
		dialogueComponent.setDialogue(Minestuck.id("individual/kernelsprite/start"), true);
	}
	
	public static AttributeSupplier.Builder kernelspriteAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80);
	}
	
	@Override
	protected MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
	@Override
	public boolean canBeLeashed()
	{
		return super.canBeLeashed();
	}
	
	@Override
	public void move(MoverType type, Vec3 pos)
	{
		super.move(type, pos);
		this.checkInsideBlocks();
	}
	
	@Override
	public void tick()
	{
		//using Vex as base
		noPhysics = true;
		super.tick();
		noPhysics = false;
		setNoGravity(true);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		super.defineSynchedData(builder);
		builder.define(COLOR, ChatFormatting.BLUE.getColor());
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		boolean isBusy = this.goalSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning)
				.anyMatch(goal -> goal.getGoal() instanceof MoveToBlockGoal);
		
		if(!this.isAlive() || player.isShiftKeyDown() || isBusy)
			return InteractionResult.PASS;
		
		if(!(player instanceof ServerPlayer serverPlayer))
			return InteractionResult.SUCCESS;
		
		if(this.dialogueComponent.hasAnyOngoingDialogue())
			return InteractionResult.FAIL;
		
		this.dialogueComponent.tryStartDialogue(serverPlayer);
		this.goalSelector.addGoal(7, new StayPutGoal());
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
	}
	
	@Override
	public float getLightLevelDependentMagicValue()
	{
		return 1.0F;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("color", this.getColor());
		owner.saveToNBT(compound, "owner");
		compound.put("dialogue", dialogueComponent.write());
		
		if(this.boundOrigin != null)
		{
			compound.putInt("BoundX", this.boundOrigin.getX());
			compound.putInt("BoundY", this.boundOrigin.getY());
			compound.putInt("BoundZ", this.boundOrigin.getZ());
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		this.setColor(compound.getInt("color"));
		this.owner = IdentifierHandler.load(compound, "owner").result().orElse(null);
		dialogueComponent.read(compound.getCompound("dialogue"));
		
		if(compound.contains("BoundX"))
		{
			this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
		}
	}
	
	@Nullable
	public BlockPos getBoundOrigin()
	{
		return this.boundOrigin;
	}
	
	public void setBoundOrigin(@Nullable BlockPos boundOrigin)
	{
		this.boundOrigin = boundOrigin;
	}
	
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
	
	public void setOwner(PlayerIdentifier owner)
	{
		this.owner = owner;
	}
	
	public void setColor(int i)
	{
		this.entityData.set(COLOR, i);
	}
	
	public int getColor()
	{
		return this.entityData.get(COLOR);
	}
	
	@Override
	public DialogueComponent getDialogueComponent()
	{
		return dialogueComponent;
	}
	
	@Override
	public ChatFormatting getChatColor()
	{
		return ChatFormatting.BLACK;
		//return getColor();
	}
	
	@Override
	public String getSpriteType()
	{
		return this.getType().builtInRegistryHolder().getKey().location().getPath();
	}
	
	@Override
	public boolean isFlying()
	{
		return true;
	}
	
	class KernelspriteMoveControl extends MoveControl
	{
		public KernelspriteMoveControl(KernelspriteEntity kernelsprite) {
			super(kernelsprite);
		}
		
		@Override
		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vec3 = new Vec3(this.wantedX - KernelspriteEntity.this.getX(), this.wantedY - KernelspriteEntity.this.getY(), this.wantedZ - KernelspriteEntity.this.getZ());
				double d0 = vec3.length();
				if (d0 < KernelspriteEntity.this.getBoundingBox().getSize()) {
					this.operation = MoveControl.Operation.WAIT;
					KernelspriteEntity.this.setDeltaMovement(KernelspriteEntity.this.getDeltaMovement().scale(0.5));
				} else {
					KernelspriteEntity.this.setDeltaMovement(KernelspriteEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05 / d0)));
					if (KernelspriteEntity.this.getTarget() == null) {
						Vec3 vec31 = KernelspriteEntity.this.getDeltaMovement();
						KernelspriteEntity.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180.0F / (float)Math.PI));
						KernelspriteEntity.this.yBodyRot = KernelspriteEntity.this.getYRot();
					} else {
						double d2 = KernelspriteEntity.this.getTarget().getX() - KernelspriteEntity.this.getX();
						double d1 = KernelspriteEntity.this.getTarget().getZ() - KernelspriteEntity.this.getZ();
						KernelspriteEntity.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180.0F / (float)Math.PI));
						KernelspriteEntity.this.yBodyRot = KernelspriteEntity.this.getYRot();
					}
				}
			}
		}
	}
	
	class StayPutGoal extends Goal
	{
		private int timer = -1;
		
		@Override
		public boolean canUse()
		{
			return timer > 0 || timer == -1;
		}
		
		@Override
		public void start()
		{
			super.start();
			timer = 600;
			KernelspriteEntity.this.goalSelector.removeGoal(randomMoveGoal);
		}
		
		@Override
		public void stop()
		{
			super.stop();
			timer = -1;
			KernelspriteEntity.this.goalSelector.removeGoal(this);
			KernelspriteEntity.this.goalSelector.addGoal(8, randomMoveGoal);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public void tick()
		{
			super.tick();
			timer--;
		}
	}
	
	/**
	 * Built directly off {@link net.minecraft.world.entity.monster.Vex.VexRandomMoveGoal}
	 */
	class RandomMoveGoal extends Goal
	{
		private final KernelspriteEntity entity;
		
		public RandomMoveGoal(KernelspriteEntity entity)
		{
			this.entity = entity;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean canUse()
		{
			return !entity.getMoveControl().hasWanted() && entity.random.nextInt(reducedTickDelay(7)) == 0;
		}
		
		@Override
		public void tick()
		{
			BlockPos blockpos = entity.getBoundOrigin();
			if(blockpos == null)
			{
				blockpos = entity.blockPosition();
			}
			
			for(int i = 0; i < 3; i++)
			{
				BlockPos blockpos1 = blockpos.offset(entity.random.nextInt(15) - 7, entity.random.nextInt(11) - 5, entity.random.nextInt(15) - 7);
				if(entity.level().isEmptyBlock(blockpos1))
				{
					entity.moveControl
							.setWantedPosition((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 0.25);
					if(entity.getTarget() == null)
					{
						entity.getLookControl()
								.setLookAt((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}
	
	@SubscribeEvent
	private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PlayerData playerData = PlayerData.get(player).orElseThrow();
		
		if(playerData.getExistingData(MSAttachments.HAS_KERNELSPRITE).isEmpty())
			playerData.setData(MSAttachments.HAS_KERNELSPRITE, false);
	}
}
