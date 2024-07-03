package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.redstone.BlockTeleporterBlockEntity;
import com.mraof.minestuck.network.block.BlockTeleporterSettingsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BlockTeleporterScreen extends Screen
{
	public static final String TITLE = "minestuck.block_teleporter";
	public static final String X_MESSAGE = "minestuck.block_teleporter.x_message";
	public static final String Y_MESSAGE = "minestuck.block_teleporter.y_message";
	public static final String Z_MESSAGE = "minestuck.block_teleporter.z_message";
	public static final String DONE_MESSAGE = "minestuck.block_teleporter.done";
	private static final ResourceLocation GUI_BACKGROUND = new ResourceLocation("minestuck", "textures/gui/generic_medium.png");
	
	private static final int GUI_WIDTH = 150;
	private static final int GUI_HEIGHT = 98;
	
	private final BlockTeleporterBlockEntity be;
	private EditBox xPosOffsetTextField;
	private EditBox yPosOffsetTextField;
	private EditBox zPosOffsetTextField;
	private boolean validInput = true;
	
	
	BlockTeleporterScreen(BlockTeleporterBlockEntity be)
	{
		super(Component.translatable(TITLE));
		
		this.be = be;
	}
	
	@Override
	public void init()
	{
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		
		this.xPosOffsetTextField = new EditBox(this.font, this.width / 2 - 60, yOffset + 15, 40, 20, Component.translatable(X_MESSAGE));
		this.xPosOffsetTextField.setValue(String.valueOf(be.getTeleportOffset().getX()));
		addRenderableWidget(xPosOffsetTextField);
		
		this.yPosOffsetTextField = new EditBox(this.font, this.width / 2 - 20, yOffset + 15, 40, 20, Component.translatable(Y_MESSAGE));
		this.yPosOffsetTextField.setValue(String.valueOf(be.getTeleportOffset().getY()));
		addRenderableWidget(yPosOffsetTextField);
		
		this.zPosOffsetTextField = new EditBox(this.font, this.width / 2 + 20, yOffset + 15, 40, 20, Component.translatable(Z_MESSAGE));
		this.zPosOffsetTextField.setValue(String.valueOf(be.getTeleportOffset().getZ()));
		addRenderableWidget(zPosOffsetTextField);
		
		addRenderableWidget(new ExtendedButton(this.width / 2 - 20, yOffset + 70, 40, 20, Component.translatable(DONE_MESSAGE), button -> finish()));
	}
	
	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		
		int yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
		guiGraphics.blit(GUI_BACKGROUND, (this.width / 2) - (GUI_WIDTH / 2), yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
	}
	
	private void finish()
	{
		int x = parseInt(xPosOffsetTextField);
		int y = parseInt(yPosOffsetTextField);
		int z = parseInt(zPosOffsetTextField);
		
		BlockPos offsetPos = new BlockPos(x, y, z);
		
		if(validInput)
		{
			PacketDistributor.SERVER.noArg().send(new BlockTeleporterSettingsPacket(offsetPos, be.getBlockPos()));
			onClose();
		}
		
		validInput = true; //allows players to try again
	}
	
	private int parseInt(EditBox widget)
	{
		int parsedValue = 0; //arbitrary starting number that will not be used in the packet as is
		
		try
		{
			parsedValue = Integer.parseInt(widget.getValue());
			widget.setTextColor(0XFFFFFF); //refreshes text color to white in case it was invalid before but is now acceptable
		} catch(NumberFormatException ignored)
		{
			validInput = false;
			widget.setTextColor(0XFF0000); //changes text to red to indicate that it is an invalid type
		}
		
		return parsedValue;
	}
}
