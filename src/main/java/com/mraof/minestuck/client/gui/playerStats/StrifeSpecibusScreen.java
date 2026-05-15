package com.mraof.minestuck.client.gui.playerStats;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.ClientSpecibusData;
import com.mraof.minestuck.network.SpecibusPacket;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class StrifeSpecibusScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.strife_specibus";
	public static final String ABSTRATA_TITLE = "minestuck.kind_abstrata";
	public static final String CONFIRM_BODY = "minestuck.specibus.confirm_body";
	public static final String SLOTS = "minestuck.specibus.slots";
	public static final String DAMAGE = "minestuck.specibus.damage";
	public static final String DAMAGE_DESC = "minestuck.specibus.damage_desc";
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
	
	@Nullable
	private KindAbstratusType pendingType = null;
	
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
	
	private static float getDisplayMultiplier(int count, int max)
	{
		if(count <= 0) return 1.0f;
		if(max <= 1) return 2.0f;
		float step = 0.75f / (max - 1);
		return 2.0f - step * (count - 1);
	}
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		List<String> selected = mc.player.getData(MSAttachments.SELECTED_SPECIBUS);
		boolean isFull = selected.size() >= ClientSpecibusData.maxCount;
		
		List<Component> tooltip = null;
		
		List<KindAbstratusType> types = KindAbstratusList.getTypeList();
		for(int i = 0; i < types.size(); i++)
		{
			KindAbstratusType type = types.get(i);
			String name = type.getDisplayName().getString();
			
			int cellX = xOffset + 9 + COLUMN_WIDTH * (i % COLUMNS);
			int cellY = yOffset + 35 + (mc.font.lineHeight + 1) * (i / COLUMNS);
			int textX = cellX + COLUMN_WIDTH - mc.font.width(name);
			
			boolean isSelected = selected.contains(type.getUnlocalizedName());
			boolean isLocked = isFull && !isSelected;
			boolean isHovered = !isLocked && !isSelected
					&& isPointInRegion(cellX + 1, cellY - 1, COLUMN_WIDTH - 1, mc.font.lineHeight + 1, mouseX, mouseY);
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
				guiGraphics.drawString(font, name, textX, cellY, isLocked ? COLOR_LOCKED : COLOR_UNSELECTED, false);
			}
		}
		if(!selected.isEmpty())
		{
			int iconX = xOffset + 8;
			int iconY = yOffset + 8;
			
			MobEffectTextureManager effectSprites = mc.getMobEffectTextures();
			guiGraphics.blit(iconX, iconY, 0, 18, 18, effectSprites.get(MobEffects.DAMAGE_BOOST));
			
			float multiplier = getDisplayMultiplier(selected.size(), ClientSpecibusData.maxCount);
			String multiplierText = Math.round(multiplier * 100) + "%";
			
			guiGraphics.drawString(font,
					Component.translatable(DAMAGE),
					iconX + 20, iconY, COLOR_TITLE, false);
			guiGraphics.drawString(font,
					multiplierText,
					iconX + 20, iconY + 10, 0x0094FF, false);
			
			if(mouseInBounds(mouseY, iconY + 10, mouseX, iconX + 20, mc.font.width(multiplierText)))
				tooltip = ImmutableList.of(
						Component.translatable(DAMAGE_DESC)
				);
		}
		
		int slotsIconX = xOffset + guiWidth - 80;
		int slotsIconY = yOffset + 8;
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(PlayerStatsScreen.icons, slotsIconX, slotsIconY + 1, 16, 64, 16, 16);
		
		String slotsValue = String.valueOf(ClientSpecibusData.maxCount - selected.size());
		
		guiGraphics.drawString(font, Component.translatable(SLOTS), slotsIconX + 20, slotsIconY, COLOR_TITLE, false);
		guiGraphics.drawString(font, slotsValue, slotsIconX + 20, slotsIconY + 10, 0x0094FF, false);
		
		drawActiveTabAndOther(guiGraphics, mouseX, mouseY);
		
		if(tooltip != null)
			guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
	}
	private boolean mouseInBounds(int mouseY, int minY, int mouseX, int minX, int xDiff)
	{
		return (mouseY >= minY && mouseY < minY + mc.font.lineHeight) && (mouseX >= minX && mouseX < minX + xDiff);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button != 0) return super.mouseClicked(mouseX, mouseY, button);
		
		List<String> selected = mc.player.getData(MSAttachments.SELECTED_SPECIBUS);
		if(selected.size() >= ClientSpecibusData.maxCount) return super.mouseClicked(mouseX, mouseY, button);
		
		List<KindAbstratusType> types = KindAbstratusList.getTypeList();
		for(int i = 0; i < types.size(); i++)
		{
			int cellX = xOffset + 9 + COLUMN_WIDTH * (i % COLUMNS);
			int cellY = yOffset + 35 + (mc.font.lineHeight + 1) * (i / COLUMNS);
			
			if(isPointInRegion(cellX + 1, cellY - 1, COLUMN_WIDTH - 1, mc.font.lineHeight + 1, (int) mouseX, (int) mouseY))
			{
				String name = types.get(i).getUnlocalizedName();
				if(!selected.contains(name))
					openConfirmDialog(types.get(i));
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private void openConfirmDialog(KindAbstratusType type)
	{
		pendingType = type;
		mc.setScreen(new ConfirmScreen(
				this::onConfirm,
				Component.translatable(ABSTRATA_TITLE),
				Component.translatable(CONFIRM_BODY, type.getDisplayName())
		));
	}
	
	private void onConfirm(boolean result)
	{
		if(result && pendingType != null)
			PacketDistributor.sendToServer(new SpecibusPacket(pendingType.getUnlocalizedName()));
		pendingType = null;
		mc.setScreen(this);
	}
}