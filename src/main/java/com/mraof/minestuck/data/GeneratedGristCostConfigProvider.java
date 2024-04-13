package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.*;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GeneratedGristCostConfigProvider implements DataProvider
{
	private final PackOutput output;
	private final String modid;
	private final List<RecipeGeneratedCostHandler.SourceEntry> entries = new ArrayList<>();
	
	public GeneratedGristCostConfigProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void addEntries()
	{
		serializer(RecipeSerializer.SHAPED_RECIPE);
		serializer(RecipeSerializer.SHAPELESS_RECIPE);
		serializer(MSRecipeTypes.NON_MIRRORED.get());
		type(RecipeType.STONECUTTING);
		serializer(RecipeSerializer.SMITHING_TRANSFORM, SmithingInterpreter.INSTANCE);
		type(RecipeType.SMELTING, new CookingCostInterpreter(GristTypes.TAR.get().amount(1)));
		type(MSRecipeTypes.IRRADIATING_TYPE.get(), new CookingCostInterpreter(GristTypes.URANIUM.get().amount(1)));
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		addEntries();
		
		Path jsonPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
				.resolve(modid).resolve(RecipeGeneratedCostHandler.PATH);
		
		JsonElement json = RecipeGeneratedCostHandler.SourceEntry.LIST_CODEC.encodeStart(JsonOps.INSTANCE, this.entries)
				.getOrThrow(false, LOGGER::error);
		
		return DataProvider.saveStable(cache, json, jsonPath);
	}
	
	protected void recipe(ResourceLocation location)
	{
		recipe(location, DefaultInterpreter.INSTANCE);
	}
	
	protected void serializer(RecipeSerializer<?> serializer)
	{
		serializer(serializer, DefaultInterpreter.INSTANCE);
	}
	
	protected void type(RecipeType<?> type)
	{
		type(type, DefaultInterpreter.INSTANCE);
	}
	
	protected void recipe(ResourceLocation location, RecipeInterpreter interpreter)
	{
		addEntry(new RecipeSource.SingleRecipe(location), interpreter);
	}
	
	protected void serializer(RecipeSerializer<?> serializer, RecipeInterpreter interpreter)
	{
		addEntry(new RecipeSource.BySerializer(serializer), interpreter);
	}
	
	protected void type(RecipeType<?> type, RecipeInterpreter interpreter)
	{
		addEntry(new RecipeSource.ByType(type), interpreter);
	}
	
	private void addEntry(RecipeSource source, RecipeInterpreter interpreter)
	{
		entries.add(new RecipeGeneratedCostHandler.SourceEntry(source, interpreter));
	}
	
	@Override
	public String getName()
	{
		return "Minestuck generated grist costs config";
	}
}
