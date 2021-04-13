package com.mraof.minestuck.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class RazorBladeItem extends Item
{
	
	public RazorBladeItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if(attacker instanceof PlayerEntity)
		{
			if(!((PlayerEntity) attacker).isCreative())
			{
				ItemEntity razor = new ItemEntity(attacker.world, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), stack.copy());
				if(!attacker.world.isRemote)
				{
					razor.getItem().setCount(1);
					razor.setPickupDelay(40);
					attacker.world.addEntity(razor);
					stack.shrink(1);
					ITextComponent message = new TranslationTextComponent("While you handle the razor blade, you accidentally cut yourself and drop it.");
					attacker.sendMessage(message);
				}
				attacker.setHealth(attacker.getHealth() - 1);
				return true;
			}
		}
		return super.hitEntity(stack, target, attacker);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(entityLiving instanceof PlayerEntity)
		{
			if(!((PlayerEntity) entityLiving).isCreative())
			{
				ItemEntity razor = new ItemEntity(entityLiving.world, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), stack.copy());
				if(!entityLiving.world.isRemote)
				{
					razor.getItem().setCount(1);
					razor.setPickupDelay(40);
					entityLiving.world.addEntity(razor);
					stack.shrink(1);
					ITextComponent message = new TranslationTextComponent("While you handle the razor blade, you accidentally cut yourself and drop it.");
					entityLiving.sendMessage(message);
				}
				entityLiving.attackEntityFrom(DamageSource.GENERIC, 1);
			}
		}
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
}
