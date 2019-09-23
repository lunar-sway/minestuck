package com.mraof.minestuck.jei;
/*
import com.mraof.minestuck.item.crafting.alchemy.GristAmount;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import mezz.jei.api.ingredients.IIngredientHelper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GristIngredientHelper implements IIngredientHelper<GristAmount>
{
	public static final List<GristAmount> createList()
	{
		List<GristAmount> list = new ArrayList<>();
		for(GristType gristType : GristType.values())
			list.add(new GristAmount(gristType, 1));
		return list;
	}
	
	@Override
	public List<GristAmount> expandSubtypes(List<GristAmount> ingredients)
	{
		return ingredients;
	}
	
	@Nullable
	@Override
	public GristAmount getMatch(Iterable<GristAmount> ingredients, GristAmount ingredientToMatch)
	{
		for(GristAmount grist : ingredients)
			if(grist.getType() == ingredientToMatch.getType())
				return grist;
		return null;
	}
	
	@Override
	public String getDisplayName(GristAmount ingredient)
	{
		return ingredient.getType().getDisplayName();
	}
	
	@Override
	public String getUniqueId(GristAmount ingredient)
	{
		return "grist:"+ingredient.getType().getName();
	}
	
	@Override
	public String getWildcardId(GristAmount ingredient)
	{
		return getUniqueId(ingredient);
	}
	
	@Override
	public String getModId(GristAmount ingredient)
	{
		return ingredient.getType().getRegistryName().getResourceDomain();
	}
	
	@Override
	public Iterable<Color> getColors(GristAmount ingredient)
	{
		return Collections.emptyList();	//Not dealing with this right now
	}
	
	@Override
	public String getResourceId(GristAmount ingredient)
	{
		return ingredient.getType().getRegistryName().getResourcePath();
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
		else return "grist:"+ingredient.getType().getName()+":"+ingredient.getAmount();
	}
}*/