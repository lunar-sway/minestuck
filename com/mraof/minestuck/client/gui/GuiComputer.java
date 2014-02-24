package com.mraof.minestuck.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.ClearMessagePacket;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiComputer extends GuiScreen
{

	public static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/Sburb.png");
	public static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/BsodMessage.png");
	
	public static final int xSize = 176;
	public static final int ySize = 166;
	
	private GuiButton programButton;
	
	/**
	 * Contains the usernames that possibly is displayed on the shown buttons.
	 * Used by the client program when connecting to a server.
	 */
	private final String[] usernameList = new String[4];
	private String displayMessage = "";
	private int index = 0;
	/**
	 * Used to count which four button strings that will be added.
	 */
	private int stringIndex;

	public Minecraft mc;
	public TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();
		
		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
		te.gui = this;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		
		this.drawDefaultBackground();
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(!te.errored()?guiBackground:guiBsod);
		
		int yOffset = (this.height / 2) - (ySize / 2);
		this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		if(!te.errored())
			if(te.latestmessage.get(te.programSelected) == null || te.latestmessage.get(te.programSelected).isEmpty())
				if(te.programSelected == -1){
					fontRenderer.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
				} else fontRenderer.drawString(displayMessage, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
			else fontRenderer.drawString(StatCollector.translateToLocal(te.latestmessage.get(te.programSelected)), (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
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
		
		int id = -1;
		if(te.programSelected == -1 && !te.errored())
			for(Entry<Integer, Boolean> entry : te.installedPrograms.entrySet())
				if(entry.getValue() && (id == -1 || id > entry.getKey()))
						id = entry.getKey();
		
		if(te.programSelected != -1 && (te.program == null || te.program.getId() != te.programSelected))
			te.program = ComputerProgram.getProgram(te.programSelected);
		
		programButton = new GuiButton(0, (width - xSize)/2 +95,(height - ySize)/2 +10,70,20, "PROGRAM");
		buttonList.add(programButton);
		if(te.programSelected != -1)
			te.program.onInitGui(this, buttonList, null);
		
		updateGui();
	}

	public void updateGui() {
		
		programButton.enabled = te.installedPrograms.size() > 1;
		
		if(te.errored()) {
			buttonList.clear();
			return;
		}
		
		if(te.program != null)
			te.program.onUpdateGui(this, buttonList);
		
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		if(te.errored())
			return;
		
		if(guibutton == programButton) {
			te.programSelected = getNextProgram();
			initGui();
		} else te.program.onButtonPressed(te, guibutton);
		
		updateGui();
	}

	private int getNextProgram() {
	   	if (te.installedPrograms.size() == 1) {
	   		return te.programSelected;
	   	}
   	   	Iterator it = te.installedPrograms.entrySet().iterator();
	   	int place = 0;
	   	boolean found = false;
	   	int lastProgram = te.programSelected;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            int program = (Integer) pairs.getKey();
            if (found) {
            	return program;
            } else if (program==te.programSelected) {
            	found = true;
            } else {
            	lastProgram = program;
            }
            place++;
        }
		return lastProgram;
	}
}
