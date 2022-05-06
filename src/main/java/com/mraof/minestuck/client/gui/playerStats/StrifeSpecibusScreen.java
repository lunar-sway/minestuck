package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class StrifeSpecibusScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.strife_specibus";
	public static final String KIND_ABSTRATUS = "minestuck.kind_abstratus";
	
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_selector.png");
	
	private static final int columnWidth = 70, columns = 3;
	
	public StrifeSpecibusScreen()
	{
		super(new TranslationTextComponent(TITLE));
		guiWidth = 228;
		guiHeight = 150;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int xcor, int ycor, float par3)
	{
		super.render(matrixStack, xcor, ycor, par3);
		this.renderBackground(matrixStack);
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs(matrixStack);
		
		this.mc.getTextureManager().bind(guiStrifeSelector);
		
		this.blit(matrixStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String message = "This feature isn't implemented yet.";//new TranslationTextComponent(KIND_ABSTRATUS).getFormattedText();
		mc.font.draw(matrixStack, message, (this.width / 2F) - mc.font.width(message) / 2F, yOffset + 12, 0x404040);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			String typeName = type.getDisplayName().getString();
			int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-mc.font.width(typeName);
			int yPos = yOffset+35+(mc.font.lineHeight+1)*(int)(i/columns);
			
			if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, mc.font.lineHeight+1, xcor, ycor))
				mc.font.draw(matrixStack, typeName, xPos, yPos, 0xFFFFFF);
			else {
				fill(matrixStack, xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+mc.font.lineHeight, 0xFFAFAFAF);
				mc.font.draw(matrixStack, typeName, xPos, yPos, 0x000000);
			}
			i++;
		}
		
		drawActiveTabAndOther(matrixStack, xcor, ycor);
		
	}
	
}
