package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.GristWidgetContainer;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.tileentity.machine.GristWidgetTileEntity;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class GristWidgetScreen extends MachineScreen<GristWidgetContainer>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/widget.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/widget.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public GristWidgetScreen(GristWidgetContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(GristWidgetTileEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets prgress bar information
		progressX = 54;
		progressY = 23;
		progressWidth = 71;
		progressHeight = 10;
		goX = 72;
		goY = 31;
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
		font.draw(matrixStack, this.title.getString(), 8, 6, 0xFFFFFF);
		//draws "Inventory" or your regional equivalent
		font.draw(matrixStack, this.inventory.getDisplayName().getString(), 8, imageHeight - 96 + 2, 0xFFFFFF);
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			GristSet set = GristWidgetTileEntity.getGristWidgetResult(menu.getSlot(0).getItem(), minecraft.level);

			GuiUtil.drawGristBoard(matrixStack, set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, font);
			
			int cost = GristWidgetTileEntity.getGristWidgetBoondollarValue(set);
			long has = ClientPlayerData.getBoondollars();
			String costText = GuiUtil.addSuffix(cost)+"\u00a3("+GuiUtil.addSuffix(has)+")";
			font.draw(matrixStack, costText, imageWidth - 9 - font.width(costText), imageHeight - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00);
			
			ITextComponent tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.GRIST_WIDGET, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				this.renderTooltip(matrixStack, tooltip, mouseX - this.leftPos, mouseY - this.topPos);
			else if(mouseY - topPos >= imageHeight - 96 + 3 && mouseY - topPos < imageHeight - 96 + 3 + font.lineHeight)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - leftPos < imageWidth - 9 - font.width("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(costText))
					renderTooltip(matrixStack, new StringTextComponent(String.valueOf(cost)), mouseX - this.leftPos, mouseY - this.topPos);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - leftPos < imageWidth - 9 - font.width(")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(GuiUtil.addSuffix(has)+")"))
					renderTooltip(matrixStack, new StringTextComponent(String.valueOf(has)), mouseX - this.leftPos, mouseY - this.topPos);
			}
		}
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
		int width = getScaledValue(menu.getProgress(), GristWidgetTileEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		int height = progressHeight;
		blit(matrixStack, x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, new StringTextComponent(menu.overrideStop() ? "STOP" : "GO"));
		addButton(goButton);
		if(MinestuckConfig.SERVER.disableGristWidget.get())
			goButton.active = false;
	}
}