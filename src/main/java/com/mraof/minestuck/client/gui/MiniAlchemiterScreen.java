package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.MiniAlchemiterMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristCostRecipe;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.blockentity.machine.MiniAlchemiterBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class MiniAlchemiterScreen extends MachineScreen<MiniAlchemiterMenu> implements Positioned
{
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/alchemiter.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public MiniAlchemiterScreen(MiniAlchemiterMenu screenContainer, Inventory inv, Component titleIn)
	{
		super(MiniAlchemiterBlockEntity.TYPE, screenContainer, inv, titleIn);
		
		//sets progress bar information
		progressX = 54;
		progressY = 23;
		progressWidth = 71;
		progressHeight = 10;
		goX = 72;
		goY = 31;
	}
	
	@Override
	public BlockPos getPosition()
	{
		return getMenu().machinePos;
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
		super.renderLabels(poseStack, mouseX, mouseY);
		
		if (menu.getSlot(0).hasItem())
		{
			//Render grist requirements
			ItemStack stack;
			if(!AlchemyHelper.hasDecodedItem(menu.getSlot(0).getItem()))
				stack = new ItemStack(MSBlocks.GENERIC_OBJECT.get());
			else stack = AlchemyHelper.getDecodedItem(menu.getSlot(0).getItem());
			
			Optional<GristCostRecipe> recipe = GristCostRecipe.findRecipeForItem(stack, minecraft.level);
			GristSet set = recipe.map(recipe1 -> recipe1.getGristCost(stack, menu.getWildcardType(), false, minecraft.level)).orElse(null);
			boolean useWildcard = recipe.map(GristCostRecipe::canPickWildcard).orElse(false);
			
			GuiUtil.drawGristBoard(poseStack, set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45, font);

			Component tooltip = GuiUtil.getGristboardTooltip(set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, mouseX - this.leftPos, mouseY - this.topPos, 9, 45, font);
			if(tooltip != null)
				this.renderTooltip(poseStack, tooltip, mouseX - this.leftPos, mouseY - this.topPos);

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
		RenderSystem.setShaderTexture(0,PROGRESS);
		int width = getScaledValue(menu.getProgress(), MiniAlchemiterBlockEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		int height = progressHeight;
		blit(poseStack, x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		goButton = new GoButton((width - imageWidth) / 2 + goX, (height - imageHeight) / 2 + goY, 30, 12, new TextComponent(menu.overrideStop() ? "STOP" : "GO"));
		addRenderableWidget(goButton);
	}
	
	@Override
	public boolean mouseClicked(double par1, double par2, int par3)
	{
		boolean b = super.mouseClicked(par1, par2, par3);
		if (par3 == 0 && menu.getCarried().isEmpty() && AlchemyHelper.getDecodedItem(menu.getSlot(0).getItem()).getItem() == MSItems.CAPTCHA_CARD.get()
				&& par1 >= leftPos + 9 && par1 < leftPos + 167 && par2 >= topPos + 45 && par2 < topPos + 70)
		{
			minecraft.screen = new GristSelectorScreen<>(this);
			minecraft.screen.init(minecraft, width, height);
			return true;
		}
		return b;
	}
}