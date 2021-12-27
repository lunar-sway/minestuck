package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.MiniCruxtruderContainer;
import com.mraof.minestuck.tileentity.machine.MiniCruxtruderTileEntity;
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
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		font.draw(matrixStack, this.title.getString(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		font.draw(matrixStack, inventory.getDisplayName().getString(), 8, imageHeight - 96 + 2, 4210752);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float par1, int par2, int par3)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.minecraft.getTextureManager().bind(BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(matrixStack, x, y, 0, 0, imageWidth, imageHeight);

		//draw progress bar
		this.minecraft.getTextureManager().bind(PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(menu.getProgress(), MiniCruxtruderTileEntity.DEFAULT_MAX_PROGRESS, progressHeight);
		blit(matrixStack, x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
	}
}