package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckBlocks
{
	//Blocks
	public static Block chessTile = new BlockChessTile();
	public static Block skaiaPortal = new BlockSkaiaPortal(Material.PORTAL);
	
	public static Block coloredDirt = new BlockColoredDirt().setUnlocalizedName("coloredDirt").setHardness(0.5F);
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
	public static Block petrifiedLog = new BlockPetrifiedLog();
	public static Block petrifiedPoppy = new BlockPetrifiedFlora("petrifiedPoppy");
	public static Block petrifiedGrass = new BlockPetrifiedFlora("petrifiedGrass");
	public static Block bloomingCactus = new BlockDesertFlora("bloomingCactus"); 
	public static Block desertBush = new BlockDesertBush("desertBush");
	public static Block glowingMushroom = new BlockGlowingMushroom();
	public static Block glowingLog = new BlockGlowingLog();
	public static Block glowingPlanks = new BlockCustom(Material.WOOD, MapColor.LIGHT_BLUE, SoundType.WOOD).setFireInfo(5, 20).setUnlocalizedName("glowingPlanks").setLightLevel(0.5F).setHardness(2.0F).setResistance(5.0F).setCreativeTab(TabMinestuck.instance);
	public static Block stone = new BlockMinestuckStone();
	public static Block glowyGoop = new BlockGlowyGoop("glowyGoop");
	public static Block coagulatedBlood = new BlockGoop("coagulatedBlood");
	public static Block coarseStoneStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE)).setUnlocalizedName("stairsMinestuck.coarse");
	public static Block shadeBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK)).setUnlocalizedName("stairsMinestuck.shadeBrick");
	public static Block frostBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK)).setUnlocalizedName("stairsMinestuck.frostBrick");
	public static Block castIronStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.CAST_IRON)).setUnlocalizedName("stairsMinestuck.castIron");
	public static Block myceliumBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.MYCELIUM_BRICK)).setUnlocalizedName("stairsMinestuck.myceliumBrick");
	public static Block log = new BlockMinestuckLog1();
	public static Block leaves1 = new BlockMinestuckLeaves1();
	public static Block planks = new BlockMinestuckPlanks();
	public static Block frostPlanks = new BlockFrostPlanks();
	public static Block aspectSapling = new BlockAspectSapling().setCreativeTab(null);
	public static Block rainbowSapling = new BlockRainbowSapling();
	public static Block aspectLog1 = new BlockAspectLog().setCreativeTab(null);
	public static Block aspectLog2 = new BlockAspectLog2().setCreativeTab(null);
	public static Block aspectLog3 = new BlockAspectLog3().setCreativeTab(null);
	public static Block woodenCactus = new BlockCactusSpecial(SoundType.WOOD, "axe").setHardness(1.0F).setResistance(2.5F).setUnlocalizedName("woodenCactus");
	public static Block sugarCube = new BlockCustom(Material.SAND, MapColor.SNOW, SoundType.SAND).setUnlocalizedName("sugarCube").setHardness(0.4F).setCreativeTab(TabMinestuck.instance);
	public static Block rabbitSpawner = new BlockMobSpawner().setUnlocalizedName("rabbitSpawner");
	public static Block appleCake = new BlockSimpleCake(2, 0.5F, null).setUnlocalizedName("appleCake");
	public static Block blueCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 150, 0))).setUnlocalizedName("blueCake");
	public static Block coldCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));}).setUnlocalizedName("coldCake");
	public static Block redCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.heal(1)).setUnlocalizedName("redCake");
	public static Block hotCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.setFire(4)).setUnlocalizedName("hotCake");
	public static Block reverseCake = new BlockSimpleCake(2, 0.1F, null).setUnlocalizedName("cake");
	public static Block fuchsiaCake = new BlockSimpleCake(3, 0.5F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 350, 1));player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 0));}).setUnlocalizedName("fuchsiaCake");
	public static Block vein = new BlockVein("vein");
	public static Block veinCorner = new BlockVeinCorner("veinCorner");
	public static Block veinCornerInverted = new BlockVeinCorner("veinCornerInverted");
	
	public static Block treatedPlanks = new BlockCustom(Material.WOOD, MapColor.WOOD, SoundType.WOOD).setFireInfo(1, 0).setUnlocalizedName("treatedPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	
	public static Block endLog = new BlockEndLog();
	public static Block endLeaves = new BlockEndLeaves();
	public static Block endPlanks = new BlockCustom(Material.WOOD, MapColor.SAND, SoundType.WOOD).setFireInfo(1, 250).setUnlocalizedName("endPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block endSapling = new BlockEndSapling();
	public static Block endGrass = new BlockEndGrass();
	public static Block coarseEndStone = new BlockCustomStone(MapColor.SAND).setHardness(3.0F).setUnlocalizedName("coarseEndStone").setCreativeTab(TabMinestuck.instance);
	
	public static Block floweryMossStone = new BlockCustomStone(MapColor.GRAY).setUnlocalizedName("floweryMossStone").setCreativeTab(TabMinestuck.instance);
	public static Block floweryMossBrick = new BlockCustomStone(MapColor.GRAY).setUnlocalizedName("floweryMossBrick").setCreativeTab(TabMinestuck.instance);
	public static Block strawberry = new BlockStrawberry();
	public static Block strawberryStem = new BlockMinestuckStem((BlockDirectional) strawberry).setUnlocalizedName("strawberryStem").setCreativeTab(null);
	
	public static Block deadLog = new BlockMinestuckLog().setUnlocalizedName("logDead").setCreativeTab(TabMinestuck.instance);
	public static Block deadPlanks = new BlockCustom(Material.WOOD, MapColor.WOOD, SoundType.WOOD).setFireInfo(5, 5).setUnlocalizedName("deadPlanks").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block chalk = new BlockCustomStone(MapColor.SNOW).setUnlocalizedName("chalk")	.setCreativeTab(TabMinestuck.instance);
	public static Block chalkBricks = new BlockCustomStone(MapColor.SNOW).setUnlocalizedName("chalkBricks")	.setCreativeTab(TabMinestuck.instance);
	public static Block chalkChisel = new BlockCustomStone(MapColor.SNOW).setUnlocalizedName("chalkChisel")	.setCreativeTab(TabMinestuck.instance);
	public static Block chalkPolish = new BlockCustomStone(MapColor.SNOW).setUnlocalizedName("chalkPolish")	.setCreativeTab(TabMinestuck.instance);
	public static Block pinkStoneSmooth = new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStoneSmooth")	.setCreativeTab(TabMinestuck.instance);
	public static Block pinkStoneBricks = new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStoneBricks")	.setCreativeTab(TabMinestuck.instance);
	public static Block pinkStoneChisel = new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStoneChisel")	.setCreativeTab(TabMinestuck.instance);
	public static Block pinkStoneCracked= new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStoneCracked").setCreativeTab(TabMinestuck.instance);
	public static Block pinkStoneMossy = new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStoneMossy")	.setCreativeTab(TabMinestuck.instance);
	public static Block pinkStonePolish = new BlockCustomStone(MapColor.PINK).setUnlocalizedName("pinkStonePolish")	.setCreativeTab(TabMinestuck.instance);
	public static Block denseCloud = new BlockDenseCloud();
	
	//Ores
	public static Block oreCruxite = new BlockCruxiteOre();
	public static BlockUraniumOre oreUranium = (BlockUraniumOre) new BlockUraniumOre().setUnlocalizedName("oreUranium");
	public static Block coalOreNetherrack = new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setUnlocalizedName("oreCoal");
	public static Block coalOrePinkStone = new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setUnlocalizedName("oreCoal");
	public static Block ironOreEndStone = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron");
	public static Block ironOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron");
	public static Block ironOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron");
	public static Block goldOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold");
	public static Block goldOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold");
	public static Block goldOrePinkStone = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold");
	public static Block redstoneOreEndStone = new BlockVanillaOre(BlockVanillaOre.OreType.REDSTONE).setUnlocalizedName("oreRedstone");
	public static Block quartzOreStone = new BlockVanillaOre(BlockVanillaOre.OreType.QUARTZ).setUnlocalizedName("oreQuartz");
	public static Block lapisOrePinkStone = new BlockVanillaOre(BlockVanillaOre.OreType.LAPIS).setUnlocalizedName("oreLapis");
	public static Block diamondOrePinkStone = new BlockVanillaOre(BlockVanillaOre.OreType.DIAMOND).setUnlocalizedName("oreDiamond");
	
	public static Block uraniumCooker = new BlockUraniumCooker().setUnlocalizedName("uraniumCooker");
	
	public static Block primedTnt = new BlockTNTSpecial(true, false, false).setUnlocalizedName("primedTnt");
	public static Block unstableTnt = new BlockTNTSpecial(false, true, false).setUnlocalizedName("unstableTnt");
	public static Block instantTnt = new BlockTNTSpecial(false, false, true).setUnlocalizedName("instantTnt");
	public static Block woodenExplosiveButton = new BlockButtonSpecial(true, true).setUnlocalizedName("buttonTnt");
	public static Block stoneExplosiveButton = new BlockButtonSpecial(false, true).setUnlocalizedName("buttonTnt");
	
	public static BlockLayered layeredSand = (BlockLayered) new BlockLayered(Blocks.SAND.getDefaultState()).setUnlocalizedName("layeredSand");
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

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.register(chessTile.setRegistryName("chess_tile"));
		registry.register(coloredDirt.setRegistryName("colored_dirt"));
		registry.register(layeredSand.setRegistryName("layered_sand"));
		registry.register(stone.setRegistryName("stone"));
		registry.register(sugarCube.setRegistryName("sugar_cube"));
		
		registry.register(log.setRegistryName("log"));
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
		registry.register(glowyGoop.setRegistryName("glowy_goop"));
		registry.register(coagulatedBlood.setRegistryName("coagulated_blood"));
		registry.register(petrifiedLog.setRegistryName("petrified_log"));
		registry.register(petrifiedPoppy.setRegistryName("petrified_poppy"));
		registry.register(petrifiedGrass.setRegistryName("petrified_grass"));
		registry.register(bloomingCactus.setRegistryName("blooming_cactus"));
		registry.register(desertBush.setRegistryName("desert_bush"));
		registry.register(woodenCactus.setRegistryName("wooden_cactus"));
		
		registry.register(oreCruxite.setRegistryName("ore_cruxite"));
		registry.register(oreUranium.setRegistryName("ore_uranium"));
		registry.register(coalOreNetherrack.setRegistryName("coal_ore_netherrack"));
		registry.register(coalOrePinkStone.setRegistryName("coal_ore_pink_stone"));
		registry.register(ironOreEndStone.setRegistryName("iron_ore_end_stone"));
		registry.register(ironOreSandstone.setRegistryName("iron_ore_sandstone"));
		registry.register(ironOreSandstoneRed.setRegistryName("iron_ore_sandstone_red"));
		registry.register(goldOreSandstone.setRegistryName("gold_ore_sandstone"));
		registry.register(goldOreSandstoneRed.setRegistryName("gold_ore_sandstone_red"));
		registry.register(goldOrePinkStone.setRegistryName("gold_ore_pink_stone"));
		registry.register(redstoneOreEndStone.setRegistryName("redstone_ore_end_stone"));
		registry.register(quartzOreStone.setRegistryName("quartz_ore_stone"));
		registry.register(lapisOrePinkStone.setRegistryName("lapis_ore_pink_stone"));
		registry.register(diamondOrePinkStone.setRegistryName("diamond_ore_pink_stone"));
		
		registry.register(cruxiteBlock.setRegistryName("cruxite_block"));
		registry.register(uraniumBlock.setRegistryName("uranium_block"));
		registry.register(genericObject.setRegistryName("generic_object"));
		registry.register(blockCruxiteDowel.setRegistryName("cruxite_dowel"));
		
		registry.register(coarseStoneStairs.setRegistryName("coarse_stone_stairs"));
		registry.register(shadeBrickStairs.setRegistryName("shade_brick_stairs"));
		registry.register(frostBrickStairs.setRegistryName("frost_brick_stairs"));
		registry.register(castIronStairs.setRegistryName("cast_iron_stairs"));
		registry.register(myceliumBrickStairs.setRegistryName("mycelium_brick_stairs"));
		
		registry.register(skaiaPortal.setRegistryName("skaia_portal"));
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
		registry.register(fuchsiaCake.setRegistryName("fuchsia_cake"));
		
		registry.register(coarseEndStone.setRegistryName("coarse_end_stone"));
		registry.register(endGrass.setRegistryName("end_grass"));
		registry.register(endLog.setRegistryName("end_log"));
		registry.register(endLeaves.setRegistryName("end_leaves"));
		registry.register(endPlanks.setRegistryName("end_planks"));
		registry.register(endSapling.setRegistryName("end_sapling"));
		
		registry.register(treatedPlanks.setRegistryName("treated_planks"));
		registry.register(floweryMossStone.setRegistryName("flowery_moss_stone"));
		registry.register(floweryMossBrick.setRegistryName("flowery_moss_brick"));
		registry.register(strawberry.setRegistryName("strawberry"));
		registry.register(strawberryStem.setRegistryName("strawberry_stem"));
		
		registry.register(deadLog.setRegistryName("dead_log"));
		registry.register(deadPlanks.setRegistryName("dead_planks"));
		registry.register(chalk.setRegistryName("chalk"));
		registry.register(chalkBricks.setRegistryName("chalk_bricks"));
		registry.register(chalkChisel.setRegistryName("chiseled_chalk_bricks"));
		registry.register(chalkPolish.setRegistryName("polished_chalk"));
		registry.register(pinkStoneSmooth.setRegistryName("pink_stone"));
		registry.register(pinkStoneBricks.setRegistryName("pink_stone_bricks"));
		registry.register(pinkStoneChisel.setRegistryName("pink_chiseled_stone"));
		registry.register(pinkStoneCracked.setRegistryName("pink_cracked_stone"));
		registry.register(pinkStoneMossy.setRegistryName("pink_moss_stone_bricks"));
		registry.register(pinkStonePolish.setRegistryName("pink_polished_stone"));
		registry.register(denseCloud.setRegistryName("dense_cloud"));
		
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