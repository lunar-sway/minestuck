package com.mraof.minestuck.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.api.alchemy.GristAmount;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GristIngredientRenderer implements IIngredientRenderer<GristAmount>
{
	@Override
	public void render(GuiGraphics guiGraphics, @Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, ingredient.type().getIcon());
		
		Matrix4f pose = guiGraphics.pose().last().pose();
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
		list.add(ingredient.type().getDisplayName());
		list.add(Component.literal(String.valueOf(ingredient.type())).withStyle(ChatFormatting.DARK_GRAY));
		return list;
	}
}