package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.ThemeSelectPacket;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class SettingsApp extends ButtonListProgram
{
	public static final String THEME = "minestuck.program.settings.theme";
	public static final String TITLE = "minestuck.program.settings.title";
	
	public static final ResourceLocation ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/settings.png");
	
	@Override
	protected ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be)
	{
		var list = new ArrayList<UnlocalizedString>();
		
		list.add(new UnlocalizedString(TITLE));
		list.add(new UnlocalizedString(THEME, I18n.get(be.getTheme().getName())));
		
		return list;
	}
	
	@Override
	protected void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data)
	{
		//TODO ADD MORE SETTINGS
		switch(buttonName)
		{
			case THEME -> MSPacketHandler.sendToServer(ThemeSelectPacket.create(be, be.getTheme().next()));
		}
	}
	
	@Override
	public ResourceLocation getIcon()
	{
		return ICON;
	}
}
