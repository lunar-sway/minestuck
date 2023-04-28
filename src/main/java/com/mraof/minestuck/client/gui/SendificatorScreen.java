package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import com.mraof.minestuck.inventory.SendificatorMenu;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SendificatorPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SendificatorScreen extends MachineScreen<SendificatorMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/sendificator.png");
	private static final ResourceLocation PROGRESS_BAR_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/uranium_level.png");
	
	private static final int PROGRESS_BAR_X = 52;
	private static final int PROGRESS_BAR_Y = 24;
	private static final int PROGRESS_BAR_WIDTH = 35;
	private static final int PROGRESS_BAR_HEIGHT = 39;
	private static final int BUTTON_X = 115;
	private static final int BUTTON_Y = 60;
	
	private EditBox destinationTextFieldX;
	private EditBox destinationTextFieldY;
	private EditBox destinationTextFieldZ;
	private ExtendedButton updateButton;
	private ExtendedButton goButton;
	@Nullable
	private BlockPos parsedPos;
	
	
	SendificatorScreen(SendificatorMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		updateButton = addRenderableWidget(new ExtendedButton((width - imageWidth) / 2 + 105, this.topPos + 40, 50, 12, Component.literal("Update"), button -> updateDestinationPos()));
		
		this.destinationTextFieldX = new EditBox(this.font, this.width / 2 - 10, this.topPos + 10, 35, 15, Component.literal("X value of destination block pos")); //TODO make these translatable
		destinationTextFieldX.setMaxLength(10);
		addRenderableWidget(destinationTextFieldX);
		destinationTextFieldX.setResponder(s -> onTextFieldChange());
		
		this.destinationTextFieldY = new EditBox(this.font, this.width / 2 + 25, this.topPos + 10, 20, 15, Component.literal("Y value of destination block pos"));
		destinationTextFieldY.setMaxLength(3);
		addRenderableWidget(destinationTextFieldY);
		destinationTextFieldY.setResponder(s -> onTextFieldChange());
		
		this.destinationTextFieldZ = new EditBox(this.font, this.width / 2 + 45, this.topPos + 10, 35, 15, Component.literal("Z value of destination block pos"));
		destinationTextFieldZ.setMaxLength(10);
		addRenderableWidget(destinationTextFieldZ);
		destinationTextFieldZ.setResponder(s -> onTextFieldChange());
		
		//activates processContents() in SendificatorBlockEntity
		goButton = new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, true);
		addRenderableWidget(goButton);
		
		BlockPos destination = this.menu.getDestination();
		if(destination != null)
		{
			this.destinationTextFieldX.setValue(String.valueOf(destination.getX()));
			this.destinationTextFieldY.setValue(String.valueOf(destination.getY()));
			this.destinationTextFieldZ.setValue(String.valueOf(destination.getZ()));
		}
		
		updateButton.active = false;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		goButton.active = this.menu.hasDestination();
		// Make the update button clickable only when there is a parsed position and it is different from the original
		updateButton.active = parsedPos != null && !parsedPos.equals(this.menu.getDestination());
		
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
	{
		//draws the name of the BE
		font.draw(poseStack, this.title, 8, 6, 0x404040);
		
		//draws "Inventory" or your regional equivalent
		font.draw(poseStack, this.playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040);
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float par1, int par2, int par3)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		//draw background
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS_BAR_TEXTURE);
		int height = getScaledValue(menu.getFuel(), SendificatorBlockEntity.MAX_FUEL, PROGRESS_BAR_HEIGHT);
		blit(poseStack, this.leftPos + PROGRESS_BAR_X, this.topPos + PROGRESS_BAR_Y + PROGRESS_BAR_HEIGHT - height,
				0, PROGRESS_BAR_HEIGHT - height, PROGRESS_BAR_WIDTH, height, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}
	
	@Override
	public void resize(Minecraft minecraft, int width, int height)
	{
		String destX = this.destinationTextFieldX.getValue();
		String destY = this.destinationTextFieldY.getValue();
		String destZ = this.destinationTextFieldZ.getValue();
		this.init(minecraft, width, height);
		this.destinationTextFieldX.setValue(destX);
		this.destinationTextFieldY.setValue(destY);
		this.destinationTextFieldZ.setValue(destZ);
	}
	
	private void updateDestinationPos()
	{
		if(parsedPos != null)
		{
			MSPacketHandler.sendToServer(new SendificatorPacket(parsedPos));
		}
	}
	
	/**
	 * Sets the Update button to active if a valid change has occurred
	 */
	private void onTextFieldChange()
	{
		try
		{
			parsedPos = parseBlockPos();
		} catch(NumberFormatException ignored)
		{
			parsedPos = null;
		}
	}
	
	private BlockPos parseBlockPos()
			throws NumberFormatException
	{
		int x = Integer.parseInt(destinationTextFieldX.getValue());
		int y = Integer.parseInt(destinationTextFieldY.getValue());
		int z = Integer.parseInt(destinationTextFieldZ.getValue());
		
		return new BlockPos(x, y, z);
	}
}