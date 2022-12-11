package com.mraof.minestuck.item;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The class for the items Sburb Code and Completed Sburb Code which primarily allows them to be read, with Sburb Code extending its usage.
 * Some checks for the use of sburb code exists in ComputerBlock, and functions from here are used in both ComputerBlock and ComputerTileEntity
 */
public abstract class ReadableSburbCodeItem extends Item
{
	public ReadableSburbCodeItem(Item.Properties properties)
	{
		super(properties);
	}
	
	public abstract boolean getParadoxInfo(ItemStack stack);
	
	/**
	 * Loads the set of hieroglyph blocks that have been recorded into this item.
	 */
	public abstract Set<Block> getRecordedBlocks(ItemStack stack);
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level levelIn, Player playerIn, InteractionHand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		
		if(levelIn.isClientSide())
		{
			MSScreenFactories.displayReadableSburbCodeScreen(getRecordedBlocks(itemStackIn), getParadoxInfo(itemStackIn));
		}
		
		return InteractionResultHolder.sidedSuccess(itemStackIn, levelIn.isClientSide());
	}
	
	@Override
	public InteractionResult onItemUseFirst(ItemStack heldStack, UseOnContext context)
	{
		Level level = context.getLevel();
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		BlockPos pos = context.getClickedPos();
		
		if(player != null && level.getBlockEntity(pos) instanceof ComputerBlockEntity blockEntity)
		{
			if(useOnComputer(heldStack, player, hand, blockEntity))
				return InteractionResult.sidedSuccess(level.isClientSide);
			else
				return InteractionResult.FAIL;
		}
		
		return InteractionResult.PASS;
	}
	
	protected boolean useOnComputer(ItemStack heldStack, Player player, InteractionHand hand, ComputerBlockEntity blockEntity)
	{
		boolean newInfo = false;
		
		if(getParadoxInfo(heldStack) && !blockEntity.hasParadoxInfoStored)
		{
			blockEntity.hasParadoxInfoStored = true;
			newInfo = true;
		}
		
		//for each block in the item's list, adds it to the block entity should it not exist there yet
		for(Block iterateBlock : getRecordedBlocks(heldStack))
		{
			if(iterateBlock.defaultBlockState().is(MSTags.Blocks.GREEN_HIEROGLYPHS) && !blockEntity.hieroglyphsStored.contains(iterateBlock))
			{
				blockEntity.hieroglyphsStored.add(iterateBlock);
				newInfo = true;
			}
		}
		
		if(newInfo)
		{
			blockEntity.setChanged();
			blockEntity.markBlockForUpdate();
			
			return true;
		}
		
		return false;
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
	public static class Completed extends ReadableSburbCodeItem
	{
		public Completed(Properties properties)
		{
			super(properties);
		}
		
		@Override
		public boolean getParadoxInfo(ItemStack stack)
		{
			return true;
		}
		
		@Override
		public Set<Block> getRecordedBlocks(ItemStack stack)
		{
			return MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS);
		}
		
		@Override
		public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
		{
			if(Screen.hasShiftDown())
				tooltip.add(new TranslatableComponent("item.minestuck.completed_sburb_code.additional_info"));
			else
				tooltip.add(new TranslatableComponent("item.minestuck.completed_sburb_code.shift_for_more_info"));
		}
		
	}
}
