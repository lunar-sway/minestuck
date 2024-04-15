package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.machine.MiniPunchDesignixBlockEntity;
import com.mraof.minestuck.inventory.MiniPunchDesignixMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MiniPunchDesignixScreen extends MachineScreen<MiniPunchDesignixMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minestuck:textures/gui/designix.png");
	private static final ResourceLocation PROGRESS_BAR_TEXTURE = new ResourceLocation("minestuck:textures/gui/progress/designix.png");
	private static final int PROGRESS_BAR_X = 63;
	private static final int PROGRESS_BAR_Y = 38;
	private static final int PROGRESS_BAR_WIDTH = 43;
	private static final int PROGRESS_BAR_HEIGHT = 17;
	private static final int BUTTON_X = 66;
	private static final int BUTTON_Y = 55;
	
	public MiniPunchDesignixScreen(MiniPunchDesignixMenu screenContainer, Inventory inv, Component titleIn)
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

		int width = getScaledValue(menu.getProgress(), MiniPunchDesignixBlockEntity.MAX_PROGRESS, PROGRESS_BAR_WIDTH);
		guiGraphics.blit(PROGRESS_BAR_TEXTURE, this.leftPos + PROGRESS_BAR_X, this.topPos + PROGRESS_BAR_Y,
				0, 0, width, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}

	@Override
	public void init()
	{
		super.init();
		
		goButton = addRenderableWidget(new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, false));
	}
}