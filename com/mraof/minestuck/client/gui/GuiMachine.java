package com.mraof.minestuck.client.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.network.ComboButtonPacket;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.CombinationMode;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMachine extends GuiContainer {
	
	private static final String[] guis = {"cruxtruder","designex","lathe","alchemiter"};
	private static final String[] guiTitles = {"Cruxtruder","Punch Designex","Totem Lathe","Alchemiter"};
	
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
    	break;
    case (2):
    	progressX = 69;
    	progressY = 33;
    	progressWidth = 44;
    	progressHeight = 17;
    	break;
    case (3):
    	progressX = 54;
    	progressY = 23;
    	progressWidth = 71;
    	progressHeight = 10;
    	break;
    }
}

@Override
protected void drawGuiContainerForegroundLayer(int param1, int param2) {
    fontRenderer.drawString(guiTitles[metadata], 8, 6, 4210752);
    //draws "Inventory" or your regional equivalent
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    if (metadata == 3 && te.inv[1] != null) {
    	//Render grist requirements
    	NBTTagCompound nbttagcompound = te.inv[1].getTagCompound();
    	GristSet set = GristRegistry.getGristConversion(new ItemStack(nbttagcompound.getInteger("contentID"),1,nbttagcompound.getInteger("contentMeta")));
    	if (set == null) {return;}
    	Hashtable reqs = set.getTable();
    	//System.out.println("reqs: " + reqs.size());
    	if (reqs != null) {
    	   	Iterator it = reqs.entrySet().iterator();
    	   	int place = 0;
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                int type = (Integer) pairs.getKey();
                int need = (Integer) pairs.getValue();
                int have =  player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag("Grist").getInteger(EntityGrist.gristTypes[type]);
                
                int row = place % 3;
                int col = place / 3;
                
                int color = need <= have ? 65280 : 16711680; //Green if we have enough grist, red if not
                
                fontRenderer.drawString(need + " " + EntityGrist.gristTypes[type] + " (" + have + ")", 9 + (80 * col),45 + (8 * (row)), color);
                
                place++;
                
                //System.out.println("Need" + need + ". Have " + have);
            }
    	}
    }
}

@Override
protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //int texture = mc.renderEngine.getTexture("/gui/cruxtruder.png");
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    //this.mc.renderEngine.bindTexture(texture);
    
    //draw background
    this.mc.func_110434_K().func_110577_a(guiBackground);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
    //draw progress bar
    this.mc.func_110434_K().func_110577_a(guiProgress);
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
        	modeButton = new GuiButton(1, (width - xSize) / 2 + buttonX, (height - ySize) / 2 + buttonY, 20, 20, te.mode == CombinationMode.AND ? "&&": "||");
        	buttonList.add(modeButton);
        }
        if (metadata == 3) {
        	//... and so does the Alchemiter.
        	modeButton = new GuiButton(1, (width - xSize) / 2 + 72, (height - ySize) / 2 + 31, 30, 12, "GO!");
        	buttonList.add(modeButton);
        }
//        if (metadata == 3) {
//        	//If it's an Alchemiter, we need the player who placed it
//    		Packet250CustomPayload packet = new Packet250CustomPayload();
//    		packet.channel = "Minestuck";
//    		packet.data = MinestuckPacket.makePacket(Type.MACHINEOWNER);
//    		packet.length = packet.data.length;
//    		this.mc.getNetHandler().addToSendQueue(packet);
//        }
}

protected void actionPerformed(GuiButton guibutton) {

        
	if (metadata == 1) {
		//Sends new mode info to server
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.COMBOBUTTON,te.mode == CombinationMode.AND ? false : true);
		packet.length = packet.data.length;
		this.mc.getNetHandler().addToSendQueue(packet);
	
		te.mode = te.mode == CombinationMode.AND ? CombinationMode.OR : CombinationMode.AND;
		modeButton.displayString = te.mode == CombinationMode.AND ? "&&" : "||";
	}
	
	if (metadata == 3) {
		//Tell the Alchemiter to go
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.GOBUTTON,true);
		packet.length = packet.data.length;
		this.mc.getNetHandler().addToSendQueue(packet);
		
		te.alcReady = true;
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
