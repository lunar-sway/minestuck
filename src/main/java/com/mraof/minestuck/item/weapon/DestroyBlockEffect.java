package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.function.Supplier;

public interface DestroyBlockEffect
{
	void onDestroyBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entity);
	
	DestroyBlockEffect DOUBLE_FARM = (stack, worldIn, state, pos, entity) -> {
		if((state.getBlock() instanceof CropsBlock && ((CropsBlock) state.getBlock()).isMaxAge(state)))
			CropsBlock.spawnDrops(state, worldIn, pos);
	};
	
	static DestroyBlockEffect extraHarvests(boolean melonOverload, float percentage, int maxBonusItems, Supplier<Item> itemDropped, Supplier<Block> harvestedBlock)
	{
		return (stack, worldIn, state, pos, entity) -> {
			if(state == harvestedBlock.get().getDefaultState() && !worldIn.isRemote)
			{
				int harvestCounter = 0;
				for(float i = entity.getRNG().nextFloat(); i <= percentage && harvestCounter < maxBonusItems; i = entity.getRNG().nextFloat())
				{
					ItemStack harvestItemStack = new ItemStack(itemDropped.get(), 1);
					ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), harvestItemStack);
					worldIn.addEntity(item);
					
					harvestCounter++;
				}
				
				if(melonOverload && harvestCounter >= 9 && entity instanceof PlayerEntity)
				{
					IFormattableTextComponent message = new TranslationTextComponent(stack.getTranslationKey() + ".message");
					entity.sendMessage(message.mergeStyle(TextFormatting.GREEN, TextFormatting.BOLD), Util.DUMMY_UUID);
					
					entity.addPotionEffect(new EffectInstance(Effects.STRENGTH, 1800, 3));
					entity.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 1800, 3));
					entity.addPotionEffect(new EffectInstance(Effects.HASTE, 1800, 3));
					
					MSCriteriaTriggers.MELON_OVERLOAD.trigger((ServerPlayerEntity) entity);
				}
			}
		};
	}
}