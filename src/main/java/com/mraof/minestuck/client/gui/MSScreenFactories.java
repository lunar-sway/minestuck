package com.mraof.minestuck.client.gui;

import com.google.common.collect.Maps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.blockentity.TransportalizerBlockEntity;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.blockentity.machine.PunchDesignixBlockEntity;
import com.mraof.minestuck.blockentity.redstone.*;
import com.mraof.minestuck.client.gui.captchalouge.*;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.inventory.MSMenuTypes;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.inventory.captchalogue.ModusTypes;
import com.mraof.minestuck.player.Title;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This is the class which registers container type -> screen constructor factories,
 * and this is also the class to hide away all screen display code to prevent standalone server crashes due to references to {@link net.minecraft.client.gui.screens.Screen}
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID)
public class MSScreenFactories
{
	private static final Map<ModusType<?>, Function<Modus, ? extends SylladexScreen>> SYLLADEX_FACTORIES = Maps.newHashMap();
	
	@SubscribeEvent
	public static void registerScreenFactories(RegisterMenuScreensEvent event)
	{
		event.register(MSMenuTypes.MINI_CRUXTRUDER.get(), MiniCruxtruderScreen::new);
		event.register(MSMenuTypes.MINI_TOTEM_LATHE.get(), MiniTotemLatheScreen::new);
		event.register(MSMenuTypes.MINI_ALCHEMITER.get(), MiniAlchemiterScreen::new);
		event.register(MSMenuTypes.MINI_PUNCH_DESIGNIX.get(), MiniPunchDesignixScreen::new);
		event.register(MSMenuTypes.SENDIFICATOR.get(), SendificatorScreen::new);
		event.register(MSMenuTypes.GRIST_WIDGET.get(), GristWidgetScreen::new);
		event.register(MSMenuTypes.URANIUM_COOKER.get(), UraniumCookerScreen::new);
		event.register(MSMenuTypes.ANTHVIL.get(), AnthvilScreen::new);
		event.register(MSMenuTypes.CONSORT_MERCHANT.get(), ConsortShopScreen::new);
		event.register(MSMenuTypes.CASSETTE_CONTAINER.get(), CassetteContainerScreen::new);
		
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
	
	public static void displayBlockTeleporterScreen(BlockTeleporterBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new BlockTeleporterScreen(blockEntity));
	}
	
	public static void displayAlchemiterScreen(AlchemiterBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new AlchemiterScreen(blockEntity));
	}
	
	public static void displayPunchDesignixScreen(PunchDesignixBlockEntity blockEntity)
	{
		Minecraft.getInstance().setScreen(new PunchDesignixScreen(blockEntity));
	}
	
	public static void displayStoneTabletScreen(Player playerIn, InteractionHand handIn, String text, boolean canEdit)
	{
		Minecraft.getInstance().setScreen(new StoneTabletScreen(playerIn, handIn, text, canEdit));
	}
	
	public static void displayReadableSburbCodeScreen(Set<Block> blockList, boolean paradoxCode)
	{
		Minecraft.getInstance().setScreen(new ReadableSburbCodeScreen(blockList, paradoxCode));
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
	
	public static void displayDialogueScreen(int dialogueId, Dialogue.DialogueData dialogueData)
	{
		Minecraft.getInstance().setScreen(new DialogueScreen(dialogueId, dialogueData));
	}
	
	public static void closeDialogueScreen()
	{
		Minecraft minecraft = Minecraft.getInstance();
		if(minecraft.screen instanceof DialogueScreen)
			minecraft.setScreen(null);
	}
	
	public static void updateSylladexScreen()
	{
		if(Minecraft.getInstance().screen instanceof SylladexScreen screen)
			screen.updateContent();
	}
}