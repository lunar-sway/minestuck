package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;

public class GuiGristCache extends GuiPlayerStats
{
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	private int page = 0;

	private GuiButton previousButton;
	private GuiButton nextButton;

	public GuiGristCache()
	{
		super();
		guiWidth = 226;
		guiHeight = 190;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.previousButton = new GuiButtonExt(1, this.xOffset + 8, this.yOffset + 8, 16, 16, "<");
		this.nextButton = new GuiButtonExt(2, this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, ">");
		if(GristType.REGISTRY.getValues().size() > rows * columns)
		{
			this.buttonList.add(this.nextButton);
		}
	}

	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		this.drawDefaultBackground();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		drawTabs();

		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage;
		if (ClientEditHandler.isActive() || MinestuckPlayerData.title == null)
			cacheMessage = I18n.format("gui.gristCache.name");
		else cacheMessage = MinestuckPlayerData.title.getTitleName();
		mc.fontRenderer.drawString(cacheMessage, (this.width / 2) - mc.fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		super.drawScreen(xcor, ycor, par3);

		drawActiveTabAndOther(xcor, ycor);

		GlStateManager.color(1, 1, 1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		this.drawGrist(xOffset, yOffset, xcor, ycor, page);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		int maxPage = (GristType.REGISTRY.getValues().size() - 1) / (rows * columns);
		if (button == previousButton && page > 0)
		{
			page--;
			if(page == 0) {
				this.buttonList.remove(previousButton);
			}
			if(!this.buttonList.contains(nextButton)) {
				this.addButton(nextButton);
			}
		}
		else if (button == nextButton && page < maxPage)
		{
			page++;
			if(page == maxPage) {
				this.buttonList.remove(nextButton);
			}
			if(!this.buttonList.contains(previousButton)) {
				this.addButton(previousButton);
			}
		}
	}
}
