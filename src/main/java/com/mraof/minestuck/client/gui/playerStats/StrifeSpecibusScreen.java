package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		drawTabs(guiGraphics);
		guiGraphics.blit(guiStrifeSelector, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		String message = "This feature isn't implemented yet.";//new TranslationTextComponent(KIND_ABSTRATUS).getFormattedText();
		guiGraphics.drawString(font, message, (this.width / 2F) - mc.font.width(message) / 2F, yOffset + 12, 0x404040, false);
		
		int i = 0;
		for(KindAbstratusType type : KindAbstratusList.getTypeList()) {
			String typeName = type.getDisplayName().getString();
			int xPos = xOffset+9+(columnWidth)*((i%columns)+1)-mc.font.width(typeName);
			int yPos = yOffset+35+(mc.font.lineHeight+1)*(i/columns);
			
			if(!isPointInRegion(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, columnWidth-1, mc.font.lineHeight+1, mouseX, mouseY))
				guiGraphics.drawString(font, typeName, xPos, yPos, 0xFFFFFF, false);
			else {
				guiGraphics.fill(xOffset+9+(columnWidth)*(i%columns)+1, yPos-1, xOffset+9+(columnWidth)*((i%columns)+1), yPos+mc.font.lineHeight, 0xFFAFAFAF);
				guiGraphics.drawString(font, typeName, xPos, yPos, 0x000000, false);
			}
			i++;
		}
		
		drawActiveTabAndOther(guiGraphics, mouseX, mouseY);
		
	}
	
}
