package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSBlockEntities
{
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Minestuck.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<MSSignBlockEntity>> MOD_SIGN =
			BLOCK_ENTITIES.register("mod_sign", () ->
					BlockEntityType.Builder.of(MSSignBlockEntity::new,
							MSBlocks.PERFECTLY_GENERIC_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_SIGN.get(),
							MSBlocks.CARVED_SIGN.get(), MSBlocks.CARVED_WALL_SIGN.get(),
							MSBlocks.DEAD_SIGN.get(), MSBlocks.DEAD_WALL_SIGN.get(),
							MSBlocks.END_SIGN.get(), MSBlocks.END_WALL_SIGN.get(),
							MSBlocks.FROST_SIGN.get(), MSBlocks.FROST_WALL_SIGN.get(),
							MSBlocks.GLOWING_SIGN.get(), MSBlocks.GLOWING_WALL_SIGN.get(),
							MSBlocks.RAINBOW_SIGN.get(), MSBlocks.RAINBOW_WALL_SIGN.get(),
							MSBlocks.SHADEWOOD_SIGN.get(), MSBlocks.SHADEWOOD_WALL_SIGN.get(),
							MSBlocks.TREATED_SIGN.get(), MSBlocks.TREATED_WALL_SIGN.get(),
							MSBlocks.LACQUERED_SIGN.get(), MSBlocks.LACQUERED_WALL_SIGN.get(),
							MSBlocks.CINDERED_SIGN.get(), MSBlocks.CINDERED_WALL_SIGN.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<MSHangingSignBlockEntity>> MOD_HANGING_SIGN =
			BLOCK_ENTITIES.register("mod_hanging_sign", () ->
					BlockEntityType.Builder.of(MSHangingSignBlockEntity::new,
							MSBlocks.PERFECTLY_GENERIC_HANGING_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_HANGING_SIGN.get(),
							MSBlocks.CARVED_HANGING_SIGN.get(), MSBlocks.CARVED_WALL_HANGING_SIGN.get(),
							MSBlocks.DEAD_HANGING_SIGN.get(), MSBlocks.DEAD_WALL_HANGING_SIGN.get(),
							MSBlocks.END_HANGING_SIGN.get(), MSBlocks.END_WALL_HANGING_SIGN.get(),
							MSBlocks.FROST_HANGING_SIGN.get(), MSBlocks.FROST_WALL_HANGING_SIGN.get(),
							MSBlocks.GLOWING_HANGING_SIGN.get(), MSBlocks.GLOWING_WALL_HANGING_SIGN.get(),
							MSBlocks.RAINBOW_HANGING_SIGN.get(), MSBlocks.RAINBOW_WALL_HANGING_SIGN.get(),
							MSBlocks.SHADEWOOD_HANGING_SIGN.get(), MSBlocks.SHADEWOOD_WALL_HANGING_SIGN.get(),
							MSBlocks.TREATED_HANGING_SIGN.get(), MSBlocks.TREATED_WALL_HANGING_SIGN.get(),
							MSBlocks.LACQUERED_HANGING_SIGN.get(), MSBlocks.LACQUERED_WALL_HANGING_SIGN.get(),
							MSBlocks.CINDERED_HANGING_SIGN.get(), MSBlocks.CINDERED_WALL_HANGING_SIGN.get()).build(null));
	
	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
	
}