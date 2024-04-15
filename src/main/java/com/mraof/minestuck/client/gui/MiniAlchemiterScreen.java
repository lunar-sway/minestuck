package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.MiniAlchemiterMenu;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class MiniAlchemiterScreen extends MachineScreen<MiniAlchemiterMenu>
{
	
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/alchemiter.png");
	private static final ResourceLocation PROGRESS_BAR_TEXTURE = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/progress/alchemiter.png");
	private static final int PROGRESS_BAR_X = 54;
	private static final int PROGRESS_BAR_Y = 23;
	private static final int PROGRESS_BAR_WIDTH = 71;
	private static final int PROGRESS_BAR_HEIGHT = 10;
	private static final int BUTTON_X = 72;
	private static final int BUTTON_Y = 31;
	
	public MiniAlchemiterScreen(MiniAlchemiterMenu screenContainer, Inventory inv, Component titleIn)
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
		Objects.requireNonNull(minecraft);
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			ItemStack stack;
			if(!AlchemyHelper.hasDecodedItem(menu.getSlot(0).getItem()))
				stack = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
			else stack = AlchemyHelper.getDecodedItem(menu.getSlot(0).getItem());
			
			Optional<GristCostRecipe> recipe = GristCostRecipe.findRecipeForItem(stack, minecraft.level);
			GristSet set = recipe.map(recipe1 -> recipe1.getGristCost(stack, menu.getWildcardType(), false)).orElse(null);
			boolean useWildcard = recipe.map(GristCostRecipe::canPickWildcard).orElse(false);
			
			GuiUtil.drawGristBoard(guiGraphics, set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45, font);

			Component tooltip = GuiUtil.getGristboardTooltip(set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				guiGraphics.renderTooltip(font, tooltip, mouseX - this.leftPos, mouseY - this.topPos);

		}
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);
		
		int width = getScaledValue(menu.getProgress(), MiniAlchemiterBlockEntity.MAX_PROGRESS, PROGRESS_BAR_WIDTH);
		guiGraphics.blit(PROGRESS_BAR_TEXTURE, this.leftPos + PROGRESS_BAR_X, this.topPos + PROGRESS_BAR_Y,
				0, 0, width, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		goButton = addRenderableWidget(new GoButton(this.leftPos + BUTTON_X, this.topPos + BUTTON_Y, 30, 12, this.menu, true));
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		Objects.requireNonNull(minecraft);
		boolean b = super.mouseClicked(mouseX, mouseY, button);
		if (button == GLFW.GLFW_MOUSE_BUTTON_1 && menu.getCarried().isEmpty() && AlchemyHelper.getDecodedItem(menu.getSlot(0).getItem()).getItem() == MSItems.CAPTCHA_CARD.get()
				&& mouseX >= leftPos + 9 && mouseX < leftPos + 167 && mouseY >= topPos + 45 && mouseY < topPos + 70)
		{
			minecraft.pushGuiLayer(new GristSelectorScreen(this.getMenu().machinePos));
			return true;
		}
		return b;
	}
}