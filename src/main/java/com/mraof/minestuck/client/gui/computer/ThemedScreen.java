package com.mraof.minestuck.client.gui.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Shows the disks loaded into the computer and allows for them to be ejected.
 */
@ParametersAreNonnullByDefault
public abstract class ThemedScreen extends Screen
{
	public static final ResourceLocation GUI_MAIN = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/sburb.png");
	public static final ResourceLocation GUI_BSOD = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/bsod_message.png");
	
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 166;
	public static final int COMPUTER_SCREEN_WIDTH = 158;
	public static final int COMPUTER_SCREEN_HEIGHT = 120;
	public static final int SCREEN_OFFSET_X = 9;
	public static final int SCREEN_OFFSET_Y = 38;
	
	public final ComputerBlockEntity computer;
	public ComputerTheme selectedTheme;
	
	public PowerButton powerButton;
	
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
		setOffsets();
		powerButton = addRenderableWidget(new PowerButton());
		if(!this.selectedTheme.id().equals(computer.getTheme()))
			this.selectedTheme = ComputerThemes.instance().lookup(computer.getTheme());
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		
		setOffsets();
		
		//corner bits (goes on top of computer screen slightly)
		guiGraphics.blit(GUI_MAIN, xOffset + 9, yOffset + 38, GUI_WIDTH, 0, 3, 3);
		guiGraphics.blit(GUI_MAIN, xOffset + 164, yOffset + 38, GUI_WIDTH + 3, 0, 3, 3);
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		boolean bsod = computer.isBroken();
		
		guiGraphics.blit(GUI_MAIN, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		if(bsod)
			guiGraphics.blit(GUI_BSOD, xOffset + 9, yOffset + 38, 0, 0, 158, 120);
		else
			guiGraphics.blit(selectedTheme.data().texturePath(), xOffset + SCREEN_OFFSET_X, yOffset + SCREEN_OFFSET_Y, 0, 0, COMPUTER_SCREEN_WIDTH, COMPUTER_SCREEN_HEIGHT);
	}
	
	
	
	public void setOffsets()
	{
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
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
	
	// make this method public so that programs can add widgets
	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T button)
	{
		return super.addRenderableWidget(button);
	}
	
	public class PowerButton extends Button
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
