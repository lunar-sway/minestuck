package com.mraof.minestuck.item;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ReadableSburbCodeItem extends Item
{
	public ReadableSburbCodeItem(Item.Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack itemStackIn = playerIn.getItemInHand(handIn);
		
		if(worldIn.isClientSide)
		{
			MSScreenFactories.displayReadableSburbCodeScreen(getRecordedBlocks(itemStackIn));
		}
		return ActionResult.success(itemStackIn);
		//return super.use(worldIn, playerIn, handIn);
	}
	
	/**
	 * Uses the nbt of the itemstack and attempts to return a list of blocks based on the string of their registry name
	 */
	public static List<Block> getRecordedBlocks(ItemStack stack)
	{
		if(stack.getItem() == MSItems.COMPLETED_SBURB_CODE)
			return MSTags.Blocks.GREEN_HIEROGLYPHS.getValues();
		
		List<ResourceLocation> blockStringList = new ArrayList<>();
		
		if(stack.getTag() != null)
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
}
