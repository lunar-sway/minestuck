package com.mraof.minestuck.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GristIngredientRenderer implements IIngredientRenderer<GristAmount>
{
	@Override
	public void render(MatrixStack matrixStack, int xPosition, int yPosition, @Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return;
		RenderSystem.enableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1, 1, 1, 1);

		ResourceLocation icon = ingredient.getType().getIcon();
		Minecraft.getInstance().getTextureManager().bind(icon);

		float scale = (float) 1 / 16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		BufferBuilder render = Tessellator.getInstance().getBuilder();
		render.begin(7, DefaultVertexFormats.POSITION_TEX);
		render.vertex(xPosition, yPosition + iconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(xPosition + iconX, yPosition + iconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(xPosition + iconX, yPosition, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(xPosition, yPosition, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		Tessellator.getInstance().end();
		
		RenderSystem.disableAlphaTest();
		RenderSystem.disableBlend();
	}

	@Override
	public List<ITextComponent> getTooltip(GristAmount ingredient, ITooltipFlag tooltipFlag)
	{
		List<ITextComponent> list = new ArrayList<>();
		list.add(ingredient.getType().getDisplayName());
		list.add(new StringTextComponent(String.valueOf(ingredient.getType().getRegistryName())).withStyle(TextFormatting.DARK_GRAY));
		return list;
	}
}