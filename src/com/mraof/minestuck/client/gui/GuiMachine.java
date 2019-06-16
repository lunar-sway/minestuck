package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachineProcess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.config.GuiButtonExt;

/**
 * Created by mraof on 2017 December 07 at 12:55 AM.
 */
@OnlyIn(Dist.CLIENT)
public abstract class GuiMachine extends GuiContainer
{
	private TileEntityMachineProcess te;
	protected GoButton goButton;
	
	public GuiMachine(Container inventorySlotsIn, TileEntityMachineProcess tileEntity)
	{
		super(inventorySlotsIn);
		this.te = tileEntity;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == 28)
		{
			this.mc.getSoundHandler().play(SimpleSound.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

			boolean mode = te.getRunType() == TileEntityMachineProcess.RunType.BUTTON_OVERRIDE && (InputMappings.isKeyDown(42) || InputMappings.isKeyDown(54));
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, mode && !te.overrideStop);
			MinestuckPacketHandler.sendToServer(packet);

			if (!mode)
				te.ready = true;
			te.overrideStop = mode && !te.overrideStop;
			goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			return true;
		}
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	protected class GoButton extends GuiButtonExt
	{
		public GoButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
		{
			super(buttonId, x, y, widthIn, heightIn, buttonText);
		}
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int mouseKey)
		{
			if(!isPressable(mouseX, mouseY))
				return false;
			if(mouseKey == 0)
			{
				this.playPressSound(Minecraft.getInstance().getSoundHandler());
				if(!te.overrideStop)
				{
					//Tell the machine to go once
					MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, false);
					MinestuckPacketHandler.sendToServer(packet);
					
					te.ready = true;
					te.overrideStop = false;
					goButton.displayString = I18n.format("gui.buttonGo");
				}
				return true;
			} else if(mouseKey == 1 && te.getRunType() == TileEntityMachineProcess.RunType.BUTTON_OVERRIDE)
			{
				this.playPressSound(Minecraft.getInstance().getSoundHandler());
				//Tell the machine to go until stopped
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, !te.overrideStop);
				MinestuckPacketHandler.sendToServer(packet);
				
				te.overrideStop = !te.overrideStop;
				goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Returns a number to be used in calculation of progress bar length.
	 *
	 * @param progress the progress done.
	 * @param max      The maximum amount of progress.
	 * @param imageMax The length of the progress bar image to scale to
	 * @return The length the progress bar should be shown to
	 */
	public static int getScaledValue(int progress, int max, int imageMax)
	{
		return (int) ((float) imageMax * ((float) progress / (float) max));
	}
}
