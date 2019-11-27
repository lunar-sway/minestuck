package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import java.util.Collection;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class GristTypes
{
	public static IForgeRegistry<GristType> REGISTRY;
	
	public static final GristType BUILD = getNull();
	public static final GristType AMBER = getNull();
	public static final GristType CAULK = getNull();
	public static final GristType CHALK = getNull();
	public static final GristType IODINE = getNull();
	public static final GristType SHALE = getNull();
	public static final GristType TAR = getNull();
	public static final GristType COBALT = getNull();
	public static final GristType MARBLE = getNull();
	public static final GristType MERCURY = getNull();
	public static final GristType QUARTZ = getNull();
	public static final GristType SULFUR = getNull();
	public static final GristType AMETHYST = getNull();
	public static final GristType GARNET = getNull();
	public static final GristType RUBY = getNull();
	public static final GristType RUST = getNull();
	public static final GristType DIAMOND = getNull();
	public static final GristType GOLD = getNull();
	public static final GristType URANIUM = getNull();
	public static final GristType ARTIFACT = getNull();
	public static final GristType ZILLIUM = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	
	public static GristType getTypeFromString(String string)
	{
		if (!string.contains(":"))
		{
			string = Minestuck.MOD_ID + ":" + string;
		}
		return REGISTRY.getValue(new ResourceLocation(string));
	}
	
	public static Collection<GristType> values()
	{
		return REGISTRY.getValues();
	}
	
	@SubscribeEvent
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		REGISTRY = new RegistryBuilder<GristType>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "grist"))
				.setType(GristType.class)
				.set(DummyFactory.INSTANCE)
				.create();
	}
	
	@SubscribeEvent
	public static void registerGrist(final RegistryEvent.Register<GristType> event)
	{
		event.getRegistry().registerAll(
				new GristType(0.0F, 1).setCandyItem(new ItemStack(MSItems.BUILD_GUSHERS)).setRegistryName("build"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.AMBER_GUMMY_WORM)).setRegistryName("amber"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.CAULK_PRETZEL)).setRegistryName("caulk"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.CHALK_CANDY_CIGARETTE)).setRegistryName("chalk"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.IODINE_LICORICE)).setRegistryName("iodine"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.SHALE_PEEP)).setRegistryName("shale"),
				new GristType(0.5F, 1.5F).setCandyItem(new ItemStack(MSItems.TAR_LICORICE)).setRegistryName("tar"),
				new GristType(0.4F, 2).setCandyItem(new ItemStack(MSItems.COBALT_GUM)).setRegistryName("cobalt"),
				new GristType(0.4F, 2).setCandyItem(new ItemStack(MSItems.MARBLE_JAWBREAKER)).setRegistryName("marble"),
				new GristType(0.4F, 2).setCandyItem(new ItemStack(MSItems.MERCURY_SIXLETS)).setRegistryName("mercury"),
				new GristType(0.4F, 2).setCandyItem(new ItemStack(MSItems.QUARTZ_JELLY_BEAN)).setRegistryName("quartz"),
				new GristType(0.4F, 2).setCandyItem(new ItemStack(MSItems.SULFUR_CANDY_APPLE)).setRegistryName("sulfur"),
				new GristType(0.3F, 3).setCandyItem(new ItemStack(MSItems.AMETHYST_HARD_CANDY)).setRegistryName("amethyst"),
				new GristType(0.3F, 3).setCandyItem(new ItemStack(MSItems.GARNET_TWIX)).setRegistryName("garnet"),
				new GristType(0.3F, 3).setCandyItem(new ItemStack(MSItems.RUBY_LOLLIPOP)).setRegistryName("ruby"),
				new GristType(0.3F, 3).setCandyItem(new ItemStack(MSItems.RUST_GUMMY_EYE)).setRegistryName("rust"),
				new GristType(0.2F, 5).setCandyItem(new ItemStack(MSItems.DIAMOND_MINT)).setRegistryName("diamond"),
				new GristType(0.2F, 5).setCandyItem(new ItemStack(MSItems.GOLD_CANDY_RIBBON)).setRegistryName("gold"),
				new GristType(0.2F, 5).setCandyItem(new ItemStack(MSItems.URANIUM_GUMMY_BEAR)).setRegistryName("uranium"),
				new GristType(0.1F, 1).setCandyItem(new ItemStack(MSItems.ARTIFACT_WARHEAD)).setRegistryName("artifact"),
				new GristType(0.0F, 10).setCandyItem(new ItemStack(MSItems.ZILLIUM_SKITTLES)).setRegistryName("zillium")
		);
	}
	
	private static class DummyFactory implements IForgeRegistry.DummyFactory<GristType>
	{
		private static final DummyFactory INSTANCE = new DummyFactory();
		
		@Override
		public GristType createDummy(ResourceLocation key)
		{
			return new GristType.DummyType().setRegistryName(key);
		}
	}
}