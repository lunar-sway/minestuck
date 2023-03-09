package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import com.mraof.minestuck.inventory.SendificatorMenu;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SendificatorPacket;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import javax.annotation.Nullable;

public class SendificatorScreen extends MachineScreen<SendificatorMenu>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/sendificator.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/uranium_level.png");
	
	private final int progressX;
	private final int progressY;
	private final int progressWidth;
	private final int progressHeight;
	private final int goX;
	private final int goY;
	
	private EditBox destinationTextFieldX;
	private EditBox destinationTextFieldY;
	private EditBox destinationTextFieldZ;
	private ExtendedButton updateButton;
	private ExtendedButton goButton;
	@Nullable
	private BlockPos parsedPos;
	
	
	SendificatorScreen(SendificatorMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(MachineProcessBlockEntity.RunType.BUTTON, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 67 - 15;
		progressY = 24;
		progressWidth = 35;
		progressHeight = 39;
		goX = 115;
		goY = 60;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		int yOffset = (height - imageHeight) / 2;
		
		updateButton = addRenderableWidget(new ExtendedButton((width - imageWidth) / 2 + 105, yOffset + 40, 50, 12, Component.literal("Update"), button -> updateDestinationPos()));
		
		this.destinationTextFieldX = new EditBox(this.font, this.width / 2 - 10, yOffset + 10, 35, 15, Component.literal("X value of destination block pos")); //TODO make these translatable
		destinationTextFieldX.setMaxLength(10);
		addRenderableWidget(destinationTextFieldX);
		destinationTextFieldX.setResponder(s -> onTextFieldChange());
		
		this.destinationTextFieldY = new EditBox(this.font, this.width / 2 + 25, yOffset + 10, 20, 15, Component.literal("Y value of destination block pos"));
		destinationTextFieldY.setMaxLength(3);
		addRenderableWidget(destinationTextFieldY);
		destinationTextFieldY.setResponder(s -> onTextFieldChange());
		
		this.destinationTextFieldZ = new EditBox(this.font, this.width / 2 + 45, yOffset + 10, 35, 15, Component.literal("Z value of destination block pos"));
		destinationTextFieldZ.setMaxLength(10);
		addRenderableWidget(destinationTextFieldZ);
		destinationTextFieldZ.setResponder(s -> onTextFieldChange());
		
		//activates processContents() in SendificatorBlockEntity
		goButton = new GoButton((width - imageWidth) / 2 + goX, yOffset + goY, 30, 12, Component.literal(menu.overrideStop() ? "STOP" : "GO"));
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
		//draws the name of the TE
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
		RenderSystem.setShaderTexture(0, BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
		
		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS);
		int width = progressWidth;
		int height = getScaledValue(menu.getFuel(), SendificatorBlockEntity.MAX_FUEL, progressHeight);
		blit(poseStack, x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
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