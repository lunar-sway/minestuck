package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class GristCacheScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.grist_cache";
	
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");
	private int page = 0;

	private Button previousButton;
	private Button nextButton;

	public GristCacheScreen()
	{
		super(new TranslatableComponent(TITLE));
		guiWidth = 226;
		guiHeight = 190;
	}

	@Override
	public void init()
	{
		super.init();
		this.previousButton = new ExtendedButton(this.xOffset + 8, this.yOffset + 8, 16, 16, new TextComponent("<"), button -> prevPage());
		this.nextButton = new ExtendedButton(this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, new TextComponent(">"), button -> nextPage());
		addRenderableWidget(this.nextButton);
		addRenderableWidget(this.previousButton);
		previousButton.visible = false;
		if(GristTypes.getRegistry().getValues().size() <= rows * columns)
		{
			nextButton.visible = false;
		}
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);

		drawTabs(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1,1,1, 1);
		RenderSystem.setShaderTexture(0, guiGristcache);
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage;
		if(ClientEditHandler.isActive() || ClientPlayerData.getTitle() == null)
			cacheMessage = getTitle().getString();
		else cacheMessage = ClientPlayerData.getTitle().asTextComponent().getString();
		mc.font.draw(poseStack, cacheMessage, (this.width / 2F) - mc.font.width(cacheMessage) / 2F, yOffset + 12, 0x404040);
		super.render(poseStack, mouseX, mouseY, partialTicks);

		drawActiveTabAndOther(poseStack, mouseX, mouseY);
		
		this.drawGrist(poseStack, xOffset, yOffset, mouseX, mouseY, page);
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
		int maxPage = (GristTypes.getRegistry().getValues().size() - 1) / (rows * columns);
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