package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.*;
import com.mraof.minestuck.item.crafting.alchemy.generator.ContainerGristCost;
import com.mraof.minestuck.item.crafting.alchemy.generator.ItemSourceGristCost;
import com.mraof.minestuck.item.crafting.alchemy.generator.RecipeGeneratedGristCost;
import com.mraof.minestuck.item.crafting.alchemy.generator.TagSourceGristCost;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSRecipeTypes
{
	public static IRecipeType<IrradiatingRecipe> IRRADIATING_TYPE;
	public static IRecipeType<GristCostRecipe> GRIST_COST_TYPE;
	public static IRecipeType<AbstractCombinationRecipe> COMBINATION_TYPE;
	
	public static final IRecipeSerializer<NonMirroredRecipe> NON_MIRRORED = getNull();
	public static final CookingRecipeSerializer<IrradiatingRecipe> IRRADIATING = getNull();
	public static final IRecipeSerializer<IrradiatingFallbackRecipe> IRRADIATING_FALLBACK = getNull();
	public static final IRecipeSerializer<GristCost> GRIST_COST = getNull();
	public static final IRecipeSerializer<GristCost> CONTAINER_GRIST_COST = getNull();
	public static final IRecipeSerializer<GristCostRecipe> WILDCARD_GRIST_COST = getNull();
	public static final IRecipeSerializer<UnavailableGristCost> UNAVAILABLE_GRIST_COST = getNull();
	public static final IRecipeSerializer<RecipeGeneratedGristCost> RECIPE_GRIST_COST = getNull();
	public static final IRecipeSerializer<TagSourceGristCost> TAG_SOURCE_GRIST_COST = getNull();
	public static final IRecipeSerializer<ItemSourceGristCost> ITEM_SOURCE_GRIST_COST = getNull();
	public static final IRecipeSerializer<CombinationRecipe> COMBINATION = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		IRRADIATING_TYPE = IRecipeType.register(Minestuck.MOD_ID+":irradiating");
		GRIST_COST_TYPE = IRecipeType.register(Minestuck.MOD_ID+":grist_cost");
		COMBINATION_TYPE = IRecipeType.register(Minestuck.MOD_ID+":combination");
		
		IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
		registry.register(new NonMirroredRecipe.Serializer().setRegistryName("non_mirrored"));
		registry.register(new CookingRecipeSerializer<>(IrradiatingRecipe::new, 20).setRegistryName("irradiating"));
		registry.register(new IrradiatingFallbackRecipe.Serializer().setRegistryName("irradiating_fallback"));
		
		registry.register(new GristCost.Serializer().setRegistryName("grist_cost"));
		registry.register(new ContainerGristCost.Serializer().setRegistryName("container_grist_cost"));
		registry.register(new WildcardGristCost.Serializer().setRegistryName("wildcard_grist_cost"));
		registry.register(new UnavailableGristCost.Serializer().setRegistryName("unavailable_grist_cost"));
		registry.register(new RecipeGeneratedGristCost.Serializer().setRegistryName("recipe_grist_cost"));
		registry.register(new ItemSourceGristCost.Serializer().setRegistryName("item_source_grist_cost"));
		registry.register(new TagSourceGristCost.Serializer().setRegistryName("tag_source_grist_cost"));
		registry.register(new CombinationRecipe.Serializer().setRegistryName("combination"));
		
		MSLootTables.registerLootSerializers();	//Needs to be called somewhere, preferably during a registry event, and this is close enough
	}
}