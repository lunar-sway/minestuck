package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.TransportalizerPacket;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class TransportalizerScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/transportalizer.png");

	private static final int guiWidth = 126;
	private static final int guiHeight = 98;

	TransportalizerTileEntity te;
	private TextFieldWidget destinationTextField;
	
	
	TransportalizerScreen(TransportalizerTileEntity te)
	{
		super(new StringTextComponent("Transportalizer"));

		this.te = te;
	}

	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.destinationTextField = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 25, 40, 20, "Transportalizer destination code");	//TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.destinationTextField.setMaxStringLength(4);
		this.destinationTextField.setText(te.getDestId());
		this.destinationTextField.setFocused2(true);
		addButton(destinationTextField);
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 50, 40, 20, I18n.format("gui.done"), button -> finish()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.drawString(te.getId(), (this.width / 2) - font.getStringWidth(te.getId()) / 2, yOffset + 10, te.getActive() ? 0x404040 : 0xFF0000);
		super.render(mouseX, mouseY, partialTicks);
	}

	private void finish()
	{
		if(this.destinationTextField.getText().length() == 4)
		{
			//Debug.print("Sending transportalizer packet with destination of " + this.destinationTextField.getText());
			TransportalizerPacket packet = new TransportalizerPacket(te.getPos(), destinationTextField.getText().toUpperCase());
			MSPacketHandler.sendToServer(packet);
			this.minecraft.displayGuiScreen(null);
		}
	}

}

