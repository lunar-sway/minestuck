package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.inventory.ContainerPunchDesignix;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiPunchDesignix extends GuiContainer
{
	
	private static final String[] guis = {"cruxtruder", "designix", "lathe", "alchemiter"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private MachineType type;
	protected TileEntityPunchDesignix te;
	//private EntityPlayer player;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	private GuiButton goButton;

	public GuiPunchDesignix (InventoryPlayer inventoryPlayer, TileEntityPunchDesignix tileEntity) 
	{
	super(new ContainerPunchDesignix(inventoryPlayer, tileEntity));
	this.te = tileEntity;
	this.type = MachineType.PUNCH_DESIGNIX;
	guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[type.ordinal()] + ".png");
	guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[type.ordinal()] + ".png");
	//this.player = inventoryPlayer.player;
	
	//sets prgress bar information based on machine type

		progressX = 63;
		progressY = 38;
		progressWidth = 43;
		progressHeight = 17;
		goX = 66;
		goY = 55;
		
}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(I18n.format("gui."+guis[type.ordinal()]+".name"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		
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
		int width = getScaledValue(te.progress, te.maxProgress, progressWidth);
		int height = progressHeight ;
		this.drawModalRectWithCustomSizedTexture(x+progressX, y+progressY, 0, 0, width, height, progressWidth, progressHeight);
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
	 * Draws a box like drawModalRect, but with custom width and height values.
	 */
	public void drawCustomBox(int par1, int par2, int par3, int par4, int par5, int par6, int width, int height)
	{
		float f = 1/(float)width;
		float f1 = 1/(float)height;
		BufferBuilder render = Tessellator.getInstance().getBuffer();
		render.begin(7, DefaultVertexFormats.POSITION_TEX);
		render.pos(par1, par2 + par6, 0D).tex((par3)*f, (par4 + par6)*f1).endVertex();
		render.pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 + par5)*f, (par4 + par6)*f1).endVertex();
		render.pos(par1 + par5, par2, this.zLevel).tex((par3 + par5)*f, (par4)*f1).endVertex();
		render.pos(par1, par2, this.zLevel).tex((par3)*f, (par4)*f1).endVertex();
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
	public int getScaledValue(int progress,int max,int imageMax)
	{
		return (int) ((float) imageMax*((float)progress/(float)max));
	}
}