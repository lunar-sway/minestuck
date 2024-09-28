package com.mraof.minestuck.entity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.network.LotusFlowerAnimationPacket;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LotusFlowerEntity extends Entity implements GeoEntity, IEntityWithComplexSpawn
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	// Animation lengths
	private static final int OPENING_LENGTH = 120;        //6 sec open animation * 20 ticks/sec = 120
	private static final int GROWTH_LENGTH = 21;		//1.04 sec grow animation * 20 ticks/sec = ~21
	private static final int OPEN_IDLE_LENGTH = 240;    //4 sec idle animation * 3 loops * 20 ticks/sec = 240
	private static final int VANISHING_LENGTH = 13;        //0.65 sec vanish animation * 20 ticks/sec = 13
	
	//because eventTimer can be of arbitrary length, the Idle and Growth animations occupy negative values
	//Growth (-23 to -2) -> Idle (-1) -> Open (0 to 119) -> Open Idle (120 to 359) -> Vanish (360 to 372) -> Empty (373 to 6000 by default)
	
	// Animation start times
	private static final int IDLE_TIME = -1;
	private static final int GROWTH_STOP = IDLE_TIME - 1;
	private static final int GROWTH_START = GROWTH_STOP - GROWTH_LENGTH;
	private static final int OPEN_START = 0;
	private static final int OPEN_IDLE_START = OPEN_START + OPENING_LENGTH;
	private static final int VANISH_START = OPEN_IDLE_START + OPEN_IDLE_LENGTH;
	private static final int ANIMATION_END = VANISH_START + VANISHING_LENGTH;
	
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
	//Only used server-side. Used to track the flower state and the progression of the animation
	private int eventTimer = IDLE_TIME;
	
	public static final String REGROW = "minestuck.lotusflowerentity.regrow";
	
	// Specifies the current animation phase
	@Nonnull
	private Animation animationType = Animation.IDLE;
	
	protected LotusFlowerEntity(EntityType<? extends LotusFlowerEntity> type, Level level)
	{
		super(type, level);
		
		setInvulnerable(true);
	}
	
	private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state)
	{
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
	public boolean isPickable()
	{
		return true;
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand)
	{
		if(isAlive() && !player.isShiftKeyDown() && animationType == Animation.IDLE)
		{
			startLotusAnimation();
			return InteractionResult.SUCCESS;
		}
		if(isAlive() && animationType == Animation.EMPTY)
		{
			ItemStack itemstack = player.getItemInHand(hand);
			
			if(player.distanceToSqr(this) < 36 && itemstack.is(Items.BONE_MEAL) && player.isCreative())
			{
				restoreFromBonemeal();
			} else if(level().isClientSide && player.distanceToSqr(this) < 36)
			{
				player.sendSystemMessage(Component.translatable(REGROW));
			}
			
			return InteractionResult.SUCCESS;
		} else
			return super.interact(player, hand);
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		if(!level().isClientSide)
		{
			if(animationType != Animation.IDLE)
				setEventTimer(eventTimer + 1);
			
			if(eventTimer == OPEN_IDLE_START)
				spawnLoot();
			else if(eventTimer >= MinestuckConfig.SERVER.lotusRestorationTime.get() * 20) //300(default) seconds from animation start to flower restoration
				setEventTimer(GROWTH_START);
			else if(eventTimer == GROWTH_STOP)
				setEventTimer(IDLE_TIME);
		}
	}
	
	private void startLotusAnimation()
	{
		if(!level().isClientSide)
		{
			setEventTimer(OPEN_START);
			
			Vec3 posVec = position();
			level().playSound(null, posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_OPEN.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
		}
	}
	
	private void restoreFromBonemeal()
	{
		if(!level().isClientSide)
			setEventTimer(GROWTH_START);
		
		Vec3 posVec = position();
		for(int i = 0; i < 10; i++)
			this.level().addParticle(ParticleTypes.COMPOSTER, posVec.x, posVec.y + 0.5, posVec.z, 0.5 - random.nextDouble(), 0.5 - random.nextDouble(), 0.5 - random.nextDouble());
	}
	
	private void setEventTimer(int time)
	{
		if(level().isClientSide)
			throw new IllegalStateException("Shouldn't call setEventTimer client-side!");
		
		eventTimer = time;
		
		Animation newAnimation = animationFromEventTimer();
		if(newAnimation != animationType)
			updateAndSendAnimation(newAnimation);
	}
	
	private Animation animationFromEventTimer()
	{
		if(eventTimer >= ANIMATION_END)
			return Animation.EMPTY;
		else if(eventTimer >= VANISH_START)
			return Animation.VANISH;
		else if(eventTimer >= OPEN_IDLE_START)
			return Animation.OPEN_IDLE;
		else if(eventTimer >= OPEN_START)
			return Animation.OPEN;
		else if(eventTimer < IDLE_TIME)
			return Animation.GROW;
		else
			return Animation.IDLE;
	}
	
	protected void updateAndSendAnimation(Animation animation)
	{
		this.animationType = animation;
		LotusFlowerAnimationPacket packet = LotusFlowerAnimationPacket.createPacket(this, animation); //this packet allows information to be exchanged between server and client where one side cant access the other easily or reliably
		PacketDistributor.TRACKING_ENTITY.with(this).send(packet);
	}
	
	/**
	 * Spawns loot from the LOTUS_FLOWER_DEFAULT loot table
	 */
	protected void spawnLoot()
	{
		if(!level().isClientSide)
		{
			ServerLevel serverLevel = (ServerLevel) level();
			
			LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(MSLootTables.LOTUS_FLOWER_DEFAULT);
			List<ItemStack> loot = lootTable.getRandomItems(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY));
			if(loot.isEmpty())
				LOGGER.warn("Tried to generate loot for Lotus Flower, but no items were generated!");
			
			for(ItemStack itemStack : loot)
			{
				this.spawnAtLocation(itemStack, 1F);
			}
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		compound.putInt("EventTimer", eventTimer);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		if(compound.contains("EventTimer", Tag.TAG_ANY_NUMERIC))
		{
			eventTimer = compound.getInt("EventTimer");
			animationType = animationFromEventTimer();
		}
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(animationType);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		animationType = additionalData.readEnum(Animation.class);
	}
	
	public void setAnimationFromPacket(Animation newAnimation)
	{
		if(level().isClientSide) //allows client-side effects tied to server-side events
		{
			animationType = newAnimation;
			if(animationType == Animation.IDLE)
				addRestoreEffects();
			if(animationType == Animation.OPEN_IDLE)
				addLootSpawnEffects();
		}
	}
	
	protected void addRestoreEffects()
	{
		Vec3 posVec = this.position();
		this.level().addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
		this.level().playLocalSound(posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_RESTORE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
	}
	
	protected void addLootSpawnEffects()
	{
		Vec3 posVec = this.position();
		this.level().addParticle(ParticleTypes.FLASH, posVec.x, posVec.y + 0.5D, posVec.z, 0.0D, 0.0D, 0.0D);
		this.level().playLocalSound(posVec.x(), posVec.y(), posVec.z(), MSSoundEvents.EVENT_LOTUS_FLOWER_LOOT_SPAWN.get(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
	}
	
	@Override
	protected MovementEmission getMovementEmission()
	{
		return Entity.MovementEmission.NONE;
	}
	
	@Override
	protected void defineSynchedData()
	{
	
	}
	
	@Override
	public void move(MoverType typeIn, Vec3 pos)
	{
	}
	
	public enum Animation //animationName set in assets/minestuck/animations/lotus_flower.animation.json. Animated blocks/entities also need a section in assets/minestuck/geo
	{
		IDLE("lotus.idle"),
		OPEN("lotus.open"),
		OPEN_IDLE("lotus.open.idle"),
		VANISH("lotus.vanish"),
		EMPTY("lotus.bud"),
		GROW("lotus.grow");
		
		private final RawAnimation animation;
		
		Animation(String animationName)
		{
			this.animation = RawAnimation.begin().thenLoop(animationName);
		}
	}
}
