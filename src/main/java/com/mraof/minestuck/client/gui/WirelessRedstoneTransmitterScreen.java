package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.WirelessRedstoneTransmitterPacket;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class WirelessRedstoneTransmitterScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	WirelessRedstoneTransmitterTileEntity te;
	private TextFieldWidget destinationTextFieldX;
	private TextFieldWidget destinationTextFieldY;
	private TextFieldWidget destinationTextFieldZ;
	
	
	WirelessRedstoneTransmitterScreen(WirelessRedstoneTransmitterTileEntity te)
	{
		super(new StringTextComponent("Wireless Redstone"));
		
		this.te = te;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.destinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 10, 40, 20, new StringTextComponent("X value of destination block pos")); //TODO make these translatable
		this.destinationTextFieldX.setValue(String.valueOf(te.getDestinationBlockPosFromOffset().getX()));
		addButton(destinationTextFieldX);
		
		this.destinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 10, 40, 20, new StringTextComponent("Y value of destination block pos"));
		this.destinationTextFieldY.setValue(String.valueOf(te.getDestinationBlockPosFromOffset().getY()));
		addButton(destinationTextFieldY);
		
		this.destinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 10, 40, 20, new StringTextComponent("Z value of destination block pos"));
		this.destinationTextFieldZ.setValue(String.valueOf(te.getDestinationBlockPosFromOffset().getZ()));
		addButton(destinationTextFieldZ);
		
		addButton(new ExtendedButton(this.width / 2 - 45, yOffset + 40, 90, 20, new StringTextComponent("Find Receiver"), button -> findReceiver()));
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 70, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(GUI_BACKGROUND);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		this.blit(matrixStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void findReceiver()
	{
		BlockPos receiverPos = te.findReceiver();
		if(receiverPos != null)
		{
			destinationTextFieldX.setValue(String.valueOf(receiverPos.getX()));
			destinationTextFieldY.setValue(String.valueOf(receiverPos.getY()));
			destinationTextFieldZ.setValue(String.valueOf(receiverPos.getZ()));
		}
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new WirelessRedstoneTransmitterPacket(parseBlockPos(), te.getBlockPos()));
		onClose();
	}
	
	private static int parseInt(TextFieldWidget widget)
	{
		try
		{
			return Integer.parseInt(widget.getValue());
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}
	
	private BlockPos parseBlockPos()
	{
		int x = parseInt(destinationTextFieldX);
		int y = parseInt(destinationTextFieldY);
		int z = parseInt(destinationTextFieldZ);
		
		return new BlockPos(x, y, z);
	}
}