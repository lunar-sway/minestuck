package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiGristCost;
import com.mraof.minestuck.util.ExtraJSONUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WildcardGristCost extends GristCostRecipe
{
	public final long wildcardCost;
	private JeiGristCost jeiCost;
	
	public WildcardGristCost(ResourceLocation id, Ingredient ingredient, long wildcardCost, Integer priority)
	{
		super(id, ingredient, priority);
		this.wildcardCost = wildcardCost;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, World world)
	{
		return wildcardType != null ? scaleToCountAndDurability(new GristSet(wildcardType, wildcardCost), input, shouldRoundDown).asImmutable() : null;
	}
	
	@Override
	public boolean canPickWildcard()
	{
		return true;
	}
	
	@Override
	public JeiGristCost getJeiCost()
	{
		if(jeiCost == null)
			jeiCost = new JeiGristCost.Wildcard(wildcardCost);
		return jeiCost;
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.WILDCARD_GRIST_COST;
	}
	
	public static class Serializer extends AbstractSerializer<WildcardGristCost>
	{
		@Override
		protected WildcardGristCost read(ResourceLocation recipeId, JsonObject json, Ingredient ingredient, Integer priority)
		{
			long wildcardCost = ExtraJSONUtils.getLong(json, "grist_cost");
			return new WildcardGristCost(recipeId, ingredient, wildcardCost, priority);
		}
		
		@Override
		protected WildcardGristCost read(ResourceLocation recipeId, PacketBuffer buffer, Ingredient ingredient, int priority)
		{
			long wildcardCost = buffer.readLong();
			return new WildcardGristCost(recipeId, ingredient, wildcardCost, priority);
		}
		
		@Override
		public void write(PacketBuffer buffer, WildcardGristCost recipe)
		{
			super.write(buffer, recipe);
			buffer.writeLong(recipe.wildcardCost);
		}
	}
}
