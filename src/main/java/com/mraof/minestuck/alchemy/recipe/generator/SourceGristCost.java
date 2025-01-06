package com.mraof.minestuck.alchemy.recipe.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratedCostProvider;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
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
public final class SourceGristCost implements GristCostRecipe
{
	private final Ingredient ingredient;
	private final List<Source> sources;
	private final float multiplier;
	private final GristSet.Immutable addedCost;
	@Nullable
	private final Integer priority;
	private final GeneratedGristCostCache cache = new GeneratedGristCostCache();
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public SourceGristCost(Ingredient ingredient, List<Source> sources, float multiplier, GristSet.Immutable addedCost, Optional<Integer> priority)
	{
		this.ingredient = ingredient;
		this.sources = sources;
		this.multiplier = multiplier;
		this.addedCost = addedCost;
		this.priority = priority.orElse(null);
	}
	
	@Override
	public boolean matches(SingleRecipeInput input, Level level)
	{
		return cache.getCachedCost() != null && ingredient.test(input.item());
	}
	
	@Override
	public int getPriority()
	{
		return Objects.requireNonNullElseGet(this.priority, () -> GristCostRecipe.defaultPriority(this.ingredient));
	}
	
	@Nullable
	private GristSet generateCost(GeneratorCallback callback, List<Source> sources, float multiplier, GristSet.Immutable addedCost)
	{
		MutableGristSet costSum = MutableGristSet.newDefault();
		for(Source source : sources)
		{
			GristSet sourceCost = source.getCostFor(callback);
			if(sourceCost != null)
				costSum.add(sourceCost);
			else return null;
		}
		
		return costSum.scale(multiplier, false).add(addedCost);
	}
	
	@Override
	public GristSet getGristCost(ItemStack input, @Nullable GristType wildcardType, boolean shouldRoundDown)
	{
		return GristCostRecipe.scaleToCountAndDurability(cache.getCachedCost(), input, shouldRoundDown);
	}
	
	@Override
	public void addCostProvider(BiConsumer<Item, GeneratedCostProvider> consumer, ResourceLocation recipeId)
	{
		GristCostRecipe.addCostProviderForIngredient(consumer, this.ingredient,
				this.cache.generatedProvider(callback -> generateCost(callback, sources, multiplier, addedCost)));
	}
	
	@Override
	public List<JeiGristCost> getJeiCosts(Level level)
	{
		if(cache.getCachedCost() != null)
			return Collections.singletonList(new JeiGristCost.Set(ingredient, cache.getCachedCost()));
		else return Collections.emptyList();
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.SOURCE_GRIST_COST.get();
	}
	
	public static class Serializer implements RecipeSerializer<SourceGristCost>
	{
		private static final MapCodec<SourceGristCost> CODEC = RecordCodecBuilder.mapCodec(instance ->
				instance.group(
						Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
						Source.CODEC.listOf().fieldOf("sources").forGetter(recipe -> recipe.sources),
						Codec.FLOAT.optionalFieldOf("multiplier", 1F).forGetter(recipe -> recipe.multiplier),
						GristSet.Codecs.MAP_CODEC.fieldOf("grist_cost").forGetter(recipe -> recipe.addedCost),
						Codec.INT.optionalFieldOf("priority").forGetter(recipe -> Optional.ofNullable(recipe.priority))
				).apply(instance, SourceGristCost::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, SourceGristCost> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);
		
		@Override
		public MapCodec<SourceGristCost> codec()
		{
			return CODEC;
		}
		
		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SourceGristCost> streamCodec()
		{
			return STREAM_CODEC;
		}
		
		private static void toNetwork(RegistryFriendlyByteBuf buffer, SourceGristCost recipe)
		{
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
			buffer.writeInt(recipe.getPriority());
			recipe.cache.toNetwork(buffer);
		}
		
		private static SourceGristCost fromNetwork(RegistryFriendlyByteBuf buffer)
		{
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			int priority = buffer.readInt();
			
			var recipe = new SourceGristCost(ingredient, Collections.emptyList(), 1, GristSet.EMPTY, Optional.of(priority));
			recipe.cache.fromNetwork(buffer);
			
			return recipe;
		}
	}
	
	public sealed interface Source
	{
		//todo handle resource location parse errors
		Codec<Source> CODEC = Codec.STRING.comapFlatMap(
				name -> name.startsWith("#")
						? ResourceLocation.read(name.substring(1)).map(TagSource::new)
						: ResourceLocation.read(name).map(ItemSource::new),
				Source::toString
				);
		
		@Nullable
		GristSet getCostFor(GeneratorCallback callback);
	}
	
	public record ItemSource(Item item) implements Source
	{
		private ItemSource(ResourceLocation name)
		{
			this(BuiltInRegistries.ITEM.get(name));
		}
		
		@Override
		public GristSet getCostFor(GeneratorCallback callback)
		{
			return callback.lookupCostFor(item);
		}
		
		@Override
		public String toString()
		{
			return BuiltInRegistries.ITEM.getKey(this.item).toString();
		}
	}
	
	public record TagSource(TagKey<Item> tag) implements Source
	{
		private TagSource(ResourceLocation name)
		{
			this(TagKey.create(Registries.ITEM, name));
		}
		
		@Override
		public GristSet getCostFor(GeneratorCallback callback)
		{
			GristSet maxCost = null;
			for(Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(this.tag))
			{
				GristSet cost = callback.lookupCostFor(item.value());
				
				if(cost != null && (maxCost == null || cost.getValue() > maxCost.getValue()))
					maxCost = cost;
			}
			return maxCost;
		}
		
		@Override
		public String toString()
		{
			return "#" + this.tag.location();
		}
	}
}
