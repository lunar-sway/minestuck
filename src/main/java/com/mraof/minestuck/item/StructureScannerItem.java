package com.mraof.minestuck.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.function.Supplier;

public class StructureScannerItem extends Item
{
	private final TagKey<Structure> structure;
	private final Supplier<Item> fuelItem;
	
	public StructureScannerItem(Properties properties, TagKey<Structure> structure, Supplier<Item> fuelItem)
	{
		super(properties);
		this.structure = structure;
		this.fuelItem = fuelItem;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn)
	{
		levelIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.UI_BUTTON_CLICK, SoundSource.AMBIENT, 0.8F, 1.3F);
		
		ItemStack item = playerIn.getItemInHand(handIn);
		boolean foundItem = false;
		
		if(fuelItem != null)
		{
			ItemStack fuelStack = new ItemStack(fuelItem.get());
			
			for(ItemStack invItem : playerIn.getInventory().items)
			{
				if(ItemStack.isSame(invItem, fuelStack))
				{
					foundItem = true;
					if(!levelIn.isClientSide && levelIn.getRandom().nextFloat() >= 0.95F)
					{
						invItem.shrink(1);
						levelIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BLAZE_SHOOT, SoundSource.AMBIENT, 0.4F, 2F);
					}
					
					break;
				}
			}
		} else
		{
			foundItem = true;
		}
		
		if(levelIn instanceof ServerLevel level)
		{
			if(foundItem || playerIn.isCreative())
			{
				BlockPos structureBlockPos = level.findNearestMapStructure(structure, playerIn.blockPosition(), 100, false);
				
				if(structureBlockPos != null)
				{
					BlockPos playerBlockPos = playerIn.blockPosition();
					playerIn.sendSystemMessage(Component.translatable(getDescriptionId() + ".successMessage",
							(int) Math.sqrt(playerBlockPos.distSqr(structureBlockPos.above(64)))).withStyle(ChatFormatting.AQUA));
				} else
				{
					playerIn.sendSystemMessage(Component.translatable(getDescriptionId() + ".failMessage").withStyle(ChatFormatting.RED));
				}
			} else
			{
				playerIn.sendSystemMessage(Component.translatable(getDescriptionId() + ".noFuelMessage").withStyle(ChatFormatting.RED));
			}
		}
		return InteractionResultHolder.success(item);
	}
}
