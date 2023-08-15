package com.mraof.minestuck.item.armor;

import com.mojang.logging.LogUtils;
import com.mraof.minestuck.util.MSParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class IronLassArmorItem extends ArmorItem
{
	public static final Logger LOGGER = LogUtils.getLogger();
	
	public IronLassArmorItem(ArmorMaterial mat, ArmorItem.Type slot, Properties props)
	{
		super(mat, slot, props);
	}
	
	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity)
	{
		return entity instanceof Player;
	}
	
	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks)
	{
		if (entity instanceof Player player) {
			if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronLassArmorItem && player.isShiftKeyDown()) {
				player.level().playSound(player, player.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1, 0);
				
				player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX(), player.getY(), player.getZ(), 0, 0, 0);
				
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
				player.setDeltaMovement(movement.add(
						look.x * 0.1D + (look.x * 1.5D - movement.x) * 0.2D,
						look.y * 0.1D + (look.y * 1.5D - movement.y) * 0.2D,
						look.z * 0.1D + (look.z * 1.5D - movement.z) * 0.2D));
			}
			
			// damage gear
			if (!player.level().isClientSide && (player.getFallFlyingTicks() + 1) % 20 == 0) {
				stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.CHEST));
				if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronLassArmorItem)
					player.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlot.FEET));
			}
		}
		
		return true;
	}
}
