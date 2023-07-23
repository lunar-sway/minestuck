package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TransportalizerPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class TransportalizerScreen extends Screen
{
	public static final String TITLE = "minestuck.transportalizer";
	public static final String DESTINATION_CODE_MESSAGE = "minestuck.transportalizer.destination_code";
	public static final String DONE_MESSAGE = "minestuck.transportalizer.done";
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_small.png");

	private static final int guiWidth = 126;
	private static final int guiHeight = 98;

	TransportalizerBlockEntity be;
	private EditBox destinationTextField;
	private Button doneButton;
	
	
	TransportalizerScreen(TransportalizerBlockEntity be)
	{
		super(Component.translatable(TITLE));

		this.be = be;
	}

	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.destinationTextField = new EditBox(this.font, this.width / 2 - 20, yOffset + 25, 40, 20, Component.translatable(DESTINATION_CODE_MESSAGE));	//TODO Maybe look at other text fields for what the text should be
		this.destinationTextField.setCanLoseFocus(false);
		this.destinationTextField.setMaxLength(4);
		this.destinationTextField.setValue(be.getDestId());
		destinationTextField.setResponder(s -> doneButton.active = s.length() == 4);
		addRenderableWidget(destinationTextField);
		setInitialFocus(destinationTextField);
		
		addRenderableWidget(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
		doneButton.active = destinationTextField.getValue().length() == 4;
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(guiGraphics);
		
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(guiBackground, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		
		guiGraphics.drawString(font, be.getId(), (this.width / 2F) - font.width(be.getId()) / 2F, yOffset + 10, be.isActive() ? 0x404040 : 0xFF0000, false);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}

	private void finish()
	{
		if(this.destinationTextField.getValue().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPacket packet = new TransportalizerPacket(be.getBlockPos(), destinationTextField.getValue().toUpperCase());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.setScreen(null);
		}
	}

}

