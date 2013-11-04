package com.mraof.minestuck.client.gui;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.grist.GristHelper;
import com.mraof.minestuck.grist.GristRegistry;
import com.mraof.minestuck.grist.GristSet;
import com.mraof.minestuck.grist.GristStorage;
import com.mraof.minestuck.grist.GristType;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class GuiMachine extends GuiContainer {
	
	private static final String[] guis = {"cruxtruder","designex","lathe","alchemiter","widget"};
	private static final String[] guiTitles = {"Cruxtruder","Punch Designex","Totem Lathe","Alchemiter","GristWidget 12000"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private int metadata;
	private TileEntityMachine te;
	private EntityPlayer player;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int buttonX = 24;
	private int buttonY = 23;
	private GuiButton modeButton;
	private int goX;
	private int goY;
	private GuiButton goButton;

    public GuiMachine (InventoryPlayer inventoryPlayer,
            TileEntityMachine tileEntity) {
    super(new ContainerMachine(inventoryPlayer, tileEntity));
    this.te = tileEntity;
    this.metadata = tileEntity.getMetadata();
    guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[metadata] + ".png");
    guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[metadata] + ".png");
    this.player = inventoryPlayer.player;
    
    //sets prgress bar information based on machine type
    switch (metadata) {
    case (0):
    	progressX = 82;
    	progressY = 42;
    	progressWidth = 10;
    	progressHeight = 13;
    	break;
    case (1):
    	progressX = 81;
    	progressY = 38;
    	progressWidth = 43;
    	progressHeight = 17;
    	goX = 84;
    	goY = 55;
    	break;
    case (2):
    	progressX = 69;
    	progressY = 33;
    	progressWidth = 44;
    	progressHeight = 17;
    	goX = 73;
    	goY = 53;
    	break;
    case (3):
    	progressX = 54;
    	progressY = 23;
    	progressWidth = 71;
    	progressHeight = 10;
    	goX = 72;
    	goY = 31;
    	break;
    case (4):
       	progressX = 54;
		progressY = 23;
		progressWidth = 71;
		progressHeight = 10;
		goX = 72;
		goY = 31;
		break;
    }
}

@Override
protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    fontRenderer.drawString(guiTitles[metadata], 8, 6, 4210752);
    //draws "Inventory" or your regional equivalent
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    if ((metadata == 3 || metadata ==4) && te.inv[1] != null) 
    {
    	//Render grist requirements
    	NBTTagCompound nbttagcompound = te.inv[1].getTagCompound();
    	GristSet set = GristRegistry.getGristConversion(metadata == 3? AlchemyRecipeHandler.getDecodedItem(te.inv[1]) : te.inv[1]);
    	
    	if (set == null) {fontRenderer.drawString("Not Alchemizable", 9,45, 16711680); return;}
    	Hashtable reqs = set.getHashtable();
    	//Debug.print("reqs: " + reqs.size());
    	if (reqs != null) {
    		if (reqs.size() == 0) {
    			fontRenderer.drawString("Free!", 9,45, 65280);
    			return;
    		}
    	   	Iterator it = reqs.entrySet().iterator();
    	   	int place = 0;
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                int type = (Integer) pairs.getKey();
                int need = (Integer) pairs.getValue();
                int have = GristStorage.getClientGrist().getGrist(GristType.values()[type]);
                
                int row = place % 3;
                int col = place / 3;
                
                int color = metadata == 3 ? (need <= have ? 65280 : 16711680) : 0; //Green if we have enough grist, red if not, black if GristWidget
                
                fontRenderer.drawString(need + " " + GristType.values()[type].getName() + " (" + have + ")", 9 + (80 * col),45 + (8 * (row)), color);
                
                place++;
                
                //Debug.print("Need" + need + ". Have " + have);
            }
    	} else {
    		fontRenderer.drawString("Not Alchemizable", 9,45, 16711680);
    		return;
    	}
    } else if (metadata == 1) {
    	modeButton.drawButton = (te.inv[1] != null && te.inv[2] != null);
    }
}

@Override
protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //int texture = mc.renderEngine.getTexture("/gui/cruxtruder.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //this.mc.renderEngine.bindTexture(texture);
    
    //draw background
    this.mc.getTextureManager().bindTexture(guiBackground);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
    //draw progress bar
    this.mc.getTextureManager().bindTexture(guiProgress);
    int width = metadata == 0 ? progressWidth : getScaledValue(te.progress,te.maxProgress,progressWidth);
    int height = metadata != 0 ? progressHeight : getScaledValue(te.progress,te.maxProgress,progressHeight);
    this.drawCustomBox(x+progressX, y+progressY, 0, 0, width, height,progressWidth,progressHeight);
}

@Override
public void initGui() {
        super.initGui();
        //make buttons:		id, x, y, width, height, text
        if (metadata == 1) {
        	//The Designex's needs a button...
        	modeButton = new GuiButton(1, (width - xSize) / 2 + buttonX, (height - ySize) / 2 + buttonY, 20, 20, te.mode ? "&&": "||");
        	buttonList.add(modeButton);
        	modeButton.drawButton = (te.inv[1] != null && te.inv[2] != null);
        }
        if (metadata != 0) {
        	//All non-Cruxtruders need a Go button.
        	goButton = new GuiButton(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
        	buttonList.add(goButton);
        }
}

protected void actionPerformed(GuiButton guibutton) {

        
	if (guibutton == modeButton) {
		//Sends new mode info to server
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.COMBOBUTTON,te.mode ? false : true);
		packet.length = packet.data.length;
		this.mc.getNetHandler().addToSendQueue(packet);
	
		te.mode = !te.mode;
		modeButton.displayString = te.mode ? "&&" : "||";
	}
	
	if (guibutton == goButton) {
		
		if (Mouse.isButtonDown(0) && !te.overrideStop) {
			//Tell the machine to go once
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.GOBUTTON,true,false);
			packet.length = packet.data.length;
			this.mc.getNetHandler().addToSendQueue(packet);
			
			te.ready = true;
			goButton.displayString = te.overrideStop ? "STOP" : "GO";
		} else if (Mouse.getEventButton() < 2) {
			//Tell the machine to go until stopped
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.GOBUTTON,true,!te.overrideStop);
			packet.length = packet.data.length;
			this.mc.getNetHandler().addToSendQueue(packet);
			
			te.overrideStop = !te.overrideStop;
			goButton.displayString = te.overrideStop ? "STOP" : "GO";
		}
	}
}

@Override
protected void mouseClicked(int par1, int par2, int par3)
{
	super.mouseClicked(par1,par2,par3);
    if (par3 == 1)
    {
        for (int l = 0; l < this.buttonList.size(); ++l)
        {
            GuiButton guibutton = (GuiButton)this.buttonList.get(l);

            if (guibutton.mousePressed(this.mc, par1, par2) && guibutton == goButton)
            {
                this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                this.actionPerformed(guibutton);
            }
        }
    }
}

/**
 * Draws a box like drawModalRect, but with custom width and height values.
 */
public void drawCustomBox(int par1, int par2, int par3, int par4, int par5, int par6, int width, int height)
{
    float f = 1/(float)width;
    float f1 = 1/(float)height;
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
    tessellator.draw();
}

/**
 * Returns a number to be used in calculation of progres bar legnth.
 * 
 * @param progress the progress done.
 * @param max The maximim amount of progress.
 * @param imageMax The legnth of the progress bar image to scale to
 * @return The legnth the progress bar should be shown to
 */
public int getScaledValue(int progress,int max,int imageMax) {
	return (int) ((float) imageMax*((float)progress/(float)max));
}

}
