package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EcheladderBonusType;
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
		return getDecodedItem(card, false);
	}
	
	@Nonnull
	public static ItemStack getDecodedItem(ItemStack card, boolean ignoreGhost)
	{
		EncodedItemComponent component = card.getOrDefault(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.EMPTY);
		return ignoreGhost && component.type() == EncodedItemComponent.EncodeType.GHOST ? ItemStack.EMPTY : component.storedStack();
		
	}
	
	public static boolean isReadableCard(ItemStack card)
	{
		return getCode(card) != null;
	}
	
	@Nullable
	public static String getCode(ItemStack card)
	{
		return card.getOrDefault(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.EMPTY).code();
	}
	
	public static boolean isPunchedCard(ItemStack item)
	{
		return item.is(CAPTCHA_CARD) && item.getOrDefault(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.EMPTY).isEncoded();
	}
	
	public static boolean isGhostCard(ItemStack item)
	{
		return item.is(CAPTCHA_CARD) && item.getOrDefault(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.EMPTY).isGhostType();
	}
	
	
	public static boolean hasDecodedItem(ItemStack item)
	{
		return item.has(MSItemComponents.ENCODED_ITEM);
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
		itemOut.set(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.createEncodedItem(itemIn));
		return itemOut;
	}
	
	@Nonnull
	public static ItemStack createPunchedCard(Item itemIn)
	{
		return createEncodedItem(itemIn, new ItemStack(CAPTCHA_CARD.get()));
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack itemIn)
	{
		ItemStack itemOut = new ItemStack(CAPTCHA_CARD.get());
		itemOut.set(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.createStoredItem(itemIn));
		return itemOut;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack itemIn)
	{
		ItemStack itemOut = new ItemStack(CAPTCHA_CARD.get());
		itemOut.set(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.createGhostItem(itemIn));
		return itemOut;
	}
	
	public static void removeItemFromCard(ItemStack card)
	{
		card.remove(MSItemComponents.ENCODED_ITEM);
	}
}
