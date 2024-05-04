package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.musicplayer.CassetteContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CassetteContainerScreen extends AbstractContainerScreen<CassetteContainerMenu>
{
	private final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/cassette_container.png");
	
	public CassetteContainerScreen(CassetteContainerMenu container, Inventory pPlayerInventory, Component pTitle)
	{
		super(container, pPlayerInventory, pTitle);
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float par1, int par2, int par3)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		//draw background
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
}
