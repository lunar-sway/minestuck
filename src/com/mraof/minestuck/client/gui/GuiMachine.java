package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Mouse;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.GristType;

public class GuiMachine extends GuiContainer {
	
	private static final String[] guis = {"cruxtruder","designix","lathe","alchemiter","widget"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private int metadata;
	protected TileEntityMachine te;
	//private EntityPlayer player;
	
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

	public GuiMachine (InventoryPlayer inventoryPlayer, TileEntityMachine tileEntity) 
	{
	super(new ContainerMachine(inventoryPlayer, tileEntity));
	this.te = tileEntity;
	this.metadata = tileEntity.getMetadata();
	guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[metadata] + ".png");
	guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[metadata] + ".png");
	//this.player = inventoryPlayer.player;
	
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
		fontRendererObj.drawString(StatCollector.translateToLocal("gui."+guis[metadata]+".name"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if ((metadata == 3 || metadata ==4) && te.inv[1] != null) 
		{
			//Render grist requirements
			GristSet set = GristRegistry.getGristConversion(AlchemyRecipeHandler.getDecodedItem(te.inv[1], metadata == 3? true : false)).copy();
			boolean selectedType = AlchemyRecipeHandler.getDecodedItem(te.inv[1], true).getItem() == Minestuck.captchaCard;
			if(selectedType)
				set = metadata == 3 ? new GristSet(te.selectedGrist, 1) : null;
			if(metadata == 4 && set != null)
			{
				float multiplier = AlchemyRecipeHandler.getDecodedItem(te.inv[1], false).stackSize;
				if(multiplier != 1)
					set.scaleGrist(multiplier);
			}
			
		if (set == null) {fontRendererObj.drawString(StatCollector.translateToLocal("gui.notAlchemizable"), 9,45, 16711680); return;}
			Hashtable<Integer, Integer> reqs = set.getHashtable();
		//Debug.print("reqs: " + reqs.size());
		if (reqs != null) {
			if (reqs.size() == 0) {
				fontRendererObj.drawString(StatCollector.translateToLocal("gui.free"), 9,45, 65280);
				return;
			}
				Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
		   	int place = 0;
			while (it.hasNext()) {
					Map.Entry<Integer, Integer> pairs = it.next();
				int type = pairs.getKey();
				int need = pairs.getValue();
				int have = MinestuckPlayerData.getClientGrist().getGrist(GristType.values()[type]);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = metadata == 3 ? (selectedType ? 0xFF0000FF : need <= have ? 0x00FF00 : 0xFF0000) : 0; //Green if we have enough grist, red if not, black if GristWidget
				
				fontRendererObj.drawString(need + " " + GristType.values()[type].getDisplayName() + " (" + have + ")", 9 + (80 * col),45 + (8 * (row)), color);
				
				place++;
				
				//Debug.print("Need" + need + ". Have " + have);
			}
		} else {
			fontRendererObj.drawString(StatCollector.translateToLocal("gui.notAlchemizable"), 9,45, 16711680);
			return;
		}
	} else if (metadata == 1) {
		modeButton.visible = (te.inv[1] != null && te.inv[2] != null);
	}
}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
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

	@SuppressWarnings("unchecked")
	@Override
public void initGui() {
		super.initGui();
		//make buttons:		id, x, y, width, height, text
		if (metadata == 1) {
			//The Designix's needs a button...
			modeButton = new GuiButton(1, (width - xSize) / 2 + buttonX, (height - ySize) / 2 + buttonY, 20, 20, te.mode ? "&&": "||");
			buttonList.add(modeButton);
			modeButton.visible = (te.inv[1] != null && te.inv[2] != null);
		}
		if (metadata != 0) {
			//All non-Cruxtruders need a Go button.
			goButton = new GuiButton(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
			buttonList.add(goButton);
		}
}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		
		if (guibutton == modeButton)
		{
			//Sends new mode info to server
			te.mode = !te.mode;
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.MACHINE_STATE, te.mode);
			MinestuckChannelHandler.sendToServer(packet);
			modeButton.displayString = te.mode ? "&&" : "||";
		}
		
		if (guibutton == goButton)
		{
			if (Mouse.getEventButton() == 0 && !te.overrideStop)
			{
				//Tell the machine to go once
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true,false);
				MinestuckChannelHandler.sendToServer(packet);
				
				te.ready = true;
				te.overrideStop = false;
				goButton.displayString = StatCollector.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
			else if (Mouse.getEventButton() == 1 && te.getBlockMetadata() > 2)
			{
				//Tell the machine to go until stopped
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true,!te.overrideStop);
				MinestuckChannelHandler.sendToServer(packet);
				
				te.overrideStop = !te.overrideStop;
				goButton.displayString = StatCollector.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
		}
	}
	
@Override
protected void mouseClicked(int par1, int par2, int par3) throws IOException
{
	super.mouseClicked(par1,par2,par3);
	if (par3 == 1)
	{
		for (int l = 0; l < this.buttonList.size(); ++l)
		{
			GuiButton guibutton = (GuiButton)this.buttonList.get(l);

			if (guibutton.mousePressed(this.mc, par1, par2) && guibutton == goButton)
			{
				
				guibutton.playPressSound(this.mc.getSoundHandler());
				this.actionPerformed(guibutton);
			}
		}
	}
	else if(te.getBlockMetadata() == 3 && par3 == 0 && mc.thePlayer.inventory.getItemStack() == null
			&& te.inv[1] != null && AlchemyRecipeHandler.getDecodedItem(te.inv[1], true).getItem() == Minestuck.captchaCard
			&& par1 >= guiLeft + 9 && par1 < guiLeft + 167 && par2 >= guiTop + 45 && par2 < guiTop + 70)
	{
		mc.currentScreen = new GuiGristSelector(this);
		mc.currentScreen.setWorldAndResolution(mc, width, height);
	}
}

	/**
	 * Draws a box like drawModalRect, but with custom width and height values.
	 */
	public void drawCustomBox(int par1, int par2, int par3, int par4, int par5, int par6, int width, int height)
	{
		float f = 1/(float)width;
		float f1 = 1/(float)height;
		WorldRenderer render = Tessellator.getInstance().getWorldRenderer();
		render.startDrawingQuads();
		render.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
		render.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
		render.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
		render.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
		Tessellator.getInstance().draw();
	}

/**
 * Returns a number to be used in calculation of progress bar length.
 * 
 * @param progress the progress done.
 * @param max The maximum amount of progress.
 * @param imageMax The length of the progress bar image to scale to
 * @return The length the progress bar should be shown to
 */
public int getScaledValue(int progress,int max,int imageMax) {
	return (int) ((float) imageMax*((float)progress/(float)max));
}

}
