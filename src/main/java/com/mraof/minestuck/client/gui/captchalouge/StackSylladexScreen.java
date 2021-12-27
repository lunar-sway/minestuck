package com.mraof.minestuck.client.gui.captchalouge;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class StackSylladexScreen extends SylladexScreen
{
	
	private Modus modus;
	
	public StackSylladexScreen(Modus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 0;
	}
	
	@Override
	public void updateContent()
	{
		NonNullList<ItemStack> stacks = modus.getItems();
		this.cards.clear();
		this.maxWidth = Math.max(mapWidth, 10 + (stacks.size()*CARD_WIDTH + (stacks.size() - 1)*5));
		this.maxHeight = mapHeight;
		super.updateContent();
		int start = Math.max(5, (mapWidth - (stacks.size()*CARD_WIDTH + (stacks.size() - 1)*5))/2);
		
		for(int i = 0; i < stacks.size(); i++)
			this.cards.add(new GuiCard(stacks.get(i), this, i == 0 ? 0 : -1, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2));
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
	public void drawGuiMap(MatrixStack matrixStack, int xcor, int ycor)
	{
		super.drawGuiMap(matrixStack, xcor, ycor);
		
		if(!cards.isEmpty())
		{
			int startX = Math.max(0, cards.get(0).xPos + CARD_WIDTH - mapX);
			int endX = Math.min(mapWidth, cards.get(cards.size() - 1).xPos - mapX);
			int y = mapHeight/2 + 1;
			fill(matrixStack, startX, y, endX, y + 2, 0xFF000000);
		}
	}
	
}
