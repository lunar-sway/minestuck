package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Shows the disks loaded into the computer and allows for them to be ejected.
 */
@ParametersAreNonnullByDefault
public abstract class ThemedScreen extends Screen
{
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 166;
	public static final int COMPUTER_SCREEN_WIDTH = 158;
	public static final int COMPUTER_SCREEN_HEIGHT = 120;
	public static final int SCREEN_OFFSET_X = 9;
	public static final int SCREEN_OFFSET_Y = 38;
	
	public final ComputerBlockEntity computer;
	public ComputerTheme selectedTheme;
	
	public int xOffset;
	public int yOffset;
	
	public ThemedScreen(ComputerBlockEntity computer, MutableComponent title)
	{
		super(title);
		
		this.computer = computer;
		this.selectedTheme = ComputerThemes.instance().lookup(computer.getTheme());
	}
	
	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		addRenderableWidget(new PowerButton());
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		//TODO theme texture keeps getting placed on top
		guiGraphics.blit(selectedTheme.data().texturePath(), xOffset + SCREEN_OFFSET_X, yOffset + SCREEN_OFFSET_Y, 0, 0, COMPUTER_SCREEN_WIDTH, COMPUTER_SCREEN_HEIGHT);
		guiGraphics.blit(ComputerScreen.guiMain, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return false;
	}
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(pKeyCode == GLFW.GLFW_KEY_ESCAPE)
		{
			MSScreenFactories.displayComputerScreen(computer);
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	//copy of ComputerScreen code
	private class PowerButton extends Button
	{
		public PowerButton()
		{
			super(builder(Component.empty(), b -> minecraft.setScreen(null))
					.pos((ThemedScreen.this.width - GUI_WIDTH) / 2 + 143, (ThemedScreen.this.height - GUI_HEIGHT) / 2 + 3)
					.size(29, 29));
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float pt)
		{
		}
	}
}
