package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EcheladderBonusType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.mraof.minestuck.item.MSItems.CAPTCHA_CARD;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class AlchemyHelper
{
	@SubscribeEvent
	public static void onAlchemizedItem(AlchemyEvent event)
	{
		Echeladder e = Echeladder.get(event.getPlayer(), event.getLevel());
		
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
		if (card.has(MSItemComponents.ENCODED_ITEM))
			return new ItemStack(card.get(MSItemComponents.ENCODED_ITEM).item());
		
		return card.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).storedStack();
	}
	
	public static boolean isReadableCard(ItemStack card)
	{
		return getCode(card) != null;
	}
	
	@Nullable
	public static String getCode(ItemStack card)
	{
		return card.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).code();
	}
	
	public static boolean isPunchedCard(ItemStack item)
	{
		return item.is(CAPTCHA_CARD) && item.has(MSItemComponents.ENCODED_ITEM);
	}
	
	public static boolean isGhostCard(ItemStack item)
	{
		if(!item.is(CAPTCHA_CARD)) return false;
		return item.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).isGhostItem();
	}
	
	public static boolean hasDecodedItem(ItemStack item)
	{
		return item.has(MSItemComponents.ENCODED_ITEM) || item.has(MSItemComponents.CARD_STORED_ITEM);
	}
	
	/**
	 * Given a punched card, this method returns a new item that represents the encoded data,
	 * or it just returns the item directly if it's not a punched card.
	 */
	@Nonnull
	public static ItemStack getDecodedItemDesignix(ItemStack card)
	{
		
		if (card.isEmpty()) {return ItemStack.EMPTY;}
		
		if (card.getItem().equals(CAPTCHA_CARD.get()) && hasDecodedItem(card))
		{
			return getDecodedItem(card);
		}
		else
		{
			return card.copy();
		}
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(Item itemIn, ItemStack itemOut)
	{
		itemOut.set(MSItemComponents.ENCODED_ITEM, new EncodedItemComponent(itemIn));
		return itemOut;
	}
	
	@Nonnull
	public static ItemStack createPunchedCard(Item itemIn)
	{
		return createEncodedItem(itemIn, new ItemStack(CAPTCHA_CARD.get()));
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack itemIn, MinecraftServer mcServer)
	{
		ItemStack itemOut = new ItemStack(CAPTCHA_CARD.get());
		itemOut.set(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.create(itemIn, false, mcServer));
		return itemOut;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack itemIn, MinecraftServer mcServer)
	{
		ItemStack itemOut = new ItemStack(CAPTCHA_CARD.get());
		itemOut.set(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.create(itemIn, true, mcServer));
		return itemOut;
	}
	
	public static void removeItemFromCard(ItemStack card)
	{
		card.remove(MSItemComponents.CARD_STORED_ITEM);
	}
}
