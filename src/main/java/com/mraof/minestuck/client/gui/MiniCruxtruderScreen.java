package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.MiniCruxtruderContainer;
import com.mraof.minestuck.tileentity.MiniCruxtruderTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MiniCruxtruderScreen extends MachineScreen<MiniCruxtruderContainer>
{
	
	private ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/cruxtruder.png");
	private ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/cruxtruder.png");
	//private EntityPlayer player;
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public MiniCruxtruderScreen(MiniCruxtruderContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(MiniCruxtruderTileEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 82;
		progressY = 42;
		progressWidth = 10;
		progressHeight = 13;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		font.drawString(matrixStack, this.title.getString(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		font.drawString(matrixStack, playerInventory.getDisplayName().getString(), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float par1, int par2, int par3)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(matrixStack, x, y, 0, 0, xSize, ySize);

		//draw progress bar
		this.minecraft.getTextureManager().bindTexture(PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(container.getProgress(), MiniCruxtruderTileEntity.DEFAULT_MAX_PROGRESS, progressHeight);
		blit(matrixStack, x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
	}
}