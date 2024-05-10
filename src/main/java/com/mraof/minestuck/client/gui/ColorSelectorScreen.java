package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@ParametersAreNonnullByDefault
public class ColorSelectorScreen extends Screen
{
	public static final String TITLE = "minestuck.color_selector";
	public static final String ADVANCED_TAB = "minestuck.color_selector.advanced_tab";
	public static final String BASIC_TAB = "minestuck.color_selector.basic_tab";
	public static final String CHOOSE_MESSAGE = "minestuck.color_selector.choose";
	public static final String SELECT_COLOR = "minestuck.color_selector.select_color";
	public static final String COLOR_SELECTED = "minestuck.color_selector.color_selected";
	public static final String DEFAULT_COLOR_SELECTED = "minestuck.color_selector.default_color_selected";
	
	private static final ResourceLocation guiBackground = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/color_selector.png");
	private static final int guiWidth = 176, guiHeight = 157;
	private static final List<ColorSelector> canonColors;
	private static int xOffset, yOffset;
	
	private final boolean firstTime;
	private int selectedIndex = -1;
	private ExtendedSlider redSlider, greenSlider, blueSlider;
	private ExtendedButton tabButton;
	private EditBox hexBox;
	private Tab tab = Tab.Canon;
	
	private @Nullable ComputerBlockEntity be;
	
	// initialize color selectors
	static
	{
		canonColors = new ArrayList<>();
		int i = 0;
		int offset = 0;
		for(int yIndex = 0; yIndex < 5; yIndex++)
		{
			if (yIndex == 1 ) { offset+=3; }
			if (yIndex == 2 ) { offset+=3; }
			for(int xIndex = 0; xIndex < 4; xIndex++)
				canonColors.add(new ColorSelector(21 + 34 * xIndex, 32 + 18 * yIndex + offset, i++));
		}
	}
	
	public ColorSelectorScreen(boolean firstTime)
	{
		super(Component.translatable(TITLE));
		
		this.firstTime = firstTime;
		
		for(ColorSelector colorSelector : canonColors)
			if(ClientPlayerData.getPlayerColor() == colorSelector.getColor())
				selectedIndex = colorSelector.id;
	}
	
	public ColorSelectorScreen(ComputerBlockEntity blockEntity)
	{
		super(Component.translatable(TITLE));
		this.firstTime = false;
		this.be = blockEntity;
		
		for(ColorSelector colorSelector : canonColors)
			if(ClientPlayerData.getPlayerColor() == colorSelector.getColor())
				selectedIndex = colorSelector.id;
	}
	
	@Override
	public void init()
	{
		xOffset = (width - guiWidth)/2;
		yOffset = (height - guiHeight)/2;
		
		redSlider = addRenderableWidget(new ColorSlider(ColorSlider.ColorComp.R));
		greenSlider = addRenderableWidget(new ColorSlider(ColorSlider.ColorComp.G));
		blueSlider = addRenderableWidget(new ColorSlider(ColorSlider.ColorComp.B));
		
		hexBox = addRenderableWidget(new EditBox(minecraft.font, xOffset+23, yOffset+109, 80, 15, Component.empty()));
		hexBox.setResponder(this::onBoxUpdate);
		hexBox.setFilter((text) -> Pattern.matches("^[0-9a-fA-F]+$", text) || text.isEmpty());
		hexBox.setMaxLength(6);
		
		tabButton = addRenderableWidget(new ExtendedButton(xOffset+20, yOffset+130, 65, 20, Component.translatable(ADVANCED_TAB), button -> setTab(tab==Tab.RGB?Tab.Canon:Tab.RGB)));
		addRenderableWidget(new ExtendedButton(xOffset+91, yOffset+130, 65, 20, Component.translatable(CHOOSE_MESSAGE), button -> selectColor()));
		
		setTab(tab);
	}
	
	@Override
	public void resize(Minecraft mc, int width, int height)
	{
		String hexPrev = hexBox.getValue();
		double redPrev = redSlider.getValue();
		double greenPrev = greenSlider.getValue();
		double bluePrev = blueSlider.getValue();
		
		super.resize(mc, width, height);
		
		redSlider.setValue(redPrev);
		greenSlider.setValue(greenPrev);
		blueSlider.setValue(bluePrev);
		hexBox.setValue(hexPrev);
	}
	
	private int getPlayerColorComponent(int comp)
	{
		return (ClientPlayerData.getPlayerColor() >> (comp*8)) & 0xFF;
	}
	
	private void setTab(Tab tab)
	{
		this.tab = tab;
		redSlider.visible = greenSlider.visible = blueSlider.visible = hexBox.visible = tab==Tab.RGB;
		
		if(tab==Tab.RGB && selectedIndex!=-1)
		{
			var color = ColorHandler.BuiltinColors.getColor(selectedIndex);
			redSlider.setValue((color >> 16) & 0xFF);
			greenSlider.setValue((color >> 8) & 0xFF);
			blueSlider.setValue(color & 0xFF);
			hexBox.setValue(toFormattedHex(color));
		}
		else if(tab==Tab.RGB)
		{
			hexBox.setValue(toFormattedHex(getSlidersValue()));
		}
		
		tabButton.setMessage( tab==Tab.RGB? Component.translatable(BASIC_TAB) : Component.translatable(ADVANCED_TAB) );
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		guiGraphics.blit(guiBackground, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		String cacheMessage = I18n.get(SELECT_COLOR);
		guiGraphics.drawString(font, cacheMessage, (width / 2F) - font.width(cacheMessage) / 2F, yOffset + 12, 0x404040, false);
		
		if(tab==Tab.RGB)
		{
			// hide the color box outlines by putting this other box on top
			RenderSystem.setShaderColor(1, 1, 1, 1);
			guiGraphics.blit(guiBackground, xOffset+20, yOffset+31, 0, guiHeight, 138, 98);
			
			// spirograph color preview
			RenderSystem.setShaderColor((float)redSlider.getValue()/255F, (float)greenSlider.getValue()/255, (float)blueSlider.getValue()/255, 0.5F);
			guiGraphics.blit(guiBackground, xOffset+106, yOffset+57, 47, 47, guiWidth, 20, 64, 64, 256, 256);
			
		} else
		{
			for(ColorSelector canonColor : canonColors)
			{
				canonColor.draw(guiGraphics);
				
				if(selectedIndex == canonColor.id)
					drawSelectionBox(guiGraphics, canonColor);
			}
		}
		
		//resets shader color to avoid tinting the whole gui
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		if(tab==Tab.Canon)
			for(ColorSelector canonColor : canonColors)
				if(canonColor.pointWithin(mouseX, mouseY))
					guiGraphics.renderTooltip(font, canonColor.getName(), mouseX, mouseY);
	}
	
	private void drawSelectionBox(GuiGraphics guiGraphics, ColorSelector canonColor)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiBackground, xOffset + canonColor.x - 2, yOffset + canonColor.y - 2, guiWidth, 0, ColorSelector.WIDTH + 4, ColorSelector.HEIGHT + 4);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT && tab==Tab.Canon)
			for(ColorSelector canonColor : canonColors)
				if(canonColor.pointWithin(mouseX, mouseY))
				{
					if(selectedIndex != canonColor.id)
						selectedIndex = canonColor.id;
					else
						selectedIndex = -1;
					return true;
				}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	private int getSlidersValue()
	{
		return redSlider.getValueInt() << 16 | greenSlider.getValueInt() << 8 | blueSlider.getValueInt();
	}
	
	private static String toFormattedHex(int color)
	{
		return String.format("%6s", Integer.toHexString(color)).replace(' ', '0');
	}
	
	private void onBoxUpdate(String hex)
	{
		if(hex.length()!=6) return;
		redSlider.setValue(Integer.parseInt(hex.substring(0,2),16));
		greenSlider.setValue(Integer.parseInt(hex.substring(2,4),16));
		blueSlider.setValue(Integer.parseInt(hex.substring(4,6),16));
	}
	
	private void selectColor()
	{
		if(tab != Tab.RGB)
			ClientPlayerData.selectColor(selectedIndex);
		else if(hexBox.getValue().length() != 6)
			ClientPlayerData.selectColorRGB(getSlidersValue());
		else
			ClientPlayerData.selectColorRGB(Integer.parseInt(hexBox.getValue(), 16));
		
		if(be != null)
			minecraft.setScreen(new ComputerScreen(minecraft, be));
		else
			onClose();
	}
	
	@Override
	public boolean shouldCloseOnEsc() { return false; }
	
	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
	{
		if(pKeyCode == GLFW.GLFW_KEY_ESCAPE) // close programs on esc
		{
			if(be != null) minecraft.setScreen(new ComputerScreen(minecraft, be));
			else onClose();
			
			return true;
		}
		
		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}
	
	@Override
	public void removed()
	{
		if(firstTime && minecraft != null && minecraft.player != null)
		{
			minecraft.player.sendSystemMessage(
					Component.literal("[Minestuck] ").append(
							ClientPlayerData.getPlayerColor()==ColorHandler.BuiltinColors.DEFAULT_COLOR
									? Component.translatable(DEFAULT_COLOR_SELECTED)
									: Component.translatable(COLOR_SELECTED))
			);
		}
	}
	
	private class ColorSlider extends ExtendedSlider
	{
		public enum ColorComp {
			R(34, Component.literal("r: ").withStyle(ChatFormatting.RED)),
			G(59, Component.literal("g: ").withStyle(ChatFormatting.GREEN)),
			B(84, Component.literal("b: ").withStyle(ChatFormatting.BLUE));
			public final int yBonus;
			public final Component prefix;
			ColorComp(int yBonus, Component prefix){this.yBonus=yBonus;this.prefix=prefix;}
		}
		ColorComp color;
		public ColorSlider(ColorComp color)
		{
			super(xOffset+23, yOffset+color.yBonus, 80, 20, color.prefix, Component.empty(), 0.0, 255.0, getPlayerColorComponent(color.ordinal()), true);
			this.color = color;
		}
		
		@Override
		protected void applyValue()
		{
			String hex = toFormattedHex(getSlidersValue());
			if(!hexBox.getValue().equalsIgnoreCase(hex))
				hexBox.setValue(hex);
		}
	}
	
	private record ColorSelector (int x, int y, int id)
	{
		public static final int WIDTH = 32, HEIGHT = 16;
		public Component getName() { return ColorHandler.BuiltinColors.getName(id); }
		public int getColor() { return ColorHandler.BuiltinColors.getColor(id); }
		public void draw(GuiGraphics guiGraphics)
		{
			guiGraphics.fill(xOffset+x, yOffset+y, xOffset+x+WIDTH, yOffset+y+HEIGHT,
					ColorHandler.BuiltinColors.getColor(id)|0xFF000000);
		}
		public boolean pointWithin(double mouseX, double mouseY)
		{
			return mouseX>=x+xOffset && mouseX<x+WIDTH+xOffset && mouseY>=y+yOffset && mouseY<y+HEIGHT+yOffset;
		}
	}
	
	private enum Tab { Canon, RGB }
}