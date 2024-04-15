package com.mraof.minestuck.jei;

import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GristIngredientHelper implements IIngredientHelper<GristAmount>
{
	public static List<GristAmount> createList()
	{
		List<GristAmount> list = new ArrayList<>();
		for(GristType gristType : GristTypes.REGISTRY)
			list.add(gristType.amount(1));
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
		return ingredient.type().getDisplayName().getString();
	}

	@Override
	public String getUniqueId(GristAmount ingredient, UidContext context)
	{
		return "grist:" + getResourceLocation(ingredient);
	}
	
	@Override
	public Iterable<Integer> getColors(GristAmount ingredient)
	{
		return Collections.emptyList();	//Not dealing with this right now
	}
	
	@Override
	public ResourceLocation getResourceLocation(GristAmount ingredient)
	{
		return ingredient.type().getIdOrThrow();
	}
	
	@Override
	public GristAmount copyIngredient(GristAmount ingredient)
	{
		return new GristAmount(ingredient.type(), ingredient.amount());
	}

	@Override
	public String getErrorInfo(@Nullable GristAmount ingredient)
	{
		if(ingredient == null)
			return "grist:null";
		else if(ingredient.type() == null)
			return "grist:null:"+ingredient.amount();
		else return "grist:"+getResourceLocation(ingredient)+":"+ingredient.amount();
	}
}