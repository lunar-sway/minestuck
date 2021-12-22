package com.mraof.minestuck.item.weapon.projectiles;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.BouncingProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BouncingProjectileWeaponItem extends ReturningProjectileWeaponItem
{
	public BouncingProjectileWeaponItem(Properties properties, float velocity, float accuracy, int damage, int maxTick)
	{
		super(properties, velocity, accuracy, damage, maxTick);
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.2F);
		
		if(!worldIn.isClientSide)
		{
			BouncingProjectileEntity projectileEntity = new BouncingProjectileEntity(MSEntityTypes.BOUNCING_PROJECTILE, playerIn, worldIn, maxTick);
			projectileEntity.setItem(item);
			projectileEntity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, velocity, accuracy);
			projectileEntity.setNoGravity(true);
			worldIn.addFreshEntity(projectileEntity);
		}
		
		item.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
		
		playerIn.getCooldowns().addCooldown(this, maxTick);
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}