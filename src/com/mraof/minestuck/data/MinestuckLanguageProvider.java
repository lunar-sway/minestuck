package com.mraof.minestuck.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class MinestuckLanguageProvider extends LanguageProvider
{
	public MinestuckLanguageProvider(DataGenerator gen, String modid, String locale)
	{
		super(gen, modid, locale);
	}
	
	protected void addTooltip(IItemProvider key, String value)
	{
		addExtra(key, "tooltip", value);
	}
	protected void addExtra(IItemProvider key, String type, String value)
	{
		add(key.asItem().getTranslationKey()+"."+type, value);
	}
	protected void addStore(IItemProvider key, String value)
	{
		add("store."+key.asItem().getTranslationKey(), value);
	}
	protected void addStore(ItemStack key, String value)
	{
		add("store."+key.getTranslationKey(), value);
	}
	protected void addStoreTooltip(IItemProvider key, String value)
	{
		add("store."+key.asItem().getTranslationKey()+".tooltip", value);
	}
	protected void addStoreTooltip(ItemStack key, String value)
	{
		add("store."+key.getTranslationKey()+".tooltip", value);
	}
	protected void addExtra(EntityType<?> key, String type, String value)
	{
		add(key.getTranslationKey()+"."+type, value);
	}
}