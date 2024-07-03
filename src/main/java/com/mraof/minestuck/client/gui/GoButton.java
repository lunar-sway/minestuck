package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.block.MachinePackets;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@MethodsReturnNonnullByDefault
public class GoButton extends ExtendedButton
{
	public static final String GO = "minestuck.button.go";
	public static final String STOP = "minestuck.button.stop";
	private static final Component GO_COMPONENT = Component.translatable(GO);
	private static final Component STOP_COMPONENT = Component.translatable(STOP);
	
	private final MachineContainerMenu menu;
	private final boolean allowsLooping;
	
	public GoButton(int x, int y, int widthIn, int heightIn, MachineContainerMenu menu, boolean allowsLooping)
	{
		super(x, y, widthIn, heightIn, null, null);
		this.menu = menu;
		this.allowsLooping = allowsLooping;
	}
	
	@Override
	public Component getMessage()
	{
		return menu.isRunning() ? STOP_COMPONENT : GO_COMPONENT;
	}
	
	@Override
	protected boolean isValidClickButton(int mouseKey)    //TODO We probably want to move away from using the right mouse button
	{
		return mouseKey == GLFW.GLFW_MOUSE_BUTTON_1 || mouseKey == GLFW.GLFW_MOUSE_BUTTON_2 && allowsLooping;
	}
	
	@Override
	public void onPress()
	{
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseKey)
	{
		if(this.active && this.visible)
		{
			if(this.isValidClickButton(mouseKey))
			{
				boolean flag = this.clicked(mouseX, mouseY);
				if(flag)
				{
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					this.onClick(mouseKey);
					return true;
				}
			}
			
			return false;
		} else
		{
			return false;
		}
	}
	
	private void onClick(int mouseKey)
	{
		if(mouseKey == GLFW.GLFW_MOUSE_BUTTON_1)
			onRegularClick();
		else if(mouseKey == GLFW.GLFW_MOUSE_BUTTON_2 && allowsLooping)
			onLoopClick();
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if(this.active && this.visible && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER))
		{
			this.playDownSound(Minecraft.getInstance().getSoundManager());
			
			if(allowsLooping && Screen.hasShiftDown())
				onLoopClick();
			else
				onRegularClick();
			return true;
		}
		return false;
	}
	
	private void onRegularClick()
	{
		PacketDistributor.SERVER.noArg().send(new MachinePackets.SetRunning(!this.menu.isRunning()));
	}
	
	private void onLoopClick()
	{
		PacketDistributor.SERVER.noArg().send(new MachinePackets.SetLooping(!this.menu.isLooping()));
	}
}
