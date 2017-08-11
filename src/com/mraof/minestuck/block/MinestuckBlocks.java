package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class MinestuckBlocks
{
	
	//Blocks
	public static Block chessTile;
	public static BlockColoredDirt coloredDirt;
	public static Block skaiaPortal;
	public static BlockCruxiteOre oreCruxite;
	public static Block cruxiteBlock;
	public static Block genericObject;
	public static Block sburbMachine;
	public static Block crockerMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	public static Block transportalizer;
	public static BlockGoldSeeds blockGoldSeeds;
	public static Block returnNode;
	public static Block gate;
	public static BlockGlowingMushroom glowingMushroom;
	public static Block glowingLog;
	public static Block glowingPlanks;
	public static Block stone;
	public static Block coarseStoneStairs;
	public static Block shadeBrickStairs;
	public static Block frostBrickStairs;
	public static Block castIronStairs;
	public static Block log;
	public static Block woodenCactus;
	
	public static Block coalOreNetherrack;
	public static Block ironOreSandstone;
	public static Block ironOreSandstoneRed;
	public static Block goldOreSandstone;
	public static Block goldOreSandstoneRed;
	
	public static Block primedTnt;
	public static Block unstableTnt;
	public static Block instantTnt;
	public static Block woodenExplosiveButton;
	public static Block stoneExplosiveButton;
	
	public static Block blockOil;
	public static Block blockBlood;
	public static Block blockBrainJuice;
	public static Block layeredSand;
	public static Block glowystoneWire;

	public static Block[] liquidGrists;
	
	public static Fluid fluidOil;
	public static Fluid fluidBlood;
	public static Fluid fluidBrainJuice;

	public static Fluid[] gristFluids;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		//blocks
		chessTile = register(registry, new BlockChessTile().setRegistryName("chess_tile"));
		skaiaPortal = register(registry, new BlockSkaiaPortal(Material.PORTAL).setRegistryName("skaia_portal"));
		
		oreCruxite = (BlockCruxiteOre) register(registry, new BlockCruxiteOre().setRegistryName("ore_cruxite"));
		coalOreNetherrack = register(registry, new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setRegistryName("coal_ore_netherrack")).setUnlocalizedName("oreCoal");
		ironOreSandstone = register(registry, new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone")).setUnlocalizedName("oreIron");
		ironOreSandstoneRed = register(registry, new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone_red")).setUnlocalizedName("oreIron");
		goldOreSandstone = register(registry, new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone")).setUnlocalizedName("oreGold");
		goldOreSandstoneRed = register(registry, new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone_red")).setUnlocalizedName("oreGold");
		
		cruxiteBlock = register(registry, new Block(Material.ROCK, MapColor.LIGHT_BLUE).setRegistryName("cruxite_block")).setUnlocalizedName("cruxiteBlock").setHardness(3.0F).setCreativeTab(MinestuckItems.tabMinestuck);
		genericObject = register(registry, new BlockCustom(Material.GOURD, MapColor.LIME, SoundType.WOOD).setRegistryName("generic_object")).setUnlocalizedName("genericObject").setHardness(1.0F).setCreativeTab(MinestuckItems.tabMinestuck);
		sburbMachine = register(registry, new BlockSburbMachine().setRegistryName("sburb_machine"));
		crockerMachine = register(registry, new BlockCrockerMachine().setRegistryName("crocker_machine"));
		blockComputerOff = register(registry, new BlockComputerOff().setRegistryName("computer_standard"));
		blockComputerOn = register(registry, new BlockComputerOn().setRegistryName("computer_standard_on"));
		transportalizer = register(registry, new BlockTransportalizer().setRegistryName("transportalizer"));
		blockGoldSeeds = (BlockGoldSeeds) register(registry, new BlockGoldSeeds().setRegistryName("gold_seeds"));
		returnNode = register(registry, new BlockReturnNode().setRegistryName("return_node"));
		gate = register(registry, new BlockGate().setRegistryName("gate"));
		
		layeredSand = register(registry, new BlockLayered(Blocks.SAND.getDefaultState()).setRegistryName("layered_sand")).setUnlocalizedName("layeredSand");
		glowystoneWire = register(registry, new BlockGlowystoneWire().setRegistryName("glowystone_wire").setUnlocalizedName("glowystoneWire"));
		coloredDirt = (BlockColoredDirt) register(registry, new BlockColoredDirt().setRegistryName("colored_dirt")).setUnlocalizedName("coloredDirt").setHardness(0.5F);
		glowingMushroom = (BlockGlowingMushroom) register(registry, new BlockGlowingMushroom().setRegistryName("glowing_mushroom"));
		glowingLog = register(registry, new BlockGlowingLog().setRegistryName("glowing_log"));
		glowingPlanks = register(registry, new BlockCustom(Material.WOOD, MapColor.LIGHT_BLUE, SoundType.WOOD).setFireInfo(5, 20).setRegistryName("glowing_planks")).setUnlocalizedName("glowingPlanks").setLightLevel(0.5F).setHardness(2.0F).setResistance(5.0F).setCreativeTab(MinestuckItems.tabMinestuck);
		stone = register(registry, new BlockMinestuckStone().setRegistryName("stone"));	//Full name will be minestuck:stone and because of that not produce any collisions
		coarseStoneStairs = register(registry, new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE)).setRegistryName("coarse_stone_stairs")).setUnlocalizedName("stairsMinestuck.coarse");
		shadeBrickStairs = register(registry, new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK)).setRegistryName("shade_brick_stairs")).setUnlocalizedName("stairsMinestuck.shadeBrick");
		frostBrickStairs = register(registry, new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK)).setRegistryName("frost_brick_stairs")).setUnlocalizedName("stairsMinestuck.frostBrick");
		castIronStairs = register(registry, new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.CAST_IRON)).setRegistryName("cast_iron_stairs")).setUnlocalizedName("stairsMinestuck.castIron");
		log = register(registry, new BlockMinestuckLog().setRegistryName("log"));
		woodenCactus = register(registry, new BlockCactusSpecial(SoundType.WOOD, "axe").setRegistryName("wooden_cactus")).setHardness(1.0F).setResistance(2.5F).setUnlocalizedName("woodenCactus");
		
		primedTnt = register(registry, new BlockTNTSpecial(true, false, false).setRegistryName("primed_tnt")).setUnlocalizedName("primedTnt");
		unstableTnt = register(registry, new BlockTNTSpecial(false, true, false).setRegistryName("unstable_tnt")).setUnlocalizedName("unstableTnt");
		instantTnt = register(registry, new BlockTNTSpecial(false, false, true).setRegistryName("instant_tnt")).setUnlocalizedName("instantTnt");
		woodenExplosiveButton = register(registry, new BlockButtonSpecial(true, true).setRegistryName("wooden_button_explosive")).setUnlocalizedName("buttonTnt");
		stoneExplosiveButton = register(registry, new BlockButtonSpecial(false, true).setRegistryName("stone_button_explosive")).setUnlocalizedName("buttonTnt");
		//fluids
		fluidOil = new Fluid("Oil", new ResourceLocation("minestuck", "blocks/oil_still"), new ResourceLocation("minestuck", "blocks/oil_flowing"));
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood", new ResourceLocation("minestuck", "blocks/blood_still"), new ResourceLocation("minestuck", "blocks/blood_flowing"));
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice", new ResourceLocation("minestuck", "blocks/brain_juice_still"), new ResourceLocation("minestuck", "blocks/brain_juice_flowing"));
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = register(registry, new BlockFluidClassic(fluidOil, Material.WATER).setRegistryName("block_oil")).setUnlocalizedName("oil");
		blockBlood = register(registry, new BlockFluidClassic(fluidBlood, Material.WATER).setRegistryName("block_blood")).setUnlocalizedName("blood");
		blockBrainJuice = register(registry, new BlockFluidClassic(fluidBrainJuice, Material.WATER).setRegistryName("block_brain_juice")).setUnlocalizedName("brainJuice");
		
		/*liquidGrists = new Block[GristType.allGrists];
		gristFluids = new Fluid[GristType.allGrists];
		for(GristType grist : GristType.values()) {
			gristFluids[grist.ordinal()] = new Fluid(grist.getName(), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Still"), new ResourceLocation("minestuck", "blocks/Liquid" + grist.getName() + "Flowing"));
			FluidRegistry.registerFluid(gristFluids[grist.ordinal()]);
			liquidGrists[grist.ordinal()] = GameRegistry.register(new BlockFluidGrist(gristFluids[grist.ordinal()], Material.WATER).setRegistryName("liquid_" + grist.getName())).setUnlocalizedName("liquid_" + grist.getName());
		}*/
		
		cruxiteBlock.setHarvestLevel("pickaxe", 0);
		
		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
	}
	
	private static Block register(IForgeRegistry<Block> registry, Block block) //Because the new registry method doesn't return the block
	{
		registry.register(block);
		return block;
	}
}