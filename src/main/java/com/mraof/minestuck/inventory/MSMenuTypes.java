package com.mraof.minestuck.inventory;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
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
public class MSMenuTypes
{
	public static final MenuType<MiniCruxtruderMenu> MINI_CRUXTRUDER = getNull();
	public static final MenuType<MiniTotemLatheMenu> MINI_TOTEM_LATHE = getNull();
	public static final MenuType<MiniAlchemiterMenu> MINI_ALCHEMITER = getNull();
	public static final MenuType<MiniPunchDesignixMenu> MINI_PUNCH_DESIGNIX = getNull();
	public static final MenuType<SendificatorMenu> SENDIFICATOR = getNull();
	public static final MenuType<GristWidgetMenu> GRIST_WIDGET = getNull();
	public static final MenuType<UraniumCookerMenu> URANIUM_COOKER = getNull();
	public static final MenuType<CaptchaDeckMenu> CAPTCHA_DECK = getNull();
	public static final MenuType<EditmodeMenu> EDIT_MODE = getNull();
	public static final MenuType<ConsortMerchantMenu> CONSORT_MERCHANT = getNull();
	
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
		registry.register(new MenuType<>((IContainerFactory<MiniCruxtruderMenu>) MiniCruxtruderMenu::new).setRegistryName("mini_cruxtruder"));
		registry.register(new MenuType<>((IContainerFactory<MiniTotemLatheMenu>) MiniTotemLatheMenu::new).setRegistryName("mini_totem_lathe"));
		registry.register(new MenuType<>((IContainerFactory<MiniAlchemiterMenu>) MiniAlchemiterMenu::new).setRegistryName("mini_alchemiter"));
		registry.register(new MenuType<>((IContainerFactory<MiniPunchDesignixMenu>) MiniPunchDesignixMenu::new).setRegistryName("mini_punch_designix"));
		registry.register(new MenuType<>((IContainerFactory<SendificatorMenu>) SendificatorMenu::newFromPacket).setRegistryName("sendificator"));
		registry.register(new MenuType<>((IContainerFactory<GristWidgetMenu>) GristWidgetMenu::new).setRegistryName("grist_widget"));
		registry.register(new MenuType<>((IContainerFactory<UraniumCookerMenu>) UraniumCookerMenu::new).setRegistryName("uranium_cooker"));
		registry.register(new MenuType<>(CaptchaDeckMenu::new).setRegistryName("captcha_deck"));
		registry.register(new MenuType<>(EditmodeMenu::new).setRegistryName("edit_mode"));
		registry.register(new MenuType<>((IContainerFactory<ConsortMerchantMenu>) ConsortMerchantMenu::load).setRegistryName("consort_merchant"));
	}
}
