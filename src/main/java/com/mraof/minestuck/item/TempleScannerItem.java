package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
		worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.UI_TOAST_IN, SoundCategory.AMBIENT, 1.5F, 1.0F);
		
		ItemStack item = playerIn.getHeldItem(handIn);
		if(worldIn instanceof ServerWorld)
		{
			BlockPos blockpos = ((ServerWorld) worldIn).getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, "minestuck:frog_temple", new BlockPos(playerIn), 100, false);
			//BlockPos blockpos = ((ServerWorld) worldIn).getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, "Stronghold", new BlockPos(playerIn), 100, false);
			
			if(blockpos != null)
			{
				ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".successMessage", blockpos.getX(), blockpos.getZ());
				message.getStyle().setColor(TextFormatting.AQUA);
				playerIn.sendMessage(message);
			} else
			{
				ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".failMessage");
				message.getStyle().setColor(TextFormatting.RED);
				playerIn.sendMessage(message);
			}
		}
		
		return ActionResult.resultSuccess(item);
	}
}
