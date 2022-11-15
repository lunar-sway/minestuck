package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public interface DestroyBlockEffect
{
	void onDestroyBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity);
	
	DestroyBlockEffect DOUBLE_FARM = (stack, levelIn, state, pos, entity) -> {
		if((state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)))
			CropBlock.dropResources(state, levelIn, pos);
	};
	
	DestroyBlockEffect DROP_GRIST = (stack, levelIn, state, pos, entity) -> {
		if(stack.isCorrectToolForDrops(state) && entity instanceof Player player)
		{
			ItemStack droppedStack = state.getCloneItemStack(null, levelIn, pos, player);
			GristSet gristSet = GristCostRecipe.findCostForItem(droppedStack, null, false, levelIn);
			if(gristSet != null && !gristSet.isEmpty())
			{
				for(GristAmount gristAmount : gristSet.getAmounts())
				{
					if(gristAmount.getAmount() > 0)
						levelIn.addFreshEntity(new GristEntity(levelIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, gristAmount));
				}
				
				levelIn.destroyBlock(pos, false);
			}
		}
	};
	
	static DestroyBlockEffect extraHarvests(boolean melonOverload, float percentage, int maxBonusItems, Supplier<Item> itemDropped, Supplier<Block> harvestedBlock)
	{
		return (stack, levelIn, state, pos, entity) -> {
			if(state == harvestedBlock.get().defaultBlockState() && !levelIn.isClientSide)
			{
				int harvestCounter = 0;
				for(float i = entity.getRandom().nextFloat(); i <= percentage && harvestCounter < maxBonusItems; i = entity.getRandom().nextFloat())
				{
					ItemStack harvestItemStack = new ItemStack(itemDropped.get(), 1);
					ItemEntity item = new ItemEntity(levelIn, pos.getX(), pos.getY(), pos.getZ(), harvestItemStack);
					levelIn.addFreshEntity(item);
					
					harvestCounter++;
				}
				
				if(melonOverload && harvestCounter >= 9 && entity instanceof ServerPlayer player)
				{
					TranslatableComponent message = new TranslatableComponent(stack.getDescriptionId() + ".message");
					entity.sendMessage(message.withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD), Util.NIL_UUID);
					
					entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 3));
					entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1800, 3));
					entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1800, 3));
					
					MSCriteriaTriggers.MELON_OVERLOAD.trigger(player);
				}
			}
		};
	}
}