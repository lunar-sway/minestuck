package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.AnthvilMenu;
import com.mraof.minestuck.network.block.TriggerAnthvilPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnthvilScreen extends AbstractContainerScreen<AnthvilMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/anthvil.png");
	private static final ResourceLocation FUEL_STATUS = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/uranium_level.png");
	
	private static final int FUEL_STATUS_X = 133;
	private static final int FUEL_STATUS_Y = 7;
	private static final int STATUS_WIDTH = 35;
	private static final int STATUS_HEIGHT = 39;
	private static final int BUTTON_X = 12;
	private static final int BUTTON_Y = 38;
	
	private final AnthvilMenu screenContainer;
	
	AnthvilScreen(AnthvilMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
		
		this.screenContainer = screenContainer;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		addRenderableWidget(new ExtendedButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, Component.literal("MEND"), button -> mend()));
		addRenderableWidget(new ExtendedButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y + 16, 30, 12, Component.literal("DONE"), button -> finish()));
	}
	
	private void mend()
	{
		//sends a request to mend and refuel uranium
		PacketDistributor.SERVER.noArg().send(new TriggerAnthvilPacket());
	}
	
	private void finish()
	{
		onClose();
	}
	
	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);
		
		if(minecraft != null && minecraft.level != null)
		{
			ItemStack stack = screenContainer.getSlot(0).getItem();
			GristSet fullSet = GristCostRecipe.findCostForItem(stack, null, false, minecraft.level);
			if(fullSet != null && !fullSet.isEmpty())
			{
				GristAmount pickedGrist = AnthvilBlockEntity.getUsedGrist(fullSet);
				
				GuiUtil.drawGristBoard(graphics, pickedGrist, GuiUtil.GristboardMode.ALCHEMITER, (width - this.leftPos) / 2 - 4, (height - this.topPos) / 2 - 48, font, 2);
				//draw the grist
				Component tooltip = GuiUtil.getGristboardTooltip(pickedGrist, GuiUtil.GristboardMode.ALCHEMITER, mouseX, mouseY, (width - this.leftPos) / 2 - 4, (height - this.topPos) / 2 - 48, font, 2);
				if(tooltip != null)
					graphics.renderTooltip(font, tooltip, mouseX, mouseY);
			}
		}
	}
	
	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY)
	{
		//draws the name of the BE
		graphics.drawString(font, this.title, 8, 6, 0x404040, false);
		
		//draws "Inventory" or your regional equivalent
		graphics.drawString(font, this.playerInventoryTitle, 8, imageHeight - 96 + 2, 0x404040, false);
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float par1, int par2, int par3)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		//draw background
		graphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		//draw fuel bar
		int fuelHeight = MachineScreen.getScaledValue(menu.getFuel(), AnthvilBlockEntity.MAX_FUEL, STATUS_HEIGHT);
		graphics.blit(FUEL_STATUS, this.leftPos + FUEL_STATUS_X, this.topPos + FUEL_STATUS_Y + STATUS_HEIGHT - fuelHeight,
				0, STATUS_HEIGHT - fuelHeight, STATUS_WIDTH, fuelHeight, STATUS_WIDTH, STATUS_HEIGHT);
	}
}
