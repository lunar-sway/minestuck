package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	
	public static Fluid fluidOil;
	public static Fluid fluidBlood;
	public static Fluid fluidBrainJuice;
	
	public static void registerBlocks()
	{
		//blocks
		chessTile = GameRegistry.register(new BlockChessTile().setRegistryName("chess_tile"));
		skaiaPortal = GameRegistry.register(new BlockSkaiaPortal(Material.portal).setRegistryName("skaia_portal"));
		
		oreCruxite = (BlockCruxiteOre) GameRegistry.register(new BlockCruxiteOre().setRegistryName("ore_cruxite"));
		coalOreNetherrack = GameRegistry.register(new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setRegistryName("coal_ore_netherrack")).setUnlocalizedName("oreCoal");
		ironOreSandstone = GameRegistry.register(new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone")).setUnlocalizedName("oreIron");
		ironOreSandstoneRed = GameRegistry.register(new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setRegistryName("iron_ore_sandstone_red")).setUnlocalizedName("oreIron");
		goldOreSandstone = GameRegistry.register(new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone")).setUnlocalizedName("oreGold");
		goldOreSandstoneRed = GameRegistry.register(new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setRegistryName("gold_ore_sandstone_red")).setUnlocalizedName("oreGold");
		
		cruxiteBlock = GameRegistry.register(new Block(Material.rock, MapColor.lightBlueColor).setRegistryName("cruxite_block")).setUnlocalizedName("cruxiteBlock").setHardness(3.0F).setCreativeTab(Minestuck.tabMinestuck);
		genericObject = GameRegistry.register(new BlockSound(Material.gourd, MapColor.limeColor, SoundType.WOOD).setRegistryName("generic_object")).setUnlocalizedName("genericObject").setHardness(1.0F).setCreativeTab(Minestuck.tabMinestuck);
		sburbMachine = GameRegistry.register(new BlockSburbMachine().setRegistryName("sburb_machine"));
		crockerMachine = GameRegistry.register(new BlockCrockerMachine().setRegistryName("crocker_machine"));
		blockComputerOff = GameRegistry.register(new BlockComputerOff().setRegistryName("computer_standard"));
		blockComputerOn = GameRegistry.register(new BlockComputerOn().setRegistryName("computer_standard_on"));
		transportalizer = GameRegistry.register(new BlockTransportalizer().setRegistryName("transportalizer"));
		blockGoldSeeds = (BlockGoldSeeds) GameRegistry.register(new BlockGoldSeeds().setRegistryName("gold_seeds"));
		returnNode = GameRegistry.register(new BlockReturnNode().setRegistryName("return_node"));
		gate = GameRegistry.register(new BlockGate().setRegistryName("gate"));
		
		layeredSand = GameRegistry.register(new BlockLayered(Blocks.sand.getDefaultState()).setRegistryName("layered_sand")).setUnlocalizedName("layeredSand");
		coloredDirt = (BlockColoredDirt) GameRegistry.register(new BlockColoredDirt().setRegistryName("colored_dirt")).setUnlocalizedName("coloredDirt").setHardness(0.5F);
		glowingMushroom = (BlockGlowingMushroom) GameRegistry.register(new BlockGlowingMushroom().setRegistryName("glowing_mushroom"));
		glowingLog = GameRegistry.register(new BlockGlowingLog().setRegistryName("glowing_log"));
		
		primedTnt = GameRegistry.register(new BlockTNTSpecial(true, false, false).setRegistryName("primed_tnt")).setUnlocalizedName("primedTnt");
		unstableTnt = GameRegistry.register(new BlockTNTSpecial(false, true, false).setRegistryName("unstable_tnt")).setUnlocalizedName("unstableTnt");
		instantTnt = GameRegistry.register(new BlockTNTSpecial(false, false, true).setRegistryName("instant_tnt")).setUnlocalizedName("instantTnt");
		woodenExplosiveButton = GameRegistry.register(new BlockButtonSpecial(true, true).setRegistryName("wooden_button_explosive")).setUnlocalizedName("buttonTnt");
		stoneExplosiveButton = GameRegistry.register(new BlockButtonSpecial(false, true).setRegistryName("stone_button_explosive")).setUnlocalizedName("buttonTnt");
		//fluids
		fluidOil = new Fluid("Oil", new ResourceLocation("minestuck", "blocks/OilStill"), new ResourceLocation("minestuck", "blocks/OilFlowing"));
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood", new ResourceLocation("minestuck", "blocks/BloodStill"), new ResourceLocation("minestuck", "blocks/BloodFlowing"));
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice", new ResourceLocation("minestuck", "blocks/BrainJuiceStill"), new ResourceLocation("minestuck", "blocks/BrainJuiceFlowing"));
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = GameRegistry.register(new BlockFluidClassic(fluidOil, Material.water).setRegistryName("block_oil")).setUnlocalizedName("oil");
		blockBlood = GameRegistry.register(new BlockFluidClassic(fluidBlood, Material.water).setRegistryName("block_blood")).setUnlocalizedName("blood");
		blockBrainJuice = GameRegistry.register(new BlockFluidClassic(fluidBrainJuice, Material.water).setRegistryName("block_brain_juice")).setUnlocalizedName("brainJuice");
		
		cruxiteBlock.setHarvestLevel("pickaxe", 0);
		
		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
	}
}