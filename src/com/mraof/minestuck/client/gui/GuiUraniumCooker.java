package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockUraniumCooker.MachineType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.ContainerUraniumCooker;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiUraniumCooker extends GuiContainer
{
	
	private static final String[] guis = {"uranium_cooker"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private MachineType type;
	protected TileEntityUraniumCooker te;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	private GuiButton goButton;

	public GuiUraniumCooker (InventoryPlayer inventoryPlayer, TileEntityUraniumCooker tileEntity) 
	{
		super(new ContainerUraniumCooker(inventoryPlayer, tileEntity));
		this.te = tileEntity;
		this.type = tileEntity.getMachineType();
		guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[type.ordinal()] + ".png");
		guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[type.ordinal()] + ".png");
		
		//sets progress bar information based on machine type
		switch (type)
		{
		case URANIUM_COOKER:
			progressX = 54;
			progressY = 23;
			progressWidth = 71;
			progressHeight = 10;
			goX = 69;
			goY = 69;
			break;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//draws "Cookalyzer"
		fontRendererObj.drawString(I18n.format("gui."+guis[type.ordinal()]+".name"), 8, 6, 4210752);
		
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
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
		int width = getScaledValue(te.getFuel(),te.getMaxFuel(),progressWidth);
		int height = progressHeight;
		drawModalRectWithCustomSizedTexture(x+progressX, y+progressY, 0, 0, width, height, progressWidth, progressHeight);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		if(!te.isAutomatic())
		{
			goButton = new GuiButtonExt(1, (width - xSize) / 2 + goX, (height - ySize) / 2 + goY, 30, 12, te.overrideStop ? "STOP" : "GO");
			buttonList.add(goButton);
		}
	}
	
	@Override
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
				goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
			else if (Mouse.getEventButton() == 1 && te.allowOverrideStop())
			{
				//Tell the machine to go until stopped
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true,!te.overrideStop);
				MinestuckChannelHandler.sendToServer(packet);
				
				te.overrideStop = !te.overrideStop;
				goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
		}
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1,par2,par3);
		if (par3 == 1)
		{
			if(goButton != null && goButton.mousePressed(this.mc, par1, par2))
			{
				goButton.playPressSound(this.mc.getSoundHandler());
				this.actionPerformed(goButton);
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		
		if(keyCode == 28)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			
			boolean mode = te.allowOverrideStop() && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true, mode && !te.overrideStop);
			MinestuckChannelHandler.sendToServer(packet);
			
			if(!mode)
				te.ready = true;
			te.overrideStop = mode && !te.overrideStop;
			goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
		}
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
	
}