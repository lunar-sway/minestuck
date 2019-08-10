package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class GristType extends ForgeRegistryEntry<GristType> implements Comparable<GristType>
{
	public static final GristType BUILD = new GristType("build", 0.0F, 1, new ResourceLocation("minestuck", "build"));
	public static final GristType AMBER = new GristType("amber", 0.5F, 1.5F, new ResourceLocation("minestuck", "amber"));
	public static final GristType CAULK = new GristType("caulk", 0.5F, 1.5F, new ResourceLocation("minestuck", "caulk"));
	public static final GristType CHALK = new GristType("chalk", 0.5F, 1.5F, new ResourceLocation("minestuck", "chalk"));
	public static final GristType IODINE = new GristType("iodine", 0.5F, 1.5F, new ResourceLocation("minestuck", "iodine"));
	public static final GristType SHALE = new GristType("shale", 0.5F, 1.5F, new ResourceLocation("minestuck", "shale"));
	public static final GristType TAR = new GristType("tar", 0.5F, 1.5F, new ResourceLocation("minestuck", "tar"));
	public static final GristType COBALT = new GristType("cobalt", 0.4F, 2, new ResourceLocation("minestuck", "cobalt"));
	public static final GristType MARBLE = new GristType("marble", 0.4F, 2, new ResourceLocation("minestuck", "marble"));
	public static final GristType MERCURY = new GristType("mercury", 0.4F, 2, new ResourceLocation("minestuck", "mercury"));
	public static final GristType QUARTZ = new GristType("quartz", 0.4F, 2, new ResourceLocation("minestuck", "quartz"));
	public static final GristType SULFUR = new GristType("sulfur", 0.4F, 2, new ResourceLocation("minestuck", "sulfur"));
	public static final GristType AMETHYST = new GristType("amethyst", 0.3F, 3, new ResourceLocation("minestuck", "amethyst"));
	public static final GristType GARNET = new GristType("garnet", 0.3F, 3, new ResourceLocation("minestuck", "garnet"));
	public static final GristType RUBY = new GristType("ruby", 0.3F, 3, new ResourceLocation("minestuck", "ruby"));
	public static final GristType RUST = new GristType("rust", 0.3F, 3, new ResourceLocation("minestuck", "rust"));
	public static final GristType DIAMOND = new GristType("diamond", 0.2F, 5, new ResourceLocation("minestuck", "diamond"));
	public static final GristType GOLD = new GristType("gold", 0.2F, 5, new ResourceLocation("minestuck", "gold"));
	public static final GristType URANIUM = new GristType("uranium", 0.2F, 5, new ResourceLocation("minestuck", "uranium"));
	public static final GristType ARTIFACT = new GristType("artifact", 0.1F, 1,  new ResourceLocation("minestuck", "artifact"));
	public static final GristType ZILLIUM = new GristType("zillium", 0.0F, 10, new ResourceLocation("minestuck", "zillium"));
	public static ForgeRegistry<GristType> REGISTRY;
	final String name;
	final float rarity;
	final float value;
	private final ResourceLocation icon;
	private ItemStack candyItem = ItemStack.EMPTY;

	public GristType(String name, float rarity, ResourceLocation icon)
	{
		this(name, rarity, 2, icon);
	}
	
	public GristType(String name, float rarity, float value, ResourceLocation icon)
	{
		this.name = name;
		this.rarity = rarity;
		this.value = value;
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

	public static Collection<GristType> values()
	{
		return REGISTRY.getValues();
	}

	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("grist." + name);
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
	
	
	/**
	 * @return a value estimate for this grist type
	 */
	public float getValue()
	{
		return value;
	}
	
	public ResourceLocation getIcon()
	{
		return icon;
	}
	
	public ItemStack getCandyItem()
	{
		return candyItem.copy();
	}
	
	public GristType setCandyItem(ItemStack stack)	//TODO Verify that the grist registry is loaded after the item registry
	{
		candyItem = stack;
		return this;
	}
	
	public int getId()
	{
		return REGISTRY.getID(this);
	}

	@SubscribeEvent
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry<GristType>) new RegistryBuilder<GristType>()
				.setName(new ResourceLocation("minestuck", "grist"))
				.setType(GristType.class)
				.create();
	}

	@SubscribeEvent
	public static void registerGrist(final RegistryEvent.Register<GristType> event)
	{
		event.getRegistry().registerAll(
				BUILD.setRegistryName("minestuck", "build").setCandyItem(new ItemStack(MinestuckItems.BUILD_GUSHERS)),
				AMBER.setRegistryName("minestuck", "amber").setCandyItem(new ItemStack(MinestuckItems.AMBER_GUMMY_WORM)),
				CAULK.setRegistryName("minestuck", "caulk").setCandyItem(new ItemStack(MinestuckItems.CAULK_PRETZEL)),
				CHALK.setRegistryName("minestuck", "chalk").setCandyItem(new ItemStack(MinestuckItems.CHALK_CANDY_CIGARETTE)),
				IODINE.setRegistryName("minestuck", "iodine").setCandyItem(new ItemStack(MinestuckItems.IODINE_LICORICE)),
				SHALE.setRegistryName("minestuck", "shale").setCandyItem(new ItemStack(MinestuckItems.SHALE_PEEP)),
				TAR.setRegistryName("minestuck", "tar").setCandyItem(new ItemStack(MinestuckItems.TAR_LICORICE)),
				COBALT.setRegistryName("minestuck", "cobalt").setCandyItem(new ItemStack(MinestuckItems.COBALT_GUM)),
				MARBLE.setRegistryName("minestuck", "marble").setCandyItem(new ItemStack(MinestuckItems.MARBLE_JAWBREAKER)),
				MERCURY.setRegistryName("minestuck", "mercury").setCandyItem(new ItemStack(MinestuckItems.MERCURY_SIXLETS)),
				QUARTZ.setRegistryName("minestuck", "quartz").setCandyItem(new ItemStack(MinestuckItems.QUARTZ_JELLY_BEAN)),
				SULFUR.setRegistryName("minestuck", "sulfur").setCandyItem(new ItemStack(MinestuckItems.SULFUR_CANDY_APPLE)),
				AMETHYST.setRegistryName("minestuck", "amethyst").setCandyItem(new ItemStack(MinestuckItems.AMETHYST_HARD_CANDY)),
				GARNET.setRegistryName("minestuck", "garnet").setCandyItem(new ItemStack(MinestuckItems.GARNET_TWIX)),
				RUBY.setRegistryName("minestuck", "ruby").setCandyItem(new ItemStack(MinestuckItems.RUBY_LOLLIPOP)),
				RUST.setRegistryName("minestuck", "rust").setCandyItem(new ItemStack(MinestuckItems.RUST_GUMMY_EYE)),
				DIAMOND.setRegistryName("minestuck", "diamond").setCandyItem(new ItemStack(MinestuckItems.DIAMOND_MINT)),
				GOLD.setRegistryName("minestuck", "gold").setCandyItem(new ItemStack(MinestuckItems.GOLD_CANDY_RIBBON)),
				URANIUM.setRegistryName("minestuck", "uranium").setCandyItem(new ItemStack(MinestuckItems.URANIUM_GUMMY_BEAR)),
				ARTIFACT.setRegistryName("minestuck", "artifact").setCandyItem(new ItemStack(MinestuckItems.ARTIFACT_WARHEAD)),
				ZILLIUM.setRegistryName("minestuck", "zillium").setCandyItem(new ItemStack(MinestuckItems.ZILLIUM_SKITTLES))
		);
	}

	@Override
	public int compareTo(GristType gristType)
	{
		return this.getId() - gristType.getId();
	}
}

