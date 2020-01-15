package com.mraof.minestuck.item.crafting.alchemy.generator;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
import com.mraof.minestuck.item.crafting.alchemy.GristSet;
import com.mraof.minestuck.item.crafting.alchemy.GristType;
import com.mraof.minestuck.jei.JeiGristCost;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class GeneratedGristCost extends GristCostRecipe
{
	private final GristCostGenerator generator;
	
	private GeneratedGristCost(ResourceLocation id, GristCostGenerator generator)
	{
		super(id, null, 0);
		this.generator = generator;
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, GristType wildcardType, boolean shouldRoundDown, World world)
	{
		if(world != null)
		{
			return getCost(input.getItem());
		}
		return null;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn)
	{
		return worldIn != null && getCost(inv.getStackInSlot(0).getItem()) != null;
	}
	
	private GristSet getCost(Item item)
	{
		if(generator != null)	//We are client side and this recipe has a local cache
			return generator.getGristCost(item);
		else return GristCostGenerator.getInstance().getGristCost(item);
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return NonNullList.create();
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(World world)
	{
		if(generator != null)
			return generator.createJeiCosts();
		else return GristCostGenerator.getInstance().createJeiCosts();
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.GENERATED_GRIST_COST;
	}
	
	@Override
	public boolean isDynamic()
	{
		return true;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GeneratedGristCost>
	{
		@Override
		public GeneratedGristCost read(ResourceLocation recipeId, JsonObject json)
		{
			return new GeneratedGristCost(recipeId, null);
		}
		
		@Override
		public GeneratedGristCost read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			return new GeneratedGristCost(recipeId, GristCostGenerator.read(buffer));
		}
		
		@Override
		public void write(PacketBuffer buffer, GeneratedGristCost recipe)
		{
			GristCostGenerator.getInstance().write(buffer);
		}
	}
}