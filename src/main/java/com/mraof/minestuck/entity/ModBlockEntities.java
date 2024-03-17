package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;

public class ModBlockEntities {
	
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Minestuck.MOD_ID);
		
	public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
			BLOCK_ENTITIES.register("mod_sign", () ->
					BlockEntityType.Builder.of(ModSignBlockEntity::new,
							MSBlocks.CARVED_SIGN.get(), MSBlocks.CARVED_WALL_SIGN.get(),
							MSBlocks.DEAD_SIGN.get(), MSBlocks.DEAD_WALL_SIGN.get(),
							MSBlocks.END_SIGN.get(), MSBlocks.END_WALL_SIGN.get(),
							MSBlocks.FROST_SIGN.get(), MSBlocks.FROST_WALL_SIGN.get(),
							MSBlocks.GLOWING_SIGN.get(), MSBlocks.GLOWING_WALL_SIGN.get(),
							MSBlocks.RAINBOW_SIGN.get(), MSBlocks.RAINBOW_WALL_SIGN.get(),
							MSBlocks.SHADEWOOD_SIGN.get(), MSBlocks.SHADEWOOD_WALL_SIGN.get(),
							MSBlocks.TREATED_SIGN.get(), MSBlocks.TREATED_WALL_SIGN.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN =
			BLOCK_ENTITIES.register("mod_hanging_sign", () ->
					BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
							MSBlocks.CARVED_HANGING_SIGN.get(), MSBlocks.CARVED_WALL_HANGING_SIGN.get(),
							MSBlocks.DEAD_HANGING_SIGN.get(), MSBlocks.DEAD_WALL_HANGING_SIGN.get(),
							MSBlocks.END_HANGING_SIGN.get(), MSBlocks.END_WALL_HANGING_SIGN.get(),
							MSBlocks.FROST_HANGING_SIGN.get(), MSBlocks.FROST_WALL_HANGING_SIGN.get(),
							MSBlocks.GLOWING_HANGING_SIGN.get(), MSBlocks.GLOWING_WALL_HANGING_SIGN.get(),
							MSBlocks.RAINBOW_HANGING_SIGN.get(), MSBlocks.RAINBOW_WALL_HANGING_SIGN.get(),
							MSBlocks.SHADEWOOD_HANGING_SIGN.get(), MSBlocks.SHADEWOOD_WALL_HANGING_SIGN.get(),
							MSBlocks.TREATED_HANGING_SIGN.get(), MSBlocks.TREATED_WALL_HANGING_SIGN.get()).build(null));
	
	
	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
	
}
