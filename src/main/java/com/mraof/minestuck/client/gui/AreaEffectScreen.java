package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.AreaEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class AreaEffectScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int guiWidth = 150;
	private static final int guiHeight = 98;
	
	private static final String minPosMessage = "Min Pos Facing Offset";
	private static final String maxPosMessage = "Max Pos Facing Offset";
	
	private final AreaEffectTileEntity te;
	private TextFieldWidget minPosDestinationTextFieldX;
	private TextFieldWidget minPosDestinationTextFieldY;
	private TextFieldWidget minPosDestinationTextFieldZ;
	private TextFieldWidget maxPosDestinationTextFieldX;
	private TextFieldWidget maxPosDestinationTextFieldY;
	private TextFieldWidget maxPosDestinationTextFieldZ;
	
	
	AreaEffectScreen(AreaEffectTileEntity te)
	{
		super(new StringTextComponent("Area Effect Block"));
		
		this.te = te;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (guiHeight / 2);
		
		this.minPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, new StringTextComponent("X value of min effect pos"));
		this.minPosDestinationTextFieldX.setValue(String.valueOf(te.getMinAreaOffset().getX()));
		addButton(minPosDestinationTextFieldX);
		
		this.minPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, new StringTextComponent("Y value of min effect pos"));
		this.minPosDestinationTextFieldY.setValue(String.valueOf(te.getMinAreaOffset().getY()));
		addButton(minPosDestinationTextFieldY);
		
		this.minPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, new StringTextComponent("Z value of min effect pos"));
		this.minPosDestinationTextFieldZ.setValue(String.valueOf(te.getMinAreaOffset().getZ()));
		addButton(minPosDestinationTextFieldZ);
		
		this.maxPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 50, 40, 20, new StringTextComponent("X value of max effect pos"));
		this.maxPosDestinationTextFieldX.setValue(String.valueOf(te.getMaxAreaOffset().getX()));
		addButton(maxPosDestinationTextFieldX);
		
		this.maxPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 50, 40, 20, new StringTextComponent("Y value of max effect pos"));
		this.maxPosDestinationTextFieldY.setValue(String.valueOf(te.getMaxAreaOffset().getY()));
		addButton(maxPosDestinationTextFieldY);
		
		this.maxPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 50, 40, 20, new StringTextComponent("Z value of max effect pos"));
		this.maxPosDestinationTextFieldZ.setValue(String.valueOf(te.getMaxAreaOffset().getZ()));
		addButton(maxPosDestinationTextFieldZ);
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 73, 40, 20, new StringTextComponent("DONE"), button -> finish()));
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bind(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit(matrixStack, (this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.draw(matrixStack, minPosMessage, (width / 2) - font.width(minPosMessage) / 2, yOffset + 5, 0x404040);
		font.draw(matrixStack, maxPosMessage, (width / 2) - font.width(maxPosMessage) / 2, yOffset + 40, 0x404040);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		int minX = parseInt(minPosDestinationTextFieldX);
		int minY = parseInt(minPosDestinationTextFieldY);
		int minZ = parseInt(minPosDestinationTextFieldZ);
		int maxX = parseInt(maxPosDestinationTextFieldX);
		int maxY = parseInt(maxPosDestinationTextFieldY);
		int maxZ = parseInt(maxPosDestinationTextFieldZ);
		
		BlockPos minOffsetPos = new BlockPos(minX, minY, minZ);
		BlockPos maxOffsetPos = new BlockPos(maxX, maxY, maxZ);
		
		MSPacketHandler.sendToServer(new AreaEffectPacket(minOffsetPos, maxOffsetPos, te.getBlockPos()));
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
}