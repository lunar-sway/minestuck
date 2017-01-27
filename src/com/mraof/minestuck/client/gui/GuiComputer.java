package com.mraof.minestuck.client.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;

@SideOnly(Side.CLIENT)
public class GuiComputer extends GuiScreen
{

	public static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	public GuiButton programButton;
	
	public Minecraft mc;
	public TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();
		
		this.mc = mc;
		this.fontRendererObj = mc.fontRendererObj;
		this.te = te;
		te.gui = this;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		
		this.drawDefaultBackground();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if(te.hasProgram(-1)) {
			this.mc.getTextureManager().bindTexture(guiBsod);
			int yOffset = (this.height / 2) - (ySize / 2);
			this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		} else if(te.program != null)
			te.program.paintGui(this, te);
		else {
			this.mc.getTextureManager().bindTexture(guiBackground);
			int yOffset = (this.height / 2) - (ySize / 2);
			this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
			fontRendererObj.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		}
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

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
		
		if(te.programSelected == -1 && !te.hasProgram(-1))
			for(Entry<Integer, Boolean> entry : te.installedPrograms.entrySet())
				if(entry.getValue() && (te.programSelected == -1 || te.programSelected > entry.getKey()))
						te.programSelected = entry.getKey();
		
		if(te.programSelected != -1 && (te.program == null || te.program.getId() != te.programSelected))
			te.program = ComputerProgram.getProgram(te.programSelected);
		
		programButton = new GuiButton(0, (width - xSize)/2 +95,(height - ySize)/2 +10,70,20, "");
		buttonList.add(programButton);
		if(te.programSelected != -1)
			te.program.onInitGui(this, buttonList, null);
		
		updateGui();
	}
	
	public void updateGui()
	{
		
		programButton.enabled = te.installedPrograms.size() > 1;
		
		if(te.hasProgram(-1)) {
			buttonList.clear();
			return;
		}
		
		if(te.program != null) {
			te.program.onUpdateGui(this, buttonList);
			programButton.displayString = I18n.format(te.program.getName());
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if(te.hasProgram(-1))
			return;
		
		if(guibutton == programButton) 
		{
			te.programSelected = getNextProgram();
			ComputerProgram program = te.program;
			te.program = ComputerProgram.getProgram(te.programSelected);
			te.program.onInitGui(this, buttonList, program);
		}
		else
			te.program.onButtonPressed(te, guibutton);
		updateGui();
	}

	private int getNextProgram() {
	   	if (te.installedPrograms.size() == 1) {
	   		return te.programSelected;
	   	}
		Iterator<Entry<Integer, Boolean>> it = te.installedPrograms.entrySet().iterator();
		//int place = 0;
	   	boolean found = false;
	   	int lastProgram = te.programSelected;
        while (it.hasNext()) {
			Map.Entry<Integer, Boolean> pairs = (Entry<Integer, Boolean>) it
					.next();
            int program = (Integer) pairs.getKey();
            if (found) {
            	return program;
            } else if (program==te.programSelected) {
            	found = true;
            } else {
            	lastProgram = program;
            }
            //place++;
        }
		return lastProgram;
	}
}
