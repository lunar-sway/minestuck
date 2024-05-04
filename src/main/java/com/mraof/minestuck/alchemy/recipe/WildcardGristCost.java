package com.mraof.minestuck.alchemy.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WildcardGristCost implements GristCostRecipe
{
	private final Ingredient ingredient;
	private final Integer priority;
	private final long wildcardCost;
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public WildcardGristCost(Ingredient ingredient, long wildcardCost, Optional<Integer> priority)
	{
		this.ingredient = ingredient;
		this.priority = priority.orElse(null);
		this.wildcardCost = wildcardCost;
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
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return wildcardType != null ? GristCostRecipe.scaleToCountAndDurability(wildcardType.amount(wildcardCost), input, shouldRoundDown) : null;
	}
	
	@Override
	public boolean canPickWildcard()
	{
		return true;
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, ResourceLocation recipeId)
	{
		GristCostRecipe.addSimpleCostProvider(consumer, this, this.ingredient);
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		return Collections.singletonList(new JeiGristCost.Wildcard(ingredient, wildcardCost));
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.WILDCARD_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<WildcardGristCost>
	{
		private static final Codec<WildcardGristCost> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
						Codec.LONG.fieldOf("grist_cost").forGetter(recipe -> recipe.wildcardCost),
						ExtraCodecs.strictOptionalField(Codec.INT, "priority").forGetter(recipe -> Optional.ofNullable(recipe.priority))
				).apply(instance, WildcardGristCost::new));
		
		@Override
		public Codec<WildcardGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buffer, WildcardGristCost recipe)
		{
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.getPriority());
			buffer.writeLong(recipe.wildcardCost);
		}
		
		@Override
		public WildcardGristCost fromNetwork(FriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int priority = buffer.readInt();
			long wildcardCost = buffer.readLong();
			
			return new WildcardGristCost(ingredient, wildcardCost, Optional.of(priority));
		}
	}
}
