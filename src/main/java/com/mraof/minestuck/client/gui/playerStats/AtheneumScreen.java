package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.network.AtheneumPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

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
	protected void renderBg(PoseStack poseStack, float par1, int xcor, int ycor)
	{
		drawTabs(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI_BACKGROUND);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		LocalDate localdate = LocalDate.now();
		int d = localdate.getDayOfMonth();
		Month m = localdate.getMonth();
		boolean b1 = MinestuckConfig.SERVER.hardMode.get();
		boolean b2 = !b1 && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		
		this.blit(poseStack, xOffset+ ARROW_X, yOffset+ UP_ARROW_Y, guiWidth + (b2?36:0), (less?0:18) + (b1?36:0), 18, 18);
		this.blit(poseStack, xOffset+ ARROW_X, yOffset+ DOWN_ARROW_Y, guiWidth+18 + (b2?36:0), (more?0:18) + (b1?36:0), 18, 18);
		
		drawActiveTabAndIcons(poseStack);
		
	}
	
	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
	{
		this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 0x404040);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(xcor >= xOffset + ARROW_X && xcor < xOffset + ARROW_X + 18)
		{
			AtheneumPacket packet = null;
			if(less && ycor >= yOffset + UP_ARROW_Y && ycor < yOffset + UP_ARROW_Y + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = AtheneumPacket.scroll(true);
			}
			else if(more && ycor >= yOffset + DOWN_ARROW_Y && ycor < yOffset + DOWN_ARROW_Y + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = AtheneumPacket.scroll(false);
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
