package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.blockentity.machine.ProgressTracker;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.GoButtonPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;

/**
 * Created by mraof on 2017 December 07 at 12:55 AM.
 */
public abstract class MachineScreen<T extends MachineContainerMenu> extends AbstractContainerScreen<T>
{
	public static final String GO = "minestuck.button.go";
	public static final String STOP = "minestuck.button.stop";
	
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
	
	protected class GoButton extends ExtendedButton
	{
		private static final Component GO_COMPONENT = Component.translatable(GO);
		private static final Component STOP_COMPONENT = Component.translatable(STOP);
		
		public GoButton(int x, int y, int widthIn, int heightIn)
		{
			super(x, y, widthIn, heightIn, null, null);
		}
		
		@Override
		public Component getMessage()
		{
			return menu.isLooping() ? STOP_COMPONENT : GO_COMPONENT;
		}
		
		@Override
		protected boolean isValidClickButton(int mouseKey)	//TODO We probably want to move away from using the right mouse button
		{
			return mouseKey == 0 || mouseKey == 1;
		}
		
		@Override
		public void onPress()
		{
		}
		
		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int mouseKey)
		{
			if (this.active && this.visible) {
				if (this.isValidClickButton(mouseKey)) {
					boolean flag = this.clicked(mouseX, mouseY);
					if (flag) {
						this.playDownSound(Minecraft.getInstance().getSoundManager());
						this.onClick(mouseKey);
						return true;
					}
				}
				
				return false;
			} else {
				return false;
			}
		}
		
		private void onClick(int mouseKey)
		{
			if(mouseKey == GLFW.GLFW_MOUSE_BUTTON_1)
			{
				if(!menu.isLooping())
				{
					//Tell the machine to go once
					MSPacketHandler.sendToServer(new GoButtonPacket(true, false));
				}
			} else if(mouseKey == GLFW.GLFW_MOUSE_BUTTON_2 && runType == ProgressTracker.RunType.ONCE_OR_LOOPING)
			{
				//Tell the machine to go until stopped
				MSPacketHandler.sendToServer(new GoButtonPacket(true, !menu.isLooping()));
			}
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