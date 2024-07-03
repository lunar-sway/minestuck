package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.network.block.TransportalizerPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
		int yOffset = (height / 2) - (guiHeight / 2);
		destinationTextField = new EditBox(font, width / 2 - 20, yOffset + 25, 40, 20, Component.translatable(DESTINATION_CODE_MESSAGE));	//TODO Maybe look at other text fields for what the text should be
		destinationTextField.setCanLoseFocus(false);
		destinationTextField.setMaxLength(4);
		destinationTextField.setValue(be.getDestId());
		destinationTextField.setResponder(s -> doneButton.active = s.length() == 4);
		addRenderableWidget(destinationTextField);
		setInitialFocus(destinationTextField);
		
		addRenderableWidget(doneButton = new ExtendedButton(width / 2 - 20, yOffset + 50, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
		doneButton.active = destinationTextField.getValue().length() == 4;
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (height / 2) - (guiHeight / 2);
		guiGraphics.blit(guiBackground, (width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (height / 2) - (guiHeight / 2);
		if(!be.hasId())
			guiGraphics.drawString(font, "Enter ID:", width / 2F - font.width("Enter ID:") / 2F, yOffset + 10, 0x404040, false);
		else
			guiGraphics.drawString(font, be.getId(), (width / 2F) - font.width(be.getId()) / 2F, yOffset + 10, be.isActive() ? 0x404040 : 0xFF0000, false);
	}

	private void finish()
	{
		String text = destinationTextField.getValue().toUpperCase();
		if(text.length() != 4)
			return;
		
		if(be.hasId()) {
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPackets.SetDestId packet = new TransportalizerPackets.SetDestId(be.getBlockPos(), text);
			PacketDistributor.SERVER.noArg().send(packet);
			minecraft.setScreen(null);
		} else {
			TransportalizerPackets.SetId packet = new TransportalizerPackets.SetId(be.getBlockPos(), text);
			PacketDistributor.SERVER.noArg().send(packet);
			minecraft.setScreen(null);
		}
	}
}
