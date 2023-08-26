package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.time.LocalDate;
import java.time.Month;

public class InventoryEditmodeScreen extends PlayerStatsContainerScreen<EditmodeMenu>
{
	public static final String TITLE = "minestuck.deploy_list";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/gui_inv_editmode.png");
	private static final int LEFT_ARROW_X = 7, RIGHT_ARROW_X = 151, ARROW_Y = 23;
	
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
		
		guiGraphics.blit(GUI_BACKGROUND, xOffset + LEFT_ARROW_X, yOffset + ARROW_Y, guiWidth + (b2 ? 36 : 0), (less ? 0 : 18) + (b1 ? 36 : 0), 18, 18);
		guiGraphics.blit(GUI_BACKGROUND, xOffset + RIGHT_ARROW_X, yOffset + ARROW_Y, guiWidth + 18 + (b2 ? 36 : 0), (more ? 0 : 18) + (b1 ? 36 : 0), 18, 18);
		
		drawActiveTabAndIcons(guiGraphics);
		
		//TODO add more options (potentially work with icons)
		addRenderableWidget(new ExtendedButton(xOffset + LEFT_ARROW_X + 31, yOffset + ARROW_Y + 31, 100, 16, Component.literal("TELEPORT CLOSEST"), button -> teleport()));
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
		if(ycor >= yOffset + ARROW_Y && ycor < yOffset + ARROW_Y + 18)
		{
			EditmodeInventoryPacket packet = null;
			if(less && xcor >= xOffset + LEFT_ARROW_X && xcor < xOffset + LEFT_ARROW_X + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = EditmodeInventoryPacket.scroll(false);
			} else if(more && xcor >= xOffset + RIGHT_ARROW_X && xcor < xOffset + RIGHT_ARROW_X + 18)
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