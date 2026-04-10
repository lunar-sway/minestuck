package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class KernelspriteEntity extends PathfinderMob implements DialogueEntity, FlyingAnimal
{
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(KernelspriteEntity.class, EntityDataSerializers.INT);
	
	private final DialogueComponent dialogueComponent = new DialogueComponent(this);
	
	@Nullable
	private BlockPos boundOrigin;
	
	protected KernelspriteEntity(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level);
		setInvulnerable(true);
		setPersistenceRequired();
		
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
		this.goalSelector.addGoal(8, new RandomMoveGoal());
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
		compound.put("dialogue", dialogueComponent.write());
		
		if (this.boundOrigin != null) {
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
		dialogueComponent.read(compound.getCompound("dialogue"));
		
		if (compound.contains("BoundX")) {
			this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
		}
	}
	
	@Nullable
	public BlockPos getBoundOrigin() {
		return this.boundOrigin;
	}
	
	public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
		this.boundOrigin = boundOrigin;
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
	
	class StayPutGoal extends Goal
	{
		private int timer;
		
		@Override
		public boolean canUse()
		{
			return timer > 0;
		}
		
		@Override
		public void start()
		{
			super.start();
			timer = 600;
		}
		
		@Override
		public void stop()
		{
			super.stop();
			KernelspriteEntity.this.goalSelector.removeGoal(this);
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
		public RandomMoveGoal()
		{
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}
		
		@Override
		public boolean canUse()
		{
			return !KernelspriteEntity.this.getMoveControl().hasWanted() && KernelspriteEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
		}
		
		@Override
		public boolean canContinueToUse()
		{
			return false;
		}
		
		@Override
		public void tick()
		{
			BlockPos blockpos = KernelspriteEntity.this.getBoundOrigin();
			if(blockpos == null)
			{
				blockpos = KernelspriteEntity.this.blockPosition();
			}
			
			for(int i = 0; i < 3; i++)
			{
				BlockPos blockpos1 = blockpos.offset(KernelspriteEntity.this.random.nextInt(15) - 7, KernelspriteEntity.this.random.nextInt(11) - 5, KernelspriteEntity.this.random.nextInt(15) - 7);
				if(KernelspriteEntity.this.level().isEmptyBlock(blockpos1))
				{
					KernelspriteEntity.this.moveControl
							.setWantedPosition((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 0.25);
					if(KernelspriteEntity.this.getTarget() == null)
					{
						KernelspriteEntity.this.getLookControl()
								.setLookAt((double) blockpos1.getX() + 0.5, (double) blockpos1.getY() + 0.5, (double) blockpos1.getZ() + 0.5, 180.0F, 20.0F);
					}
					break;
				}
			}
		}
	}
}
