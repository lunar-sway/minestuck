package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.LandSelectPackets;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class LandSelectorScreen extends Screen
{
	public static final String TITLE = "minestuck.land_selector";
	public static final String SELECT_LAND = "minestuck.select_land";
	public static final String SELECT = "minestuck.select_land.select";
	public static final String RANDOMIZE = "minestuck.select_land.randomize";
	public static final String PREVIOUS_PAGE = "minestuck.select_land.previous_page";
	public static final String NEXT_PAGE = "minestuck.select_land.next_page";
	public static final String PAGE = "minestuck.select_land.page";
	public static final String TERRAIN_NAME = "minestuck.select_land.terrain_name";
	
	private static final int columns = 2, rowsPerPage = 5, perPage = columns * rowsPerPage;
	
	private final List<TerrainLandType> terrainTypes;
	
	private TerrainLandType currentTerrain;
	private int page;
	
	LandSelectorScreen(List<TerrainLandType> terrainTypes)
	{
		super(Component.translatable(TITLE));
		this.terrainTypes = terrainTypes;
	}
	
	@Override
	public void init()
	{
		int leftX = (width - TitleSelectorScreen.guiWidth) / 2, topY = (height - TitleSelectorScreen.guiHeight) / 2;
		
		int pageCount = pageCount();
		page = Math.max(0, Math.min(pageCount - 1, page));
		
		for(int i = page * perPage; i < Math.min(terrainTypes.size(), (page + 1) * perPage); i++)
		{
			TerrainLandType type = terrainTypes.get(i);
			int local = i - page * perPage;
			Button button = Button.builder(landName(type), button1 -> pickTerrain(type)).bounds(leftX + 4 + (local % columns) * 98, topY + 24 + (local / columns) * 16, 80, 16).build();
			button.active = type != currentTerrain;
			addRenderableWidget(button);
		}
		
		if(pageCount > 1)
		{
			Button previous = new ExtendedButton(leftX + 58, topY + 108, 16, 16, Component.translatable(PREVIOUS_PAGE), button -> changePage(-1));
			previous.active = page > 0;
			addRenderableWidget(previous);
			Button next = new ExtendedButton(leftX + 112, topY + 108, 16, 16, Component.translatable(NEXT_PAGE), button -> changePage(1));
			next.active = page < pageCount - 1;
			addRenderableWidget(next);
		}
		
		Button selectButton = new ExtendedButton(leftX + 24, topY + 128, 60, 20, Component.translatable(SELECT), button -> select());
		selectButton.active = currentTerrain != null;
		addRenderableWidget(selectButton);
		addRenderableWidget(new ExtendedButton(leftX + 102, topY + 128, 60, 20, Component.translatable(RANDOMIZE), button -> random()));
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		int xOffset = (width - TitleSelectorScreen.guiWidth) / 2;
		int yOffset = (height - TitleSelectorScreen.guiHeight) / 2;
		guiGraphics.blit(TitleSelectorScreen.guiBackground, xOffset, yOffset, 0, 0, TitleSelectorScreen.guiWidth, TitleSelectorScreen.guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (height - TitleSelectorScreen.guiHeight) / 2;
		
		String message = I18n.get(SELECT_LAND);
		guiGraphics.drawString(font, message, (this.width / 2F) - font.width(message) / 2F, yOffset + 10, 0x404040, false);
		
		if(pageCount() > 1)
		{
			message = I18n.get(PAGE, page + 1, pageCount());
			guiGraphics.drawString(font, message, (this.width / 2F) - font.width(message) / 2F, yOffset + 112, 0x404040, false);
		}
	}
	
	private static Component landName(TerrainLandType type)
	{
		ResourceLocation id = LandTypes.TERRAIN_REGISTRY.getKey(type);
		return Component.translatableWithFallback(id.toLanguageKey(TERRAIN_NAME), I18n.get("land." + type.getNames()[0]));
	}
	
	private int pageCount()
	{
		return Math.max(1, (terrainTypes.size() + perPage - 1) / perPage);
	}
	
	private void changePage(int change)
	{
		page += change;
		rebuildWidgets();
	}
	
	private void pickTerrain(TerrainLandType type)
	{
		currentTerrain = currentTerrain == type ? null : type;
		rebuildWidgets();
	}
	
	private void select()
	{
		PacketDistributor.sendToServer(LandSelectPackets.PickLand.pick(currentTerrain));
		onClose();
	}
	
	private void random()
	{
		PacketDistributor.sendToServer(LandSelectPackets.PickLand.random());
		onClose();
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
}
