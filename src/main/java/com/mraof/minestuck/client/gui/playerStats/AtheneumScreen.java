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
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/gui_inv_atheneum.png");
	private static final ResourceLocation icons = new ResourceLocation("minestuck", "textures/gui/icons.png");
	
	private static final int upArrowY = 14, downArrowY = 48, arrowX = 151;
	
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
		RenderSystem.setShaderTexture(0, guiBackground);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		LocalDate localdate = LocalDate.now();
		int d = localdate.getDayOfMonth();
		Month m = localdate.getMonth();
		boolean b1 = MinestuckConfig.SERVER.hardMode.get();
		boolean b2 = !b1 && (m == Month.APRIL && d == 13 || m == Month.JUNE && d == 12
				|| m == Month.OCTOBER && d == 25 || m == Month.NOVEMBER && d == 11
				|| m == Month.NOVEMBER && d == 27);
		this.blit(poseStack, xOffset+arrowX, yOffset+upArrowY, guiWidth + (b2?36:0), (less?0:18) + (b1?36:0), 18, 18);
		this.blit(poseStack, xOffset+arrowX, yOffset+downArrowY, guiWidth+18 + (b2?36:0), (more?0:18) + (b1?36:0), 18, 18);
		
		drawActiveTabAndIcons(poseStack);
		
	}
	
	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		
		this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 0x404040);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(xcor >= xOffset + arrowX && xcor < xOffset + arrowX + 18)
		{
			AtheneumPacket packet = null;
			if(less && ycor >= yOffset + upArrowY && ycor < yOffset + upArrowY + 18)
			{
				minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
				packet = AtheneumPacket.scroll(true);
			} else if(more && ycor >= yOffset + downArrowY && ycor < yOffset + downArrowY + 18)
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
