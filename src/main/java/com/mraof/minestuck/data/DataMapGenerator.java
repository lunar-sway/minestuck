package com.mraof.minestuck.data;

import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.ParrotImitation;

import java.util.concurrent.CompletableFuture;

public class DataMapGenerator extends DataMapProvider
{
	
	public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider)
	{
		super(output, provider);
	}
	
	@Override
	protected void gather()
	{
		/* COMPOSTABLES */
		
		var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);

		//foliage
		compostables.add(MSTags.Items.ASPECT_SAPLINGS, new Compostable(0.2f),false);
		compostables.add(MSTags.Items.ASPECT_LEAVES, new Compostable(0.2f),false);
		
		compostables.add(MSItems.SHADEWOOD_SAPLING, new Compostable(0.2f), false);
		compostables.add(MSItems.SHADEWOOD_LEAVES, new Compostable(0.2f), false);
		compostables.add(MSItems.SHROOMY_SHADEWOOD_LEAVES, new Compostable(0.2f), false);
		compostables.add(MSItems.FROST_SAPLING, new Compostable(0.2f), false);
		compostables.add(MSItems.FROST_LEAVES_FLOWERING, new Compostable(0.2f), false);
		compostables.add(MSItems.RAINBOW_SAPLING, new Compostable(0.2f), false);
		compostables.add(MSItems.RAINBOW_LEAVES, new Compostable(0.2f), false);
		compostables.add(MSItems.END_SAPLING, new Compostable(0.2f), false);
		compostables.add(MSItems.END_LEAVES, new Compostable(0.2f), false);
		
		compostables.add(MSItems.DESERT_BUSH, new Compostable(0.5f), false);
		compostables.add(MSItems.BLOOMING_CACTUS, new Compostable(0.5f), false);
		compostables.add(MSItems.END_GRASS, new Compostable(0.5f), false);
		
		//bugs
		compostables.add(MSItems.CHOCOLATE_BEETLE, new Compostable(0.1f), false);
		compostables.add(MSItems.CONE_OF_FLIES, new Compostable(0.1f), false);
		compostables.add(MSItems.GRASSHOPPER, new Compostable(0.1f), false);
		compostables.add(MSItems.CICADA, new Compostable(0.1f), false);

		//fruits & vegetables
		compostables.add(MSItems.ONION, new Compostable(0.5f), false);
		compostables.add(MSItems.DESERT_FRUIT, new Compostable(0.5f), false);
		compostables.add(MSItems.FUNGAL_SPORE, new Compostable(0.5f), false);
		compostables.add(MSItems.STRAWBERRY_CHUNK, new Compostable(0.5f), false);

		//fungi
		compostables.add(MSItems.MOREL_MUSHROOM, new Compostable(0.65f), false);
		compostables.add(MSItems.SUSHROOM, new Compostable(0.65f), false);
		compostables.add(MSItems.GLOWING_MUSHROOM, new Compostable(0.65f), false);
		compostables.add(MSItems.GLOWING_MUSHROOM_VINES, new Compostable(0.65f), false);
		compostables.add(MSItems.COCOA_WART, new Compostable(0.65f), false);
		
		//edibles
		compostables.add(MSItems.STRAWBERRY, new Compostable(0.65f), false);
		compostables.add(MSItems.FRENCH_FRY, new Compostable(0.85f), false);
		compostables.add(MSItems.SPOREO, new Compostable(0.85f), false);
		compostables.add(MSItems.GRUB_SAUCE, new Compostable(0.85f), false);
		compostables.add(MSItems.SURPRISE_EMBRYO, new Compostable(0.85f), false);
		compostables.add(MSItems.BUG_MAC, new Compostable(1.0f), false);
		compostables.add(MSItems.APPLE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.BLUE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.COLD_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.RED_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.HOT_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.REVERSE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.FUCHSIA_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.NEGATIVE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.CARROT_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.LARGE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.PINK_FROSTED_TOP_LARGE_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.CHOCOLATEY_CAKE, new Compostable(1.0f), false);
		compostables.add(MSItems.MOON_CAKE, new Compostable(1.0f), false);

		//weapons
		compostables.add(MSItems.FUDGESICKLE, new Compostable(1.0f), false);
		compostables.add(MSItems.DOCTOR_DETERRENT, new Compostable(1.0f), false);
		compostables.add(MSItems.CHOCO_LOCO_WOODSPLITTER, new Compostable(1.0f), false);
		compostables.add(MSItems.STALE_BAGUETTE, new Compostable(1.0f), false);
		compostables.add(MSItems.MELONSBANE, new Compostable(1.0f), false);
		compostables.add(MSItems.CROP_CHOP, new Compostable(1.0f), false);
		compostables.add(MSItems.QUENCH_CRUSHER, new Compostable(1.0f), false);
		compostables.add(MSItems.PARADISES_PORTABELLO, new Compostable(1.0f), false);
		
		/* FUELS */
		
		var fuels = this.builder(NeoForgeDataMaps.FURNACE_FUELS);
		
		fuels.add(MSTags.Items.ASPECT_SAPLINGS, new FurnaceFuel(100), false);
		fuels.add(MSTags.Items.ASPECT_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSTags.Items.ASPECT_WOOD, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BLOOD_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.BREATH_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.DOOM_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.HEART_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HEART_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.HOPE_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIFE_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.LIGHT_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.MIND_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_PLANKS_CARVED_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.MIND_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.RAGE_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.SPACE_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.TIME_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.TIME_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(AspectTreeBlocks.VOID_ASPECT_DOOR_ITEM, new FurnaceFuel(200), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_TRAPDOOR_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_FENCE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_FENCE_GATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_CARVED_PLANKS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_STAIRS_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_SLAB_ITEM, new FurnaceFuel(150), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_BUTTON_ITEM, new FurnaceFuel(100), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_PRESSURE_PLATE_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_BOOKSHELF_ITEM, new FurnaceFuel(300), false);
		fuels.add(AspectTreeBlocks.VOID_ASPECT_LADDER_ITEM, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.GLOWING_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.GLOWING_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.GLOWING_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.GLOWING_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.GLOWING_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.FROST_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.FROST_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.FROST_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.FROST_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.FROST_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.RAINBOW_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.RAINBOW_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.RAINBOW_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.RAINBOW_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.RAINBOW_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.END_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.END_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.END_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.END_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.END_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.VINE_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSTags.Items.FLOWERY_VINE_LOGS, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.DEAD_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.DEAD_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.DEAD_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.DEAD_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.DEAD_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.CINDERED_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.CINDERED_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.CINDERED_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.CINDERED_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.CINDERED_LADDER, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.PETRIFIED_LOGS, new FurnaceFuel(300), false);
		
		fuels.add(MSTags.Items.SHADEWOOD_LOGS, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_DOOR, new FurnaceFuel(200), false);
		fuels.add(MSItems.SHADEWOOD_TRAPDOOR, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_FENCE, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_FENCE_GATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_STAIRS, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_SLAB, new FurnaceFuel(150), false);
		fuels.add(MSItems.SHADEWOOD_BUTTON, new FurnaceFuel(100), false);
		fuels.add(MSItems.SHADEWOOD_PRESSURE_PLATE, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_BOOKSHELF, new FurnaceFuel(300), false);
		fuels.add(MSItems.SHADEWOOD_LADDER, new FurnaceFuel(300), false);
		
		/* PARROT IMITATIONS */
		
		var parrot = this.builder(NeoForgeDataMaps.PARROT_IMITATIONS);
		parrot.add((Holder<EntityType<?>>) MSEntityTypes.IMP, new ParrotImitation(MSSoundEvents.ENTITY_IMP_AMBIENT.get()), false);
		parrot.add((Holder<EntityType<?>>) MSEntityTypes.OGRE, new ParrotImitation(MSSoundEvents.ENTITY_OGRE_AMBIENT.get()), false);
		parrot.add((Holder<EntityType<?>>) MSEntityTypes.BASILISK, new ParrotImitation(MSSoundEvents.ENTITY_BASILISK_AMBIENT.get()), false);
		parrot.add((Holder<EntityType<?>>) MSEntityTypes.LICH, new ParrotImitation(MSSoundEvents.ENTITY_LICH_AMBIENT.get()), false);
		
	}

}