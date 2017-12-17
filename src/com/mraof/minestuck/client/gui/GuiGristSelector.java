package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.GristType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GuiGristSelector extends GuiScreenMinestuck
{

	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");

	private static final int guiWidth = 226, guiHeight = 190;

	private GuiSburbMachine otherGui;
	private int page = 0;
	private GuiButtonExt previousButton;
	private GuiButtonExt nextButton;

	protected GuiGristSelector(GuiSburbMachine guiMachine)
	{
		this.otherGui = guiMachine;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;
		this.previousButton = new GuiButtonExt(1, (this.width) + 8, yOffset + 8, 16, 16, "<");
		this.nextButton = new GuiButtonExt(2, xOffset + guiWidth - 24, yOffset + 8, 16, 16, ">");
		if(GristType.REGISTRY.getValues().size() > rows * columns)
		{
			this.buttonList.add(this.nextButton);
		}
	}

	@Override
	public void drawScreen(int xcor, int ycor, float par3)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;

		this.drawDefaultBackground();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(guiGristcache);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage = I18n.format("gui.selectGrist");
		mc.fontRenderer.drawString(cacheMessage, (this.width / 2) - mc.fontRenderer.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		super.drawScreen(xcor, ycor, par3);

		GlStateManager.color(1, 1, 1);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		this.drawGrist(xOffset, yOffset, xcor, ycor, page);
		
/*		if (tooltip != -1)
			if(tooltip % 2 == 0)
				drawHoveringText(Arrays.asList(I18n.format("grist.format", GristType.values()[tooltip/2].getDisplayName())),
						xcor, ycor, fontRenderer);
			else drawHoveringText(Arrays.asList(String.valueOf(clientGrist.getGrist(GristType.values()[tooltip/2]))), xcor, ycor, fontRenderer);*/
	}

	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
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
					otherGui.te.selectedGrist = type;
					otherGui.width = this.width;
					otherGui.height = this.height;
					mc.currentScreen = otherGui;
					MinestuckPacket packet = MinestuckPacket.makePacket(Type.MACHINE_STATE, type);
					MinestuckChannelHandler.sendToServer(packet);
					break;
				}
				offset++;
			}
		}
	}

	@Override
	public void onGuiClosed()
	{
		mc.currentScreen = otherGui;
		mc.player.closeScreen();
	}

	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
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
