package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerThemeScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SettingsApp extends ButtonListProgram
{
	public static final String THEME = "minestuck.program.settings.theme";
	public static final String TITLE = "minestuck.program.settings.title";
	
	public static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/gui/desktop_icon/settings.png");
	
	@Override
	protected InterfaceData getInterfaceData(ComputerBlockEntity be)
	{
		return new InterfaceData(new UnlocalizedString(TITLE), List.of(new UnlocalizedString(THEME)));
	}
	
	@Override
	protected void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		if(be.getLevel() == null)
			return;
		
		//TODO ADD MORE SETTINGS
		switch(buttonName)
		{
			case THEME -> {
				be.gui.getMinecraft().setScreen(null);
				be.gui.getMinecraft().setScreen(new ComputerThemeScreen(be));
			}
		}
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}
