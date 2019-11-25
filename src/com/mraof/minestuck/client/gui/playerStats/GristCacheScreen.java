package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GristCacheScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.grist_cache";
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	private int page = 0;

	private Button previousButton;
	private Button nextButton;

	public GristCacheScreen()
	{
		super(new TranslationTextComponent(TITLE));
		guiWidth = 226;
		guiHeight = 190;
	}

	@Override
	public void init()
	{
		super.init();
		this.previousButton = new GuiButtonExt(this.xOffset + 8, this.yOffset + 8, 16, 16, "<", button -> prevPage());
		this.nextButton = new GuiButtonExt(this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, ">", button -> nextPage());
		if(GristTypes.REGISTRY.getValues().size() > rows * columns)
		{
			addButton(this.nextButton);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTabs();

		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage;
		if (ClientEditHandler.isActive() || PlayerSavedData.title == null)
			cacheMessage = getTitle().getFormattedText();
		else cacheMessage = PlayerSavedData.title.asTextComponent().getFormattedText();
		mc.fontRenderer.drawString(cacheMessage, (this.width / 2F) - mc.fontRenderer.getStringWidth(cacheMessage) / 2F, yOffset + 12, 0x404040);
		super.render(mouseX, mouseY, partialTicks);

		drawActiveTabAndOther(mouseX, mouseY);

		GlStateManager.color3f(1, 1, 1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();

		this.drawGrist(xOffset, yOffset, mouseX, mouseY, page);
	}
	
	private void prevPage()
	{
		if(page > 0)
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
	}
	
	private void nextPage()
	{
		int maxPage = (GristTypes.REGISTRY.getValues().size() - 1) / (rows * columns);
		if(page < maxPage)
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