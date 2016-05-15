package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;

import com.mraof.minestuck.inventory.captchalouge.Modus;

public class SetGuiHandler extends SylladexGuiHandler
{
	
	private Modus modus;
	
	public SetGuiHandler(Modus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 5;
	}
	
	@Override
	public void updateContent()
	{
		ItemStack[] stacks = modus.getItems();
		this.cards.clear();
		int columns = (stacks.length + 1)/2;
		this.maxWidth = Math.max(mapWidth, 10 + (columns*CARD_WIDTH + (columns - 1)*5));
		this.maxHeight = mapHeight;
		super.updateContent();
		
		int start = Math.max(5, (mapWidth - (columns*CARD_WIDTH + (columns - 1)*5))/2);
		for(int i = 0; i < stacks.length; i++)
			this.cards.add(new GuiCard(stacks[i], this, i, start + i/2*(CARD_WIDTH + 5), (mapHeight - 2*CARD_HEIGHT - 5)/2 + (i%2)*(CARD_HEIGHT + 5)));
	}
	
	@Override
	public void updatePosition()
	{
		int columns = (cards.size() + 1)/2;
		this.maxWidth = Math.max(mapWidth, 10 + (columns*CARD_WIDTH + (columns - 1)*5));
		this.maxHeight = mapHeight;
		
		int start = Math.max(5, (mapWidth - (columns*CARD_WIDTH + (columns - 1)*5))/2);
		for(int i = 0; i < cards.size(); i++)
		{
			GuiCard card = cards.get(i);
			card.xPos = start + i/2*(CARD_WIDTH + 5);
			card.yPos = (mapHeight - 2*CARD_HEIGHT - 5)/2 + (i%2)*(CARD_HEIGHT + 5);
		}
	}
}