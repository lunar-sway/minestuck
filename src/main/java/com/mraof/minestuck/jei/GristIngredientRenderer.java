package com.mraof.minestuck.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GristIngredientRenderer implements IIngredientRenderer<GristAmount>
{
	@Override
	public void render(PoseStack matrixStack, int xPosition, int yPosition, @Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, ingredient.getType().getIcon());

		float scale = (float) 1 / 16;

		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;

		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		render.vertex(xPosition, yPosition + iconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(xPosition + iconX, yPosition + iconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(xPosition + iconX, yPosition, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(xPosition, yPosition, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		Tesselator.getInstance().end();
		
		RenderSystem.disableBlend();
	}

	@Override
	public List<Component> getTooltip(GristAmount ingredient, TooltipFlag tooltipFlag)
	{
		List<Component> list = new ArrayList<>();
		list.add(ingredient.getType().getDisplayName());
		list.add(new TextComponent(String.valueOf(ingredient.getType().getRegistryName())).withStyle(ChatFormatting.DARK_GRAY));
		return list;
	}
}