package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.computer.theme.ComputerThemes;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.network.computer.ThemeSelectPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Shows the list of computer Themes which are available from the given client's resource pack.
 * The currently active theme is rendered and its name is shown on screen.
 */
@ParametersAreNonnullByDefault
public class ComputerThemeScreen extends Screen
{
	public static final String TITLE = "minestuck.computer_themes";
	public static final String SELECTED_THEME = "minestuck.computer_themes.divide_value";
	public static final String DONE_MESSAGE = "minestuck.computer_themes.done";
	
	private static final int GUI_WIDTH = 176;
	private static final int GUI_HEIGHT = 166;
	private static final int COMPUTER_SCREEN_WIDTH = 158;
	private static final int COMPUTER_SCREEN_HEIGHT = 120;
	private static final int SCREEN_OFFSET_X = 9;
	private static final int SCREEN_OFFSET_Y = 38;
	
	private static final int ENTRIES_ACROSS = 2;
	private static final int ENTRIES_DOWN = 4;
	private static final int ENTRIES_PER_PAGE = ENTRIES_ACROSS * ENTRIES_DOWN;
	
	private static final Comparator<ComputerTheme> THEME_SORTER = Comparator.comparing(theme -> !theme.id().equals(MSComputerThemes.DEFAULT));
	
	private int page = 0;
	private Button previousButton;
	private Button nextButton;
	
	private final ComputerBlockEntity computer;
	private ComputerTheme selectedTheme;
	
	private int xOffset;
	private int yOffset;
	
	private final List<Button> entryButtons = new ArrayList<>();
	
	private final List<ComputerTheme> themes = new ArrayList<>();
	
	
	public ComputerThemeScreen(ComputerBlockEntity computer)
	{
		super(Component.translatable(TITLE));
		
		this.computer = computer;
		this.selectedTheme = ComputerThemes.instance().lookup(computer.getTheme());
	}
	
	@Override
	public void init()
	{
		yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		xOffset = (this.width / 2) - (GUI_WIDTH / 2);
		
		//gets the full list of themes, and reorders it so the default theme is first
		themes.clear();
		ComputerThemes.instance().allThemes().stream().sorted(THEME_SORTER).forEach(themes::add);
		
		this.previousButton = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 108, yOffset + SCREEN_OFFSET_Y + 8, 16, 16, Component.literal("<"), button -> prevPage());
		this.nextButton = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 133, yOffset + SCREEN_OFFSET_Y + 8, 16, 16, Component.literal(">"), button -> nextPage());
		addRenderableWidget(this.nextButton);
		addRenderableWidget(this.previousButton);
		
		recreateThemeButtons();
		
		addRenderableWidget(new PowerButton());
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 20, yOffset + SCREEN_OFFSET_Y + 104, 40, 14, Component.translatable(DONE_MESSAGE), button -> finish()));
	}
	
	public void recreateThemeButtons()
	{
		entryButtons.forEach(this::removeWidget);
		entryButtons.clear();
		
		for(int i = 0; i < themes.size(); i++)
		{
			ComputerTheme theme = themes.get(i);
			int yPositionOffset = 18 * ((i / ENTRIES_ACROSS) % ENTRIES_DOWN);
			int xPositionOffset = 76 * (i % ENTRIES_ACROSS);
			
			ExtendedButton entryButton = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 5 + xPositionOffset, yOffset + SCREEN_OFFSET_Y + 30 + yPositionOffset, 72, 14, theme.name(),
					button -> selectedTheme = theme);
			entryButtons.add(addRenderableWidget(entryButton));
		}
		
		updateButtonStates();
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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		guiGraphics.drawString(font, Component.translatable(SELECTED_THEME), xOffset + SCREEN_OFFSET_X + 8, yOffset + SCREEN_OFFSET_Y + 8, selectedTheme.data().textColor(), false);
		guiGraphics.drawString(font, selectedTheme.name(), xOffset + SCREEN_OFFSET_X + 8, yOffset + SCREEN_OFFSET_Y + 18, selectedTheme.data().textColor(), false);
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
	
	private void finish()
	{
		if(!selectedTheme.id().equals(computer.getTheme()))
		{
			PacketDistributor.SERVER.noArg().send(ThemeSelectPacket.create(computer, selectedTheme.id()));
		}
		
		onClose();
	}
	
	//copy of ComputerScreen code
	private class PowerButton extends Button
	{
		public PowerButton()
		{
			super(builder(Component.empty(), b -> minecraft.setScreen(null))
					.pos((ComputerThemeScreen.this.width - GUI_WIDTH) / 2 + 143, (ComputerThemeScreen.this.height - GUI_HEIGHT) / 2 + 3)
					.size(29, 29));
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float pt)
		{
		}
	}
}