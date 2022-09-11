package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.redstone.WirelessRedstoneTransmitterBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.WirelessRedstoneTransmitterPacket;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class WirelessRedstoneTransmitterScreen extends Screen
{
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	WirelessRedstoneTransmitterBlockEntity be;
	private EditBox destinationTextFieldX;
	private EditBox destinationTextFieldY;
	private EditBox destinationTextFieldZ;
	
	
	WirelessRedstoneTransmitterScreen(WirelessRedstoneTransmitterBlockEntity be)
	{
		super(new TextComponent("Wireless Redstone"));
		
		this.be = be;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.destinationTextFieldX = new EditBox(this.font, this.width / 2 - 60, yOffset + 10, 40, 20, new TextComponent("X value of destination block pos")); //TODO make these translatable
		this.destinationTextFieldX.setValue(String.valueOf(be.getDestinationBlockPosFromOffset().getX()));
		addRenderableWidget(destinationTextFieldX);
		
		this.destinationTextFieldY = new EditBox(this.font, this.width / 2 - 20, yOffset + 10, 40, 20, new TextComponent("Y value of destination block pos"));
		this.destinationTextFieldY.setValue(String.valueOf(be.getDestinationBlockPosFromOffset().getY()));
		addRenderableWidget(destinationTextFieldY);
		
		this.destinationTextFieldZ = new EditBox(this.font, this.width / 2 + 20, yOffset + 10, 40, 20, new TextComponent("Z value of destination block pos"));
		this.destinationTextFieldZ.setValue(String.valueOf(be.getDestinationBlockPosFromOffset().getZ()));
		addRenderableWidget(destinationTextFieldZ);
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 45, yOffset + 40, 90, 20, new TextComponent("Find Receiver"), button -> findReceiver()));
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 20, yOffset + 70, 40, 20, new TextComponent("DONE"), button -> finish()));
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, GUI_BACKGROUND);
		this.blit(poseStack, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	private void findReceiver()
	{
		BlockPos receiverPos = be.findReceiver();
		if(receiverPos != null)
		{
			destinationTextFieldX.setValue(String.valueOf(receiverPos.getX()));
			destinationTextFieldY.setValue(String.valueOf(receiverPos.getY()));
			destinationTextFieldZ.setValue(String.valueOf(receiverPos.getZ()));
		}
	}
	
	private void finish()
	{
		MSPacketHandler.sendToServer(new WirelessRedstoneTransmitterPacket(parseBlockPos(), be.getBlockPos()));
		onClose();
	}
	
	private static int parseInt(EditBox widget)
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