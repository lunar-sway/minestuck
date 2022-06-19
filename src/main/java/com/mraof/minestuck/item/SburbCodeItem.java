package com.mraof.minestuck.item;

import com.mraof.minestuck.util.BlockHitResultUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class SburbCodeItem extends ReadableSburbCodeItem
{
	public SburbCodeItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context)
	{
		ItemStack stackInUse = context.getItemInHand();
		World worldIn = context.getLevel();
		PlayerEntity player = context.getPlayer();
		Hand handIn = context.getHand();
		
		if(!worldIn.isClientSide && player != null)
		{
			BlockRayTraceResult blockRayTraceResult = BlockHitResultUtil.getPlayerPOVHitResult(worldIn, player);
			
			if(blockRayTraceResult.getType() == RayTraceResult.Type.BLOCK)
			{
				BlockState raytraceState = BlockHitResultUtil.collidedBlockState(player, blockRayTraceResult);
				
				List<Block> preProcessBlockList = getRecordedBlocks(stackInUse); //used to differentiate from after processing
				ItemStack processedStack = addCarvingsToCode(raytraceState.getBlock(), stackInUse); //the call to addCarvingsToCode is what processes the stack
				List<Block> postProcessBlockList = getRecordedBlocks(processedStack);
				if(!preProcessBlockList.containsAll(postProcessBlockList))
				{
					worldIn.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
				}
				
				attemptConversionToCompleted(player, handIn); //if after addCarvingsToCode the item now possesses all hieroglyphs, convert it to a completed sburb code item
			}
		}
		
		return ActionResultType.PASS;
	}
	
	/**
	 * Checks if the block being investigated is in the GREEN_HIEROGLYPHS block tag, if true then it passes to addRecordedInfo()
	 */
	public ItemStack addCarvingsToCode(Block hieroglyphBlock, ItemStack stackInUse)
	{
		if(MSTags.Blocks.GREEN_HIEROGLYPHS.contains(hieroglyphBlock))
		{
			List<Block> hieroglpyhsList = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
			
			for(Block block : hieroglpyhsList)
			{
				if(block == hieroglyphBlock)
				{
					addRecordedInfo(stackInUse, hieroglyphBlock);
					break;
				}
			}
		}
		
		return stackInUse;
	}
	
	/**
	 * Takes a block thats in the HIEROGLYPHS block tag and adds its registry name(as a string) to the item's nbt if it did not already have it stored
	 */
	public static ItemStack addRecordedInfo(ItemStack stack, Block block)
	{
		String blockRegistryString = String.valueOf(block.getRegistryName());
		
		CompoundNBT nbt = stack.getOrCreateTag();
		
		ListNBT hieroglyphList = nbt.getList("recordedHieroglyphs", Constants.NBT.TAG_STRING);
		if(!getRecordedBlocks(stack).contains(block))
		{
			hieroglyphList.add(StringNBT.valueOf(blockRegistryString));
			nbt.put("recordedHieroglyphs", hieroglyphList);
		}
		
		stack.setTag(nbt);
		return stack;
	}
	
	public void attemptConversionToCompleted(PlayerEntity player, Hand hand)
	{
		ItemStack stackInHand = player.getItemInHand(hand);
		List<Block> recordedList = getRecordedBlocks(stackInHand);
		List<Block> completeList = MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
		
		if(recordedList.containsAll(completeList))
		{
			player.setItemInHand(hand, MSItems.COMPLETED_SBURB_CODE.getDefaultInstance());
		}
	}
	
	public ItemStack setRecordedInfo(ItemStack stack, List<Block> blockList)
	{
		CompoundNBT nbt = stack.getOrCreateTag();
		ListNBT hieroglyphList = nbt.getList("recordedHieroglyphs", Constants.NBT.TAG_STRING);
		hieroglyphList.clear();
		nbt.put("recordedHieroglyphs", hieroglyphList);
		
		for(Block iterateBlock : blockList)
		{
			addRecordedInfo(stack, iterateBlock);
		}
		
		return stack;
	}
	
	public static float percentCompletion(float sizeOfList)
	{
		return ((sizeOfList + 1) / (MSTags.Blocks.GREEN_HIEROGLYPHS.getValues().size() + 1)) * 100; //+1 is to give the illusion that part of it has already been filled in before it was sent through the lotus flower
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if(level != null)
			tooltip.add(new TranslationTextComponent("item.minestuck.sburb_code.completion", (byte) percentCompletion(getRecordedBlocks(stack).size())));
		
		if(Screen.hasShiftDown())
		{
			tooltip.add(new TranslationTextComponent("item.minestuck.sburb_code.additional_info"));
		} else
			tooltip.add(new TranslationTextComponent("item.minestuck.sburb_code.shift_for_more_info"));
	}
}
