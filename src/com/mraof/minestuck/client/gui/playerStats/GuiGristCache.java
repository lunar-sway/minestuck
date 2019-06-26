package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.client.gui.GuiButtonImpl;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiGristCache extends GuiPlayerStats implements GuiButtonImpl.ButtonClickhandler
{
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	private int page = 0;

	private GuiButtonImpl previousButton;
	private GuiButtonImpl nextButton;

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
		this.previousButton = new GuiButtonImpl(this, 1, this.xOffset + 8, this.yOffset + 8, 16, 16, "<");
		this.nextButton = new GuiButtonImpl(this, 2, this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, ">");
		if(GristType.REGISTRY.getValues().size() > rows * columns)
		{
			addButton(this.nextButton);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTabs();

		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage;
		if (ClientEditHandler.isActive() || MinestuckPlayerData.title == null)
			cacheMessage = I18n.format("gui.grist_cache.name");
		else cacheMessage = MinestuckPlayerData.title.getTitleName();
		mc.fontRenderer.drawString(cacheMessage, (this.width / 2) - mc.fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		super.render(mouseX, mouseY, partialTicks);

		drawActiveTabAndOther(mouseX, mouseY);

		GlStateManager.color3f(1, 1, 1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();

		this.drawGrist(xOffset, yOffset, mouseX, mouseY, page);
	}

	@Override
	public void actionPerformed(GuiButtonImpl button)
	{
		int maxPage = (GristType.REGISTRY.getValues().size() - 1) / (rows * columns);
		if (button == previousButton && page > 0)
		{
			page--;
			if(page == 0) {
				this.buttons.remove(previousButton);
				this.children.remove(previousButton);
			}
			if(!this.buttons.contains(nextButton)) {
				this.addButton(nextButton);
			}
		}
		else if (button == nextButton && page < maxPage)
		{
			page++;
			if(page == maxPage) {
				this.buttons.remove(nextButton);
				this.children.remove(nextButton);
			}
			if(!this.buttons.contains(previousButton)) {
				this.addButton(previousButton);
			}
		}
	}
}