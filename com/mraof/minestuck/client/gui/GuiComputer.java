package com.mraof.minestuck.client.gui;


import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.SburbConnection;

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
	private String displayLine = "";
	private String programName = "";
	private int program;
	private String displayName;

	public Minecraft mc;
	private TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();

		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
		te.gui = this;
		
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
        	GuiButton button = new GuiButton(i, (width - xSize) / 2 +14, (height - ySize) / 2 +60 + i*24, 120, 20,"");
        	selButtons.add(button);
        	buttonList.add(button);
        }
        
        upButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +60, 20, 20,"^");
        buttonList.add(upButton);
        downButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +132, 20, 20,"v");
        buttonList.add(downButton);
        
    	updateGui();
	}

	public void updateGui() {
		
		displayName = "";
		//Debug.print("Conn'd to "+te.connectedTo);
		String[] parts = te.connectedTo.split("\0");
		for (String part : parts) {
			displayName += part;
		}
		
    	upButton.enabled = false;
    	downButton.enabled = false;
    	
    	for (Object button : selButtons) {
    		((GuiButton)button).enabled = false;
    		((GuiButton)button).displayString = "";
    	}
    	
		switch(program) {
		case(0):
			if (!te.connectedTo.equals("")) {
				this.displayLine = "Connected to "+displayName+".";		    	
			} else {
				this.displayLine = "Select a server below.";
		    	
		    	int i = 0;
		    	for (Object server : SburbConnection.getServersOpen()) {
		    		if (i < selButtons.size() && selButtons.get(i) != null) {
		    			selButtons.get(i).displayString = (String)server;
		    			selButtons.get(i).enabled = true;
		    			i++;
		    		} else {
		    			downButton.enabled = true;
		    		}
		    	}
			}
	    	break;
		case(1):
			if (te.givenItems) {
					displayLine = "Connected to "+displayName+".";
			} else if (!te.connectedTo.equals("")) {
				GuiButton button = selButtons.get(0);
				displayLine = "Connected to "+displayName+".";
				button.displayString = "Give client items";
				button.enabled = true;
			} else if (!te.connectedFrom.equals("") && te.connectedTo.equals("")) {
					GuiButton button = selButtons.get(0);
					button.displayString = "";
					button.enabled = false;
					displayLine = "Waiting for client...";
			} else {
				this.displayLine = "Click the button below.";
			
		      	for (Object button : selButtons) {
		    		if (((GuiButton)button).id == 0) {
		    			((GuiButton)button).enabled = true;
		    			((GuiButton)button).displayString = "Open to clients";
		    		}
		    	}
			}
	    	break;
		}
	}

	protected void actionPerformed(GuiButton guibutton) {
		switch(program) {
		case(0):
			if (guibutton == upButton) {
				
			} else if (guibutton == downButton) {
				
			} else {

				te.connectedTo = guibutton.displayString;
				te.connectedFrom = mc.thePlayer.username;
				
				te.updateConnection();
			}
			break;
		case(1):
			if (!te.connectedTo.equals("")) {
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE,te.xCoord,te.yCoord,te.zCoord,te.connectedTo);
				packet.length = packet.data.length;
				this.mc.getNetHandler().addToSendQueue(packet);
				te.givenItems = true;
				te.updateConnection();
			} else {
				te.connectedFrom  = mc.thePlayer.username;
				te.updateConnection();
			}
			break;
		}
		updateGui();
	}
		
}
