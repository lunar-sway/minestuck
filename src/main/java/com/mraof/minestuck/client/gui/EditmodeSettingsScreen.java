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
 * Essentially a sub-menu of InventoryEditmodeScreen. Used for selecting an EditmodeLocation to teleport to
 */
public class EditmodeSettingsScreen extends MinestuckScreen
{
	public static final String TITLE = "minestuck.punch_designix";
	
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_extra_large.png");
	
	private static final int GUI_WIDTH = 224;
	private static final int GUI_HEIGHT = 176;
	
	private static final int SETTINGS_X = 100, SETTINGS_Y = 145;
	private static final int SETTINGS_SIZE = InventoryEditmodeScreen.SETTINGS_SIZE;
	
	private final Player player;
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
		
		this.player = player;
		EditmodeLocations locations = ClientEditHandler.locations; //approach means it wont update until the screen is reopened
		
		establishEntries(player, locations);
	}
	
	private void establishEntries(Player player, EditmodeLocations locations)
	{
		//only shows locations in the current dimension for now
		ResourceKey<Level> playerDimension = player.level().dimension();
		List<Pair<BlockPos, EditmodeLocations.Source>> entitySourceLocationEntries = new ArrayList<>();
		List<Pair<BlockPos, EditmodeLocations.Source>> blockSourceLocationEntries = new ArrayList<>();
		List<Pair<BlockPos, EditmodeLocations.Source>> entrySourceLocationEntries = new ArrayList<>();
		locations.getLocations().forEach((entryDimension, posSourcePair) ->
		{
			if(playerDimension.equals(entryDimension))
			{
				EditmodeLocations.Source entrySource = posSourcePair.getSecond();
				if(entrySource == EditmodeLocations.Source.ENTITY)
					entitySourceLocationEntries.add(posSourcePair);
				else if(entrySource == EditmodeLocations.Source.BLOCK)
					blockSourceLocationEntries.add(posSourcePair);
				else if(entrySource == EditmodeLocations.Source.ENTRY)
					entrySourceLocationEntries.add(posSourcePair);
			}
		});
		
		//entity sources appear first, followed by block sources, then entry sources
		locationEntries.addAll(entitySourceLocationEntries);
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
		if(locationEntries.size() < 5)
		{
			nextButton.active = false;
		}
		
		for(int i = 0; i < locationEntries.size(); i++)
		{
			Pair<BlockPos, EditmodeLocations.Source> entryIterate = locationEntries.get(i);
			int positionOffset = 20 * (i % 5); //assumes 5 entries per page
			
			Component buttonComponent;
			BlockPos entryPos = entryIterate.getFirst();
			
			if(entryIterate.getSecond() == EditmodeLocations.Source.ENTITY)
			{
				buttonComponent = Component.literal("Player");
			} else
			{
				int entryX = entryPos.getX();
				int entryY = entryPos.getY();
				int entryZ = entryPos.getZ();
				
				buttonComponent = Component.literal(entryX + " | " + entryY + " | " + entryZ);
			}
			
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
		
		graphics.blit(InventoryEditmodeScreen.SETTINGS_ICON, xOffset + SETTINGS_X, yOffset + SETTINGS_Y, SETTINGS_SIZE, SETTINGS_SIZE, 0, 0, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE, SETTINGS_SIZE);
		if(InventoryEditmodeScreen.overtopSettingsIconBounds(mouseX, mouseY, xOffset, yOffset, SETTINGS_X, SETTINGS_Y))
			graphics.renderTooltip(font, Component.translatable("Return"), mouseX, mouseY);
		
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
		int startElement = page * 5;
		
		entryButtons.forEach(button -> button.visible = false); //makes all buttons invisible
		entryButtons.forEach(button -> button.active = false); //makes all buttons invisible
		
		for(int i = startElement; i < startElement + 5; i++)
		{
			if(i < entryButtons.size())
			{
				entryButtons.get(i).visible = true; //selectively reenables certain buttons
				entryButtons.get(i).active = true;
			} else
				break;
		}
	}
}