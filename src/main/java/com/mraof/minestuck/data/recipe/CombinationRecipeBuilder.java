package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.item.crafting.alchemy.CombinationRegistry;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CombinationRecipeBuilder
{
	private final ItemStack output;
	private Ingredient input1, input2;
	private CombinationRegistry.Mode mode;
	private String suffix = "";
	
	private CombinationRecipeBuilder(ItemStack output)
	{
		this.output = Objects.requireNonNull(output);
	}
	
	public static CombinationRecipeBuilder of(Supplier<? extends IItemProvider> supplier)
	{
		return of(supplier.get());
	}
	
	public static CombinationRecipeBuilder of(IItemProvider output)
	{
		return of(new ItemStack(output.asItem()));
	}
	
	public static CombinationRecipeBuilder of(ItemStack output)
	{
		return new CombinationRecipeBuilder(output);
	}
	
	public CombinationRecipeBuilder input(Tag<Item> tag)
	{
		return input(Ingredient.fromTag(tag));
	}
	
	public CombinationRecipeBuilder input(IItemProvider item)
	{
		return input(Ingredient.fromItems(item));
	}
	
	public CombinationRecipeBuilder input(Ingredient ingredient)
	{
		if(input1 == null)
			input1 = Objects.requireNonNull(ingredient);
		else if(input2 == null)
			input2 = Objects.requireNonNull(ingredient);
		else throw new IllegalStateException("Can't set more than two inputs");
		return this;
	}
	
	public CombinationRecipeBuilder namedInput(Tag<Item> tag)
	{
		input(Ingredient.fromTag(tag));
		return namedSource(tag.getId().getPath());
	}
	
	public CombinationRecipeBuilder namedInput(IItemProvider item)
	{
		input(Ingredient.fromItems(item));
		return namedSource(Objects.requireNonNull(item.asItem().getRegistryName()).getPath());
	}
	
	public CombinationRecipeBuilder namedSource(String str)
	{
		if(suffix.isEmpty())
			suffix = "_from_" + str;
		else suffix = suffix + "_and_" + str;
		return this;
	}
	
	public CombinationRecipeBuilder and()
	{
		return mode(CombinationRegistry.Mode.MODE_AND);
	}
	
	public CombinationRecipeBuilder or()
	{
		return mode(CombinationRegistry.Mode.MODE_OR);
	}
	
	public CombinationRecipeBuilder mode(CombinationRegistry.Mode mode)
	{
		if(this.mode == null)
			this.mode = mode;
		else throw new IllegalStateException("Can't set mode twice");
		return this;
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(output.getItem().getRegistryName());
		build(recipeSaver, new ResourceLocation(name.getNamespace(), name.getPath() + suffix));
	}
	
	public void buildFor(Consumer<IFinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(output.getItem().getRegistryName());
		build(recipeSaver, new ResourceLocation(modId, name.getPath() + suffix));
	}
	
	public void build(Consumer<IFinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(new ResourceLocation(id.getNamespace(), "combinations/"+id.getPath()), output, input1, input2, mode));
	}
	
	public static class Result implements IFinishedRecipe
	{
		private final ResourceLocation id;
		private final ItemStack output;
		private final Ingredient input1, input2;
		private final CombinationRegistry.Mode mode;
		
		public Result(ResourceLocation id, ItemStack output, Ingredient input1, Ingredient input2, CombinationRegistry.Mode mode)
		{
			this.id = Objects.requireNonNull(id);
			this.output = Objects.requireNonNull(output);
			this.input1 = Objects.requireNonNull(input1, "Both input items must be set");
			this.input2 = Objects.requireNonNull(input2, "Both input items must be set");
			this.mode = Objects.requireNonNull(mode, "Combination mode must be set");
		}
		
		@Override
		public void serialize(JsonObject json)
		{
			json.add("input1", input1.serialize());
			json.add("input2", input2.serialize());
			json.addProperty("mode", mode.asString());
			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", output.getItem().getRegistryName().toString());
			if(output.getCount() > 1)
			{
				outputJson.addProperty("count", output.getCount());
			}
			json.add("output", outputJson);
		}
		
		@Override
		public ResourceLocation getID()
		{
			return id;
		}
		
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return MSRecipeTypes.COMBINATION;
		}
		
		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return null;
		}
	}
}