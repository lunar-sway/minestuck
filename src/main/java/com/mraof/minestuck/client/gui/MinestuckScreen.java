package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Created by mraof on 2017 December 06 at 11:31 PM.
 */
public abstract class MinestuckScreen extends Screen
{
	protected static final int gristIconX = 21, gristIconY = 32;
	protected static final int gristDisplayXOffset = 66, gristDisplayYOffset = 21;
	protected static final int gristCountX = 44, gristCountY = 36;
	protected static final int rows = 7;
	protected static final int columns = 3;
	private static final ResourceLocation barCovers = new ResourceLocation("minestuck", "textures/gui/bar_covers.png");
	
	protected MinestuckScreen(Component titleIn)
	{
		super(titleIn);
	}
	
	public void drawGrist(GuiGraphics guiGraphics, int xOffset, int yOffset, int xcor, int ycor, int page)
	{
		if(minecraft == null)
			return;
		
		//Show the name of the grist instead of the count if displaying a tooltip
		boolean showName = false;
		GristType tooltipType = null;
		ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(ClientEditmodeData.isInEditmode() ? ClientPlayerData.CacheSource.EDITMODE : ClientPlayerData.CacheSource.PLAYER);
		GristSet clientGrist = cache.set();
		long cacheLimit = cache.limit();
		
		List<GristType> types = GristTypes.REGISTRY.stream().sorted().skip((long) page * rows * columns).limit(rows * columns).toList();

		int offset = 0;
		for (GristType type : types)
		{
			int column = offset % columns;
			int row = offset / columns;
			int gristXOffset = xOffset + (gristDisplayXOffset * column - column);
			int gristYOffset = yOffset + (gristDisplayYOffset * row - row);
			String amount = GuiUtil.addSuffix(clientGrist.getGrist(type));
			
			if (this.isPointInRegion(gristXOffset + gristIconX, gristYOffset + gristIconY, 16, 16, xcor, ycor))
			{
				guiGraphics.fillGradient(gristXOffset + gristIconX, gristYOffset + gristIconY, gristXOffset + gristIconX + 16, gristYOffset + gristIconY + 17, 0x80ffffff, 0x80ffffff);
				tooltipType = type;
				showName = true;
			}
			if (!String.valueOf(clientGrist.getGrist(type)).equals(amount)
					&& this.isPointInRegion(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, 35, 10, xcor, ycor))
			{
				tooltipType = type;
				showName = false;
			}
			//draws grist icons and current/maximum text
			this.drawIcon(gristXOffset + gristIconX, gristYOffset + gristIconY, type.getIcon());//grist icon
			guiGraphics.drawString(font, amount, gristXOffset + gristCountX - 2, gristYOffset + gristCountY + 10, 0x19b3ef, false);//renders the text
			//renders bars
			double gristFraction = Math.min(1, (double) clientGrist.getGrist(type) / cacheLimit);
			guiGraphics.fill(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, (int) (gristXOffset + gristCountX + (34.0 * gristFraction)), gristYOffset + (gristCountY + 9), 0xff19B3EF); //0xE64C10
			guiGraphics.fill(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, (int) (gristXOffset + gristCountX + (34.0 * gristFraction)), gristYOffset + (gristCountY + 2), 0xff7ED8E5); //0xE64C10
			offset++;
		}
		if (tooltipType != null)
		{
			if (showName)
				guiGraphics.renderTooltip(font, tooltipType.getNameWithSuffix(), xcor, ycor);
			else
				guiGraphics.renderTooltip(font, Component.literal(String.valueOf(clientGrist.getGrist(tooltipType))), xcor, ycor);
		}
	}
	private void drawCovers(int x, int y, ResourceLocation barCovers)//this can go fuck itself
	{
		if(barCovers == null || minecraft == null)
			return;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, barCovers);
		
		float scale = (float) 1 / 16;
		
		int iconX = 48;
		int iconY = 22;
		int iconU = 0;
		int iconV = 0;
		
		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		render.vertex(x, y + iconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y + iconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(x, y, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		Tesselator.getInstance().end();
	}
	private void drawIcon(int x, int y, ResourceLocation icon)
	{
		if(icon == null || minecraft == null)
			return;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, icon);

		float scale = (float) 1 / 16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		render.vertex(x, y + iconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y + iconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(x, y, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		Tesselator.getInstance().end();
	}

	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
}
