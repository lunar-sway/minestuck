package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
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

public class LotusFlowerEntity extends CreatureEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.idle", true));
		return PlayState.CONTINUE;
	}
	
	public LotusFlowerEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
		this.ignoreFrustumCheck = true;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	@Override
	protected void registerGoals() {
		//this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		super.registerGoals();
	}
}

/*public class LotusFlowerEntity extends Entity implements IAnimatable
{
	private AnimationFactory factory = new AnimationFactory(this);
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("lotus.idle", true));
		return PlayState.CONTINUE;
	}
	
	public int age = 0;
	
	//public float animationOffset;
	
	public LotusFlowerEntity(World world, double x, double y, double z)
	{
		this(MSEntityTypes.LOTUS_FLOWER, world, x, y, z);
	}
	
	protected LotusFlowerEntity(EntityType<? extends LotusFlowerEntity> type, World world, double x, double y, double z)
	{
		super(type, world);
		this.setPosition(x, y, z);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.setMotion(world.rand.nextGaussian() * 0.2D - 0.1D, world.rand.nextGaussian() * 0.2D, world.rand.nextGaussian() * 0.2D - 0.1D);
	}

	public LotusFlowerEntity(EntityType<? extends LotusFlowerEntity> type, World world)
	{
		super(type, world);
		//animationOffset = (float) (Math.random() * Math.PI * 2.0D);
	}
	
	/*@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand)
	{
		return super.applyPlayerInteraction(player, vec, hand);
	}*/
	
	/*@Override
	protected void registerData()
	{
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.putShort("age", (short)this.age);
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.age = compound.getShort("age");
	}
	
	@Override
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public void registerControllers(AnimationData animationData)
	{
		animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
}*/