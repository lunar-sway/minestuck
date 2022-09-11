package com.mraof.minestuck.item;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.util.BlockHitResultUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The class for the items Sburb Code and Completed Sburb Code which primarily allows them to be read, with Sburb Code extending its usage.
 * Some checks for the use of sburb code exists in ComputerBlock, and functions from here are used in both ComputerBlock and ComputerTileEntity
 */
public class ReadableSburbCodeItem extends Item
{
	public ReadableSburbCodeItem(Item.Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn)
	{
		if(playerIn.isShiftKeyDown() || !BlockHitResultUtil.collidedBlockState(levelIn, playerIn).is(MSTags.Blocks.GREEN_HIEROGLYPHS))
		{
			ItemStack itemStackIn = playerIn.getItemInHand(handIn);
			
			if(levelIn.isClientSide)
			{
				MSScreenFactories.displayReadableSburbCodeScreen(getRecordedBlocks(itemStackIn), getParadoxInfo(itemStackIn));
			}
			
			return InteractionResultHolder.success(itemStackIn);
		}
		
		return super.use(levelIn, playerIn, handIn);
	}
	
	/**
	 * Uses the nbt of the itemstack and attempts to return a list of blocks based on the string of their registry name
	 */
	public static List<Block> getRecordedBlocks(ItemStack stack)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE.get())
		{
			return MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		}
		
		
		List<ResourceLocation> blockStringList = new ArrayList<>();
		
		if(stack.getTag() != null && stack.getTag().contains("recordedHieroglyphs"))
		{
			ListTag hieroglyphList = stack.getTag().getList("recordedHieroglyphs", Tag.TAG_STRING);
			
			for(int iterate = 0; iterate < hieroglyphList.size(); iterate++)
			{
				ResourceLocation iterateResourceLocation = ResourceLocation.tryParse(hieroglyphList.getString(iterate));
				if(iterateResourceLocation != null)
					blockStringList.add(iterateResourceLocation);
			}
		}
		
		List<Block> blockList = new ArrayList<>();
		
		for(ResourceLocation iterateList : blockStringList)
		{
			Block iterateBlock = ForgeRegistries.BLOCKS.getValue(iterateList);
			
			if(iterateBlock != null)
				blockList.add(iterateBlock);
		}
		
		return blockList;
	}
	
	/**
	 * Uses the nbt of a tile entity and attempts to return a list of blocks based on the string of their registry name, used by ComputerTileEntity
	 */
	public static List<Block> getRecordedBlocks(ListTag hieroglyphList)
	{
		List<ResourceLocation> blockStringList = new ArrayList<>();
		
		for(int iterate = 0; iterate < hieroglyphList.size(); iterate++)
		{
			ResourceLocation iterateResourceLocation = ResourceLocation.tryParse(hieroglyphList.getString(iterate));
			if(iterateResourceLocation != null)
				blockStringList.add(iterateResourceLocation);
		}
		
		List<Block> blockList = new ArrayList<>();
		
		for(ResourceLocation iterateList : blockStringList)
		{
			Block iterateBlock = ForgeRegistries.BLOCKS.getValue(iterateList);
			
			if(iterateBlock != null)
				blockList.add(iterateBlock);
		}
		
		return blockList;
	}
	
	public static ListTag getListTagFromBlockList(List<Block> blockList)
	{
		ListTag hieroglyphList = new ListTag();
		if(blockList != null && !blockList.isEmpty())
		{
			for(Block blockIterate : blockList)
			{
				String blockRegistryString = String.valueOf(blockIterate.getRegistryName());
				
				hieroglyphList.add(StringTag.valueOf(blockRegistryString));
			}
		}
		
		return hieroglyphList;
	}
	
	public static boolean getParadoxInfo(ItemStack stack)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE.get())
			return true;
		else
		{
			CompoundTag nbt = stack.getTag();
			
			return nbt != null && nbt.contains("hasParadoxInfo") && nbt.getBoolean("hasParadoxInfo");
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE.get())
		{
			if(Screen.hasShiftDown())
			{
				tooltip.add(new TranslatableComponent("item.minestuck.completed_sburb_code.additional_info"));
			} else
				tooltip.add(new TranslatableComponent("item.minestuck.completed_sburb_code.shift_for_more_info"));
		}
	}
}
