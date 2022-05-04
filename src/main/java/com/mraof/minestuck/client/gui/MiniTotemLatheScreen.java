package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.inventory.MiniTotemLatheContainer;
import com.mraof.minestuck.tileentity.machine.MiniTotemLatheTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		font.draw(matrixStack, title.getString(), 8, 6, 4210752);
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
		int width = getScaledValue(menu.getProgress(), MiniTotemLatheTileEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		blit(matrixStack, x + progressX, y + progressY, 0, 0, width, progressHeight, progressWidth, progressHeight);
	}

	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, new StringTextComponent(menu.overrideStop() ? "STOP" : "GO"));
		addButton(goButton);
	}
}