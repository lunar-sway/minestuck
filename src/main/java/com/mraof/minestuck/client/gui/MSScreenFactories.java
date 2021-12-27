package com.mraof.minestuck.client.gui;

import com.google.common.collect.Maps;
import com.mraof.minestuck.client.gui.captchalouge.*;
import com.mraof.minestuck.inventory.MSContainerTypes;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.tileentity.TransportalizerTileEntity;
import com.mraof.minestuck.tileentity.machine.AlchemiterTileEntity;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.redstone.WirelessRedstoneTransmitterTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Map;
import java.util.function.Function;

/**
 * This is the class which registers container type -> screen constructor factories,
 * and this is also the class to hide away all screen display code to prevent standalone server crashes due to references to {@link net.minecraft.client.gui.screen.Screen}
 */
public class MSScreenFactories
{
	private static final Map<ModusType<?>, Function<Modus, ? extends SylladexScreen>> SYLLADEX_FACTORIES = Maps.newHashMap();
	
	public static void registerScreenFactories()
	{
		ScreenManager.register(MSContainerTypes.MINI_CRUXTRUDER, MiniCruxtruderScreen::new);
		ScreenManager.register(MSContainerTypes.MINI_TOTEM_LATHE, MiniTotemLatheScreen::new);
		ScreenManager.register(MSContainerTypes.MINI_ALCHEMITER, MiniAlchemiterScreen::new);
		ScreenManager.register(MSContainerTypes.MINI_PUNCH_DESIGNIX, MiniPunchDesignixScreen::new);
		ScreenManager.register(MSContainerTypes.GRIST_WIDGET, GristWidgetScreen::new);
		ScreenManager.register(MSContainerTypes.URANIUM_COOKER, UraniumCookerScreen::new);
		//ScreenManager.registerFactory(ModContainerTypes.CAPTCHA_DECK, );
		//ScreenManager.registerFactory(ModContainerTypes.EDITMODE, );
		ScreenManager.register(MSContainerTypes.CONSORT_MERCHANT, ConsortShopScreen::new);
		
		registerSylladexFactory(ModusTypes.STACK, StackSylladexScreen::new);
		registerSylladexFactory(ModusTypes.QUEUE, QueueSylladexScreen::new);
		registerSylladexFactory(ModusTypes.QUEUE_STACK, QueuestackSylladexScreen::new);
		registerSylladexFactory(ModusTypes.TREE, TreeSylladexScreen::new);
		registerSylladexFactory(ModusTypes.HASH_MAP, HashMapSylladexScreen::new);
		registerSylladexFactory(ModusTypes.SET, SetSylladexScreen::new);
	}
	
	public static void registerSylladexFactory(ModusType<?> type, Function<Modus, ? extends SylladexScreen> factory)
	{
		SYLLADEX_FACTORIES.put(type, factory);
	}
	
	public static void displayComputerScreen(ComputerTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new ComputerScreen(Minecraft.getInstance(), tileEntity));
	}
	
	public static void displayTransportalizerScreen(TransportalizerTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new TransportalizerScreen(tileEntity));
	}
	
	public static void displayAreaEffectScreen(AreaEffectTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new AreaEffectScreen(tileEntity));
	}
	
	public static void displayWirelessRedstoneTransmitterScreen(WirelessRedstoneTransmitterTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new WirelessRedstoneTransmitterScreen(tileEntity));
	}
	
	public static void displayStatStorerScreen(StatStorerTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new StatStorerScreen(tileEntity));
	}
	
	public static void displayRemoteObserverScreen(RemoteObserverTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new RemoteObserverScreen(tileEntity));
	}
	
	public static void displayAlchemiterScreen(AlchemiterTileEntity tileEntity)
	{
		Minecraft.getInstance().setScreen(new AlchemiterScreen(tileEntity));
	}
	
	public static void displayStoneTabletScreen(PlayerEntity playerIn, Hand handIn, String text, boolean canEdit)
	{
		Minecraft.getInstance().setScreen(new StoneTabletScreen(playerIn, handIn, text, canEdit));
	}
	
	public static void displayTitleSelectScreen(Title title)
	{
		Minecraft.getInstance().setScreen(new TitleSelectorScreen(title));
	}
	
	public static void displaySylladexScreen(Modus modus)
	{
		if(SYLLADEX_FACTORIES.containsKey(modus.getType()))
		{
			SylladexScreen screen = SYLLADEX_FACTORIES.get(modus.getType()).apply(modus);
			Minecraft.getInstance().setScreen(screen);
		}
	}
	
	public static void updateSylladexScreen()
	{
		Screen currentScreen = Minecraft.getInstance().screen;
		if(currentScreen instanceof SylladexScreen)
			((SylladexScreen) currentScreen).updateContent();
	}
}