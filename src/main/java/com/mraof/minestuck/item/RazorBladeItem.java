package com.mraof.minestuck.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
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
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		if(attacker instanceof PlayerEntity)
		{
			if(!((PlayerEntity) attacker).isCreative())
			{
				ItemEntity razor = new ItemEntity(attacker.level, attacker.getX(), attacker.getY(), attacker.getZ(), stack.copy());
				if(!attacker.level.isClientSide)
				{
					razor.getItem().setCount(1);
					razor.setPickUpDelay(40);
					attacker.level.addFreshEntity(razor);
					stack.shrink(1);
					ITextComponent message = new TranslationTextComponent("While you handle the razor blade, you accidentally cut yourself and drop it.");
					attacker.sendMessage(message, Util.NIL_UUID);
				}
				attacker.setHealth(attacker.getHealth() - 1);
				return true;
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	@Override
	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{
		if(entityLiving instanceof PlayerEntity)
		{
			if(!((PlayerEntity) entityLiving).isCreative())
			{
				ItemEntity razor = new ItemEntity(entityLiving.level, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), stack.copy());
				if(!entityLiving.level.isClientSide)
				{
					razor.getItem().setCount(1);
					razor.setPickUpDelay(40);
					entityLiving.level.addFreshEntity(razor);
					stack.shrink(1);
					ITextComponent message = new TranslationTextComponent("While you handle the razor blade, you accidentally cut yourself and drop it.");
					entityLiving.sendMessage(message, Util.NIL_UUID);
				}
				entityLiving.hurt(DamageSource.GENERIC, 1);
			}
		}
		return super.mineBlock(stack, worldIn, state, pos, entityLiving);
	}
}
