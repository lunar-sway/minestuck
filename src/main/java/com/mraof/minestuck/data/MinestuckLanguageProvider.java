package com.mraof.minestuck.data;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.block.ItemBlockPair;
import com.mraof.minestuck.computer.theme.ComputerTheme;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.function.Supplier;

/**
 * This is made to also be useful for mods other than minestuck. If you're making a plugin, this class can be rather helpful!
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class MinestuckLanguageProvider extends LanguageProvider
{
	private final String modid;
	
	public MinestuckLanguageProvider(PackOutput output, String modid, String locale)
	{
		super(output, modid, locale);
		this.modid = modid;
	}
	
	protected void add(ItemBlockPair<?, ?> pair, String name)
	{
		add(pair.asBlock(), name);
	}
	protected void addBlockTooltip(Supplier<Block> key, String value)
	{
		addTooltip(key.get(), value);
	}
	protected void addItemTooltip(Supplier<Item> key, String value)
	{
		addTooltip(key.get(), value);
	}
	protected void addTooltip(ItemLike key, String value)
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
	protected void addExtra(ItemLike key, String type, String value)
	{
		add(key.asItem().getDescriptionId()+"."+type, value);
	}
	protected void addBlockStore(Supplier<Block> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addItemStore(Supplier<Item> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addStore(ItemLike key, String value)
	{
		add("store."+key.asItem().getDescriptionId(), value);
	}
	protected void addItemStackStore(Supplier<ItemStack> key, String value)
	{
		addStore(key.get(), value);
	}
	protected void addStore(ItemStack key, String value)
	{
		add("store."+key.getDescriptionId(), value);
	}
	protected void addBlockStoreTooltip(Supplier<Block> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addItemStoreTooltip(Supplier<Item> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addStoreTooltip(ItemLike key, String value)
	{
		add("store."+key.asItem().getDescriptionId()+".tooltip", value);
	}
	protected void addItemStackStoreTooltip(Supplier<ItemStack> key, String value)
	{
		addStoreTooltip(key.get(), value);
	}
	protected void addStoreTooltip(ItemStack key, String value)
	{
		add("store."+key.getDescriptionId()+".tooltip", value);
	}
	protected void add(CreativeModeTab key, String value)
	{
		add(((TranslatableContents)key.getDisplayName().getContents()).getKey(), value);
	}
	protected void addEntityTypeExtra(Supplier<? extends EntityType<?>> key, String type, String value)
	{
		addExtra(key.get(), type, value);
	}
	protected void addExtra(EntityType<?> key, String type, String value)
	{
		add(key.getDescriptionId()+"."+type, value);
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
		add(Echeladder.translationKey(rung), value);
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
	protected void addSubtitles(String key, String value)
	{
		add("subtitles."+modid+"."+key, value);
	}
	protected void addEntitySubtitles(Supplier<? extends EntityType<?>> type, String key, String value)
	{
		add("subtitles."+type.get().getDescriptionId()+"."+key, value);
	}
	protected void addColor(String key, String value)
	{
		add("minestuck.color."+key, value);
	}
	protected void addBaseDeathMessage(String key, String value)
	{
		add("death.attack." + key, value);
	}
	protected void addKillCreditDeathMessage(String key, String value)
	{
		add("death.attack." + key + ".player", value);
	}
	protected void addDeathMessageWithItem(String key, String value)
	{
		add("death.attack." + key + ".item", value);
	}
	/**
	 * Use this for damage types which will only exist in damage sources without an entity source.
	 * @param base Death message with an argument for the entity that died.
	 * @param killCredit Death message with the first argument for the entity that died,
	 *                      and the second argument for the kill credit entity.
	 */
	protected void addSimpleDeathMessage(String key, String base, String killCredit)
	{
		addBaseDeathMessage(key, base);
		addKillCreditDeathMessage(key, killCredit);
	}
	/**
	 * Use this for damage types which will only exist in damage sources with an entity source.
	 * @param base Death message with the first argument for the entity that died,
	 *                and the second argument for the entity that caused the damage.
	 * @param withItem Death message with the first argument for the entity that died,
	 *                    the second argument for the entity that caused the damage,
	 *                    and third being the custom name of the item held by the damage-causing entity.
	 */
	protected void addEntitySourceDeathMessage(String key, String base, String withItem)
	{
		addBaseDeathMessage(key, base);
		addDeathMessageWithItem(key, withItem);
	}
	protected void addThemeName(ResourceLocation themeId, String themeName)
	{
		add(ComputerTheme.translationKeyFromId(themeId), themeName);
	}
}