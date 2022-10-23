package com.mraof.minestuck.client.gui;

import com.google.common.collect.Maps;
import com.mraof.minestuck.client.gui.captchalouge.*;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.blockentity.redstone.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is the class which registers container type -> screen constructor factories,
 * and this is also the class to hide away all screen display code to prevent standalone server crashes due to references to {@link net.minecraft.client.gui.screens.Screen}
 */
public class MSScreenFactories
{
	private static final Map<ModusType<?>, Function<Modus, ? extends SylladexScreen>> SYLLADEX_FACTORIES = Maps.newHashMap();
	
	public static void registerScreenFactories()
	{
		MenuScreens.register(MSMenuTypes.MINI_CRUXTRUDER.get(), MiniCruxtruderScreen::new);
		MenuScreens.register(MSMenuTypes.MINI_TOTEM_LATHE.get(), MiniTotemLatheScreen::new);
		MenuScreens.register(MSMenuTypes.MINI_ALCHEMITER.get(), MiniAlchemiterScreen::new);
		MenuScreens.register(MSMenuTypes.MINI_PUNCH_DESIGNIX.get(), MiniPunchDesignixScreen::new);
		MenuScreens.register(MSMenuTypes.SENDIFICATOR.get(), SendificatorScreen::new);
		MenuScreens.register(MSMenuTypes.GRIST_WIDGET.get(), GristWidgetScreen::new);
		MenuScreens.register(MSMenuTypes.URANIUM_COOKER.get(), UraniumCookerScreen::new);
		MenuScreens.register(MSMenuTypes.CONSORT_MERCHANT.get(), ConsortShopScreen::new);
		
		registerSylladexFactory(ModusTypes.STACK, StackSylladexScreen::new);
		registerSylladexFactory(ModusTypes.QUEUE, QueueSylladexScreen::new);
		registerSylladexFactory(ModusTypes.QUEUE_STACK, QueuestackSylladexScreen::new);
		registerSylladexFactory(ModusTypes.TREE, TreeSylladexScreen::new);
		registerSylladexFactory(ModusTypes.HASH_MAP, HashMapSylladexScreen::new);
		registerSylladexFactory(ModusTypes.SET, SetSylladexScreen::new);
	}
	
	public static void registerSylladexFactory(Supplier<? extends ModusType<?>> type, Function<Modus, ? extends SylladexScreen> factory)
	{
		registerSylladexFactory(type.get(), factory);
	}
	
	public static void registerSylladexFactory(ModusType<?> type, Function<Modus, ? extends SylladexScreen> factory)
	{
		SYLLADEX_FACTORIES.put(type, factory);
	}
	
	public static void displayComputerScreen(ComputerBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new ComputerScreen(Minecraft.getInstance(), blockEntity));
	}
	
	public static void displayTransportalizerScreen(TransportalizerBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new TransportalizerScreen(blockEntity));
	}
	
	public static void displayAreaEffectScreen(AreaEffectBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new AreaEffectScreen(blockEntity));
	}
	
	public static void displayWirelessRedstoneTransmitterScreen(WirelessRedstoneTransmitterBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new WirelessRedstoneTransmitterScreen(blockEntity));
	}
	
	public static void displayStatStorerScreen(StatStorerBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new StatStorerScreen(blockEntity));
	}
	
	public static void displayRemoteObserverScreen(RemoteObserverBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new RemoteObserverScreen(blockEntity));
	}
	
	public static void displaySummonerScreen(SummonerBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new SummonerScreen(blockEntity));
	}
	
	public static void displayStructureCoreScreen(StructureCoreBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new StructureCoreScreen(blockEntity));
	}
	
	public static void displayAlchemiterScreen(AlchemiterBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new AlchemiterScreen(blockEntity));
	}
	
	public static void displayStoneTabletScreen(Player playerIn, InteractionHand handIn, String text, boolean canEdit)
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
		if(Minecraft.getInstance().screen instanceof SylladexScreen screen)
			screen.updateContent();
	}
}