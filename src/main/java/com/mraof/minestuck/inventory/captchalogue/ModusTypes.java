package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModusTypes
{
	public static IForgeRegistry<ModusType<?>> REGISTRY;
	
	public static final ModusType<StackModus> STACK = getNull();
	public static final ModusType<QueueModus> QUEUE = getNull();
	public static final ModusType<QueueStackModus> QUEUE_STACK = getNull();
	public static final ModusType<TreeModus> TREE = getNull();
	public static final ModusType<HashMapModus> HASH_MAP = getNull();
	public static final ModusType<SetModus> SET = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@Nullable
	public static ModusType<?> getTypeFromItem(Item item)
	{
		return REGISTRY.getValues().stream().filter(modusType -> modusType.getItem() == item).findAny().orElse(null);
	}
	
	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void onRegistryNewRegistry(final NewRegistryEvent event)
	{
		event.create(new RegistryBuilder<ModusType<?>>()
						.setName(new ResourceLocation(Minestuck.MOD_ID, "modus_type"))
						.setType((Class<ModusType<?>>) (Class<?>) ModusType.class),
				registry -> REGISTRY = registry);
	}
	
	@SubscribeEvent
	public static void registerTypes(final RegistryEvent.Register<ModusType<?>> event)
	{
		event.getRegistry().register(new ModusType<>(StackModus::new, MSItems.STACK_MODUS_CARD.get()).setRegistryName("stack"));
		event.getRegistry().register(new ModusType<>(QueueModus::new, MSItems.QUEUE_MODUS_CARD.get()).setRegistryName("queue"));
		event.getRegistry().register(new ModusType<>(QueueStackModus::new, MSItems.QUEUESTACK_MODUS_CARD.get()).setRegistryName("queue_stack"));
		event.getRegistry().register(new ModusType<>(TreeModus::new, MSItems.TREE_MODUS_CARD.get()).setRegistryName("tree"));
		event.getRegistry().register(new ModusType<>(HashMapModus::new, MSItems.HASHMAP_MODUS_CARD.get()).setRegistryName("hash_map"));
		event.getRegistry().register(new ModusType<>(SetModus::new, MSItems.SET_MODUS_CARD.get()).setRegistryName("set"));
	}
}