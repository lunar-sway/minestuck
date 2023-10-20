package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.network.EditmodeTeleportPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.Map;

/**
 * Essentially a sub-menu of InventoryEditmodeScreen. Used for selecting an EditmodeLocation to teleport to
 */
public class EditmodeSettingsScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.punch_designix";
	
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_large.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 132;
	
	private static final int SETTINGS_X = 76, SETTINGS_Y = 54;
	private static final int SETTINGS_SIZE = 16;
	
	private Button doneButton;
	
	private final Player player;
	private final EditmodeLocations locations;
	
	
	public EditmodeSettingsScreen(Player player)
	{
		super(Component.translatable(TITLE));
		
		this.player = player;
		this.locations = ClientEditHandler.locations; //approach means it wont update until the screen is reopened
	}
	
	@Override
	public void init()
	{
		ResourceKey<Level> playerDimension = player.level().dimension();
		//int dimensionIterate = 0; if we allow other TP to other dimensions
		int valueIterate = 0;
		
		for(Map.Entry<ResourceKey<Level>, Pair<BlockPos, EditmodeLocations.Source>> entry : locations.getLocations().entries())
		{
			ResourceKey<Level> entryDimension = entry.getKey();
			if(playerDimension.equals(entryDimension))
			{
				int positionOffset = 20 * valueIterate;
				
				BlockPos entryPos = entry.getValue().getFirst();
				int entryX = entryPos.getX();
				int entryY = entryPos.getY();
				int entryZ = entryPos.getZ();
				
				Component buttonComponent = Component.literal(entryX + " | " + entryY + " | " + entryZ);
				
				addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 60, yOffset + 70 + positionOffset, 120, 16, buttonComponent, button -> teleport(entryPos)));
				
				valueIterate++;
			}
		}
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);
		
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		int xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		graphics.blit(GUI_BACKGROUND, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.render(graphics, mouseX, mouseY, partialTicks);
	}
	
	private void teleport(BlockPos pos)
	{
		EditmodeTeleportPacket packet = new EditmodeTeleportPacket(pos);
		MSPacketHandler.sendToServer(packet);
	}
	
	private void finish()
	{
		this.minecraft.setScreen(null);
	}
}