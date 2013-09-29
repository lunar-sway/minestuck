package com.mraof.minestuck.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
	private GuiButton programButton;
	
	private ArrayList<GuiButton> selButtons = new ArrayList<GuiButton>();
	private ArrayList<String> buttonStrings;
	private String displayPlayer = "";
	private String displayMessage = "";
	private int index = 0;

	public Minecraft mc;
	private TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();
		
		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
		te.gui = this;
		buttonStrings = new ArrayList<String>();
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{
		
		this.drawDefaultBackground();
		
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiBackground);
		
		int yOffset = (this.height / 2) - (ySize / 2);
		this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		if(te.latestmessage.isEmpty())
			if(te.programSelected == -1){
				fontRenderer.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
			} else fontRenderer.drawString(displayMessage, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		else fontRenderer.drawString(te.latestmessage, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
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
			GuiButton button = new GuiButton(i+2, (width - xSize) / 2 +14, (height - ySize) / 2 +60 + i*24, 120, 20,"");
			selButtons.add(button);
			buttonList.add(button);
		}
		
		programButton = new GuiButton(0, (width - xSize)/2 +95,(height - ySize)/2 +10,70,20, "PROGRAM");
		buttonList.add(programButton);
		
		upButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +60, 20, 20,"^");
		buttonList.add(upButton);
		downButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +132, 20, 20,"v");
		buttonList.add(downButton);
		if(te.programSelected == -1)
			if(te.hasClient())
				te.programSelected = 0;
			else if(te.hasServer())
				te.programSelected = 1;
		
		updateGui();
	}

	public void updateGui() {
		
    	//clientButton.enabled = te.hasClient();
    	//serverButton.enabled = te.hasServer();
		
		//Debug.print("Conn'd to "+te.connectedTo);
		if(te.programSelected == 0 && te.server != null && te.server.getServer() != null)
			displayPlayer = te.server.getServer().getOwner();
		else if(te.programSelected == 1 && te.client != null && te.client.getClient() != null)
			displayPlayer = te.client.getServer().getOwner();
		else displayPlayer = "UNDEFINED";	//Should never be shown
		
		buttonStrings.clear();
		
		if(te.programSelected == 0){
			programButton.displayString = "Client";
			SburbConnection c = SburbConnection.getClientConnection(te.owner);
			if(SburbConnection.enteredMedium(te.owner)) //if it should view the grist cache button. NEW Now even if the player isn't connected (but you still need to have had a connection there.
				buttonStrings.add("View Gristcache");
			if (te.server != null) { //If it is connected to someone.
				displayMessage = "Connected to "+displayPlayer;
				buttonStrings.add("Disconnect");
			} else if(te.resumingClient){
				displayMessage = "Waiting for server...";
				buttonStrings.add("Disconnect");
			} else if(c == null && !SburbConnection.isResuming(te.owner, true)){ //If the player doesn't have an other active client
				displayMessage = "Select a server below";
				if(SburbConnection.hasMainClient(te.owner)) //If it has a resumeable connection
					buttonStrings.add("Resume connection");
		    	for (String server : SburbConnection.getServersOpen())
		    		buttonStrings.add(server);
			} else 
				displayMessage = "A client is already active";
		}
		else if(te.programSelected == 1){
			programButton.displayString = "Server";
			if (te.client != null) {
				displayMessage = "Connected to "+displayPlayer;
				buttonStrings.add("Disconnect");
				if(!te.client.givenItems())
					buttonStrings.add("Give items");
			} else if (te.openToClients && te.client == null) {
				displayMessage = "Waiting for client...";
				buttonStrings.add("Disconnect");
			} else if(SburbConnection.isServerOpen(te.owner))
				displayMessage = "Server with your name exists";
			else {
				displayMessage = "Server offline";
				buttonStrings.add("Open to clients");
				if(SburbConnection.hasMainServer(te.owner) && SburbConnection.getClientConnection(SburbConnection.getAssociatedPartner(te.owner, false)) == null)
					buttonStrings.add("Resume connection");
		    	}
			}
    	upButton.enabled = index > 0;
    	downButton.enabled = 4+index < buttonStrings.size();
    	for (int i = 0; i < selButtons.size(); i++) {
    		GuiButton button = selButtons.get(i);
    		String s = getButtonString(i+index);
    		button.enabled = !s.equals("");
			button.displayString = s;
		}
	}
	
	/**
	 * Returns a string from <code>currentList</code>.
	 * This method returns an empty string if it otherwise would return an
	 * <code>ArrayIndexOutOfBoundsException</code>.
	 * @param index The index of the string wanted. Starts with the toopmost button of index 0.
	 * @return The string at position <code>index</> in the <code>currentList</code>.
	 */
	protected String getButtonString(int index){
		if(index >= buttonStrings.size() || index < 0)
			return "";
		else return buttonStrings.get(index);
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.equals(programButton))
			te.programSelected = getNextProgram();
		else if(guibutton.displayString.equals("Disconnect"))
			te.closeConnection(te.programSelected == 0, te.programSelected == 1);
		else if(te.programSelected == 0){
			if (guibutton == upButton) {
				index--;
			} else if (guibutton == downButton) {
				index++;
			} else if(guibutton.displayString.equals("View Gristcache")){
				mc.thePlayer.openGui(Minestuck.instance, 2, te.worldObj, te.xCoord, te.yCoord, te.zCoord);
			} else if(guibutton.displayString.equals("Resume connection")){
				te.resume(true);
			} else{
				te.connectToServer(guibutton.displayString);
			}
		} else if(te.programSelected == 1){
			if(guibutton.displayString.equals("Give items")){
				te.giveItems();
			} else if(guibutton.displayString.equals("Resume connection")){
				te.resume(false);
			} else if(guibutton.displayString.equals("Open to clients")){
				te.openServer();
			}
		}
		updateGui();
		te.latestmessage = "";
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
