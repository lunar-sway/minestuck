package com.mraof.minestuck.client.settings;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;

import com.mraof.minestuck.client.gui.GuiGristCache;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class MinestuckKeyHandler extends KeyHandler 
{
    private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
    
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
//		System.out.println(kb.keyDescription);
		if(kb.keyDescription == "key.gristCache")
		{
			GuiGristCache.visible = true;
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) 
	{
		if(kb.keyDescription == "key.gristCache")
		{
			GuiGristCache.visible = false;
		}
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return this.tickTypes;
	}

}
