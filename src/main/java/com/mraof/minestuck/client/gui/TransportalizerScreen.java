package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TransportalizerPacket;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class TransportalizerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_small.png");

	private static final int guiWidth = 126;
	private static final int guiHeight = 98;

	TransportalizerTileEntity te;
	private TextFieldWidget destinationTextField;
	private Button doneButton;
	
	
	TransportalizerScreen(TransportalizerTileEntity te)
	{
		super(new StringTextComponent("Transportalizer"));

		this.te = te;
	}

	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.destinationTextField = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 25, 40, 20, new StringTextComponent("Transportalizer destination code"));	//TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.destinationTextField.setMaxLength(4);
		this.destinationTextField.setValue(te.getDestId());
		this.destinationTextField.setFocus(true);
		destinationTextField.setResponder(s -> doneButton.active = s.length() == 4);
		addButton(destinationTextField);
		setInitialFocus(destinationTextField);
		
		addButton(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, new TranslationTextComponent("gui.done"), button -> finish()));
		doneButton.active = destinationTextField.getValue().length() == 4;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.draw(matrixStack, te.getId(), (this.width / 2) - font.width(te.getId()) / 2, yOffset + 10, te.isActive() ? 0x404040 : 0xFF0000);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	private void finish()
	{
		if(this.destinationTextField.getValue().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPacket packet = new TransportalizerPacket(te.getBlockPos(), destinationTextField.getValue().toUpperCase());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.setScreen(null);
		}
	}

}

