package com.mraof.minestuck.inventory;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSContainerTypes
{
	public static final MenuType<MiniCruxtruderContainer> MINI_CRUXTRUDER = getNull();
	public static final MenuType<MiniTotemLatheContainer> MINI_TOTEM_LATHE = getNull();
	public static final MenuType<MiniAlchemiterContainer> MINI_ALCHEMITER = getNull();
	public static final MenuType<MiniPunchDesignixContainer> MINI_PUNCH_DESIGNIX = getNull();
	public static final MenuType<SendificatorContainer> SENDIFICATOR = getNull();
	public static final MenuType<GristWidgetContainer> GRIST_WIDGET = getNull();
	public static final MenuType<UraniumCookerContainer> URANIUM_COOKER = getNull();
	public static final MenuType<CaptchaDeckContainer> CAPTCHA_DECK = getNull();
	public static final MenuType<EditmodeContainer> EDIT_MODE = getNull();
	public static final MenuType<ConsortMerchantContainer> CONSORT_MERCHANT = getNull();
	
	public static final MenuType<MusicPlayerContainer> MUSIC_PLAYER = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void onRegisterMenuType(RegistryEvent.Register<MenuType<?>> event)
	{
		IForgeRegistry<MenuType<?>> registry = event.getRegistry();
		registry.register(new MenuType<>((IContainerFactory<MiniCruxtruderContainer>) MiniCruxtruderContainer::new).setRegistryName("mini_cruxtruder"));
		registry.register(new MenuType<>((IContainerFactory<MiniTotemLatheContainer>) MiniTotemLatheContainer::new).setRegistryName("mini_totem_lathe"));
		registry.register(new MenuType<>((IContainerFactory<MiniAlchemiterContainer>) MiniAlchemiterContainer::new).setRegistryName("mini_alchemiter"));
		registry.register(new MenuType<>((IContainerFactory<MiniPunchDesignixContainer>) MiniPunchDesignixContainer::new).setRegistryName("mini_punch_designix"));
		registry.register(new MenuType<>((IContainerFactory<SendificatorContainer>) SendificatorContainer::newFromPacket).setRegistryName("sendificator"));
		registry.register(new MenuType<>((IContainerFactory<GristWidgetContainer>) GristWidgetContainer::new).setRegistryName("grist_widget"));
		registry.register(new MenuType<>((IContainerFactory<UraniumCookerContainer>) UraniumCookerContainer::new).setRegistryName("uranium_cooker"));
		registry.register(new MenuType<>(CaptchaDeckContainer::new).setRegistryName("captcha_deck"));
		registry.register(new MenuType<>(EditmodeContainer::new).setRegistryName("edit_mode"));
		registry.register(new MenuType<>((IContainerFactory<ConsortMerchantContainer>) ConsortMerchantContainer::load).setRegistryName("consort_merchant"));
		registry.register(new MenuType<>(MusicPlayerContainer::new).setRegistryName("music_player"));
	}
}
