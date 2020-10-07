package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

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
		this.previousButton = new ExtendedButton(this.xOffset + 8, this.yOffset + 8, 16, 16, new StringTextComponent("<"), button -> prevPage());
		this.nextButton = new ExtendedButton(this.xOffset + guiWidth - 24, this.yOffset + 8, 16, 16, new TranslationTextComponent(">"), button -> nextPage());
		if(GristTypes.getRegistry().getValues().size() > rows * columns)
		{
			addButton(this.nextButton);
		}
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTabs(matrixStack);

		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.blit(matrixStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage;
		if(ClientEditHandler.isActive() || ClientPlayerData.getTitle() == null)
			cacheMessage = getTitle().getString();
		else cacheMessage = ClientPlayerData.getTitle().asTextComponent().getString();
		mc.fontRenderer.drawString(matrixStack, cacheMessage, (this.width / 2F) - mc.fontRenderer.getStringWidth(cacheMessage) / 2F, yOffset + 12, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		drawActiveTabAndOther(matrixStack, mouseX, mouseY);

		RenderSystem.color3f(1, 1, 1);
		RenderSystem.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();

		this.drawGrist(matrixStack, xOffset, yOffset, mouseX, mouseY, page);
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
		int maxPage = (GristTypes.getRegistry().getValues().size() - 1) / (rows * columns);
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