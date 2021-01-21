package com.mraof.minestuck.item.weapon.aspect;

import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TeleportingWeaponItem extends WeaponItem
{
	public TeleportingWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
	{
		boolean critical = attacker.fallDistance > 0.0F && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(Effects.BLINDNESS) && !attacker.isPassenger() && !attacker.isBeingRidden();
		
		if(attacker instanceof ServerPlayerEntity && critical)
		{
			Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
			if(title != null)
			{
				if(title.getHeroAspect() == EnumAspect.SPACE)
				{
					double oldPosX = attacker.getPosX();
					double oldPosY = attacker.getPosY();
					double oldPosZ = attacker.getPosZ();
					World worldIn = attacker.world;
					
					for(int i = 0; i < 16; ++i)
					{
						double newPosX = attacker.getPosX() + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
						double newPosY = MathHelper.clamp(attacker.getPosY() + (double) (attacker.getRNG().nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
						double newPosZ = attacker.getPosZ() + (attacker.getRNG().nextDouble() - 0.5D) * 16.0D;
						if(attacker.isPassenger())
						{
							attacker.stopRiding();
						}
						
						if(attacker.attemptTeleport(newPosX, newPosY, newPosZ, true))
						{
							worldIn.playSound(null, oldPosX, oldPosY, oldPosZ, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
							attacker.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
							attacker.lookAt(attacker.getCommandSource().getEntityAnchorType(), target.getPositionVec());
							break;
						}
					}
				}
			}
		}
		return super.hitEntity(itemStack, target, attacker);
	}
}
