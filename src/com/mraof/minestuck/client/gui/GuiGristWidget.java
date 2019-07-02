package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.ContainerGristWidget;
import com.mraof.minestuck.tileentity.TileEntityGristWidget;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiGristWidget extends GuiMachine
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/widget.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/widget.png");
	
	protected final TileEntityGristWidget te;
	protected final InventoryPlayer playerInventory;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;

	public GuiGristWidget(InventoryPlayer inventoryPlayer, TileEntityGristWidget tileEntity)
	{
		super(new ContainerGristWidget(inventoryPlayer, tileEntity), tileEntity);
		this.te = tileEntity;
		this.playerInventory = inventoryPlayer;
		
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
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(this.te.getDisplayName().getFormattedText(), 8, 6, 0xFFFFFF);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 0xFFFFFF);
		if (!te.getStackInSlot(0).isEmpty())
		{
			//Render grist requirements
			GristSet set = te.getGristWidgetResult();

			GuiUtil.drawGristBoard(set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, fontRenderer);
			
			int cost = te.getGristWidgetBoondollarValue();
			long has = PlayerSavedData.boondollars;
			String costText = GuiUtil.addSuffix(cost)+"£("+GuiUtil.addSuffix(has)+")";
			fontRenderer.drawString(costText, xSize - 9 - fontRenderer.getStringWidth(costText), ySize - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00);
			
			List<String> tooltip = GuiUtil.getGristboardTooltip(set, mouseX - this.guiLeft, mouseY - this.guiTop, 9, 45, fontRenderer);
			if (tooltip != null)
				this.drawHoveringText(tooltip, mouseX - this.guiLeft, mouseY - this.guiTop, fontRenderer);
			else if(mouseY - guiTop >= ySize - 96 + 3 && mouseY - guiTop < ySize - 96 + 3 + fontRenderer.FONT_HEIGHT)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - guiLeft < xSize - 9 - fontRenderer.getStringWidth("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - guiLeft >= xSize - 9 - fontRenderer.getStringWidth(costText))
					drawHoveringText(String.valueOf(cost), mouseX - this.guiLeft, mouseY - this.guiTop);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - guiLeft < xSize - 9 - fontRenderer.getStringWidth(")")
						&& mouseX - guiLeft >= xSize - 9 - fontRenderer.getStringWidth(GuiUtil.addSuffix(has)+")"))
					drawHoveringText(String.valueOf(has), mouseX - this.guiLeft, mouseY - this.guiTop);
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		//draw progress bar
		this.mc.getTextureManager().bindTexture(PROGRESS);
		int width = getScaledValue(te.progress, te.maxProgress, progressWidth);
		int height = progressHeight;
		drawModalRectWithCustomSizedTexture(x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		
		goButton = new GoButton(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
		addButton(goButton);
		if(MinestuckConfig.clientDisableGristWidget)
			goButton.enabled = false;
	}
}