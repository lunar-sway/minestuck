package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiButtonImpl;
import com.mraof.minestuck.inventory.captchalogue.HashmapModus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HashmapGuiHandler extends SylladexGuiHandler
{
	
	private HashmapModus modus;
	protected GuiButtonImpl guiButton;
	
	public HashmapGuiHandler(HashmapModus modus)
	{
		super();
		this.modus = modus;
		this.textureIndex = 4;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		guiButton = new GuiButtonImpl(this, 0, (width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, "");
		buttons.add(guiButton);
	}
	
	@Override
	public void render(int xcor, int ycor, float f)
	{
		guiButton.x = (width - GUI_WIDTH)/2 + 15;
		guiButton.y = (height - GUI_HEIGHT)/2 + 175;
		boolean active = MinestuckConfig.clientHashmapChat == 0 ? modus.ejectByChat : MinestuckConfig.clientHashmapChat == 1;
		guiButton.displayString = I18n.format(active ? "gui.ejectByChat.on" : "gui.ejectByChat.off");
		guiButton.enabled = MinestuckConfig.clientHashmapChat == 0;
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
						MinestuckPacket packet = MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.GET, this.index, true);
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
		int y = mapHeight/2 - CARD_HEIGHT/2 - 3 - mc.fontRenderer.FONT_HEIGHT;
		int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
		
		for(int i = 0; i < cards.size(); i++)
		{
			String s = String.valueOf(i);
			int width = mc.fontRenderer.getStringWidth(s);
			int x = start + i*(CARD_WIDTH + 5) + CARD_WIDTH/2 - mapX - width/2;
			if(x + width > 0 && x < mapWidth)
				mc.fontRenderer.drawString(s, x, y, 0x000000);
		}
	}
	
	@Override
	public void actionPerformed(GuiButtonImpl button)
	{
		super.actionPerformed(button);
		if(button == this.guiButton && MinestuckConfig.clientHashmapChat == 0)
		{
			modus.ejectByChat = !modus.ejectByChat;
			MinestuckPacketHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.VALUE, (byte) 0, modus.ejectByChat ? 1 : 0));
		}
	}
}