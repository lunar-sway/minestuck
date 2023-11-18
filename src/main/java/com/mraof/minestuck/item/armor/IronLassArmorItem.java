package com.mraof.minestuck.item.armor;

import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class IronLassArmorItem extends ArmorItem
{
	public IronLassArmorItem(ArmorMaterial mat, ArmorItem.Type slot, Properties props)
	{
		super(mat, slot, props);
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
				player.level().playSound(player, player.blockPosition(), MSSoundEvents.ITEM_JETPACK_FLIGHT.get(), SoundSource.PLAYERS, 1, player.getRandom().nextFloat()+0.35f);
				player.level().addParticle(MSParticleType.EXHAUST.get(), player.getX(), player.getY(), player.getZ(), 0, 0, 0);
				
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
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
}
