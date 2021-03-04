package com.mraof.minestuck.item.weapon;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public interface ItemRightClickEffect
{
	ActionResult<ItemStack> onRightClick(World world, PlayerEntity player, Hand hand);
	
	ItemRightClickEffect ACTIVE_HAND = (world, player, hand) -> {
		player.setActiveHand(hand);
		return ActionResult.resultConsume(player.getHeldItem(hand));
	};
	
	static ItemRightClickEffect switchTo(Supplier<Item> otherItem)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			if(player.isSneaking())
			{
				ItemStack newItem = new ItemStack(otherItem.get(), itemStackIn.getCount());
				newItem.setTag(itemStackIn.getTag());
				
				return ActionResult.resultSuccess(newItem);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect summonFireball()
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 0.8F);
			
			AxisAlignedBB axisalignedbb = player.getBoundingBox().grow(32.0D, 32.0D, 32.0D);
			List<LivingEntity> list = player.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
			list.remove(player);
			if (!list.isEmpty() && !world.isRemote)
			{
				for(LivingEntity livingentity : list){
					FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
					fireball.explosionPower = 1;
					fireball.setPosition(livingentity.getPosX()+(player.getRNG().nextInt(6)-3), livingentity.getPosY()+40, livingentity.getPosZ()+(player.getRNG().nextInt(6)-3));
					player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
					itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
					world.addEntity(fireball);
				}
			}
			else if (!world.isRemote)
			{
				FireballEntity fireball = new FireballEntity(world, player, 0, -8.0, 0);
				fireball.explosionPower = 1;
				fireball.setPosition(player.getPosX()+(player.getRNG().nextInt(20)-10), player.getPosY()+40, player.getPosZ()+(player.getRNG().nextInt(20)-10));
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 20);
				itemStackIn.damageItem(2, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
				world.addEntity(fireball);
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
	
	static ItemRightClickEffect extinguishFire(int mod)
	{
		return (world, player, hand) -> {
			ItemStack itemStackIn = player.getHeldItem(hand);
			
			if(!world.isRemote){
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 1.0F, 1.4F);
				
				for(BlockPos blockPos : BlockPos.getAllInBoxMutable(player.getPosition().add(2*mod, mod, 2*mod), player.getPosition().add(-2*mod, -1*mod, -2*mod))) {
					BlockState blockState = world.getBlockState(blockPos);
					if(blockState.getBlock() == Blocks.FIRE){
						world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1.0F);
					}
				}
				
				player.swing(hand, true);
				player.getCooldownTracker().setCooldown(itemStackIn.getItem(), 15);
				itemStackIn.damageItem(1, player, playerEntity -> playerEntity.sendBreakAnimation(Hand.MAIN_HAND));
			}
			return ActionResult.resultPass(itemStackIn);
		};
	}
}