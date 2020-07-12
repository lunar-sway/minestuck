package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.jei.JeiCombination;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CombinationRecipe extends AbstractCombinationRecipe
{
	private final Ingredient input1, input2;
	private final CombinationMode mode;
	private final ItemStack output;
	
	public CombinationRecipe(ResourceLocation id, Ingredient input1, Ingredient input2, CombinationMode mode, ItemStack output)
	{
		super(id);
		this.input1 = input1;
		this.input2 = input2;
		this.mode = mode;
		this.output = output;
	}
	
	@Override
	public boolean matches(ItemCombiner inv, World worldIn)
	{
		ItemStack item1 = AlchemyHelper.getDecodedItem(inv.getStackInSlot(0)), item2 = AlchemyHelper.getDecodedItem(inv.getStackInSlot(1));
		return inv.getMode() == this.mode && (input1.test(item1) && input2.test(item2) || input2.test(item1) && input1.test(item2));
	}
	
	@Override
	public ItemStack getCraftingResult(ItemCombiner inv)
	{
		return getRecipeOutput();
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return output.copy();
	}
	
	@Override
	public List<JeiCombination> getJeiCombinations()
	{
		return Collections.singletonList(new JeiCombination(input1, input2, output, mode));
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.COMBINATION;
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CombinationRecipe>
	{
		@Override
		public CombinationRecipe read(ResourceLocation recipeId, JsonObject json)
		{
			Ingredient input1 = Ingredient.deserialize(json.get("input1"));
			Ingredient input2 = Ingredient.deserialize(json.get("input2"));
			CombinationMode mode = CombinationMode.fromString(JSONUtils.getString(json, "mode"));
			ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
			
			return new CombinationRecipe(recipeId, input1, input2, mode, output);
		}
		
		@Nullable
		@Override
		public CombinationRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			Ingredient input1 = Ingredient.read(buffer);
			Ingredient input2 = Ingredient.read(buffer);
			CombinationMode mode = CombinationMode.fromBoolean(buffer.readBoolean());
			ItemStack output = buffer.readItemStack();
			
			return new CombinationRecipe(recipeId, input1, input2, mode, output);
		}
		
		@Override
		public void write(PacketBuffer buffer, CombinationRecipe recipe)
		{
			recipe.input1.write(buffer);
			recipe.input2.write(buffer);
			buffer.writeBoolean(recipe.mode.asBoolean());
			buffer.writeItemStack(recipe.output);
		}
	}
}