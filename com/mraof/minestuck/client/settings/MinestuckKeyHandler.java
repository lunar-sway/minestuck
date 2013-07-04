package com.mraof.minestuck.client.settings;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import com.mraof.minestuck.client.gui.GuiGristCache;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class MinestuckKeyHandler extends KeyHandler 
{
    private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
    boolean gristHasBeenPressed = false;
    
	public MinestuckKeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) 
	{
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel()
	{
		return "MinestuckKeyHandler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) 
	{
		if(kb.keyDescription == "key.gristCache")
		{
			if(!gristHasBeenPressed )
			{
				if(Minecraft.getMinecraft().currentScreen == null)
					Minecraft.getMinecraft().displayGuiScreen(new GuiGristCache(Minecraft.getMinecraft()));
				else if(Minecraft.getMinecraft().currentScreen instanceof GuiGristCache)
					Minecraft.getMinecraft().displayGuiScreen(null);
				gristHasBeenPressed = true;
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) 
	{
		gristHasBeenPressed = false;
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return this.tickTypes;
	}

}
