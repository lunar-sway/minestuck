package com.mraof.minestuck.inventory;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import net.minecraft.inventory.container.ContainerType;

public class ModContainerTypes
{
	public static final ContainerType<MiniCruxtruderContainer> MINI_CRUXTRUDER = new ContainerType<>(MiniCruxtruderContainer::new);
	public static final ContainerType<MiniTotemLatheContainer> MINI_TOTEM_LATHE = new ContainerType<>(MiniTotemLatheContainer::new);
	public static final ContainerType<MiniAlchemiterContainer> MINI_ALCHEMITER = new ContainerType<>(MiniAlchemiterContainer::new);
	public static final ContainerType<MiniPunchDesignixContainer> MINI_PUNCH_DESIGNIX = new ContainerType<>(MiniPunchDesignixContainer::new);
	public static final ContainerType<GristWidgetContainer> GRIST_WIDGET = new ContainerType<>(GristWidgetContainer::new);
	public static final ContainerType<UraniumCookerContainer> URANIUM_COOKER = new ContainerType<>(UraniumCookerContainer::new);
	public static final ContainerType<CaptchaDeckContainer> CAPTCHA_DECK = new ContainerType<>(CaptchaDeckContainer::new);
	public static final ContainerType<EditmodeContainer> EDITMODE = new ContainerType<>(EditmodeContainer::new);
	public static final ContainerType<ConsortMerchantContainer> CONSORT_MERCHANT = new ContainerType<>(ConsortMerchantContainer::new);
	
}
