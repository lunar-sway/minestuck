package com.mraof.minestuck.item.artifact;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class CruxiteAppleItem extends CruxiteArtifactItem
{
	public CruxiteAppleItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 32;
	}
	
	@Override
	public UseAction getUseAnimation(ItemStack stack)
	{
		return UseAction.EAT;
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		stack.shrink(1);
		
		if(entityLiving instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) entityLiving;
			worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.random.nextFloat() * 0.1F + 0.9F);
			onArtifactActivated(player);
		}
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		playerIn.startUsingItem(handIn);
		return ActionResult.success(playerIn.getItemInHand(handIn));
	}
}