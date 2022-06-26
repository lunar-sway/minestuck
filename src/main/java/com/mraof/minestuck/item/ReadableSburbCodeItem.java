package com.mraof.minestuck.item;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.util.BlockHitResultUtil;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(playerIn.isShiftKeyDown() || !MSTags.Blocks.GREEN_HIEROGLYPHS.contains(BlockHitResultUtil.collidedBlockState(worldIn, playerIn).getBlock()))
		{
			ItemStack itemStackIn = playerIn.getItemInHand(handIn);
			
			if(worldIn.isClientSide)
			{
				MSScreenFactories.displayReadableSburbCodeScreen(getRecordedBlocks(itemStackIn), getParadoxInfo(itemStackIn));
			}
			return ActionResult.success(itemStackIn);
		}
		
		return super.use(worldIn, playerIn, handIn);
	}
	
	/**
	 * Uses the nbt of the itemstack and attempts to return a list of blocks based on the string of their registry name
	 */
	public static List<Block> getRecordedBlocks(ItemStack stack)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE)
			return MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
		
		List<ResourceLocation> blockStringList = new ArrayList<>();
		
		if(stack.getTag() != null && stack.getTag().contains("recordedHieroglyphs"))
		{
			ListNBT hieroglyphList = stack.getTag().getList("recordedHieroglyphs", Constants.NBT.TAG_STRING);
			
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
	public static List<Block> getRecordedBlocks(ListNBT hieroglyphList)
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
	
	public static ListNBT getListNBTFromBlockList(List<Block> blockList)
	{
		ListNBT hieroglyphList = new ListNBT();
		if(blockList != null && !blockList.isEmpty())
		{
			for(Block blockIterate : blockList)
			{
				String blockRegistryString = String.valueOf(blockIterate.getRegistryName());
				
				hieroglyphList.add(StringNBT.valueOf(blockRegistryString));
			}
		}
		
		return hieroglyphList;
	}
	
	public static boolean getParadoxInfo(ItemStack stack)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE)
			return true;
		else
		{
			CompoundNBT nbt = stack.getTag();
			
			return nbt != null && nbt.contains("hasParadoxInfo") && nbt.getBoolean("hasParadoxInfo");
		}
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE)
		{
			if(Screen.hasShiftDown())
			{
				tooltip.add(new TranslationTextComponent("item.minestuck.completed_sburb_code.additional_info"));
			} else
				tooltip.add(new TranslationTextComponent("item.minestuck.completed_sburb_code.shift_for_more_info"));
		}
	}
}
