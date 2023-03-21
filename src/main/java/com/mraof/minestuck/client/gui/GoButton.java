package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.GoButtonPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ExtendedButton;
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
		return menu.isLooping() ? STOP_COMPONENT : GO_COMPONENT;
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
		{
			if(!menu.isLooping())
			{
				//Tell the machine to go once
				MSPacketHandler.sendToServer(new GoButtonPacket(true, false));
			}
		} else if(mouseKey == GLFW.GLFW_MOUSE_BUTTON_2 && allowsLooping)
		{
			//Tell the machine to go until stopped
			MSPacketHandler.sendToServer(new GoButtonPacket(true, !menu.isLooping()));
		}
	}
}
