package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StrifeSpecibusScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.strife_specibus";
	public static final String KIND_ABSTRATUS = "minestuck.kind_abstratus";
	
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_selector.png");
	
	private static final int columnWidth = 70, columns = 3;
	
	public StrifeSpecibusScreen()
	{
		super(Component.translatable(TITLE));
		guiWidth = 228;
		guiHeight = 150;
	}
	
	@Override
	public void render(PoseStack poseStack, int xcor, int ycor, float par3)
	{
		super.render(poseStack, xcor, ycor, par3);
		this.renderBackground(poseStack);
		
		drawTabs(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1,1,1, 1);
		RenderSystem.setShaderTexture(0, guiStrifeSelector);
		
		this.blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = "This feature isn't implemented yet.";//new TranslationTextComponent(KIND_ABSTRATUS).getFormattedText();
		mc.font.draw(poseStack, message, (this.width / 2F) - mc.font.width(message) / 2F, yOffset + 12, 0x404040);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			String typeName = type.getDisplayName().getString();
			int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-mc.font.width(typeName);
			int yPos = yOffset+35+(mc.font.lineHeight+1)*(i/columns);
			
			if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, mc.font.lineHeight+1, xcor, ycor))
				mc.font.draw(poseStack, typeName, xPos, yPos, 0xFFFFFF);
			else {
				fill(poseStack, xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+mc.font.lineHeight, 0xFFAFAFAF);
				mc.font.draw(poseStack, typeName, xPos, yPos, 0x000000);
			}
			i++;
		}
		
		drawActiveTabAndOther(poseStack, xcor, ycor);
		
	}
	
}
