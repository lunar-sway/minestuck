package com.mraof.minestuck.modSupport.crafttweaker;

import com.mraof.minestuck.alchemy.AlchemyCostRegistry;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenClass("minestuck.Alchemy")
@ZenRegister
public class Alchemy
{
	static List<IAction> recipes = new ArrayList<>();
	
	@ZenMethod
	public static void setCost(IItemStack iStack, String cost)
	{
		ItemStack stack = (ItemStack) iStack.getInternal();
		GristSet grist = getGrist(cost);
		if(grist == null)
			return;
		recipes.add(new SetCost(stack.getItem(), stack.getItemDamage(), grist));
	}
	
	@ZenMethod
	public static void setOreDictCost(String name, String cost)
	{
		GristSet grist = getGrist(cost);
		if(grist == null)
			return;
		recipes.add(new SetCost(name, OreDictionary.WILDCARD_VALUE, grist));
	}
	
	@ZenMethod
	public static void removeCost(IItemStack iStack)
	{
		ItemStack stack = (ItemStack) iStack.getInternal();
		recipes.add(new SetCost(stack.getItem(), stack.getItemDamage(), null));
	}
	
	@ZenMethod
	public static void removeOreDictCost(String name)
	{
		recipes.add(new SetCost(name, OreDictionary.WILDCARD_VALUE, null));
	}
	
	private static class SetCost implements IAction
	{
		
		private final List<Object> items;
		private final GristSet cost;
		
		public SetCost(Object item, int metadata, GristSet cost)
		{
			items = Arrays.asList(item, metadata);
			this.cost = cost;
		}
		
		@Override
		public void apply()
		{
			AlchemyCostRegistry.getAllConversions().remove(items);
			if(cost != null)
				AlchemyCostRegistry.getAllConversions().put(items, cost);
		}
		
		@Override
		public String describe()
		{
			if(cost == null)
				return "Removing Grist Cost for \""+getInputName()+"\"";
			else return "Adding Grist Cost for \""+getInputName()+"\"";
		}
		
		public String getInputName()
		{
			if(items.get(0) instanceof String)
				return items.get(0).toString();
			else if(items.get(1).equals(OreDictionary.WILDCARD_VALUE))
				return I18n.translateToLocal(((Item) items.get(0)).getUnlocalizedName());
			ItemStack stack = new ItemStack((Item) items.get(0), 1, (Integer) items.get(1));
			return stack.getDisplayName();
		}
	}
	
	private static GristSet getGrist(String str)
	{
		if(str == null)
			return null;
		
		GristSet grist = new GristSet();
		String[] values = str.split(",");
		for(String value : values)
		{
			value = value.trim();
			String[] gristAmount = value.split("\\s+");
			if(gristAmount.length == 2)
			{
				if(GristType.getTypeFromString(gristAmount[0].toLowerCase()) != null)
					grist.addGrist(GristType.getTypeFromString(gristAmount[0].toLowerCase()), Integer.parseInt(gristAmount[1]));
				else
				{
					CraftTweakerAPI.logError("\""+gristAmount[0].toLowerCase()+"\" does not match any grist types. Look for typos and make sure the grist type is actually in the mod.");
					return null;
				}
			} else
			{
				CraftTweakerAPI.logError("\""+value + "\" is not an acceptable grist value. Separate grist-number pairings with commas.");
				return null;
			}
		}
		
		return grist;
	}
}