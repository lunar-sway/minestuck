package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.GristWidgetMenu;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.blockentity.machine.GristWidgetBlockEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GristWidgetScreen extends MachineScreen<GristWidgetMenu>
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/widget.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/widget.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public GristWidgetScreen(GristWidgetMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(GristWidgetBlockEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets prgress bar information
		progressX = 54;
		progressY = 23;
		progressWidth = 71;
		progressHeight = 10;
		goX = 72;
		goY = 31;
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
		font.draw(poseStack, this.title.getString(), 8, 6, 0xFFFFFF);
		//draws "Inventory" or your regional equivalent
		font.draw(poseStack, playerInventoryTitle.getString(), 8, imageHeight - 96 + 2, 0xFFFFFF);
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			GristSet set = GristWidgetBlockEntity.getGristWidgetResult(menu.getSlot(0).getItem(), minecraft.level);

			GuiUtil.drawGristBoard(poseStack, set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, font);
			
			int cost = GristWidgetBlockEntity.getGristWidgetBoondollarValue(set);
			long has = ClientPlayerData.getBoondollars();
			String costText = GuiUtil.addSuffix(cost)+"\u00a3("+GuiUtil.addSuffix(has)+")";
			font.draw(poseStack, costText, imageWidth - 9 - font.width(costText), imageHeight - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00);
			
			Component tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.GRIST_WIDGET, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				this.renderTooltip(poseStack, tooltip, mouseX - this.leftPos, mouseY - this.topPos);
			else if(mouseY - topPos >= imageHeight - 96 + 3 && mouseY - topPos < imageHeight - 96 + 3 + font.lineHeight)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - leftPos < imageWidth - 9 - font.width("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(costText))
					renderTooltip(poseStack, new TextComponent(String.valueOf(cost)), mouseX - this.leftPos, mouseY - this.topPos);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - leftPos < imageWidth - 9 - font.width(")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(GuiUtil.addSuffix(has)+")"))
					renderTooltip(poseStack, new TextComponent(String.valueOf(has)), mouseX - this.leftPos, mouseY - this.topPos);
			}
		}
	}

	@Override
	protected void renderBg(PoseStack poseStack, float par1, int par2, int par3)
	{
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		//draw background
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

		//draw progress bar
		RenderSystem.setShaderTexture(0, PROGRESS);
		int width = getScaledValue(menu.getProgress(), GristWidgetBlockEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		int height = progressHeight;
		blit(poseStack, x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, new TextComponent(menu.overrideStop() ? "STOP" : "GO"));
		addRenderableWidget(goButton);
		if(MinestuckConfig.SERVER.disableGristWidget.get())
			goButton.active = false;
	}
}