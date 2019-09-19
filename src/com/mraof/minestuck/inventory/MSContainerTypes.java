package com.mraof.minestuck.inventory;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSContainerTypes
{
	public static final ContainerType<MiniCruxtruderContainer> MINI_CRUXTRUDER = getNull();
	public static final ContainerType<MiniTotemLatheContainer> MINI_TOTEM_LATHE = getNull();
	public static final ContainerType<MiniAlchemiterContainer> MINI_ALCHEMITER = getNull();
	public static final ContainerType<MiniPunchDesignixContainer> MINI_PUNCH_DESIGNIX = getNull();
	public static final ContainerType<GristWidgetContainer> GRIST_WIDGET = getNull();
	public static final ContainerType<UraniumCookerContainer> URANIUM_COOKER = getNull();
	public static final ContainerType<CaptchaDeckContainer> CAPTCHA_DECK = getNull();
	public static final ContainerType<EditmodeContainer> EDIT_MODE = getNull();
	public static final ContainerType<ConsortMerchantContainer> CONSORT_MERCHANT = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void onRegisterContainerType(RegistryEvent.Register<ContainerType<?>> event)
	{
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		registry.register(new ContainerType<>(MiniCruxtruderContainer::new).setRegistryName("mini_cruxtruder"));
		registry.register(new ContainerType<>(MiniTotemLatheContainer::new).setRegistryName("mini_totem_lathe"));
		registry.register(new ContainerType<>(MiniAlchemiterContainer::new).setRegistryName("mini_alchemiter"));
		registry.register(new ContainerType<>(MiniPunchDesignixContainer::new).setRegistryName("mini_punch_designix"));
		registry.register(new ContainerType<>(GristWidgetContainer::new).setRegistryName("grist_widget"));
		registry.register(new ContainerType<>(UraniumCookerContainer::new).setRegistryName("uranium_cooker"));
		registry.register(new ContainerType<>(CaptchaDeckContainer::new).setRegistryName("captcha_deck"));
		registry.register(new ContainerType<>(EditmodeContainer::new).setRegistryName("edit_mode"));
		registry.register(new ContainerType<>(ConsortMerchantContainer::new).setRegistryName("consort_merchant"));
	}
}
