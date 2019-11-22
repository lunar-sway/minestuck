package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import java.util.Collection;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class GristType extends ForgeRegistryEntry<GristType> implements Comparable<GristType>
{
	public static String FORMAT = "grist.format";	//TODO Readd final once the ObjectHolder use has been sorted out
	
	private static final ResourceLocation DUMMY_ICON_LOCATION = new ResourceLocation(Minestuck.MOD_ID, "textures/grist/dummy.png");
	
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
	
	public static ForgeRegistry<GristType> REGISTRY;
	private final float rarity;
	private final float value;
	private ItemStack candyItem = ItemStack.EMPTY;
	private String translationKey;
	private ResourceLocation icon;
	
	public GristType(float rarity)
	{
		this(rarity, 2);
	}
	
	public GristType(float rarity, float value)
	{
		this.rarity = rarity;
		this.value = value;
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
	
	public ITextComponent getNameWithSuffix()
	{
		return new TranslationTextComponent(FORMAT, getDisplayName());
	}
	
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(getTranslationKey());
	}
	/**
	 * Returns the grist's full unlocalized name.
	 */
	public String getTranslationKey()
	{
		if(translationKey == null)
			translationKey = Util.makeTranslationKey("grist", REGISTRY.getKey(this));
		
		return translationKey;
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
		return rarity != 0 ? 1 / rarity : 100;
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
		if(icon == null)
			icon = makeIconPath(getEffectiveName());
		
		return icon;
	}
	
	public ResourceLocation getEffectiveName()
	{
		ResourceLocation name = REGISTRY.getKey(this);
		if(name == null)
			return new ResourceLocation(Minestuck.MOD_ID, "dummy");
		else return name;
	}
	
	public ItemStack getCandyItem()
	{
		return candyItem.copy();
	}
	
	public GristType setCandyItem(ItemStack stack)
	{
		candyItem = stack;
		return this;
	}
	
	public int getId()
	{
		return REGISTRY.getID(this);
	}
	
	/**
	 * Returns the resource location to the dummy grist icon texture
	 * The actual field is private as to not get caught by the ObjectHolder on the class (because it checks all public static final fields).
	 */
	public static ResourceLocation getDummyIcon()
	{
		return DUMMY_ICON_LOCATION;
	}
	
	private static ResourceLocation makeIconPath(ResourceLocation entry)
	{
		if(entry == null)
			return DUMMY_ICON_LOCATION;
		else return new ResourceLocation(entry.getNamespace(), "textures/grist/"+entry.getPath()+".png");
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		REGISTRY = (ForgeRegistry<GristType>) new RegistryBuilder<GristType>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "grist"))
				.setType(GristType.class)
				.set(DummyFactory.INSTANCE)
				.create();
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
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
	
	@Override
	public String toString()
	{
		return String.valueOf(getRegistryName());
	}
	
	@Override
	public int compareTo(GristType gristType)
	{
		return this.getId() - gristType.getId();
	}
	
	private static class DummyFactory implements IForgeRegistry.DummyFactory<GristType>
	{
		private static final DummyFactory INSTANCE = new DummyFactory();
		
		@Override
		public GristType createDummy(ResourceLocation key)
		{
			return new DummyType().setRegistryName(key);
		}
	}
	
	private static class DummyType extends GristType
	{
		public DummyType()
		{
			super(0.3F, 3);
		}
		
		@Override
		public ResourceLocation getIcon()
		{
			return DUMMY_ICON_LOCATION;
		}
		
		@Override
		public ResourceLocation getEffectiveName()
		{
			return new ResourceLocation(Minestuck.MOD_ID, "dummy");
		}
	}
}