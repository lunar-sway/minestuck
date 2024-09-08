package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.fluid.MSLiquidBlock;
import com.mraof.minestuck.block.fluid.WaterColorsBlock;
import com.mraof.minestuck.block.machine.*;
import com.mraof.minestuck.block.plant.*;
import com.mraof.minestuck.block.redstone.*;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.computer.theme.MSComputerThemes;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.world.gen.feature.MSCFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

public final class MSBlocks
{
	public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(Minestuck.MOD_ID);
	
	//Cruxite ores
	public static final DeferredBlock<Block> STONE_CRUXITE_ORE = REGISTER.register("stone_cruxite_ore", () -> cruxiteOre(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<Block> NETHERRACK_CRUXITE_ORE = REGISTER.register("netherrack_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> COBBLESTONE_CRUXITE_ORE = REGISTER.register("cobblestone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> SANDSTONE_CRUXITE_ORE = REGISTER.register("sandstone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> RED_SANDSTONE_CRUXITE_ORE = REGISTER.register("red_sandstone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> END_STONE_CRUXITE_ORE = REGISTER.register("end_stone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> SHADE_STONE_CRUXITE_ORE = REGISTER.register("shade_stone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> PINK_STONE_CRUXITE_ORE = REGISTER.register("pink_stone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> MYCELIUM_STONE_CRUXITE_ORE = REGISTER.register("mycelium_stone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> UNCARVED_WOOD_CRUXITE_ORE = REGISTER.register("uncarved_wood_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	public static final DeferredBlock<Block> BLACK_STONE_CRUXITE_ORE = REGISTER.register("black_stone_cruxite_ore", () -> cruxiteOre(ofFullCopy(STONE_CRUXITE_ORE.get())));
	
	private static Block cruxiteOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(UniformInt.of(2, 5), properties);
	}
	
	//Uranium ores
	public static final DeferredBlock<Block> STONE_URANIUM_ORE = REGISTER.register("stone_uranium_ore", () -> uraniumOre(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 3)));
	public static final DeferredBlock<Block> DEEPSLATE_URANIUM_ORE = REGISTER.register("deepslate_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get()).mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
	public static final DeferredBlock<Block> NETHERRACK_URANIUM_ORE = REGISTER.register("netherrack_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> COBBLESTONE_URANIUM_ORE = REGISTER.register("cobblestone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> SANDSTONE_URANIUM_ORE = REGISTER.register("sandstone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> RED_SANDSTONE_URANIUM_ORE = REGISTER.register("red_sandstone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> END_STONE_URANIUM_ORE = REGISTER.register("end_stone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> SHADE_STONE_URANIUM_ORE = REGISTER.register("shade_stone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> PINK_STONE_URANIUM_ORE = REGISTER.register("pink_stone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> MYCELIUM_STONE_URANIUM_ORE = REGISTER.register("mycelium_stone_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> UNCARVED_WOOD_URANIUM_ORE = REGISTER.register("uncarved_wood_uranium_ore", () -> uraniumOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	public static final DeferredBlock<Block> BLACK_STONE_URANIUM_ORE = REGISTER.register("black_stone_uranium_ore", () -> cruxiteOre(ofFullCopy(STONE_URANIUM_ORE.get())));
	
	private static Block uraniumOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(UniformInt.of(2, 5), properties);
	}
	
	//Land-specific vanilla ores
	public static final DeferredBlock<Block> NETHERRACK_COAL_ORE = REGISTER.register("netherrack_coal_ore", () -> coalOre(ofFullCopy(Blocks.COAL_ORE)));
	public static final DeferredBlock<Block> SHADE_STONE_COAL_ORE = REGISTER.register("shade_stone_coal_ore", () -> coalOre(ofFullCopy(Blocks.COAL_ORE)));
	public static final DeferredBlock<Block> PINK_STONE_COAL_ORE = REGISTER.register("pink_stone_coal_ore", () -> coalOre(ofFullCopy(Blocks.COAL_ORE)));
	
	private static Block coalOre(BlockBehaviour.Properties properties)
	{
		return new DropExperienceBlock(UniformInt.of(0, 2), properties);
	}
	
	public static final DeferredBlock<Block> END_STONE_IRON_ORE = REGISTER.register("end_stone_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.IRON_ORE)));
	public static final DeferredBlock<Block> SANDSTONE_IRON_ORE = REGISTER.register("sandstone_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.IRON_ORE)));
	public static final DeferredBlock<Block> RED_SANDSTONE_IRON_ORE = REGISTER.register("red_sandstone_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.IRON_ORE)));
	public static final DeferredBlock<Block> UNCARVED_WOOD_IRON_ORE = REGISTER.register("uncarved_wood_iron_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.IRON_ORE)));
	
	
	public static final DeferredBlock<Block> SANDSTONE_GOLD_ORE = REGISTER.register("sandstone_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)));
	public static final DeferredBlock<Block> RED_SANDSTONE_GOLD_ORE = REGISTER.register("red_sandstone_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)));
	public static final DeferredBlock<Block> SHADE_STONE_GOLD_ORE = REGISTER.register("shade_stone_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)));
	public static final DeferredBlock<Block> PINK_STONE_GOLD_ORE = REGISTER.register("pink_stone_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)));
	public static final DeferredBlock<Block> BLACK_STONE_GOLD_ORE = REGISTER.register("black_stone_gold_ore", () -> new DropExperienceBlock(ConstantInt.of(0), ofFullCopy(Blocks.GOLD_ORE)));
	
	public static final DeferredBlock<Block> END_STONE_REDSTONE_ORE = REGISTER.register("end_stone_redstone_ore", () -> new RedStoneOreBlock(ofFullCopy(Blocks.REDSTONE_ORE)));
	public static final DeferredBlock<Block> UNCARVED_WOOD_REDSTONE_ORE = REGISTER.register("uncarved_wood_redstone_ore", () -> new RedStoneOreBlock(ofFullCopy(Blocks.REDSTONE_ORE)));
	public static final DeferredBlock<Block> BLACK_STONE_REDSTONE_ORE = REGISTER.register("black_stone_redstone_ore", () -> new RedStoneOreBlock(ofFullCopy(Blocks.REDSTONE_ORE)));
	
	public static final DeferredBlock<Block> STONE_QUARTZ_ORE = REGISTER.register("stone_quartz_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), ofFullCopy(Blocks.NETHER_QUARTZ_ORE)));
	public static final DeferredBlock<Block> BLACK_STONE_QUARTZ_ORE = REGISTER.register("black_stone_quartz_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), ofFullCopy(Blocks.NETHER_QUARTZ_ORE)));
	
	public static final DeferredBlock<Block> PINK_STONE_LAPIS_ORE = REGISTER.register("pink_stone_lapis_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), ofFullCopy(Blocks.LAPIS_ORE)));
	
	public static final DeferredBlock<Block> PINK_STONE_DIAMOND_ORE = REGISTER.register("pink_stone_diamond_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), ofFullCopy(Blocks.DIAMOND_ORE)));
	
	public static final DeferredBlock<Block> UNCARVED_WOOD_EMERALD_ORE = REGISTER.register("uncarved_wood_emerald_ore", () -> new DropExperienceBlock(UniformInt.of(3, 7), ofFullCopy(Blocks.EMERALD_ORE)));
	
	//Resource Blocks
	public static final DeferredBlock<Block> CRUXITE_BLOCK = REGISTER.register("cruxite_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<StairBlock> CRUXITE_STAIRS = REGISTER.register("cruxite_stairs", () -> new StairBlock(() -> MSBlocks.CRUXITE_BLOCK.get().defaultBlockState(), ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<SlabBlock> CRUXITE_SLAB = REGISTER.register("cruxite_slab", () -> new SlabBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<WallBlock> CRUXITE_WALL = REGISTER.register("cruxite_wall", () -> new WallBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<ButtonBlock> CRUXITE_BUTTON = REGISTER.register("cruxite_button", () -> new ButtonBlock(MSBlockSetType.CRUXITE, 10, ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<PressurePlateBlock> CRUXITE_PRESSURE_PLATE = REGISTER.register("cruxite_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.CRUXITE, ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<DoorBlock> CRUXITE_DOOR = REGISTER.register("cruxite_door", () -> new DoorBlock(MSBlockSetType.CRUXITE, ofFullCopy(Blocks.OAK_DOOR)));
	public static final DeferredBlock<TrapDoorBlock> CRUXITE_TRAPDOOR = REGISTER.register("cruxite_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.CRUXITE, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<Block> POLISHED_CRUXITE_BLOCK = REGISTER.register("polished_cruxite_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<StairBlock> POLISHED_CRUXITE_STAIRS = REGISTER.register("polished_cruxite_stairs", () -> new StairBlock(() -> MSBlocks.CRUXITE_BLOCK.get().defaultBlockState(), ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_CRUXITE_SLAB = REGISTER.register("polished_cruxite_slab", () -> new SlabBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<WallBlock> POLISHED_CRUXITE_WALL = REGISTER.register("polished_cruxite_wall", () -> new WallBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	
	public static final DeferredBlock<Block> CRUXITE_BRICKS = REGISTER.register("cruxite_bricks", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<StairBlock> CRUXITE_BRICK_STAIRS = REGISTER.register("cruxite_brick_stairs", () -> new StairBlock(() -> MSBlocks.CRUXITE_BLOCK.get().defaultBlockState(), ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<SlabBlock> CRUXITE_BRICK_SLAB = REGISTER.register("cruxite_brick_slab", () -> new SlabBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	public static final DeferredBlock<WallBlock> CRUXITE_BRICK_WALL = REGISTER.register("cruxite_brick_wall", () -> new WallBlock(ofFullCopy(CRUXITE_BLOCK.get())));
	
	public static final DeferredBlock<Block> SMOOTH_CRUXITE_BLOCK = REGISTER.register("smooth_cruxite_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<Block> CHISELED_CRUXITE_BLOCK = REGISTER.register("chiseled_cruxite_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<Block> CRUXITE_PILLAR = REGISTER.register("cruxite_pillar", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(DyeColor.LIGHT_BLUE).instrument(NoteBlockInstrument.CHIME).strength(3.0F).requiresCorrectToolForDrops()));
	public static final DeferredBlock<Block> CRUXITE_LAMP = REGISTER.register("cruxite_lamp", () -> new CustomLampBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE).sound(SoundType.AMETHYST)
			.strength(1f).lightLevel(state -> state.getValue(CustomLampBlock.CLICKED) ? 15 : 0)));
	
	public static final DeferredBlock<Block> URANIUM_BLOCK = REGISTER.register("uranium_block", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIME).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7)));
	public static final DeferredBlock<StairBlock> URANIUM_STAIRS = REGISTER.register("uranium_stairs", () -> new StairBlock(() -> MSBlocks.URANIUM_BLOCK.get().defaultBlockState(), ofFullCopy(URANIUM_BLOCK.get())));
	public static final DeferredBlock<SlabBlock> URANIUM_SLAB = REGISTER.register("uranium_slab", () -> new SlabBlock(ofFullCopy(URANIUM_BLOCK.get())));
	public static final DeferredBlock<WallBlock> URANIUM_WALL = REGISTER.register("uranium_wall", () -> new WallBlock(ofFullCopy(URANIUM_BLOCK.get())));
	public static final DeferredBlock<ButtonBlock> URANIUM_BUTTON = REGISTER.register("uranium_button", () -> new ButtonBlock(MSBlockSetType.URANIUM, 10, ofFullCopy(URANIUM_BLOCK.get())));
	public static final DeferredBlock<PressurePlateBlock> URANIUM_PRESSURE_PLATE = REGISTER.register("uranium_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.URANIUM, ofFullCopy(URANIUM_BLOCK.get())));
	
	public static final DeferredBlock<Block> GENERIC_OBJECT = REGISTER.register("generic_object", () -> new Block(Block.Properties.of().mapColor(DyeColor.LIME).strength(1.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> PERFECTLY_GENERIC_STAIRS = REGISTER.register("perfectly_generic_stairs", () -> new StairBlock(() -> MSBlocks.GENERIC_OBJECT.get().defaultBlockState(), ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<SlabBlock> PERFECTLY_GENERIC_SLAB = REGISTER.register("perfectly_generic_slab", () -> new SlabBlock(ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<WallBlock> PERFECTLY_GENERIC_WALL = REGISTER.register("perfectly_generic_wall", () -> new WallBlock(ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<FenceBlock> PERFECTLY_GENERIC_FENCE = REGISTER.register("perfectly_generic_fence", () -> new FenceBlock(ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<FenceGateBlock> PERFECTLY_GENERIC_FENCE_GATE = REGISTER.register("perfectly_generic_fence_gate", () -> new FenceGateBlock(ofFullCopy(GENERIC_OBJECT.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<ButtonBlock> PERFECTLY_GENERIC_BUTTON = REGISTER.register("perfectly_generic_button", () -> new ButtonBlock(MSBlockSetType.PERFECTLY_GENERIC, 10, ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<PressurePlateBlock> PERFECTLY_GENERIC_PRESSURE_PLATE = REGISTER.register("perfectly_generic_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.PERFECTLY_GENERIC, ofFullCopy(GENERIC_OBJECT.get())));
	public static final DeferredBlock<DoorBlock> PERFECTLY_GENERIC_DOOR = REGISTER.register("perfectly_generic_door", () -> new DoorBlock(MSBlockSetType.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_DOOR)));
	public static final DeferredBlock<TrapDoorBlock> PERFECTLY_GENERIC_TRAPDOOR = REGISTER.register("perfectly_generic_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> PERFECTLY_GENERIC_SIGN = REGISTER.register("perfectly_generic_sign", () -> new MSStandingSignBlock(MSWoodTypes.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> PERFECTLY_GENERIC_WALL_SIGN = REGISTER.register("perfectly_generic_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> PERFECTLY_GENERIC_HANGING_SIGN = REGISTER.register("perfectly_generic_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> PERFECTLY_GENERIC_WALL_HANGING_SIGN = REGISTER.register("perfectly_generic_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.PERFECTLY_GENERIC, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	//Land Environment
	//Aspect Terrain Dirt
	public static final DeferredBlock<Block> BLUE_DIRT = REGISTER.register("blue_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(0.5F).sound(SoundType.GRAVEL)));
	public static final DeferredBlock<Block> THOUGHT_DIRT = REGISTER.register("thought_dirt", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(0.5F).sound(SoundType.GRAVEL)));
	
	//Coarse
	public static final DeferredBlock<Block> COARSE_STONE = REGISTER.register("coarse_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
	public static final DeferredBlock<StairBlock> COARSE_STONE_STAIRS = REGISTER.register("coarse_stone_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE.get().defaultBlockState(), ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<SlabBlock> COARSE_STONE_SLAB = REGISTER.register("coarse_stone_slab", () -> new SlabBlock(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<WallBlock> COARSE_STONE_WALL = REGISTER.register("coarse_stone_wall", () -> new WallBlock(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<ButtonBlock> COARSE_STONE_BUTTON = REGISTER.register("coarse_stone_button", () -> new ButtonBlock(MSBlockSetType.COARSE_STONE, 10, ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> COARSE_STONE_PRESSURE_PLATE = REGISTER.register("coarse_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.COARSE_STONE, ofFullCopy(COARSE_STONE.get())));
	
	public static final DeferredBlock<Block> COARSE_STONE_BRICKS = REGISTER.register("coarse_stone_bricks", () -> new Block(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<StairBlock> COARSE_STONE_BRICK_STAIRS = REGISTER.register("coarse_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.COARSE_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(COARSE_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> COARSE_STONE_BRICK_SLAB = REGISTER.register("coarse_stone_brick_slab", () -> new SlabBlock(ofFullCopy(COARSE_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> COARSE_STONE_BRICK_WALL = REGISTER.register("coarse_stone_brick_wall", () -> new WallBlock(ofFullCopy(COARSE_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> COARSE_STONE_COLUMN = REGISTER.register("coarse_stone_column", () -> new MSDirectionalBlock(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_COARSE_STONE_BRICKS = REGISTER.register("chiseled_coarse_stone_bricks", () -> new Block(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<Block> CRACKED_COARSE_STONE_BRICKS = REGISTER.register("cracked_coarse_stone_bricks", () -> new Block(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<Block> MOSSY_COARSE_STONE_BRICKS = REGISTER.register("mossy_coarse_stone_bricks", () -> new Block(ofFullCopy(COARSE_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_COARSE_STONE = REGISTER.register("chiseled_coarse_stone", () -> new Block(ofFullCopy(COARSE_STONE.get())));
	
	//Shade
	public static final DeferredBlock<Block> SHADE_STONE = REGISTER.register("shade_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> SHADE_STAIRS = REGISTER.register("shade_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_STONE.get().defaultBlockState(), ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<SlabBlock> SHADE_SLAB = REGISTER.register("shade_slab", () -> new SlabBlock(ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<WallBlock> SHADE_WALL = REGISTER.register("shade_wall", () -> new WallBlock(ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<ButtonBlock> SHADE_BUTTON = REGISTER.register("shade_button", () -> new ButtonBlock(MSBlockSetType.SHADE_STONE, 10, ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> SHADE_PRESSURE_PLATE = REGISTER.register("shade_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.SHADE_STONE, ofFullCopy(SHADE_STONE.get())));
	
	public static final DeferredBlock<Block> SHADE_BRICKS = REGISTER.register("shade_bricks", () -> new Block(ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<StairBlock> SHADE_BRICK_STAIRS = REGISTER.register("shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.SHADE_BRICKS.get().defaultBlockState(), ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> SHADE_BRICK_SLAB = REGISTER.register("shade_brick_slab", () -> new SlabBlock(ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> SHADE_BRICK_WALL = REGISTER.register("shade_brick_wall", () -> new WallBlock(ofFullCopy(SHADE_BRICKS.get())));
	
	public static final DeferredBlock<Block> SMOOTH_SHADE_STONE = REGISTER.register("smooth_shade_stone", () -> new Block(ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<StairBlock> SMOOTH_SHADE_STONE_STAIRS = REGISTER.register("smooth_shade_stone_stairs", () -> new StairBlock(() -> MSBlocks.SMOOTH_SHADE_STONE.get().defaultBlockState(), ofFullCopy(SMOOTH_SHADE_STONE.get())));
	public static final DeferredBlock<SlabBlock> SMOOTH_SHADE_STONE_SLAB = REGISTER.register("smooth_shade_stone_slab", () -> new SlabBlock(ofFullCopy(SMOOTH_SHADE_STONE.get())));
	public static final DeferredBlock<WallBlock> SMOOTH_SHADE_STONE_WALL = REGISTER.register("smooth_shade_stone_wall", () -> new WallBlock(ofFullCopy(SMOOTH_SHADE_STONE.get())));
	
	public static final DeferredBlock<Block> SHADE_COLUMN = REGISTER.register("shade_column", () -> new MSDirectionalBlock(ofFullCopy(SHADE_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_SHADE_BRICKS = REGISTER.register("chiseled_shade_bricks", () -> new Block(ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<Block> CRACKED_SHADE_BRICKS = REGISTER.register("cracked_shade_bricks", () -> new Block(ofFullCopy(SHADE_BRICKS.get())));
	
	public static final DeferredBlock<Block> MOSSY_SHADE_BRICKS = REGISTER.register("mossy_shade_bricks", () -> new Block(ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MOSSY_SHADE_BRICK_STAIRS = REGISTER.register("mossy_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_SHADE_BRICKS.get().defaultBlockState(), ofFullCopy(MOSSY_SHADE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MOSSY_SHADE_BRICK_SLAB = REGISTER.register("mossy_shade_brick_slab", () -> new SlabBlock(ofFullCopy(MOSSY_SHADE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MOSSY_SHADE_BRICK_WALL = REGISTER.register("mossy_shade_brick_wall", () -> new WallBlock(ofFullCopy(MOSSY_SHADE_BRICKS.get())));
	
	public static final DeferredBlock<Block> BLOOD_SHADE_BRICKS = REGISTER.register("blood_shade_bricks", () -> new Block(ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> BLOOD_SHADE_BRICK_STAIRS = REGISTER.register("blood_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLOOD_SHADE_BRICKS.get().defaultBlockState(), ofFullCopy(BLOOD_SHADE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> BLOOD_SHADE_BRICK_SLAB = REGISTER.register("blood_shade_brick_slab", () -> new SlabBlock(ofFullCopy(BLOOD_SHADE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> BLOOD_SHADE_BRICK_WALL = REGISTER.register("blood_shade_brick_wall", () -> new WallBlock(ofFullCopy(BLOOD_SHADE_BRICKS.get())));
	
	public static final DeferredBlock<Block> TAR_SHADE_BRICKS = REGISTER.register("tar_shade_bricks", () -> new Block(ofFullCopy(SHADE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> TAR_SHADE_BRICK_STAIRS = REGISTER.register("tar_shade_brick_stairs", () -> new StairBlock(() -> MSBlocks.TAR_SHADE_BRICKS.get().defaultBlockState(), ofFullCopy(TAR_SHADE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> TAR_SHADE_BRICK_SLAB = REGISTER.register("tar_shade_brick_slab", () -> new SlabBlock(ofFullCopy(TAR_SHADE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> TAR_SHADE_BRICK_WALL = REGISTER.register("tar_shade_brick_wall", () -> new WallBlock(ofFullCopy(TAR_SHADE_BRICKS.get())));
	
	//Frost
	public static final DeferredBlock<Block> FROST_TILE = REGISTER.register("frost_tile", () -> new Block(Block.Properties.of().mapColor(MapColor.ICE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> FROST_TILE_STAIRS = REGISTER.register("frost_tile_stairs", () -> new StairBlock(() -> MSBlocks.FROST_TILE.get().defaultBlockState(), ofFullCopy(FROST_TILE.get())));
	public static final DeferredBlock<SlabBlock> FROST_TILE_SLAB = REGISTER.register("frost_tile_slab", () -> new SlabBlock(ofFullCopy(FROST_TILE.get())));
	public static final DeferredBlock<WallBlock> FROST_TILE_WALL = REGISTER.register("frost_tile_wall", () -> new WallBlock(ofFullCopy(FROST_TILE.get())));
	
	public static final DeferredBlock<Block> FROST_BRICKS = REGISTER.register("frost_bricks", () -> new Block(ofFullCopy(FROST_TILE.get())));
	public static final DeferredBlock<StairBlock> FROST_BRICK_STAIRS = REGISTER.register("frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FROST_BRICKS.get().defaultBlockState(), ofFullCopy(FROST_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> FROST_BRICK_SLAB = REGISTER.register("frost_brick_slab", () -> new SlabBlock(ofFullCopy(FROST_BRICKS.get())));
	public static final DeferredBlock<WallBlock> FROST_BRICK_WALL = REGISTER.register("frost_brick_wall", () -> new WallBlock(ofFullCopy(FROST_BRICKS.get())));
	
	public static final DeferredBlock<Block> CHISELED_FROST_TILE = REGISTER.register("chiseled_frost_tile", () -> new Block(ofFullCopy(FROST_TILE.get())));
	public static final DeferredBlock<Block> FROST_COLUMN = REGISTER.register("frost_column", () -> new MSDirectionalBlock(ofFullCopy(FROST_TILE.get())));
	public static final DeferredBlock<Block> CHISELED_FROST_BRICKS = REGISTER.register("chiseled_frost_bricks", () -> new Block(ofFullCopy(FROST_BRICKS.get()))); //while it is a pillar block, it cannot be rotated, making it similar to cut sandstone
	public static final DeferredBlock<Block> CRACKED_FROST_BRICKS = REGISTER.register("cracked_frost_bricks", () -> new Block(ofFullCopy(FROST_BRICKS.get())));
	
	public static final DeferredBlock<Block> FLOWERY_FROST_BRICKS = REGISTER.register("flowery_frost_bricks", () -> new Block(ofFullCopy(FROST_BRICKS.get())));
	public static final DeferredBlock<StairBlock> FLOWERY_FROST_BRICK_STAIRS = REGISTER.register("flowery_frost_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_FROST_BRICKS.get().defaultBlockState(), ofFullCopy(FLOWERY_FROST_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> FLOWERY_FROST_BRICK_SLAB = REGISTER.register("flowery_frost_brick_slab", () -> new SlabBlock(ofFullCopy(FLOWERY_FROST_BRICKS.get())));
	public static final DeferredBlock<WallBlock> FLOWERY_FROST_BRICK_WALL = REGISTER.register("flowery_frost_brick_wall", () -> new WallBlock(ofFullCopy(FLOWERY_FROST_BRICKS.get())));
	
	
	//Cast Iron
	public static final DeferredBlock<Block> CAST_IRON = REGISTER.register("cast_iron", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final DeferredBlock<StairBlock> CAST_IRON_STAIRS = REGISTER.register("cast_iron_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON.get().defaultBlockState(), ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<SlabBlock> CAST_IRON_SLAB = REGISTER.register("cast_iron_slab", () -> new SlabBlock(ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<WallBlock> CAST_IRON_WALL = REGISTER.register("cast_iron_wall", () -> new WallBlock(ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<ButtonBlock> CAST_IRON_BUTTON = REGISTER.register("cast_iron_button", () -> new ButtonBlock(MSBlockSetType.CAST_IRON, 10, ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<PressurePlateBlock> CAST_IRON_PRESSURE_PLATE = REGISTER.register("cast_iron_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.CAST_IRON, ofFullCopy(CAST_IRON.get())));
	
	public static final DeferredBlock<Block> CAST_IRON_TILE = REGISTER.register("cast_iron_tile", () -> new Block(ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<StairBlock> CAST_IRON_TILE_STAIRS = REGISTER.register("cast_iron_tile_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON_TILE.get().defaultBlockState(), ofFullCopy(CAST_IRON_TILE.get())));
	public static final DeferredBlock<SlabBlock> CAST_IRON_TILE_SLAB = REGISTER.register("cast_iron_tile_slab", () -> new SlabBlock(ofFullCopy(CAST_IRON_TILE.get())));
	
	public static final DeferredBlock<Block> CAST_IRON_SHEET = REGISTER.register("cast_iron_sheet", () -> new Block(ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<StairBlock> CAST_IRON_SHEET_STAIRS = REGISTER.register("cast_iron_sheet_stairs", () -> new StairBlock(() -> MSBlocks.CAST_IRON_SHEET.get().defaultBlockState(), ofFullCopy(CAST_IRON_SHEET.get())));
	public static final DeferredBlock<SlabBlock> CAST_IRON_SHEET_SLAB = REGISTER.register("cast_iron_sheet_slab", () -> new SlabBlock(ofFullCopy(CAST_IRON_SHEET.get())));
	
	public static final DeferredBlock<Block> CHISELED_CAST_IRON = REGISTER.register("chiseled_cast_iron", () -> new Block(ofFullCopy(CAST_IRON.get())));
	public static final DeferredBlock<Block> CAST_IRON_FRAME = REGISTER.register("cast_iron_frame", () -> new MSDirectionalBlock(Block.Properties.of().noOcclusion().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	public static final DeferredBlock<Block> STEEL_BEAM = REGISTER.register("steel_beam", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	
	//Sulfur
	public static final DeferredBlock<Block> NATIVE_SULFUR = REGISTER.register("native_sulfur", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(0.5F, 1.0F)));
	
	//Mycelium
	public static final DeferredBlock<Block> MYCELIUM_STONE = REGISTER.register("mycelium_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> MYCELIUM_STAIRS = REGISTER.register("mycelium_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_STONE.get().defaultBlockState(), ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<SlabBlock> MYCELIUM_SLAB = REGISTER.register("mycelium_slab", () -> new SlabBlock(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<WallBlock> MYCELIUM_STONE_WALL = REGISTER.register("mycelium_stone_wall", () -> new WallBlock(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<ButtonBlock> MYCELIUM_STONE_BUTTON = REGISTER.register("mycelium_stone_button", () -> new ButtonBlock(MSBlockSetType.MYCELIUM_STONE, 10, ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> MYCELIUM_STONE_PRESSURE_PLATE = REGISTER.register("mycelium_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.MYCELIUM_STONE, ofFullCopy(MYCELIUM_STONE.get())));
	
	public static final DeferredBlock<Block> MYCELIUM_COBBLESTONE = REGISTER.register("mycelium_cobblestone", () -> new Block(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<StairBlock> MYCELIUM_COBBLESTONE_STAIRS = REGISTER.register("mycelium_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_COBBLESTONE.get().defaultBlockState(), ofFullCopy(MYCELIUM_COBBLESTONE.get())));
	public static final DeferredBlock<SlabBlock> MYCELIUM_COBBLESTONE_SLAB = REGISTER.register("mycelium_cobblestone_slab", () -> new SlabBlock(ofFullCopy(MYCELIUM_COBBLESTONE.get())));
	public static final DeferredBlock<WallBlock> MYCELIUM_COBBLESTONE_WALL = REGISTER.register("mycelium_cobblestone_wall", () -> new WallBlock(ofFullCopy(MYCELIUM_COBBLESTONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_MYCELIUM_STONE = REGISTER.register("polished_mycelium_stone", () -> new Block(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_MYCELIUM_STONE_STAIRS = REGISTER.register("polished_mycelium_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_MYCELIUM_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_MYCELIUM_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_MYCELIUM_STONE_SLAB = REGISTER.register("polished_mycelium_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_MYCELIUM_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_MYCELIUM_STONE_WALL = REGISTER.register("polished_mycelium_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_MYCELIUM_STONE.get())));
	
	public static final DeferredBlock<Block> MYCELIUM_BRICKS = REGISTER.register("mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<StairBlock> MYCELIUM_BRICK_STAIRS = REGISTER.register("mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MYCELIUM_BRICKS.get().defaultBlockState(), ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MYCELIUM_BRICK_SLAB = REGISTER.register("mycelium_brick_slab", () -> new SlabBlock(ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MYCELIUM_BRICK_WALL = REGISTER.register("mycelium_brick_wall", () -> new WallBlock(ofFullCopy(MYCELIUM_BRICKS.get())));
	
	public static final DeferredBlock<Block> MYCELIUM_COLUMN = REGISTER.register("mycelium_column", () -> new MSDirectionalBlock(ofFullCopy(MYCELIUM_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_MYCELIUM_BRICKS = REGISTER.register("chiseled_mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<Block> SUSPICIOUS_CHISELED_MYCELIUM_BRICKS = REGISTER.register("suspicious_chiseled_mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<Block> CRACKED_MYCELIUM_BRICKS = REGISTER.register("cracked_mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_BRICKS.get())));
	
	public static final DeferredBlock<Block> MOSSY_MYCELIUM_BRICKS = REGISTER.register("mossy_mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MOSSY_MYCELIUM_BRICK_STAIRS = REGISTER.register("mossy_mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_MYCELIUM_BRICKS.get().defaultBlockState(), ofFullCopy(MOSSY_MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MOSSY_MYCELIUM_BRICK_SLAB = REGISTER.register("mossy_mycelium_brick_slab", () -> new SlabBlock(ofFullCopy(MOSSY_MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MOSSY_MYCELIUM_BRICK_WALL = REGISTER.register("mossy_mycelium_brick_wall", () -> new WallBlock(ofFullCopy(MOSSY_MYCELIUM_BRICKS.get())));
	
	public static final DeferredBlock<Block> FLOWERY_MYCELIUM_BRICKS = REGISTER.register("flowery_mycelium_bricks", () -> new Block(ofFullCopy(MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<StairBlock> FLOWERY_MYCELIUM_BRICK_STAIRS = REGISTER.register("flowery_mycelium_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MYCELIUM_BRICKS.get().defaultBlockState(), ofFullCopy(FLOWERY_MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> FLOWERY_MYCELIUM_BRICK_SLAB = REGISTER.register("flowery_mycelium_brick_slab", () -> new SlabBlock(ofFullCopy(FLOWERY_MYCELIUM_BRICKS.get())));
	public static final DeferredBlock<WallBlock> FLOWERY_MYCELIUM_BRICK_WALL = REGISTER.register("flowery_mycelium_brick_wall", () -> new WallBlock(ofFullCopy(FLOWERY_MYCELIUM_BRICKS.get())));
	
	//Black
	public static final DeferredBlock<Block> BLACK_SAND = REGISTER.register("black_sand", () -> new ColoredFallingBlock(new ColorRGBA(0x181915), Block.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
	
	public static final DeferredBlock<Block> BLACK_STONE = REGISTER.register("black_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> BLACK_STONE_STAIRS = REGISTER.register("black_stone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE.get().defaultBlockState(), ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<SlabBlock> BLACK_STONE_SLAB = REGISTER.register("black_stone_slab", () -> new SlabBlock(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<WallBlock> BLACK_STONE_WALL = REGISTER.register("black_stone_wall", () -> new WallBlock(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<ButtonBlock> BLACK_STONE_BUTTON = REGISTER.register("black_stone_button", () -> new ButtonBlock(MSBlockSetType.BLACK_STONE, 10, ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> BLACK_STONE_PRESSURE_PLATE = REGISTER.register("black_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.BLACK_STONE, ofFullCopy(BLACK_STONE.get())));
	
	public static final DeferredBlock<Block> BLACK_COBBLESTONE = REGISTER.register("black_cobblestone", () -> new Block(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<StairBlock> BLACK_COBBLESTONE_STAIRS = REGISTER.register("black_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_COBBLESTONE.get().defaultBlockState(), ofFullCopy(BLACK_COBBLESTONE.get())));
	public static final DeferredBlock<SlabBlock> BLACK_COBBLESTONE_SLAB = REGISTER.register("black_cobblestone_slab", () -> new SlabBlock(ofFullCopy(BLACK_COBBLESTONE.get())));
	public static final DeferredBlock<WallBlock> BLACK_COBBLESTONE_WALL = REGISTER.register("black_cobblestone_wall", () -> new WallBlock(ofFullCopy(BLACK_COBBLESTONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_BLACK_STONE = REGISTER.register("polished_black_stone", () -> new Block(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_BLACK_STONE_STAIRS = REGISTER.register("polished_black_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_BLACK_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_BLACK_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_BLACK_STONE_SLAB = REGISTER.register("polished_black_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_BLACK_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_BLACK_STONE_WALL = REGISTER.register("polished_black_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_BLACK_STONE.get())));
	
	public static final DeferredBlock<Block> BLACK_STONE_BRICKS = REGISTER.register("black_stone_bricks", () -> new Block(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<StairBlock> BLACK_STONE_BRICK_STAIRS = REGISTER.register("black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> BLACK_STONE_BRICK_SLAB = REGISTER.register("black_stone_brick_slab", () -> new SlabBlock(ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> BLACK_STONE_BRICK_WALL = REGISTER.register("black_stone_brick_wall", () -> new WallBlock(ofFullCopy(BLACK_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> BLACK_STONE_COLUMN = REGISTER.register("black_stone_column", () -> new MSDirectionalBlock(ofFullCopy(BLACK_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_BLACK_STONE_BRICKS = REGISTER.register("chiseled_black_stone_bricks", () -> new Block(ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> CRACKED_BLACK_STONE_BRICKS = REGISTER.register("cracked_black_stone_bricks", () -> new Block(ofFullCopy(BLACK_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> MAGMATIC_BLACK_STONE_BRICKS = REGISTER.register("magmatic_black_stone_bricks", () -> new Block(ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MAGMATIC_BLACK_STONE_BRICK_STAIRS = REGISTER.register("magmatic_black_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MAGMATIC_BLACK_STONE_BRICK_SLAB = REGISTER.register("magmatic_black_stone_brick_slab", () -> new SlabBlock(ofFullCopy(BLACK_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MAGMATIC_BLACK_STONE_BRICK_WALL = REGISTER.register("magmatic_black_stone_brick_wall", () -> new WallBlock(ofFullCopy(BLACK_STONE_BRICKS.get())));
	
	//Igneous
	public static final DeferredBlock<Block> IGNEOUS_STONE = REGISTER.register("igneous_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.GUITAR).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> IGNEOUS_STONE_STAIRS = REGISTER.register("igneous_stone_stairs", () -> new StairBlock(() -> MSBlocks.IGNEOUS_STONE.get().defaultBlockState(), ofFullCopy(IGNEOUS_STONE.get())));
	public static final DeferredBlock<SlabBlock> IGNEOUS_STONE_SLAB = REGISTER.register("igneous_stone_slab", () -> new SlabBlock(ofFullCopy(IGNEOUS_STONE.get())));
	public static final DeferredBlock<WallBlock> IGNEOUS_STONE_WALL = REGISTER.register("igneous_stone_wall", () -> new WallBlock(ofFullCopy(IGNEOUS_STONE.get())));
	public static final DeferredBlock<ButtonBlock> IGNEOUS_STONE_BUTTON = REGISTER.register("igneous_stone_button", () -> new ButtonBlock(MSBlockSetType.IGNEOUS_STONE, 10, ofFullCopy(IGNEOUS_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> IGNEOUS_STONE_PRESSURE_PLATE = REGISTER.register("igneous_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.IGNEOUS_STONE, ofFullCopy(IGNEOUS_STONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_IGNEOUS_STONE = REGISTER.register("polished_igneous_stone", () -> new Block(ofFullCopy(IGNEOUS_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_IGNEOUS_STAIRS = REGISTER.register("polished_igneous_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_IGNEOUS_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_IGNEOUS_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_IGNEOUS_SLAB = REGISTER.register("polished_igneous_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_IGNEOUS_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_IGNEOUS_WALL = REGISTER.register("polished_igneous_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_IGNEOUS_STONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_IGNEOUS_BRICKS = REGISTER.register("polished_igneous_bricks", () -> new Block(ofFullCopy(POLISHED_IGNEOUS_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_IGNEOUS_BRICK_STAIRS = REGISTER.register("polished_igneous_brick_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_IGNEOUS_BRICKS.get().defaultBlockState(), ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_IGNEOUS_BRICK_SLAB = REGISTER.register("polished_igneous_brick_slab", () -> new SlabBlock(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<WallBlock> POLISHED_IGNEOUS_BRICK_WALL = REGISTER.register("polished_igneous_brick_wall", () -> new WallBlock(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	
	public static final DeferredBlock<Block> POLISHED_IGNEOUS_PILLAR = REGISTER.register("polished_igneous_pillar", () -> new MSDirectionalBlock(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<Block> CHISELED_IGNEOUS_STONE = REGISTER.register("chiseled_igneous_stone", () -> new Block(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<Block> CRACKED_POLISHED_IGNEOUS_BRICKS = REGISTER.register("cracked_polished_igneous_bricks", () -> new Block(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	
	public static final DeferredBlock<Block> MAGMATIC_POLISHED_IGNEOUS_BRICKS = REGISTER.register("magmatic_polished_igneous_bricks", () -> new Block(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MAGMATIC_POLISHED_IGNEOUS_BRICK_STAIRS = REGISTER.register("magmatic_polished_igneous_brick_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_IGNEOUS_BRICKS.get().defaultBlockState(), ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MAGMATIC_POLISHED_IGNEOUS_BRICK_SLAB = REGISTER.register("magmatic_polished_igneous_brick_slab", () -> new SlabBlock(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MAGMATIC_POLISHED_IGNEOUS_BRICK_WALL = REGISTER.register("magmatic_polished_igneous_brick_wall", () -> new WallBlock(ofFullCopy(POLISHED_IGNEOUS_BRICKS.get())));
	
	public static final DeferredBlock<Block> MAGMATIC_IGNEOUS_STONE = REGISTER.register("magmatic_igneous_stone", () -> new Block(ofFullCopy(IGNEOUS_STONE.get())));
	
	//Pumice
	public static final DeferredBlock<Block> PUMICE_STONE = REGISTER.register("pumice_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.GUITAR).requiresCorrectToolForDrops().strength(0.5F)));
	public static final DeferredBlock<StairBlock> PUMICE_STONE_STAIRS = REGISTER.register("pumice_stone_stairs", () -> new StairBlock(() -> MSBlocks.PUMICE_STONE.get().defaultBlockState(), ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<SlabBlock> PUMICE_STONE_SLAB = REGISTER.register("pumice_stone_slab", () -> new SlabBlock(ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<WallBlock> PUMICE_STONE_WALL = REGISTER.register("pumice_stone_wall", () -> new WallBlock(ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<ButtonBlock> PUMICE_STONE_BUTTON = REGISTER.register("pumice_stone_button", () -> new ButtonBlock(MSBlockSetType.PUMICE_STONE, 10, ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> PUMICE_STONE_PRESSURE_PLATE = REGISTER.register("pumice_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.PUMICE_STONE, ofFullCopy(PUMICE_STONE.get())));
	
	public static final DeferredBlock<Block> PUMICE_BRICKS = REGISTER.register("pumice_bricks", () -> new Block(ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<StairBlock> PUMICE_BRICK_STAIRS = REGISTER.register("pumice_brick_stairs", () -> new StairBlock(() -> MSBlocks.PUMICE_BRICKS.get().defaultBlockState(), ofFullCopy(PUMICE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> PUMICE_BRICK_SLAB = REGISTER.register("pumice_brick_slab", () -> new SlabBlock(ofFullCopy(PUMICE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> PUMICE_BRICK_WALL = REGISTER.register("pumice_brick_wall", () -> new WallBlock(ofFullCopy(PUMICE_BRICKS.get())));
	
	public static final DeferredBlock<Block> PUMICE_TILES = REGISTER.register("pumice_tiles", () -> new Block(ofFullCopy(PUMICE_STONE.get())));
	public static final DeferredBlock<StairBlock> PUMICE_TILE_STAIRS = REGISTER.register("pumice_tile_stairs", () -> new StairBlock(() -> MSBlocks.PUMICE_TILES.get().defaultBlockState(), ofFullCopy(PUMICE_TILES.get())));
	public static final DeferredBlock<SlabBlock> PUMICE_TILE_SLAB = REGISTER.register("pumice_tile_slab", () -> new SlabBlock(ofFullCopy(PUMICE_TILES.get())));
	public static final DeferredBlock<WallBlock> PUMICE_TILE_WALL = REGISTER.register("pumice_tile_wall", () -> new WallBlock(ofFullCopy(PUMICE_TILES.get())));
	
	public static final DeferredBlock<Block> HEAT_LAMP = REGISTER.register("heat_lamp", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.GUITAR).strength(1.5F).lightLevel(state -> 15).sound(SoundType.SHROOMLIGHT)));
	
	//Flowery
	public static final DeferredBlock<Block> FLOWERY_MOSSY_COBBLESTONE = REGISTER.register("flowery_mossy_cobblestone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> FLOWERY_MOSSY_COBBLESTONE_STAIRS = REGISTER.register("flowery_mossy_cobblestone_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_COBBLESTONE.get().defaultBlockState(), ofFullCopy(FLOWERY_MOSSY_COBBLESTONE.get())));
	public static final DeferredBlock<SlabBlock> FLOWERY_MOSSY_COBBLESTONE_SLAB = REGISTER.register("flowery_mossy_cobblestone_slab", () -> new SlabBlock(ofFullCopy(FLOWERY_MOSSY_COBBLESTONE.get())));
	public static final DeferredBlock<WallBlock> FLOWERY_MOSSY_COBBLESTONE_WALL = REGISTER.register("flowery_mossy_cobblestone_wall", () -> new WallBlock(ofFullCopy(FLOWERY_MOSSY_COBBLESTONE.get())));
	
	public static final DeferredBlock<Block> FLOWERY_MOSSY_STONE_BRICKS = REGISTER.register("flowery_mossy_stone_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> FLOWERY_MOSSY_STONE_BRICK_STAIRS = REGISTER.register("flowery_mossy_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> FLOWERY_MOSSY_STONE_BRICK_SLAB = REGISTER.register("flowery_mossy_stone_brick_slab", () -> new SlabBlock(ofFullCopy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> FLOWERY_MOSSY_STONE_BRICK_WALL = REGISTER.register("flowery_mossy_stone_brick_wall", () -> new WallBlock(ofFullCopy(FLOWERY_MOSSY_STONE_BRICKS.get())));
	
	//Decrepit
	public static final DeferredBlock<Block> DECREPIT_STONE_BRICKS = REGISTER.register("decrepit_stone_bricks", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> DECREPIT_STONE_BRICK_STAIRS = REGISTER.register("decrepit_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.DECREPIT_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(DECREPIT_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> DECREPIT_STONE_BRICK_SLAB = REGISTER.register("decrepit_stone_brick_slab", () -> new SlabBlock(ofFullCopy(DECREPIT_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> DECREPIT_STONE_BRICK_WALL = REGISTER.register("decrepit_stone_brick_wall", () -> new WallBlock(ofFullCopy(DECREPIT_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> MOSSY_DECREPIT_STONE_BRICKS = REGISTER.register("mossy_decrepit_stone_bricks", () -> new Block(ofFullCopy(DECREPIT_STONE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MOSSY_DECREPIT_STONE_BRICK_STAIRS = REGISTER.register("mossy_decrepit_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MOSSY_DECREPIT_STONE_BRICK_SLAB = REGISTER.register("mossy_decrepit_stone_brick_slab", () -> new SlabBlock(ofFullCopy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MOSSY_DECREPIT_STONE_BRICK_WALL = REGISTER.register("mossy_decrepit_stone_brick_wall", () -> new WallBlock(ofFullCopy(MOSSY_DECREPIT_STONE_BRICKS.get())));
	
	//End
	public static final DeferredBlock<Block> COARSE_END_STONE = REGISTER.register("coarse_end_stone", () -> new TillableBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F), Blocks.END_STONE::defaultBlockState));
	
	//Chalk
	public static final DeferredBlock<Block> CHALK = REGISTER.register("chalk", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> CHALK_STAIRS = REGISTER.register("chalk_stairs", () -> new StairBlock(() -> MSBlocks.CHALK.get().defaultBlockState(), ofFullCopy(CHALK.get())));
	public static final DeferredBlock<SlabBlock> CHALK_SLAB = REGISTER.register("chalk_slab", () -> new SlabBlock(ofFullCopy(CHALK.get())));
	public static final DeferredBlock<WallBlock> CHALK_WALL = REGISTER.register("chalk_wall", () -> new WallBlock(ofFullCopy(CHALK.get())));
	public static final DeferredBlock<ButtonBlock> CHALK_BUTTON = REGISTER.register("chalk_button", () -> new ButtonBlock(MSBlockSetType.CHALK, 10, ofFullCopy(CHALK.get())));
	public static final DeferredBlock<PressurePlateBlock> CHALK_PRESSURE_PLATE = REGISTER.register("chalk_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.CHALK, ofFullCopy(CHALK.get())));
	
	public static final DeferredBlock<Block> POLISHED_CHALK = REGISTER.register("polished_chalk", () -> new Block(ofFullCopy(CHALK.get())));
	public static final DeferredBlock<StairBlock> POLISHED_CHALK_STAIRS = REGISTER.register("polished_chalk_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_CHALK.get().defaultBlockState(), ofFullCopy(POLISHED_CHALK.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_CHALK_SLAB = REGISTER.register("polished_chalk_slab", () -> new SlabBlock(ofFullCopy(POLISHED_CHALK.get())));
	public static final DeferredBlock<WallBlock> POLISHED_CHALK_WALL = REGISTER.register("polished_chalk_wall", () -> new WallBlock(ofFullCopy(POLISHED_CHALK.get())));
	
	public static final DeferredBlock<Block> CHALK_BRICKS = REGISTER.register("chalk_bricks", () -> new Block(ofFullCopy(CHALK.get())));
	public static final DeferredBlock<StairBlock> CHALK_BRICK_STAIRS = REGISTER.register("chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.CHALK_BRICKS.get().defaultBlockState(), ofFullCopy(CHALK_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> CHALK_BRICK_SLAB = REGISTER.register("chalk_brick_slab", () -> new SlabBlock(ofFullCopy(CHALK_BRICKS.get())));
	public static final DeferredBlock<WallBlock> CHALK_BRICK_WALL = REGISTER.register("chalk_brick_wall", () -> new WallBlock(ofFullCopy(CHALK_BRICKS.get())));
	
	public static final DeferredBlock<Block> CHALK_COLUMN = REGISTER.register("chalk_column", () -> new MSDirectionalBlock(ofFullCopy(CHALK.get())));
	public static final DeferredBlock<Block> CHISELED_CHALK_BRICKS = REGISTER.register("chiseled_chalk_bricks", () -> new Block(ofFullCopy(CHALK_BRICKS.get())));
	
	public static final DeferredBlock<Block> MOSSY_CHALK_BRICKS = REGISTER.register("mossy_chalk_bricks", () -> new Block(ofFullCopy(CHALK_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MOSSY_CHALK_BRICK_STAIRS = REGISTER.register("mossy_chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_CHALK_BRICKS.get().defaultBlockState(), ofFullCopy(MOSSY_CHALK_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MOSSY_CHALK_BRICK_SLAB = REGISTER.register("mossy_chalk_brick_slab", () -> new SlabBlock(ofFullCopy(MOSSY_CHALK_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MOSSY_CHALK_BRICK_WALL = REGISTER.register("mossy_chalk_brick_wall", () -> new WallBlock(ofFullCopy(MOSSY_CHALK_BRICKS.get())));
	
	public static final DeferredBlock<Block> FLOWERY_CHALK_BRICKS = REGISTER.register("flowery_chalk_bricks", () -> new Block(ofFullCopy(CHALK_BRICKS.get())));
	public static final DeferredBlock<StairBlock> FLOWERY_CHALK_BRICK_STAIRS = REGISTER.register("flowery_chalk_brick_stairs", () -> new StairBlock(() -> MSBlocks.FLOWERY_CHALK_BRICKS.get().defaultBlockState(), ofFullCopy(FLOWERY_CHALK_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> FLOWERY_CHALK_BRICK_SLAB = REGISTER.register("flowery_chalk_brick_slab", () -> new SlabBlock(ofFullCopy(FLOWERY_CHALK_BRICKS.get())));
	public static final DeferredBlock<WallBlock> FLOWERY_CHALK_BRICK_WALL = REGISTER.register("flowery_chalk_brick_wall", () -> new WallBlock(ofFullCopy(FLOWERY_CHALK_BRICKS.get())));
	
	//Pink
	public static final DeferredBlock<Block> PINK_STONE = REGISTER.register("pink_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
	public static final DeferredBlock<StairBlock> PINK_STONE_STAIRS = REGISTER.register("pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE.get().defaultBlockState(), ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<SlabBlock> PINK_STONE_SLAB = REGISTER.register("pink_stone_slab", () -> new SlabBlock(ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<WallBlock> PINK_STONE_WALL = REGISTER.register("pink_stone_wall", () -> new WallBlock(ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<ButtonBlock> PINK_STONE_BUTTON = REGISTER.register("pink_stone_button", () -> new ButtonBlock(MSBlockSetType.PINK_STONE, 10, ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> PINK_STONE_PRESSURE_PLATE = REGISTER.register("pink_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.PINK_STONE, ofFullCopy(PINK_STONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_PINK_STONE = REGISTER.register("polished_pink_stone", () -> new Block(ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_PINK_STONE_STAIRS = REGISTER.register("polished_pink_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_PINK_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_PINK_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_PINK_STONE_SLAB = REGISTER.register("polished_pink_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_PINK_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_PINK_STONE_WALL = REGISTER.register("polished_pink_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_PINK_STONE.get())));
	
	public static final DeferredBlock<Block> PINK_STONE_BRICKS = REGISTER.register("pink_stone_bricks", () -> new Block(ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<StairBlock> PINK_STONE_BRICK_STAIRS = REGISTER.register("pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.PINK_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> PINK_STONE_BRICK_SLAB = REGISTER.register("pink_stone_brick_slab", () -> new SlabBlock(ofFullCopy(PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> PINK_STONE_BRICK_WALL = REGISTER.register("pink_stone_brick_wall", () -> new WallBlock(ofFullCopy(PINK_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> PINK_STONE_COLUMN = REGISTER.register("pink_stone_column", () -> new MSDirectionalBlock(ofFullCopy(PINK_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_PINK_STONE_BRICKS = REGISTER.register("chiseled_pink_stone_bricks", () -> new Block(ofFullCopy(PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> CRACKED_PINK_STONE_BRICKS = REGISTER.register("cracked_pink_stone_bricks", () -> new Block(ofFullCopy(PINK_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> MOSSY_PINK_STONE_BRICKS = REGISTER.register("mossy_pink_stone_bricks", () -> new Block(ofFullCopy(PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> MOSSY_PINK_STONE_BRICK_STAIRS = REGISTER.register("mossy_pink_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.MOSSY_PINK_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(MOSSY_PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> MOSSY_PINK_STONE_BRICK_SLAB = REGISTER.register("mossy_pink_stone_brick_slab", () -> new SlabBlock(ofFullCopy(MOSSY_PINK_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> MOSSY_PINK_STONE_BRICK_WALL = REGISTER.register("mossy_pink_stone_brick_wall", () -> new WallBlock(ofFullCopy(MOSSY_PINK_STONE_BRICKS.get())));
	
	//Brown
	public static final DeferredBlock<Block> BROWN_STONE = REGISTER.register("brown_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final DeferredBlock<StairBlock> BROWN_STONE_STAIRS = REGISTER.register("brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE.get().defaultBlockState(), ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<SlabBlock> BROWN_STONE_SLAB = REGISTER.register("brown_stone_slab", () -> new SlabBlock(ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<WallBlock> BROWN_STONE_WALL = REGISTER.register("brown_stone_wall", () -> new WallBlock(ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<ButtonBlock> BROWN_STONE_BUTTON = REGISTER.register("brown_stone_button", () -> new ButtonBlock(MSBlockSetType.BROWN_STONE, 10, ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> BROWN_STONE_PRESSURE_PLATE = REGISTER.register("brown_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.BROWN_STONE, ofFullCopy(BROWN_STONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_BROWN_STONE = REGISTER.register("polished_brown_stone", () -> new Block(ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_BROWN_STONE_STAIRS = REGISTER.register("polished_brown_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_BROWN_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_BROWN_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_BROWN_STONE_SLAB = REGISTER.register("polished_brown_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_BROWN_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_BROWN_STONE_WALL = REGISTER.register("polished_brown_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_BROWN_STONE.get())));
	
	public static final DeferredBlock<Block> BROWN_STONE_BRICKS = REGISTER.register("brown_stone_bricks", () -> new Block(ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<StairBlock> BROWN_STONE_BRICK_STAIRS = REGISTER.register("brown_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.BROWN_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(BROWN_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> BROWN_STONE_BRICK_SLAB = REGISTER.register("brown_stone_brick_slab", () -> new SlabBlock(ofFullCopy(BROWN_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> BROWN_STONE_BRICK_WALL = REGISTER.register("brown_stone_brick_wall", () -> new WallBlock(ofFullCopy(BROWN_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> CRACKED_BROWN_STONE_BRICKS = REGISTER.register("cracked_brown_stone_bricks", () -> new Block(ofFullCopy(BROWN_STONE.get())));
	public static final DeferredBlock<Block> BROWN_STONE_COLUMN = REGISTER.register("brown_stone_column", () -> new MSDirectionalBlock(ofFullCopy(BROWN_STONE.get())));
	
	//Green
	public static final DeferredBlock<Block> GREEN_STONE = REGISTER.register("green_stone", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 7.0F)));
	public static final DeferredBlock<StairBlock> GREEN_STONE_STAIRS = REGISTER.register("green_stone_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE.get().defaultBlockState(), ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<SlabBlock> GREEN_STONE_SLAB = REGISTER.register("green_stone_slab", () -> new SlabBlock(ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<WallBlock> GREEN_STONE_WALL = REGISTER.register("green_stone_wall", () -> new WallBlock(ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<ButtonBlock> GREEN_STONE_BUTTON = REGISTER.register("green_stone_button", () -> new ButtonBlock(MSBlockSetType.GREEN_STONE, 10, ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<PressurePlateBlock> GREEN_STONE_PRESSURE_PLATE = REGISTER.register("green_stone_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.GREEN_STONE, ofFullCopy(GREEN_STONE.get())));
	
	public static final DeferredBlock<Block> POLISHED_GREEN_STONE = REGISTER.register("polished_green_stone", () -> new Block(ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<StairBlock> POLISHED_GREEN_STONE_STAIRS = REGISTER.register("polished_green_stone_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_GREEN_STONE.get().defaultBlockState(), ofFullCopy(POLISHED_GREEN_STONE.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_GREEN_STONE_SLAB = REGISTER.register("polished_green_stone_slab", () -> new SlabBlock(ofFullCopy(POLISHED_GREEN_STONE.get())));
	public static final DeferredBlock<WallBlock> POLISHED_GREEN_STONE_WALL = REGISTER.register("polished_green_stone_wall", () -> new WallBlock(ofFullCopy(POLISHED_GREEN_STONE.get())));
	
	public static final DeferredBlock<Block> GREEN_STONE_BRICKS = REGISTER.register("green_stone_bricks", () -> new Block(ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<StairBlock> GREEN_STONE_BRICK_STAIRS = REGISTER.register("green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> GREEN_STONE_BRICK_SLAB = REGISTER.register("green_stone_brick_slab", () -> new SlabBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> GREEN_STONE_BRICK_WALL = REGISTER.register("green_stone_brick_wall", () -> new WallBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> GREEN_STONE_COLUMN = REGISTER.register("green_stone_column", () -> new MSDirectionalBlock(ofFullCopy(GREEN_STONE.get())));
	public static final DeferredBlock<Block> CHISELED_GREEN_STONE_BRICKS = REGISTER.register("chiseled_green_stone_bricks", () -> new Block(ofFullCopy(GREEN_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> HORIZONTAL_GREEN_STONE_BRICKS = REGISTER.register("horizontal_green_stone_bricks", () -> new Block(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> HORIZONTAL_GREEN_STONE_BRICK_STAIRS = REGISTER.register("horizontal_green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> HORIZONTAL_GREEN_STONE_BRICK_SLAB = REGISTER.register("horizontal_green_stone_brick_slab", () -> new SlabBlock(ofFullCopy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> HORIZONTAL_GREEN_STONE_BRICK_WALL = REGISTER.register("horizontal_green_stone_brick_wall", () -> new WallBlock(ofFullCopy(HORIZONTAL_GREEN_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> VERTICAL_GREEN_STONE_BRICKS = REGISTER.register("vertical_green_stone_bricks", () -> new Block(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<StairBlock> VERTICAL_GREEN_STONE_BRICK_STAIRS = REGISTER.register("vertical_green_stone_brick_stairs", () -> new StairBlock(() -> MSBlocks.VERTICAL_GREEN_STONE_BRICKS.get().defaultBlockState(), ofFullCopy(VERTICAL_GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<SlabBlock> VERTICAL_GREEN_STONE_BRICK_SLAB = REGISTER.register("vertical_green_stone_brick_slab", () -> new SlabBlock(ofFullCopy(VERTICAL_GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<WallBlock> VERTICAL_GREEN_STONE_BRICK_WALL = REGISTER.register("vertical_green_stone_brick_wall", () -> new WallBlock(ofFullCopy(VERTICAL_GREEN_STONE_BRICKS.get())));
	
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_EMBEDDED_LADDER = REGISTER.register("green_stone_brick_embedded_ladder", () -> new CustomShapeBlock(ofFullCopy(GREEN_STONE_BRICKS.get()), MSBlockShapes.EMBEDDED_STAIRS)); //uses the tag CLIMBABLE
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_TRIM = REGISTER.register("green_stone_brick_trim", () -> new MSDirectionalBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_FROG = REGISTER.register("green_stone_brick_frog", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_IGUANA_LEFT = REGISTER.register("green_stone_brick_iguana_left", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_IGUANA_RIGHT = REGISTER.register("green_stone_brick_iguana_right", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_LOTUS = REGISTER.register("green_stone_brick_lotus", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_NAK_LEFT = REGISTER.register("green_stone_brick_nak_left", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_NAK_RIGHT = REGISTER.register("green_stone_brick_nak_right", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_SALAMANDER_LEFT = REGISTER.register("green_stone_brick_salamander_left", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_SALAMANDER_RIGHT = REGISTER.register("green_stone_brick_salamander_right", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_SKAIA = REGISTER.register("green_stone_brick_skaia", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	public static final DeferredBlock<Block> GREEN_STONE_BRICK_TURTLE = REGISTER.register("green_stone_brick_turtle", () -> new HieroglyphBlock(ofFullCopy(GREEN_STONE_BRICKS.get())));
	//TODO when walking down steep stairs, players take fall damage that cannot be circumvented by the fallOn function. This includes when there is only an air block underneath them
	public static final DeferredBlock<Block> STEEP_GREEN_STONE_BRICK_STAIRS_BASE = REGISTER.register("steep_green_stone_brick_stairs_base", () -> new CustomShapeBlock(ofFullCopy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_BASE));
	public static final DeferredBlock<Block> STEEP_GREEN_STONE_BRICK_STAIRS_TOP = REGISTER.register("steep_green_stone_brick_stairs_top", () -> new CustomShapeBlock(ofFullCopy(GREEN_STONE.get()), MSBlockShapes.STEEP_STAIRS_TOP));
	
	//Sandstone
	public static final DeferredBlock<Block> SANDSTONE_COLUMN = REGISTER.register("sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final DeferredBlock<Block> CHISELED_SANDSTONE_COLUMN = REGISTER.register("chiseled_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final DeferredBlock<Block> RED_SANDSTONE_COLUMN = REGISTER.register("red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	public static final DeferredBlock<Block> CHISELED_RED_SANDSTONE_COLUMN = REGISTER.register("chiseled_red_sandstone_column", () -> new MSDirectionalBlock(Block.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
	
	//Wood
	public static final DeferredBlock<Block> CARVED_LOG = REGISTER.register("carved_log", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> CARVED_WOODEN_LEAF = REGISTER.register("carved_wooden_leaf", () -> new CustomShapeBlock(Block.Properties.of().noOcclusion().mapColor(MapColor.WOOD).strength(0.4F).sound(SoundType.WOOD), MSBlockShapes.CARVED_WOODEN_LEAF));
	
	public static final DeferredBlock<Block> UNCARVED_WOOD = REGISTER.register("uncarved_wood", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> UNCARVED_WOOD_STAIRS = REGISTER.register("uncarved_wood_stairs", () -> new StairBlock(() -> MSBlocks.UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> UNCARVED_WOOD_SLAB = REGISTER.register("uncarved_wood_slab", () -> new SlabBlock(ofFullCopy(UNCARVED_WOOD.get())));
	public static final DeferredBlock<ButtonBlock> UNCARVED_WOOD_BUTTON = REGISTER.register("uncarved_wood_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(UNCARVED_WOOD.get())));
	public static final DeferredBlock<PressurePlateBlock> UNCARVED_WOOD_PRESSURE_PLATE = REGISTER.register("uncarved_wood_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceBlock> UNCARVED_WOOD_FENCE = REGISTER.register("uncarved_wood_fence", () -> new FenceBlock(ofFullCopy(UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceGateBlock> UNCARVED_WOOD_FENCE_GATE = REGISTER.register("uncarved_wood_fence_gate", () -> new FenceGateBlock(ofFullCopy(UNCARVED_WOOD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> CHIPBOARD = REGISTER.register("chipboard", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final DeferredBlock<StairBlock> CHIPBOARD_STAIRS = REGISTER.register("chipboard_stairs", () -> new StairBlock(() -> MSBlocks.CHIPBOARD.get().defaultBlockState(), ofFullCopy(CHIPBOARD.get())));
	public static final DeferredBlock<SlabBlock> CHIPBOARD_SLAB = REGISTER.register("chipboard_slab", () -> new SlabBlock(ofFullCopy(CHIPBOARD.get())));
	public static final DeferredBlock<ButtonBlock> CHIPBOARD_BUTTON = REGISTER.register("chipboard_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(CHIPBOARD.get())));
	public static final DeferredBlock<PressurePlateBlock> CHIPBOARD_PRESSURE_PLATE = REGISTER.register("chipboard_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(CHIPBOARD.get())));
	public static final DeferredBlock<FenceBlock> CHIPBOARD_FENCE = REGISTER.register("chipboard_fence", () -> new FenceBlock(ofFullCopy(CHIPBOARD.get())));
	public static final DeferredBlock<FenceGateBlock> CHIPBOARD_FENCE_GATE = REGISTER.register("chipboard_fence_gate", () -> new FenceGateBlock(ofFullCopy(CHIPBOARD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> WOOD_SHAVINGS = REGISTER.register("wood_shavings", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.4F).sound(SoundType.SAND)));
	public static final DeferredBlock<Block> CARVED_HEAVY_PLANKS = REGISTER.register("carved_heavy_planks", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> CARVED_HEAVY_PLANK_STAIRS = REGISTER.register("carved_heavy_plank_stairs", () -> new StairBlock(() -> MSBlocks.CARVED_HEAVY_PLANKS.get().defaultBlockState(), ofFullCopy(CARVED_HEAVY_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> CARVED_HEAVY_PLANK_SLAB = REGISTER.register("carved_heavy_plank_slab", () -> new SlabBlock(ofFullCopy(CARVED_HEAVY_PLANKS.get())));
	
	public static final DeferredBlock<Block> CARVED_PLANKS = REGISTER.register("carved_planks", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> CARVED_STAIRS = REGISTER.register("carved_stairs", () -> new StairBlock(() -> MSBlocks.CARVED_PLANKS.get().defaultBlockState(), ofFullCopy(CARVED_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> CARVED_SLAB = REGISTER.register("carved_slab", () -> new SlabBlock(ofFullCopy(CARVED_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> CARVED_BUTTON = REGISTER.register("carved_button", () -> new ButtonBlock(MSBlockSetType.CARVED, 10, ofFullCopy(CARVED_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> CARVED_PRESSURE_PLATE = REGISTER.register("carved_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.CARVED, ofFullCopy(CARVED_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> CARVED_FENCE = REGISTER.register("carved_fence", () -> new FenceBlock(ofFullCopy(CARVED_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> CARVED_FENCE_GATE = REGISTER.register("carved_fence_gate", () -> new FenceGateBlock(ofFullCopy(CARVED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> CARVED_DOOR = REGISTER.register("carved_door", () -> new DoorBlock(MSBlockSetType.CARVED, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> CARVED_TRAPDOOR = REGISTER.register("carved_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.CARVED, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> CARVED_SIGN = REGISTER.register("carved_sign", () -> new MSStandingSignBlock(MSWoodTypes.CARVED, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> CARVED_WALL_SIGN = REGISTER.register("carved_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.CARVED, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> CARVED_HANGING_SIGN = REGISTER.register("carved_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.CARVED, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> CARVED_WALL_HANGING_SIGN = REGISTER.register("carved_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.CARVED, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	public static final DeferredBlock<Block> POLISHED_UNCARVED_WOOD = REGISTER.register("polished_carved_wood", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> POLISHED_UNCARVED_STAIRS = REGISTER.register("polished_uncarved_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(POLISHED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_UNCARVED_SLAB = REGISTER.register("polished_uncarved_slab", () -> new SlabBlock(ofFullCopy(POLISHED_UNCARVED_WOOD.get())));
	
	public static final DeferredBlock<Block> CARVED_BUSH = REGISTER.register("carved_bush", () -> new WoodenFloraBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASEDRUM).strength(0.6F).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noCollission().offsetType(BlockBehaviour.OffsetType.XZ), WoodenFloraBlock.FLOWER_SHAPE));
	public static final DeferredBlock<Block> CARVED_KNOTTED_WOOD = REGISTER.register("carved_knotted_wood", () -> new MSHorizontalDirectionalBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> WOODEN_GRASS = REGISTER.register("wooden_grass", () -> new WoodenFloraBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.1F, 2.5F).requiresCorrectToolForDrops().sound(SoundType.WOOD).offsetType(BlockBehaviour.OffsetType.XYZ), WoodenFloraBlock.GRASS_SHAPE));
	
	public static final DeferredBlock<Block> TREATED_UNCARVED_WOOD = REGISTER.register("treated_uncarved_wood", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> TREATED_UNCARVED_WOOD_STAIRS = REGISTER.register("treated_uncarved_wood_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> TREATED_UNCARVED_WOOD_SLAB = REGISTER.register("treated_uncarved_wood_slab", () -> new SlabBlock(ofFullCopy(TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<ButtonBlock> TREATED_UNCARVED_WOOD_BUTTON = REGISTER.register("treated_uncarved_wood_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<PressurePlateBlock> TREATED_UNCARVED_WOOD_PRESSURE_PLATE = REGISTER.register("treated_uncarved_wood_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceBlock> TREATED_UNCARVED_WOOD_FENCE = REGISTER.register("treated_uncarved_wood_fence", () -> new FenceBlock(ofFullCopy(TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceGateBlock> TREATED_UNCARVED_WOOD_FENCE_GATE = REGISTER.register("treated_uncarved_wood_fence_gate", () -> new FenceGateBlock(ofFullCopy(TREATED_UNCARVED_WOOD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> TREATED_CHIPBOARD = REGISTER.register("treated_chipboard", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final DeferredBlock<StairBlock> TREATED_CHIPBOARD_STAIRS = REGISTER.register("treated_chipboard_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_CHIPBOARD.get().defaultBlockState(), ofFullCopy(TREATED_CHIPBOARD.get())));
	public static final DeferredBlock<SlabBlock> TREATED_CHIPBOARD_SLAB = REGISTER.register("treated_chipboard_slab", () -> new SlabBlock(ofFullCopy(TREATED_CHIPBOARD.get())));
	public static final DeferredBlock<ButtonBlock> TREATED_CHIPBOARD_BUTTON = REGISTER.register("treated_chipboard_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(TREATED_CHIPBOARD.get())));
	public static final DeferredBlock<PressurePlateBlock> TREATED_CHIPBOARD_PRESSURE_PLATE = REGISTER.register("treated_chipboard_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(TREATED_CHIPBOARD.get())));
	public static final DeferredBlock<FenceBlock> TREATED_CHIPBOARD_FENCE = REGISTER.register("treated_chipboard_fence", () -> new FenceBlock(ofFullCopy(TREATED_CHIPBOARD.get())));
	public static final DeferredBlock<FenceGateBlock> TREATED_CHIPBOARD_FENCE_GATE = REGISTER.register("treated_chipboard_fence_gate", () -> new FenceGateBlock(ofFullCopy(TREATED_CHIPBOARD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> TREATED_WOOD_SHAVINGS = REGISTER.register("treated_wood_shavings", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.4F).sound(SoundType.SAND)));
	public static final DeferredBlock<Block> TREATED_HEAVY_PLANKS = REGISTER.register("treated_heavy_planks", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> TREATED_HEAVY_PLANK_STAIRS = REGISTER.register("treated_heavy_plank_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_HEAVY_PLANKS.get().defaultBlockState(), ofFullCopy(TREATED_HEAVY_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> TREATED_HEAVY_PLANK_SLAB = REGISTER.register("treated_heavy_plank_slab", () -> new SlabBlock(ofFullCopy(TREATED_HEAVY_PLANKS.get())));
	
	public static final DeferredBlock<Block> TREATED_PLANKS = REGISTER.register("treated_planks", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> TREATED_PLANKS_STAIRS = REGISTER.register("treated_planks_stairs", () -> new StairBlock(() -> MSBlocks.TREATED_PLANKS.get().defaultBlockState(), ofFullCopy(TREATED_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> TREATED_PLANKS_SLAB = REGISTER.register("treated_planks_slab", () -> new SlabBlock(ofFullCopy(TREATED_PLANKS.get())));
	public static final DeferredBlock<Block> TREATED_BOOKSHELF = REGISTER.register("treated_bookshelf", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> TREATED_LADDER = REGISTER.register("treated_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<ButtonBlock> TREATED_BUTTON = REGISTER.register("treated_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(TREATED_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> TREATED_PRESSURE_PLATE = REGISTER.register("treated_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(TREATED_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> TREATED_FENCE = REGISTER.register("treated_fence", () -> new FenceBlock(ofFullCopy(TREATED_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> TREATED_FENCE_GATE = REGISTER.register("treated_fence_gate", () -> new FenceGateBlock(ofFullCopy(TREATED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> TREATED_DOOR = REGISTER.register("treated_door", () -> new DoorBlock(BlockSetType.OAK, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> TREATED_TRAPDOOR = REGISTER.register("treated_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> TREATED_SIGN = REGISTER.register("treated_sign", () -> new MSStandingSignBlock(MSWoodTypes.TREATED, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> TREATED_WALL_SIGN = REGISTER.register("treated_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.TREATED, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> TREATED_HANGING_SIGN = REGISTER.register("treated_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.TREATED, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> TREATED_WALL_HANGING_SIGN = REGISTER.register("treated_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.TREATED, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	
	public static final DeferredBlock<Block> POLISHED_TREATED_UNCARVED_WOOD = REGISTER.register("polished_treated_carved_wood", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> POLISHED_TREATED_UNCARVED_STAIRS = REGISTER.register("polished_treated_uncarved_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_TREATED_UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(POLISHED_TREATED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_TREATED_UNCARVED_SLAB = REGISTER.register("polished_treated_uncarved_slab", () -> new SlabBlock(ofFullCopy(POLISHED_TREATED_UNCARVED_WOOD.get())));
	
	public static final DeferredBlock<Block> TREATED_CARVED_KNOTTED_WOOD = REGISTER.register("treated_carved_knotted_wood", () -> new MSHorizontalDirectionalBlock(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> TREATED_WOODEN_GRASS = REGISTER.register("treated_wooden_grass", () -> new WoodenFloraBlock(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noCollission().strength(0.1F, 2.5F).requiresCorrectToolForDrops().sound(SoundType.WOOD).offsetType(BlockBehaviour.OffsetType.XYZ), WoodenFloraBlock.GRASS_SHAPE));
	
	public static final DeferredBlock<Block> LACQUERED_UNCARVED_WOOD = REGISTER.register("lacquered_uncarved_wood", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> LACQUERED_UNCARVED_WOOD_STAIRS = REGISTER.register("lacquered_uncarved_wood_stairs", () -> new StairBlock(() -> MSBlocks.LACQUERED_UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> LACQUERED_UNCARVED_WOOD_SLAB = REGISTER.register("lacquered_uncarved_wood_slab", () -> new SlabBlock(ofFullCopy(LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<ButtonBlock> LACQUERED_UNCARVED_WOOD_BUTTON = REGISTER.register("lacquered_uncarved_wood_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<PressurePlateBlock> LACQUERED_UNCARVED_WOOD_PRESSURE_PLATE = REGISTER.register("lacquered_uncarved_wood_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceBlock> LACQUERED_UNCARVED_WOOD_FENCE = REGISTER.register("lacquered_uncarved_wood_fence", () -> new FenceBlock(ofFullCopy(LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<FenceGateBlock> LACQUERED_UNCARVED_WOOD_FENCE_GATE = REGISTER.register("lacquered_uncarved_wood_fence_gate", () -> new FenceGateBlock(ofFullCopy(LACQUERED_UNCARVED_WOOD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> LACQUERED_CHIPBOARD = REGISTER.register("lacquered_chipboard", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.0F).requiresCorrectToolForDrops().sound(SoundType.SCAFFOLDING)));
	public static final DeferredBlock<StairBlock> LACQUERED_CHIPBOARD_STAIRS = REGISTER.register("lacquered_chipboard_stairs", () -> new StairBlock(() -> MSBlocks.LACQUERED_CHIPBOARD.get().defaultBlockState(), ofFullCopy(LACQUERED_CHIPBOARD.get())));
	public static final DeferredBlock<SlabBlock> LACQUERED_CHIPBOARD_SLAB = REGISTER.register("lacquered_chipboard_slab", () -> new SlabBlock(ofFullCopy(LACQUERED_CHIPBOARD.get())));
	public static final DeferredBlock<ButtonBlock> LACQUERED_CHIPBOARD_BUTTON = REGISTER.register("lacquered_chipboard_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(LACQUERED_CHIPBOARD.get())));
	public static final DeferredBlock<PressurePlateBlock> LACQUERED_CHIPBOARD_PRESSURE_PLATE = REGISTER.register("lacquered_chipboard_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(LACQUERED_CHIPBOARD.get())));
	public static final DeferredBlock<FenceBlock> LACQUERED_CHIPBOARD_FENCE = REGISTER.register("lacquered_chipboard_fence", () -> new FenceBlock(ofFullCopy(LACQUERED_CHIPBOARD.get())));
	public static final DeferredBlock<FenceGateBlock> LACQUERED_CHIPBOARD_FENCE_GATE = REGISTER.register("lacquered_chipboard_fence_gate", () -> new FenceGateBlock(ofFullCopy(LACQUERED_CHIPBOARD.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	
	public static final DeferredBlock<Block> LACQUERED_WOOD_SHAVINGS = REGISTER.register("lacquered_wood_shavings", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.4F).sound(SoundType.SAND)));
	public static final DeferredBlock<Block> LACQUERED_HEAVY_PLANKS = REGISTER.register("lacquered_heavy_planks", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> LACQUERED_HEAVY_PLANK_STAIRS = REGISTER.register("lacquered_heavy_plank_stairs", () -> new StairBlock(() -> MSBlocks.LACQUERED_HEAVY_PLANKS.get().defaultBlockState(), ofFullCopy(LACQUERED_HEAVY_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> LACQUERED_HEAVY_PLANK_SLAB = REGISTER.register("lacquered_heavy_plank_slab", () -> new SlabBlock(ofFullCopy(LACQUERED_HEAVY_PLANKS.get())));
	
	public static final DeferredBlock<Block> LACQUERED_PLANKS = REGISTER.register("lacquered_planks", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> LACQUERED_STAIRS = REGISTER.register("lacquered_stairs", () -> new StairBlock(() -> MSBlocks.LACQUERED_PLANKS.get().defaultBlockState(), ofFullCopy(LACQUERED_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> LACQUERED_SLAB = REGISTER.register("lacquered_slab", () -> new SlabBlock(ofFullCopy(LACQUERED_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> LACQUERED_BUTTON = REGISTER.register("lacquered_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(LACQUERED_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> LACQUERED_PRESSURE_PLATE = REGISTER.register("lacquered_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(LACQUERED_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> LACQUERED_FENCE = REGISTER.register("lacquered_fence", () -> new FenceBlock(ofFullCopy(LACQUERED_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> LACQUERED_FENCE_GATE = REGISTER.register("lacquered_fence_gate", () -> new FenceGateBlock(ofFullCopy(LACQUERED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> LACQUERED_DOOR = REGISTER.register("lacquered_door", () -> new DoorBlock(BlockSetType.OAK, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> LACQUERED_TRAPDOOR = REGISTER.register("lacquered_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> LACQUERED_SIGN = REGISTER.register("lacquered_sign", () -> new MSStandingSignBlock(MSWoodTypes.LACQUERED, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> LACQUERED_WALL_SIGN = REGISTER.register("lacquered_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.LACQUERED, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> LACQUERED_HANGING_SIGN = REGISTER.register("lacquered_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.LACQUERED, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> LACQUERED_WALL_HANGING_SIGN = REGISTER.register("lacquered_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.LACQUERED, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	public static final DeferredBlock<Block> POLISHED_LACQUERED_UNCARVED_WOOD = REGISTER.register("polished_lacquered_carved_wood", () -> new FlammableBlock(0, 0, Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> POLISHED_LACQUERED_UNCARVED_STAIRS = REGISTER.register("polished_lacquered_uncarved_stairs", () -> new StairBlock(() -> MSBlocks.POLISHED_LACQUERED_UNCARVED_WOOD.get().defaultBlockState(), ofFullCopy(POLISHED_LACQUERED_UNCARVED_WOOD.get())));
	public static final DeferredBlock<SlabBlock> POLISHED_LACQUERED_UNCARVED_SLAB = REGISTER.register("polished_lacquered_uncarved_slab", () -> new SlabBlock(ofFullCopy(POLISHED_LACQUERED_UNCARVED_WOOD.get())));
	
	public static final DeferredBlock<Block> LACQUERED_CARVED_KNOTTED_WOOD = REGISTER.register("lacquered_carved_knotted_wood", () -> new MSHorizontalDirectionalBlock(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).requiresCorrectToolForDrops().sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> LACQUERED_WOODEN_MUSHROOM = REGISTER.register("lacquered_wooden_mushroom", () -> new WoodenFloraBlock(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noCollission().strength(0.1F, 2.5F).requiresCorrectToolForDrops().sound(SoundType.WOOD).offsetType(BlockBehaviour.OffsetType.XYZ), WoodenFloraBlock.FLOWER_SHAPE));
	
	public static final DeferredBlock<Block> WOODEN_LAMP = REGISTER.register("wooden_lamp", () -> new Block(Block.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 15).sound(SoundType.WOOD)));
	
	//Cloud
	public static final DeferredBlock<Block> DENSE_CLOUD = REGISTER.register("dense_cloud", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.HAT).strength(0.5F).sound(SoundType.SNOW).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> BRIGHT_DENSE_CLOUD = REGISTER.register("bright_dense_cloud", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.HAT).strength(0.5F).sound(SoundType.SNOW).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> SUGAR_CUBE = REGISTER.register("sugar_cube", () -> new Block(Block.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.SNARE).strength(0.4F).sound(SoundType.SAND)));
	
	//Land Tree Blocks
	//Glowing
	public static final DeferredBlock<Block> GLOWING_LOG = REGISTER.register("glowing_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> GLOWING_WOOD = REGISTER.register("glowing_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_GLOWING_LOG = REGISTER.register("stripped_glowing_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_GLOWING_WOOD = REGISTER.register("stripped_glowing_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 11).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> GLOWING_PLANKS = REGISTER.register("glowing_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> GLOWING_BOOKSHELF = REGISTER.register("glowing_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> GLOWING_LADDER = REGISTER.register("glowing_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<StairBlock> GLOWING_STAIRS = REGISTER.register("glowing_stairs", () -> new StairBlock(() -> MSBlocks.GLOWING_PLANKS.get().defaultBlockState(), ofFullCopy(GLOWING_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> GLOWING_SLAB = REGISTER.register("glowing_slab", () -> new SlabBlock(ofFullCopy(GLOWING_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> GLOWING_BUTTON = REGISTER.register("glowing_button", () -> new ButtonBlock(BlockSetType.OAK, 10, ofFullCopy(GLOWING_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> GLOWING_PRESSURE_PLATE = REGISTER.register("glowing_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, ofFullCopy(GLOWING_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> GLOWING_FENCE = REGISTER.register("glowing_fence", () -> new FenceBlock(ofFullCopy(GLOWING_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> GLOWING_FENCE_GATE = REGISTER.register("glowing_fence_gate", () -> new FenceGateBlock(ofFullCopy(GLOWING_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> GLOWING_DOOR = REGISTER.register("glowing_door", () -> new DoorBlock(BlockSetType.OAK,
			Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	public static final DeferredBlock<TrapDoorBlock> GLOWING_TRAPDOOR = REGISTER.register("glowing_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK,
			Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).lightLevel(state -> 7).sound(SoundType.WOOD)));
	
	public static final DeferredBlock<StandingSignBlock> GLOWING_SIGN = REGISTER.register("glowing_sign", () -> new MSStandingSignBlock(MSWoodTypes.GLOWING, ofFullCopy(Blocks.OAK_SIGN).lightLevel(state -> 11)));
	public static final DeferredBlock<WallSignBlock> GLOWING_WALL_SIGN = REGISTER.register("glowing_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.GLOWING, ofFullCopy(Blocks.OAK_WALL_SIGN).lightLevel(state -> 11)));
	public static final DeferredBlock<Block> GLOWING_HANGING_SIGN = REGISTER.register("glowing_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.GLOWING, ofFullCopy(Blocks.OAK_HANGING_SIGN).lightLevel(state -> 11)));
	public static final DeferredBlock<Block> GLOWING_WALL_HANGING_SIGN = REGISTER.register("glowing_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.GLOWING, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lightLevel(state -> 11)));
	
	//Frost
	public static final DeferredBlock<Block> FROST_LOG = REGISTER.register("frost_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.ICE).strength(2.0F).ignitedByLava().instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> FROST_WOOD = REGISTER.register("frost_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.ICE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_FROST_LOG = REGISTER.register("stripped_frost_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_FROST_WOOD = REGISTER.register("stripped_frost_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> FROST_PLANKS = REGISTER.register("frost_planks", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.ICE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> FROST_LEAVES = REGISTER.register("frost_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> FROST_LEAVES_FLOWERING = REGISTER.register("frost_leaves_flowering", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> FROST_BOOKSHELF = REGISTER.register("frost_bookshelf", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> FROST_LADDER = REGISTER.register("frost_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<StairBlock> FROST_STAIRS = REGISTER.register("frost_stairs", () -> new StairBlock(() -> MSBlocks.FROST_PLANKS.get().defaultBlockState(), ofFullCopy(FROST_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> FROST_SLAB = REGISTER.register("frost_slab", () -> new SlabBlock(ofFullCopy(FROST_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> FROST_BUTTON = REGISTER.register("frost_button", () -> new ButtonBlock(MSBlockSetType.FROST, 10, ofFullCopy(FROST_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> FROST_PRESSURE_PLATE = REGISTER.register("frost_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.FROST, ofFullCopy(FROST_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> FROST_FENCE = REGISTER.register("frost_fence", () -> new FenceBlock(ofFullCopy(FROST_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> FROST_FENCE_GATE = REGISTER.register("frost_fence_gate", () -> new FenceGateBlock(ofFullCopy(FROST_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> FROST_DOOR = REGISTER.register("frost_door", () -> new DoorBlock(MSBlockSetType.FROST, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> FROST_TRAPDOOR = REGISTER.register("frost_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.FROST, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<Block> FROST_SAPLING = REGISTER.register("frost_sapling", () -> new FrostSaplingBlock(ofFullCopy(Blocks.OAK_SAPLING)));
	public static final DeferredBlock<Block> POTTED_FROST_SAPLING = REGISTER.register("potted_frost_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, FROST_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<StandingSignBlock> FROST_SIGN = REGISTER.register("frost_sign", () -> new MSStandingSignBlock(MSWoodTypes.FROST, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> FROST_WALL_SIGN = REGISTER.register("frost_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.FROST, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> FROST_HANGING_SIGN = REGISTER.register("frost_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.FROST, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> FROST_WALL_HANGING_SIGN = REGISTER.register("frost_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.FROST, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	//Rainbow
	public static final DeferredBlock<Block> RAINBOW_LOG = REGISTER.register("rainbow_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> RAINBOW_WOOD = REGISTER.register("rainbow_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_RAINBOW_LOG = REGISTER.register("stripped_rainbow_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_RAINBOW_WOOD = REGISTER.register("stripped_rainbow_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> RAINBOW_PLANKS = REGISTER.register("rainbow_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> RAINBOW_STAIRS = REGISTER.register("rainbow_planks_stairs", () -> new StairBlock(() -> MSBlocks.RAINBOW_PLANKS.get().defaultBlockState(), ofFullCopy(RAINBOW_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> RAINBOW_SLAB = REGISTER.register("rainbow_planks_slab", () -> new SlabBlock(ofFullCopy(RAINBOW_PLANKS.get())));
	public static final DeferredBlock<Block> RAINBOW_LEAVES = REGISTER.register("rainbow_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> RAINBOW_SAPLING = REGISTER.register("rainbow_sapling", () -> new RainbowSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredBlock<Block> POTTED_RAINBOW_SAPLING = REGISTER.register("potted_rainbow_sapling",() -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, RAINBOW_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	public static final DeferredBlock<Block> RAINBOW_BOOKSHELF = REGISTER.register("rainbow_bookshelf", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> RAINBOW_LADDER = REGISTER.register("rainbow_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<ButtonBlock> RAINBOW_BUTTON = REGISTER.register("rainbow_button", () -> new ButtonBlock(MSBlockSetType.RAINBOW, 10, ofFullCopy(RAINBOW_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> RAINBOW_PRESSURE_PLATE = REGISTER.register("rainbow_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.RAINBOW, ofFullCopy(RAINBOW_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> RAINBOW_FENCE = REGISTER.register("rainbow_fence", () -> new FenceBlock(ofFullCopy(RAINBOW_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> RAINBOW_FENCE_GATE = REGISTER.register("rainbow_fence_gate", () -> new FenceGateBlock(ofFullCopy(RAINBOW_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> RAINBOW_DOOR = REGISTER.register("rainbow_door", () -> new DoorBlock(MSBlockSetType.RAINBOW, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> RAINBOW_TRAPDOOR = REGISTER.register("rainbow_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.RAINBOW, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> RAINBOW_SIGN = REGISTER.register("rainbow_sign", () -> new MSStandingSignBlock(MSWoodTypes.RAINBOW, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> RAINBOW_WALL_SIGN = REGISTER.register("rainbow_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.RAINBOW, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> RAINBOW_HANGING_SIGN = REGISTER.register("rainbow_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.RAINBOW, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> RAINBOW_WALL_HANGING_SIGN = REGISTER.register("rainbow_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.RAINBOW, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	
	//End
	public static final DeferredBlock<Block> END_LOG = REGISTER.register("end_log", () -> new DoubleLogBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> END_WOOD = REGISTER.register("end_wood", () -> new FlammableLogBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_END_LOG = REGISTER.register("stripped_end_log", () -> new DoubleLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.COLOR_MAGENTA, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_END_WOOD = REGISTER.register("stripped_end_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.COLOR_MAGENTA, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> END_PLANKS = REGISTER.register("end_planks", () -> new FlammableBlock(1, 250, Block.Properties.of().mapColor(MapColor.SAND).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> END_STAIRS = REGISTER.register("end_planks_stairs", () -> new StairBlock(() -> MSBlocks.END_PLANKS.get().defaultBlockState(), ofFullCopy(END_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> END_SLAB = REGISTER.register("end_planks_slab", () -> new SlabBlock(ofFullCopy(END_PLANKS.get())));
	public static final DeferredBlock<Block> END_LEAVES = REGISTER.register("end_leaves", () -> new EndLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<BushBlock> END_SAPLING = REGISTER.register("end_sapling", () -> new EndSaplingBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS)));
	public static final DeferredBlock<Block> POTTED_END_SAPLING = REGISTER.register("potted_end_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, END_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	public static final DeferredBlock<Block> END_LADDER = REGISTER.register("end_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<Block> END_BOOKSHELF = REGISTER.register("end_bookshelf", () -> new FlammableBlock(1, 250, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<ButtonBlock> END_BUTTON = REGISTER.register("end_button", () -> new ButtonBlock(MSBlockSetType.END, 10, ofFullCopy(END_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> END_PRESSURE_PLATE = REGISTER.register("end_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.END, ofFullCopy(END_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> END_FENCE = REGISTER.register("end_fence", () -> new FenceBlock(ofFullCopy(END_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> END_FENCE_GATE = REGISTER.register("end_fence_gate", () -> new FenceGateBlock(ofFullCopy(END_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> END_DOOR = REGISTER.register("end_door", () -> new DoorBlock(MSBlockSetType.END, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> END_TRAPDOOR = REGISTER.register("end_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.END, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> END_SIGN = REGISTER.register("end_sign", () -> new MSStandingSignBlock(MSWoodTypes.END, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> END_WALL_SIGN = REGISTER.register("end_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.END, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> END_HANGING_SIGN = REGISTER.register("end_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.END, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> END_WALL_HANGING_SIGN = REGISTER.register("end_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.END, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	//Vine
	public static final DeferredBlock<Block> VINE_LOG = REGISTER.register("vine_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> VINE_WOOD = REGISTER.register("vine_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	public static final DeferredBlock<Block> FLOWERY_VINE_LOG = REGISTER.register("flowery_vine_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> FLOWERY_VINE_WOOD = REGISTER.register("flowery_vine_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	
	//Dead
	public static final DeferredBlock<Block> DEAD_LOG = REGISTER.register("dead_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> DEAD_WOOD = REGISTER.register("dead_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.PODZOL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_DEAD_LOG = REGISTER.register("stripped_dead_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_DEAD_WOOD = REGISTER.register("stripped_dead_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> DEAD_PLANKS = REGISTER.register("dead_planks", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> DEAD_STAIRS = REGISTER.register("dead_planks_stairs", () -> new StairBlock(() -> MSBlocks.DEAD_PLANKS.get().defaultBlockState(), ofFullCopy(DEAD_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> DEAD_SLAB = REGISTER.register("dead_planks_slab", () -> new SlabBlock(ofFullCopy(DEAD_PLANKS.get())));
	public static final DeferredBlock<Block> DEAD_BOOKSHELF = REGISTER.register("dead_bookshelf", () -> new FlammableBlock(5, 5, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> DEAD_LADDER = REGISTER.register("dead_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final DeferredBlock<ButtonBlock> DEAD_BUTTON = REGISTER.register("dead_button", () -> new ButtonBlock(MSBlockSetType.DEAD, 10, ofFullCopy(DEAD_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> DEAD_PRESSURE_PLATE = REGISTER.register("dead_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.DEAD, ofFullCopy(DEAD_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> DEAD_FENCE = REGISTER.register("dead_fence", () -> new FenceBlock(ofFullCopy(DEAD_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> DEAD_FENCE_GATE = REGISTER.register("dead_fence_gate", () -> new FenceGateBlock(ofFullCopy(DEAD_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> DEAD_DOOR = REGISTER.register("dead_door", () -> new DoorBlock(MSBlockSetType.DEAD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> DEAD_TRAPDOOR = REGISTER.register("dead_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.DEAD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> DEAD_SIGN = REGISTER.register("dead_sign", () -> new MSStandingSignBlock(MSWoodTypes.DEAD, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> DEAD_WALL_SIGN = REGISTER.register("dead_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.DEAD, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> DEAD_HANGING_SIGN = REGISTER.register("dead_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.DEAD, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> DEAD_WALL_HANGING_SIGN = REGISTER.register("dead_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.DEAD, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	//Petrified
	public static final DeferredBlock<Block> PETRIFIED_LOG = REGISTER.register("petrified_log", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STONE)));
	public static final DeferredBlock<Block> PETRIFIED_WOOD = REGISTER.register("petrified_wood", () -> new RotatedPillarBlock(Block.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.STONE)));
	
	//Cindered
	public static final DeferredBlock<Block> CINDERED_LOG = REGISTER.register("cindered_log", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.GUITAR).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> CINDERED_WOOD = REGISTER.register("cindered_wood", () -> new FlammableLogBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.GUITAR).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_CINDERED_LOG = REGISTER.register("stripped_cindered_log", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> STRIPPED_CINDERED_WOOD = REGISTER.register("stripped_cindered_wood", () -> new FlammableLogBlock(BlockBehaviour.Properties.of().mapColor(logColors(MapColor.WOOD, MapColor.PODZOL)).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> CINDERED_PLANKS = REGISTER.register("cindered_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.GUITAR).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> CINDERED_STAIRS = REGISTER.register("cindered_stairs", () -> new StairBlock(() -> MSBlocks.CINDERED_PLANKS.get().defaultBlockState(), ofFullCopy(CINDERED_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> CINDERED_SLAB = REGISTER.register("cindered_slab", () -> new SlabBlock(ofFullCopy(CINDERED_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> CINDERED_BUTTON = REGISTER.register("cindered_button", () -> new ButtonBlock(MSBlockSetType.CINDERED, 10, ofFullCopy(CINDERED_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> CINDERED_PRESSURE_PLATE = REGISTER.register("cindered_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.CINDERED, ofFullCopy(CINDERED_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> CINDERED_FENCE = REGISTER.register("cindered_fence", () -> new FenceBlock(ofFullCopy(CINDERED_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> CINDERED_FENCE_GATE = REGISTER.register("cindered_fence_gate", () -> new FenceGateBlock(ofFullCopy(CINDERED_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> CINDERED_DOOR = REGISTER.register("cindered_door", () -> new DoorBlock(MSBlockSetType.CINDERED, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> CINDERED_TRAPDOOR = REGISTER.register("cindered_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.CINDERED, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> CINDERED_SIGN = REGISTER.register("cindered_sign", () -> new MSStandingSignBlock(MSWoodTypes.CINDERED, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> CINDERED_WALL_SIGN = REGISTER.register("cindered_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.CINDERED, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> CINDERED_HANGING_SIGN = REGISTER.register("cindered_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.CINDERED, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> CINDERED_WALL_HANGING_SIGN = REGISTER.register("cindered_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.CINDERED, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	//Shadewood
	public static final DeferredBlock<Block> SHADEWOOD_LOG = REGISTER.register("shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final DeferredBlock<Block> SHADEWOOD = REGISTER.register("shadewood", () -> new StrippableFlammableLogBlock(ofFullCopy(SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_SHADEWOOD.get().defaultBlockState()));
	public static final DeferredBlock<Block> SHADEWOOD_PLANKS = REGISTER.register("shadewood_planks", () -> new FlammableBlock(5, 20, Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<StairBlock> SHADEWOOD_STAIRS = REGISTER.register("shadewood_stairs", () -> new StairBlock(() -> MSBlocks.SHADEWOOD_PLANKS.get().defaultBlockState(), ofFullCopy(SHADEWOOD_PLANKS.get())));
	public static final DeferredBlock<SlabBlock> SHADEWOOD_SLAB = REGISTER.register("shadewood_slab", () -> new SlabBlock(ofFullCopy(SHADEWOOD_PLANKS.get())));
	public static final DeferredBlock<ButtonBlock> SHADEWOOD_BUTTON = REGISTER.register("shadewood_button", () -> new ButtonBlock(MSBlockSetType.SHADEWOOD, 10, ofFullCopy(SHADEWOOD_PLANKS.get())));
	public static final DeferredBlock<PressurePlateBlock> SHADEWOOD_PRESSURE_PLATE = REGISTER.register("shadewood_pressure_plate", () -> new PressurePlateBlock(MSBlockSetType.SHADEWOOD, ofFullCopy(SHADEWOOD_PLANKS.get())));
	public static final DeferredBlock<FenceBlock> SHADEWOOD_FENCE = REGISTER.register("shadewood_fence", () -> new FenceBlock(ofFullCopy(SHADEWOOD_PLANKS.get())));
	public static final DeferredBlock<FenceGateBlock> SHADEWOOD_FENCE_GATE = REGISTER.register("shadewood_fence_gate", () -> new FenceGateBlock(ofFullCopy(SHADEWOOD_PLANKS.get()), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
	public static final DeferredBlock<DoorBlock> SHADEWOOD_DOOR = REGISTER.register("shadewood_door", () -> new DoorBlock(MSBlockSetType.SHADEWOOD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	public static final DeferredBlock<TrapDoorBlock> SHADEWOOD_TRAPDOOR = REGISTER.register("shadewood_trapdoor", () -> new TrapDoorBlock(MSBlockSetType.SHADEWOOD, ofFullCopy(Blocks.OAK_TRAPDOOR)));
	
	public static final DeferredBlock<StandingSignBlock> SHADEWOOD_SIGN = REGISTER.register("shadewood_sign", () -> new MSStandingSignBlock(MSWoodTypes.SHADEWOOD, ofFullCopy(Blocks.OAK_SIGN)));
	public static final DeferredBlock<WallSignBlock> SHADEWOOD_WALL_SIGN = REGISTER.register("shadewood_wall_sign", () -> new MSWallSignBlock(MSWoodTypes.SHADEWOOD, ofFullCopy(Blocks.OAK_WALL_SIGN)));
	public static final DeferredBlock<Block> SHADEWOOD_HANGING_SIGN = REGISTER.register("shadewood_hanging_sign", () -> new MSHangingSignBlock(MSWoodTypes.SHADEWOOD, ofFullCopy(Blocks.OAK_HANGING_SIGN)));
	public static final DeferredBlock<Block> SHADEWOOD_WALL_HANGING_SIGN = REGISTER.register("shadewood_wall_hanging_sign", () -> new MSWallHangingSignBlock(MSWoodTypes.SHADEWOOD, ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));
	
	
	public static final DeferredBlock<Block> SHADEWOOD_LEAVES = REGISTER.register("shadewood_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	public static final DeferredBlock<Block> SHROOMY_SHADEWOOD_LEAVES = REGISTER.register("shroomy_shadewood_leaves", () -> new FlammableLeavesBlock(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().lightLevel(state -> 11).sound(SoundType.GRASS).noOcclusion().isValidSpawn(MSBlocks::leafSpawns).isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never).isRedstoneConductor(MSBlocks::never)));
	
	public static final DeferredBlock<Block> SHADEWOOD_SAPLING = REGISTER.register("shadewood_sapling", () -> new SaplingBlock(
			//One should be careful where to create tree growers because they're getting added to an internal data structure.
			// Doing it during item registration like here should be fine.
			new TreeGrower(Minestuck.id("shadewood").toString(), 0.05F, Optional.empty(), Optional.empty(), Optional.of(MSCFeatures.SHADEWOOD_TREE), Optional.of(MSCFeatures.SCARRED_SHADEWOOD_TREE), Optional.empty(), Optional.empty()),
			Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().lightLevel(state -> 8).strength(0).sound(SoundType.GRASS)));
	public static final DeferredBlock<Block> POTTED_SHADEWOOD_SAPLING = REGISTER.register("potted_shadewood_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SHADEWOOD_SAPLING, ofFullCopy(Blocks.POTTED_OAK_SAPLING)));
	
	public static final DeferredBlock<Block> SCARRED_SHADEWOOD_LOG = REGISTER.register("scarred_shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 6).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_SCARRED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final DeferredBlock<Block> SCARRED_SHADEWOOD = REGISTER.register("scarred_shadewood", () -> new StrippableFlammableLogBlock(ofFullCopy(SCARRED_SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_SCARRED_SHADEWOOD.get().defaultBlockState()));
	
	public static final DeferredBlock<Block> ROTTED_SHADEWOOD_LOG = REGISTER.register("rotted_shadewood_log", () -> new StrippableFlammableLogBlock(Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).lightLevel(state -> 5).sound(SoundType.WOOD), () -> MSBlocks.STRIPPED_ROTTED_SHADEWOOD_LOG.get().defaultBlockState()));
	public static final DeferredBlock<Block> ROTTED_SHADEWOOD = REGISTER.register("rotted_shadewood", () -> new StrippableFlammableLogBlock(ofFullCopy(ROTTED_SHADEWOOD_LOG.get()), () -> MSBlocks.STRIPPED_ROTTED_SHADEWOOD.get().defaultBlockState()));
	
	public static final DeferredBlock<Block> STRIPPED_SHADEWOOD_LOG = REGISTER.register("stripped_shadewood_log", () -> new FlammableLogBlock(ofFullCopy(SHADEWOOD_LOG.get())));
	public static final DeferredBlock<Block> STRIPPED_SHADEWOOD = REGISTER.register("stripped_shadewood", () -> new FlammableLogBlock(ofFullCopy(SHADEWOOD_LOG.get())));
	
	public static final DeferredBlock<Block> STRIPPED_SCARRED_SHADEWOOD_LOG = REGISTER.register("stripped_scarred_shadewood_log", () -> new FlammableLogBlock(ofFullCopy(SCARRED_SHADEWOOD_LOG.get())));
	public static final DeferredBlock<Block> STRIPPED_SCARRED_SHADEWOOD = REGISTER.register("stripped_scarred_shadewood", () -> new FlammableLogBlock(ofFullCopy(SCARRED_SHADEWOOD_LOG.get())));
	
	public static final DeferredBlock<Block> STRIPPED_ROTTED_SHADEWOOD_LOG = REGISTER.register("stripped_rotted_shadewood_log", () -> new FlammableLogBlock(ofFullCopy(ROTTED_SHADEWOOD_LOG.get())));
	public static final DeferredBlock<Block> STRIPPED_ROTTED_SHADEWOOD = REGISTER.register("stripped_rotted_shadewood", () -> new FlammableLogBlock(ofFullCopy(ROTTED_SHADEWOOD_LOG.get())));
	
	
	//Land Plant Blocks
	public static final DeferredBlock<Block> GLOWING_MUSHROOM = REGISTER.register("glowing_mushroom", () -> new GlowingMushroomBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11).offsetType(BlockBehaviour.OffsetType.XZ)));
	
	public static final DeferredBlock<Block> DESERT_BUSH = REGISTER.register("desert_bush", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> BLOOMING_CACTUS = REGISTER.register("blooming_cactus", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> SANDY_GRASS = REGISTER.register("sandy_grass", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> TALL_SANDY_GRASS = REGISTER.register("tall_sandy_grass", () -> new TallDesertPlantBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> DEAD_FOLIAGE = REGISTER.register("dead_foliage", () -> new DesertFloraBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> TALL_DEAD_BUSH = REGISTER.register("tall_dead_bush", () -> new TallDesertPlantBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	
	public static final DeferredBlock<Block> PETRIFIED_GRASS = REGISTER.register("petrified_grass", () -> new PetrifiedFloraBlock(Block.Properties.of().mapColor(DyeColor.GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XYZ), PetrifiedFloraBlock.GRASS_SHAPE));
	public static final DeferredBlock<Block> PETRIFIED_POPPY = REGISTER.register("petrified_poppy", () -> new PetrifiedFloraBlock(Block.Properties.of().mapColor(DyeColor.GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0).sound(SoundType.STONE).offsetType(BlockBehaviour.OffsetType.XZ), PetrifiedFloraBlock.FLOWER_SHAPE));
	
	public static final DeferredBlock<Block> IGNEOUS_SPIKE = REGISTER.register("igneous_spike", () -> new BurnedFoliageBlock(Block.Properties.of().mapColor(DyeColor.BROWN).instrument(NoteBlockInstrument.GUITAR).requiresCorrectToolForDrops().strength(1.5F).sound(SoundType.STONE)));
	public static final DeferredBlock<Block> SINGED_GRASS = REGISTER.register("singed_grass", () -> new BurnedFoliageBlock(Block.Properties.of().mapColor(DyeColor.GRAY).instrument(NoteBlockInstrument.GUITAR).pushReaction(PushReaction.DESTROY).noCollission().strength(0).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> SINGED_FOLIAGE = REGISTER.register("singed_foliage", () -> new BurnedFoliageBlock(ofFullCopy(SINGED_GRASS.get())));
	public static final DeferredBlock<Block> SULFUR_BUBBLE = REGISTER.register("sulfur_bubble", () -> new SulfurBubbleBlock(Block.Properties.of().mapColor(DyeColor.LIME).instrument(NoteBlockInstrument.BELL).pushReaction(PushReaction.DESTROY).strength(0.5F).sound(SoundType.GLASS)));
	
	public static final DeferredBlock<Block> GLOWING_MUSHROOM_VINES = REGISTER.register("glowing_mushroom_vines", () -> new GlowingMushroomVinesBlock(Block.Properties.of().mapColor(MapColor.DIAMOND).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.GRASS).lightLevel(state -> 11).offsetType(BlockBehaviour.OffsetType.XZ)));
	public static final DeferredBlock<Block> STRAWBERRY = REGISTER.register("strawberry", () -> new StrawberryBlock(Block.Properties.of().mapColor(MapColor.COLOR_RED).pushReaction(PushReaction.DESTROY).strength(1.0F).sound(SoundType.WOOD)));
	public static final DeferredBlock<AttachedStemBlock> ATTACHED_STRAWBERRY_STEM = REGISTER.register("attached_strawberry_stem",
			() -> new AttachedStemBlock(MSBlocks.STRAWBERRY_STEM.getKey(), MSBlocks.STRAWBERRY.getKey(), MSItems.STRAWBERRY_CHUNK.getKey(),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final DeferredBlock<StemBlock> STRAWBERRY_STEM = REGISTER.register("strawberry_stem",
			() -> new StemBlock(MSBlocks.STRAWBERRY.getKey(), MSBlocks.ATTACHED_STRAWBERRY_STEM.getKey(), MSItems.STRAWBERRY_CHUNK.getKey(),
					Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0).sound(SoundType.WOOD)));
	public static final DeferredBlock<Block> END_GRASS = REGISTER.register("end_grass", () -> new EndGrassBlock(Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 9.0F)));
	public static final DeferredBlock<Block> TALL_END_GRASS = REGISTER.register("tall_end_grass", () -> new TallEndGrassBlock(Block.Properties.of().mapColor(DyeColor.GREEN).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY).noCollission().randomTicks().strength(0.1F).sound(SoundType.NETHER_WART).offsetType(BlockBehaviour.OffsetType.XYZ)));
	public static final DeferredBlock<Block> GLOWFLOWER = REGISTER.register("glowflower", () -> new FlowerBlock(() -> MobEffects.GLOWING, 20, Block.Properties.of().mapColor(DyeColor.YELLOW).pushReaction(PushReaction.DESTROY).noCollission().strength(0).lightLevel(state -> 12).sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
	
	
	//Special Land Blocks
	public static final DeferredBlock<Block> GLOWY_GOOP = REGISTER.register("glowy_goop", () -> new SlimeBlock(Block.Properties.of().mapColor(MapColor.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK).lightLevel(state -> 14)));
	public static final DeferredBlock<Block> COAGULATED_BLOOD = REGISTER.register("coagulated_blood", () -> new SlimeBlock(Block.Properties.of().mapColor(MapColor.CLAY).strength(0.1F).sound(SoundType.SLIME_BLOCK)));
	public static final DeferredBlock<Block> PIPE = REGISTER.register("pipe", () -> new DirectionalCustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL), MSBlockShapes.PIPE));
	public static final DeferredBlock<Block> PIPE_INTERSECTION = REGISTER.register("pipe_intersection", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL))); //the intention is that later down the line, someone will improve the code of pipe blocks to allow for intersections or a separate intersection blockset will be made that actually work
	public static final DeferredBlock<Block> PARCEL_PYXIS = REGISTER.register("parcel_pyxis", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F), MSBlockShapes.PARCEL_PYXIS));
	public static final DeferredBlock<Block> PYXIS_LID = REGISTER.register("pyxis_lid", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1.0F), MSBlockShapes.PYXIS_LID));
	public static final DeferredBlock<Block> STONE_TABLET = REGISTER.register("stone_tablet", () -> new StoneTabletBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.3F)));
	public static final DeferredBlock<Block> NAKAGATOR_STATUE = REGISTER.register("nakagator_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5F), MSBlockShapes.NAKAGATOR_STATUE));
	
	
	//Redstone Blocks
	public static final DeferredBlock<Block> TRAJECTORY_BLOCK = REGISTER.register("trajectory_block", () -> new TrajectoryBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> STAT_STORER = REGISTER.register("stat_storer", () -> new StatStorerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> new RemoteObserverBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> new WirelessRedstoneTransmitterBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> new WirelessRedstoneReceiverBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).randomTicks()));
	public static final DeferredBlock<Block> SOLID_SWITCH = REGISTER.register("solid_switch", () -> new SolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(SolidSwitchBlock.POWERED) ? 15 : 0)));
	public static final DeferredBlock<Block> VARIABLE_SOLID_SWITCH = REGISTER.register("variable_solid_switch", () -> new VariableSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(VariableSolidSwitchBlock.POWER))));
	public static final DeferredBlock<Block> ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("one_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 20));
	public static final DeferredBlock<Block> TWO_SECOND_INTERVAL_TIMED_SOLID_SWITCH = REGISTER.register("two_second_interval_timed_solid_switch", () -> new TimedSolidSwitchBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL).lightLevel(state -> state.getValue(TimedSolidSwitchBlock.POWER)), 40));
	public static final DeferredBlock<Block> SUMMONER = REGISTER.register("summoner", () -> new SummonerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> AREA_EFFECT_BLOCK = REGISTER.register("area_effect_block", () -> new AreaEffectBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> new PlatformGeneratorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> PLATFORM_BLOCK = REGISTER.register("platform_block", () -> new PlatformBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).strength(0.2F).sound(SoundType.SCAFFOLDING).lightLevel(state -> 6).randomTicks().noOcclusion().isSuffocating(MSBlocks::never).isViewBlocking(MSBlocks::never)));
	public static final DeferredBlock<Block> PLATFORM_RECEPTACLE = REGISTER.register("platform_receptacle", () -> new PlatformReceptacleBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> ITEM_MAGNET = REGISTER.register("item_magnet", () -> new ItemMagnetBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL), new CustomVoxelShape(new double[]{0, 0, 0, 16, 1, 16}, new double[]{1, 1, 1, 15, 15, 15}, new double[]{0, 15, 0, 16, 16, 16})));
	public static final DeferredBlock<Block> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> new RedstoneClockBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> ROTATOR = REGISTER.register("rotator", () -> new RotatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> TOGGLER = REGISTER.register("toggler", () -> new TogglerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> new RemoteComparatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> STRUCTURE_CORE = REGISTER.register("structure_core", () -> new StructureCoreBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> FALL_PAD = REGISTER.register("fall_pad", () -> new FallPadBlock(Block.Properties.of().mapColor(MapColor.WOOL).requiresCorrectToolForDrops().strength(1).sound(SoundType.WOOL)));
	public static final DeferredBlock<Block> FRAGILE_STONE = REGISTER.register("fragile_stone", () -> new FragileBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final DeferredBlock<Block> SPIKES = REGISTER.register("spikes", () -> new SpikeBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(2).sound(SoundType.METAL), MSBlockShapes.SPIKES));
	public static final DeferredBlock<Block> RETRACTABLE_SPIKES = REGISTER.register("retractable_spikes", () -> new RetractableSpikesBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> BLOCK_PRESSURE_PLATE = REGISTER.register("block_pressure_plate", () -> new BlockPressurePlateBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.STONE)));
	public static final DeferredBlock<Block> PUSHABLE_BLOCK = REGISTER.register("pushable_block", () -> new PushableBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1).sound(SoundType.GILDED_BLACKSTONE), PushableBlock.Maneuverability.PUSH_AND_PULL));
	public static final DeferredBlock<Block> BLOCK_TELEPORTER = REGISTER.register("block_teleporter", () -> new BlockTeleporterBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL)));
	
	public static final DeferredBlock<Block> AND_GATE_BLOCK = REGISTER.register("and_gate_block", () -> new LogicGateBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1).sound(SoundType.METAL), LogicGateBlock.State.AND));
	public static final DeferredBlock<Block> OR_GATE_BLOCK = REGISTER.register("or_gate_block", () -> new LogicGateBlock(ofFullCopy(AND_GATE_BLOCK.get()), LogicGateBlock.State.OR));
	public static final DeferredBlock<Block> XOR_GATE_BLOCK = REGISTER.register("xor_gate_block", () -> new LogicGateBlock(ofFullCopy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XOR));
	public static final DeferredBlock<Block> NAND_GATE_BLOCK = REGISTER.register("nand_gate_block", () -> new LogicGateBlock(ofFullCopy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NAND));
	public static final DeferredBlock<Block> NOR_GATE_BLOCK = REGISTER.register("nor_gate_block", () -> new LogicGateBlock(ofFullCopy(AND_GATE_BLOCK.get()), LogicGateBlock.State.NOR));
	public static final DeferredBlock<Block> XNOR_GATE_BLOCK = REGISTER.register("xnor_gate_block", () -> new LogicGateBlock(ofFullCopy(AND_GATE_BLOCK.get()), LogicGateBlock.State.XNOR));
	
	
	//Core Functional Land Blocks
	public static final DeferredBlock<Block> GATE = REGISTER.register("gate", () -> new GateBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final DeferredBlock<Block> GATE_MAIN = REGISTER.register("gate_main", () -> new GateBlock.Main(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 25.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final DeferredBlock<Block> RETURN_NODE = REGISTER.register("return_node", () -> new ReturnNodeBlock(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	public static final DeferredBlock<Block> RETURN_NODE_MAIN = REGISTER.register("return_node_main", () -> new ReturnNodeBlock.Main(Block.Properties.of().pushReaction(PushReaction.BLOCK).noCollission().strength(-1.0F, 10.0F).sound(SoundType.GLASS).lightLevel(state -> 11).noLootTable()));
	
	
	//Misc Functional Land Blocks
	
	//Sburb Machines
	public static final DeferredBlock<Block> CRUXTRUDER_LID = REGISTER.register("cruxtruder_lid", () -> new CruxtruderLidBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(1.0F)));
	public static final CruxtruderMultiblock CRUXTRUDER = new CruxtruderMultiblock(REGISTER);
	public static final TotemLatheMultiblock TOTEM_LATHE = new TotemLatheMultiblock(REGISTER);
	public static final AlchemiterMultiblock ALCHEMITER = new AlchemiterMultiblock(REGISTER);
	public static final PunchDesignixMultiblock PUNCH_DESIGNIX = new PunchDesignixMultiblock(REGISTER);
	
	public static final DeferredBlock<Block> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_CRUXTRUDER.createRotatedShapes(), MSBlockEntityTypes.MINI_CRUXTRUDER, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final DeferredBlock<Block> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_TOTEM_LATHE.createRotatedShapes(), MSBlockEntityTypes.MINI_TOTEM_LATHE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final DeferredBlock<Block> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> new MiniAlchemiterBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final DeferredBlock<Block> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> new SmallMachineBlock<>(MSBlockShapes.SMALL_PUNCH_DESIGNIX.createRotatedShapes(), MSBlockEntityTypes.MINI_PUNCH_DESIGNIX, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	
	public static final DeferredBlock<Block> HOLOPAD = REGISTER.register("holopad", () -> new HolopadBlock(Block.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	public static final DeferredBlock<Block> INTELLIBEAM_LASERSTATION = REGISTER.register("intellibeam_laserstation", () -> new IntellibeamLaserstationBlock(Block.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(3.0F)));
	
	
	//Misc Machines
	public static final DeferredBlock<Block> COMPUTER = REGISTER.register("computer", () -> new ComputerBlock(ComputerBlock.COMPUTER_SHAPE, ComputerBlock.COMPUTER_SHAPE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> LAPTOP = REGISTER.register("laptop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> CROCKERTOP = REGISTER.register("crockertop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, MSComputerThemes.CROCKER, Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> HUBTOP = REGISTER.register("hubtop", () -> new ComputerBlock(ComputerBlock.LAPTOP_OPEN_SHAPE, ComputerBlock.LAPTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.COLOR_GREEN).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> LUNCHTOP = REGISTER.register("lunchtop", () -> new ComputerBlock(ComputerBlock.LUNCHTOP_OPEN_SHAPE, ComputerBlock.LUNCHTOP_CLOSED_SHAPE, Block.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> OLD_COMPUTER = REGISTER.register("old_computer", () -> new ComputerBlock(ComputerBlock.OLD_COMPUTER_SHAPE, ComputerBlock.OLD_COMPUTER_SHAPE, MSComputerThemes.SBURB_95, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(4.0F)));
	public static final DeferredBlock<Block> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> new TransportalizerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> TRANS_PORTALIZER = REGISTER.register("trans_portalizer", () -> new TransportalizerBlock(ofFullCopy(TRANSPORTALIZER.get())));
	public static final DeferredBlock<Block> SENDIFICATOR = REGISTER.register("sendificator", () -> new SendificatorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> GRIST_WIDGET = REGISTER.register("grist_widget", () -> new GristWidgetBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> new SmallMachineBlock<>(new CustomVoxelShape(new double[]{4, 0, 4, 12, 6, 12}).createRotatedShapes(), MSBlockEntityTypes.URANIUM_COOKER, Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.0F)));
	public static final DeferredBlock<Block> GRIST_COLLECTOR = REGISTER.register("grist_collector", () -> new GristCollectorBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> ANTHVIL = REGISTER.register("anthvil", () -> new AnthvilBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> SKAIANET_DENIER = REGISTER.register("skaianet_denier", () -> new Block(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(6).sound(SoundType.METAL)));
	public static final DeferredBlock<Block> POWER_HUB = REGISTER.register("power_hub", () -> new PowerHubBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(2).sound(SoundType.METAL)));
	
	
	//Misc Core Objects
	public static final DeferredBlock<Block> CRUXITE_DOWEL = REGISTER.register("cruxite_dowel", () -> new CruxiteDowelBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).pushReaction(PushReaction.DESTROY).strength(0.0F)));
	public static final DeferredBlock<Block> EMERGING_CRUXITE_DOWEL = REGISTER.register("emerging_cruxite_dowel", () -> new EmergingCruxiteDowelBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).strength(0.0F)));
	public static final LotusTimeCapsuleMultiblock LOTUS_TIME_CAPSULE_BLOCK = new LotusTimeCapsuleMultiblock(REGISTER);
	
	
	//Misc Alchemy Semi-Plants
	public static final DeferredBlock<Block> GOLD_SEEDS = REGISTER.register("gold_seeds", () -> new GoldSeedsBlock(Block.Properties.of().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).strength(0.1F).sound(SoundType.METAL).noCollission()));
	public static final DeferredBlock<Block> WOODEN_CACTUS = REGISTER.register("wooden_cactus", () -> new WoodenCactusBlock(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).randomTicks().strength(1.0F, 2.5F).sound(SoundType.WOOD)));
	
	
	//Cakes
	public static final DeferredBlock<Block> APPLE_CAKE = REGISTER.register("apple_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.5F, null));
	public static final DeferredBlock<Block> BLUE_CAKE = REGISTER.register("blue_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 0))));
	public static final DeferredBlock<Block> COLD_CAKE = REGISTER.register("cold_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
	}));
	public static final DeferredBlock<Block> RED_CAKE = REGISTER.register("red_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.1F, player -> player.heal(1)));
	public static final DeferredBlock<Block> HOT_CAKE = REGISTER.register("hot_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.1F, player -> player.setSecondsOnFire(4)));
	public static final DeferredBlock<Block> REVERSE_CAKE = REGISTER.register("reverse_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.1F, null));
	public static final DeferredBlock<Block> FUCHSIA_CAKE = REGISTER.register("fuchsia_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 3, 0.5F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 350, 1));
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
	}));
	public static final DeferredBlock<Block> NEGATIVE_CAKE = REGISTER.register("negative_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.3F, player -> {
		player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0));
		player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 250, 0));
	}));
	public static final DeferredBlock<Block> CARROT_CAKE = REGISTER.register("carrot_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.3F, player -> player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0))));
	public static final DeferredBlock<Block> LARGE_CAKE = REGISTER.register("large_cake", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.4F).sound(SoundType.WOOL)));
	public static final DeferredBlock<Block> PINK_FROSTED_TOP_LARGE_CAKE = REGISTER.register("pink_frosted_top_large_cake", () -> new Block(Block.Properties.of().mapColor(MapColor.COLOR_BROWN).strength(0.5F).sound(SoundType.WOOL)));
	public static final DeferredBlock<Block> CHOCOLATEY_CAKE = REGISTER.register("chocolatey_cake", () -> new SimpleCakeBlock(ofFullCopy(Blocks.CAKE), 2, 0.5F, null));
	
	
	//Explosives
	public static final DeferredBlock<Block> PRIMED_TNT = REGISTER.register("primed_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never), true, false, false));
	public static final DeferredBlock<Block> UNSTABLE_TNT = REGISTER.register("unstable_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never).randomTicks(), false, true, false));
	public static final DeferredBlock<Block> INSTANT_TNT = REGISTER.register("instant_tnt", () -> new SpecialTNTBlock(Block.Properties.of().mapColor(MapColor.FIRE).ignitedByLava().strength(0.0F).sound(SoundType.GRASS).isRedstoneConductor(MSBlocks::never), false, false, true));
	public static final DeferredBlock<ButtonBlock> WOODEN_EXPLOSIVE_BUTTON = REGISTER.register("wooden_explosive_button",
			() -> new SpecialButtonBlock(true, BlockSetType.OAK, 30, Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.WOOD)));
	public static final DeferredBlock<ButtonBlock> STONE_EXPLOSIVE_BUTTON = REGISTER.register("stone_explosive_button",
			() -> new SpecialButtonBlock(true, BlockSetType.STONE, 20, Block.Properties.of().pushReaction(PushReaction.DESTROY).noCollission().strength(0.5F).sound(SoundType.STONE)));
	
	//Misc Clutter
	public static final DeferredBlock<Block> BLENDER = REGISTER.register("blender", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.BLENDER));
	public static final DeferredBlock<Block> CHESSBOARD = REGISTER.register("chessboard", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.CHESSBOARD));
	public static final DeferredBlock<Block> MINI_FROG_STATUE = REGISTER.register("mini_frog_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.FROG_STATUE));
	public static final DeferredBlock<Block> MINI_WIZARD_STATUE = REGISTER.register("mini_wizard_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.WIZARD_STATUE));
	public static final DeferredBlock<Block> MINI_TYPHEUS_STATUE = REGISTER.register("mini_typheus_statue", () -> new CustomShapeBlock(Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.5F), MSBlockShapes.DENIZEN_STATUE));
	public static final DeferredBlock<CassettePlayerBlock> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> new CassettePlayerBlock(Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.METAL), MSBlockShapes.CASSETTE_PLAYER));
	public static final HorseClockMultiblock HORSE_CLOCK = new HorseClockMultiblock(REGISTER);
	public static final DeferredBlock<Block> GLOWYSTONE_DUST = REGISTER.register("glowystone_dust", () -> new GlowystoneWireBlock(Block.Properties.of().pushReaction(PushReaction.DESTROY).strength(0.0F).lightLevel(state -> 16).noCollission()));
	public static final DeferredBlock<Block> MIRROR = REGISTER.register("mirror", () -> new CustomShapeBlock(ofFullCopy(Blocks.OAK_PLANKS), MSBlockShapes.MIRROR));
	
	public static final DeferredBlock<LiquidBlock> OIL = REGISTER.register("oil", () -> new MSLiquidBlock(MSFluids.OIL, new Vec3(0.0, 0.0, 0.0), 0.80f, false, Block.Properties.of().mapColor(MapColor.COLOR_BLACK).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> BLOOD = REGISTER.register("blood", () -> new MSLiquidBlock(MSFluids.BLOOD, new Vec3(0.8, 0.0, 0.0), 0.35f, true, Block.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> BRAIN_JUICE = REGISTER.register("brain_juice", () -> new MSLiquidBlock(MSFluids.BRAIN_JUICE, new Vec3(0.55, 0.25, 0.7), 0.25f, true, Block.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> WATER_COLORS = REGISTER.register("water_colors", () -> new WaterColorsBlock(MSFluids.WATER_COLORS, 0.20f, Block.Properties.of().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> ENDER = REGISTER.register("ender", () -> new MSLiquidBlock(MSFluids.ENDER, new Vec3(0, 0.35, 0.35), (Float.MAX_VALUE), false, Block.Properties.of().mapColor(MapColor.COLOR_CYAN).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> LIGHT_WATER = REGISTER.register("light_water", () -> new MSLiquidBlock(MSFluids.LIGHT_WATER, new Vec3(0.2, 0.3, 1.0), 0.20f, true, Block.Properties.of().mapColor(MapColor.WATER).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).lightLevel(state -> 8).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> CAULK = REGISTER.register("caulk", () -> new MSLiquidBlock(MSFluids.CAULK, new Vec3(0.79, 0.74, 0.63), 0.80f, false, Block.Properties.of().mapColor(MapColor.COLOR_GRAY).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).noLootTable().sound(SoundType.EMPTY)));
	public static final DeferredBlock<LiquidBlock> MOLTEN_AMBER = REGISTER.register("molten_amber", () -> new MSLiquidBlock(MSFluids.MOLTEN_AMBER, new Vec3(2.21, 1.29, 0.0), 0.90f, false, Block.Properties.of().mapColor(MapColor.FIRE).replaceable().pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).lightLevel(state -> 15).noLootTable().sound(SoundType.EMPTY)));
	
	
	protected static Function<BlockState, MapColor> logColors(MapColor topColor, MapColor barkColor)
	{
		return state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor;
	}
	
	protected static Boolean leafSpawns(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type)
	{
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}
	
	protected static boolean never(BlockState state, BlockGetter level, BlockPos pos)
	{
		return false;
	}
	
	protected static Boolean never(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type)
	{
		return false;
	}
}
