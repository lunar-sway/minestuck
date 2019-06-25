package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ContainerMiniPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntityMiniPunchDesignix;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMiniPunchDesignix extends GuiMachine
{
	protected final TileEntityMiniPunchDesignix te;
	protected final InventoryPlayer playerInventory;
	
	private ResourceLocation BACKGROUND = new ResourceLocation("minestuck:textures/gui/designix.png");
	private ResourceLocation PROGRESS = new ResourceLocation("minestuck:textures/gui/progress/designix.png");
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;

	public GuiMiniPunchDesignix(InventoryPlayer inventoryPlayer, TileEntityMiniPunchDesignix tileEntity)
	{
		super(new ContainerMiniPunchDesignix(inventoryPlayer, tileEntity), tileEntity);
		this.te = tileEntity;
		this.playerInventory = inventoryPlayer;
		
		//sets progress bar information
		progressX = 63;
		progressY = 38;
		progressWidth = 43;
		progressHeight = 17;
		goX = 66;
		goY = 55;
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
		int width = getScaledValue(te.progress, te.maxProgress, progressWidth);
		int height = progressHeight;
		drawModalRectWithCustomSizedTexture(x + progressX, y + progressY, 0, 0, width, height, progressWidth, progressHeight);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		
		goButton = new GoButton(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
		addButton(goButton);
	}
}