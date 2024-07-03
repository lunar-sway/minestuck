package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.network.editmode.AtheneumPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import java.time.LocalDate;
import java.time.Month;

public class AtheneumScreen extends PlayerStatsContainerScreen<AtheneumMenu>
{
	public static final String TITLE = "minestuck.atheneum";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/gui_inv_atheneum.png");
	private static final int UP_ARROW_Y = 14, DOWN_ARROW_Y = 48, ARROW_X = 151;
	
	public boolean more, less;
	
	public AtheneumScreen(int windowId, Inventory playerInventory)
	{
		super(new AtheneumMenu(windowId, playerInventory), playerInventory, Component.translatable(TITLE));
		guiWidth = 176;
		guiHeight = 98;
		
		titleLabelX = 6;
		titleLabelY = 4;
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
		boolean hardmode = MinestuckConfig.SERVER.hardMode.get();
		boolean memeNumberDate = !hardmode && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		
		guiGraphics.blit(GUI_BACKGROUND, xOffset+ ARROW_X, yOffset+ UP_ARROW_Y, guiWidth + (memeNumberDate?36:0), (less?0:18) + (hardmode?36:0), 18, 18);
		guiGraphics.blit(GUI_BACKGROUND, xOffset+ ARROW_X, yOffset+ DOWN_ARROW_Y, guiWidth+18 + (memeNumberDate?36:0), (more?0:18) + (hardmode?36:0), 18, 18);
		drawActiveTabAndIcons(guiGraphics);
		
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(xcor >= xOffset + ARROW_X && xcor < xOffset + ARROW_X + 18)
		{
			AtheneumPackets.Scroll packet = null;
			if(less && ycor >= yOffset + UP_ARROW_Y && ycor < yOffset + UP_ARROW_Y + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = new AtheneumPackets.Scroll(true);
			}
			else if(more && ycor >= yOffset + DOWN_ARROW_Y && ycor < yOffset + DOWN_ARROW_Y + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = new AtheneumPackets.Scroll(false);
			}
			if(packet != null)
			{
				PacketDistributor.SERVER.noArg().send(packet);
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
	{
		if(mouseX >= xOffset && mouseX < xOffset + guiWidth)
		{
			if(mouseY >= yOffset && mouseY < yOffset + guiHeight)
			{
				AtheneumPackets.Scroll packet = null;
				if(less && scrollY > 0)
					packet = new AtheneumPackets.Scroll(true);
				else if(more && scrollY < 0)
					packet = new AtheneumPackets.Scroll(false);
				
				if(packet != null)
				{
					PacketDistributor.SERVER.noArg().send(packet);
					return true;
				}
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
}
