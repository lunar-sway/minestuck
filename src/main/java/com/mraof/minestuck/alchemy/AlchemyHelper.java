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
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nonnull;

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
		EncodedItemComponent encodedItemComponent = card.get(MSItemComponents.ENCODED_ITEM);
		if (encodedItemComponent != null)
			return encodedItemComponent.asItemStack();
		
		return card.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).storedStack();
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
}
