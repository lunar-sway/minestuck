package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MSBlocks.CRUXITE_DOWEL;
import static com.mraof.minestuck.item.MSItems.CAPTCHA_CARD;
import static com.mraof.minestuck.item.MSItems.SHUNT;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlchemyHelper
{
	@SubscribeEvent
	public static void onAlchemizedItem(AlchemyEvent event)
	{
		Echeladder e = PlayerSavedData.getData(event.getPlayer(), event.getLevel()).getEcheladder();
		
		if(!(event.getItemResult().getItem() instanceof CruxiteArtifactItem))
		{
			e.checkBonus(EcheladderBonusType.ALCHEMY_1);
			GristSet cost = event.getCost();
			double value = cost.getValue();
			if(value >= 50)
				e.checkBonus(EcheladderBonusType.ALCHEMY_2);
			if(value >= 500)
				e.checkBonus(EcheladderBonusType.ALCHEMY_3);
		}
		
	}
	
	/**
	 * Given a punched card or a carved dowel, returns a new item that represents the encoded data.
	 * 
	 * @param card - The dowel or card with encoded data
	 * @return An item, or null if the data was invalid.
	 */
	@Nonnull
	public static ItemStack getDecodedItem(ItemStack card)
	{
		return getDecodedItem(card, false);
	}
	
	@Nonnull
	public static ItemStack getDecodedItem(ItemStack card, boolean ignoreGhost)
	{
		if (!hasDecodedItem(card))
		{
			return ItemStack.EMPTY;
		}
		CompoundTag tag = card.getTag();
		
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString(("contentID"))));
		if (item == null) {return ItemStack.EMPTY;}
		ItemStack newItem = new ItemStack(item);
		
		if(tag.contains("contentTags"))
			newItem.setTag(tag.getCompound("contentTags"));
		
		if(ignoreGhost && tag.contains("contentSize") && tag.getInt("contentSize") <= 0)
			newItem.setCount(0);
		else if(tag.contains("contentSize") && tag.getInt("contentSize") >= 1)
			newItem.setCount(tag.getInt("contentSize"));
		
		return newItem;
		
	}
	
	public static boolean isPunchedCard(ItemStack item)
	{
		return item.getItem() == MSItems.CAPTCHA_CARD.get() && item.hasTag() && item.getTag().getBoolean("punched");
	}
	
	public static boolean isGhostCard(ItemStack item)
	{
		return item.getItem() == MSItems.CAPTCHA_CARD.get() && hasDecodedItem(item) && item.getTag().getInt("contentSize") <= 0;
	}
	
	public static boolean hasDecodedItem(ItemStack item)
	{
		return item.hasTag() && item.getTag().contains("contentID", Tag.TAG_STRING);
	}
	
	/**
	 * Given a punched card, this method returns a new item that represents the encoded data,
	 * or it just returns the item directly if it's not a punched card.
	 */
	@Nonnull
	public static ItemStack getDecodedItemDesignix(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		if (card.getItem().equals(CAPTCHA_CARD.get()) && card.hasTag() && card.getTag().contains("contentID"))
		{
			return getDecodedItem(card);
		}
		else
		{
			return card.copy();
		}
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack item, boolean registerToCard)
	{
		CompoundTag nbt = null;
		if(!item.isEmpty())
		{
			nbt = new CompoundTag();
			nbt.putString("contentID", ForgeRegistries.ITEMS.getKey(item.getItem()).toString());
		}
		ItemStack stack = new ItemStack(registerToCard ? CAPTCHA_CARD.get() : CRUXITE_DOWEL.get());
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, ItemStack itemOut)
	{
		CompoundTag nbt = null;
		if(!itemIn.isEmpty())
		{
			nbt = new CompoundTag();
			nbt.putString("contentID", ForgeRegistries.ITEMS.getKey(itemIn.getItem()).toString());
		}
		ItemStack stack = itemOut;
		
		
		stack.setTag(nbt);
		return stack;
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, Item itemOut)
	{
		return createEncodedItem(itemIn, new ItemStack(itemOut));
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack item, boolean punched)
	{
		ItemStack stack = createEncodedItem(item, true);
		stack.getOrCreateTag().putBoolean("punched", punched);
		if(!punched)
		{
			if(item.hasTag())
				stack.getOrCreateTag().put("contentTags", item.getTag());
			stack.getOrCreateTag().putInt("contentSize", item.getCount());
		}
		
		return stack;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, true);
		stack.getOrCreateTag().putBoolean("punched", false);
			if(item.hasTag())
				stack.getOrCreateTag().put("contentTags", item.getTag());
			stack.getOrCreateTag().putInt("contentSize", 0);
		return stack;
	}
	
	public static void removeItemFromCard(ItemStack card)
	{
		card.removeTagKey("contentID");
		card.removeTagKey("punched");
		card.removeTagKey("contentTags");
		card.removeTagKey("contentSize");
	}
	
	public static ItemStack changeEncodeSize(ItemStack stack, int size)
	{
		stack.getOrCreateTag().putInt("contentSize", size);
		
		return stack;
	}
	
	public static ItemStack createShunt(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, SHUNT.get());
		stack.getOrCreateTag().putBoolean("punched", true);
		
		if(item.hasTag())
			stack.getOrCreateTag().put("contentTags", item.getTag());
		stack.getOrCreateTag().putInt("contentSize", item.getCount());
		
		return stack;
	}
}