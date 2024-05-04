package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.UraniumCookerBlockEntity;
import com.mraof.minestuck.inventory.UraniumCookerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class UraniumCookerScreen extends MachineScreen<UraniumCookerMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/uranium_cooker.png");
	private static final ResourceLocation FUEL_BAR_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/uranium_level.png");
	private static final int FUEL_BAR_X = 67;
	private static final int FUEL_BAR_Y = 24;
	private static final int FUEL_BAR_WIDTH = 35;
	private static final int FUEL_BAR_HEIGHT = 39;
	private static final int BUTTON_X = 69;
	private static final int BUTTON_Y = 69;
	
	public UraniumCookerScreen(UraniumCookerMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float par1, int par2, int par3)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		int height = getScaledValue(menu.getFuel(), UraniumCookerBlockEntity.MAX_FUEL, FUEL_BAR_HEIGHT);
		guiGraphics.blit(FUEL_BAR_TEXTURE, this.leftPos + FUEL_BAR_X, this.topPos + FUEL_BAR_Y + FUEL_BAR_HEIGHT - height,
				0, FUEL_BAR_HEIGHT - height, FUEL_BAR_WIDTH, height, FUEL_BAR_WIDTH, FUEL_BAR_HEIGHT);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = addRenderableWidget(new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, true));
	}
}