package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSCreativeTabs
{
	public static final String MAIN_KEY = "minestuck.item_group.main";
	public static final String LANDS_KEY = "minestuck.item_group.lands";
	public static final String WEAPONS_KEY = "minestuck.item_group.weapons";
	
	public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minestuck.MOD_ID);
	
	public static final RegistryObject<CreativeModeTab> MAIN = REGISTER.register("main", () -> CreativeModeTab.builder()
			.title(Component.translatable(MAIN_KEY)).icon(() -> new ItemStack(MSItems.CLIENT_DISK.get())).displayItems(MSCreativeTabs::buildMainTab).build());
	public static final RegistryObject<CreativeModeTab> LANDS = REGISTER.register("lands", () -> CreativeModeTab.builder()
			.title(Component.translatable(LANDS_KEY)).icon(() -> new ItemStack(MSItems.GLOWING_MUSHROOM.get())).displayItems(MSCreativeTabs::buildLandsTab).build());
	public static final RegistryObject<CreativeModeTab> WEAPONS = REGISTER.register("weapons", () -> CreativeModeTab.builder()
			.title(Component.translatable(WEAPONS_KEY)).icon(() -> new ItemStack(MSItems.ZILLYHOO_HAMMER.get())).displayItems(MSCreativeTabs::buildWeaponsTab).build());
	
	private static void buildMainTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(MSItems.CRUXITE_APPLE.get());
		//TODO
	}
	
	private static void buildLandsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(MSItems.GLOWING_MUSHROOM.get());
		//TODO
	}
	
	private static void buildWeaponsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
	{
		output.accept(MSItems.ZILLYHOO_HAMMER.get());
		//TODO
	}
}