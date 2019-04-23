package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityMachineProcess;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by mraof on 2017 December 07 at 12:55 AM.
 */
public abstract class GuiMachine extends GuiContainer
{
	private TileEntityMachineProcess te;
	protected GuiButton goButton;
	
	public GuiMachine(Container inventorySlotsIn, TileEntityMachineProcess tileEntity)
	{
		super(inventorySlotsIn);
		this.te = tileEntity;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);

		if (keyCode == 28)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));

			boolean mode = te.allowOverrideStop() && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, mode && !te.overrideStop);
			MinestuckChannelHandler.sendToServer(packet);

			if (!mode)
				te.ready = true;
			te.overrideStop = mode && !te.overrideStop;
			goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
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
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, false);
				MinestuckChannelHandler.sendToServer(packet);

				te.ready = true;
				te.overrideStop = false;
				goButton.displayString = I18n.format("gui.buttonGo");
			}
			else if (Mouse.getEventButton() == 1 && te.allowOverrideStop())
			{
				//Tell the machine to go until stopped
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.GOBUTTON, true, !te.overrideStop);
				MinestuckChannelHandler.sendToServer(packet);

				te.overrideStop = !te.overrideStop;
				goButton.displayString = I18n.format(te.overrideStop ? "gui.buttonStop" : "gui.buttonGo");
			}
		}
	}
}
