package com.mraof.minestuck.item.weapon.projectiles;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ReturningProjectileEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ReturningProjectileWeaponItem extends ConsumableProjectileWeaponItem
{
	protected final int maxTick;
	
	public ReturningProjectileWeaponItem(Properties properties, float velocity, float accuracy, int damage, int maxTick)
	{
		super(properties, velocity, accuracy, damage);
		this.maxTick = maxTick;
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.2F);
		
		if(!worldIn.isClientSide)
		{
			boolean noBlockCollision = false;
			Title title = PlayerSavedData.getData((ServerPlayerEntity) playerIn).getTitle();
			if(title != null)
			{
				noBlockCollision = title.getHeroAspect() == EnumAspect.VOID && item.getItem() == MSItems.UMBRAL_INFILTRATOR;
			}
			
			ReturningProjectileEntity projectileEntity = new ReturningProjectileEntity(MSEntityTypes.RETURNING_PROJECTILE, playerIn, worldIn, maxTick, noBlockCollision);
			projectileEntity.setItem(item);
			projectileEntity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, velocity, accuracy);
			projectileEntity.setNoGravity(true);
			if(noBlockCollision)
				projectileEntity.setGlowing(true);
			worldIn.addFreshEntity(projectileEntity);
		}
		
		item.hurtAndBreak(1, playerIn, playerEntity -> playerEntity.broadcastBreakEvent(Hand.MAIN_HAND));
		
		playerIn.getCooldowns().addCooldown(this, maxTick);
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}