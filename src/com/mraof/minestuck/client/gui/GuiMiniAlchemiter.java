package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityMiniAlchemiter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiMiniAlchemiter extends GuiMachine
{
	protected final TileEntityMiniAlchemiter te;
	protected final InventoryPlayer playerInventory;
	
	private ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/alchemiter.png");
	private ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/alchemiter.png");
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;

	public GuiMiniAlchemiter(InventoryPlayer inventoryPlayer, TileEntityMiniAlchemiter tileEntity)
	{
		super(new ContainerSburbMachine(inventoryPlayer, tileEntity), tileEntity);
		this.te = tileEntity;
		this.playerInventory = inventoryPlayer;
		
		//sets progress bar information
		progressX = 54;
		progressY = 23;
		progressWidth = 71;
		progressHeight = 10;
		goX = 72;
		goY = 31;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(te.getDisplayName().getFormattedText(), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96 + 2, 4210752);
		if (!te.getStackInSlot(0).isEmpty())
		{
			//Render grist requirements
			ItemStack stack = AlchemyRecipes.getDecodedItem(te.getStackInSlot(0));
			if (!(te.getStackInSlot(0).hasTag() && te.getStackInSlot(0).getTag().hasKey("contentID")))
				stack = new ItemStack(MinestuckBlocks.GENERIC_OBJECT);

			GristSet set = AlchemyCostRegistry.getGristConversion(stack);
			boolean useSelectedType = stack.getItem() == MinestuckItems.CAPTCHA_CARD;
			if (useSelectedType)
				set = new GristSet(te.selectedGrist, MinestuckConfig.clientCardCost);
			
			GuiUtil.drawGristBoard(set, useSelectedType ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45, fontRenderer);

			List<String> tooltip = GuiUtil.getGristboardTooltip(set, mouseX - this.guiLeft, mouseY - this.guiTop, 9, 45, fontRenderer);
			if (tooltip != null)
				this.drawHoveringText(tooltip, mouseX - this.guiLeft, mouseY - this.guiTop, fontRenderer);

		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		//draw background
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		//draw progress bar
		this.mc.getTextureManager().bindTexture(PROGRESS);
		int width = getScaledValue(te.progress, te.maxProgress, progressWidth);
		int height = progressHeight;
		drawModalRectWithCustomSizedTexture(x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}

	@Override
	public void initGui()
	{
		super.initGui();

			goButton = new GoButton(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
			buttons.add(goButton);
	}
	
	@Override
	public boolean mouseClicked(double par1, double par2, int par3)
	{
		boolean b = super.mouseClicked(par1, par2, par3);
		if (par3 == 0 && mc.player.inventory.getItemStack().isEmpty()
				&& te.getStackInSlot(0) != null && AlchemyRecipes.getDecodedItem(te.getStackInSlot(0)).getItem() == MinestuckItems.CAPTCHA_CARD
				&& par1 >= guiLeft + 9 && par1 < guiLeft + 167 && par2 >= guiTop + 45 && par2 < guiTop + 70)
		{
			mc.currentScreen = new GuiGristSelector(this);
			mc.currentScreen.setWorldAndResolution(mc, width, height);
			return true;
		}
		return b;
	}
}