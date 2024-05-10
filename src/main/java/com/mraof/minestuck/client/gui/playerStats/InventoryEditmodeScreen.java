package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.EditmodeSettingsScreen;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.network.editmode.EditmodeInventoryPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.time.LocalDate;
import java.time.Month;

public class InventoryEditmodeScreen extends PlayerStatsContainerScreen<EditmodeMenu>
{
	public static final String TITLE = "minestuck.deploy_list";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	public static final ResourceLocation SETTINGS_ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/settings.png");
	private static final int LEFT_ARROW_X = 7, RIGHT_ARROW_X = 151, ARROW_Y = 23;
	private static final int SETTINGS_X = 80, SETTINGS_Y = 54;
	public static final int ARROW_SIZE = 18, SETTINGS_SIZE = 16; //same width and height
	
	public boolean more, less;
	
	private Player player;
	
	public InventoryEditmodeScreen(int windowId, Inventory playerInventory)
	{
		super(new EditmodeMenu(windowId, playerInventory), playerInventory, Component.translatable(TITLE));
		guiWidth = 176;
		guiHeight = 98;
		
		player = playerInventory.player;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		if(overtopSettingsIconBounds(mouseX, mouseY, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
			guiGraphics.renderTooltip(font, Component.translatable("Editmode settings"), mouseX, mouseY);
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
		
		guiGraphics.blit(SETTINGS_ICON, xOffset + SETTINGS_X, yOffset + SETTINGS_Y, SETTINGS_SIZE, SETTINGS_SIZE, 0, 0, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(overtopSettingsIconBounds(xcor, ycor, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
		{
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			this.minecraft.setScreen(null);
			this.minecraft.setScreen(new EditmodeSettingsScreen(player));
		}
		
		boolean clickedInArrowIconYRange = ycor >= yOffset + ARROW_Y && ycor < yOffset + ARROW_Y + ARROW_SIZE;
		if(clickedInArrowIconYRange)
		{
			CustomPacketPayload packet = null;
			
			boolean clickedInLeftArrowXRange = less && xcor >= xOffset + LEFT_ARROW_X && xcor < xOffset + LEFT_ARROW_X + ARROW_SIZE;
			boolean clickedInRightArrowXRange = more && xcor >= xOffset + RIGHT_ARROW_X && xcor < xOffset + RIGHT_ARROW_X + ARROW_SIZE;
			if(clickedInLeftArrowXRange)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = new EditmodeInventoryPackets.Scroll(false);
			} else if(clickedInRightArrowXRange)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = new EditmodeInventoryPackets.Scroll(true);
			}
			if(packet != null)
			{
				PacketDistributor.SERVER.noArg().send(packet);
				return true;
			}
		}
		
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	public static boolean overtopSettingsIconBounds(double xPos, double yPos, int xOffset, int yOffset, int SETTINGS_X, int SETTINGS_Y)
	{
		boolean inYRange = yPos >= yOffset + SETTINGS_Y && yPos < yOffset + SETTINGS_Y + SETTINGS_SIZE;
		boolean inXRange = xPos >= xOffset + SETTINGS_X && xPos < xOffset + SETTINGS_X + SETTINGS_SIZE;
		
		return inYRange && inXRange;
	}
}
