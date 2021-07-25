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
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Supplier;

public class StructureScannerItem extends Item
{
	private final Supplier<Structure<?>> structure;
	private final Supplier<Item> fuelItem;
	
	public StructureScannerItem(Properties properties, Supplier<Structure<?>> structure, Supplier<Item> fuelItem)
	{
		super(properties);
		this.structure = structure;
		this.fuelItem = fuelItem;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 0.8F, 1.3F);
		
		ItemStack item = playerIn.getHeldItem(handIn);
		boolean foundItem = false;
		
		if(fuelItem != null)
		{
			ItemStack fuelStack = new ItemStack(fuelItem.get());
			
			for(ItemStack invItem : playerIn.inventory.mainInventory)
			{
				if(ItemStack.areItemsEqual(invItem, fuelStack))
				{
					foundItem = true;
					if(!worldIn.isRemote && random.nextFloat() >= 0.95F)
					{
						invItem.shrink(1);
						worldIn.playSound(playerIn, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT, 0.4F, 2F);
					}
					
					break;
				}
			}
		} else
		{
			foundItem = true;
		}
		
		if(worldIn instanceof ServerWorld)
		{
			if(foundItem || playerIn.isCreative())
			{
				BlockPos structureBlockPos = ((ServerWorld) worldIn).getChunkProvider().getChunkGenerator().findNearestStructure(worldIn, structure.get().getStructureName(), new BlockPos(playerIn), 100, false);
				
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
				ITextComponent message = new TranslationTextComponent(getTranslationKey() + ".noFuelMessage");
				message.getStyle().setColor(TextFormatting.RED);
				playerIn.sendMessage(message);
			}
		}
		return ActionResult.resultSuccess(item);
	}
}
