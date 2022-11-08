package com.mraof.minestuck.item;

import com.mraof.minestuck.util.BlockHitResultUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Used for the Sburb Code item, extends ReadableSburbCodeItem which is used by Completed Sburb Code.
 * Allows players to store a list of blocks from the GREEN_HIEROGLPYH block tag that in game reflects the collection of genetic code for the creation of sburb
 */
public class IncompleteSburbCodeItem extends ReadableSburbCodeItem
{
	public IncompleteSburbCodeItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		ItemStack stackInUse = context.getItemInHand();
		Level worldIn = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand handIn = context.getHand();
		
		if(!worldIn.isClientSide && player != null)
		{
			BlockHitResult blockRayTraceResult = BlockHitResultUtil.getPlayerPOVHitResult(worldIn, player);
			
			if(blockRayTraceResult.getType() == HitResult.Type.BLOCK)
			{
				BlockState raytraceState = BlockHitResultUtil.collidedBlockState(player, blockRayTraceResult);
				
				List<Block> preProcessBlockList = getRecordedBlocks(stackInUse); //used to differentiate from after processing
				ItemStack processedStack = addCarvingsToCode(raytraceState.getBlock(), stackInUse); //the call to addCarvingsToCode is what processes the stack
				List<Block> postProcessBlockList = getRecordedBlocks(processedStack);
				if(!preProcessBlockList.containsAll(postProcessBlockList))
				{
					worldIn.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
			}
			
			attemptConversionToCompleted(player, handIn); //if after addCarvingsToCode the item now possesses all hieroglyphs, convert it to a completed sburb code item
		}
		
		return InteractionResult.PASS;
	}
	
	/**
	 * Checks if the block being investigated is in the GREEN_HIEROGLYPHS block tag, if true then it passes to addRecordedInfo()
	 */
	public ItemStack addCarvingsToCode(Block hieroglyphBlock, ItemStack stackInUse)
	{
		List<Block> hieroglpyhsList = MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		
		if(hieroglpyhsList.contains(hieroglyphBlock))
		{
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
		
		CompoundTag nbt = stack.getOrCreateTag();
		
		ListTag hieroglyphList = nbt.getList("recordedHieroglyphs", Tag.TAG_STRING);
		if(!getRecordedBlocks(stack).contains(block))
		{
			hieroglyphList.add(StringTag.valueOf(blockRegistryString));
			nbt.put("recordedHieroglyphs", hieroglyphList);
		}
		
		stack.setTag(nbt);
		return stack;
	}
	
	public static void attemptConversionToCompleted(Player player, InteractionHand hand)
	{
		ItemStack stackInHand = player.getItemInHand(hand);
		List<Block> recordedList = getRecordedBlocks(stackInHand);
		
		if(hasAllBlocks(recordedList) && getParadoxInfo(stackInHand))
		{
			player.setItemInHand(hand, MSItems.COMPLETED_SBURB_CODE.get().getDefaultInstance());
		}
	}
	
	public static boolean hasAllBlocks(List<Block> blockList)
	{
		List<Block> completeList = MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		
		return blockList.containsAll(completeList);
	}
	
	public static void setParadoxInfo(ItemStack stack, boolean hasInfo)
	{
		CompoundTag nbt = stack.getOrCreateTag();
		nbt.putBoolean("hasParadoxInfo", hasInfo);
		stack.setTag(nbt);
	}
	
	public static ItemStack setRecordedInfo(ItemStack stack, List<Block> blockList)
	{
		CompoundTag nbt = stack.getOrCreateTag();
		ListTag hieroglyphList = nbt.getList("recordedHieroglyphs", Tag.TAG_STRING);
		hieroglyphList.clear();
		nbt.put("recordedHieroglyphs", hieroglyphList);
		
		for(Block iterateBlock : blockList)
		{
			addRecordedInfo(stack, iterateBlock);
		}
		
		return stack;
	}
	
	public static float percentCompletion(ItemStack stack)
	{
		int mod = getParadoxInfo(stack) ? 1 : 0; //the mod of 1 is to give the illusion that part of it has already been filled in before it was sent through the lotus flower, if it has the paradox code
		float sizeOfList = getRecordedBlocks(stack).size();
		return ((sizeOfList + mod) / (MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS).size() + 1)) * 100;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
	{
		if(level != null)
		{
			tooltip.add(new TranslatableComponent("item.minestuck.sburb_code.completion", (byte) percentCompletion(stack)));
			if(hasAllBlocks(getRecordedBlocks(stack)))
				tooltip.add(new TranslatableComponent("item.minestuck.sburb_code.paradox_hint"));
		}
		
		if(Screen.hasShiftDown())
		{
			tooltip.add(new TranslatableComponent("item.minestuck.sburb_code.additional_info"));
		} else
			tooltip.add(new TranslatableComponent("item.minestuck.sburb_code.shift_for_more_info"));
	}
}
