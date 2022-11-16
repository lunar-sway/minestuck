package com.mraof.minestuck.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mraof.minestuck.alchemy.GristAmount;
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
	public void render(PoseStack matrixStack, @Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, ingredient.getType().getIcon());
		
		Matrix4f pose = matrixStack.last().pose();
		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		render.vertex(pose, 0, 16, 0).uv(0, 1).endVertex();
		render.vertex(pose, 16, 16, 0).uv(1, 1).endVertex();
		render.vertex(pose, 16, 0, 0).uv(1, 0).endVertex();
		render.vertex(pose, 0, 0, 0).uv(0, 0).endVertex();
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