package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import com.mraof.minestuck.inventory.AnthvilMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnthvilScreen extends MachineScreen<AnthvilMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/anthvil.png");
	private static final ResourceLocation FUEL_STATUS = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/uranium_level.png");
	private static final ResourceLocation CRUXITE_STATUS = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/cruxite_level.png");
	
	private static final int FUEL_STATUS_X = 134;
	private static final int FUEL_STATUS_Y = 7;
	private static final int CRUXITE_STATUS_X = 85;
	private static final int CRUXITE_STATUS_Y = 7;
	private static final int STATUS_WIDTH = 35;
	private static final int STATUS_HEIGHT = 39;
	private static final int BUTTON_X = 20;
	private static final int BUTTON_Y = 30;
	
	private ExtendedButton goButton;
	
	
	AnthvilScreen(AnthvilMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		//activates processContents() in AnthvilBlockEntity
		goButton = new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, true);
		addRenderableWidget(goButton);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		goButton.active = true;
		
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
		
		//draw fuel bar
		RenderSystem.setShaderTexture(0, FUEL_STATUS);
		int fuelHeight = getScaledValue(menu.getFuel(), AnthvilBlockEntity.MAX_FUEL, STATUS_HEIGHT);
		blit(poseStack, this.leftPos + FUEL_STATUS_X, this.topPos + FUEL_STATUS_Y + STATUS_HEIGHT - fuelHeight,
				0, STATUS_HEIGHT - fuelHeight, STATUS_WIDTH, fuelHeight, STATUS_WIDTH, STATUS_HEIGHT);
		
		//draw cruxite bar
		RenderSystem.setShaderTexture(0, CRUXITE_STATUS);
		int cruxiteHeight = getScaledValue(menu.getCruxite(), AnthvilBlockEntity.MAX_CRUXITE, STATUS_HEIGHT);
		blit(poseStack, this.leftPos + CRUXITE_STATUS_X, this.topPos + CRUXITE_STATUS_Y + STATUS_HEIGHT - cruxiteHeight,
				0, STATUS_HEIGHT - cruxiteHeight, STATUS_WIDTH, cruxiteHeight, STATUS_WIDTH, STATUS_HEIGHT);
	}
}