package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Collection;
import java.util.function.Supplier;

public class GristTypes
{
	public static final DeferredRegister<GristType> GRIST_TYPES = DeferredRegister.create(GristType.class, Minestuck.MOD_ID);
	
	private static final Supplier<IForgeRegistry<GristType>> REGISTRY = GRIST_TYPES.makeRegistry("grist", () -> new RegistryBuilder<GristType>().set(DummyFactory.INSTANCE));
	
	public static final RegistryObject<GristType> BUILD = GRIST_TYPES.register("build", () -> new GristType(new GristType.Properties(1.0F, 1).candy(() -> MSItems.BUILD_GUSHERS).notUnderlingType()));
	public static final RegistryObject<GristType> AMBER = GRIST_TYPES.register("amber", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.AMBER_GUMMY_WORM).secondary(GristTypes.RUST).secondary(GristTypes.SULFUR)));
	public static final RegistryObject<GristType> CAULK = GRIST_TYPES.register("caulk", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.CAULK_PRETZEL).secondary(GristTypes.IODINE).secondary(GristTypes.CHALK)));
	public static final RegistryObject<GristType> CHALK = GRIST_TYPES.register("chalk", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.CHALK_CANDY_CIGARETTE).secondary(GristTypes.SHALE).secondary(GristTypes.MARBLE)));
	public static final RegistryObject<GristType> IODINE = GRIST_TYPES.register("iodine", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.IODINE_LICORICE).secondary(GristTypes.AMBER).secondary(GristTypes.CHALK)));
	public static final RegistryObject<GristType> SHALE = GRIST_TYPES.register("shale", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.SHALE_PEEP).secondary(GristTypes.MERCURY).secondary(GristTypes.TAR)));
	public static final RegistryObject<GristType> TAR = GRIST_TYPES.register("tar", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(() -> MSItems.TAR_LICORICE).secondary(GristTypes.AMBER).secondary(GristTypes.COBALT)));
	public static final RegistryObject<GristType> COBALT = GRIST_TYPES.register("cobalt", () -> new GristType(new GristType.Properties(0.4F, 2).candy(() -> MSItems.COBALT_GUM).secondary(GristTypes.RUBY).secondary(GristTypes.AMETHYST)));
	public static final RegistryObject<GristType> MARBLE = GRIST_TYPES.register("marble", () -> new GristType(new GristType.Properties(0.4F, 2).candy(() -> MSItems.MARBLE_JAWBREAKER).secondary(GristTypes.CAULK).secondary(GristTypes.AMETHYST)));
	public static final RegistryObject<GristType> MERCURY = GRIST_TYPES.register("mercury", () -> new GristType(new GristType.Properties(0.4F, 2).candy(() -> MSItems.MERCURY_SIXLETS).secondary(GristTypes.COBALT).secondary(GristTypes.RUST)));
	public static final RegistryObject<GristType> QUARTZ = GRIST_TYPES.register("quartz", () -> new GristType(new GristType.Properties(0.4F, 2).candy(() -> MSItems.QUARTZ_JELLY_BEAN).secondary(GristTypes.MARBLE).secondary(GristTypes.URANIUM)));
	public static final RegistryObject<GristType> SULFUR = GRIST_TYPES.register("sulfur", () -> new GristType(new GristType.Properties(0.4F, 2).candy(() -> MSItems.SULFUR_CANDY_APPLE).secondary(GristTypes.IODINE).secondary(GristTypes.TAR)));
	public static final RegistryObject<GristType> AMETHYST = GRIST_TYPES.register("amethyst", () -> new GristType(new GristType.Properties(0.3F, 3).candy(() -> MSItems.AMETHYST_HARD_CANDY).secondary(GristTypes.QUARTZ).secondary(GristTypes.GARNET)));
	public static final RegistryObject<GristType> GARNET = GRIST_TYPES.register("garnet", () -> new GristType(new GristType.Properties(0.3F, 3).candy(() -> MSItems.GARNET_TWIX).secondary(GristTypes.RUBY).secondary(GristTypes.GOLD)));
	public static final RegistryObject<GristType> RUBY = GRIST_TYPES.register("ruby", () -> new GristType(new GristType.Properties(0.3F, 3).candy(() -> MSItems.RUBY_LOLLIPOP).secondary(GristTypes.QUARTZ).secondary(GristTypes.DIAMOND)));
	public static final RegistryObject<GristType> RUST = GRIST_TYPES.register("rust", () -> new GristType(new GristType.Properties(0.3F, 3).candy(() -> MSItems.RUST_GUMMY_EYE).secondary(GristTypes.SHALE).secondary(GristTypes.GARNET)));
	public static final RegistryObject<GristType> DIAMOND = GRIST_TYPES.register("diamond", () -> new GristType(new GristType.Properties(0.2F, 5).candy(() -> MSItems.DIAMOND_MINT).secondary(GristTypes.GOLD)));
	public static final RegistryObject<GristType> GOLD = GRIST_TYPES.register("gold", () -> new GristType(new GristType.Properties(0.2F, 5).candy(() -> MSItems.GOLD_CANDY_RIBBON).secondary(GristTypes.URANIUM)));
	public static final RegistryObject<GristType> URANIUM = GRIST_TYPES.register("uranium", () -> new GristType(new GristType.Properties(0.2F, 5).candy(() -> MSItems.URANIUM_GUMMY_BEAR).secondary(GristTypes.DIAMOND)));
	public static final RegistryObject<GristType> ARTIFACT = GRIST_TYPES.register("artifact", () -> new GristType(new GristType.Properties(0.1F, 1).candy(() -> MSItems.ARTIFACT_WARHEAD)));
	public static final RegistryObject<GristType> ZILLIUM = GRIST_TYPES.register("zillium", () -> new GristType(new GristType.Properties(0.0F, 10).candy(() -> MSItems.ZILLIUM_SKITTLES).notUnderlingType()));
	
	public static IForgeRegistry<GristType> getRegistry()
	{
		return REGISTRY.get();
	}
	
	public static Collection<GristType> values()
	{
		return getRegistry().getValues();
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