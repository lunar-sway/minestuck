package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.ContainerMachine;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachine;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
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
	private int goX;
	private int goY;
	private GuiButton goButton;

	public GuiMachine (InventoryPlayer inventoryPlayer, TileEntityMachine tileEntity) 
	{
	super(new ContainerMachine(inventoryPlayer, tileEntity));
	this.te = tileEntity;
	this.metadata = tileEntity.getMachineType();
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
		progressX = 63;
		progressY = 38;
		progressWidth = 43;
		progressHeight = 17;
		goX = 66;
		goY = 55;
		break;
	case (2):
		progressX = 81;
		progressY = 33;
		progressWidth = 44;
		progressHeight = 17;
		goX = 85;
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString(StatCollector.translateToLocal("gui."+guis[metadata]+".name"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if ((metadata == 3 || metadata == 4) && te.inv[1] != null) 
		{
			//Render grist requirements
			ItemStack stack = AlchemyRecipeHandler.getDecodedItem(te.inv[1]);
			if(metadata == 3 && !(te.inv[1].hasTagCompound() && te.inv[1].getTagCompound().hasKey("contentID")))
				stack = new ItemStack(MinestuckBlocks.blockStorage, 1, 1);
			
			GristSet set = GristRegistry.getGristConversion(stack);
			boolean useSelectedType = stack == null ? false : stack.getItem() == MinestuckItems.captchaCard;
			if(useSelectedType)
				set = metadata == 3 ? new GristSet(te.selectedGrist, MinestuckConfig.clientCardCost) : null;
			if(metadata == 4 && set != null)
			{
				float multiplier = stack.stackSize;
				if(multiplier != 1)
					set = set.scaleGrist(multiplier);
				set.scaleGrist(0.9F);
			}
			if(set != null && stack.isItemDamaged())
			{
				float multiplier = 1 - stack.getItem().getDamage(stack)/((float) stack.getMaxDamage());
				for(int i = 0; i < set.gristTypes.length; i++)
					if(metadata == 3)
						set.gristTypes[i] = (int) Math.ceil(set.gristTypes[i]*multiplier);
					else set.gristTypes[i] = (int) (set.gristTypes[i]*multiplier);
			}
			
			drawGristBoard(set, useSelectedType, mouseX - this.guiLeft, mouseY - this.guiTop);	//Includes tooltips, so keep this last
			
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
		if(metadata != 0)
			this.drawCustomBox(x+progressX, y+progressY, 0, 0, width, height,progressWidth,progressHeight);
		else this.drawCustomBox(x+progressX, y+progressY+progressHeight-height, 0, progressHeight-height, width, height,progressWidth,progressHeight);
	}

	@SuppressWarnings("unchecked")
	@Override
public void initGui()
	{
		super.initGui();
		
		if (metadata != 0)
		{
			//All non-Cruxtruders need a Go button.
			goButton = new GuiButtonExt(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
			buttonList.add(goButton);
			if(metadata == 4 && MinestuckConfig.clientDisableGristWidget)
				goButton.enabled = false;
		}
}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		
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
			else if (Mouse.getEventButton() == 1 && te.getMachineType() > 2)
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
			if (goButton != null && goButton.mousePressed(this.mc, par1, par2))
			{
				goButton.playPressSound(this.mc.getSoundHandler());
				this.actionPerformed(goButton);
			}
		}
		else if(te.getMachineType() == 3 && par3 == 0 && mc.thePlayer.inventory.getItemStack() == null
				&& te.inv[1] != null && AlchemyRecipeHandler.getDecodedItem(te.inv[1]) != null && AlchemyRecipeHandler.getDecodedItem(te.inv[1]).getItem() == MinestuckItems.captchaCard
				&& par1 >= guiLeft + 9 && par1 < guiLeft + 167 && par2 >= guiTop + 45 && par2 < guiTop + 70)
		{
			mc.currentScreen = new GuiGristSelector(this);
			mc.currentScreen.setWorldAndResolution(mc, width, height);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		
		if(keyCode == 28)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
			
			boolean mode = te.getMachineType() > 2 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true, mode && !te.overrideStop);
			MinestuckChannelHandler.sendToServer(packet);
			
			if(!mode)
				te.ready = true;
			te.overrideStop = mode && !te.overrideStop;
			goButton.displayString = StatCollector.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
		}
	}
	
	/**
	 * Draws a box like drawModalRect, but with custom width and height values.
	 */
	public void drawCustomBox(int par1, int par2, int par3, int par4, int par5, int par6, int width, int height)
	{
		Gui.drawModalRectWithCustomSizedTexture(par1, par2, par3, par4, par5, par6, width, height);
	}

	/**
	 * Returns a number to be used in calculation of progress bar length.
	 * 
	 * @param progress the progress done.
	 * @param max The maximum amount of progress.
	 * @param imageMax The length of the progress bar image to scale to
	 * @return The length the progress bar should be shown to
	 */
	public int getScaledValue(int progress,int max,int imageMax)
	{
		return (int) ((float) imageMax*((float)progress/(float)max));
	}
	
	private void drawGristBoard(GristSet cost, boolean selectiveType, int mouseX, int mouseY)
	{
		if (cost == null)
		{
			fontRendererObj.drawString(StatCollector.translateToLocal("gui.notAlchemizable"), 9,45, 16711680);
			return;
		}
		GristSet playerGrist = MinestuckPlayerData.getClientGrist();
		
		Hashtable<Integer, Integer> reqs = cost.getHashtable();
		if (reqs.size() == 0)
		{
			fontRendererObj.drawString(StatCollector.translateToLocal("gui.free"), 9,45, 65280);
			return;
		}
		List<String> tooltip= null;
		Iterator<Entry<Integer, Integer>> it = reqs.entrySet().iterator();
		if(!MinestuckConfig.alchemyIcons)
		{
			int place = 0;
			while (it.hasNext())
			{
				Map.Entry<Integer, Integer> pairs = it.next();
				GristType type = GristType.values()[pairs.getKey()];
				int need = pairs.getValue();
				int have = playerGrist.getGrist(type);
				
				int row = place % 3;
				int col = place / 3;
				
				int color = metadata == 3 ? (selectiveType ? 0x0000FF : need <= have ? 0x00FF00 : 0xFF0000) : 0; //Green if we have enough grist, red if not, black if GristWidget
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				fontRendererObj.drawString(needStr + " " + type.getDisplayName() + " (" + haveStr + ")", 9 + 79*col, 45 + 8*row, color);
				
				if(tooltip == null && mouseY >= 45 + 8*row && mouseY < 53 + 8*row)
				{
					int width = fontRendererObj.getStringWidth(needStr + " " + type.getDisplayName() + " (");
					if(!needStr.equals(String.valueOf(need)) && mouseX >= 9 + 79*col && mouseX < 9 + 79*col + fontRendererObj.getStringWidth(needStr))
						tooltip = Arrays.asList(String.valueOf(need));
					else if(!haveStr.equals(String.valueOf(have)) && mouseX >= 9 + 79*col + width && mouseX < 9 + 79*col + width + fontRendererObj.getStringWidth(haveStr))
						tooltip = Arrays.asList(String.valueOf(have));
				}
				
				place++;
				
			}
		} else
		{
			int index = 0;
			while(it.hasNext())
			{
				Map.Entry<Integer, Integer> pairs = it.next();
				GristType type = GristType.values()[pairs.getKey()];
				int need = pairs.getValue();
				int have = playerGrist.getGrist(type);
				int row = index/158;
				int color = metadata == 3 ? (selectiveType ? 0x0000FF : need <= have ? 0x00FF00 : 0xFF0000) : 0;
				
				String needStr = GuiHandler.addSuffix(need), haveStr = GuiHandler.addSuffix(have);
				boolean prefixHave = !haveStr.equals(String.valueOf(have));
				int haveStrWidth = fontRendererObj.getStringWidth(haveStr);
				haveStr = '('+haveStr+')';
				int needStrWidth = fontRendererObj.getStringWidth(needStr);
				if(index + needStrWidth + 10 + fontRendererObj.getStringWidth(haveStr) > (row + 1)*158)
				{
					row++;
					index = row*158;
				}
				fontRendererObj.drawString(needStr, 9 + index%158, 45 + 8*row, color);
				fontRendererObj.drawString(haveStr, needStrWidth + 19 + index%158, 45 + 8*row, color);
				
				GlStateManager.color(1, 1, 1);
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck", "textures/grist/" + type.getName()+ ".png"));
				drawCustomBox(needStrWidth + 10 + index%158, 45 + 8*row, 0, 0, 8, 8, 8, 8);
				
				if(tooltip == null && mouseY >= 45 + 8*row && mouseY < 53 + 8*row)
				{
					if(!needStr.equals(String.valueOf(need)) && mouseX >= 9 + index%158 && mouseX < 9 + index%158 + needStrWidth)
						tooltip = Arrays.asList(String.valueOf(need));
					else if(mouseX >= 10 + index%158 + needStrWidth && mouseX < 18 + index%158 + needStrWidth)
						tooltip = Arrays.asList(type.getDisplayName());
					else if(prefixHave && mouseX >= 19 + index%158 + needStrWidth + fontRendererObj.getCharWidth('(') && mouseX < 19 + index%158 + needStrWidth + fontRendererObj.getCharWidth('(') + haveStrWidth)
						tooltip = Arrays.asList(String.valueOf(have));
				}
				
				index += needStrWidth + 10 + fontRendererObj.getStringWidth(haveStr);
				index = Math.min(index + 6, (row + 1)*158);
			}
		}
		if(tooltip != null)
			this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
	}
	
}