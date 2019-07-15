package com.mraof.minestuck.client.gui;

import com.mraof.minestuck.inventory.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;

public class ModScreenManager
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
}