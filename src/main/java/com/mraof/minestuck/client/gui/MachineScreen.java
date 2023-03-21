package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.machine.ProgressTracker;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.GoButtonPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Created by mraof on 2017 December 07 at 12:55 AM.
 */
public abstract class MachineScreen<T extends MachineContainerMenu> extends AbstractContainerScreen<T>
{
	protected final ProgressTracker.RunType runType;
	@Nullable
	protected GoButton goButton;
	
	public MachineScreen(ProgressTracker.RunType runType, T screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
		this.runType = runType;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)
		{
			this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

			boolean mode = runType == ProgressTracker.RunType.ONCE_OR_LOOPING && hasShiftDown();
			MSPacketHandler.sendToServer(new GoButtonPacket(true, mode && !menu.isLooping()));
			return true;
		}
		return super.keyPressed(keyCode, scanCode, i);
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