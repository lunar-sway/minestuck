package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class GristCacheScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.grist_cache";
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	private int page = 0;

	private Button previousButton;
	private Button nextButton;

	public GristCacheScreen()
	{
		super(Component.translatable(TITLE));
		guiWidth = 226;
		guiHeight = 190;
	}

	@Override
	public void init()
	{
		super.init();
		this.previousButton = new ExtendedButton(this.xOffset + 8, this.yOffset + 8, 16, 16, Component.literal("<"), button -> prevPage());
		this.nextButton = new ExtendedButton(this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, Component.literal(">"), button -> nextPage());
		addRenderableWidget(this.nextButton);
		addRenderableWidget(this.previousButton);
		previousButton.visible = false;
		if(GristTypes.REGISTRY.size() <= rows * columns)
		{
			nextButton.visible = false;
		}
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		drawTabs(guiGraphics);
		guiGraphics.blit(guiGristcache, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		String cacheMessage;
		if(ClientEditmodeData.isInEditmode() || ClientPlayerData.getTitle() == null)
			cacheMessage = getTitle().getString();
		else cacheMessage = ClientPlayerData.getTitle().asTextComponent().getString();
		guiGraphics.drawString(font, cacheMessage, (this.width / 2F) - mc.font.width(cacheMessage) / 2F, yOffset + 12, 0x404040, false);

		drawActiveTabAndOther(guiGraphics, mouseX, mouseY);
		
		this.drawGrist(guiGraphics, xOffset, yOffset, mouseX, mouseY, page);
	}
	
	private void prevPage()
	{
		if(page > 0)
		{
			page--;
			if(page == 0) {
				previousButton.visible = false;
			}
			nextButton.visible = true;
		}
	}
	
	private void nextPage()
	{
		int maxPage = (GristTypes.REGISTRY.size() - 1) / (rows * columns);
		if(page < maxPage)
		{
			page++;
			if(page == maxPage) {
				nextButton.visible = false;
			}
			previousButton.visible = true;
		}
	}
}