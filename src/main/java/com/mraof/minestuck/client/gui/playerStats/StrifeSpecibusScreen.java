package com.mraof.minestuck.client.gui.playerStats;

import com.mraof.minestuck.network.SpecibusPacket;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class StrifeSpecibusScreen extends PlayerStatsScreen
{
	
	public static final String TITLE = "minestuck.strife_specibus";
	public static final String KIND_ABSTRATUS_PROMPT = "minestuck.kind_abstratus_prompt";
	public static final String KIND_ABSTRATUS_SELECTED = "minestuck.kind_abstratus_selected";
	
	private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/strife_selector.png");
	
	private static final int COLUMN_WIDTH = 70;
	private static final int COLUMNS = 3;
	
	private static final int COLOR_UNSELECTED = 0xFFFFFF;
	private static final int COLOR_HOVER = 0x000000;
	private static final int COLOR_SELECTED = 0xFFFFFF;
	private static final int COLOR_LOCKED = 0x888888;
	private static final int COLOR_TITLE = 0x404040;
	private static final int BG_HOVER = 0xFFAFAFAF;
	private static final int BG_SELECTED = 0xFF44AA44;
	
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
		guiGraphics.blit(GUI_TEXTURE, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		String selected = mc.player.getData(MSAttachments.SELECTED_SPECIBUS);
		boolean hasSelection = !selected.isEmpty();
		
		String header = hasSelection ? Component.translatable("minestuck.kind_abstratus_selected").getString() : Component.translatable("minestuck.kind_abstratus_prompt").getString();
		guiGraphics.drawString(font, header, (int) ((this.width / 2F) - mc.font.width(header) / 2F), yOffset + 12, COLOR_TITLE, false);
		
		List<KindAbstratusType> types = KindAbstratusList.getTypeList();
		for(int i = 0; i < types.size(); i++)
		{
			KindAbstratusType type = types.get(i);
			String name = type.getDisplayName().getString();
			
			int cellX = xOffset + 9 + COLUMN_WIDTH * (i % COLUMNS);
			int cellY = yOffset + 35 + (mc.font.lineHeight + 1) * (i / COLUMNS);
			int textX = cellX + COLUMN_WIDTH - mc.font.width(name);
			
			boolean isSelected = type.getUnlocalizedName().equals(selected);
			boolean isLocked = hasSelection && !isSelected;
			boolean isHovered = !isLocked && isPointInRegion(cellX + 1, cellY - 1, COLUMN_WIDTH - 1, mc.font.lineHeight + 1, mouseX, mouseY);
			
			if(isSelected)
			{
				guiGraphics.fill(cellX + 1, cellY - 1, cellX + COLUMN_WIDTH, cellY + mc.font.lineHeight, BG_SELECTED);
				guiGraphics.drawString(font, name, textX, cellY, COLOR_SELECTED, false);
			} else if(isHovered)
			{
				guiGraphics.fill(cellX + 1, cellY - 1, cellX + COLUMN_WIDTH, cellY + mc.font.lineHeight, BG_HOVER);
				guiGraphics.drawString(font, name, textX, cellY, COLOR_HOVER, false);
			} else
			{
				int color = isLocked ? COLOR_LOCKED : COLOR_UNSELECTED;
				guiGraphics.drawString(font, name, textX, cellY, color, false);
			}
		}
		
		drawActiveTabAndOther(guiGraphics, mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button != 0) return super.mouseClicked(mouseX, mouseY, button);
		
		String selected = mc.player.getData(MSAttachments.SELECTED_SPECIBUS);
		if(!selected.isEmpty()) return super.mouseClicked(mouseX, mouseY, button);
		
		List<KindAbstratusType> types = KindAbstratusList.getTypeList();
		for(int i = 0; i < types.size(); i++)
		{
			int cellX = xOffset + 9 + COLUMN_WIDTH * (i % COLUMNS);
			int cellY = yOffset + 35 + (mc.font.lineHeight + 1) * (i / COLUMNS);
			
			if(isPointInRegion(cellX + 1, cellY - 1, COLUMN_WIDTH - 1, mc.font.lineHeight + 1, (int) mouseX, (int) mouseY))
			{
				PacketDistributor.sendToServer(new SpecibusPacket(types.get(i).getUnlocalizedName()));
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}