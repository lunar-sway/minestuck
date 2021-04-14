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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.2F);
		
		if(!worldIn.isRemote)
		{
			BouncingProjectileEntity projectileEntity = new BouncingProjectileEntity(MSEntityTypes.BOUNCING_PROJECTILE, playerIn, worldIn, maxTick);
			projectileEntity.setItem(item);
			projectileEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, velocity, accuracy);
			projectileEntity.setNoGravity(true);
			worldIn.addEntity(projectileEntity);
		}
		
		item.damageItem(1, playerIn, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
		
		playerIn.getCooldownTracker().setCooldown(this, maxTick);
		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}