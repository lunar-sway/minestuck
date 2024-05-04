package com.mraof.minestuck.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationMode;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinerContainer;
import com.mraof.minestuck.api.alchemy.recipe.combination.JeiCombination;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RegularCombinationRecipe implements CombinationRecipe
{
	private final Ingredient input1, input2;
	private final CombinationMode mode;
	private final ItemStack output;
	
	public RegularCombinationRecipe(Ingredient input1, Ingredient input2, CombinationMode mode, ItemStack output)
	{
		this.input1 = input1;
		this.input2 = input2;
		this.mode = mode;
		this.output = output;
	}
	
	@Override
	public boolean matches(CombinerContainer inv, Level level)
	{
		ItemStack item1 = AlchemyHelper.getDecodedItem(inv.getItem(0)), item2 = AlchemyHelper.getDecodedItem(inv.getItem(1));
		return inv.getMode() == this.mode && (input1.test(item1) && input2.test(item2) || input2.test(item1) && input1.test(item2));
	}
	
	@Override
	public ItemStack assemble(CombinerContainer inv, RegistryAccess registryAccess)
	{
		return output;
	}
	
	@Override
	public boolean isSpecial()	//Makes sure that the recipe is not unlockable (because recipe book categories are hardcoded to vanilla categories)
	{
		return true;
	}
	
	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess)
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
		private static final Codec<RegularCombinationRecipe> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("input1").forGetter(recipe -> recipe.input1),
						Ingredient.CODEC_NONEMPTY.fieldOf("input2").forGetter(recipe -> recipe.input2),
						CombinationMode.CODEC.fieldOf("mode").forGetter(recipe -> recipe.mode),
						ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output)
				).apply(instance, RegularCombinationRecipe::new));
		
		@Override
		public Codec<RegularCombinationRecipe> codec()
		{
			return CODEC;
		}
		
		@Override
		public RegularCombinationRecipe fromNetwork(FriendlyByteBuf buffer)
		{
			Ingredient input1 = Ingredient.fromNetwork(buffer);
			Ingredient input2 = Ingredient.fromNetwork(buffer);
			CombinationMode mode = CombinationMode.fromBoolean(buffer.readBoolean());
			ItemStack output = buffer.readItem();
			
			return new RegularCombinationRecipe(input1, input2, mode, output);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, RegularCombinationRecipe recipe)
		{
			recipe.input1.toNetwork(buffer);
			recipe.input2.toNetwork(buffer);
			buffer.writeBoolean(recipe.mode.asBoolean());
			buffer.writeItem(recipe.output);
		}
	}
}