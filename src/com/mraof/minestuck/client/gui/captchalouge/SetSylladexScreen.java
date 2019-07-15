package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SetSylladexScreen extends SylladexScreen
{
	
	private Modus modus;
	
	public SetSylladexScreen(Modus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 5;
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = modus.getItems();
		this.cards.clear();
		int columns = (stacks.size() + 1)/2;
		this.maxWidth = Math.max(mapWidth, 10 + (columns*CARD_WIDTH + (columns - 1)*5));
		this.maxHeight = mapHeight;
		super.updateContent();
		
		int start = Math.max(5, (mapWidth - (columns*CARD_WIDTH + (columns - 1)*5))/2);
		for(int i = 0; i < stacks.size(); i++)
			this.cards.add(new GuiCard(stacks.get(i), this, i, start + i/2*(CARD_WIDTH + 5), (mapHeight - 2*CARD_HEIGHT - 5)/2 + (i%2)*(CARD_HEIGHT + 5)));
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