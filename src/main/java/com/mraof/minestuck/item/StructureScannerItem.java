package com.mraof.minestuck.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.AMBIENT, 0.8F, 1.3F);
		
		ItemStack item = playerIn.getItemInHand(handIn);
		boolean foundItem = false;
		
		if(fuelItem != null)
		{
			ItemStack fuelStack = new ItemStack(fuelItem.get());
			
			for(ItemStack invItem : playerIn.inventory.items)
			{
				if(ItemStack.isSame(invItem, fuelStack))
				{
					foundItem = true;
					if(!worldIn.isClientSide && random.nextFloat() >= 0.95F)
					{
						invItem.shrink(1);
						worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.AMBIENT, 0.4F, 2F);
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
			ServerWorld world = (ServerWorld) worldIn;
			if(foundItem || playerIn.isCreative())
			{
				BlockPos structureBlockPos = world.getChunkSource().getGenerator().findNearestMapFeature(world, structure.get(), playerIn.blockPosition(), 100, false);
				
				if(structureBlockPos != null)
				{
					BlockPos playerBlockPos = playerIn.blockPosition();
					IFormattableTextComponent message = new TranslationTextComponent(getDescriptionId() + ".successMessage", (int) Math.sqrt(playerBlockPos.distSqr(structureBlockPos.above(64))));
					message.withStyle(TextFormatting.AQUA);
					playerIn.sendMessage(message, Util.NIL_UUID);
				} else
				{
					IFormattableTextComponent message = new TranslationTextComponent(getDescriptionId() + ".failMessage");
					message.withStyle(TextFormatting.RED);
					playerIn.sendMessage(message, Util.NIL_UUID);
				}
			} else
			{
				IFormattableTextComponent message = new TranslationTextComponent(getDescriptionId() + ".noFuelMessage");
				message.withStyle(TextFormatting.RED);
				playerIn.sendMessage(message, Util.NIL_UUID);
			}
		}
		return ActionResult.success(item);
	}
}
