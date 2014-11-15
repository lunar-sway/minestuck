package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.ContainerCaptchaDeck;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiCaptchaDeck extends GuiPlayerStatsContainer
{
	
	private static final ResourceLocation guiCaptchaDeck = new ResourceLocation("minestuck", "textures/gui/captchaDeck.png");
	
	private GuiButton modusButton, sylladexMap;
	private ContainerCaptchaDeck container;
	
	public GuiCaptchaDeck()
	{
		super(new ContainerCaptchaDeck(Minecraft.getMinecraft().thePlayer));
		container = (ContainerCaptchaDeck) inventorySlots;
		guiWidth = 178;
		guiHeight= 145;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		modusButton = new GuiButton(1, xOffset + 102, yOffset + 31, 50, 18, StatCollector.translateToLocal("gui.useItem"));
		sylladexMap = new GuiButton(1, xOffset + 6, yOffset + 31, 60, 18, StatCollector.translateToLocal("gui.sylladex"));
		buttonList.add(modusButton);
		buttonList.add(sylladexMap);
		sylladexMap.enabled = CaptchaDeckHandler.clientSideModus != null;
		modusButton.enabled = container.inventory.getStackInSlot(0) != null;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		sylladexMap.enabled = CaptchaDeckHandler.clientSideModus != null;
		modusButton.enabled = container.inventory.getStackInSlot(0) != null;
		
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiCaptchaDeck);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor)
	{
		drawTabTooltip(xcor, ycor);
		
		String message = StatCollector.translateToLocal("gui.captchaDeck.name");
		mc.fontRenderer.drawString(message, (this.width / 2) - mc.fontRenderer.getStringWidth(message) / 2 - guiLeft, yOffset + 12 - guiTop, 0x404040);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button == this.modusButton && container.inventory.getStackInSlot(0) != null)
		{
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.MODUS));
		}
		else if(button == this.sylladexMap && CaptchaDeckHandler.clientSideModus != null)
		{
			mc.displayGuiScreen(CaptchaDeckHandler.clientSideModus.getGuiHandler());
		}
	}
	
}
