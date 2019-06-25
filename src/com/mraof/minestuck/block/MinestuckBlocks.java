package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.multiblock.MultiblockAlchemiter;
import com.mraof.minestuck.block.multiblock.MultiblockCruxtruder;
import com.mraof.minestuck.block.multiblock.MultiblockPunchDesignix;
import com.mraof.minestuck.block.multiblock.MultiblockTotemLathe;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.lwjgl.openal.ALC;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckBlocks
{
	//Skaia
	public static Block BLACK_CHESS_DIRT, WHITE_CHESS_DIRT, DARK_GRAY_CHESS_DIRT, LIGHT_GRAY_CHESS_DIRT;
	public static Block SKAIA_PORTAL;
	
	//Ores
	public static Block CRUXITE_ORE_STONE, CRUXITE_ORE_NETHERRACK, CRUXITE_ORE_COBBLESTONE, CRUXITE_ORE_SANDSTONE;
	public static Block CRUXITE_ORE_RED_SANDSTONE, CRUXITE_ORE_END_STONE, CRUXITE_ORE_PINK_STONE;
	public static Block URANIUM_ORE_STONE, URANIUM_ORE_NETHERRACK, URANIUM_ORE_COBBLESTONE, URANIUM_ORE_SANDSTONE;
	public static Block URANIUM_ORE_RED_SANDSTONE, URANIUM_ORE_END_STONE, URANIUM_ORE_PINK_STONE;
	public static Block COAL_ORE_NETHERRACK, COAL_ORE_PINK_STONE;
	public static Block IRON_ORE_END_STONE, IRON_ORE_SANDSTONE, IRON_ORE_SANDSTONE_RED;
	public static Block GOLD_ORE_SANDSTONE, GOLD_ORE_SANDSTONE_RED, GOLD_ORE_PINK_STONE;
	public static Block REDSTONE_ORE_END_STONE;
	public static Block QUARTZ_ORE_STONE;
	public static Block LAPIS_ORE_PINK_STONE;
	public static Block DIAMOND_ORE_PINK_STONE;
	
	//Resource Blocks
	public static Block CRUXITE_BLOCK;
	public static Block URANIUM_BLOCK;
	public static Block GENERIC_OBJECT;
	
	//Land Environment Blocks
	public static Block BLUE_DIRT, THOUGHT_DIRT;
	public static Block COARSE_STONE, CHISELED_COARSE_STONE;
	public static Block SHADE_BRICKS, SMOOTH_SHADE_STONE;
	public static Block FROST_BRICKS, FROST_TILE, CHISELED_FROST_BRICKS;
	public static Block CAST_IRON, CHISELED_CAST_IRON;
	public static Block MYCELIUM_BRICKS;
	public static Block BLACK_STONE;
	public static Block FLOWERY_MOSS_STONE, FLOWERY_MOSS_BRICKS;
	public static Block COARSE_END_STONE, END_GRASS;
	public static Block CHALK, CHALK_BRICKS, CHISELED_CHALK_BRICKS, POLISHED_CHALK;
	public static Block PINK_STONE, PINK_STONE_BRICKS, CHISELED_PINK_STONE_BRICKS, CRACKED_PINK_STONE_BRICKS, MOSSY_PINK_STONE_BRICKS, POLISHED_PINK_STONE;
	public static Block DENSE_CLOUD, BRIGHT_DENSE_CLOUD;
	public static Block SUGAR_CUBE;
	
	//Land Tree Blocks
	public static Block GLOWING_LOG, FROST_LOG, RAINBOW_LOG, END_LOG;
	public static Block VINE_LOG, FLOWERY_VINE_LOG, DEAD_LOG, PETRIFIED_LOG;
	public static Block GLOWING_WOOD, FROST_WOOD, RAINBOW_WOOD, END_WOOD;
	public static Block VINE_WOOD, FLOWERY_VINE_WOOD, DEAD_WOOD, PETRIFIED_WOOD;
	public static Block GLOWING_PLANKS, FROST_PLANKS, RAINBOW_PLANKS, END_PLANKS;
	public static Block DEAD_PLANKS, TREATED_PLANKS;
	public static Block FROST_LEAVES, RAINBOW_LEAVES, END_LEAVES;
	public static Block RAINBOW_SAPLING, END_SAPLING;
	
	//Aspect Tree Blocks
	public static Block BLOOD_ASPECT_LOG, BREATH_ASPECT_LOG, DOOM_ASPECT_LOG, HEART_ASPECT_LOG, HOPE_ASPECT_LOG, LIFE_ASPECT_LOG;
	public static Block LIGHT_ASPECT_LOG, MIND_ASPECT_LOG, RAGE_ASPECT_LOG, SPACE_ASPECT_LOG, TIME_ASPECT_LOG, VOID_ASPECT_LOG;
	public static Block BLOOD_ASPECT_PLANKS, BREATH_ASPECT_PLANKS, DOOM_ASPECT_PLANKS, HEART_ASPECT_PLANKS, HOPE_ASPECT_PLANKS, LIFE_ASPECT_PLANKS;
	public static Block LIGHT_ASPECT_PLANKS, MIND_ASPECT_PLANKS, RAGE_ASPECT_PLANKS, SPACE_ASPECT_PLANKS, TIME_ASPECT_PLANKS, VOID_ASPECT_PLANKS;
	public static Block BLOOD_ASPECT_LEAVES, BREATH_ASPECT_LEAVES, DOOM_ASPECT_LEAVES, HEART_ASPECT_LEAVES, HOPE_ASPECT_LEAVES, LIFE_ASPECT_LEAVES;
	public static Block LIGHT_ASPECT_LEAVES, MIND_ASPECT_LEAVES, RAGE_ASPECT_LEAVES, SPACE_ASPECT_LEAVES, TIME_ASPECT_LEAVES, VOID_ASPECT_LEAVES;
	public static Block BLOOD_ASPECT_SAPLING, BREATH_ASPECT_SAPLING, DOOM_ASPECT_SAPLING, HEART_ASPECT_SAPLING, HOPE_ASPECT_SAPLING, LIFE_ASPECT_SAPLING;
	public static Block LIGHT_ASPECT_SAPLING, MIND_ASPECT_SAPLING, RAGE_ASPECT_SAPLING, SPACE_ASPECT_SAPLING, TIME_ASPECT_SAPLING, VOID_ASPECT_SAPLING;
	
	//Land Plant Blocks
	public static Block GLOWING_MUSHROOM;
	public static Block DESERT_BUSH;
	public static Block BLOOMING_CACTUS;
	public static Block PETRIFIED_GRASS;
	public static Block PETRIFIED_POPPY;
	public static Block STRAWBERRY, ATTACHED_STRAWBERRY_STEM, STRAWBERRY_STEM;
	
	//Special Land Blocks
	public static Block LAYERED_SAND, LAYERED_RED_SAND;
	public static Block GLOWY_GOOP;
	public static Block COAGULATED_BLOOD;
	public static Block VEIN;
	public static Block VEIN_CORNER;
	public static Block VEIN_CORNER_INVERTED;
	
	//Structure Land Blocks
	public static Block COARSE_STONE_STAIRS, SHADE_BRICK_STAIRS, FROST_BRICK_STAIRS, CAST_IRON_STAIRS, MYCELIUM_BRICK_STAIRS;
	public static Block CHALK_STAIRS, CHALK_BRICK_STAIRS, PINK_STONE_BRICK_STAIRS;
	public static Block RAINBOW_PLANKS_STAIRS, END_PLANKS_STAIRS, DEAD_PLANKS_STAIRS, TREATED_PLANKS_STAIRS;
	public static Block CHALK_SLAB, CHALK_BRICK_SLAB, PINK_STONE_BRICK_SLAB;
	public static Block RAINBOW_PLANKS_SLAB, END_PLANKS_SLAB, DEAD_PLANKS_SLAB, TREATED_PLANKS_SLAB;
	
	//Core Functional Land Blocks
	public static Block GATE;
	public static Block RETURN_NODE;
	
	//Misc Functional Land Blocks
	public static Block RABBIT_SPAWNER;
	
	//Sburb Machines
	public static MultiblockCruxtruder CRUXTRUDER;
	public static Block CRUXTRUDER_LID;
	public static MultiblockTotemLathe TOTEM_LATHE;
	public static MultiblockAlchemiter ALCHEMITER;
	public static MultiblockPunchDesignix PUNCH_DESIGNIX;
	public static Block MINI_CRUXTRUDER;
	public static Block MINI_TOTEM_LATHE;
	public static Block MINI_ALCHEMITER;
	public static Block MINI_PUNCH_DESIGNIX;
	public static Block HOLOPAD;
	/*public static BlockJumperBlock[] jumperBlockExtension = BlockJumperBlock.createBlocks();
	public static BlockAlchemiterUpgrades[] alchemiterUpgrades = BlockAlchemiterUpgrades.createBlocks();*/
	
	//Misc Machines
	public static Block COMPUTER_ON, COMPUTER_OFF;
	public static Block LAPTOP_ON, LAPTOP_OFF;
	public static Block CROCKERTOP_ON, CROCKERTOP_OFF;
	public static Block HUBTOP_ON, HUBTOP_OFF;
	public static Block LUNCHTOP_ON, LUNCHTOP_OFF;
	public static Block TRANSPORTALIZER;
	public static Block GRIST_WIDGET;
	public static Block URANIUM_COOKER;
	
	//Misc Core Objects
	public static Block CRUXITE_DOWEL;
	
	//Misc Alchemy Semi-Plants
	public static Block GOLD_SEEDS;
	public static Block WOODEN_CACTUS;
	
	//Cakes
	public static Block APPLE_CAKE;
	public static Block BLUE_CAKE;
	public static Block COLD_CAKE;
	public static Block RED_CAKE;
	public static Block HOT_CAKE;
	public static Block REVERSE_CAKE;
	public static Block FUCHSIA_CAKE;
	
	//Explosion and Redstone
	public static Block PRIMED_TNT;
	public static Block UNSTABLE_TNT;
	public static Block INSTANT_TNT;
	public static Block WOODEN_EXPLOSIVE_BUTTON;
	public static Block STONE_EXPLOSIVE_BUTTON;
	
	//Misc Alchemy Objects
	public static Block BLENDER;
	public static Block CHESSBOARD;
	public static Block MINI_FROG_STATUE;
	public static Block GLOWYSTONE_WIRE;
	
	//TODO Look more into fluids
	/*public static Fluid fluidOil = createFluid("oil", new ResourceLocation("minestuck", "blocks/oil_still"), new ResourceLocation("minestuck", "blocks/oil_flowing"), "tile.oil");
	public static Fluid fluidBlood = createFluid("blood", new ResourceLocation("minestuck", "blocks/blood_still"), new ResourceLocation("minestuck", "blocks/blood_flowing"), "tile.blood");
	public static Fluid fluidBrainJuice = createFluid("brain_juice", new ResourceLocation("minestuck", "blocks/brain_juice_still"), new ResourceLocation("minestuck", "blocks/brain_juice_flowing"), "tile.brainJuice");
	public static Fluid fluidWatercolors = createFluid("watercolors", new ResourceLocation("minestuck", "blocks/watercolors_still"), new ResourceLocation("minestuck", "blocks/watercolors_flowing"), "tile.watercolors");
	public static Fluid fluidEnder = createFluid("ender", new ResourceLocation("minestuck", "blocks/ender_still"), new ResourceLocation("minestuck", "blocks/ender_flowing"), "tile.ender");
	public static Fluid fluidLightWater = createFluid("light_water", new ResourceLocation("minestuck", "blocks/light_water_still"), new ResourceLocation("minestuck", "blocks/light_water_flowing"), "tile.lightWater");
	
	public static Block blockOil = new BlockFluidClassic(fluidOil, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.0, 0.0, 0.0);
		}
	}.setUnlocalizedName("oil").setLightOpacity(2);
	
	public static Block blockBlood = new BlockFluidClassic(fluidBlood, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.8, 0.0, 0.0);
		}
	}.setUnlocalizedName("blood").setLightOpacity(1);
	
	public static Block blockBrainJuice = new BlockFluidClassic(fluidBrainJuice, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.55, 0.25, 0.7);
		}
	}.setUnlocalizedName("brainJuice").setLightOpacity(1);
	
	public static Block blockWatercolors = new BlockFluidClassic(fluidWatercolors, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			Vec3d newColor = new Vec3d(0.0, 20.0, 30.0);
			newColor = newColor.rotateYaw((float) (entity.posX / 2.0));
			newColor = newColor.rotatePitch((float) (entity.posZ / 2.0));
			newColor = newColor.rotateYaw((float) (entity.posY));
			newColor = newColor.normalize();
			newColor = new Vec3d(newColor.x % 1.0, newColor.y % 1.0, newColor.z % 1.0);
			
			return newColor;
		}
	}.setUnlocalizedName("watercolors").setLightOpacity(1);
	
	public static Block blockEnder = new BlockFluidEnder(fluidEnder, Material.WATER).setUnlocalizedName("ender").setLightOpacity(1);
	
	public static Block blockLightWater = new BlockFluidClassic(fluidLightWater, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.2, 0.3, 1.0);
		}
	}.setUnlocalizedName("lightWater").setLightOpacity(1);
	
	public static Block[] liquidGrists;
	public static Fluid[] gristFluids;*/
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.register(BLACK_CHESS_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.BLACK).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("black_chess_dirt"));
		registry.register(WHITE_CHESS_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.SNOW).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("white_chess_dirt"));
		registry.register(DARK_GRAY_CHESS_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.GRAY).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("dark_gray_chess_dirt"));
		registry.register(LIGHT_GRAY_CHESS_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("light_gray_chess_dirt"));
		registry.register(SKAIA_PORTAL = new BlockSkaiaPortal(Block.Properties.create(Material.PORTAL, MaterialColor.CYAN).doesNotBlockMovement().lightValue(11).hardnessAndResistance(-1.0F, 3600000.0F)).setRegistryName("skaia_portal"));
		
		registry.register(CRUXITE_ORE_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_stone"));
		registry.register(CRUXITE_ORE_NETHERRACK = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_netherrack"));
		registry.register(CRUXITE_ORE_COBBLESTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_cobblestone"));
		registry.register(CRUXITE_ORE_SANDSTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_sandstone"));
		registry.register(CRUXITE_ORE_RED_SANDSTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_red_sandstone"));
		registry.register(CRUXITE_ORE_END_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_end_stone"));
		registry.register(CRUXITE_ORE_PINK_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_pink_stone"));
		registry.register(URANIUM_ORE_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_stone"));
		registry.register(URANIUM_ORE_NETHERRACK = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_netherrack"));
		registry.register(URANIUM_ORE_COBBLESTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_cobblestone"));
		registry.register(URANIUM_ORE_SANDSTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_sandstone"));
		registry.register(URANIUM_ORE_RED_SANDSTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_red_sandstone"));
		registry.register(URANIUM_ORE_END_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_end_stone"));
		registry.register(URANIUM_ORE_PINK_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(3)).setRegistryName("uranium_ore_pink_stone"));
		registry.register(COAL_ORE_NETHERRACK = new BlockVanillaOre(BlockVanillaOre.OreType.COAL, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("coal_ore_netherrack"));
		registry.register(COAL_ORE_PINK_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.COAL, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("coal_ore_pink_stone"));
		registry.register(IRON_ORE_END_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.IRON, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("iron_ore_end_stone"));
		registry.register(IRON_ORE_SANDSTONE = new BlockVanillaOre(BlockVanillaOre.OreType.IRON, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("iron_ore_sandstone"));
		registry.register(IRON_ORE_SANDSTONE_RED = new BlockVanillaOre(BlockVanillaOre.OreType.IRON, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("iron_ore_sandstone_red"));
		registry.register(GOLD_ORE_SANDSTONE = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("gold_ore_sandstone"));
		registry.register(GOLD_ORE_SANDSTONE_RED = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("gold_ore_sandstone_red"));
		registry.register(GOLD_ORE_PINK_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("gold_ore_pink_stone"));
		registry.register(REDSTONE_ORE_END_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.REDSTONE, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("redstone_ore_end_stone"));
		registry.register(QUARTZ_ORE_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.QUARTZ, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("quartz_ore_stone"));
		registry.register(LAPIS_ORE_PINK_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.LAPIS, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("lapis_ore_pink_stone"));
		registry.register(DIAMOND_ORE_PINK_STONE = new BlockVanillaOre(BlockVanillaOre.OreType.DIAMOND, Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("diamond_ore_pink_stone"));
		
		registry.register(CRUXITE_BLOCK = new Block(Block.Properties.create(Material.ROCK, EnumDyeColor.LIGHT_BLUE).hardnessAndResistance(3.0F)).setRegistryName("cruxite_block"));
		registry.register(URANIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK, EnumDyeColor.LIME).hardnessAndResistance(3.0F).lightValue(7)).setRegistryName("uranium_block"));
		registry.register(GENERIC_OBJECT = new Block(Block.Properties.create(Material.GOURD, EnumDyeColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD)).setRegistryName("generic_object"));
		
		registry.register(BLUE_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.BLUE).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("blue_dirt"));
		registry.register(THOUGHT_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.LIME).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("thought_dirt"));
		registry.register(COARSE_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryName("coarse_stone"));
		registry.register(CHISELED_COARSE_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryName("chiseled_coarse_stone"));
		registry.register(SHADE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("shade_bricks"));
		registry.register(SMOOTH_SHADE_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("smooth_shade_stone"));
		registry.register(FROST_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("frost_bricks"));
		registry.register(FROST_TILE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("frost_tile"));
		registry.register(CHISELED_FROST_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chiseled_frost_bricks"));
		registry.register(CAST_IRON = new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("cast_iron"));
		registry.register(CHISELED_CAST_IRON = new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("chiseled_cast_iron"));
		registry.register(MYCELIUM_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.MAGENTA).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("mycelium_bricks"));
		registry.register(BLACK_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2.5F, 6.0F)).setRegistryName("black_stone"));
		registry.register(FLOWERY_MOSS_STONE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("flowery_moss_stone"));
		registry.register(FLOWERY_MOSS_BRICKS = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("flowery_moss_bricks"));
		registry.register(COARSE_END_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("coarse_end_stone"));
		registry.register(END_GRASS = new BlockEndGrass(Block.Properties.create(Material.ROCK, MaterialColor.PURPLE).hardnessAndResistance(3.0F, 9.0F).needsRandomTick()).setRegistryName("end_grass"));
		registry.register(CHALK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chalk"));
		registry.register(CHALK_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chalk_bricks"));
		registry.register(CHISELED_CHALK_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chiseled_chalk_bricks"));
		registry.register(POLISHED_CHALK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("polished_chalk"));
		registry.register(PINK_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_stone"));
		registry.register(PINK_STONE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_stone_bricks"));
		registry.register(CHISELED_PINK_STONE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chiseled_pink_stone_bricks"));
		registry.register(CRACKED_PINK_STONE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("cracked_pink_stone_bricks"));
		registry.register(MOSSY_PINK_STONE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("mossy_pink_stone_bricks"));
		registry.register(POLISHED_PINK_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("polished_pink_stone"));
		registry.register(DENSE_CLOUD = new Block(Block.Properties.create(Material.GLASS, MaterialColor.YELLOW).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud"));
		registry.register(BRIGHT_DENSE_CLOUD = new Block(Block.Properties.create(Material.GLASS, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("bright_dense_cloud"));
		registry.register(SUGAR_CUBE = new Block(Block.Properties.create(Material.SAND, MaterialColor.SNOW).hardnessAndResistance(0.4F).sound(SoundType.SAND)).setRegistryName("sugar_cube"));
		
		registry.register(GLOWING_LOG = new BlockMinestuckLog(MaterialColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F).lightValue(11).sound(SoundType.WOOD)).setRegistryName("glowing_log"));
		registry.register(FROST_LOG = new BlockMinestuckLog(MaterialColor.ICE, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("frost_log"));
		registry.register(RAINBOW_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_log"));
		registry.register(END_LOG = new BlockDoubleLog(MaterialColor.SAND, 1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("end_log"));
		registry.register(VINE_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("vine_log"));
		registry.register(FLOWERY_VINE_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("flowery_vine_log"));
		registry.register(DEAD_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("dead_log"));
		registry.register(PETRIFIED_LOG = new BlockMinestuckLog(MaterialColor.WOOD, 0, 0, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.STONE)).setRegistryName("petrified_log"));
		registry.register(GLOWING_WOOD = new BlockMinestuckLog(MaterialColor.LIGHT_BLUE, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F).lightValue(11).sound(SoundType.WOOD)).setRegistryName("glowing_wood"));
		registry.register(FROST_WOOD = new BlockMinestuckLog(MaterialColor.ICE, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("frost_wood"));
		registry.register(RAINBOW_WOOD = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_wood"));
		registry.register(END_WOOD = new BlockMinestuckLog(MaterialColor.SAND, 1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("end_wood"));
		registry.register(VINE_WOOD = new BlockMinestuckLog(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("vine_wood"));
		registry.register(FLOWERY_VINE_WOOD = new BlockMinestuckLog(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("flowery_vine_wood"));
		registry.register(DEAD_WOOD = new BlockMinestuckLog(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("dead_wood"));
		registry.register(PETRIFIED_WOOD = new BlockMinestuckLog(MaterialColor.OBSIDIAN, 0, 0, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.STONE)).setRegistryName("petrified_wood"));
		registry.register(GLOWING_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F, 3.0F).lightValue(7).sound(SoundType.WOOD)).setRegistryName("glowing_planks"));
		registry.register(FROST_PLANKS = new BlockCustom(5, 5, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("frost_planks"));
		registry.register(RAINBOW_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rainbow_planks"));
		registry.register(END_PLANKS = new BlockCustom(1, 250, Block.Properties.create(Material.WOOD, MaterialColor.SAND).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("end_planks"));
		registry.register(DEAD_PLANKS = new BlockCustom(5, 5, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("dead_planks"));
		registry.register(TREATED_PLANKS = new BlockCustom(1, 0, Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("treated_planks"));
		registry.register(FROST_LEAVES = new BlockMinestuckLeaves(null, Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("frost_leaves"));
		registry.register(RAINBOW_LEAVES = new BlockMinestuckLeaves(() -> RAINBOW_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("rainbow_leaves"));
		registry.register(END_LEAVES = new BlockEndLeaves(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("end_leaves"));
		registry.register(RAINBOW_SAPLING = new BlockRainbowSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("rainbow_sapling"));
		registry.register(END_SAPLING = new BlockEndSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("end_sapling"));
		
		registry.register(BLOOD_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("blood_aspect_log"));
		registry.register(BREATH_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("breath_aspect_log"));
		registry.register(DOOM_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("doom_aspect_log"));
		registry.register(HEART_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("heart_aspect_log"));
		registry.register(HOPE_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("hope_aspect_log"));
		registry.register(LIFE_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("life_aspect_log"));
		registry.register(LIGHT_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("light_aspect_log"));
		registry.register(MIND_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("mind_aspect_log"));
		registry.register(RAGE_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("rage_aspect_log"));
		registry.register(SPACE_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("space_aspect_log"));
		registry.register(TIME_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("time_aspect_log"));
		registry.register(VOID_ASPECT_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("void_aspect_log"));
		registry.register(BLOOD_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("blood_aspect_planks"));
		registry.register(BREATH_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("breath_aspect_planks"));
		registry.register(DOOM_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("doom_aspect_planks"));
		registry.register(HEART_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("heart_aspect_planks"));
		registry.register(HOPE_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("hope_aspect_planks"));
		registry.register(LIFE_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("life_aspect_planks"));
		registry.register(LIGHT_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("light_aspect_planks"));
		registry.register(MIND_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("mind_aspect_planks"));
		registry.register(RAGE_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("rage_aspect_planks"));
		registry.register(SPACE_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("space_aspect_planks"));
		registry.register(TIME_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("time_aspect_planks"));
		registry.register(VOID_ASPECT_PLANKS = new BlockCustom(5, 20, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("void_aspect_planks"));
		registry.register(BLOOD_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> BLOOD_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("blood_aspect_leaves"));
		registry.register(BREATH_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> BREATH_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("breath_aspect_leaves"));
		registry.register(DOOM_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> DOOM_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("doom_aspect_leaves"));
		registry.register(HEART_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> HEART_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("heart_aspect_leaves"));
		registry.register(HOPE_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> HOPE_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("hope_aspect_leaves"));
		registry.register(LIFE_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> LIFE_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("life_aspect_leaves"));
		registry.register(LIGHT_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> LIGHT_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("light_aspect_leaves"));
		registry.register(MIND_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> MIND_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("mind_aspect_leaves"));
		registry.register(RAGE_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> RAGE_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("rage_aspect_leaves"));
		registry.register(SPACE_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> SPACE_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("space_aspect_leaves"));
		registry.register(TIME_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> TIME_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("time_aspect_leaves"));
		registry.register(VOID_ASPECT_LEAVES = new BlockMinestuckLeaves(() -> VOID_ASPECT_SAPLING.asItem(), Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).needsRandomTick().sound(SoundType.PLANT)).setRegistryName("void_aspect_leaves"));
		registry.register(BLOOD_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("blood_aspect_sapling"));
		registry.register(BREATH_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("breath_aspect_sapling"));
		registry.register(DOOM_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("doom_aspect_sapling"));
		registry.register(HEART_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("heart_aspect_sapling"));
		registry.register(HOPE_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("hope_aspect_sapling"));
		registry.register(LIFE_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("life_aspect_sapling"));
		registry.register(LIGHT_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("light_aspect_sapling"));
		registry.register(MIND_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("mind_aspect_sapling"));
		registry.register(RAGE_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("rage_aspect_sapling"));
		registry.register(SPACE_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("space_aspect_sapling"));
		registry.register(TIME_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("time_aspect_sapling"));
		registry.register(VOID_ASPECT_SAPLING = new BlockAspectSapling(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("void_aspect_sapling"));
		
		registry.register(GLOWING_MUSHROOM = new BlockGlowingMushroom(Block.Properties.create(Material.PLANTS, MaterialColor.DIAMOND).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.PLANT).lightValue(11)).setRegistryName("glowing_mushroom"));
		registry.register(DESERT_BUSH = new BlockDesertBush(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("desert_bush"));
		registry.register(BLOOMING_CACTUS = new BlockDesertFlora(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)).setRegistryName("blooming_cactus"));
		registry.register(PETRIFIED_GRASS = new BlockPetrifiedFlora(Block.Properties.create(Material.ROCK, EnumDyeColor.GRAY).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.STONE)).setRegistryName("petrified_grass"));
		registry.register(PETRIFIED_POPPY = new BlockPetrifiedFlora(Block.Properties.create(Material.ROCK, EnumDyeColor.GRAY).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.STONE)).setRegistryName("petrified_poppy"));
		registry.register(STRAWBERRY = new BlockStrawberry(Block.Properties.create(Material.GOURD, MaterialColor.RED).hardnessAndResistance(1.0F).sound(SoundType.WOOD)).setRegistryName("strawberry"));
		registry.register(ATTACHED_STRAWBERRY_STEM = new BlockStrawberry.AttachedStem((BlockStemGrown) STRAWBERRY, Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("attached_strawberry_stem"));
		registry.register(STRAWBERRY_STEM = new BlockStrawberry.Stem((BlockStemGrown) STRAWBERRY, Block.Properties.create(Material.PLANTS).doesNotBlockMovement().needsRandomTick().hardnessAndResistance(0).sound(SoundType.WOOD)).setRegistryName("strawberry_stem"));
		
		registry.register(LAYERED_SAND = new BlockLayered(Blocks.SAND, Block.Properties.from(Blocks.SAND)).setRegistryName("layered_sand"));
		registry.register(LAYERED_RED_SAND = new BlockLayered(Blocks.RED_SAND, Block.Properties.from(Blocks.RED_SAND)).setRegistryName("layered_red_sand"));
		registry.register(GLOWY_GOOP = new BlockGoop(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME).lightValue(14)).setRegistryName("glowy_goop"));
		registry.register(COAGULATED_BLOOD = new BlockGoop(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME)).setRegistryName("coagulated_blood"));
		registry.register(VEIN = new BlockVein(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("vein"));
		registry.register(VEIN_CORNER = new BlockVeinCorner(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("vein_corner"));
		registry.register(VEIN_CORNER_INVERTED = new BlockVeinCorner(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.45F).sound(SoundType.SLIME)).setRegistryName("vein_corner_inverted"));
		
		registry.register(COARSE_STONE_STAIRS = new BlockMinestuckStairs(COARSE_STONE.getDefaultState(), Block.Properties.from(COARSE_STONE)).setRegistryName("coarse_stone_stairs"));
		registry.register(SHADE_BRICK_STAIRS = new BlockMinestuckStairs(SHADE_BRICKS.getDefaultState(), Block.Properties.from(SHADE_BRICKS)).setRegistryName("shade_brick_stairs"));
		registry.register(FROST_BRICK_STAIRS = new BlockMinestuckStairs(FROST_BRICKS.getDefaultState(), Block.Properties.from(FROST_BRICKS)).setRegistryName("frost_brick_stairs"));
		registry.register(CAST_IRON_STAIRS = new BlockMinestuckStairs(CAST_IRON.getDefaultState(), Block.Properties.from(CAST_IRON)).setRegistryName("cast_iron_stairs"));
		registry.register(MYCELIUM_BRICK_STAIRS = new BlockMinestuckStairs(MYCELIUM_BRICKS.getDefaultState(), Block.Properties.from(MYCELIUM_BRICKS)).setRegistryName("mycelium_brick_stairs"));
		registry.register(CHALK_STAIRS = new BlockMinestuckStairs(CHALK.getDefaultState(), Block.Properties.from(CHALK)).setRegistryName("chalk_stairs"));
		registry.register(CHALK_BRICK_STAIRS = new BlockMinestuckStairs(CHALK_BRICKS.getDefaultState(), Block.Properties.from(CHALK_BRICKS)).setRegistryName("chalk_brick_stairs"));
		registry.register(PINK_STONE_BRICK_STAIRS = new BlockMinestuckStairs(PINK_STONE_BRICKS.getDefaultState(), Block.Properties.from(PINK_STONE_BRICKS)).setRegistryName("pink_stone_brick_stairs"));
		registry.register(RAINBOW_PLANKS_STAIRS = new BlockMinestuckStairs(RAINBOW_PLANKS.getDefaultState(), Block.Properties.from(RAINBOW_PLANKS)).setRegistryName("rainbow_planks_stairs"));
		registry.register(END_PLANKS_STAIRS = new BlockMinestuckStairs(END_PLANKS.getDefaultState(), Block.Properties.from(END_PLANKS)).setRegistryName("end_planks_stairs"));
		registry.register(DEAD_PLANKS_STAIRS = new BlockMinestuckStairs(DEAD_PLANKS.getDefaultState(), Block.Properties.from(DEAD_PLANKS)).setRegistryName("dead_planks_stairs"));
		registry.register(TREATED_PLANKS_STAIRS = new BlockMinestuckStairs(TREATED_PLANKS.getDefaultState(), Block.Properties.from(TREATED_PLANKS)).setRegistryName("treated_planks_stairs"));
		registry.register(CHALK_SLAB = new BlockSlab(Block.Properties.from(CHALK)).setRegistryName("chalk_slab"));
		registry.register(CHALK_BRICK_SLAB = new BlockSlab(Block.Properties.from(CHALK_BRICKS)).setRegistryName("chalk_brick_slab"));
		registry.register(PINK_STONE_BRICK_SLAB = new BlockSlab(Block.Properties.from(PINK_STONE_BRICKS)).setRegistryName("pink_stone_brick_slab"));
		registry.register(RAINBOW_PLANKS_SLAB = new BlockSlab(Block.Properties.from(RAINBOW_PLANKS)).setRegistryName("rainbow_planks_slab"));
		registry.register(END_PLANKS_SLAB = new BlockSlab(Block.Properties.from(END_PLANKS)).setRegistryName("end_planks_slab"));
		registry.register(DEAD_PLANKS_SLAB = new BlockSlab(Block.Properties.from(DEAD_PLANKS)).setRegistryName("dead_planks_slab"));
		registry.register(TREATED_PLANKS_SLAB = new BlockSlab(Block.Properties.from(TREATED_PLANKS)).setRegistryName("treated_planks_slab"));
		
		registry.register(GATE = new BlockGate(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F, 25.0F).sound(SoundType.GLASS).lightValue(11)).setRegistryName("gate"));
		registry.register(RETURN_NODE = new BlockReturnNode(Block.Properties.create(Material.PORTAL).doesNotBlockMovement().hardnessAndResistance(-1.0F, 10.0F).sound(SoundType.GLASS).lightValue(11)).setRegistryName("return_node"));
		
		registry.register(RABBIT_SPAWNER = new BlockMobSpawner(Block.Properties.create(Material.AIR).needsRandomTick().doesNotBlockMovement()).setRegistryName("rabbit_spawner"));
		
		CRUXTRUDER = new MultiblockCruxtruder();
		CRUXTRUDER.registerBlocks(registry);
		registry.register(CRUXTRUDER_LID = new BlockCruxtruderLid(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F)).setRegistryName("cruxtruder_lid"));
		TOTEM_LATHE = new MultiblockTotemLathe();
		TOTEM_LATHE.registerBlocks(registry);
		ALCHEMITER = new MultiblockAlchemiter();
		ALCHEMITER.registerBlocks(registry);
		PUNCH_DESIGNIX = new MultiblockPunchDesignix();
		PUNCH_DESIGNIX.registerBlocks(registry);
		registry.register(MINI_CRUXTRUDER = new BlockMiniCruxtruder(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_cruxtruder"));
		registry.register(MINI_TOTEM_LATHE = new BlockMiniTotemLathe(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_totem_lathe"));
		registry.register(MINI_ALCHEMITER = new BlockMiniAlchemiter(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_alchemiter"));
		registry.register(MINI_PUNCH_DESIGNIX = new BlockMiniPunchDesignix(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("mini_punch_designix"));
		registry.register(HOLOPAD = new BlockHolopad(Block.Properties.create(Material.IRON, MaterialColor.SNOW).hardnessAndResistance(3.0F)).setRegistryName("holopad"));
		
		registry.register(jumperBlockExtension[0].setRegistryName("jumper_block_extension"));
		registry.register(jumperBlockExtension[1].setRegistryName("jumper_block_extension2"));
		registry.register(jumperBlockExtension[2].setRegistryName("jumper_block_extension3"));
		registry.register(jumperBlockExtension[3].setRegistryName("jumper_block_extension4"));
		
		registry.register(alchemiterUpgrades[0].setRegistryName("alchemiter_upgrade"));
		registry.register(alchemiterUpgrades[1].setRegistryName("alchemiter_upgrade2"));
		registry.register(alchemiterUpgrades[2].setRegistryName("alchemiter_upgrade3"));
		registry.register(alchemiterUpgrades[3].setRegistryName("alchemiter_upgrade4"));
		
		registry.register(COMPUTER_ON = new BlockComputerOn(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F), BlockComputerOff.COMPUTER_SHAPE, BlockComputerOff.COMPUTER_COLLISION_SHAPE, () -> COMPUTER_OFF.asItem()).setRegistryName("computer_on"));
		registry.register(COMPUTER_OFF = new BlockComputerOff(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F), COMPUTER_ON, BlockComputerOff.COMPUTER_SHAPE, BlockComputerOff.COMPUTER_COLLISION_SHAPE).setRegistryName("computer_off"));
		registry.register(LAPTOP_ON = new BlockComputerOn(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F), BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE, () -> LAPTOP_OFF.asItem()).setRegistryName("laptop_on"));
		registry.register(LAPTOP_OFF = new BlockComputerOff(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F), LAPTOP_ON, BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE).setRegistryName("laptop_off"));
		registry.register(CROCKERTOP_ON = new BlockComputerOn(Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F), BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE, () -> CROCKERTOP_OFF.asItem()).setRegistryName("crockertop_on"));
		registry.register(CROCKERTOP_OFF = new BlockComputerOff(Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F), CROCKERTOP_ON, BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE).setRegistryName("crockertop_off"));
		registry.register(HUBTOP_ON = new BlockComputerOn(Block.Properties.create(Material.IRON, MaterialColor.GREEN).hardnessAndResistance(4.0F), BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE, () -> HUBTOP_OFF.asItem()).setRegistryName("hubtop_on"));
		registry.register(HUBTOP_OFF = new BlockComputerOff(Block.Properties.create(Material.IRON, MaterialColor.GREEN).hardnessAndResistance(4.0F), HUBTOP_ON, BlockComputerOff.LAPTOP_SHAPE, BlockComputerOff.LAPTOP_COLLISION_SHAPE).setRegistryName("hubtop_off"));
		registry.register(LUNCHTOP_ON = new BlockComputerOn(Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F), BlockComputerOff.LUNCHTOP_SHAPE, BlockComputerOff.LUNCHTOP_SHAPE, () -> LUNCHTOP_OFF.asItem()).setRegistryName("lunchtop_on"));
		registry.register(LUNCHTOP_OFF = new BlockComputerOff(Block.Properties.create(Material.IRON, MaterialColor.RED).hardnessAndResistance(4.0F), LUNCHTOP_ON, BlockComputerOff.LUNCHTOP_SHAPE, BlockComputerOff.LUNCHTOP_SHAPE).setRegistryName("lunchtop_off"));
		registry.register(TRANSPORTALIZER = new BlockTransportalizer(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("transportalizer"));
		registry.register(GRIST_WIDGET = new BlockGristWidget(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("grist_widget"));
		registry.register(URANIUM_COOKER = new BlockUraniumCooker(Block.Properties.create(Material.IRON).hardnessAndResistance(3.0F)).setRegistryName("uranium_cooker"));
		
		registry.register(CRUXITE_DOWEL = new BlockCruxiteDowel(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.0F)).setRegistryName("cruxite_dowel"));
		
		registry.register(GOLD_SEEDS = new BlockGoldSeeds(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.1F).sound(SoundType.METAL).doesNotBlockMovement()).setRegistryName("gold_seeds"));
		registry.register(WOODEN_CACTUS = new BlockCactusSpecial(Block.Properties.create(Material.WOOD).needsRandomTick().hardnessAndResistance(1.0F, 2.5F).sound(SoundType.WOOD), ToolType.AXE).setRegistryName("wooden_cactus"));
		
		registry.register(APPLE_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.5F, null).setRegistryName("apple_cake"));
		registry.register(BLUE_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 150, 0))).setRegistryName("blue_cake"));
		registry.register(COLD_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.3F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));}).setRegistryName("cold_cake"));
		registry.register(RED_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, (EntityPlayer player) -> player.heal(1)).setRegistryName("red_cake"));
		registry.register(HOT_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, (EntityPlayer player) -> player.setFire(4)).setRegistryName("hot_cake"));
		registry.register(REVERSE_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 2, 0.1F, null).setRegistryName("reverse_cake"));
		registry.register(FUCHSIA_CAKE = new BlockSimpleCake(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH), 3, 0.5F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 350, 1));player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));}).setRegistryName("fuchsia_cake"));
		
		registry.register(PRIMED_TNT = new BlockTNTSpecial(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT), true, false, false).setRegistryName("primed_tnt"));
		registry.register(UNSTABLE_TNT = new BlockTNTSpecial(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT).needsRandomTick(), false, true, false).setRegistryName("unstable_tnt"));
		registry.register(INSTANT_TNT = new BlockTNTSpecial(Block.Properties.create(Material.TNT).hardnessAndResistance(0.0F).sound(SoundType.PLANT), false, false, true).setRegistryName("instant_tnt"));
		registry.register(WOODEN_EXPLOSIVE_BUTTON = new BlockButtonSpecial(Block.Properties.create(Material.CIRCUITS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD), true, true).setRegistryName("wooden_explosive_button"));
		registry.register(STONE_EXPLOSIVE_BUTTON = new BlockButtonSpecial(Block.Properties.create(Material.CIRCUITS).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.STONE), true, false).setRegistryName("stone_explosive_button"));
		
		registry.register(BLENDER = new BlockDecor(Block.Properties.create(Material.IRON).hardnessAndResistance(0.5F).sound(SoundType.METAL), BlockDecor.BLENDER_SHAPE, BlockAlchemiter.UNDEFINED_FACE_SHAPES).setRegistryName("blender"));
		registry.register(CHESSBOARD = new BlockDecor(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), BlockDecor.CHESSBOARD_SHAPE, BlockAlchemiter.CORNER_FACE_SHAPES).setRegistryName("chessboard"));
		registry.register(MINI_FROG_STATUE = new BlockDecor(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5F), BlockDecor.FROG_STATUE_SHAPE, BlockAlchemiter.UNDEFINED_FACE_SHAPES).setRegistryName("mini_frog_statue"));
		registry.register(GLOWYSTONE_WIRE = new BlockGlowystoneWire(Block.Properties.create(Material.CIRCUITS).hardnessAndResistance(0.0F).lightValue(16).doesNotBlockMovement()).setRegistryName("glowystone_wire"));
		
		/*registry.register(blockOil.setRegistryName("block_oil"));
		registry.register(blockBlood.setRegistryName("block_blood"));
		registry.register(blockBrainJuice.setRegistryName("block_brain_juice"));
		registry.register(blockWatercolors.setRegistryName("block_watercolors"));
		registry.register(blockEnder.setRegistryName("block_ender"));
		registry.register(blockLightWater.setRegistryName("block_light_water"));*/
		
		//fluids
		/*liquidGrists = new Block[GristType.allGrists];
		gristFluids = new Fluid[GristType.allGrists];
		for(GristType grist : GristType.values()) {
			gristFluids[grist.getId()] = new Fluid(grist.getName(), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Still"), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Flowing"));
			FluidRegistry.registerFluid(gristFluids[grist.getId()]);
			liquidGrists[grist.getId()] = GameRegistry.register(new BlockFluidGrist(gristFluids[grist.getId()], Material.WATER).setRegistryName("liquid_" + grist.getName())).setUnlocalizedName("liquid_" + grist.getName());
		}*/
	}
	
	private static Fluid createFluid(String name, ResourceLocation still, ResourceLocation flowing, String unlocalizedName)
	{
		Fluid fluid = new Fluid(name, still, flowing);
		
		/*boolean useFluid = FluidRegistry.registerFluid(fluid);
		
		if(useFluid)
			fluid.setUnlocalizedName(unlocalizedName);
		else fluid = FluidRegistry.getFluid(name);*/
		
		return fluid;
	}
}