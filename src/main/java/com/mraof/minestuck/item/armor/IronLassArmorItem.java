package com.mraof.minestuck.item.armor;

import com.mraof.minestuck.client.model.armor.IronLassArmorModel;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

import static software.bernie.geckolib.constant.DefaultAnimations.FLY;
import static software.bernie.geckolib.constant.DefaultAnimations.IDLE;

public class IronLassArmorItem extends ArmorItem implements GeoItem
{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	
	public IronLassArmorItem(ArmorMaterial mat, ArmorItem.Type slot, Properties props)
	{
		super(mat, slot, props);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}
	
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		return true;
	}
	
	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		if(entity instanceof Player player)
		{
			if(player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronLassArmorItem && player.isShiftKeyDown())
			{
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
				
				player.level().playSound(player, player.blockPosition(), MSSoundEvents.ITEM_JETPACK_FLIGHT.get(), SoundSource.PLAYERS, 1, player.getRandom().nextFloat()+0.35f);
				player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX(), player.getY(), player.getZ(), 0, 0, 0);
				player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX()-movement.x/2, player.getY()-movement.y/2, player.getZ()-movement.z/2, 0, 0, 0);
				
				player.setDeltaMovement(movement.add(
						look.x * 0.1D + (look.x * 1.5D - movement.x) * 0.2D,
						look.y * 0.1D + (look.y * 1.5D - movement.y) * 0.2D,
						look.z * 0.1D + (look.z * 1.5D - movement.z) * 0.2D));
			}
			
			// damage gear
			if (!player.level().isClientSide)
			{
				// chest every 20 ticks
				if((player.getFallFlyingTicks() + 1) % 20 == 0)
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
				// shoes every 5 ticks when boosting
				ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
				if(feet.getItem() instanceof IronLassArmorItem && player.isShiftKeyDown() && (player.getFallFlyingTicks() + 1) % 5 == 0)
					feet.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.FEET));
			}
		}
		return true;
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			private GeoArmorRenderer<?> renderer;
			
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				if(this.renderer == null)
					this.renderer = new GeoArmorRenderer<>(new IronLassArmorModel());
				
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
				return this.renderer;
			}
		});
	}
	
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar)
	{
		
		controllerRegistrar.add(new AnimationController<>(this, "Fly/Idle", 3, state ->
		{
			Entity entity = state.getData(DataTickets.ENTITY);
			
			if(entity instanceof LivingEntity livingEntity && livingEntity.isFallFlying())
				return state.setAndContinue(FLY);
			else
				return state.setAndContinue(IDLE);
		}
		));
	}
	
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}
}
