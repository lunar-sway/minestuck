package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TempleScannerItem extends Item
{
	public TempleScannerItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 0.8F, 1.3F);
		
		ItemStack item = playerIn.getHeldItem(handIn);
		ItemStack uraniumStack = new ItemStack(MSItems.RAW_URANIUM);
		boolean foundItem = false;
		
		for(ItemStack invItem : playerIn.inventory.mainInventory)
		{
			if(ItemStack.areItemsEqual(invItem, uraniumStack))
			{
				foundItem = true;
				if(random.nextFloat() >= 0.95F)
				{
					invItem.shrink(1);
					worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT, 0.3F, 2F);
				}
				
				break;
			}
		}
		
		if(worldIn instanceof ServerWorld)
		{
			if(foundItem || playerIn.isCreative())
			{
				BlockPos structureBlockPos = ((ServerWorld) worldIn).getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, "minestuck:frog_temple", new BlockPos(playerIn), 100, false);
				
				if(structureBlockPos != null)
				{
					BlockPos playerBlockPos = playerIn.getPosition();
					ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".successMessage", (int) Math.sqrt(playerBlockPos.distanceSq(structureBlockPos.up(64))));
					message.getStyle().setColor(TextFormatting.AQUA);
					playerIn.sendMessage(message);
				} else
				{
					ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".failMessage");
					message.getStyle().setColor(TextFormatting.RED);
					playerIn.sendMessage(message);
				}
			} else
			{
				ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".noUraniumMessage");
				message.getStyle().setColor(TextFormatting.RED);
				playerIn.sendMessage(message);
			}
		}
		return ActionResult.resultSuccess(item);
	}
}
