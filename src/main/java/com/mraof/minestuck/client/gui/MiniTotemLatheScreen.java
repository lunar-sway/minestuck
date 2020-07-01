package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.inventory.MiniTotemLatheContainer;
import com.mraof.minestuck.tileentity.MiniTotemLatheTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MiniTotemLatheScreen extends MachineScreen<MiniTotemLatheContainer>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/totem_lathe.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/totem_lathe.png");
	
	private static final int progressX = 81;
	private static final int progressY = 33;
	private static final int progressWidth = 44;
	private static final int progressHeight = 17;
	private static final int goX = 85;
	private static final int goY = 53;
	
	public MiniTotemLatheScreen(MiniTotemLatheContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(MiniTotemLatheTileEntity.TYPE, screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		font.drawString(title.getFormattedText(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		font.drawString(playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(x, y, 0, 0, xSize, ySize);

		//draw progress bar
		this.minecraft.getTextureManager().bindTexture(PROGRESS);
		int width = getScaledValue(container.getProgress(), MiniTotemLatheTileEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		blit(x + progressX, y + progressY, 0, 0, width, progressHeight, progressWidth, progressHeight);
	}

	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, container.overrideStop() ? "STOP" : "GO");
		addButton(goButton);
	}
}