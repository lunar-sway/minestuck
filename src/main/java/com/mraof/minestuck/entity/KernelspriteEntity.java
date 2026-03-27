package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ai.attack.MoveToTargetGoal;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KernelspriteEntity extends PathfinderMob implements DialogueEntity
{
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(KernelspriteEntity.class, EntityDataSerializers.INT);
	
	private final DialogueComponent dialogueComponent = new DialogueComponent(this);
	
	protected KernelspriteEntity(EntityType<? extends PathfinderMob> entityType, Level level)
	{
		super(entityType, level);
		dialogueComponent.setDialogue(Minestuck.id("individual/kernelsprite/start"), true);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder)
	{
		super.defineSynchedData(builder);
		builder.define(COLOR, ChatFormatting.AQUA.getColor());
	}
	
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		boolean isBusy = this.goalSelector.getAvailableGoals().stream().filter(WrappedGoal::isRunning)
				.anyMatch(goal -> goal.getGoal() instanceof MoveToTargetGoal);
		
		if(!this.isAlive() || player.isShiftKeyDown() || isBusy)
			return InteractionResult.PASS;
		
		if(!(player instanceof ServerPlayer serverPlayer))
			return InteractionResult.SUCCESS;
		
		if(this.dialogueComponent.hasAnyOngoingDialogue())
			return InteractionResult.FAIL;
		
		this.dialogueComponent.tryStartDialogue(serverPlayer);
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("Color", this.getColor());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		
		this.setColor(compound.getInt("Color"));
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
		return ChatFormatting.AQUA;
		//return getColor();
	}
	
	@Override
	public String getSpriteType()
	{
		return this.getType().builtInRegistryHolder().getKey().location().getPath();
	}
}
