package com.mraof.minestuck.entity;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class LotusFlowerEntity extends CreatureEntity implements IAnimatable
{
	
	private AnimationFactory factory = new AnimationFactory(this);
	
	public int eventTimer = -1;
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		if(eventTimer == -1)
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.idle", true));
		if(eventTimer == 10000)
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open", false));
		if(eventTimer == 9880) //6 sec open animation * 20 ticks/sec, 10000 - 120 = 9880
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.open.idle", true));
		if(eventTimer == 9560) //4 sec idle animation * 4 loops * 20 ticks/sec, 9880 - 320 = 9560
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.vanish", false));
		if(eventTimer == 9547) //0.65 sec vanish animation * 20 ticks/sec, 9560 - 13 = 9547
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.empty", true));
		
		return PlayState.CONTINUE;
	}
	
	public LotusFlowerEntity(EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.ignoreFrustumCheck = true;
		entityCollisionReduction = 0F;
		this.setInvulnerable(true);
		this.setNoAI(true);
		this.enablePersistence();
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
		if(this.isAlive() && !player.isSneaking() && eventTimer < 0)
		{
			Vec3d posVec = this.getPositionVec();
			
			player.world.playSound(posVec.getX(), posVec.getY(), posVec.getZ(), SoundEvents.BLOCK_COMPOSTER_READY, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
			setLotusActivatedTimer();
			
			return true;
		}
		if(this.isAlive() && eventTimer > 2 && eventTimer <= 9547)
		{
			ItemStack itemstack = player.getHeldItem(hand);
			
			if(player.getDistanceSq(this) < 9.0D && itemstack.getItem() == Items.BONE_MEAL && player.isCreative())
			{
				Vec3d posVec = this.getPositionVec();
				eventTimer = 2;
				for(int i = 0; i < 10; i++)
				{
					this.world.addParticle(ParticleTypes.COMPOSTER, posVec.x, posVec.y + 0.5D, posVec.z, 0.5D - this.rand.nextDouble(), 0.5D - this.rand.nextDouble(), 0.5D - this.rand.nextDouble());
				}
			}
			
			if(!world.isRemote && itemstack.getItem() != Items.BONE_MEAL)
			{
				ITextComponent message = new TranslationTextComponent("There are no petals on this plant, maybe it will regrow?");
				player.sendMessage(message);
			}
			
			return true;
		} else
			return super.processInteract(player, hand);
	}
	
	protected void setLotusActivatedTimer()
	{
		eventTimer = 10001;
	}
	
	protected void spawnLoot()
	{
		World worldIn = this.world;
		Vec3d posVec = this.getPositionVec();
		
		ItemEntity unpoweredComputerItemEntity = new ItemEntity(worldIn, posVec.getX(), posVec.getY() + 1D, posVec.getZ(), new ItemStack(MSItems.COMPUTER_PARTS, 1));
		worldIn.addEntity(unpoweredComputerItemEntity);
		
		/*ItemEntity blankDiskItemEntity = new ItemEntity(worldIn, posVec.getX(), posVec.getY() + 1D, posVec.getZ(), new ItemStack(MSItems.BLANK_DISK, 2));
		worldIn.addEntity(blankDiskItemEntity);*/
		
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
		
		if(eventTimer >= 0)
			eventTimer--;
		
		if(eventTimer == 9880)
			spawnLoot();
		
		if(eventTimer == 0)
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
		
		compound.putInt("eventTimer", eventTimer);
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		
		eventTimer = compound.getInt("eventTimer");
	}
}