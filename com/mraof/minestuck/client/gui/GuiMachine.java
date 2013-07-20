package com.mraof.minestuck.client.gui;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMachine extends GuiContainer {
	
	private static final String[] guis = {"Minestuck:/gui/cruxtruder.png","Minestuck:/gui/designex.png","Minestuck:/gui/lathe.png","Minestuck:/gui/alchemiter.png"};
	private static final String[] guiTitles = {"Cruxtruder","Punch Desgnex","Totem Lathe","Alchemiter"};
	
	private ResourceLocation guiBackground;
	private int metadata;

    public GuiMachine (InventoryPlayer inventoryPlayer,
            TileEntityMachine tileEntity) {
    //the container is instanciated and passed to the superclass for handling
    super(new ContainerMachine(inventoryPlayer, tileEntity));
    this.metadata = tileEntity.getMetadata();
    guiBackground = new ResourceLocation(guis[metadata]);
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
    this.mc.func_110434_K().func_110577_a(guiBackground);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
}

}
