package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.network.AreaEffectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class AreaEffectScreen extends Screen
{
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/wireless_redstone.png");
	
	private static final int guiWidth = 150; //126 previously
	private static final int guiHeight = 98;
	
	private static final String minPosMessage = "Min Effect Pos";
	private static final String maxPosMessage = "Max Effect Pos";
	
	AreaEffectTileEntity te;
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
		
		this.minPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, "");
		this.minPosDestinationTextFieldX.setText(String.valueOf(te.getMinEffectPos().getX()));
		addButton(minPosDestinationTextFieldX);
		
		this.minPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, "Min pos coords");    //TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.minPosDestinationTextFieldY.setText(String.valueOf(te.getMinEffectPos().getY()));
		addButton(minPosDestinationTextFieldY);
		
		this.minPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, ""); //was yOffset + 25
		this.minPosDestinationTextFieldZ.setText(String.valueOf(te.getMinEffectPos().getZ()));
		addButton(minPosDestinationTextFieldZ);
		
		this.maxPosDestinationTextFieldX = new TextFieldWidget(this.font, this.width / 2 - 60, yOffset + 50, 40, 20, "");
		this.maxPosDestinationTextFieldX.setText(String.valueOf(te.getMaxEffectPos().getX()));
		addButton(maxPosDestinationTextFieldX);
		
		this.maxPosDestinationTextFieldY = new TextFieldWidget(this.font, this.width / 2 - 20, yOffset + 50, 40, 20, "Max pos coords");    //TODO Use translation instead, and maybe look at other text fields for what the text should be
		this.maxPosDestinationTextFieldY.setText(String.valueOf(te.getMaxEffectPos().getY()));
		addButton(maxPosDestinationTextFieldY);
		
		this.maxPosDestinationTextFieldZ = new TextFieldWidget(this.font, this.width / 2 + 20, yOffset + 50, 40, 20, "");
		this.maxPosDestinationTextFieldZ.setText(String.valueOf(te.getMaxEffectPos().getZ()));
		addButton(maxPosDestinationTextFieldZ);
		
		addButton(new ExtendedButton(this.width / 2 - 20, yOffset + 73, 40, 20, I18n.format("gui.done"), button -> finish()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		int yOffset = (this.height / 2) - (guiHeight / 2);
		this.blit((this.width / 2) - (guiWidth / 2), yOffset, 0, 0, guiWidth, guiHeight);
		font.drawString(minPosMessage, (width / 2) - font.getStringWidth(minPosMessage) / 2, yOffset + 5, 0x404040);
		font.drawString(maxPosMessage, (width / 2) - font.getStringWidth(maxPosMessage) / 2, yOffset + 40, 0x404040);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	private void finish()
	{
		AreaEffectPacket packet = new AreaEffectPacket(parseMinBlockPos(), parseMaxBlockPos(), te.getPos());
		MSPacketHandler.sendToServer(packet);
		onClose();
	}
	
	private static int parseInt(TextFieldWidget widget)
	{
		try
		{
			return Integer.parseInt(widget.getText());
		} catch(NumberFormatException ignored)
		{
			return 0;
		}
	}
	
	private BlockPos parseMinBlockPos()
	{
		int x = parseInt(minPosDestinationTextFieldX);
		int y = parseInt(minPosDestinationTextFieldY);
		int z = parseInt(minPosDestinationTextFieldZ);
		
		return new BlockPos(x, y, z);
	}
	
	private BlockPos parseMaxBlockPos()
	{
		int x = parseInt(maxPosDestinationTextFieldX);
		int y = parseInt(maxPosDestinationTextFieldY);
		int z = parseInt(maxPosDestinationTextFieldZ);
		
		return new BlockPos(x, y, z);
	}
}