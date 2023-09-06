package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.redstone.StatStorerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.StatStorerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class StatStorerScreen extends Screen
{
	public static final String TITLE = "minestuck.stat_storer";
	public static final String DIVIDE_VALUE_MESSAGE = "minestuck.stat_storer.divide_value";
	public static final String DIVIDE_COMPARATOR_MESSAGE = "minestuck.stat_storer.divide_comparator";
	public static final String DONE_MESSAGE = "minestuck.stat_storer.done";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final StatStorerBlockEntity be;
	private StatStorerBlockEntity.ActiveType activeType;
	
	private Button typeButton;
	
	private EditBox divideTextField;
	
	
	StatStorerScreen(StatStorerBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
		this.activeType = be.getActiveType();
	}
	
	@Override
	public void init()
	{
		addRenderableWidget(typeButton = new ExtendedButton(this.width / 2 - 67, (height - GUI_HEIGHT) / 2 + 15, 135, 20, Component.literal(activeType.getNameNoSpaces()), button -> changeActiveType()));
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		this.divideTextField = new EditBox(this.font, this.width / 2 - 18, yOffset + 50, 40, 18, Component.translatable(DIVIDE_COMPARATOR_MESSAGE));
		this.divideTextField.setValue(String.valueOf(be.getDivideValueBy()));
		addRenderableWidget(divideTextField);
		setInitialFocus(divideTextField);
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 18, yOffset + 70, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
	}
	
	/**
	 * Gets the ordinal of the current active type, and either cycles it back to the beginning at one if it is the last ordinal in the set or just sets it to the next ordinal in line
	 */
	private void changeActiveType()
	{
		activeType = StatStorerBlockEntity.ActiveType.fromInt(activeType.ordinal() < StatStorerBlockEntity.ActiveType.values().length - 1 ? activeType.ordinal() + 1 : 0);
		typeButton.setMessage(Component.literal(activeType.getNameNoSpaces()));
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(GUI_BACKGROUND, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		guiGraphics.drawString(font, Component.translatable(DIVIDE_VALUE_MESSAGE), (width / 2) - font.width(Component.translatable(DIVIDE_VALUE_MESSAGE)) / 2, yOffset + 40, 0x404040, false);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new StatStorerPacket(activeType, be.getBlockPos(), textToInt()));
		onClose();
	}
	
	private int textToInt()
	{
		try
		{
			return Integer.parseInt(divideTextField.getValue());
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}
}
