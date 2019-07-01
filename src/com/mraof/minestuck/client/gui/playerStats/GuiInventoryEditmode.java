package com.mraof.minestuck.client.gui.playerStats;

import java.time.LocalDate;
import java.time.Month;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiInventoryEditmode extends GuiPlayerStatsContainer
{

	private ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	private ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int leftArrowX = 7, rightArrowX = 151, arrowY = 23;
	
	public boolean more, less;
	public ContainerEditmode inventoryEditmode;
	
	public GuiInventoryEditmode()
	{
		super(new ContainerEditmode(Minecraft.getInstance().player));
		guiWidth = 176;
		guiHeight = 98;
		inventoryEditmode = (ContainerEditmode) this.inventorySlots;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		drawTabs();
		
		mc.getTextureManager().bindTexture(guiBackground);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		LocalDate localdate = LocalDate.now();
		int d = localdate.getDayOfMonth();
		Month m = localdate.getMonth();
		boolean b1 = MinestuckConfig.clientHardMode;
		boolean b2 = !b1 && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		this.drawTexturedModalRect(xOffset+leftArrowX, yOffset+arrowY, guiWidth + (b2?36:0), (less?0:18) + (b1?36:0), 18, 18);
		this.drawTexturedModalRect(xOffset+rightArrowX, yOffset+arrowY, guiWidth+18 + (b2?36:0), (more?0:18) + (b1?36:0), 18, 18);
		
		drawActiveTabAndIcons();
		
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(ycor >= yOffset + arrowY && ycor < yOffset + arrowY + 18)
		{
			EditmodeInventoryPacket packet = null;
			if(less && xcor >= xOffset + leftArrowX && xcor < xOffset + leftArrowX + 18)
			{
				mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(false);
			} else if(more && xcor >= xOffset + rightArrowX && xcor < xOffset + rightArrowX + 18)
			{
				mc.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(true);
			}
			if(packet != null)
			{
				MinestuckPacketHandler.INSTANCE.sendToServer(packet);
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int xcor, int ycor)
	{
		drawTabTooltip(xcor, ycor);
	}
	
}
