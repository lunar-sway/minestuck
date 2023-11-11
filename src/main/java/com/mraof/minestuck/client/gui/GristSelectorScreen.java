package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.network.GristWildcardPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GristSelectorScreen extends MinestuckScreen
{
	public static final String TITLE = "minestuck.grist_selector";
	public static final String SELECT_GRIST = "minestuck.select_grist";
	private static final ResourceLocation guiGristcache = new ResourceLocation("minestuck", "textures/gui/grist_cache.png");

	private static final int guiWidth = 226, guiHeight = 190;

	private final BlockPos gristHolderPos;
	private int page = 0;
	private ExtendedButton previousButton;
	private ExtendedButton nextButton;

	public GristSelectorScreen(BlockPos gristHolderPos)
	{
		super(Component.translatable(TITLE));
		this.gristHolderPos = gristHolderPos;
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
		this.previousButton = addRenderableWidget(new ExtendedButton((this.width) + 8, yOffset + 8, 16, 16, Component.literal("<"), button -> prevPage()));
		this.nextButton = addRenderableWidget(new ExtendedButton(xOffset + guiWidth - 24, yOffset + 8, 16, 16, Component.literal(">"), button -> nextPage()));
		
		previousButton.visible = false;
		nextButton.visible = GristTypes.getRegistry().getValues().size() > rows * columns;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth) / 2;
		int yOffset = (height - guiHeight) / 2;

		this.renderBackground(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiGristcache, xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		String cacheMessage = I18n.get(SELECT_GRIST);
		guiGraphics.drawString(font, cacheMessage, (this.width / 2F) - minecraft.font.width(cacheMessage) / 2F, yOffset + 12, 0x404040, false);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.disableDepthTest();

		this.drawGrist(guiGraphics, xOffset, yOffset, mouseX, mouseY, page);
		
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

			List<GristType> types = new ArrayList<>(GristTypes.getRegistry().getValues());
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
					this.onClose();
					MSPacketHandler.INSTANCE.sendToServer(new GristWildcardPacket(gristHolderPos, type));
					return true;
				}
				offset++;
			}
		}
		return false;
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