package com.mraof.minestuck.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
	public boolean matches(Container inv, Level level)
	{
		return ingredient.test(inv.getItem(0));
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
		private static final Codec<UnavailableGristCost> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
						ExtraCodecs.strictOptionalField(Codec.INT, "priority").forGetter(recipe -> Optional.ofNullable(recipe.priority))
				).apply(instance, UnavailableGristCost::new));
		
		@Override
		public Codec<UnavailableGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, UnavailableGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
		}
		
		@Override
		public UnavailableGristCost fromNetwork(FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			
			return new UnavailableGristCost(ingredient, Optional.of(priority));
		}
	}
}
