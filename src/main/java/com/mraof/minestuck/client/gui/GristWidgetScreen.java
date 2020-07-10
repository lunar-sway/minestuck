package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.GristWidgetContainer;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.tileentity.GristWidgetTileEntity;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

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
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		font.drawString(this.title.getFormattedText(), 8, 6, 0xFFFFFF);
		//draws "Inventory" or your regional equivalent
		font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 0xFFFFFF);
		if (container.getSlot(0).getHasStack())
		{
			//Render grist requirements
			GristSet set = GristWidgetTileEntity.getGristWidgetResult(container.getSlot(0).getStack(), minecraft.world);

			GuiUtil.drawGristBoard(set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, font);
			
			int cost = GristWidgetTileEntity.getGristWidgetBoondollarValue(set);
			long has = ClientPlayerData.boondollars;
			String costText = GuiUtil.addSuffix(cost)+"£("+GuiUtil.addSuffix(has)+")";
			font.drawString(costText, xSize - 9 - font.getStringWidth(costText), ySize - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00);
			
			List<String> tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.GRIST_WIDGET, mouseX - this.guiLeft, mouseY - this.guiTop, 9, 45, font);
			if(!tooltip.isEmpty())
				this.renderTooltip(tooltip, mouseX - this.guiLeft, mouseY - this.guiTop, font);
			else if(mouseY - guiTop >= ySize - 96 + 3 && mouseY - guiTop < ySize - 96 + 3 + font.FONT_HEIGHT)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - guiLeft < xSize - 9 - font.getStringWidth("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - guiLeft >= xSize - 9 - font.getStringWidth(costText))
					renderTooltip(String.valueOf(cost), mouseX - this.guiLeft, mouseY - this.guiTop);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - guiLeft < xSize - 9 - font.getStringWidth(")")
						&& mouseX - guiLeft >= xSize - 9 - font.getStringWidth(GuiUtil.addSuffix(has)+")"))
					renderTooltip(String.valueOf(has), mouseX - this.guiLeft, mouseY - this.guiTop);
			}
		}
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
		int width = getScaledValue(container.getProgress(), GristWidgetTileEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		int height = progressHeight;
		blit(x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, container.overrideStop() ? "STOP" : "GO");
		addButton(goButton);
		if(MinestuckConfig.clientDisableGristWidget)
			goButton.active = false;
	}
}