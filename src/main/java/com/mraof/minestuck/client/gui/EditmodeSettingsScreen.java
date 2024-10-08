package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.editmode.EditmodeTeleportPacket;
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
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Essentially a sub-menu of InventoryEditmodeScreen. Used for selecting an EditmodeLocation to teleport to.
 * Also has buttons to enable/disable editmode relevant properties including noclip and interaction mode (not currently implemented)
 */
@ParametersAreNonnullByDefault
public final class EditmodeSettingsScreen extends MinestuckScreen
{
	public static final String TITLE = "minestuck.editmode_settings";
	public static final String EDITMODE_LOCATIONS = "minestuck.editmode_locations";
	public static final String RETURN = "minestuck.editmode_settings.return";
	public static final String INTERACTION_MODE_UNAVAILABLE = "minestuck.editmode_settings.interaction_mode_unavailable";
	public static final String NOCLIP_UNAVAILABLE = "minestuck.editmode_settings.noclip_unavailable";
	
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_extra_large.png");
	@SuppressWarnings("unused")
	private static final ResourceLocation INTERACT_SINGLE_MODE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/interact_single_mode.png");
	private static final ResourceLocation INTERACT_MULTIPLE_MODE_ICON = new ResourceLocation("minestuck", "textures/gui/editmode/interact_multiple_mode.png");
	@SuppressWarnings("unused")
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
	
	private final ResourceKey<Level> level;
	
	private int page = 0;
	private Button previousButton;
	private Button nextButton;
	
	private int xOffset;
	private int yOffset;
	
	private final List<Button> entryButtons = new ArrayList<>();
	
	@SuppressWarnings("resource")
	public EditmodeSettingsScreen(Player player)
	{
		super(Component.translatable(TITLE));
		this.level = player.level().dimension();
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
		
		recreateTeleportButtons();
	}
	
	public void recreateTeleportButtons()
	{
		entryButtons.forEach(this::removeWidget);
		entryButtons.clear();
		
		EditmodeLocations locations = ClientEditmodeData.getLocations();
		
		List<BlockPos> locationEntries = locations == null ? Collections.emptyList()
				: locations.getSortedPositions(level, ClientEditmodeData.getClientLand());
		
		for(int i = 0; i < locationEntries.size(); i++)
		{
			BlockPos entryPos = locationEntries.get(i);
			int positionOffset = 20 * (i % ENTRIES_PER_PAGE);
			
			Component buttonComponent = Component.literal(entryPos.getX() + " | " + entryPos.getY() + " | " + entryPos.getZ());
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + 10, yOffset + 45 + positionOffset, 120, 16, buttonComponent,
					button -> teleport(entryPos));
			entryButtons.add(addRenderableWidget(entryButton));
		}
		
		updateButtonStates();
	}
	
	@Override
	public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(graphics, mouseX, mouseY, partialTicks);
		
		graphics.blit(GUI_BACKGROUND, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(graphics, mouseX, mouseY, partialTicks);
		
		graphics.drawString(this.font, this.title, xOffset + 10, yOffset + 10, 4210752, false);
		graphics.drawString(this.font, Component.translatable(EDITMODE_LOCATIONS), xOffset + 55, yOffset + 30, 9013641, false);
		
		graphics.blit(InventoryEditmodeScreen.SETTINGS_ICON, xOffset + SETTINGS_X, yOffset + SETTINGS_Y, SETTINGS_SIZE, SETTINGS_SIZE, 0, 0, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
		if(InventoryEditmodeScreen.overtopSettingsIconBounds(mouseX, mouseY, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
			graphics.renderTooltip(font, Component.translatable(RETURN), mouseX, mouseY);
		
		graphics.blit(INTERACT_MULTIPLE_MODE_ICON, xOffset + INTERACT_ICON_X, yOffset + INTERACT_ICON_Y, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, 0, 0, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE);
		if(overtopToggleableIconBounds(mouseX, mouseY, xOffset, yOffset, INTERACT_ICON_X, INTERACT_ICON_Y))
			graphics.renderTooltip(font, Component.translatable(INTERACTION_MODE_UNAVAILABLE), mouseX, mouseY);
		graphics.blit(NOCLIP_INACTIVE_ICON, xOffset + NOCLIP_ICON_X, yOffset + NOCLIP_ICON_Y, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, 0, 0, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE, TOGGLE_ICON_SIZE);
		if(overtopToggleableIconBounds(mouseX, mouseY, xOffset, yOffset, NOCLIP_ICON_X, NOCLIP_ICON_Y))
			graphics.renderTooltip(font, Component.translatable(NOCLIP_UNAVAILABLE), mouseX, mouseY);
		
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
	
	private void teleport(BlockPos pos)
	{
		EditmodeTeleportPacket packet = new EditmodeTeleportPacket(pos);
		PacketDistributor.SERVER.noArg().send(packet);
	}
	
	private void prevPage()
	{
		page--;
		updateButtonStates();
	}
	
	private void nextPage()
	{
		page++;
		updateButtonStates();
	}
	
	private void updateButtonStates()
	{
		int startElement = page * ENTRIES_PER_PAGE;
		
		previousButton.active = 0 < startElement;
		nextButton.active = startElement + ENTRIES_PER_PAGE < entryButtons.size();
		
		for(int i = 0; i < entryButtons.size(); i++)
		{
			Button button = entryButtons.get(i);
			button.visible = startElement <= i && i < startElement + ENTRIES_PER_PAGE;
		}
	}
	
	public static boolean overtopToggleableIconBounds(double xPos, double yPos, int xOffset, int yOffset, int ICON_X, int ICON_Y)
	{
		boolean inYRange = yPos >= yOffset + ICON_Y && yPos < yOffset + ICON_Y + TOGGLE_ICON_SIZE;
		boolean inXRange = xPos >= xOffset + ICON_X && xPos < xOffset + ICON_X + TOGGLE_ICON_SIZE;
		
		return inYRange && inXRange;
	}
}