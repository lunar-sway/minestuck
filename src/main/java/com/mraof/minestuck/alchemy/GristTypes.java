package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.function.Supplier;

public class GristTypes
{
	public static final DeferredRegister<GristType> GRIST_TYPES = DeferredRegister.create(new ResourceLocation(Minestuck.MOD_ID, "grist"), Minestuck.MOD_ID);
	
	private static final Supplier<IForgeRegistry<GristType>> REGISTRY = GRIST_TYPES.makeRegistry(() -> new RegistryBuilder<GristType>().set(DummyFactory.INSTANCE).hasTags());
	
	public static final RegistryObject<GristType> BUILD = GRIST_TYPES.register("build", () -> new GristType(new GristType.Properties(1.0F, 1).candy(MSItems.BUILD_GUSHERS).notUnderlingType()));
	public static final RegistryObject<GristType> AMBER = GRIST_TYPES.register("amber", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.AMBER_GUMMY_WORM).color(0xe68600)));
	public static final RegistryObject<GristType> CAULK = GRIST_TYPES.register("caulk", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.CAULK_PRETZEL).color(0x797979)));
	public static final RegistryObject<GristType> CHALK = GRIST_TYPES.register("chalk", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.CHALK_CANDY_CIGARETTE).color(0xebebeb)));
	public static final RegistryObject<GristType> IODINE = GRIST_TYPES.register("iodine", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.IODINE_LICORICE).color(0x4f4c79)));
	public static final RegistryObject<GristType> SHALE = GRIST_TYPES.register("shale", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.SHALE_PEEP).color(0x202027)));
	public static final RegistryObject<GristType> TAR = GRIST_TYPES.register("tar", () -> new GristType(new GristType.Properties(0.5F, 1.5F).candy(MSItems.TAR_LICORICE).color(0x000000)));
	public static final RegistryObject<GristType> COBALT = GRIST_TYPES.register("cobalt", () -> new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.COBALT_GUM).color(0x0a4bba)));
	public static final RegistryObject<GristType> MARBLE = GRIST_TYPES.register("marble", () -> new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.MARBLE_JAWBREAKER).color(0xffffff)));
	public static final RegistryObject<GristType> MERCURY = GRIST_TYPES.register("mercury", () -> new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.MERCURY_SIXLETS).color(0xb1adad)));
	public static final RegistryObject<GristType> QUARTZ = GRIST_TYPES.register("quartz", () -> new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.QUARTZ_JELLY_BEAN).color(0xd0e1ff)));
	public static final RegistryObject<GristType> SULFUR = GRIST_TYPES.register("sulfur", () -> new GristType(new GristType.Properties(0.4F, 2).candy(MSItems.SULFUR_CANDY_APPLE).color(0xe6f62a)));
	public static final RegistryObject<GristType> AMETHYST = GRIST_TYPES.register("amethyst", () -> new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.AMETHYST_HARD_CANDY).color(0x7c32b4)));
	public static final RegistryObject<GristType> GARNET = GRIST_TYPES.register("garnet", () -> new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.GARNET_TWIX).color(0x831e2d)));
	public static final RegistryObject<GristType> RUBY = GRIST_TYPES.register("ruby", () -> new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.RUBY_LOLLIPOP).color(0xfe0127)));
	public static final RegistryObject<GristType> RUST = GRIST_TYPES.register("rust", () -> new GristType(new GristType.Properties(0.3F, 3).candy(MSItems.RUST_GUMMY_EYE).color(0x4f0716)));
	public static final RegistryObject<GristType> DIAMOND = GRIST_TYPES.register("diamond", () -> new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.DIAMOND_MINT).color(0xffffff)));
	public static final RegistryObject<GristType> GOLD = GRIST_TYPES.register("gold", () -> new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.GOLD_CANDY_RIBBON).color(0xffbf00)));
	public static final RegistryObject<GristType> URANIUM = GRIST_TYPES.register("uranium", () -> new GristType(new GristType.Properties(0.2F, 5).candy(MSItems.URANIUM_GUMMY_BEAR).color(0x14e130)));
	public static final RegistryObject<GristType> ARTIFACT = GRIST_TYPES.register("artifact", () -> new GristType(new GristType.Properties(0.1F, 1).candy(MSItems.ARTIFACT_WARHEAD).color(0xffffff)));
	public static final RegistryObject<GristType> ZILLIUM = GRIST_TYPES.register("zillium", () -> new GristType(new GristType.Properties(0.0F, 10).candy(MSItems.ZILLIUM_SKITTLES).notUnderlingType().color(0xffffff)));
	
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
			return new GristType.DummyType();
		}
	}
}