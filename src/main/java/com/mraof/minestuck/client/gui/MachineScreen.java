package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nullable;

/**
 * Created by mraof on 2017 December 07 at 12:55 AM.
 */
public abstract class MachineScreen<T extends MachineContainerMenu> extends AbstractContainerScreen<T>
{
	@Nullable
	protected GoButton goButton;
	
	public MachineScreen(T screenContainer, Inventory inv, Component titleIn)
	{
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(this.getFocused() == null && this.goButton != null && this.goButton.keyPressed(keyCode, scanCode, i))
			return true;
		else
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