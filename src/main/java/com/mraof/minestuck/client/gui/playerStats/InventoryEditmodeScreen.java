package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.network.EditmodeInventoryPacket;
import com.mraof.minestuck.network.EditmodeTeleportPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

import java.time.LocalDate;
import java.time.Month;

public class InventoryEditmodeScreen extends PlayerStatsContainerScreen<EditmodeMenu>
{
	public static final String TITLE = "minestuck.deploy_list";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	public static final ResourceLocation SETTINGS_ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/settings.png");
	private static final int LEFT_ARROW_X = 7, RIGHT_ARROW_X = 151, ARROW_Y = 23;
	private static final int SETTINGS_X = 76, SETTINGS_Y = 54;
	private static final int ARROW_SIZE = 18, SETTINGS_SIZE = 16; //same width and height
	
	public boolean more, less;
	
	public InventoryEditmodeScreen(int windowId, Inventory playerInventory)
	{
		super(new EditmodeMenu(windowId, playerInventory), playerInventory, Component.translatable(TITLE));
		guiWidth = 176;
		guiHeight = 98;
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float par1, int xcor, int ycor)
	{
		drawTabs(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(GUI_BACKGROUND, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		LocalDate localdate = LocalDate.now();
		int d = localdate.getDayOfMonth();
		Month m = localdate.getMonth();
		boolean b1 = MinestuckConfig.SERVER.hardMode.get();
		boolean b2 = !b1 && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		
		guiGraphics.blit(GUI_BACKGROUND, xOffset + LEFT_ARROW_X, yOffset + ARROW_Y, guiWidth + (b2 ? 36 : 0), (less ? 0 : ARROW_SIZE) + (b1 ? 36 : 0), ARROW_SIZE, ARROW_SIZE);
		guiGraphics.blit(GUI_BACKGROUND, xOffset + RIGHT_ARROW_X, yOffset + ARROW_Y, guiWidth + 18 + (b2 ? 36 : 0), (more ? 0 : ARROW_SIZE) + (b1 ? 36 : 0), ARROW_SIZE, ARROW_SIZE);
		
		drawActiveTabAndIcons(guiGraphics);
		
		//TODO add more options (potentially work with icons)
		guiGraphics.blit(SETTINGS_ICON, xOffset + SETTINGS_X, yOffset + SETTINGS_Y, SETTINGS_SIZE, SETTINGS_SIZE, 0, 0, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
	}
	
	private void teleport()
	{
		EditmodeTeleportPacket packet = new EditmodeTeleportPacket();
		MSPacketHandler.sendToServer(packet);
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		boolean clickedInSettingsIconYRange = ycor >= yOffset + SETTINGS_Y && ycor < yOffset + SETTINGS_Y + SETTINGS_SIZE;
		boolean clickedInSettingsIconXRange = xcor >= xOffset + SETTINGS_X && xcor < xOffset + SETTINGS_X + SETTINGS_SIZE;
		
		if(clickedInSettingsIconYRange && clickedInSettingsIconXRange)
		{
			teleport();
		}
		
		boolean clickedInArrowIconYRange = ycor >= yOffset + ARROW_Y && ycor < yOffset + ARROW_Y + ARROW_SIZE;
		if(clickedInArrowIconYRange)
		{
			EditmodeInventoryPacket packet = null;
			
			boolean clickedInLeftArrowXRange = less && xcor >= xOffset + LEFT_ARROW_X && xcor < xOffset + LEFT_ARROW_X + ARROW_SIZE;
			boolean clickedInRightArrowXRange = more && xcor >= xOffset + RIGHT_ARROW_X && xcor < xOffset + RIGHT_ARROW_X + ARROW_SIZE;
			if(clickedInLeftArrowXRange)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(false);
			} else if(clickedInRightArrowXRange)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
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
}