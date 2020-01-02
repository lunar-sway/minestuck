package com.mraof.minestuck.item.crafting;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.GristCostRecipe;
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
	public static IRecipeType<IrradiatingRecipe> IRRADIATING_TYPE = IRecipeType.register(Minestuck.MOD_ID+":irradiating");
	public static IRecipeType<GristCostRecipe> GRIST_COST_TYPE = IRecipeType.register(Minestuck.MOD_ID+":grist_cost");
	
	public static final IRecipeSerializer<NonMirroredRecipe> NON_MIRRORED = getNull();
	public static final CookingRecipeSerializer<IrradiatingRecipe> IRRADIATING = getNull();
	public static final IRecipeSerializer<IrradiatingFallbackRecipe> IRRADIATING_FALLBACK = getNull();
	public static final IRecipeSerializer<GristCostRecipe> GRIST_COST = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event)
	{
		IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
		registry.register(new NonMirroredRecipe.Serializer().setRegistryName("non_mirrored"));
		registry.register(new CookingRecipeSerializer<>(IrradiatingRecipe::new, 20).setRegistryName("irradiating"));
		registry.register(new IrradiatingFallbackRecipe.Serializer().setRegistryName("irradiating_fallback"));
		registry.register(new GristCostRecipe.Serializer().setRegistryName("grist_cost"));
		
		MSLootTables.registerLootSerializers();	//Needs to be called somewhere, preferably during a registry event, and this is close enough
	}
}