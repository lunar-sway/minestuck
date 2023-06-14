package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.GristWidgetMenu;
import com.mraof.minestuck.blockentity.machine.GristWidgetBlockEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GristWidgetScreen extends MachineScreen<GristWidgetMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/widget.png");
	private static final ResourceLocation PROGRESS_BAR_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/widget.png");
	private static final int PROGRESS_BAR_X = 54;
	private static final int PROGRESS_BAR_Y = 23;
	private static final int PROGRESS_BAR_WIDTH = 71;
	private static final int PROGRESS_BAR_HEIGHT = 10;
	private static final int BUTTON_X = 72;
	private static final int BUTTON_Y = 31;
	
	public GristWidgetScreen(GristWidgetMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY)
	{
		Objects.requireNonNull(this.minecraft);
		super.renderLabels(poseStack, mouseX, mouseY);
		
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			GristSet set = GristWidgetBlockEntity.getGristWidgetResult(menu.getSlot(0).getItem(), minecraft.level);

			GuiUtil.drawGristBoard(poseStack, set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, font);
			
			int cost = GristWidgetBlockEntity.getGristWidgetBoondollarValue(set);
			long has = ClientPlayerData.getBoondollars();
			String costText = GuiUtil.addSuffix(cost)+"£("+GuiUtil.addSuffix(has)+")";
			font.draw(poseStack, costText, imageWidth - 9 - font.width(costText), imageHeight - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00);
			
			Component tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.GRIST_WIDGET, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				this.renderTooltip(poseStack, tooltip, mouseX - this.leftPos, mouseY - this.topPos);
			else if(mouseY - topPos >= imageHeight - 96 + 3 && mouseY - topPos < imageHeight - 96 + 3 + font.lineHeight)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - leftPos < imageWidth - 9 - font.width("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(costText))
					renderTooltip(poseStack, Component.literal(String.valueOf(cost)), mouseX - this.leftPos, mouseY - this.topPos);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - leftPos < imageWidth - 9 - font.width(")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(GuiUtil.addSuffix(has)+")"))
					renderTooltip(poseStack, Component.literal(String.valueOf(has)), mouseX - this.leftPos, mouseY - this.topPos);
			}
		}
	}

	@Override
	protected void renderBg(PoseStack poseStack, float par1, int par2, int par3)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		RenderSystem.setShaderTexture(0, PROGRESS_BAR_TEXTURE);
		int width = getScaledValue(menu.getProgress(), GristWidgetBlockEntity.MAX_PROGRESS, PROGRESS_BAR_WIDTH);
		blit(poseStack, this.leftPos + PROGRESS_BAR_X, this.topPos + PROGRESS_BAR_Y,
				0, 0, width, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = addRenderableWidget(new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, true));
		if(MinestuckConfig.SERVER.disableGristWidget.get())
			goButton.active = false;
	}
}