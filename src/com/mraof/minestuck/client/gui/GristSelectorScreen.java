package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.network.GristWildcardPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GristSelectorScreen extends MinestuckScreen
{

	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");

	private static final int guiWidth = 226, guiHeight = 190;

	private Screen otherScreen;
	private int page = 0;
	private GuiButtonExt previousButton;
	private GuiButtonExt nextButton;

	protected GristSelectorScreen(MiniAlchemiterScreen screen)
	{
		super(new StringTextComponent("Grist selector"));
		this.otherScreen = screen;
	}

	public GristSelectorScreen(AlchemiterScreen screen)
	{
		super(new StringTextComponent("Grist selector"));
		this.otherScreen = screen;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void init()
	{
		super.init();
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		this.previousButton = new GuiButtonExt((this.width) + 8, yOffset + 8, 16, 16, "<", button -> prevPage());
		this.nextButton = new GuiButtonExt(xOffset + guiWidth - 24, yOffset + 8, 16, 16, ">", button -> nextPage());
		if(GristType.REGISTRY.getValues().size() > rows * columns)
		{
			this.addButton(this.nextButton);
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;

		this.renderBackground();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.minecraft.getTextureManager().bindTexture(guiGristcache);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage = I18n.format("gui.selectGrist");
		minecraft.fontRenderer.drawString(cacheMessage, (this.width / 2) - minecraft.fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		super.render(mouseX, mouseY, partialTicks);

		GlStateManager.color3f(1, 1, 1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();

		this.drawGrist(xOffset, yOffset, mouseX, mouseY, page);
		
/*		if (tooltip != -1)
			if(tooltip % 2 == 0)
				drawHoveringText(Arrays.asList(I18n.format("grist.format", GristType.values()[tooltip/2].getDisplayName())),
						xcor, ycor, fontRenderer);
			else drawHoveringText(Arrays.asList(String.valueOf(clientGrist.getGrist(GristType.values()[tooltip/2]))), xcor, ycor, fontRenderer);*/
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		super.mouseClicked(xcor, ycor, mouseButton);
		if (mouseButton == 0)
		{
			int xOffset = (width - guiWidth) / 2;
			int yOffset = (height - guiHeight) / 2;

			List<GristType> types = new ArrayList<>(GristType.REGISTRY.getValues());
			Collections.sort(types);
			types = types.stream().skip(page * rows * columns).limit(rows * columns).collect(Collectors.toList());

			int offset = 0;
			for (GristType type : types)
			{
				int row = offset / columns;
				int column = offset % columns;
				int gristXOffset = xOffset + gristIconX + (gristDisplayXOffset * column - column);
				int gristYOffset = yOffset + gristIconY + (gristDisplayYOffset * row - row);
				if (isPointInRegion(gristXOffset, gristYOffset, 16, 16, xcor, ycor))
				{
					BlockPos pos;
					if(otherScreen instanceof AlchemiterScreen)
						pos = ((AlchemiterScreen) otherScreen).getAlchemiter().getPos();
					else pos = null;
					otherScreen.width = this.width;
					otherScreen.height = this.height;
					minecraft.currentScreen = otherScreen;
					GristWildcardPacket packet = new GristWildcardPacket(pos, type);
					MSPacketHandler.INSTANCE.sendToServer(packet);
					break;
				}
				offset++;
			}
		}
		return true;
	}
	
	@Override
	public void onClose()
	{
		minecraft.currentScreen = otherScreen;
		minecraft.player.closeScreen();
	}

	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, double pointX, double pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
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
		int maxPage = (GristType.REGISTRY.getValues().size() - 1) / (rows * columns);
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