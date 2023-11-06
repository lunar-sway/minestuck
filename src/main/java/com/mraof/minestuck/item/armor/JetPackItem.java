package com.mraof.minestuck.item.armor;

import com.mojang.logging.LogUtils;
import com.mraof.minestuck.client.renderer.armor.JetpackModelRenderer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class JetPackItem extends ArmorItem implements GeoItem
{
	private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
	private int EXPLOSION_BUFFER = 0;
	
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public JetPackItem(ArmorMaterial mat, ArmorItem.Type slot, Properties props)
	{
		super(mat, slot, props);
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand)
	{
		ItemStack thrustController = new ItemStack(MSItems.THRUST_CONTROLLER.get());
		
		if(player.getInventory().getFreeSlot() <= 2)
		{
			player.getInventory().add(thrustController);
		}
		
		return super.use(level, player, hand);
	}
	
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		ItemStack jetpackItemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
		
		if(isHoldingControllers(entity) && isBoostingTagTrue(jetpackItemStack))
		{
			return entity instanceof Player;
		}
		
		return false;
	}
	
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected)
	{
		
		if(pEntity instanceof Player player && isBoostingTagTrue(pStack) && player.onGround() && EXPLOSION_BUFFER <= 40)
		{
			explodeAndStop(player, pLevel);
			EXPLOSION_BUFFER = 0;
		}
		if(pEntity instanceof Player player && isBoostingTagTrue(pStack) && isHoldingControllers((LivingEntity) pEntity))
		{
			boost(player);
			player.startFallFlying();
			EXPLOSION_BUFFER += 1;
		}
		if(!isBoostingTagTrue(pStack))
		{
			EXPLOSION_BUFFER = 0;
		}
		
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
	}
	
	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		if (entity instanceof Player player && isBoostingTagTrue(stack))
		{
			damageGear(player, stack);
		}
		
		return true;
	}
	
	public static void damageGear(Player player, ItemStack stack)
	{
		Item footItem = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
		
		if (!player.level().isClientSide && (player.getFallFlyingTicks() + 1) % 20 == 0) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
			if (footItem instanceof JetPackItem)
				player.getItemBySlot(EquipmentSlot.CHEST).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
		}
	}
	
	public static void explodeAndStop(Player player, Level level)
	{
		ItemStack jetpackItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
		
		jetpackItemStack.getOrCreateTag().putBoolean("is_boosting", false);
		level.explode(player,player.getX() + 0.5D, player.getY() + 0.5D, player.getZ() + 0.5D, 3F, Level.ExplosionInteraction.BLOCK);
	}
	
	public boolean isHoldingControllers( LivingEntity entity)
	{
		Item thrustController = MSItems.THRUST_CONTROLLER.get();
		
		boolean hasController = entity.getItemInHand(InteractionHand.MAIN_HAND).is(thrustController);
		boolean hasControllerOffhand =  entity.getItemInHand(InteractionHand.OFF_HAND).is(thrustController);
		if(hasController && hasControllerOffhand)
		{
			return true;
		}
		return false;
	}
	
	public static void boost(Player player)
	{
		Item chestItem = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
		if (chestItem instanceof JetPackItem)
		{
			player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX(), player.getY() + 0.8, player.getZ() - 0.3, 0, 0, 0);
			player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX(), player.getY() + 0.8, player.getZ() + 0.3, 0, 0, 0);
			player.level().playSound(player, player.blockPosition(), MSSoundEvents.JETPACK_THRUST.get(), SoundSource.PLAYERS, 1, 0);
			
			Vec3 look = player.getLookAngle();
			Vec3 movement = player.getDeltaMovement();
			player.setDeltaMovement(movement.add(
					look.x * 0.2D + (look.x * 2D - movement.x) * 0.3D,
					look.y * 0.2D + (look.y * 2D - movement.y) * 0.3D,
					look.z * 0.2D + (look.z * 2D - movement.z) * 0.3D));
		}
	}
	
	public boolean isBoostingTagTrue(ItemStack stack)
	{
		CompoundTag nbt = stack.getTag();
		
		return nbt != null && nbt.contains("is_boosting") && nbt.getBoolean("is_boosting");
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			private JetpackModelRenderer renderer;
			
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				if (this.renderer == null)
					this.renderer = new JetpackModelRenderer();
				
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
				return this.renderer;
			}
		});
	}
	
	private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state)
	{
		ItemStack jetpack = this.getDefaultInstance();
		if(this.isBoostingTagTrue(jetpack))
		{
			state.getController().setAnimation(RawAnimation.begin().then("jetpack.flight", software.bernie.geckolib.core.animation.Animation.LoopType.LOOP));
			return PlayState.CONTINUE;
		}
		
		state.getController().setAnimation(RawAnimation.begin().then("jetpack.idle", software.bernie.geckolib.core.animation.Animation.LoopType.LOOP));
		return PlayState.CONTINUE;
	}
	
	public enum Animation
	{
		IDLE("jetpack.idle"),
		FLIGHT("jetpack.flight");
		
		private final RawAnimation animation;
		
		Animation(String animationName)
		{
			this.animation = RawAnimation.begin().thenLoop(animationName);
		}
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
}
