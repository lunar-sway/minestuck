package com.mraof.minestuck.item.armor;

import com.mojang.logging.LogUtils;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class JetPackItem extends ArmorItem
{
	
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public JetPackItem(ArmorMaterial mat, ArmorItem.Type slot, Properties props)
	{
		super(mat, slot, props);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack thrustController = new ItemStack(MSItems.THRUST_CONTROLLER.get());
		
		player.getInventory().add(thrustController);
		
		return super.use(level, player, hand);
	}
	
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		Item thrustController = MSItems.THRUST_CONTROLLER.get();
		
		Boolean hasController = entity.getItemInHand(InteractionHand.MAIN_HAND).is(thrustController);
		Boolean hasControllerOffhand =  entity.getItemInHand(InteractionHand.OFF_HAND).is(thrustController);
		
		if(hasController && hasControllerOffhand && isBoostingTagTrue(stack))
		{
			return entity instanceof Player;
		}
		
		return false;
	}
	
	public boolean isBoostingTagTrue(ItemStack stack)
	{
		CompoundTag nbt = stack.getTag();
		
		return nbt != null && nbt.contains("is_boosting") && nbt.getBoolean("is_boosting");
	}
	
	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		if (entity instanceof Player player && isBoostingTagTrue(stack))
		{
			boost(player);
			damageGear(player, stack);
			setIsBoostingFalse(player);
		}
		
		return true;
	}
	
	public static void damageGear(Player player, ItemStack stack)
	{
		Item footItem = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
		
		if (!player.level().isClientSide && (player.getFallFlyingTicks() + 1) % 20 == 0) {
			stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
			if (footItem instanceof JetPackItem)
				player.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.FEET));
		}
	}
	
	public static void boost(Player player)
	{
		Item chestItem = player.getItemBySlot(EquipmentSlot.CHEST).getItem();
		
		if (chestItem instanceof JetPackItem)
		{
			player.level().addParticle(MSParticleType.EXHAUST.get(),
					player.getX(), player.getY(), player.getZ(),
					0, 0, 0);
			
			Vec3 look = player.getLookAngle();
			Vec3 movement = player.getDeltaMovement();
			player.setDeltaMovement(movement.add(
					look.x * 0.1D + (look.x * 1.5D - movement.x) * 0.2D,
					look.y * 0.1D + (look.y * 1.5D - movement.y) * 0.2D,
					look.z * 0.1D + (look.z * 1.5D - movement.z) * 0.2D));
		}
	}
	
	public static void setIsBoostingFalse(Player player)
	{
		ItemStack jetpackItem = new ItemStack(player.getItemBySlot(EquipmentSlot.CHEST).getItem());
		
		jetpackItem.getOrCreateTag().putBoolean("is_boosting", false);
	}
}
