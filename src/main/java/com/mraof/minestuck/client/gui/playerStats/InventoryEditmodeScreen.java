package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.EditmodeContainer;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

import java.time.LocalDate;
import java.time.Month;

public class InventoryEditmodeScreen extends PlayerStatsContainerScreen<EditmodeContainer>
{
	public static final String TITLE = "minestuck.deploy_list";

	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int leftArrowX = 7, rightArrowX = 151, arrowY = 23;
	
	public boolean more, less;
	
	public InventoryEditmodeScreen(int windowId, PlayerInventory playerInventory)
	{
		super(new EditmodeContainer(windowId, playerInventory), playerInventory, new TranslationTextComponent(TITLE));
		guiWidth = 176;
		guiHeight = 98;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int xcor, int ycor)
	{
		drawTabs();
		
		minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		LocalDate localdate = LocalDate.now();
		int d = localdate.getDayOfMonth();
		Month m = localdate.getMonth();
		boolean b1 = MinestuckConfig.SERVER.hardMode;
		boolean b2 = !b1 && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		this.blit(xOffset+leftArrowX, yOffset+arrowY, guiWidth + (b2?36:0), (less?0:18) + (b1?36:0), 18, 18);
		this.blit(xOffset+rightArrowX, yOffset+arrowY, guiWidth+18 + (b2?36:0), (more?0:18) + (b1?36:0), 18, 18);
		
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
				minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(false);
			} else if(more && xcor >= xOffset + rightArrowX && xcor < xOffset + rightArrowX + 18)
			{
				minecraft.getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(true);
			}
			if(packet != null)
			{
				MSPacketHandler.INSTANCE.sendToServer(packet);
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