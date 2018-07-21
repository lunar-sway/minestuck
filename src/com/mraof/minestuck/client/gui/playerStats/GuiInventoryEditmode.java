package com.mraof.minestuck.client.gui.playerStats;

import java.io.IOException;
import java.util.Calendar;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiInventoryEditmode extends GuiPlayerStatsContainer
{

	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	private ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int leftArrowX = 7, rightArrowX = 151, arrowY = 23;
	
	public boolean more, less;
	
	public GuiInventoryEditmode()
	{
		super(new ContainerEditmode(FMLClientHandler.instance().getClientPlayerEntity()));
		guiWidth = 176;
		guiHeight = 98;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiBackground);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		Calendar calendar = mc.world.getCurrentDate();
		boolean b1 = MinestuckConfig.clientHardMode;
		boolean b2 = !b1 && (calendar.get(2) + 1 == 4 && calendar.get(5) == 13 || calendar.get(2) + 1 == 6 && calendar.get(5) == 12
				|| calendar.get(2) + 1 == 10 && calendar.get(5) == 25 || calendar.get(2) + 1 == 11 && calendar.get(5) == 11
				|| calendar.get(2) + 1 == 11 && calendar.get(5) == 27);
		this.drawTexturedModalRect(xOffset+leftArrowX, yOffset+arrowY, guiWidth + (b2?36:0), (less?0:18) + (b1?36:0), 18, 18);
		this.drawTexturedModalRect(xOffset+rightArrowX, yOffset+arrowY, guiWidth+18 + (b2?36:0), (more?0:18) + (b1?36:0), 18, 18);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		if(ycor >= yOffset + arrowY && ycor < yOffset + arrowY + 18)
		{
			MinestuckPacket packet = null;
			if(less && xcor >= xOffset + leftArrowX && xcor < xOffset + leftArrowX + 18)
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = MinestuckPacket.makePacket(Type.INVENTORY, 0, false);
			} else if(more && xcor >= xOffset + rightArrowX && xcor < xOffset + rightArrowX + 18)
			{
				mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = MinestuckPacket.makePacket(Type.INVENTORY, 0, true);
			}
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
