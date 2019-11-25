package com.mraof.minestuck.inventory.captchalogue;

import com.google.common.collect.Maps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModusTypes
{
	private static final ResourceLocation ITEM_TO_MODUS = new ResourceLocation(Minestuck.MOD_ID, "item_to_modus_map");
	public static IForgeRegistry<ModusType<?>> REGISTRY;
	private static Map<Item, ModusType<?>> itemToModusMap;
	
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
	
	public static ModusType<?> getTypeFromItem(Item item)
	{
		return itemToModusMap.get(item);
	}
	
	@SubscribeEvent
	@SuppressWarnings("unchecked")
	public static void onRegistryNewRegistry(final RegistryEvent.NewRegistry event)
	{
		REGISTRY = new RegistryBuilder<ModusType<?>>()
				.setName(new ResourceLocation(Minestuck.MOD_ID, "modus_type"))
				.setType((Class<ModusType<?>>) (Class<?>) ModusType.class)
				.addCallback(ModusCallbacks.INSTANCE)
				.create();
		itemToModusMap = REGISTRY.getSlaveMap(ITEM_TO_MODUS, Map.class);
	}
	
	@SubscribeEvent
	public static void registerTypes(final RegistryEvent.Register<ModusType<?>> event)
	{
		event.getRegistry().register(new ModusType<>(StackModus::new, MSItems.STACK_MODUS_CARD).setRegistryName("stack"));
		event.getRegistry().register(new ModusType<>(QueueModus::new, MSItems.QUEUE_MODUS_CARD).setRegistryName("queue"));
		event.getRegistry().register(new ModusType<>(QueueStackModus::new, MSItems.QUEUESTACK_MODUS_CARD).setRegistryName("queue_stack"));
		event.getRegistry().register(new ModusType<>(TreeModus::new, MSItems.TREE_MODUS_CARD).setRegistryName("tree"));
		event.getRegistry().register(new ModusType<>(HashMapModus::new, MSItems.HASHMAP_MODUS_CARD).setRegistryName("hash_map"));
		event.getRegistry().register(new ModusType<>(SetModus::new, MSItems.SET_MODUS_CARD).setRegistryName("set"));
	}
	
	private static class ModusCallbacks implements IForgeRegistry.AddCallback<ModusType<?>>, IForgeRegistry.ClearCallback<ModusType<?>>, IForgeRegistry.CreateCallback<ModusType<?>>
	{
		private static final ModusCallbacks INSTANCE = new ModusCallbacks();
		
		@Override
		public void onAdd(IForgeRegistryInternal<ModusType<?>> owner, RegistryManager stage, int id, ModusType<?> obj, @Nullable ModusType<?> oldObj)
		{
			@SuppressWarnings("unchecked")
			Map<Item, ModusType<?>> itemToModus = owner.getSlaveMap(ITEM_TO_MODUS, Map.class);
			
			if(oldObj != null && oldObj.getItem() != null)
			{
				itemToModus.remove(oldObj.getItem());
			}
			
			if(obj.getItem() != null)
			{
				itemToModus.put(obj.getItem(), obj);
			}
		}
		
		@Override
		public void onClear(IForgeRegistryInternal<ModusType<?>> owner, RegistryManager stage)
		{
			owner.getSlaveMap(ITEM_TO_MODUS, Map.class).clear();
		}
		
		@Override
		public void onCreate(IForgeRegistryInternal<ModusType<?>> owner, RegistryManager stage)
		{
			owner.setSlaveMap(ITEM_TO_MODUS, Maps.newHashMap());
		}
	}
}