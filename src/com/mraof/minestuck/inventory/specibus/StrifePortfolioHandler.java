package com.mraof.minestuck.inventory.specibus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.SpecibusPacket;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;

public class StrifePortfolioHandler 
{
	//private static String[] metaConvert = {};
	private static List<KindAbstratusType> abstrataList = KindAbstratusList.getTypeList();
	private static Random rand;
	
	public static StrifeSpecibus createSpecibus(int id, Side side)
	{
		StrifeSpecibus specibus = new StrifeSpecibus(id);
		specibus.side = side;
		return specibus;
	}
	
	public static KindAbstratusType getType(int id)
	{
		return abstrataList.get(id);
	}

	public static void addSpecibus(EntityPlayer player, StrifeSpecibus specibus) 
	{
		String typeName = specibus.getAbstratus().getDisplayName();
		player.sendStatusMessage(new TextComponentTranslation("The " + typeName + " specibus has been added to your strife portfolio."), false);						
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.PORTFOLIO, SpecibusPacket.SPECIBUS_ADD, writeToNBT(specibus));
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void retrieveItem(EntityPlayerMP player, int specibusIndex, int itemIndex)
	{	
		StrifeSpecibus specibus = getSpecibus(player, specibusIndex);
		if(specibus == null)
			return;
		ItemStack stack = specibus.retrieveItem(itemIndex);
		spawnItem(player, stack);
		
	}
	
	public static void retrieveSpecibus(int specibusId)
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.PORTFOLIO, SpecibusPacket.SPECIBUS_REMOVE, specibusId);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static ItemStack createSpecibusItem(StrifeSpecibus specibus)
	{
		ItemStack stack = new ItemStack(MinestuckItems.strifeCard);
		stack.setTagCompound(specibus.writeToNBT(new NBTTagCompound()));
		
		return stack;
	}
	
	public static void spawnItem(EntityPlayerMP player, ItemStack stack)
	{
		if(!stack.isEmpty())
		{
			ItemStack otherStack = player.getHeldItemMainhand();
			if(otherStack.isEmpty())
				player.setHeldItem(EnumHand.MAIN_HAND, stack);
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
					player.inventoryContainer.detectAndSendChanges();
					break;
				}
				if(!placed)
					launchAnyItem(player, stack);
			}
		}
	}
	
	public static void launchAnyItem(EntityPlayer player, ItemStack item)
	{
		EntityItem entity = new EntityItem(player.world, player.posX, player.posY+1, player.posZ, item);
		entity.motionX = rand.nextDouble() - 0.5;
		entity.motionZ = rand.nextDouble() - 0.5;
		entity.setDefaultPickupDelay();
		player.world.spawnEntity(entity);
	}
	
	private static boolean canMergeItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2)
				&& stack1.isStackable() && stack1.getCount() + stack2.getCount() < stack1.getMaxStackSize();
	}
	
	public static StrifeSpecibus getSpecibus(EntityPlayerMP player, int specibusIndex)
	{
		return MinestuckPlayerData.getStrifePortfolio(IdentifierHandler.encode(player)).get(specibusIndex);
	}
	
	public static void setPortfolio(EntityPlayer player, ArrayList<StrifeSpecibus> portfolio) 
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(MinestuckPacket.Type.PORTFOLIO, SpecibusPacket.PORTFOLIO, writeToNBT(portfolio));
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static NBTTagCompound writeToNBT(StrifeSpecibus specibus)
	{
		return specibus.writeToNBT(new NBTTagCompound());
	}
	
	public static NBTTagCompound writeToNBT(ArrayList<StrifeSpecibus> portfolio)
	{
		NBTTagCompound nbt = new NBTTagCompound();
	
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			nbt.setTag("specibus"+i, specibus.writeToNBT(new NBTTagCompound()));
			i++;
		}
	
		
		return nbt;
	}
	
	public static ArrayList<StrifeSpecibus> createPortfolio(NBTTagCompound nbt)
	{
		ArrayList<StrifeSpecibus> portfolio = new ArrayList<StrifeSpecibus>();
		for(int i = 0; i < nbt.getSize(); i++)
		{
			String name = "specibus"+i;
			
			if(nbt.hasKey(name))
			{
				portfolio.add(new StrifeSpecibus(nbt.getCompoundTag(name)));
			}
		}
		return portfolio;
	}

	public static void addItemToDeck(EntityPlayerMP player) 
	{
		ItemStack hand = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		if(hand.isEmpty())
				return;
		if(!player.isSneaking())
		{
		ArrayList<StrifeSpecibus> portfolio = MinestuckPlayerData.getStrifePortfolio(IdentifierHandler.encode(player));
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			//specibus.forceItemStack(stack);
			if(specibus.putItemStack(hand))
			{
				//specibus.setAbstratus(KindAbstratusList.getTypeFromName("sword"));
				portfolio.set(i, specibus);
				hand.shrink(1);
				MinestuckPlayerData.setClientPortfolio(portfolio);
				MinestuckPlayerData.setStrifePortfolio(IdentifierHandler.encode(player), portfolio);
				return;
			}
			i++;
		}
		}
		CaptchaDeckHandler.captchalougeItem((EntityPlayerMP) player);
		
	}
}
