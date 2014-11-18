package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.util.Debug;

public class StackGuiHandler extends SylladexGuiHandler
{
	
	private Modus modus;
	
	public StackGuiHandler(Modus modus)
	{
		this.modus = modus;
		this.textureIndex = 0;
	}
	
	@Override
	public void updateContent()
	{
		ItemStack[] stacks = modus.getItems();
		this.items.clear();
		this.maxWidth = Math.max(mapWidth, 10 + (stacks.length*CARD_WIDTH + (stacks.length - 1)*10));
		this.maxHeight = mapHeight;
		int start = Math.max(5, (mapWidth - (stacks.length*CARD_WIDTH + (stacks.length - 1)*10))/2);
		
		for(int i = 0; i < stacks.length; i++)
			this.items.add(new GuiItem(stacks[i], this, i == 0 ? 0 : -1, start + i*(CARD_WIDTH + 10), (mapHeight - CARD_HEIGHT)/2));
	}
	
	@Override
	public void updatePosition()
	{
		this.maxWidth = Math.max(mapWidth, 10 + (items.size()*CARD_WIDTH + (items.size() - 1)*10));
		this.maxHeight = mapHeight;
		int start = Math.max(5, (mapWidth - (items.size()*CARD_WIDTH + (items.size() - 1)*10))/2);
		for(int i = 0; i < items.size(); i++)
		{
			GuiItem item = items.get(i);
			item.xPos = start + i*(CARD_WIDTH + 10);
			item.yPos = (mapHeight - CARD_HEIGHT)/2;
		}
	}
	
}
