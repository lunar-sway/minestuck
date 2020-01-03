package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
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
	
	@SuppressWarnings("unused")	//Should be used by commands
	public static GristType getTypeForCommand(String string)
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
				new GristType(new GristType.Properties(0.0F, 1).candy(MSItems.BUILD_GUSHERS)).setRegistryName("build"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.AMBER_GUMMY_WORM).secondary(() -> RUST).secondary(() -> SULFUR)).setRegistryName("amber"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.CAULK_PRETZEL).secondary(() -> IODINE).secondary(() -> CHALK)).setRegistryName("caulk"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.CHALK_CANDY_CIGARETTE).secondary(() -> SHALE).secondary(() -> MARBLE)).setRegistryName("chalk"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.IODINE_LICORICE).secondary(() -> AMBER).secondary(() -> CHALK)).setRegistryName("iodine"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.SHALE_PEEP).secondary(() -> MERCURY).secondary(() -> TAR)).setRegistryName("shale"),
				new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.TAR_LICORICE).secondary(() -> AMBER).secondary(() -> COBALT)).setRegistryName("tar"),
				new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.COBALT_GUM).secondary(() -> RUBY).secondary(() -> AMETHYST)).setRegistryName("cobalt"),
				new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.MARBLE_JAWBREAKER).secondary(() -> CAULK).secondary(() -> AMETHYST)).setRegistryName("marble"),
				new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.MERCURY_SIXLETS).secondary(() -> COBALT).secondary(() -> RUST)).setRegistryName("mercury"),
				new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.QUARTZ_JELLY_BEAN).secondary(() -> MARBLE).secondary(() -> URANIUM)).setRegistryName("quartz"),
				new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.SULFUR_CANDY_APPLE).secondary(() -> IODINE).secondary(() -> TAR)).setRegistryName("sulfur"),
				new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.AMETHYST_HARD_CANDY).secondary(() -> QUARTZ).secondary(() -> GARNET)).setRegistryName("amethyst"),
				new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.GARNET_TWIX).secondary(() -> RUBY).secondary(() -> GOLD)).setRegistryName("garnet"),
				new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.RUBY_LOLLIPOP).secondary(() -> QUARTZ).secondary(() -> DIAMOND)).setRegistryName("ruby"),
				new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.RUST_GUMMY_EYE).secondary(() -> SHALE).secondary(() -> GARNET)).setRegistryName("rust"),
				new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.DIAMOND_MINT).secondary(() -> GOLD)).setRegistryName("diamond"),
				new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.GOLD_CANDY_RIBBON).secondary(() -> URANIUM)).setRegistryName("gold"),
				new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.URANIUM_GUMMY_BEAR).secondary(() -> DIAMOND)).setRegistryName("uranium"),
				new GristType(new GristType.Properties(0.1F, 1).candy(MSItems.ARTIFACT_WARHEAD)).setRegistryName("artifact"),
				new GristType(new GristType.Properties(0.0F, 10).candy(MSItems.ZILLIUM_SKITTLES)).setRegistryName("zillium")
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