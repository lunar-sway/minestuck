package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckBlocks
{
	//Skaia
	public static Block CHESS_DIRT_BLACK, CHESS_DIRT_WHITE, CHESS_DIRT_DARK_GRAY, CHESS_DIRT_LIGHT_GRAY;
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
	
	//Land Environment Blocks
	public static Block BLUE_DIRT, THOUGHT_DIRT;
	public static Block COARSE_STONE, COARSE_CHISELED;
	public static Block SHADE_BRICKS, SHADE_SMOOTH;
	public static Block FROST_BRICKS, FROST_TILE, FROST_BRICKS_CHISELED;
	public static Block CAST_IRON, CAST_IRON_CHISELED;
	public static Block MYCELIUM_BRICKS;
	public static Block BLACK_STONE;
	public static Block FLOWERY_MOSS_STONE,FLOWERY_MOSS_BRICK;
	public static Block COARSE_END_STONE, END_GRASS;
	public static Block CHALK, CHALK_BRICKS, CHALK_CHISELED, CHALK_POLISHED;
	public static Block PINK_STONE, PINK_STONE_BRICKS, PINK_STONE_CHISELED, PINK_STONE_CRACKED, PINK_STONE_MOSSY, PINK_STONE_POLISHED;
	public static Block DENSE_CLOUD, DENSE_CLOUD_BRIGHT;
	public static Block GLOWY_GOOP;
	public static Block COAGULATED_BLOOD;
	public static Block SUGAR_CUBE;
	public static Block LAYERED_SAND;
	
	//Land Wood/Plant Blocks
	public static Block VINE_LOG, FLOWERY_VINE_LOG, PETRIFIED_LOG;
	public static Block FROST_LOG, FROST_PLANKS, FROST_LEAVES;
	public static Block rainbowLog, rainbowPlanks, rainbowLeaves;
	public static Block log = new BlockMinestuckLog1();
	public static Block planks = new BlockMinestuckPlanks();
	public static Block leaves1 = new BlockMinestuckLeaves1();
	public static Block frostPlanks = new BlockFrostPlanks();
	public static Block deadLog = new BlockMinestuckLog().setUnlocalizedName("logDead").setCreativeTab(TabMinestuck.instance);
	public static Block deadPlanks = new BlockCustom(Material.WOOD, MapColor.WOOD, SoundType.WOOD).setFireInfo(5, 5).setUnlocalizedName("deadPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block glowingLog = new BlockGlowingLog();
	public static Block glowingPlanks = new BlockCustom(Material.WOOD, MapColor.LIGHT_BLUE, SoundType.WOOD).setFireInfo(5, 20).setUnlocalizedName("glowingPlanks").setLightLevel(0.5F).setHardness(2.0F).setResistance(5.0F).setCreativeTab(TabMinestuck.instance);
	public static Block aspectSapling = new BlockAspectSapling().setCreativeTab(null);
	public static Block rainbowSapling = new BlockRainbowSapling();
	public static Block aspectLog1 = new BlockAspectLog().setCreativeTab(null);
	public static Block aspectLog2 = new BlockAspectLog2().setCreativeTab(null);
	public static Block aspectLog3 = new BlockAspectLog3().setCreativeTab(null);
	public static Block endLog = new BlockEndLog();
	public static Block endLeaves = new BlockEndLeaves();
	public static Block endPlanks = new BlockCustom(Material.WOOD, MapColor.SAND, SoundType.WOOD).setFireInfo(1, 250).setUnlocalizedName("endPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block endSapling = new BlockEndSapling();
	public static Block treatedPlanks = new BlockCustom(Material.WOOD, MapColor.WOOD, SoundType.WOOD).setFireInfo(1, 0).setUnlocalizedName("treatedPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	
	
	public static Block woodenCactus = new BlockCactusSpecial(SoundType.WOOD, "axe").setHardness(1.0F).setResistance(2.5F).setUnlocalizedName("woodenCactus");
	public static Block petrifiedPoppy = new BlockPetrifiedFlora("petrifiedPoppy");
	public static Block petrifiedGrass = new BlockPetrifiedFlora("petrifiedGrass");
	public static Block bloomingCactus = new BlockDesertFlora("bloomingCactus");
	public static Block desertBush = new BlockDesertBush("desertBush");
	public static Block glowingMushroom = new BlockGlowingMushroom();
	
	public static Block vein = new BlockVein("vein");
	public static Block veinCorner = new BlockVeinCorner("veinCorner");
	public static Block veinCornerInverted = new BlockVeinCorner("veinCornerInverted");
	
	public static Block cruxiteBlock = new Block(Material.ROCK, MapColor.LIGHT_BLUE).setUnlocalizedName("cruxiteBlock").setHardness(3.0F).setCreativeTab(TabMinestuck.instance);
	public static Block uraniumBlock = new Block(Material.ROCK, MapColor.LIME).setLightLevel(0.5F).setUnlocalizedName("uraniumBlock").setHardness(3.0F).setCreativeTab(TabMinestuck.instance);
	public static Block genericObject = new BlockCustom(Material.GOURD, MapColor.LIME, SoundType.WOOD).setUnlocalizedName("genericObject").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	
	public static Block blender = new BlockDecor("blender",SoundType.METAL).setCreativeTab(TabMinestuck.instance);
	public static Block chessboard = new BlockDecor("chessboard").setCreativeTab(TabMinestuck.instance);
	public static Block frogStatueReplica = new BlockDecor("frogStatueReplica").setCreativeTab(TabMinestuck.instance);
	
	public static Block sburbMachine = new BlockSburbMachine();
	public static Block crockerMachine = new BlockCrockerMachine();
	public static Block blockComputerOff = new BlockComputerOff();
	public static Block blockComputerOn = new BlockComputerOn();
	public static Block blockLaptopOff = new BlockVanityLaptopOff().setCreativeTab(null);
	public static Block blockLaptopOn = new BlockVanityLaptopOn();
	public static Block transportalizer = new BlockTransportalizer();
	
	public static Block punchDesignix = new BlockPunchDesignix();
	public static BlockTotemLathe[] totemlathe = BlockTotemLathe.createBlocks();
	public static BlockAlchemiter[] alchemiter = BlockAlchemiter.createBlocks();
	public static Block cruxtruder = new BlockCruxtruder();
	public static Block cruxtruderLid = new BlockCruxtruderLid();
	public static Block holopad = new BlockHolopad();
	public static BlockJumperBlock[] jumperBlockExtension = BlockJumperBlock.createBlocks();
	public static BlockAlchemiterUpgrades[] alchemiterUpgrades = BlockAlchemiterUpgrades.createBlocks();
	
	public static Block blockCruxiteDowel = new BlockCruxtiteDowel();
	public static Block blockGoldSeeds = new BlockGoldSeeds();
	public static Block returnNode = new BlockReturnNode();
	public static Block gate = new BlockGate();
	public static Block coarseStoneStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE)).setUnlocalizedName("stairsMinestuck.coarse");
	public static Block shadeBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK)).setUnlocalizedName("stairsMinestuck.shadeBrick");
	public static Block frostBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK)).setUnlocalizedName("stairsMinestuck.frostBrick");
	public static Block castIronStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.CAST_IRON)).setUnlocalizedName("stairsMinestuck.castIron");
	public static Block myceliumBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.MYCELIUM_BRICK)).setUnlocalizedName("stairsMinestuck.myceliumBrick");
	public static Block rabbitSpawner = new BlockMobSpawner().setUnlocalizedName("rabbitSpawner");
	public static Block appleCake = new BlockSimpleCake(2, 0.5F, null).setUnlocalizedName("appleCake");
	public static Block blueCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 150, 0))).setUnlocalizedName("blueCake");
	public static Block coldCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));}).setUnlocalizedName("coldCake");
	public static Block redCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.heal(1)).setUnlocalizedName("redCake");
	public static Block hotCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.setFire(4)).setUnlocalizedName("hotCake");
	public static Block reverseCake = new BlockSimpleCake(2, 0.1F, null).setUnlocalizedName("cake");
	
	
	public static Block strawberry = new BlockStrawberry();
	public static Block strawberryStem = new BlockMinestuckStem((BlockDirectional) strawberry).setUnlocalizedName("strawberryStem").setCreativeTab(null);
	
	
	public static Block uraniumCooker = new BlockUraniumCooker().setUnlocalizedName("uraniumCooker");
	
	public static Block primedTnt = new BlockTNTSpecial(true, false, false).setUnlocalizedName("primedTnt");
	public static Block unstableTnt = new BlockTNTSpecial(false, true, false).setUnlocalizedName("unstableTnt");
	public static Block instantTnt = new BlockTNTSpecial(false, false, true).setUnlocalizedName("instantTnt");
	public static Block woodenExplosiveButton = new BlockButtonSpecial(true, true).setUnlocalizedName("buttonTnt");
	public static Block stoneExplosiveButton = new BlockButtonSpecial(false, true).setUnlocalizedName("buttonTnt");
	
	public static Block glowystoneWire = new BlockGlowystoneWire().setUnlocalizedName("glowystoneWire");
	
	public static Fluid fluidOil = createFluid("oil", new ResourceLocation("minestuck", "blocks/oil_still"), new ResourceLocation("minestuck", "blocks/oil_flowing"), "tile.oil");
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
	public static Fluid[] gristFluids;

	public static Block[] chesstiles;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.register(CHESS_DIRT_BLACK = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.BLACK).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("chess_dirt_black"));
		registry.register(CHESS_DIRT_WHITE = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.SNOW).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("chess_dirt_white"));
		registry.register(CHESS_DIRT_DARK_GRAY = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.GRAY).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("chess_dirt__dark_gray"));
		registry.register(CHESS_DIRT_LIGHT_GRAY = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("chess_dirt_light_gray"));
		registry.register(SKAIA_PORTAL = new BlockSkaiaPortal(Block.Properties.create(Material.PORTAL, MaterialColor.CYAN).doesNotBlockMovement().lightValue(11).hardnessAndResistance(-1.0F, 3600000.0F)).setRegistryName("skaia_portal"));
		
		registry.register(CRUXITE_ORE_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_stone"));
		registry.register(CRUXITE_ORE_NETHERRACK = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_netherrack"));
		registry.register(CRUXITE_ORE_COBBLESTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_cobblestone"));
		registry.register(CRUXITE_ORE_SANDSTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_sandstone"));
		registry.register(CRUXITE_ORE_RED_SANDSTONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_red_sandstone"));
		registry.register(CRUXITE_ORE_END_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_end_stone"));
		registry.register(CRUXITE_ORE_PINK_STONE = new BlockCruxiteOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)).setRegistryName("cruxite_ore_pink_stone"));
		registry.register(URANIUM_ORE_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_stone"));
		registry.register(URANIUM_ORE_NETHERRACK = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_netherrack"));
		registry.register(URANIUM_ORE_COBBLESTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_cobblestone"));
		registry.register(URANIUM_ORE_SANDSTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_sandstone"));
		registry.register(URANIUM_ORE_RED_SANDSTONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_red_sandstone"));
		registry.register(URANIUM_ORE_END_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_end_stone"));
		registry.register(URANIUM_ORE_PINK_STONE = new BlockUraniumOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).lightValue(4)).setRegistryName("uranium_ore_pink_stone"));
		registry.register(COAL_ORE_NETHERRACK = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.COAL).setRegistryName("coal_ore_netherrack"));
		registry.register(COAL_ORE_PINK_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.COAL).setRegistryName("coal_ore_pink_stone"));
		registry.register(IRON_ORE_END_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_end_stone"));
		registry.register(IRON_ORE_SANDSTONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone"));
		registry.register(IRON_ORE_SANDSTONE_RED = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone_red"));
		registry.register(GOLD_ORE_SANDSTONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone"));
		registry.register(GOLD_ORE_SANDSTONE_RED = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone_red"));
		registry.register(GOLD_ORE_PINK_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_pink_stone"));
		registry.register(REDSTONE_ORE_END_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.REDSTONE).setRegistryName("redstone_ore_end_stone"));
		registry.register(QUARTZ_ORE_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.QUARTZ).setRegistryName("quartz_ore_stone"));
		registry.register(LAPIS_ORE_PINK_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.LAPIS).setRegistryName("lapis_ore_pink_stone"));
		registry.register(DIAMOND_ORE_PINK_STONE = new BlockVanillaOre(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), BlockVanillaOre.OreType.DIAMOND).setRegistryName("diamond_ore_pink_stone"));
		
		registry.register(BLUE_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.BLUE).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("blue_dirt"));
		registry.register(THOUGHT_DIRT = new BlockDirt(Block.Properties.create(Material.GROUND, MaterialColor.LIME).hardnessAndResistance(0.5F).sound(SoundType.GROUND)).setRegistryName("thought_dirt"));
		registry.register(COARSE_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryName("coarse_stone"));
		registry.register(COARSE_CHISELED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F)).setRegistryName("coarse_chiseled"));
		registry.register(SHADE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("shade_bricks"));
		registry.register(SHADE_SMOOTH = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("shade_smooth"));
		registry.register(FROST_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("frost_bricks"));
		registry.register(FROST_TILE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("frost_tile"));
		registry.register(FROST_BRICKS_CHISELED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.ICE).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("frost_bricks_chiseled"));
		registry.register(CAST_IRON = new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("cast_iron"));
		registry.register(CAST_IRON_CHISELED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.IRON).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("cast_iron_chiseled"));
		registry.register(MYCELIUM_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.MAGENTA).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("mycelium_bricks"));
		registry.register(BLACK_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2.5F, 6.0F)).setRegistryName("black_stone"));
		registry.register(FLOWERY_MOSS_STONE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("flowery_moss_stone"));
		registry.register(FLOWERY_MOSS_BRICK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("flowery_moss_brick"));
		registry.register(COARSE_END_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.0F, 9.0F)).setRegistryName("coarse_end_stone"));
		registry.register(END_GRASS = new BlockEndGrass(Block.Properties.create(Material.ROCK, MaterialColor.PURPLE).hardnessAndResistance(3.0F, 9.0F).needsRandomTick()).setRegistryName("end_grass"));
		registry.register(CHALK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chalk"));
		registry.register(CHALK_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chalk_bricks"));
		registry.register(CHALK_CHISELED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("chiseled_chalk_bricks"));
		registry.register(CHALK_POLISHED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("polished_chalk"));
		registry.register(PINK_STONE = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_stone"));
		registry.register(PINK_STONE_BRICKS = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_stone_bricks"));
		registry.register(PINK_STONE_CHISELED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_chiseled_stone"));
		registry.register(PINK_STONE_CRACKED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_cracked_stone"));
		registry.register(PINK_STONE_MOSSY = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_moss_stone_bricks"));
		registry.register(PINK_STONE_POLISHED = new Block(Block.Properties.create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(1.5F, 6.0F)).setRegistryName("pink_polished_stone"));
		registry.register(DENSE_CLOUD = new Block(Block.Properties.create(Material.GLASS, MaterialColor.YELLOW).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud"));
		registry.register(DENSE_CLOUD_BRIGHT = new Block(Block.Properties.create(Material.GLASS, MaterialColor.LIGHT_GRAY).hardnessAndResistance(0.5F).sound(SoundType.SNOW)).setRegistryName("dense_cloud_bright"));
		registry.register(GLOWY_GOOP = new BlockGoop(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME).lightValue(15)).setRegistryName("glowy_goop"));
		registry.register(COAGULATED_BLOOD = new BlockGoop(Block.Properties.create(Material.CLAY).hardnessAndResistance(0.1F).sound(SoundType.SLIME)).setRegistryName("coagulated_blood"));
		registry.register(SUGAR_CUBE = new Block(Block.Properties.create(Material.SAND, MaterialColor.SNOW).hardnessAndResistance(0.4F).sound(SoundType.SAND)).setRegistryName("sugar_cube"));
		registry.register(LAYERED_SAND = new BlockLayered(Blocks.SAND).setRegistryName("layered_sand"));
		
		registry.register(VINE_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("vine_log"));
		registry.register(FLOWERY_VINE_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("flowery_vine_log"));
		registry.register(PETRIFIED_LOG = new BlockMinestuckLog(MaterialColor.WOOD, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.STONE), 0, 0).setRegistryName("petrified_log"));
		registry.register(FROST_LOG = new BlockMinestuckLog(MaterialColor.ICE, Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F).sound(SoundType.WOOD)).setRegistryName("frost_log"));
		registry.register(FROST_PLANKS = new Block(Block.Properties.create(Material.WOOD, MaterialColor.ICE).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)).setRegistryName("frost_planks"));
		registry.register(leaves1.setRegistryName("leaves"));
		registry.register(planks.setRegistryName("planks"));
		registry.register(frostPlanks.setRegistryName("frost_planks"));
		registry.register(aspectSapling.setRegistryName("aspect_sapling"));
		registry.register(rainbowSapling.setRegistryName("rainbow_sapling"));
		registry.register(aspectLog1.setRegistryName("aspect_log_1"));
		registry.register(aspectLog2.setRegistryName("aspect_log_2"));
		registry.register(aspectLog3.setRegistryName("aspect_log_3"));
		registry.register(glowingLog.setRegistryName("glowing_log"));
		registry.register(glowingPlanks.setRegistryName("glowing_planks"));
		registry.register(glowingMushroom.setRegistryName("glowing_mushroom"));
		registry.register(petrifiedPoppy.setRegistryName("petrified_poppy"));
		registry.register(petrifiedGrass.setRegistryName("petrified_grass"));
		registry.register(bloomingCactus.setRegistryName("blooming_cactus"));
		registry.register(desertBush.setRegistryName("desert_bush"));
		registry.register(woodenCactus.setRegistryName("wooden_cactus"));
		
		registry.register(cruxiteBlock.setRegistryName("cruxite_block"));
		registry.register(uraniumBlock.setRegistryName("uranium_block"));
		registry.register(genericObject.setRegistryName("generic_object"));
		registry.register(blockCruxiteDowel.setRegistryName("cruxite_dowel"));
		
		registry.register(coarseStoneStairs.setRegistryName("coarse_stone_stairs"));
		registry.register(shadeBrickStairs.setRegistryName("shade_brick_stairs"));
		registry.register(frostBrickStairs.setRegistryName("frost_brick_stairs"));
		registry.register(castIronStairs.setRegistryName("cast_iron_stairs"));
		registry.register(myceliumBrickStairs.setRegistryName("mycelium_brick_stairs"));
		
		registry.register(returnNode.setRegistryName("return_node"));
		registry.register(gate.setRegistryName("gate"));
		
		registry.register(sburbMachine.setRegistryName("sburb_machine"));
		registry.register(crockerMachine.setRegistryName("crocker_machine"));
		registry.register(transportalizer.setRegistryName("transportalizer"));
		registry.register(uraniumCooker.setRegistryName("uranium_cooker"));
		
		registry.register(punchDesignix.setRegistryName("punch_designix"));
		registry.register(totemlathe[0].setRegistryName("totem_lathe"));
		registry.register(totemlathe[1].setRegistryName("totem_lathe2"));
		registry.register(totemlathe[2].setRegistryName("totem_lathe3"));
		registry.register(alchemiter[0].setRegistryName("alchemiter"));
		registry.register(alchemiter[1].setRegistryName("alchemiter2"));
		registry.register(cruxtruder.setRegistryName("cruxtruder"));
		registry.register(cruxtruderLid.setRegistryName("cruxtruder_lid"));
		/*
		registry.register(holopad.setRegistryName("holopad"));
		registry.register(jumperBlockExtension[0].setRegistryName("jumper_block_extension"));
		registry.register(jumperBlockExtension[1].setRegistryName("jumper_block_extension2"));
		registry.register(jumperBlockExtension[2].setRegistryName("jumper_block_extension3"));
		registry.register(jumperBlockExtension[3].setRegistryName("jumper_block_extension4"));
		
		registry.register(alchemiterUpgrades[0].setRegistryName("alchemiter_upgrade"));
		registry.register(alchemiterUpgrades[1].setRegistryName("alchemiter_upgrade2"));
		registry.register(alchemiterUpgrades[2].setRegistryName("alchemiter_upgrade3"));
		registry.register(alchemiterUpgrades[3].setRegistryName("alchemiter_upgrade4"));
		*/
		registry.register(blender.setRegistryName("blender"));
		registry.register(chessboard.setRegistryName("chessboard"));
		registry.register(frogStatueReplica.setRegistryName("frog_statue_replica"));
		
		registry.register(blockComputerOff.setRegistryName("computer_standard"));
		registry.register(blockComputerOn.setRegistryName("computer_standard_on"));
		registry.register(blockLaptopOff.setRegistryName("vanity_laptop"));
		registry.register(blockLaptopOn.setRegistryName("vanity_laptop_on"));
		
		registry.register(blockGoldSeeds.setRegistryName("gold_seeds"));
		registry.register(glowystoneWire.setRegistryName("glowystone_wire"));
		
		registry.register(appleCake.setRegistryName("apple_cake"));
		registry.register(blueCake.setRegistryName("blue_cake"));
		registry.register(coldCake.setRegistryName("cold_cake"));
		registry.register(redCake.setRegistryName("red_cake"));
		registry.register(hotCake.setRegistryName("hot_cake"));
		registry.register(reverseCake.setRegistryName("reverse_cake"));
		
		registry.register(endLog.setRegistryName("end_log"));
		registry.register(endLeaves.setRegistryName("end_leaves"));
		registry.register(endPlanks.setRegistryName("end_planks"));
		registry.register(endSapling.setRegistryName("end_sapling"));
		
		registry.register(treatedPlanks.setRegistryName("treated_planks"));
		registry.register(strawberry.setRegistryName("strawberry"));
		registry.register(strawberryStem.setRegistryName("strawberry_stem"));
		
		registry.register(deadLog.setRegistryName("dead_log"));
		registry.register(deadPlanks.setRegistryName("dead_planks"));
		
		registry.register(vein.setRegistryName("vein"));
		registry.register(veinCorner.setRegistryName("vein_corner"));
		registry.register(veinCornerInverted.setRegistryName("vein_corner_inverted"));
		
		registry.register(primedTnt.setRegistryName("primed_tnt"));
		registry.register(unstableTnt.setRegistryName("unstable_tnt"));
		registry.register(instantTnt.setRegistryName("instant_tnt"));
		registry.register(woodenExplosiveButton.setRegistryName("wooden_button_explosive"));
		registry.register(stoneExplosiveButton.setRegistryName("stone_button_explosive"));
		
		registry.register(blockOil.setRegistryName("block_oil"));
		registry.register(blockBlood.setRegistryName("block_blood"));
		registry.register(blockBrainJuice.setRegistryName("block_brain_juice"));
		registry.register(blockWatercolors.setRegistryName("block_watercolors"));
		registry.register(blockEnder.setRegistryName("block_ender"));
		registry.register(blockLightWater.setRegistryName("block_light_water"));
		
		registry.register(rabbitSpawner.setRegistryName("rabbit_spawner"));
		
		for(EnumSlabStairMaterial material : EnumSlabStairMaterial.values())
		{
			registry.register(material.getSlab().setRegistryName(material.getName() + "_slab"));
			registry.register(material.getSlabFull().setRegistryName(material.getName() + "_slab_full"));
			registry.register(material.getStair().setRegistryName(material.getName() + "_stairs"));
		}
		
		//fluids
		/*liquidGrists = new Block[GristType.allGrists];
		gristFluids = new Fluid[GristType.allGrists];
		for(GristType grist : GristType.values()) {
			gristFluids[grist.getId()] = new Fluid(grist.getName(), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Still"), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Flowing"));
			FluidRegistry.registerFluid(gristFluids[grist.getId()]);
			liquidGrists[grist.getId()] = GameRegistry.register(new BlockFluidGrist(gristFluids[grist.getId()], Material.WATER).setRegistryName("liquid_" + grist.getName())).setUnlocalizedName("liquid_" + grist.getName());
		}*/
		
		chesstiles = new Block[] {chessDirtBlack, chessDirtWhite, chessDirtLightGray, chessDirtDarkGray};
		
		cruxiteBlock.setHarvestLevel("pickaxe", 0);
		uraniumBlock.setHarvestLevel("pickaxe", 1);
		coalOreNetherrack.setHarvestLevel("pickaxe", Blocks.COAL_ORE.getHarvestLevel(Blocks.COAL_ORE.getDefaultState()));
		coalOrePinkStone.setHarvestLevel("pickaxe", Blocks.COAL_ORE.getHarvestLevel(Blocks.COAL_ORE.getDefaultState()));
		ironOreEndStone.setHarvestLevel("pickaxe", Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()));
		ironOreSandstone.setHarvestLevel("pickaxe", Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()));
		ironOreSandstoneRed.setHarvestLevel("pickaxe", Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()));
		goldOreSandstone.setHarvestLevel("pickaxe", Blocks.GOLD_ORE.getHarvestLevel(Blocks.GOLD_ORE.getDefaultState()));
		goldOreSandstoneRed.setHarvestLevel("pickaxe", Blocks.GOLD_ORE.getHarvestLevel(Blocks.GOLD_ORE.getDefaultState()));
		goldOrePinkStone.setHarvestLevel("pickaxe", Blocks.GOLD_ORE.getHarvestLevel(Blocks.GOLD_ORE.getDefaultState()));
		redstoneOreEndStone.setHarvestLevel("pickaxe", Blocks.REDSTONE_ORE.getHarvestLevel(Blocks.REDSTONE_ORE.getDefaultState()));
		quartzOreStone.setHarvestLevel("pickaxe", Blocks.QUARTZ_ORE.getHarvestLevel(Blocks.QUARTZ_ORE.getDefaultState()));
		lapisOrePinkStone.setHarvestLevel("pickaxe", Blocks.LAPIS_ORE.getHarvestLevel(Blocks.LAPIS_ORE.getDefaultState()));
		diamondOrePinkStone.setHarvestLevel("pickaxe", Blocks.DIAMOND_ORE.getHarvestLevel(Blocks.DIAMOND_ORE.getDefaultState()));
		petrifiedLog.setHarvestLevel("pickaxe", 0);
	}
	
	private static Fluid createFluid(String name, ResourceLocation still, ResourceLocation flowing, String unlocalizedName)
	{
		Fluid fluid = new Fluid(name, still, flowing);
		
		boolean useFluid = FluidRegistry.registerFluid(fluid);
		
		if(useFluid)
			fluid.setUnlocalizedName(unlocalizedName);
		else fluid = FluidRegistry.getFluid(name);
		
		return fluid;
	}
	
	public static enum EnumSlabStairMaterial implements IStringSerializable
	{
		TREATED	(treatedPlanks.getDefaultState(),	"treated_planks"),
		RAINBOW	(planks.getDefaultState(),		"rainbow_planks"),
		END		(endPlanks.getDefaultState(),	"end_planks"),
		DEAD	(deadPlanks.getDefaultState(),	"dead_planks"),
		CHALK	(chalk.getDefaultState(),		"chalk"),
		CHALK_BRICK	(chalkBricks.getDefaultState(),	"chalk_bricks"),
		PINK_BRICK	(pinkStoneBricks.getDefaultState(),	"pink_stone_bricks");
		
		private final String name;
		private final String unlocalizedName;
		
		private final Block stair;
		private final Block slab;
		private final Block slabF;
		private final ItemBlock slabItem;
		
		EnumSlabStairMaterial(IBlockState modelState, String name)
		{
			this.name = name;
			String[] nameParts = name.split("_");
			StringBuilder unlocName = new StringBuilder(nameParts[0]);
			for(int i=1; i<nameParts.length; i++)
			{
				unlocName.append(nameParts[i].substring(0, 1).toUpperCase());
				unlocName.append(nameParts[i].substring(1));
			}
			this.unlocalizedName = unlocName.toString();
			
			stair = new BlockMinestuckStairs(modelState).setUnlocalizedName("stairsMinestuck." + unlocalizedName);
			slab = new BlockMinestuckSlab(modelState, this, false).setUnlocalizedName("slabMinestuck." + unlocalizedName);
			slabF = new BlockMinestuckSlab(modelState, this, true).setUnlocalizedName("slabMinestuckFull." + unlocalizedName);
			
			if(modelState.getBlock().getHarvestLevel(modelState) >= 0)
			{
				slab .setHarvestLevel("pickaxe", modelState.getBlock().getHarvestLevel(modelState));
				slabF.setHarvestLevel("pickaxe", modelState.getBlock().getHarvestLevel(modelState));
			}
			
			slabItem = new ItemSlab(getSlab(), (BlockSlab) getSlab(), (BlockSlab) getSlabFull());
			slabItem.setUnlocalizedName("slabMinestuck." + unlocalizedName).setHasSubtypes(false);
		}
		
		public Block getStair()	{	return stair;	}
		public Block getSlab()	{	return slab;	}
		public Block getSlabFull()		{	return slabF;	}
		public ItemBlock getSlabItem()	{	return slabItem;}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
	}
}