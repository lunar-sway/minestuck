package com.mraof.minestuck.client.gui.captchalouge;

import net.minecraft.item.ItemStack;

import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

public class HashmapGuiHandler extends SylladexGuiHandler
{
	
	private Modus modus;
	
	public HashmapGuiHandler(Modus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 4;
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
			this.cards.add(new GuiCard(stacks[i], this, i, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2)
			{
				@Override
				public void onClick(int mouseButton)
				{
					if(this.item == null && mouseButton == 1)
					{
						MinestuckPacket packet = MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.GET, this.index, true);
						MinestuckChannelHandler.sendToServer(packet);
					} else super.onClick(mouseButton);
				}
			});
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
		int y = mapHeight/2 - CARD_HEIGHT/2 - 3 - mc.fontRendererObj.FONT_HEIGHT;
		int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
		
		for(int i = 0; i < cards.size(); i++)
		{
			String s = String.valueOf(i);
			int width = mc.fontRendererObj.getStringWidth(s);
			int x = start + i*(CARD_WIDTH + 5) + CARD_WIDTH/2 - mapX - width/2;
			if(x + width > 0 && x < mapWidth)
				mc.fontRendererObj.drawString(s, x, y, 0x000000);
		}
	}
	
}