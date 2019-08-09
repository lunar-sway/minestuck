package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ModContainerTypes;
import com.mraof.minestuck.tileentity.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import com.mraof.minestuck.util.Title;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;

/**
 * This is the class which registers container type -> screen constructor factories,
 * and this is also the class to hide away all screen display code to prevent standalone server crashes due to references to {@link net.minecraft.client.gui.screen.Screen}
 */
public class ModScreenFactories
{
	public static void registerScreenFactories()
	{
		ScreenManager.registerFactory(ModContainerTypes.MINI_CRUXTRUDER, MiniCruxtruderScreen::new);
		ScreenManager.registerFactory(ModContainerTypes.MINI_TOTEM_LATHE, MiniTotemLatheScreen::new);
		ScreenManager.registerFactory(ModContainerTypes.MINI_ALCHEMITER, MiniAlchemiterScreen::new);
		ScreenManager.registerFactory(ModContainerTypes.MINI_PUNCH_DESIGNIX, MiniPunchDesignixScreen::new);
		ScreenManager.registerFactory(ModContainerTypes.GRIST_WIDGET, GristWidgetScreen::new);
		ScreenManager.registerFactory(ModContainerTypes.URANIUM_COOKER, UraniumCookerScreen::new);
		//ScreenManager.registerFactory(ModContainerTypes.CAPTCHA_DECK, );
		//ScreenManager.registerFactory(ModContainerTypes.EDITMODE, );
		ScreenManager.registerFactory(ModContainerTypes.CONSORT_MERCHANT, ConsortShopScreen::new);
	}
	
	public static void displayComputerScreen(ComputerTileEntity tileEntity)
	{
		Minecraft.getInstance().displayGuiScreen(new ComputerScreen(Minecraft.getInstance(), tileEntity));
	}
	
	public static void displayTransportalizerScreen(TransportalizerTileEntity tileEntity)
	{
		Minecraft.getInstance().displayGuiScreen(new TransportalizerScreen(tileEntity));
	}
	
	public static void displayAlchemiterScreen(AlchemiterTileEntity tileEntity)
	{
		Minecraft.getInstance().displayGuiScreen(new AlchemiterScreen(tileEntity));
	}
	
	public static void displayTitleSelectScreen(Title title)
	{
		Minecraft.getInstance().displayGuiScreen(new TitleSelectorScreen(title));
	}
}