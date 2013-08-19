package com.mraof.minestuck.client.gui;


import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.GristType;
import com.mraof.minestuck.util.Title;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiComputer extends GuiScreen
{

    private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/Sburb.png");
    
	private static final int xSize = 176;
	private static final int ySize = 166;
	
	private GuiButton upButton;
	private GuiButton downButton;
	
	private ArrayList<GuiButton> selButtons = new ArrayList<GuiButton>();
	private String displayLine = "Starting Up...";
	private String programName = "";
	private int program;

	private Minecraft mc;

	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();

		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		
		this.program = te.program;
		switch (program) {
		case(0):
			this.programName = "Client";
			break;
		case(1):
			this.programName = "Server";
			break;
		}
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{

		this.drawDefaultBackground();
		
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(guiBackground);
		
		int yOffset = (this.height / 2) - (ySize / 2);
		this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		
		 fontRenderer.drawString(programName, (width - xSize) / 2 +124, (height - ySize) / 2 +24, 4210752);
		 fontRenderer.drawString(displayLine, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		super.drawScreen(xcor, ycor, par3);
	}

	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	public void initGui() {
        super.initGui();
        for (int i=0;i<4;i++) {
        	GuiButton button = new GuiButton(i, (width - xSize) / 2 +14, (height - ySize) / 2 +60 + i*24, 120, 20,"Option "+i);
        	selButtons.add(button);
        	buttonList.add(button);
        }
        upButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +60, 20, 20,"^");
        buttonList.add(upButton);
        downButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +132, 20, 20,"v");
        buttonList.add(downButton);
	}

}
