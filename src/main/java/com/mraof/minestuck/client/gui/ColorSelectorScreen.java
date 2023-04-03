package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
	private boolean useCanonColor = true, onRGBTab = false;
	private int redPrev = -1, greenPrev = -1, bluePrev = -1, selectedIndex = -1;
	private ForgeSlider redSlider, greenSlider, blueSlider;
	private ExtendedButton tabButton;
	private EditBox hexBox;
	
	private @Nullable ComputerBlockEntity be;
	
	// initialize color selectors
	static
	{
		canonColors = new ArrayList<>();
		int i = -1;
		int offset = 0;
		for(int yIndex = 0; yIndex < 5; yIndex++)
		{
			if (yIndex == 1 ) { offset+=3; }
			if (yIndex == 2 ) { offset+=3; }
			for(int xIndex = 0; xIndex < 4; xIndex++)
				canonColors.add(new ColorSelector(21 + 34 * xIndex, 32 + 18 * yIndex + offset, ++i));
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
		
		redSlider = addRenderableWidget(new ForgeSlider(xOffset + 23, yOffset + 34, 80, 20, Component.literal("r: ").withStyle(ChatFormatting.RED), Component.empty(), 0.0, 255.0, getPlayerColorComponent(0), true));
		greenSlider = addRenderableWidget(new ForgeSlider(xOffset + 23, yOffset + 59, 80, 20, Component.literal("g: ").withStyle(ChatFormatting.GREEN), Component.empty(), 0.0, 255.0, getPlayerColorComponent(1), true));
		blueSlider = addRenderableWidget(new ForgeSlider(xOffset + 23, yOffset + 84, 80, 20, Component.literal("b: ").withStyle(ChatFormatting.BLUE), Component.empty(), 0.0, 255.0, getPlayerColorComponent(2), true));
		
		hexBox = addRenderableWidget(new EditBox(Minecraft.getInstance().font, xOffset + 23, yOffset + 109, 80, 15, Component.empty()));
		hexBox.setResponder(this::onBoxUpdate);
		hexBox.setFilter((text) -> Pattern.matches("^[0-9a-fA-F]+$", text) || text.isEmpty());
		hexBox.setMaxLength(6);
		
		if(!firstTime)
			tabButton = addRenderableWidget(new ExtendedButton(xOffset+20, yOffset+130, 65, 20, Component.translatable(ADVANCED_TAB), button -> swapTabs()));
		
		// move choose button to middle and make larger on first time to account for missing tabs button
		int choosePos   = firstTime? 50 : 91;
		int chooseWidth = firstTime? 76 : 65;
		addRenderableWidget(new ExtendedButton(xOffset+choosePos, yOffset+130, chooseWidth, 20, Component.translatable(CHOOSE_MESSAGE), button -> selectColor()));
		
		setTab(false);
	}
	
	@Override
	public void resize(Minecraft mc, int w, int h)
	{
		xOffset = (w - guiWidth)/2;
		yOffset = (h - guiHeight)/2;
	
		super.resize(mc, w, h);
		
		redSlider.setValue(redPrev);
		greenSlider.setValue(greenPrev);
		blueSlider.setValue(bluePrev);
		
		hexBox.setValue(getSliderHex());
	}
	
	private int getPlayerColorComponent(int comp)
	{
		return Integer.parseInt(String.format("%6s" , Integer.toHexString(ClientPlayerData.getPlayerColor())).replace(' ', '0').substring(comp*2,comp*2+2),16);
	}
	
	private void setTab(boolean tab)
	{
		onRGBTab = tab;
		redSlider.visible = greenSlider.visible = blueSlider.visible = hexBox.visible = onRGBTab;
		
		if(onRGBTab && selectedIndex!=-1 && useCanonColor)
		{
			var hex = String.format("%6s", Integer.toHexString(ColorHandler.getColor(selectedIndex))).replace(' ','0');
			redSlider.setValue(Integer.parseInt(hex.substring(0,2),16));
			greenSlider.setValue(Integer.parseInt(hex.substring(2,4),16));
			blueSlider.setValue(Integer.parseInt(hex.substring(4,6),16));
		}
		
		if(!onRGBTab)
			selectedIndex = -1;
		
		if(!firstTime)
			tabButton.setMessage(onRGBTab ?Component.translatable(BASIC_TAB):Component.translatable(ADVANCED_TAB));
	}
	
	private void swapTabs()
	{
		onRGBTab = !onRGBTab;
		setTab(onRGBTab);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(poseStack);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, guiBackground);
		blit(poseStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage = I18n.get(SELECT_COLOR);
		font.draw(poseStack, cacheMessage, (width / 2F) - font.width(cacheMessage) / 2F, yOffset + 12, 0x404040);
		
		if(redSlider.getValueInt() != redPrev || greenSlider.getValueInt() != greenPrev || blueSlider.getValueInt() != bluePrev)
		{
			String hex = getSliderHex();
			if(!hexBox.getValue().equalsIgnoreCase(hex))
			{
				hexBox.setValue(hex);
				useCanonColor = false;
			}
		}
		
		redPrev = redSlider.getValueInt();
		greenPrev = greenSlider.getValueInt();
		bluePrev = blueSlider.getValueInt();
		
		if(onRGBTab)
		{
			// hide the color boxes by putting this other box on top of them
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, guiBackground);
			blit(poseStack, xOffset+20, yOffset+31, 0, guiHeight, 138, 98);
			
			// spirograph color preview
			RenderSystem.setShaderColor((float)redSlider.getValue()/255F, (float)greenSlider.getValue()/255, (float)blueSlider.getValue()/255, 0.5F);
			blit(poseStack, xOffset+106, yOffset+57, 47, 47, guiWidth, 20, 64, 64, 256, 256);
		} else
		{
			for(ColorSelector canonColor : canonColors)
			{
				canonColor.draw(poseStack);
				
				// SELECTION BOX //
				if(selectedIndex == canonColor.id)
				{
					RenderSystem.setShader(GameRenderer::getPositionTexShader);
					RenderSystem.setShaderColor(1, 1, 1, 1);
					RenderSystem.setShaderTexture(0, guiBackground);
					blit(poseStack, xOffset + canonColor.x - 2, yOffset + canonColor.y - 2, guiWidth, 0, ColorSelector.WIDTH + 4, ColorSelector.HEIGHT + 4);
				}
			}
		}
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
		
		if(!onRGBTab)
			for(ColorSelector canonColor : canonColors)
				if(canonColor.pointWithin(mouseX - xOffset, mouseY - yOffset))
					renderTooltip(poseStack, canonColor.getName(), mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0 && !onRGBTab)
		{
			int index = getColorIndexAtMouse(mouseX, mouseY);
			if(index != -1)
			{
				selectedIndex = index != selectedIndex ? index : -1;
				useCanonColor = true;
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private int getColorIndexAtMouse(double mouseX, double mouseY)
	{
		for(ColorSelector canonColor : canonColors)
			if(canonColor.pointWithin(mouseX-xOffset, mouseY-yOffset))
				return canonColor.id;
		return -1;
	}
	
	private String getSliderHex()
	{
		String rHex = String.format("%2s", Integer.toHexString(redSlider.getValueInt())).replace(' ', '0');
		String gHex = String.format("%2s", Integer.toHexString(greenSlider.getValueInt())).replace(' ', '0');
		String bHex = String.format("%2s", Integer.toHexString(blueSlider.getValueInt())).replace(' ', '0');
		return rHex+gHex+bHex;
	}
	
	private void onBoxUpdate(String hex)
	{
		if (hex.length()!=6) return;
		redSlider.setValue(Integer.parseInt(hex.substring(0,2),16));
		greenSlider.setValue(Integer.parseInt(hex.substring(2,4),16));
		blueSlider.setValue(Integer.parseInt(hex.substring(4,6),16));
		
		useCanonColor = false;
	}
	
	private void selectColor()
	{
		if(onRGBTab)
			ClientPlayerData.selectColorRGB(Integer.parseInt(hexBox.getValue(), 16));
		else
			ClientPlayerData.selectColor(selectedIndex);
		
		if(be != null)
			minecraft.setScreen(new ComputerScreen(minecraft, be));
		else
			minecraft.setScreen(null);
	}
	
	@Override
	public void removed()
	{
		if(firstTime && minecraft != null && minecraft.player != null)
		{
			minecraft.player.sendSystemMessage(
					Component.literal("[Minestuck] ").append(
							ClientPlayerData.getPlayerColor()==ColorHandler.DEFAULT_COLOR?
									Component.translatable(DEFAULT_COLOR_SELECTED):
									Component.translatable(COLOR_SELECTED))
			);
		}
	}
	
	private record ColorSelector (int x, int y, int id)
	{
		public static final int WIDTH = 32, HEIGHT = 16;
		public Component getName() { return ColorHandler.getName(id); }
		public int getColor() { return ColorHandler.getColor(id); }
		public void draw(PoseStack poseStack)
		{
			fill(poseStack, xOffset+x, yOffset+y, xOffset+x+WIDTH, yOffset+y+HEIGHT, ColorHandler.getColor(id)|0xFF000000);
		}
		public boolean pointWithin(double mouseX, double mouseY)
		{
			return mouseX>=x && mouseX<x+WIDTH && mouseY>=y && mouseY<y+HEIGHT;
		}
	}
}