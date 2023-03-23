package com.mraof.minestuck.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.regex.Pattern;

public class ColorPickerProgram extends ComputerProgram
{
	public static final String NAME = "minestuck.program.color_picker.name";
	public static final String CHOOSE = "minestuck.program.color_picker.choose";
	public static final String SELECTED = "minestuck.program.color_picker.selected";
	
	// TODO maybe make this preview look a little prettier
	public static final ResourceLocation previewCircle = new ResourceLocation("minestuck", "textures/gui/color_picker.png");
	private static final int CIRCLE_SIZE = 50, SMALL_CIRCLE_SIZE = 40;
	
	private ForgeSlider redSlider, greenSlider, blueSlider;
	private EditBox hexBox;
	private int color;
	private double redPrev, greenPrev, bluePrev; // needed to track changes to sliders
	private int xPoint, yPoint;
	
	@Override
	public void onInitGui(ComputerScreen gui)
	{
		xPoint = (gui.width - ComputerScreen.xSize) / 2;
		yPoint = (gui.height - ComputerScreen.ySize) / 2;
		
		redSlider   = gui.addRenderableWidget(new ForgeSlider(xPoint + 15, yPoint + 45, 80, 20, Component.literal("r: ").withStyle(ChatFormatting.RED), Component.empty(), 0.0, 255.0, getPlayerColorComponent(0), true));
		greenSlider = gui.addRenderableWidget(new ForgeSlider(xPoint + 15, yPoint + 70, 80, 20, Component.literal("g: ").withStyle(ChatFormatting.GREEN), Component.empty(), 0.0, 255.0, getPlayerColorComponent(1), true));
		blueSlider  = gui.addRenderableWidget(new ForgeSlider(xPoint + 15, yPoint + 95, 80, 20, Component.literal("b: ").withStyle(ChatFormatting.BLUE), Component.empty(), 0.0, 255.0, getPlayerColorComponent(2), true));
		
		hexBox = gui.addRenderableWidget(new EditBox(Minecraft.getInstance().font, xPoint + 15, yPoint + 120, 80, 15, Component.empty()));
		hexBox.setResponder(this::onBoxUpdated);
		hexBox.setFilter((text) -> Pattern.matches("^[0-9|a-f]+$", text)||text.isEmpty()); //TODO make this only regex
		hexBox.setMaxLength(6);
		
		// finalize color selection
		gui.addRenderableWidget(new ExtendedButton(xPoint + 100, yPoint + 120, 60, 15, Component.translatable(CHOOSE), this::finalizeColor));
	}
	
	/**
	 * selects the color specified for the player and sends message in chat
	 */
	private void finalizeColor(Button button)
	{
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		
		ClientPlayerData.selectColorRGB(color);
		player.displayClientMessage(Component.translatable(SELECTED, Component.literal("\u2588").withStyle(Style.EMPTY.withColor(color))), false);
	}
	
	@Override
	public void paintGui(PoseStack poseStack, ComputerScreen gui, ComputerBlockEntity be)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, ComputerScreen.guiBackground);
		gui.blit(poseStack, (gui.width / 2) - (ComputerScreen.xSize / 2), (gui.height / 2) - (ComputerScreen.ySize / 2), 0, 0, ComputerScreen.xSize, ComputerScreen.ySize);
		
		if (redSlider.getValue() != redPrev || greenSlider.getValue() != greenPrev || blueSlider.getValue() != bluePrev)
		{
			updateBox();
		}
		
		if (hexBox.getValue().length() == 6)
			color = Integer.parseInt(hexBox.getValue(), 16);
		
		redPrev = redSlider.getValue();
		greenPrev = greenSlider.getValue();
		bluePrev = blueSlider.getValue();
		
		RenderSystem.setShaderTexture(0, previewCircle);
		// player current color
		RenderSystem.setShaderColor((float)getPlayerColorComponent(0)/255, (float)getPlayerColorComponent(1)/255, (float)getPlayerColorComponent(2)/255, 0.5F);
		GuiComponent.blit(poseStack, xPoint+120, yPoint+65, SMALL_CIRCLE_SIZE, SMALL_CIRCLE_SIZE, CIRCLE_SIZE, 0, SMALL_CIRCLE_SIZE, SMALL_CIRCLE_SIZE, 128, 64);
		
		// selected in ui
		RenderSystem.setShaderColor(((float)redSlider.getValue())/255, ((float)greenSlider.getValue())/255, ((float)blueSlider.getValue())/255, 0.5F);
		GuiComponent.blit(poseStack, xPoint+100, yPoint+45, CIRCLE_SIZE, CIRCLE_SIZE, 0, 0, CIRCLE_SIZE, CIRCLE_SIZE, 128, 64);
	}
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	/**
	 * updates the hex input box to match the values of the three rgb sliders
	 */
	private void updateBox()
	{
		String rHex = String.format("%2s", Integer.toHexString(redSlider.getValueInt())).replace(' ', '0');
		String gHex = String.format("%2s", Integer.toHexString(greenSlider.getValueInt())).replace(' ', '0');
		String bHex = String.format("%2s", Integer.toHexString(blueSlider.getValueInt())).replace(' ', '0');
		hexBox.setValue(rHex + gHex + bHex);
	}
	
	/**
	 * updates the rgb sliders only if the box is a full 6-digit hex number
	 * @param s value of the box
	 */
	private void onBoxUpdated(String s)
	{
		if (s.length() == 6)
		{
			redSlider.setValue(Integer.parseInt(hexBox.getValue().substring(0,2),16));
			greenSlider.setValue(Integer.parseInt(hexBox.getValue().substring(2,4),16));
			blueSlider.setValue(Integer.parseInt(hexBox.getValue().substring(4,6),16));
		}
	}
	
	/**
	 * gets a color in hexadecimal format with six digits
	 * @param col the integer color to translate
	 * @return the hex string
	 */
	private String toHexStringColorFormat(int col)
	{
		return String.format("%6s" , Integer.toHexString(col)).replace(' ', '0');
	}
	
	/**
	 * gets an rgb component from the player's current colour
	 * integer color -> hexadecimal format -> make sure six characters -> split r/g/b -> parse back to integer
	 * @param comp 0 -> red, 1 -> green, 2 -> blue
	 * @return the value from 0 to 255 of that specific channel
	 */
	private int getPlayerColorComponent(int comp)
	{
		assert(0 <= comp && comp <= 3);
		return Integer.parseInt(toHexStringColorFormat(ClientPlayerData.getPlayerColor()).substring(comp*2,comp*2+2),16);
	}
}