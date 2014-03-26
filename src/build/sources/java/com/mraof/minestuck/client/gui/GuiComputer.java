package com.mraof.minestuck.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

//import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.ClearMessagePacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.UsernameHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiComputer extends GuiScreen
{

    private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/Sburb.png");
    private static final ResourceLocation guiBsod = new ResourceLocation("minestuck", "textures/gui/BsodMessage.png");
    
	private static final int xSize = 176;
	private static final int ySize = 166;
	
	private GuiButton upButton;
	private GuiButton downButton;
	private GuiButton programButton;
	
	private ArrayList<GuiButton> selButtons = new ArrayList<GuiButton>();
	private ArrayList<String> buttonStrings;
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
	private TileEntityComputer te;


	public GuiComputer(Minecraft mc,TileEntityComputer te)
	{
		super();
		
		this.mc = mc;
		this.fontRendererObj = mc.fontRenderer;
		this.te = te;
		te.gui = this;
		buttonStrings = new ArrayList<String>();
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{
		
		this.drawDefaultBackground();
		
		
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(!te.errored()?guiBackground:guiBsod);
		
		int yOffset = (this.height / 2) - (ySize / 2);
		this.drawTexturedModalRect((this.width / 2) - (xSize / 2), yOffset, 0, 0, xSize, ySize);
		if(!te.errored())
			if(te.latestmessage.get(te.programSelected) == null || te.latestmessage.get(te.programSelected).isEmpty())
				if(te.programSelected == -1){
					fontRendererObj.drawString("Insert disk.", (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
				} else fontRendererObj.drawString(displayMessage, (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
			else fontRendererObj.drawString(StatCollector.translateToLocal(te.latestmessage.get(te.programSelected)), (width - xSize) / 2 +15, (height - ySize) / 2 +45, 4210752);
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
			GuiButton button = new GuiButton(i + 2, (width - xSize) / 2 + 14, (height - ySize) / 2 + 60 + i*24, 120, 20,"");
			selButtons.add(button);
			buttonList.add(button);
		}
		
		programButton = new GuiButton(0, (width - xSize) / 2 + 95,(height - ySize)/2 + 10,70,20, "PROGRAM");
		buttonList.add(programButton);
		
		upButton = new GuiButton(-1, (width - xSize) / 2 + 140, (height - ySize) / 2 + 60, 20, 20,"^");
		buttonList.add(upButton);
		downButton = new GuiButton(-1, (width - xSize) / 2 + 140, (height - ySize) / 2 + 132, 20, 20,"v");
		buttonList.add(downButton);
		if(te.programSelected == -1)
			if(te.hasClient())
				te.programSelected = 0;
			else if(te.hasServer())
				te.programSelected = 1;
		
		updateGui();
	}

	public void updateGui() {
		
		programButton.enabled = te.installedPrograms.size() > 1;
		stringIndex = 0;
		
		if(te.errored()) {
			buttonList.clear();
			return;
		}
		
		String displayPlayer;
		if(te.programSelected == 0 && te.serverConnected && SkaiaClient.getClientConnection(te.owner) != null)
			displayPlayer = UsernameHandler.decode(SkaiaClient.getClientConnection(te.owner).getServerName());
		else if(te.programSelected == 1 && !te.clientName.isEmpty())
			displayPlayer = UsernameHandler.decode(te.clientName);
		else displayPlayer = "UNDEFINED";	//Should never be shown
		
		buttonStrings.clear();
		
		for(GuiButton button : selButtons)
			button.displayString = "";
		
		if(te.programSelected == 0){
			programButton.displayString = StatCollector.translateToLocal("computer.programClient");;
			SburbConnection c = SkaiaClient.getClientConnection(te.owner);
			if(!te.latestmessage.get(0).isEmpty())
				addButtonString("computer.buttonClear");
			if (te.serverConnected && c != null) { //If it is connected to someone.
				displayMessage = StatCollector.translateToLocalFormatted("computer.messageConnect", displayPlayer);
				addButtonString("computer.buttonClose");
			} else if(te.resumingClient){
				displayMessage = StatCollector.translateToLocal("computer.messageResumeClient");
				addButtonString("computer.buttonClose");
			} else if(!SkaiaClient.isActive(te.owner, true)){ //If the player doesn't have an other active client
				displayMessage = StatCollector.translateToLocal("computer.messageSelect");
				if(!SkaiaClient.getAssociatedPartner(te.owner, true).isEmpty()) //If it has a resumable connection
					addButtonString("computer.buttonResume");
				for (String server : SkaiaClient.getAvailableServers(te.owner))
					addButtonString("computer.buttonConnect",UsernameHandler.decode(server));
			} else 
				displayMessage = StatCollector.translateToLocal("computer.messageClientActive");
		}
		else if(te.programSelected == 1){
			programButton.displayString = StatCollector.translateToLocal("computer.programServer");
			if(!te.latestmessage.get(1).isEmpty())
				addButtonString("computer.buttonClear");
			if (!te.clientName.isEmpty() && SkaiaClient.getClientConnection(te.clientName) != null) {
				displayMessage = StatCollector.translateToLocalFormatted("computer.messageConnect", displayPlayer);
				addButtonString("computer.buttonClose");
				addButtonString("computer.buttonGive");
			} else if (te.openToClients) {
				displayMessage = StatCollector.translateToLocal("computer.messageResumeServer");
				addButtonString("computer.buttonClose");
			} else if(SkaiaClient.isActive(te.owner, false))
				displayMessage = StatCollector.translateToLocal("computer.messageServerActive");
			else {
				displayMessage = StatCollector.translateToLocal("computer.messageOffline");
				addButtonString("computer.buttonOpen");
				if(!SkaiaClient.getAssociatedPartner(te.owner, false).isEmpty() && SkaiaClient.getClientConnection(SkaiaClient.getAssociatedPartner(te.owner, false)) == null)
					addButtonString("computer.buttonResume");
		    	}
			}
    	upButton.enabled = index > 0;
    	downButton.enabled = 4+index < buttonStrings.size();
    	
    	for(GuiButton button : selButtons)
			button.enabled = !button.displayString.isEmpty();
    	
	}
	
	public void addButtonString(String key, Object... objects) {
		if(stringIndex >= index && stringIndex < index+4) {
			buttonStrings.add(key);
			selButtons.get(stringIndex-index).displayString = StatCollector.translateToLocalFormatted(key, objects);
			if(key.equals("computer.buttonConnect"))
				usernameList[stringIndex-index] = (String)objects[0];
		}
		stringIndex++;
	}
	
	protected void close(){
		SkaiaClient.sendCloseRequest(te,
				te.programSelected == 0?(te.resumingClient?"":SkaiaClient.getClientConnection(te.owner).getServerName()):
					(te.openToClients?"":te.clientName),
				te.programSelected == 0);
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		if(te.errored())
			return;
		if(!te.latestmessage.get(te.programSelected).isEmpty() && !guibutton.equals(programButton)
				&& !guibutton.equals(upButton) && !guibutton.equals(downButton))
			ClearMessagePacket.send(ComputerData.createData(te), te.programSelected);
		String buttonString = "";
		for(int i = 0; i < selButtons.size(); i++)
			if(guibutton.equals(selButtons.get(i))) {
				buttonString = buttonStrings.get(i);
				break;
			}
		if(guibutton.equals(programButton))
			te.programSelected = getNextProgram();
		else if(buttonString.equals("computer.buttonClose"))
			close();
		else if (guibutton == upButton)
			index--;
		else if (guibutton == downButton)
			index++;
		else if(te.programSelected == 0){
			if(buttonString.equals("computer.buttonResume")){
				SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, true), true);
			} else if(buttonString.equals("computer.buttonConnect")){
				SkaiaClient.sendConnectRequest(te, UsernameHandler.encode(usernameList[selButtons.indexOf(guibutton)]), true);
			}
		} else if(te.programSelected == 1){
			if(buttonString.equals("computer.buttonEdit")){
//				ClientEditHandler.activate(te.owner,te.clientName);
			} else if(buttonString.equals("computer.buttonGive")) {
				MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.CLIENT_EDIT, te.owner, te.clientName);
				MinestuckChannelHandler.sendToServer(packet);
			} else if(buttonString.equals("computer.buttonResume")){
				SkaiaClient.sendConnectRequest(te, SkaiaClient.getAssociatedPartner(te.owner, false), false);
			} else if(buttonString.equals("computer.buttonOpen")){
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
