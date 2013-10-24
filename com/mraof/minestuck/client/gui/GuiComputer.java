package com.mraof.minestuck.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.ClearMessagePacket;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;

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
		if(te.latestmessage.get(te.programSelected) == null || te.latestmessage.get(te.programSelected).isEmpty())
			if(te.programSelected == -1){
				fontRenderer.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
			} else fontRenderer.drawString(displayMessage, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		else fontRenderer.drawString(te.latestmessage.get(te.programSelected), (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
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
		
		programButton.enabled = te.installedPrograms.size() > 1;	//should this be implemented?
		
		//Debug.print("Conn'd to "+te.connectedTo);
		if(te.programSelected == 0 && te.serverConnected && SkaiaClient.getClientConnection(te.owner) != null)
			displayPlayer = SkaiaClient.getClientConnection(te.owner).getServerName();
		else if(te.programSelected == 1 && !te.clientName.isEmpty())
			displayPlayer = te.clientName;
		else displayPlayer = "UNDEFINED";	//Should never be shown
		
		buttonStrings.clear();
		
		if(te.programSelected == 0){
			programButton.displayString = "Client";
			SburbConnection c = SkaiaClient.getClientConnection(te.owner);
			if(!te.latestmessage.get(0).isEmpty())
				buttonStrings.add("Clear message");
			if(SkaiaClient.enteredMedium(te.owner)) //if it should view the grist cache button.
				buttonStrings.add("View Gristcache");
			if (te.serverConnected && c != null) { //If it is connected to someone.
				displayMessage = "Connected to "+displayPlayer;
				buttonStrings.add("Disconnect");
			} else if(te.resumingClient){
				displayMessage = "Waiting for server...";
				buttonStrings.add("Disconnect");
			} else if(!SkaiaClient.isActive(te.owner, true)){ //If the player doesn't have an other active client
				displayMessage = "Select a server below";
				if(!SkaiaClient.getAssociatedPartner(te.owner, true).isEmpty()) //If it has a resumable connection
					buttonStrings.add("Resume connection");
				for (String server : SkaiaClient.getAvailableServers(te.owner))
		    		buttonStrings.add(server);
			} else 
				displayMessage = "A client is already active";
		}
		else if(te.programSelected == 1){
			programButton.displayString = "Server";
			if(!te.latestmessage.get(1).isEmpty())
				buttonStrings.add("Clear message");
			if (!te.clientName.isEmpty() && SkaiaClient.getClientConnection(te.clientName) != null) {
				displayMessage = "Connected to "+displayPlayer;
				buttonStrings.add("Disconnect");
				buttonStrings.add("Experimental");
				if(!SkaiaClient.getClientConnection(te.clientName).givenItems())
					buttonStrings.add("Give items");
			} else if (te.openToClients) {
				displayMessage = "Waiting for client...";
				buttonStrings.add("Disconnect");
			} else if(SkaiaClient.isActive(te.owner, false))
				displayMessage = "Server with your name exists";
			else {
				displayMessage = "Server offline";
				buttonStrings.add("Open to clients");
				if(!SkaiaClient.getAssociatedPartner(te.owner, false).isEmpty() && SkaiaClient.getClientConnection(SkaiaClient.getAssociatedPartner(te.owner, false)) == null)
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
	
	protected void close(){
		SkaiaClient.sendCloseRequest(te,
				te.programSelected == 0?(te.resumingClient?"":SkaiaClient.getClientConnection(te.owner).getServerName()):
					(te.openToClients?"":te.clientName),
				te.programSelected == 0);
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		if(!te.latestmessage.get(te.programSelected).isEmpty() && !guibutton.equals(programButton)
				&& !guibutton.equals(upButton) && !guibutton.equals(downButton))
			ClearMessagePacket.send(ComputerData.createData(te), te.programSelected);
		if(guibutton.equals(programButton))
			te.programSelected = getNextProgram();
		else if(guibutton.displayString.equals("Disconnect"))
			close();
		else if(guibutton.displayString.equals("Clear message"))
			return;
		else if(te.programSelected == 0){
			if (guibutton == upButton) {
				index--;
			} else if (guibutton == downButton) {
				index++;
			} else if(guibutton.displayString.equals("View Gristcache")){
				mc.thePlayer.openGui(Minestuck.instance, 2, te.worldObj, te.xCoord, te.yCoord, te.zCoord);
			} else if(guibutton.displayString.equals("Resume connection")){
				SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, true), true);
			} else{
				SkaiaClient.sendConnectRequest(te, guibutton.displayString, true);
			}
		} else if(te.programSelected == 1){
			if(guibutton.displayString.equals("Experimental")){
				com.mraof.minestuck.util.SburbServerController.add(te.owner,te.clientName);
			} else if(guibutton.displayString.equals("Give items")){
				SkaiaClient.giveItems(te.clientName);
			} else if(guibutton.displayString.equals("Resume connection")){
				SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, false), false);
			} else if(guibutton.displayString.equals("Open to clients")){
				SkaiaClient.sendConnectRequest(te, "", false);
			}
		}
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
