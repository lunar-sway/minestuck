package com.mraof.minestuck.item.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class ItemBlockCraftingTab extends ItemBlock
{
	private CreativeTabs craftingTab;
	
	public ItemBlockCraftingTab(Block block, CreativeTabs tab)
	{
		super(block);
		this.craftingTab = tab;
	}
	
	@Override
	public CreativeTabs getCreativeTab()
	{
		return craftingTab;
	}
	
	@Override
	protected boolean isInCreativeTab(CreativeTabs targetTab)
	{
		CreativeTabs itemTab = this.block.getCreativeTabToDisplayOn();
		return itemTab != null && (targetTab == CreativeTabs.SEARCH || targetTab == itemTab);
	}
}
