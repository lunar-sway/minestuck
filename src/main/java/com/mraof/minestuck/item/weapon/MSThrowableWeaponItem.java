package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.ItemRenderedProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class MSThrowableWeaponItem extends Item
{
	public MSThrowableWeaponItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getHeldItem(handIn);
		
		worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.5F);
		
		if(!worldIn.isRemote)
		{
			ItemRenderedProjectileEntity projectileEntity = new ItemRenderedProjectileEntity(MSEntityTypes.SUITARANG, playerIn, worldIn);
			projectileEntity.setItem(item);
			projectileEntity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.0F, 1.2F);
			worldIn.addEntity(projectileEntity);
		}
		if(!playerIn.isCreative())
		{
			item.shrink(1);
		}
		
		playerIn.getCooldownTracker().setCooldown(playerIn.getActiveItemStack().getItem(), 60);
		playerIn.addStat(Stats.ITEM_USED.get(this));
		return new ActionResult<>(ActionResultType.SUCCESS, item);
	}
}