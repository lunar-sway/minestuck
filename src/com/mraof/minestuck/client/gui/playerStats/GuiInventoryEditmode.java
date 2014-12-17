package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;

import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiInventoryEditmode extends GuiPlayerStatsContainer
{

	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/guiInvEditmode.png");
	private ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int leftArrowX = 7, rightArrowX = 151, arrowY = 23;
	
	public boolean more, less;
	
	public GuiInventoryEditmode()
	{
		super(new ContainerEditmode());
		guiWidth = 176;
		guiHeight = 98;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiBackground);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		this.drawTexturedModalRect(xOffset+leftArrowX, yOffset+arrowY, guiWidth, less ? 0 : 18, 18, 18);
		this.drawTexturedModalRect(xOffset+rightArrowX, yOffset+arrowY, guiWidth+18, more ? 0 : 18, 18, 18);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		if(ycor >= yOffset + arrowY && ycor < yOffset + arrowY + 18)
		{
			MinestuckPacket packet = null;
			if(less && xcor >= xOffset + leftArrowX && xcor < xOffset + leftArrowX + 18)
				packet = MinestuckPacket.makePacket(Type.INVENTORY, 0, false);
			else if(more && xcor >= xOffset + rightArrowX && xcor < xOffset + rightArrowX + 18)
				packet = MinestuckPacket.makePacket(Type.INVENTORY, 0, true);
			if(packet != null)
				MinestuckChannelHandler.sendToServer(packet);
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor)
	{
		drawTabTooltip(xcor, ycor);
	}
	
}
