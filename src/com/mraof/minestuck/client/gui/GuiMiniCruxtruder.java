package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.MiniCruxtruderContainer;
import com.mraof.minestuck.tileentity.MiniCruxtruderTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMiniCruxtruder extends GuiMachine
{
	protected final MiniCruxtruderTileEntity te;
	protected final InventoryPlayer playerInventory;
	
	private ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/cruxtruder.png");
	private ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/cruxtruder.png");
	//private EntityPlayer player;
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;

	public GuiMiniCruxtruder(InventoryPlayer inventoryPlayer, MiniCruxtruderTileEntity tileEntity)
	{
		super(new MiniCruxtruderContainer(inventoryPlayer, tileEntity), tileEntity);
		this.te = tileEntity;
		this.playerInventory = inventoryPlayer;
		
		//sets progress bar information
		progressX = 82;
		progressY = 42;
		progressWidth = 10;
		progressHeight = 13;
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
		int width = progressWidth;
		int height = getScaledValue(te.progress, te.maxProgress, progressHeight);
		drawModalRectWithCustomSizedTexture(x + progressX, y + progressY + progressHeight - height, 0, progressHeight - height, width, height, progressWidth, progressHeight);
	}
}