package com.mraof.minestuck.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class UnavailableGristCost implements GristCostRecipe
{
	private final Ingredient ingredient;
	@Nullable
	private final Integer priority;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public UnavailableGristCost(Ingredient ingredient, Optional<Integer> priority)
	{
		this.ingredient = ingredient;
		this.priority = priority.orElse(null);
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level level)
	{
		return ingredient.test(input.item());
	}
	
	@Override
	public int getPriority()
	{
		return Objects.requireNonNullElseGet(this.priority, () -> GristCostRecipe.defaultPriority(this.ingredient));
	}
	
	@Nullable
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return null;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, ResourceLocation recipeId)
	{
		GristCostRecipe.addSimpleCostProvider(consumer, this, this.ingredient);
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.UNAVAILABLE_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<UnavailableGristCost>
	{
		private static final MapCodec<UnavailableGristCost> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
						Codec.INT.optionalFieldOf("priority").forGetter(recipe -> Optional.ofNullable(recipe.priority))
				).apply(instance, UnavailableGristCost::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, UnavailableGristCost> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
		
		@Override
		public MapCodec<UnavailableGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, UnavailableGristCost> streamCodec()
		{
			return STREAM_CODEC;
		}
		
		private static void toNetwork(RegistryFriendlyByteBuf buffer, UnavailableGristCost recipe)
		{
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
			buffer.writeInt(recipe.getPriority());
		}
		
		private static UnavailableGristCost fromNetwork(RegistryFriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			int priority = buffer.readInt();
			
			return new UnavailableGristCost(ingredient, Optional.of(priority));
		}
	}
}
