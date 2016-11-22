package com.mraof.minestuck.client.gui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockSburbMachine.MachineType;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.inventory.ContainerSburbMachine;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;

public class GuiSburbMachine extends GuiContainer
{
	
	private static final String[] guis = {"cruxtruder", "designix", "lathe", "alchemiter"};
	
	private ResourceLocation guiBackground;
	private ResourceLocation guiProgress;
	private MachineType type;
	protected TileEntitySburbMachine te;
	//private EntityPlayer player;
	
	private int progressX;
	private int progressY;
	private int progressWidth;
	private int progressHeight;
	private int goX;
	private int goY;
	private GuiButton goButton;

	public GuiSburbMachine (InventoryPlayer inventoryPlayer, TileEntitySburbMachine tileEntity) 
	{
	super(new ContainerSburbMachine(inventoryPlayer, tileEntity));
	this.te = tileEntity;
	this.type = tileEntity.getMachineType();
	guiBackground = new ResourceLocation("minestuck:textures/gui/" + guis[type.ordinal()] + ".png");
	guiProgress = new ResourceLocation("minestuck:textures/gui/progress/" + guis[type.ordinal()] + ".png");
	//this.player = inventoryPlayer.player;
	
	//sets prgress bar information based on machine type
	switch (type) {
	case CRUXTRUDER:
		progressX = 82;
		progressY = 42;
		progressWidth = 10;
		progressHeight = 13;
		break;
	case PUNCH_DESIGNIX:
		progressX = 63;
		progressY = 38;
		progressWidth = 43;
		progressHeight = 17;
		goX = 66;
		goY = 55;
		break;
	case TOTEM_LATHE:
		progressX = 81;
		progressY = 33;
		progressWidth = 44;
		progressHeight = 17;
		goX = 85;
		goY = 53;
		break;
	case ALCHEMITER:
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
		fontRendererObj.drawString(I18n.format("gui."+guis[type.ordinal()]+".name"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		if (type == MachineType.ALCHEMITER && te.getStackInSlot(0) != null) 
		{
			//Render grist requirements
			ItemStack stack = AlchemyRecipeHandler.getDecodedItem(te.getStackInSlot(0));
			if(type == MachineType.ALCHEMITER && !(te.getStackInSlot(0).hasTagCompound() && te.getStackInSlot(0).getTagCompound().hasKey("contentID")))
				stack = new ItemStack(MinestuckBlocks.genericObject);
			
			GristSet set = GristRegistry.getGristConversion(stack);
			boolean useSelectedType = stack == null ? false : stack.getItem() == MinestuckItems.captchaCard;
			if(useSelectedType)
				set = new GristSet(te.selectedGrist, MinestuckConfig.clientCardCost);
			if(set != null && stack.isItemDamaged())
			{
				float multiplier = 1 - stack.getItem().getDamage(stack)/((float) stack.getMaxDamage());
				for(int i = 0; i < set.gristTypes.length; i++)
					if(type == MachineType.ALCHEMITER)
						set.gristTypes[i] = (int) Math.ceil(set.gristTypes[i]*multiplier);
					else set.gristTypes[i] = (int) (set.gristTypes[i]*multiplier);
			}
			
			GuiUtil.drawGristBoard(set, useSelectedType ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45, fontRendererObj);
			
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
		int width = type == MachineType.CRUXTRUDER ? progressWidth : getScaledValue(te.progress, te.maxProgress, progressWidth);
		int height = type != MachineType.CRUXTRUDER ? progressHeight : getScaledValue(te.progress, te.maxProgress, progressHeight);
		if(type != MachineType.CRUXTRUDER)
			this.drawModalRectWithCustomSizedTexture(x+progressX, y+progressY, 0, 0, width, height, progressWidth, progressHeight);
		else this.drawModalRectWithCustomSizedTexture(x+progressX, y+progressY+progressHeight-height, 0, progressHeight-height, width, height, progressWidth, progressHeight);
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
		else if(te.getMachineType() == MachineType.ALCHEMITER && par3 == 0 && mc.player.inventory.getItemStack() == null
				&& te.getStackInSlot(0) != null && AlchemyRecipeHandler.getDecodedItem(te.getStackInSlot(0)) != null && AlchemyRecipeHandler.getDecodedItem(te.getStackInSlot(0)).getItem() == MinestuckItems.captchaCard
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
		VertexBuffer render = Tessellator.getInstance().getBuffer();
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