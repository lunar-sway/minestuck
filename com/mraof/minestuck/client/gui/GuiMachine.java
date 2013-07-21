package com.mraof.minestuck.client.gui;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMachine extends GuiContainer {
	
	private static final String[] guis = {"cruxtruder","designex","lathe","alchemiter"};
	private static final String[] guiTitles = {"Cruxtruder","Punch Designex","Totem Lathe","Alchemiter"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private int metadata;
	private TileEntityMachine te;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;

    public GuiMachine (InventoryPlayer inventoryPlayer,
            TileEntityMachine tileEntity) {
    super(new ContainerMachine(inventoryPlayer, tileEntity));
    this.te = tileEntity;
    this.metadata = tileEntity.getMetadata();
    guiBackground = new ResourceLocation("Minestuck:/gui/" + guis[metadata] + ".png");
    guiProgress = new ResourceLocation("Minestuck:/gui/progress/" + guis[metadata] + ".png");
    
    //sets prgress bar information based on machine type
    switch (metadata) {
    case (0):
    	progressX = 82;
    	progressY = 42;
    	progressWidth = 10;
    	progressHeight = 13;
    	break;
    case (1):
    	progressX = 81;
    	progressY = 38;
    	progressWidth = 43;
    	progressHeight = 17;
    	break;
    case (2):
    	progressX = 69;
    	progressY = 33;
    	progressWidth = 44;
    	progressHeight = 17;
    	break;
    case (3):
    	progressX = 54;
    	progressY = 23;
    	progressWidth = 71;
    	progressHeight = 10;
    	break;
    }
}

@Override
protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    fontRenderer.drawString(guiTitles[metadata], 8, 6, 4210752);
    //draws "Inventory" or your regional equivalent
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
}

@Override
protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //int texture = mc.renderEngine.getTexture("/gui/cruxtruder.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //this.mc.renderEngine.bindTexture(texture);
    
    //draw background
    this.mc.func_110434_K().func_110577_a(guiBackground);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
    //draw progress bar
    this.mc.func_110434_K().func_110577_a(guiProgress);
    int width = metadata == 0 ? progressWidth : getScaledValue(te.progress,te.maxProgress,progressWidth);
    int height = metadata != 0 ? progressHeight : getScaledValue(te.progress,te.maxProgress,progressHeight);
    this.drawCustomBox(x+progressX, y+progressY, 0, 0, width, height,progressWidth,progressHeight);
}

/*
 * Draws a box like drawModalRect, but with custom width and height values.
 */
public void drawCustomBox(int par1, int par2, int par3, int par4, int par5, int par6, int width, int height)
{
    float f = 1/(float)width;
    float f1 = 1/(float)height;
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
    tessellator.draw();
}

public int getScaledValue(int progress,int max,int imageMax) {
	return (int) ((float) imageMax*((float)progress/(float)max));
}

}
