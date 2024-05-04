package com.mraof.minestuck.inventory.captchalogue;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class ModusTypes
{
	public static final DeferredRegister<ModusType<?>> REGISTER = DeferredRegister.create(new ResourceLocation(Minestuck.MOD_ID, "modus_type"), Minestuck.MOD_ID);
	
	public static final Registry<ModusType<?>> REGISTRY = REGISTER.makeRegistry(builder -> builder.sync(true));
	
	public static final Supplier<ModusType<StackModus>> STACK = REGISTER.register("stack", () -> new ModusType<>(StackModus::new, MSItems.STACK_MODUS_CARD));
	public static final Supplier<ModusType<QueueModus>> QUEUE = REGISTER.register("queue", () -> new ModusType<>(QueueModus::new, MSItems.QUEUE_MODUS_CARD));
	public static final Supplier<ModusType<QueueStackModus>> QUEUE_STACK = REGISTER.register("queue_stack", () -> new ModusType<>(QueueStackModus::new, MSItems.QUEUESTACK_MODUS_CARD));
	public static final Supplier<ModusType<TreeModus>> TREE = REGISTER.register("tree", () -> new ModusType<>(TreeModus::new, MSItems.TREE_MODUS_CARD));
	public static final Supplier<ModusType<HashMapModus>> HASH_MAP = REGISTER.register("hash_map", () -> new ModusType<>(HashMapModus::new, MSItems.HASHMAP_MODUS_CARD));
	public static final Supplier<ModusType<SetModus>> SET = REGISTER.register("set", () -> new ModusType<>(SetModus::new, MSItems.SET_MODUS_CARD));
	
	@Nullable
	public static ModusType<?> getTypeFromItem(Item item)
	{
		return REGISTRY.stream().filter(modusType -> modusType.getItem() == item).findAny().orElse(null);
	}
}