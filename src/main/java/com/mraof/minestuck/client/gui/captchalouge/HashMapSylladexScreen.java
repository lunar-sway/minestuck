package com.mraof.minestuck.client.gui.captchalouge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.HashMapModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class HashMapSylladexScreen extends SylladexScreen
{
	public static final String EJECT_BY_CHAT_ON = "minestuck.eject_by_chat.on";
	public static final String EJECT_BY_CHAT_OFF = "minestuck.eject_by_chat.off";
	
	private final HashMapModus modus;
	private Button guiButton;
	
	public HashMapSylladexScreen(Modus modus)
	{
		super();
		this.modus = (HashMapModus) modus;
		this.textureIndex = 4;
	}
	
	@Override
	public void init()
	{
		super.init();
		guiButton = new ExtendedButton((width - GUI_WIDTH)/2 + 15, (height - GUI_HEIGHT)/2 + 175, 120, 20, Component.empty(), button -> changeSetting());
		addRenderableWidget(guiButton);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float f)
	{
		guiButton.x = (width - GUI_WIDTH)/2 + 15;
		guiButton.y = (height - GUI_HEIGHT)/2 + 175;
		boolean active = MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.BOTH ? modus.ejectByChat : MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.ON;
		guiButton.setMessage(Component.translatable(active ? EJECT_BY_CHAT_ON : EJECT_BY_CHAT_OFF));
		guiButton.active = MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.BOTH;
		super.render(poseStack, mouseX, mouseY, f);
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
						MSPacketHandler.sendToServer(packet);
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
	public void drawGuiMap(PoseStack poseStack, int mouseX, int mouseY)
	{
		super.drawGuiMap(poseStack, mouseX, mouseY);
		int y = mapHeight/2 - CARD_HEIGHT/2 - 3 - font.lineHeight;
		int start = Math.max(5, (mapWidth - (cards.size()*CARD_WIDTH + (cards.size() - 1)*5))/2);
		
		for(int i = 0; i < cards.size(); i++)
		{
			String s = String.valueOf(i);
			int width = font.width(s);
			int x = start + i*(CARD_WIDTH + 5) + CARD_WIDTH/2 - mapX - width/2;
			if(x + width > 0 && x < mapWidth)
				font.draw(poseStack, s, x, y, 0x000000);
		}
	}
	
	private void changeSetting()
	{
		if(MinestuckConfig.SERVER.hashmapChatModusSetting.get() == MinestuckConfig.AvailableOptions.BOTH)
		{
			modus.ejectByChat = !modus.ejectByChat;
			MSPacketHandler.sendToServer(CaptchaDeckPacket.modusParam((byte) 0, modus.ejectByChat ? 1 : 0));
		}
	}
}