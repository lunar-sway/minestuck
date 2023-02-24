package com.mraof.minestuck.jei;

import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.GristTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GristIngredientHelper implements IIngredientHelper<GristAmount>
{
	public static List<GristAmount> createList()
	{
		List<GristAmount> list = new ArrayList<>();
		for(GristType gristType : GristTypes.values())
			list.add(new GristAmount(gristType, 1));
		return list;
	}
	
	@Override
	public IIngredientType<GristAmount> getIngredientType()
	{
		return MinestuckJeiPlugin.GRIST;
	}
	
	@Override
	public String getDisplayName(GristAmount ingredient)
	{
		return ingredient.getType().getDisplayName().getString();
	}

	@Override
	public String getUniqueId(GristAmount ingredient, UidContext context)
	{
		return "grist:" + ingredient.getType().getRegistryName();
	}

	@SuppressWarnings("removal")	//getResourceLocation() replaces this. This function is fine to remove as soon as it is not required by IIngredientHelper
	@Override
	public String getModId(GristAmount ingredient)
	{
		return getResourceLocation(ingredient).getNamespace();
	}

	@Override
	public Iterable<Integer> getColors(GristAmount ingredient)
	{
		return Collections.emptyList();	//Not dealing with this right now
	}
	
	@SuppressWarnings("removal")	//getResourceLocation() replaces this. This function is fine to remove as soon as it is not required by IIngredientHelper
	@Override
	public String getResourceId(GristAmount ingredient)
	{
		return getResourceLocation(ingredient).getPath();
	}
	
	@Override
	public ResourceLocation getResourceLocation(GristAmount ingredient)
	{
		return ingredient.getType().getRegistryName();
	}
	
	@Override
	public GristAmount copyIngredient(GristAmount ingredient)
	{
		return new GristAmount(ingredient.getType(), ingredient.getAmount());
	}

	@Override
	public String getErrorInfo(@Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return "grist:null";
		else if(ingredient.getType() == null)
			return "grist:null:"+ingredient.getAmount();
		else return "grist:"+ingredient.getType().getRegistryName()+":"+ingredient.getAmount();
	}
}