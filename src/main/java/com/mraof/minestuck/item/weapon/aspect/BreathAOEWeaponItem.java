package com.mraof.minestuck.item.weapon.aspect;

import com.mraof.minestuck.item.weapon.KnockbackWeaponItem;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class BreathAOEWeaponItem extends KnockbackWeaponItem
{
	private final EnumAspect aspect;
	
	public BreathAOEWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, EnumAspect aspect, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
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
								attacker.world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.5F, 1.4F);
								livingentity.addPotionEffect(new EffectInstance(Effects.LEVITATION, 60, 0));
								attacker.world.addParticle(ParticleTypes.CLOUD, (float)attacker.getPosX() + random.nextFloat(), (float)attacker.getPosY() + random.nextFloat(), (float)attacker.getPosZ() + random.nextFloat(), target.getMotion().x, target.getMotion().y, target.getMotion().z);
								attacker.removePotionEffect(Effects.LEVITATION);
							}
						}
					}
				}
			}
		}
		return super.hitEntity(stack, target, attacker);
	}
}