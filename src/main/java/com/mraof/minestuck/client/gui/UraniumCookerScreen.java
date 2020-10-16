package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.UraniumCookerContainer;
import com.mraof.minestuck.tileentity.machine.UraniumCookerTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class UraniumCookerScreen extends MachineScreen<UraniumCookerContainer>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/uranium_cooker.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/uranium_cooker.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public UraniumCookerScreen(UraniumCookerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(UraniumCookerTileEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 67;
		progressY = 24;
		progressWidth = 35;
		progressHeight = 39;
		goX = 69;
		goY = 69;
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
		//draws "Cookalyzer"
		font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
		
		//draws "Inventory" or your regional equivalent
		font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		//draw background
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(x, y, 0, 0, xSize, ySize);
		
		//draw progress bar
		this.minecraft.getTextureManager().bindTexture(PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(container.getFuel(), UraniumCookerTileEntity.getMaxFuel(), progressHeight);
		blit(x+progressX, y+progressY+progressHeight-height, 0, progressHeight-height, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, container.overrideStop() ? "STOP" : "GO");
		addButton(goButton);
	}
}