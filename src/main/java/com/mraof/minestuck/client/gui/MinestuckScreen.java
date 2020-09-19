package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.item.crafting.alchemy.GristTypes;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	
	protected MinestuckScreen(ITextComponent titleIn)
	{
		super(titleIn);
	}
	
	public void drawGrist(int xOffset, int yOffset, int xcor, int ycor, int page)
	{
		if(minecraft == null)
			return;
		
		//Show the name of the grist instead of the count if displaying a tooltip
		boolean showName = false;
		GristType tooltipType = null;
		GristSet clientGrist = ClientPlayerData.getClientGrist();

		List<GristType> types = new ArrayList<>(GristTypes.getRegistry().getValues());
		Collections.sort(types);
		types = types.stream().skip(page * rows * columns).limit(rows * columns).collect(Collectors.toList());

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
				this.fillGradient(gristXOffset + gristIconX, gristYOffset + gristIconY, gristXOffset + gristIconX + 16, gristYOffset + gristIconY + 17, -2130706433, -2130706433);
				tooltipType = type;
				showName = true;
			}
			if (!String.valueOf(clientGrist.getGrist(type)).equals(amount)
					&& this.isPointInRegion(gristXOffset + gristCountX - 1, gristYOffset + gristCountY - 1, 35, 10, xcor, ycor))
			{
				tooltipType = type;
				showName = false;
			}

			this.drawIcon(gristXOffset + gristIconX, gristYOffset + gristIconY, type.getIcon());
			minecraft.fontRenderer.drawString(amount, gristXOffset + gristCountX, gristYOffset + gristCountY, 0xddddee);
			
			offset++;
		}
		if (tooltipType != null)
		{
			if (showName)
			{
				renderTooltip(Collections.singletonList(tooltipType.getNameWithSuffix().getFormattedText()), xcor, ycor, font);

			}
			else
			{
				renderTooltip(Collections.singletonList(String.valueOf(clientGrist.getGrist(tooltipType))), xcor, ycor, font);
			}
		}
	}

	private void drawIcon(int x, int y, ResourceLocation icon)
	{
		if(icon == null || minecraft == null)
			return;
		
		this.minecraft.getTextureManager().bindTexture(icon);

		float scale = (float) 1 / 16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		BufferBuilder render = Tessellator.getInstance().getBuffer();
		render.begin(7, DefaultVertexFormats.POSITION_TEX);
		render.pos(x, y + iconY, 0D).tex((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.pos(x + iconX, y + iconY, 0D).tex((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.pos(x + iconX, y, 0D).tex((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.pos(x, y, 0D).tex((iconU) * scale, (iconV) * scale).endVertex();
		Tessellator.getInstance().draw();
	}

	protected boolean isPointInRegion(int regionX, int regionY, int regionWidth, int regionHeight, int pointX, int pointY)
	{
		return pointX >= regionX && pointX < regionX + regionWidth && pointY >= regionY && pointY < regionY + regionHeight;
	}
}
