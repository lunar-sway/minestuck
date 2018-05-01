package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class GristType extends IForgeRegistryEntry.Impl<GristType> implements Comparable<GristType>
{
	public static final GristType Amber = new GristType("amber", 0.5F, new ResourceLocation("minestuck", "amber"));
	public static final GristType Amethyst = new GristType("amethyst", 0.3F, new ResourceLocation("minestuck", "amethyst"));
	public static final GristType Artifact = new GristType("artifact", 0.1F, new ResourceLocation("minestuck", "artifact"));
	public static final GristType Build = new GristType("build", 0.0F, new ResourceLocation("minestuck", "build"));
	public static final GristType Caulk = new GristType("caulk", 0.5F, new ResourceLocation("minestuck", "caulk"));
	public static final GristType Chalk = new GristType("chalk", 0.5F, new ResourceLocation("minestuck", "chalk"));
	public static final GristType Cobalt = new GristType("cobalt", 0.4F, new ResourceLocation("minestuck", "cobalt"));
	public static final GristType Diamond = new GristType("diamond", 0.2F, new ResourceLocation("minestuck", "diamond"));
	public static final GristType Garnet = new GristType("garnet", 0.3F, new ResourceLocation("minestuck", "garnet"));
	public static final GristType Gold = new GristType("gold", 0.2F, new ResourceLocation("minestuck", "gold"));
	public static final GristType Iodine = new GristType("iodine", 0.5F, new ResourceLocation("minestuck", "iodine"));
	public static final GristType Marble = new GristType("marble", 0.4F, new ResourceLocation("minestuck", "marble"));
	public static final GristType Mercury = new GristType("mercury", 0.4F, new ResourceLocation("minestuck", "mercury"));
	public static final GristType Quartz = new GristType("quartz", 0.4F, new ResourceLocation("minestuck", "quartz"));
	public static final GristType Ruby = new GristType("ruby", 0.3F, new ResourceLocation("minestuck", "ruby"));
	public static final GristType Rust = new GristType("rust", 0.3F, new ResourceLocation("minestuck", "rust"));
	public static final GristType Shale = new GristType("shale", 0.5F, new ResourceLocation("minestuck", "shale"));
	public static final GristType Sulfur = new GristType("sulfur", 0.4F, new ResourceLocation("minestuck", "sulfur"));
	public static final GristType Tar = new GristType("tar", 0.5F, new ResourceLocation("minestuck", "tar"));
	public static final GristType Uranium = new GristType("uranium", 0.2F, new ResourceLocation("minestuck", "uranium"));
	public static final GristType Zillium = new GristType("zillium", 0.0F, new ResourceLocation("minestuck", "zillium"));
	public static ForgeRegistry<GristType> REGISTRY;
	final String name;
	final float rarity;
	private final ResourceLocation icon;

	public GristType(String name, float rarity, ResourceLocation icon)
	{
		this.name = name;
		this.rarity = rarity;
		this.icon = icon;
	}

	public static GristType getTypeFromString(String string)
	{
		if (!string.contains(":"))
		{
			string = "minestuck:" + string;
		}
		return REGISTRY.getValue(new ResourceLocation(string));
	}

	public static List<GristType> values()
	{
		return REGISTRY.getValues();
	}

	public String getDisplayName()	//TODO Phase out serverside usage of this method
	{
		return I18n.translateToLocal("grist." + name);
	}

	/**
	 * Returns the grist's full unlocalized name.
	 *
	 * @return the grist's full unlocalized name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns the grist's rarity. Is a number from 0.0 to 1.0.
	 *
	 * @return the rarity
	 */
	public float getRarity()
	{
		return rarity;
	}

	/**
	 * Returns the power level of a underling of a grist's type. Don't call this with grists like Zillium or Build.
	 */
	public float getPower()
	{
		return 1 / rarity;
	}

	public ResourceLocation getIcon()
	{
		return icon;
	}

	public int getId()
	{
		return REGISTRY.getID(this);
	}
	
	@SubscribeEvent
	public static void onRegistryNewRegistry(RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry<GristType>) new RegistryBuilder<GristType>()
				.setName(new ResourceLocation("minestuck", "grist"))
				.setType(GristType.class)
				.addCallback((IForgeRegistry.CreateCallback<GristType>) (owner, stage) -> owner.setSlaveMap(new ResourceLocation("scapecraft:registry"), new RegistryNamespaced<ResourceLocation, GristType>()))
				.create();
	}

	@SubscribeEvent
	public static void registerSkills(RegistryEvent.Register<GristType> event)
	{
		event.getRegistry().registerAll(
				Amber.setRegistryName("minestuck", "amber"),
				Amethyst.setRegistryName("minestuck", "amethyst"),
				Artifact.setRegistryName("minestuck", "artifact"),
				Build.setRegistryName("minestuck", "build"),
				Caulk.setRegistryName("minestuck", "caulk"),
				Chalk.setRegistryName("minestuck", "chalk"),
				Cobalt.setRegistryName("minestuck", "cobalt"),
				Diamond.setRegistryName("minestuck", "diamond"),
				Garnet.setRegistryName("minestuck", "garnet"),
				Gold.setRegistryName("minestuck", "gold"),
				Iodine.setRegistryName("minestuck", "iodine"),
				Marble.setRegistryName("minestuck", "marble"),
				Mercury.setRegistryName("minestuck", "mercury"),
				Quartz.setRegistryName("minestuck", "quartz"),
				Ruby.setRegistryName("minestuck", "ruby"),
				Rust.setRegistryName("minestuck", "rust"),
				Shale.setRegistryName("minestuck", "shale"),
				Sulfur.setRegistryName("minestuck", "sulfur"),
				Tar.setRegistryName("minestuck", "tar"),
				Uranium.setRegistryName("minestuck", "uranium"),
				Zillium.setRegistryName("minestuck", "zillium")
		);
	}

	@Override
	public int compareTo(GristType gristType)
	{
		return this.getId() - gristType.getId();
	}
}

