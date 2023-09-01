package com.mraof.minestuck.api.alchemy.recipe.combination;

import com.google.gson.JsonObject;
import com.mraof.minestuck.data.recipe.AdvancementFreeRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Used to datagen a regular combination recipe.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CombinationRecipeBuilder
{
	private final ItemStack output;
	private Ingredient input1, input2;
	private CombinationMode mode;
	private String suffix = "";
	
	private CombinationRecipeBuilder(ItemStack output)
	{
		this.output = Objects.requireNonNull(output);
	}
	
	public static CombinationRecipeBuilder of(Supplier<? extends ItemLike> supplier)
	{
		return of(supplier.get());
	}
	
	public static CombinationRecipeBuilder of(ItemLike output)
	{
		return of(new ItemStack(output.asItem()));
	}
	
	public static CombinationRecipeBuilder of(ItemStack output)
	{
		return new CombinationRecipeBuilder(output);
	}
	
	public CombinationRecipeBuilder input(TagKey<Item> tag)
	{
		return input(Ingredient.of(tag));
	}
	
	public CombinationRecipeBuilder input(ItemLike item)
	{
		return input(Ingredient.of(item));
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
	
	public CombinationRecipeBuilder namedInput(TagKey<Item> tag)
	{
		input(Ingredient.of(tag));
		return namedSource(tag.location().getPath());
	}
	
	public CombinationRecipeBuilder namedInput(ItemLike item)
	{
		input(Ingredient.of(item));
		return namedSource(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.asItem())).getPath());
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
		return mode(CombinationMode.AND);
	}
	
	public CombinationRecipeBuilder or()
	{
		return mode(CombinationMode.OR);
	}
	
	public CombinationRecipeBuilder mode(CombinationMode mode)
	{
		if(this.mode == null)
			this.mode = mode;
		else throw new IllegalStateException("Can't set mode twice");
		return this;
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem()));
		build(recipeSaver, new ResourceLocation(name.getNamespace(), name.getPath() + suffix));
	}
	
	public void buildFor(Consumer<FinishedRecipe> recipeSaver, String modId)
	{
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem()));
		build(recipeSaver, new ResourceLocation(modId, name.getPath() + suffix));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new Result(id.withPrefix("combinations/"), output, input1, input2, mode));
	}
	
	private record Result(ResourceLocation id, ItemStack output, Ingredient input1, Ingredient input2, CombinationMode mode) implements AdvancementFreeRecipe
	{
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			json.add("input1", input1.toJson());
			json.add("input2", input2.toJson());
			json.addProperty("mode", mode.asString());
			JsonObject outputJson = new JsonObject();
			ResourceLocation outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem()));
			outputJson.addProperty("item", outputId.toString());
			if(output.getCount() > 1)
			{
				outputJson.addProperty("count", output.getCount());
			}
			json.add("output", outputJson);
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.COMBINATION.get();
		}
	}
}
