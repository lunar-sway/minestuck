package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.blockentity.machine.GristWidgetBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.GristWidgetMenu;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		Objects.requireNonNull(this.minecraft);
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			GristSet set = GristWidgetBlockEntity.getGristWidgetResult(menu.getSlot(0).getItem(), minecraft.level);

			GuiUtil.drawGristBoard(guiGraphics, set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, font);
			
			int cost = GristWidgetBlockEntity.getGristWidgetBoondollarValue(set);
			long has = ClientPlayerData.getBoondollars();
			String costText = GuiUtil.addSuffix(cost)+"£("+GuiUtil.addSuffix(has)+")";
			guiGraphics.drawString(font, costText, imageWidth - 9 - font.width(costText), imageHeight - 96 + 3, cost > has ? 0xFF0000 : 0x00FF00, false);
			
			Component tooltip = GuiUtil.getGristboardTooltip(set, GuiUtil.GristboardMode.GRIST_WIDGET, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				guiGraphics.renderTooltip(font, tooltip, mouseX - this.leftPos, mouseY - this.topPos);
			else if(mouseY - topPos >= imageHeight - 96 + 3 && mouseY - topPos < imageHeight - 96 + 3 + font.lineHeight)
			{
				if(!GuiUtil.addSuffix(cost).equals(String.valueOf(cost)) && mouseX - leftPos < imageWidth - 9 - font.width("£("+GuiUtil.addSuffix(has)+")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(costText))
					guiGraphics.renderTooltip(font, Component.literal(String.valueOf(cost)), mouseX - this.leftPos, mouseY - this.topPos);
				else if(!GuiUtil.addSuffix(has).equals(String.valueOf(has)) && mouseX - leftPos < imageWidth - 9 - font.width(")")
						&& mouseX - leftPos >= imageWidth - 9 - font.width(GuiUtil.addSuffix(has)+")"))
					guiGraphics.renderTooltip(font, Component.literal(String.valueOf(has)), mouseX - this.leftPos, mouseY - this.topPos);
			}
		}
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float par1, int par2, int par3)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		int width = getScaledValue(menu.getProgress(), GristWidgetBlockEntity.MAX_PROGRESS, PROGRESS_BAR_WIDTH);
		guiGraphics.blit(PROGRESS_BAR_TEXTURE, this.leftPos + PROGRESS_BAR_X, this.topPos + PROGRESS_BAR_Y,
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