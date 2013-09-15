package com.mraof.minestuck.client.gui;


import java.util.ArrayList;

import javax.jws.Oneway;

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
	private GuiButton clientButton;
	private GuiButton serverButton;
	
	private ArrayList<GuiButton> selButtons = new ArrayList<GuiButton>();
	private ArrayList<String> clientContent;
	private ArrayList<String> serverContent;
	private ArrayList<String> currentList;
	private String displayName;
	public int programSelected = -1;	//0 if client is selected, 1 if server.

	public Minecraft mc;
	private TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();

		this.mc = mc;
		this.fontRenderer = mc.fontRenderer;
		this.te = te;
		te.gui = this;
		clientContent = new ArrayList<String>();
		serverContent = new ArrayList<String>();
		currentList = new ArrayList<String>();
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{

		this.drawDefaultBackground();
		
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.func_110434_K().func_110577_a(guiBackground);
		
		int yOffset = (this.height / 2) - (ySize / 2);
		this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		if(currentList.size() == 0){
			fontRenderer.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		} else fontRenderer.drawString(currentList.get(0), (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
		
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
		
		clientButton = new GuiButton(0, (width - xSize)/2 +95,(height - ySize)/2 +10,35,20, "Client");
		buttonList.add(clientButton);
		serverButton = new GuiButton(1, (width - xSize)/2 +128, (height - ySize)/2 +10, 40, 20, "Server");
		buttonList.add(serverButton);
		
		upButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +60, 20, 20,"^");
		buttonList.add(upButton);
		downButton = new GuiButton(-1, (width - xSize) / 2 +140, (height - ySize) / 2 +132, 20, 20,"v");
		buttonList.add(downButton);
		System.out.println("Init gui: Client active:"+te.hasClient+" Server active:"+te.hasServer);
		if(te.hasClient){
			programSelected = 0;
			currentList = clientContent;
		}
		else if(te.hasServer){
			programSelected = 1;
		}
		updateGui();
	}

	public void updateGui() {
		
    	upButton.enabled = false;
    	downButton.enabled = false;
    	clientButton.enabled = te.hasClient;
    	serverButton.enabled = te.hasServer;
		
		displayName = "";
		//Debug.print("Conn'd to "+te.connectedTo);
		String[] parts;
		if(programSelected == 0)
			parts = te.connectedClient.split("\0");
		else if(programSelected == 1)
			parts = te.connectedServer.split("\0");
		else parts = new String[0];
		for (String part : parts) {
			displayName += part;
		}
		
		clientContent.clear();
		serverContent.clear();
		
		if(te.hasClient){
			if (!te.connectedServer.equals("")) {
				clientContent.add("Connected to "+displayName+".");
			} else {
				clientContent.add("Select a server below.");
		    	
		    	int i = 0;
		    	for (Object server : SburbConnection.getServersOpen()) {
		    		if (!(i < selButtons.size() && selButtons.get(i) != null))
		    			downButton.enabled = true;
		    		setButtonString(i, (String)server, clientContent);
		    		i++;
		    	}
			}
		}
		if(te.hasServer){
			if (!te.connectedClient.equals("")) {
					serverContent.add("Connected to "+displayName+".");
					if(!te.givenItems)
						setButtonString(0, "Give items.",serverContent);
			} else if (te.openToClients && te.connectedClient.equals("")) {
					serverContent.add("Waiting for client...");
			} else {
				serverContent.add("Server offline.");
			
				setButtonString(0,"Open to clients.",serverContent);
		    	}
			}
    	
    	for (int i = 0; i < selButtons.size(); i++) {
    		GuiButton button = selButtons.get(i);
    		String s = getButtonString(i+1);
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
		if(index+1 >= currentList.size() || index+1 < 0)
			return "";
		else return currentList.get(index+1);
	}
	
	/**
	 * Sets the position in the array list to the given string.
	 * Will add empty string if neccesary to avoid an <code>ArrayIndexOutOfBoundException</code>.
	 * @param index The listed button string positions. Starting from the top, at 0.
	 * @param s The new string.
	 * @param list The list that <code>s</code> is added to.
	 */
	protected void setButtonString(int index, String s, ArrayList<String> list){
		if(index+1 < 0)
			return;
		while(index+1 >= list.size())
			list.add("");
		list.set(index+1, s);
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.equals(serverButton)){
			programSelected = 1;
			currentList = serverContent;
		} else if(guibutton.equals(clientButton)){
			programSelected = 0;
			currentList = clientContent;
		} else if(programSelected == 0){
			if (guibutton == upButton) {
				
			} else if (guibutton == downButton) {
				
			} else {
				
				te.connectedServer = guibutton.displayString;
				
				te.updateConnection();
			}
		} else if(programSelected == 1){
			if (!te.connectedClient.equals("") && !te.givenItems) {
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "Minestuck";
				packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE,te.xCoord,te.yCoord,te.zCoord,te.connectedClient);
				packet.length = packet.data.length;
				this.mc.getNetHandler().addToSendQueue(packet);
				te.givenItems = true;
				te.updateConnection();
			} else {
				te.updateConnection();
			}
		}
		updateGui();
	}
}
