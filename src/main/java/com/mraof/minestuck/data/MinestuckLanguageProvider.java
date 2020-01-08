package com.mraof.minestuck.data;

import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

/**
 * This is made to also be useful for mods other than minestuck. If you're making a plugin, this class can be rather helpful!
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MinestuckLanguageProvider extends LanguageProvider
{
	private final String modid;
	
	public MinestuckLanguageProvider(DataGenerator gen, String modid, String locale)
	{
		super(gen, modid, locale);
		this.modid = modid;
	}
	
	protected void addBlockTooltip(Supplier<Block> key, String value)
	{
		addTooltip(key.get(), value);
	}
	protected void addItemTooltip(Supplier<Item> key, String value)
	{
		addTooltip(key.get(), value);
	}
	protected void addTooltip(IItemProvider key, String value)
	{
		addExtra(key, "tooltip", value);
	}
	protected void addBlockExtra(Supplier<Block> key, String type, String value)
	{
		addExtra(key.get(), type, value);
	}
	protected void addItemExtra(Supplier<Item> key, String type, String value)
	{
		addExtra(key.get(), type, value);
	}
	protected void addExtra(IItemProvider key, String type, String value)
	{
		add(key.asItem().getTranslationKey()+"."+type, value);
	}
	protected void addBlockStore(Supplier<Block> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addItemStore(Supplier<Item> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addStore(IItemProvider key, String value)
	{
		add("store."+key.asItem().getTranslationKey(), value);
	}
	protected void addItemStackStore(Supplier<ItemStack> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addStore(ItemStack key, String value)
	{
		add("store."+key.getTranslationKey(), value);
	}
	protected void addBlockStoreTooltip(Supplier<Block> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addItemStoreTooltip(Supplier<Item> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addStoreTooltip(IItemProvider key, String value)
	{
		add("store."+key.asItem().getTranslationKey()+".tooltip", value);
	}
	protected void addItemStackStoreTooltip(Supplier<ItemStack> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addStoreTooltip(ItemStack key, String value)
	{
		add("store."+key.getTranslationKey()+".tooltip", value);
	}
	protected void add(ItemGroup key, String value)
	{
		add(key.getTranslationKey(), value);
	}
	protected void addEntityTypeExtra(Supplier<EntityType<?>> key, String type, String value)
	{
		addExtra(key.get(), type, value);
	}
	protected void addExtra(EntityType<?> key, String type, String value)
	{
		add(key.getTranslationKey()+"."+type, value);
	}
	protected void addGristType(Supplier<GristType> key, String value)
	{
		add(key.get(), value);
	}
	protected void add(GristType key, String value)
	{
		add(key.getTranslationKey(), value);
	}
	protected void add(EnumClass key, String value)
	{
		add(key.getTranslationKey(), value);
	}
	protected void add(EnumAspect key, String value)
	{
		add(key.getTranslationKey(), value);
	}
	protected void addDenizen(EnumAspect key, String value)
	{
		add("denizen." + key.getTranslationKey(), value);
	}
	protected void addRung(int rung, String value)
	{
		add("echeladder.rung." + rung, value);
	}
	protected void addAdvancement(String key, String title, String description)
	{
		add("advancements."+key+".title", title);
		add("advancements."+key+".description", description);
	}
	protected void addLand(String key, String value)
	{
		add("land."+key, value);
	}
	protected void addStrife(String key, String value)
	{
		add("strife."+key, value);
	}
	protected void addDialogue(String key, String value)
	{
		add("consort."+key, value);
	}
	protected void addSubtitles(String key, String value)
	{
		add("subtitles."+modid+"."+key, value);
	}
	protected void addEntitySubtitles(EntityType<?> type, String key, String value)
	{
		add("subtitles."+type.getTranslationKey()+"."+key, value);
	}
}