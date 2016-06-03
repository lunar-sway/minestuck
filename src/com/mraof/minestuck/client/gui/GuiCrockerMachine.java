package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockCrockerMachine.MachineType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.ContainerCrockerMachine;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityCrockerMachine;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

public class GuiCrockerMachine extends GuiContainer
{
	
	private static final String[] guis = {"widget"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private MachineType type;
	protected TileEntityCrockerMachine te;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	private GuiButton goButton;

	public GuiCrockerMachine (InventoryPlayer inventoryPlayer, TileEntityCrockerMachine tileEntity) 
	{
	super(new ContainerCrockerMachine(inventoryPlayer, tileEntity));
	this.te = tileEntity;
	this.type = tileEntity.getMachineType();
	guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[type.ordinal()] + ".png");
	guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[type.ordinal()] + ".png");
	
	//sets prgress bar information based on machine type
	switch (type)
	{
	case GRIST_WIDGET:
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
		fontRendererObj.drawString(I18n.translateToLocal("gui."+guis[type.ordinal()]+".name"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (type == MachineType.GRIST_WIDGET && te.getStackInSlot(0) != null) 
		{
			//Render grist requirements
			ItemStack stack = AlchemyRecipeHandler.getDecodedItem(te.getStackInSlot(0));
			
			GristSet set = GristRegistry.getGristConversion(stack);
			if(set != null)
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
					set.gristTypes[i] = (int) (set.gristTypes[i]*multiplier);
			}
			
			GuiUtil.drawGristBoard(set, GuiUtil.GristboardMode.GRIST_WIDGET, 9, 45, fontRendererObj);
			
			List<String> tooltip = GuiUtil.getGristboardTooltip(set, mouseX - this.guiLeft, mouseY - this.guiTop, 9, 45, fontRendererObj);
			if(tooltip != null)
				this.drawHoveringText(tooltip, mouseX - this.guiLeft, mouseY - this.guiTop, fontRendererObj);
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
		int width = getScaledValue(te.progress,te.maxProgress,progressWidth);
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
			if(type == MachineType.GRIST_WIDGET && MinestuckConfig.clientDisableGristWidget)
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
				goButton.displayString = I18n.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
			else if (Mouse.getEventButton() == 1 && te.allowOverrideStop())
			{
				//Tell the machine to go until stopped
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true,!te.overrideStop);
				MinestuckChannelHandler.sendToServer(packet);
				
				te.overrideStop = !te.overrideStop;
				goButton.displayString = I18n.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
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
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ui_button_click, 1.0F));
			
			boolean mode = te.allowOverrideStop() && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON,true, mode && !te.overrideStop);
			MinestuckChannelHandler.sendToServer(packet);
			
			if(!mode)
				te.ready = true;
			te.overrideStop = mode && !te.overrideStop;
			goButton.displayString = I18n.translateToLocal(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
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