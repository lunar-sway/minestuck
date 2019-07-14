package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.ComputerProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@OnlyIn(Dist.CLIENT)
public class GuiComputer extends Screen implements GuiButtonImpl.ButtonClickhandler
{

	public static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/bsod_message.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	public Button programButton;
	
	public Minecraft mc;
	public ComputerTileEntity te;


	public GuiComputer(Minecraft mc, ComputerTileEntity te)
	{
		super();
		
		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
		te.gui = this;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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
			fontRenderer.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		}
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepthTest();

		super.render(mouseX, mouseY, partialTicks);
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
		
		programButton = new GuiButtonImpl(this, 0, (width - xSize)/2 +95,(height - ySize)/2 +10,70,20, "");
		addButton(programButton);
		if(te.programSelected != -1)
			te.program.onInitGui(this, null);
		
		updateGui();
	}
	
	public void updateGui()
	{
		
		programButton.enabled = te.installedPrograms.size() > 1;
		
		if(te.hasProgram(-1)) {
			clearButtons();
			return;
		}
		
		if(te.program != null) {
			te.program.onUpdateGui(this);
			programButton.displayString = I18n.format(te.program.getName());
		}
		
	}
	
	@Override
	public void actionPerformed(GuiButtonImpl guibutton)
	{
		if(te.hasProgram(-1))
			return;
		
		if(guibutton == programButton) 
		{
			te.programSelected = getNextProgram();
			ComputerProgram program = te.program;
			te.program = ComputerProgram.getProgram(te.programSelected);
			te.program.onInitGui(this, program);
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
			Map.Entry<Integer, Boolean> pairs = it.next();
            int program = pairs.getKey();
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
	
	@Override
	public <T extends Widget> T addButton(T buttonIn)
	{
		return super.addButton(buttonIn);
	}
	
	public void clearButtons()
	{
		children.removeAll(buttons);
		buttons.clear();
	}
}
