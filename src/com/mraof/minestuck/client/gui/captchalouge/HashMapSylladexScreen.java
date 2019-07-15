package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;

@OnlyIn(Dist.CLIENT)
public class HashMapSylladexScreen extends SylladexScreen
{
	
	private HashMapModus modus;
	protected Button guiButton;
	
	public HashMapSylladexScreen(HashMapModus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 4;
	}
	
	@Override
	public void init()
	{
		super.init();
		guiButton = new GuiButtonExt((width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, "", button -> changeSetting());
		addButton(guiButton);
	}
	
	@Override
	public void render(int xcor, int ycor, float f)
	{
		guiButton.x = (width - GUI_WIDTH)/2 + 15;
		guiButton.y = (height - GUI_HEIGHT)/2 + 175;
		boolean active = MinestuckConfig.clientHashmapChat == 0 ? modus.ejectByChat : MinestuckConfig.clientHashmapChat == 1;
		guiButton.setMessage(I18n.format(active ? "gui.ejectByChat.on" : "gui.ejectByChat.off"));
		guiButton.active = MinestuckConfig.clientHashmapChat == 0;
		super.render(xcor, ycor, f);
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
			this.cards.add(new GuiCard(stacks.get(i), this, i, start + i*(CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT)/2)
			{
				@Override
				public void onClick(int mouseButton)
				{
					if(this.item != null && mouseButton == 1)
					{
						CaptchaDeckPacket packet = CaptchaDeckPacket.get(this.index, true);
						MinestuckPacketHandler.sendToServer(packet);
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
		int y = mapHeight/2 - CARD_HEIGHT/2 - 3 - font.FONT_HEIGHT;
		int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
		
		for(int i = 0; i < cards.size(); i++)
		{
			String s = String.valueOf(i);
			int width = font.getStringWidth(s);
			int x = start + i*(CARD_WIDTH + 5) + CARD_WIDTH/2 - mapX - width/2;
			if(x + width > 0 && x < mapWidth)
				font.drawString(s, x, y, 0x000000);
		}
	}
	
	private void changeSetting()
	{
		if(MinestuckConfig.clientHashmapChat == 0)
		{
			modus.ejectByChat = !modus.ejectByChat;
			MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.modusParam((byte) 0, modus.ejectByChat ? 1 : 0));
		}
	}
}