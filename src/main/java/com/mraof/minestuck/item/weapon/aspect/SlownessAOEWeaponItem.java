package com.mraof.minestuck.item.weapon.aspect;

import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.PotionWeaponItem;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class SlownessAOEWeaponItem extends PotionWeaponItem
{
	private final EnumAspect aspect;
	
	public SlownessAOEWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EffectInstance effect, EnumAspect aspect, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, effect, true, toolType, builder);
		this.aspect = aspect;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if(attacker instanceof ServerPlayerEntity)
		{
			Title title = PlayerSavedData.getData((ServerPlayerEntity) attacker).getTitle();
			
			if(title != null && attacker.getRNG().nextFloat() < .1)
			{
				if(title.getHeroAspect() == aspect){
					AxisAlignedBB axisalignedbb = attacker.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
					List<LivingEntity> list = attacker.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
					if (!list.isEmpty()) {
						for(LivingEntity livingentity : list) {
							double d0 = attacker.getDistanceSq(livingentity);
							if (d0 < 16.0D) {
								attacker.world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1.5F, 2F);
								if(livingentity != attacker)
									livingentity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 4));
							}
						}
					}
				}
			}
		}
		return super.hitEntity(stack, target, attacker);
	}
}