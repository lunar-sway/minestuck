package com.mraof.minestuck.inventory.specibus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SpecibusPacket;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.LogicalSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrifePortfolioHandler 
{
	//private static String[] metaConvert = {};
	private static final List<KindAbstratusType> abstrataList = KindAbstratusList.getTypeList();
	private static Random rand;
	
	public static StrifeSpecibus createSpecibus(int id, LogicalSide side)
	{
		StrifeSpecibus specibus = new StrifeSpecibus(id);
		specibus.side = side;
		return specibus;
	}
	
	public static KindAbstratusType getType(int id)
	{
		return abstrataList.get(id);
	}
	
	public static void addSpecibus(PlayerEntity player, StrifeSpecibus specibus)
	{
		ITextComponent typeName = specibus.getAbstratus().getDisplayName();
		if(specibus.getAbstratus().equals(abstrataList.get(0)))
			player.sendStatusMessage(new TranslationTextComponent("specibus.success.blank"), false);
		else
			player.sendStatusMessage(new TranslationTextComponent("specibus.success", typeName), false);
		SpecibusPacket packet = SpecibusPacket.addSpecibus(writeToNBT(specibus));
		MSPacketHandler.sendToServer(packet);
	}
	
	public static boolean checkSpecibusLimit(ServerPlayerEntity player)
	{
		return PlayerSavedData.getData(player).getPortfolio().size() < 10;
	}
	
	public static void retrieveItem(ServerPlayerEntity player, int specibusIndex, int itemIndex)
	{	
		StrifeSpecibus specibus = getSpecibus(player, specibusIndex);
		if(specibus == null)
			return;
		ItemStack stack = specibus.retrieveItem(itemIndex);
		spawnItem(player, stack);
		
	}
	
	public static void retrieveSpecibus(int specibusId)
	{
		SpecibusPacket packet = SpecibusPacket.removeSpecibus(specibusId);
		MSPacketHandler.sendToServer(packet);
	}
	
	public static ItemStack createSpecibusItem(StrifeSpecibus specibus)
	{
		ItemStack stack = new ItemStack(MSItems.strifeCard);
		if(!specibus.isBlank()) stack.setTag(specibus.writeToNBT(new CompoundNBT()));
		
		return stack;
	}
	
	public static void spawnItem(ServerPlayerEntity player, ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			ItemStack otherStack = player.getHeldItemMainhand();
			if(otherStack.isEmpty())
				player.setHeldItem(Hand.MAIN_HAND, stack);
			else if(canMergeItemStacks(stack, otherStack))
			{
				otherStack.grow(stack.getCount());
				stack.setCount(0);
			}
			else
			{
				boolean placed = false;
				for(int i = 0; i < player.inventory.mainInventory.size(); i++)
				{
					otherStack = player.inventory.mainInventory.get(i);
					if(otherStack.isEmpty())
						player.inventory.mainInventory.set(i, stack.copy());
					else if(canMergeItemStacks(stack, otherStack))
						otherStack.grow(stack.getCount());
					else continue;
					
					stack.setCount(0);
					placed = true;
					player.inventory.markDirty();
					player.container.detectAndSendChanges();
					break;
				}
				if(!placed)
					CaptchaDeckHandler.launchAnyItem(player, stack);
			}
		}
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2)
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
	
	public static StrifeSpecibus getSpecibus(ServerPlayerEntity player, int specibusIndex)
	{
		StrifeSpecibus specibus = new StrifeSpecibus(0);
		List<StrifeSpecibus> portfolio = PlayerSavedData.getData(player).getPortfolio();
		if(specibusIndex < portfolio.size())
			specibus = portfolio.get(specibusIndex);
			else System.out.println("index error!!!!!!!!!");
		return specibus;
	}
	
	public static void setPortfolio(PlayerEntity player, List<StrifeSpecibus> portfolio)
	{
		SpecibusPacket packet = SpecibusPacket.sendPortfolioData(writeToNBT(portfolio));
		MSPacketHandler.sendToServer(packet);
	}
	
	public static CompoundNBT writeToNBT(StrifeSpecibus specibus)
	{
		return specibus.writeToNBT(new CompoundNBT());
	}
	
	public static CompoundNBT writeToNBT(List<StrifeSpecibus> portfolio)
	{
		CompoundNBT nbt = new CompoundNBT();
	
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			nbt.put("specibus"+i, specibus.writeToNBT(new CompoundNBT()));
			i++;
		}
		
		return nbt;
	}
	
	public static ArrayList<StrifeSpecibus> createPortfolio(CompoundNBT nbt)
	{
		ArrayList<StrifeSpecibus> portfolio = new ArrayList<>();
		for(int i = 0; i < nbt.size(); i++)
		{
			String name = "specibus"+i;
			
			if(nbt.contains(name, Constants.NBT.TAG_COMPOUND))
			{
				portfolio.add(new StrifeSpecibus(nbt.getCompound(name)));
			}
		}
		return portfolio;
	}

	public static void addItemToDeck(ServerPlayerEntity player)
	{
		ItemStack hand = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
		if(hand.isEmpty())
				return;
		if(!player.isSneaking())
		{
			//TODO modifications to the list are currently not kept,
			// and modifications to the specibi are not marking the saved data as dirty
			// This need to be fixed
			List<StrifeSpecibus> portfolio = PlayerSavedData.getData(player).getPortfolio();
			int i = 0;
			for(StrifeSpecibus specibus : portfolio)
			{
				//specibus.forceItemStack(stack);
				if(specibus.putItemStack(hand))
				{
					//specibus.setAbstratus(KindAbstratusList.getTypeFromName("sword"));
					portfolio.set(i, specibus);
					hand.shrink(1);
					//MinestuckPlayerData.setStrifePortfolio(IdentifierHandler.encode(player), portfolio);
					return;
				}
				i++;
			}
		}
		CaptchaDeckHandler.captchalogueItem((ServerPlayerEntity) player);
		
	}
}
