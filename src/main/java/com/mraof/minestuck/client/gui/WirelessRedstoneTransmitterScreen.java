package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.WirelessRedstoneTransmitterPacket;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import com.mraof.minestuck.util.Debug;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class WirelessRedstoneTransmitterScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/wireless_redstone.png");
	
	private static final int guiWidth = 150; //126 previously
	private static final int guiHeight = 98;
	
	WirelessRedstoneTransmitterTileEntity te;
	private TextFieldWidget destinationTextFieldX;
	private TextFieldWidget destinationTextFieldY;
	private TextFieldWidget destinationTextFieldZ;
	private Button findRecieverButton;
	private Button doneButton;
	
	private BlockPos outgoingDestinationPos;
	
	
	WirelessRedstoneTransmitterScreen(WirelessRedstoneTransmitterTileEntity te)
	{
		super(new StringTextComponent("Wireless Redstone"));
		
		this.te = te;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.destinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 10, 40, 20, "");
		this.destinationTextFieldX.setMaxStringLength(12);
		this.destinationTextFieldX.setText(String.valueOf(te.getDestinationBlockPos().getX()));
		this.destinationTextFieldX.setFocused2(true);
		addButton(destinationTextFieldX);
		if(this.destinationTextFieldX.active)
			this.destinationTextFieldX.setFocused2(true);
		
		this.destinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 10, 40, 20, "Wireless signal destination code");    //TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.destinationTextFieldY.setMaxStringLength(12);
		this.destinationTextFieldY.setText(String.valueOf(te.getDestinationBlockPos().getY()));
		if(this.destinationTextFieldY.active)
			this.destinationTextFieldY.setFocused2(true);
		addButton(destinationTextFieldY);
		
		this.destinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 10, 40, 20, ""); //was yOffset + 25
		this.destinationTextFieldZ.setMaxStringLength(12);
		this.destinationTextFieldZ.setText(String.valueOf(te.getDestinationBlockPos().getZ()));
		if(this.destinationTextFieldZ.active)
			this.destinationTextFieldZ.setFocused2(true);
		addButton(destinationTextFieldZ);
		
		addButton(findRecieverButton = new ExtendedButton(this.width / 2 - 45, yOffset + 40, 90, 20, "Find Reciever", button -> findReciever()));
		
		addButton(doneButton = new ExtendedButton(this.width / 2 - 20, yOffset + 70, 40, 20, I18n.format("gui.done"), button -> finish()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		//font.drawString(te.getId(), (this.width / 2) - font.getStringWidth(te.getId()) / 2, yOffset + 10, 0x404040);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void findReciever()
	{
		outgoingDestinationPos = te.findReciever(te);
		
		outgoingDestinationPos();
	}
	
	private void finish()
	{
		WirelessRedstoneTransmitterPacket packet = new WirelessRedstoneTransmitterPacket(outgoingDestinationPos(), te.getPos());
		MSPacketHandler.sendToServer(packet);
		this.minecraft.displayGuiScreen(null);
	}
	
	private BlockPos outgoingDestinationPos()
	{
		if(outgoingDestinationPos != null)
			return outgoingDestinationPos;
		else
			return textToIntToBlockPos();
	}
	
	private BlockPos textToIntToBlockPos()
	{
		int x = 0;
		int y = 0;
		int z = 0;
		
		try
		{
			x = Integer.parseInt(destinationTextFieldX.getText());
		} catch(NumberFormatException ignored)
		{
		}
		
		try
		{
			y = Integer.parseInt(destinationTextFieldY.getText());
		} catch(NumberFormatException ignored)
		{
		}
		
		try
		{
			z = Integer.parseInt(destinationTextFieldZ.getText());
		} catch(NumberFormatException ignored)
		{
		}
		Debug.debugf("%s %s %s, pos = %s", x, y, z, new BlockPos(x, y, z));
		
		return new BlockPos(x, y, z);
	}
}

