package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;

import com.mraof.minestuck.inventory.captchalouge.Modus;

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
		this.cards.clear();
		this.maxWidth = Math.max(mapWidth, 10 + (stacks.length*CARD_WIDTH + (stacks.length - 1)*5));
		this.maxHeight = mapHeight;
		super.updateContent();
		int start = Math.max(5, (mapWidth - (stacks.length*CARD_WIDTH + (stacks.length - 1)*5))/2);
		
		for(int i = 0; i < stacks.length; i++)
			this.cards.add(new GuiCard(stacks[i], this, i == 0 ? 0 : -1, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2));
	}
	
	@Override
	public void updatePosition()
	{
		this.maxWidth = Math.max(mapWidth, 10 + (cards.size()*CARD_WIDTH + (cards.size() - 1)*5));
		this.maxHeight = mapHeight;
		int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
		for(int i = 0; i < cards.size(); i++)
		{
			GuiCard card = cards.get(i);
			card.xPos = start + i*(CARD_WIDTH + 5);
			card.yPos = (mapHeight - CARD_HEIGHT)/2;
		}
	}
	
	@Override
	public void drawGuiMap(int xcor, int ycor)
	{
		super.drawGuiMap(xcor, ycor);
		
		if(!cards.isEmpty())
		{
			int startX = Math.max(0, cards.get(0).xPos + CARD_WIDTH - mapX);
			int endX = Math.min(mapWidth, cards.get(cards.size() - 1).xPos - mapX);
			int y = mapHeight/2 + 1;
			drawRect(startX, y, endX, y + 2, 0xFF000000);
		}
	}
	
}
