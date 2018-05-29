package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
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
	public static Block chessTile = new BlockChessTile().setRegistryName("chess_tile");
	public static Block skaiaPortal = new BlockSkaiaPortal(Material.PORTAL).setRegistryName("skaia_portal");
	
	public static Block coloredDirt = new BlockColoredDirt().setRegistryName("colored_dirt").setUnlocalizedName("coloredDirt").setHardness(0.5F);
	public static Block cruxiteBlock = new Block(Material.ROCK, MapColor.LIGHT_BLUE).setRegistryName("cruxite_block").setUnlocalizedName("cruxiteBlock").setHardness(3.0F).setCreativeTab(TabMinestuck.instance);
	public static Block genericObject = new BlockCustom(Material.GOURD, MapColor.LIME, SoundType.WOOD).setRegistryName("generic_object").setUnlocalizedName("genericObject").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block sburbMachine = new BlockSburbMachine().setRegistryName("sburb_machine");
	public static Block crockerMachine = new BlockCrockerMachine().setRegistryName("crocker_machine");
	public static Block blockComputerOff = new BlockComputerOff().setRegistryName("computer_standard");
	public static Block blockComputerOn = new BlockComputerOn().setRegistryName("computer_standard_on");
	public static Block blockLaptopOff = new BlockVanityLaptopOff().setRegistryName("vanity_laptop");
	public static Block blockLaptopOn = new BlockVanityLaptopOn().setRegistryName("vanity_laptop_on");
	public static Block transportalizer = new BlockTransportalizer().setRegistryName("transportalizer");
	public static Block blockGoldSeeds = new BlockGoldSeeds().setRegistryName("gold_seeds");
	public static Block returnNode = new BlockReturnNode().setRegistryName("return_node");
	public static Block gate = new BlockGate().setRegistryName("gate");
	public static Block glowingMushroom = new BlockGlowingMushroom().setRegistryName("glowing_mushroom");
	public static Block glowingLog = new BlockGlowingLog().setRegistryName("glowing_log");
	public static Block glowingPlanks = new BlockCustom(Material.WOOD, MapColor.LIGHT_BLUE, SoundType.WOOD).setFireInfo(5, 20).setRegistryName("glowing_planks").setUnlocalizedName("glowingPlanks").setLightLevel(0.5F).setHardness(2.0F).setResistance(5.0F).setCreativeTab(TabMinestuck.instance);
	public static Block stone = new BlockMinestuckStone().setRegistryName("stone");
	public static Block coarseStoneStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE)).setRegistryName("coarse_stone_stairs").setUnlocalizedName("stairsMinestuck.coarse");
	public static Block shadeBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK)).setRegistryName("shade_brick_stairs").setUnlocalizedName("stairsMinestuck.shadeBrick");
	public static Block frostBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK)).setRegistryName("frost_brick_stairs").setUnlocalizedName("stairsMinestuck.frostBrick");
	public static Block castIronStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.CAST_IRON)).setRegistryName("cast_iron_stairs").setUnlocalizedName("stairsMinestuck.castIron");
	public static Block log = new BlockMinestuckLog().setRegistryName("log");
	public static Block endLog = new BlockEndLog().setRegistryName("end_log");
	public static Block leaves1 = new BlockMinestuckLeaves1().setRegistryName("leaves");
	public static Block planks = new BlockMinestuckPlanks().setRegistryName("planks");
	public static Block aspectSapling = new BlockAspectSapling().setRegistryName("aspect_sapling");
	public static Block rainbowSapling = new BlockRainbowSapling().setRegistryName("rainbow_sapling");
	public static Block aspectLog1 = new BlockAspectLog().setRegistryName("aspect_log_1");
	public static Block aspectLog2 = new BlockAspectLog2().setRegistryName("aspect_log_2");
	public static Block aspectLog3 = new BlockAspectLog3().setRegistryName("aspect_log_3");
	public static Block woodenCactus = new BlockCactusSpecial(SoundType.WOOD, "axe").setRegistryName("wooden_cactus").setHardness(1.0F).setResistance(2.5F).setUnlocalizedName("woodenCactus");
	public static Block sugarCube = new BlockCustom(Material.SAND, MapColor.SNOW, SoundType.SAND).setRegistryName("sugar_cube").setUnlocalizedName("sugarCube").setHardness(0.4F).setCreativeTab(TabMinestuck.instance);
	public static Block rabbitSpawner = new BlockMobSpawner().setRegistryName("rabbit_spawner").setUnlocalizedName("rabbitSpawner");
	public static Block appleCake = new BlockSimpleCake(2, 0.5F, null).setRegistryName("apple_cake").setUnlocalizedName("appleCake");
	public static Block blueCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 150, 0))).setRegistryName("blue_cake").setUnlocalizedName("blueCake");
	public static Block coldCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1))).setRegistryName("cold_cake").setUnlocalizedName("coldCake");
	public static Block redCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.heal(1)).setRegistryName("red_cake").setUnlocalizedName("redCake");
	public static Block hotCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.setFire(4)).setRegistryName("hot_cake").setUnlocalizedName("hotCake");
	public static Block reverseCake = new BlockSimpleCake(2, 0.1F, null).setRegistryName("reverse_cake").setUnlocalizedName("cake");
	
	//Ores
	public static Block oreCruxite = (BlockCruxiteOre) new BlockCruxiteOre().setRegistryName("ore_cruxite");
	public static BlockUraniumOre oreUranium = (BlockUraniumOre) new BlockUraniumOre().setRegistryName("ore_uranium").setUnlocalizedName("oreUranium");
	public static Block coalOreNetherrack = new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setRegistryName("coal_ore_netherrack").setUnlocalizedName("oreCoal");
	public static Block ironOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone").setUnlocalizedName("oreIron");
	public static Block ironOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone_red").setUnlocalizedName("oreIron");
	public static Block goldOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone").setUnlocalizedName("oreGold");
	public static Block goldOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone_red").setUnlocalizedName("oreGold");
	
	public static Block uraniumCooker = new BlockUraniumCooker().setRegistryName("uranium_cooker").setUnlocalizedName("uraniumCooker");

	public static Block primedTnt = new BlockTNTSpecial(true, false, false).setRegistryName("primed_tnt").setUnlocalizedName("primedTnt");
	public static Block unstableTnt = new BlockTNTSpecial(false, true, false).setRegistryName("unstable_tnt").setUnlocalizedName("unstableTnt");
	public static Block instantTnt = new BlockTNTSpecial(false, false, true).setRegistryName("instant_tnt").setUnlocalizedName("instantTnt");
	public static Block woodenExplosiveButton = new BlockButtonSpecial(true, true).setRegistryName("wooden_button_explosive").setUnlocalizedName("buttonTnt");
	public static Block stoneExplosiveButton = new BlockButtonSpecial(false, true).setRegistryName("stone_button_explosive").setUnlocalizedName("buttonTnt");
	
	public static Block layeredSand = new BlockLayered(Blocks.SAND.getDefaultState()).setRegistryName("layered_sand").setUnlocalizedName("layeredSand");
	public static Block glowystoneWire = new BlockGlowystoneWire().setRegistryName("glowystone_wire").setUnlocalizedName("glowystoneWire");
	
	public static Fluid fluidOil = createFluid("oil", new ResourceLocation("minestuck", "blocks/oil_still"), new ResourceLocation("minestuck", "blocks/oil_flowing"), "tile.oil");
	public static Fluid fluidBlood = createFluid("blood", new ResourceLocation("minestuck", "blocks/blood_still"), new ResourceLocation("minestuck", "blocks/blood_flowing"), "tile.blood");
	public static Fluid fluidBrainJuice = createFluid("brain_juice", new ResourceLocation("minestuck", "blocks/brain_juice_still"), new ResourceLocation("minestuck", "blocks/brain_juice_flowing"), "tile.brainJuice");
	public static Fluid fluidWatercolors = createFluid("watercolors", new ResourceLocation("minestuck", "blocks/watercolors_still"), new ResourceLocation("minestuck", "blocks/watercolors_flowing"), "tile.watercolors");
	
	public static Block blockOil = new BlockFluidClassic(fluidOil, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.0, 0.0, 0.0);
		}
	}.setRegistryName("block_oil").setUnlocalizedName("oil").setLightOpacity(2);
	
	public static Block blockBlood = new BlockFluidClassic(fluidBlood, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.8, 0.0, 0.0);
		}
	}.setRegistryName("block_blood").setUnlocalizedName("blood").setLightOpacity(1);
	
	public static Block blockBrainJuice = new BlockFluidClassic(fluidBrainJuice, Material.WATER){
		@SideOnly (Side.CLIENT)
		@Override
		public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
		{
			return new Vec3d(0.55, 0.25, 0.7);
		}
	}.setRegistryName("block_brain_juice").setUnlocalizedName("brainJuice").setLightOpacity(1);
	
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
	}.setRegistryName("block_watercolors").setUnlocalizedName("watercolors").setLightOpacity(1);

	public static Block[] liquidGrists;
	public static Fluid[] gristFluids;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		//blocks
		final Block[] blocks = {chessTile, coloredDirt, layeredSand, stone, sugarCube,
				log, leaves1, planks, aspectSapling, rainbowSapling, aspectLog1, aspectLog2, aspectLog3, glowingLog, glowingPlanks, glowingMushroom, woodenCactus,
				oreCruxite, oreUranium, coalOreNetherrack, ironOreSandstone, ironOreSandstoneRed, goldOreSandstone, goldOreSandstoneRed,
				cruxiteBlock, genericObject,
				coarseStoneStairs, shadeBrickStairs, frostBrickStairs, castIronStairs,
				skaiaPortal, returnNode, gate,
				sburbMachine, crockerMachine, transportalizer, uraniumCooker,
				blockComputerOff, blockComputerOn, blockLaptopOff, blockLaptopOn,
				blockGoldSeeds, glowystoneWire,
				appleCake, blueCake, coldCake, redCake, hotCake, reverseCake,
				primedTnt, unstableTnt, instantTnt, woodenExplosiveButton, stoneExplosiveButton,
				blockOil, blockBlood, blockBrainJuice, blockWatercolors,
				rabbitSpawner};
		
		for(Block block : blocks)
			registry.register(block);
		
		//fluids
		
		
		/*liquidGrists = new Block[GristType.allGrists];
		gristFluids = new Fluid[GristType.allGrists];
		for(GristType grist : GristType.values()) {
			gristFluids[grist.getId()] = new Fluid(grist.getName(), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Still"), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Flowing"));
			FluidRegistry.registerFluid(gristFluids[grist.getId()]);
			liquidGrists[grist.getId()] = GameRegistry.register(new BlockFluidGrist(gristFluids[grist.getId()], Material.WATER).setRegistryName("liquid_" + grist.getName())).setUnlocalizedName("liquid_" + grist.getName());
		}*/

		cruxiteBlock.setHarvestLevel("pickaxe", 0);
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
}