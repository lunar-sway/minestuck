package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;

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
		this.maxWidth = Math.max(mapWidth, 10 + (stacks.length*CARD_WIDTH + (stacks.length - 1)*5));
		this.maxHeight = mapHeight;
		super.updateContent();
		int start = Math.max(5, (mapWidth - (stacks.length*CARD_WIDTH + (stacks.length - 1)*5))/2);
		
		for(int i = 0; i < stacks.length; i++)
			this.items.add(new GuiItem(stacks[i], this, i == 0 ? 0 : -1, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2));
	}
	
	@Override
	public void updatePosition()
	{
		this.maxWidth = Math.max(mapWidth, 10 + (items.size()*CARD_WIDTH + (items.size() - 1)*5));
		this.maxHeight = mapHeight;
		int start = Math.max(5, (mapWidth - (items.size()*CARD_WIDTH + (items.size() - 1)*5))/2);
		for(int i = 0; i < items.size(); i++)
		{
			GuiItem item = items.get(i);
			item.xPos = start + i*(CARD_WIDTH + 5);
			item.yPos = (mapHeight - CARD_HEIGHT)/2;
		}
	}
	
	@Override
	public void drawGuiMap(int xcor, int ycor)
	{
		super.drawGuiMap(xcor, ycor);
		
		if(!items.isEmpty())
		{
			int startX = Math.max(0, items.get(0).xPos + CARD_WIDTH - mapX);
			int endX = Math.min(mapWidth, items.get(items.size() - 1).xPos - mapX);
			int y = mapHeight/2 + 1;
			drawRect(startX, y, endX, y + 2, 0xFF000000);
		}
	}
	
}
