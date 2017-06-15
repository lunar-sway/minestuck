package com.mraof.minestuck.modSupport.minetweaker;

import com.mraof.minestuck.util.GristRegistry;
import com.mraof.minestuck.util.GristSet;
import com.mraof.minestuck.util.GristType;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ZenClass("minestuck.Alchemy")
public class Alchemy
{
	
	@ZenMethod
	public static void setCost(IItemStack iStack, String cost)
	{
		ItemStack stack = (ItemStack) iStack.getInternal();
		GristSet grist = getGrist(cost);
		if(grist == null)
			return;
		MineTweakerAPI.apply(new SetCost(stack.getItem(), stack.getItemDamage(), grist));
	}
	
	@ZenMethod
	public static void setOreDictCost(String name, String cost)
	{
		GristSet grist = getGrist(cost);
		if(grist == null)
			return;
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, grist));
	}
	
	@ZenMethod
	public static void removeCost(IItemStack iStack)
	{
		ItemStack stack = (ItemStack) iStack.getInternal();
		MineTweakerAPI.apply(new SetCost(stack.getItem(), stack.getItemDamage(), null));
	}
	
	@ZenMethod
	public static void removeOreDictCost(String name)
	{
		MineTweakerAPI.apply(new SetCost(name, OreDictionary.WILDCARD_VALUE, null));
	}
	
	private static class SetCost implements IUndoableAction
	{
		
		private final List<Object> items;
		private final GristSet cost;
		private GristSet costOld;
		
		public SetCost(Object item, int metadata, GristSet cost)
		{
			items = Arrays.asList(item, metadata);
			this.cost = cost;
		}
		
		@Override
		public void apply()
		{
			costOld = GristRegistry.getAllConversions().remove(items);
			if(cost != null)
				GristRegistry.getAllConversions().put(items, cost);
		}
		
		@Override
		public void undo()
		{
			if(costOld == null)
				GristRegistry.getAllConversions().remove(items);
			else GristRegistry.getAllConversions().put(items, costOld);
		}
		
		@Override
		public boolean canUndo()
		{
			return true;
		}
		
		@Override
		public String describe()
		{
			if(cost == null)
				return "Removing Grist Cost for \""+getInputName()+"\"";
			else return "Adding Grist Cost for \""+getInputName()+"\"";
		}
		
		@Override
		public String describeUndo()
		{
			if(cost == null)
				return "Reversing removal of Grist Cost for \""+getInputName()+"\"";
			else return "Removing GristCost for \""+getInputName()+"\"";
		}
		
		@Override
		public Object getOverrideKey()
		{
			return null;
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
					MineTweakerAPI.logError("\""+gristAmount[0].toLowerCase()+"\" does not match any grist types. Look for typos and make sure the grist type is actually in the mod.");
					return null;
				}
			} else
			{
				MineTweakerAPI.logError("\""+value + "\" is not an acceptable grist value. Separate grist-number pairings with commas.");
				return null;
			}
		}
		
		return grist;
	}
}