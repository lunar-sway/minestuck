package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.EditmodeTeleportPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Essentially a sub-menu of InventoryEditmodeScreen. Used for selecting an EditmodeLocation to teleport to.
 * Also has buttons to enable/disable editmode relevant properties including noclip and interaction mode (not currently implemented)
 */
public class EditmodeSettingsScreen extends MinestuckScreen
{
	public static final String TITLE = "minestuck.editmode_settings";
	public static final String EDITMODE_LOCATIONS = "minestuck.editmode_locations";
	
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_extra_large.png");
	private static final ResourceLocation INTERACT_SINGLE_MODE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/interact_single_mode.png");
	private static final ResourceLocation INTERACT_MULTIPLE_MODE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/interact_multiple_mode.png");
	private static final ResourceLocation NOCLIP_ACTIVE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/noclip_active.png");
	private static final ResourceLocation NOCLIP_INACTIVE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/noclip_inactive.png");
	
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private static final int SETTINGS_X = 169, SETTINGS_Y = 120;
	private static final int SETTINGS_SIZE = InventoryEditmodeScreen.SETTINGS_SIZE;
	private static final int INTERACT_ICON_X = 165, INTERACT_ICON_Y = 45;
	private static final int NOCLIP_ICON_X = 165, NOCLIP_ICON_Y = 85;
	private static final int TOGGLE_ICON_SIZE = 24;
	
	private static final int ENTRIES_PER_PAGE = 6;
	
	private final List<Pair<BlockPos, EditmodeLocations.Source>> locationEntries = new ArrayList<>();
	
	private int page = 0;
	private Button previousButton;
	private Button nextButton;
	
	private int xOffset;
	private int yOffset;
	
	private List<Button> entryButtons = new ArrayList<>();
	
	public EditmodeSettingsScreen(Player player)
	{
		super(Component.translatable(TITLE));
		
		EditmodeLocations locations = ClientEditHandler.locations; //approach means it wont update until the screen is reopened
		
		if(locations != null)
			establishEntries(player, locations);
	}
	
	@SuppressWarnings("resource")
	private void establishEntries(Player player, EditmodeLocations locations)
	{
		//only shows locations in the current dimension for now
		ResourceKey<Level> playerDimension = player.level().dimension();
		List<Pair<BlockPos, EditmodeLocations.Source>> blockSourceLocationEntries = new ArrayList<>();
		List<Pair<BlockPos, EditmodeLocations.Source>> entrySourceLocationEntries = new ArrayList<>();
		locations.getLocations().forEach((entryDimension, posSourcePair) ->
		{
			if(playerDimension.equals(entryDimension))
			{
				EditmodeLocations.Source entrySource = posSourcePair.getSecond();
				if(entrySource == EditmodeLocations.Source.BLOCK)
					blockSourceLocationEntries.add(posSourcePair);
				else if(entrySource == EditmodeLocations.Source.ENTRY)
					entrySourceLocationEntries.add(posSourcePair);
			}
		});
		
		//block sources appear first, followed by entry sources
		locationEntries.addAll(blockSourceLocationEntries);
		locationEntries.addAll(entrySourceLocationEntries);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		this.previousButton = new ExtendedButton(xOffset + 10, this.yOffset + 25, 16, 16, Component.literal("<"), button -> prevPage());
		this.nextButton = new ExtendedButton(xOffset + 35, this.yOffset + 25, 16, 16, Component.literal(">"), button -> nextPage());
		addRenderableWidget(this.nextButton);
		addRenderableWidget(this.previousButton);
		previousButton.active = false;
		if(locationEntries.size() < ENTRIES_PER_PAGE)
		{
			nextButton.active = false;
		}
		
		for(int i = 0; i < locationEntries.size(); i++)
		{
			BlockPos entryPos = locationEntries.get(i).getFirst();
			int positionOffset = 20 * (i % ENTRIES_PER_PAGE);
			
			Component buttonComponent;
			
			int entryX = entryPos.getX();
			int entryY = entryPos.getY();
			int entryZ = entryPos.getZ();
			
			buttonComponent = Component.literal(entryX + " | " + entryY + " | " + entryZ);
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 10, yOffset + 45 + positionOffset, 120, 16, buttonComponent, button -> teleport(entryPos));
			entryButtons.add(entryButton);
			addRenderableWidget(entryButton);
		}
		
		renderButtons();
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		graphics.blit(GUI_BACKGROUND, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		graphics.drawString(this.font, this.title, xOffset + 10, yOffset + 10, 4210752, false);
		graphics.drawString(this.font, Component.translatable(EDITMODE_LOCATIONS), xOffset + 55, yOffset + 30, 9013641, false);
		
		graphics.blit(InventoryEditmodeScreen.SETTINGS_ICON, xOffset + SETTINGS_X, yOffset + SETTINGS_Y, SETTINGS_SIZE, SETTINGS_SIZE, 0, 0, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
		if(InventoryEditmodeScreen.overtopSettingsIconBounds(mouseX, mouseY, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
			graphics.renderTooltip(font, Component.literal("Return"), mouseX, mouseY);
		
		graphics.blit(INTERACT_MULTIPLE_MODE_ICON, xOffset + INTERACT_ICON_X, yOffset + INTERACT_ICON_Y, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, 0, 0, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE);
		if(overtopToggleableIconBounds(mouseX, mouseY, xOffset, yOffset, INTERACT_ICON_X, INTERACT_ICON_Y))
			graphics.renderTooltip(font, Component.literal("Interaction mode toggle not available yet"), mouseX, mouseY);
		graphics.blit(NOCLIP_INACTIVE_ICON, xOffset + NOCLIP_ICON_X, yOffset + NOCLIP_ICON_Y, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, 0, 0, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE);
		if(overtopToggleableIconBounds(mouseX, mouseY, xOffset, yOffset, NOCLIP_ICON_X, NOCLIP_ICON_Y))
			graphics.renderTooltip(font, Component.literal("Noclip toggle not available yet"), mouseX, mouseY);
		
		super.render(graphics, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
	{
		if(InventoryEditmodeScreen.overtopSettingsIconBounds(pMouseX, pMouseY, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
		{
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			this.minecraft.setScreen(null);
			PlayerStatsScreen.editmodeTab = PlayerStatsScreen.EditmodeGuiType.DEPLOY_LIST;
			PlayerStatsScreen.openGui(true);
		}
		
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}
	
	@Override
	public void resize(Minecraft pMinecraft, int pWidth, int pHeight)
	{
		super.resize(pMinecraft, pWidth, pHeight);
		
		//TODO resizing does not work for button rendering
		renderButtons();
	}
	
	private void teleport(BlockPos pos)
	{
		EditmodeTeleportPacket packet = new EditmodeTeleportPacket(pos);
		MSPacketHandler.sendToServer(packet);
	}
	
	private void prevPage()
	{
		if(page > 0)
		{
			page--;
			if(page == 0)
			{
				previousButton.active = false;
			}
			nextButton.active = true;
		}
		
		renderButtons();
	}
	
	private void nextPage()
	{
		int maxPage = locationEntries.size() / 5;
		if(page < maxPage)
		{
			page++;
			if(page == maxPage)
			{
				nextButton.active = false;
			}
			previousButton.active = true;
		}
		
		renderButtons();
	}
	
	private void renderButtons()
	{
		int startElement = page * ENTRIES_PER_PAGE;
		
		entryButtons.forEach(button -> button.visible = false); //makes all buttons invisible
		entryButtons.forEach(button -> button.active = false); //makes all buttons invisible
		
		for(int i = startElement; i < startElement + ENTRIES_PER_PAGE; i++)
		{
			if(i < entryButtons.size())
			{
				entryButtons.get(i).visible = true; //selectively reenables certain buttons
				entryButtons.get(i).active = true;
			} else
				break;
		}
	}
	
	public static boolean overtopToggleableIconBounds(double xPos, double yPos, int xOffset, int yOffset, int ICON_X, int ICON_Y)
	{
		boolean inYRange = yPos >= yOffset + ICON_Y && yPos < yOffset + ICON_Y + TOGGLE_ICON_SIZE;
		boolean inXRange = xPos >= xOffset + ICON_X && xPos < xOffset + ICON_X + TOGGLE_ICON_SIZE;
		
		return inYRange && inXRange;
	}
}