package com.mraof.minestuck.alchemy.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.api.alchemy.recipe.combination.JeiCombination;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record RegularCombinationRecipe(Ingredient input1, Ingredient input2, CombinationMode mode, ItemStack output) implements CombinationRecipe
{
	@Override
	public boolean matches(CombinerContainer inv, Level level)
	{
		ItemStack item1 = AlchemyHelper.getDecodedItem(inv.getItem(0)), item2 = AlchemyHelper.getDecodedItem(inv.getItem(1));
		return inv.getMode() == this.mode && (input1.test(item1) && input2.test(item2) || input2.test(item1) && input1.test(item2));
	}
	
	@Override
	public ItemStack assemble(CombinerContainer pInput, HolderLookup.Provider pRegistries)
	{
		return output;
	}
	
	@Override
	public boolean isSpecial()	//Makes sure that the recipe is not unlockable (because recipe book categories are hardcoded to vanilla categories)
	{
		return true;
	}
	
	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries)
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public List<JeiCombination> getJeiCombinations()
	{
		return Collections.singletonList(new JeiCombination(input1, input2, output, mode));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.COMBINATION.get();
	}
	
	public static class Serializer implements RecipeSerializer<RegularCombinationRecipe>
	{
		private static final MapCodec<RegularCombinationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("input1").forGetter(recipe -> recipe.input1),
						Ingredient.CODEC_NONEMPTY.fieldOf("input2").forGetter(recipe -> recipe.input2),
						CombinationMode.CODEC.fieldOf("mode").forGetter(recipe -> recipe.mode),
						ItemStack.SIMPLE_ITEM_CODEC.fieldOf("output").forGetter(recipe -> recipe.output)
				).apply(instance, RegularCombinationRecipe::new));
		
		private static final StreamCodec<RegistryFriendlyByteBuf, RegularCombinationRecipe> STREAM_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC,
				RegularCombinationRecipe::input1,
				Ingredient.CONTENTS_STREAM_CODEC,
				RegularCombinationRecipe::input2,
				NeoForgeStreamCodecs.enumCodec(CombinationMode.class),
				RegularCombinationRecipe::mode,
				ItemStack.STREAM_CODEC,
				RegularCombinationRecipe::output,
				RegularCombinationRecipe::new
		);
		
		@Override
		public MapCodec<RegularCombinationRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RegularCombinationRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}