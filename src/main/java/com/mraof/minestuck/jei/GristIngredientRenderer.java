package com.mraof.minestuck.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GristIngredientRenderer implements IIngredientRenderer<GristAmount>
{
	@Override
	public void render(int xPosition, int yPosition, @Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return;
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1, 1, 1, 1);

		ResourceLocation icon = ingredient.getType().getIcon();
		Minecraft.getInstance().getTextureManager().bindTexture(icon);

		float scale = (float) 1 / 16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		BufferBuilder render = Tessellator.getInstance().getBuffer();
		render.begin(7, DefaultVertexFormats.POSITION_TEX);
		render.pos(xPosition, yPosition + iconY, 0D).tex((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.pos(xPosition + iconX, yPosition + iconY, 0D).tex((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.pos(xPosition + iconX, yPosition, 0D).tex((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.pos(xPosition, yPosition, 0D).tex((iconU) * scale, (iconV) * scale).endVertex();
		Tessellator.getInstance().draw();
		
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	public List<String> getTooltip(GristAmount ingredient, ITooltipFlag tooltipFlag)
	{
		List<String> list = new ArrayList<>();
		list.add(ingredient.getType().getDisplayName().getFormattedText());
		list.add(TextFormatting.DARK_GRAY+""+ingredient.getType().getRegistryName());
		return list;
	}
}