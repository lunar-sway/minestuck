package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.MiniAlchemiterContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.tileentity.machine.MiniAlchemiterTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Optional;

public class MiniAlchemiterScreen extends MachineScreen<MiniAlchemiterContainer> implements Positioned
{
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
	private static final ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/alchemiter.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	
	public MiniAlchemiterScreen(MiniAlchemiterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(MiniAlchemiterTileEntity.TYPE, screenContainer, inv, titleIn);
		
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
		return getContainer().machinePos;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY)
	{
		font.drawString(matrixStack, this.title.getString(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		font.drawString(matrixStack, playerInventory.getDisplayName().getString(), 8, ySize - 96 + 2, 4210752);
		if (container.getSlot(0).getHasStack())
		{
			//Render grist requirements
			ItemStack stack;
			if(!AlchemyHelper.hasDecodedItem(container.getSlot(0).getStack()))
				stack = new ItemStack(MSBlocks.GENERIC_OBJECT);
			else stack = AlchemyHelper.getDecodedItem(container.getSlot(0).getStack());
			
			Optional<GristCostRecipe> recipe = GristCostRecipe.findRecipeForItem(stack, minecraft.world);
			GristSet set = recipe.map(recipe1 -> recipe1.getGristCost(stack, container.getWildcardType(), false, minecraft.world)).orElse(null);
			boolean useWildcard = recipe.map(GristCostRecipe::canPickWildcard).orElse(false);
			
			GuiUtil.drawGristBoard(matrixStack, set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45, font);

			ITextComponent tooltip = GuiUtil.getGristboardTooltip(set, useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, mouseX - this.guiLeft, mouseY - this.guiTop, 9, 45, font);
			if(tooltip != null)
				this.renderTooltip(matrixStack, tooltip, mouseX - this.guiLeft, mouseY - this.guiTop);

		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float par1, int par2, int par3)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.minecraft.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.blit(matrixStack, x, y, 0, 0, xSize, ySize);

		//draw progress bar
		this.minecraft.getTextureManager().bindTexture(PROGRESS);
		int width = getScaledValue(container.getProgress(), MiniAlchemiterTileEntity.DEFAULT_MAX_PROGRESS, progressWidth);
		int height = progressHeight;
		blit(matrixStack, x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	protected void init()
	{
		super.init();
		
		goButton = new GoButton((width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, new StringTextComponent(container.overrideStop() ? "STOP" : "GO"));
		addButton(goButton);
	}
	
	@Override
	public boolean mouseClicked(double par1, double par2, int par3)
	{
		boolean b = super.mouseClicked(par1, par2, par3);
		if (par3 == 0 && minecraft.player.inventory.getItemStack().isEmpty() && AlchemyHelper.getDecodedItem(container.getSlot(0).getStack()).getItem() == MSItems.CAPTCHA_CARD
				&& par1 >= guiLeft + 9 && par1 < guiLeft + 167 && par2 >= guiTop + 45 && par2 < guiTop + 70)
		{
			minecraft.currentScreen = new GristSelectorScreen<>(this);
			minecraft.currentScreen.init(minecraft, width, height);
			return true;
		}
		return b;
	}
}