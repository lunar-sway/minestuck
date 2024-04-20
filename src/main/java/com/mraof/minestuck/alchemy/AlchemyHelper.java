package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.event.AlchemyEvent;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.artifact.CruxiteArtifactItem;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.EcheladderBonusType;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nonnull;

import static com.mraof.minestuck.block.MSBlocks.CRUXITE_DOWEL;
import static com.mraof.minestuck.item.MSItems.CAPTCHA_CARD;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
		if (!hasDecodedItem(card))
		{
			return ItemStack.EMPTY;
		}
		CompoundTag tag = card.getTag();
		
		Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(tag.getString(("contentID"))));
		if (item == null) {return ItemStack.EMPTY;}
		
		int count = 1;
		if(ignoreGhost && tag.contains("contentSize") && tag.getInt("contentSize") <= 0)
			count = 0;
		else if(tag.contains("contentSize") && tag.getInt("contentSize") >= 1)
			count = tag.getInt("contentSize");
		
		CompoundTag capabilityData = null;
		if(tag.contains("contentCaps", Tag.TAG_COMPOUND))
			capabilityData = tag.getCompound("contentCaps");
		
		ItemStack newItem = new ItemStack(item, count, capabilityData);
		
		if(tag.contains("contentTags"))
			newItem.setTag(tag.getCompound("contentTags"));
		
		return newItem;
		
	}
	
	public static boolean isReadableCard(ItemStack card)
	{
		if(!card.is(MSItems.CAPTCHA_CARD.get()) || !hasDecodedItem(card))
			return false;
		
		//either has an existing captcha code or isnt in UNREADABLE item tag to begin with
		return (card.hasTag() && card.getTag().contains("captcha_code", Tag.TAG_STRING)) || !getDecodedItem(card).is(MSTags.Items.UNREADABLE);
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
		return createEncodedItem(item, new ItemStack(registerToCard ? CAPTCHA_CARD.get() : CRUXITE_DOWEL.get()));
	}
	
	@Nonnull
	public static ItemStack createEncodedItem(ItemStack itemIn, ItemStack itemOut)
	{
		CompoundTag nbt = null;
		if(!itemIn.isEmpty())
		{
			nbt = new CompoundTag();
			nbt.putString("contentID", BuiltInRegistries.ITEM.getKey(itemIn.getItem()).toString());
		}
		
		itemOut.setTag(nbt);
		return itemOut;
	}
	
	@Nonnull
	public static ItemStack createPunchedCard(ItemStack heldStack)
	{
		ItemStack card = createEncodedItem(heldStack, true);
		card.getOrCreateTag().putBoolean("punched", true);
		
		return card;
	}
	
	@Nonnull
	public static ItemStack createCard(ItemStack heldStack, MinecraftServer server)
	{
		ItemStack card = createEncodedItem(heldStack, true);
		card.getOrCreateTag().putBoolean("punched", false);
		if(heldStack.hasTag())
			card.getOrCreateTag().put("contentTags", heldStack.getTag());
		CompoundTag capsData = heldStack.save(new CompoundTag()).getCompound("ForgeCaps");	//TODO serialize the stack in full instead, so that this hack isn't needed
		if(!capsData.isEmpty())
			card.getOrCreateTag().put("contentCaps", capsData);
		card.getOrCreateTag().putInt("contentSize", heldStack.getCount());
		
		if(!heldStack.is(MSTags.Items.UNREADABLE))
			card.getOrCreateTag().putString("captcha_code", CardCaptchas.getCaptcha(heldStack.getItem(), server));
		
		return card;
	}
	
	@Nonnull
	public static ItemStack createGhostCard(ItemStack item)
	{
		ItemStack stack = createEncodedItem(item, true);
		stack.getOrCreateTag().putBoolean("punched", false);
		stack.getOrCreateTag().putInt("contentSize", 0);
		return stack;
	}
	
	public static void removeItemFromCard(ItemStack card)
	{
		card.removeTagKey("contentID");
		card.removeTagKey("punched");
		card.removeTagKey("contentTags");
		card.removeTagKey("contentCaps");
		card.removeTagKey("contentSize");
		card.removeTagKey("captcha_code");
	}
}