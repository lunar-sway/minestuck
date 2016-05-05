package com.mraof.minestuck.block;

import com.mraof.minestuck.item.block.ItemBlockLayered;
import com.mraof.minestuck.item.block.ItemChessTile;
import com.mraof.minestuck.item.block.ItemColoredDirt;
import com.mraof.minestuck.item.block.ItemMachine;
import com.mraof.minestuck.item.block.ItemOreCruxite;
import com.mraof.minestuck.item.block.ItemStorageBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinestuckBlocks
{
	
	//Blocks
	public static Block chessTile;
	public static BlockColoredDirt coloredDirt;
	public static Block skaiaPortal;
	public static OreCruxite oreCruxite;
	public static Block blockStorage;
	public static Block blockMachine;
	public static Block blockComputerOn;
	public static Block blockComputerOff;
	public static Block transportalizer;
	public static BlockGoldSeeds blockGoldSeeds;
	public static Block returnNode;
	public static Block gate;
	public static BlockGlowingMushroom glowingMushroom;
	
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
		chessTile = GameRegistry.registerBlock(new BlockChessTile(), ItemChessTile.class, "chess_tile");
		skaiaPortal = GameRegistry.registerBlock(new BlockSkaiaPortal(Material.portal), "skaia_portal");
		
		oreCruxite = (OreCruxite) GameRegistry.registerBlock(new OreCruxite(), ItemOreCruxite.class, "ore_cruxite");
		coalOreNetherrack = GameRegistry.registerBlock(new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setUnlocalizedName("oreCoal"), "coal_ore_netherrack");
		ironOreSandstone = GameRegistry.registerBlock(new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron"), "iron_ore_sandstone");
		ironOreSandstoneRed = GameRegistry.registerBlock(new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron"), "iron_ore_sandstone_red");
		goldOreSandstone = GameRegistry.registerBlock(new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold"), "gold_ore_sandstone");
		goldOreSandstoneRed = GameRegistry.registerBlock(new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold"), "gold_ore_sandstone_red");
		
		layeredSand = GameRegistry.registerBlock(new BlockLayered(Blocks.sand), ItemBlockLayered.class, "layered_sand").setUnlocalizedName("layeredSand");
		coloredDirt = (BlockColoredDirt) GameRegistry.registerBlock(new BlockColoredDirt(), ItemColoredDirt.class, "colored_dirt").setUnlocalizedName("coloredDirt").setHardness(0.5F);
		blockStorage = GameRegistry.registerBlock(new BlockStorage(),ItemStorageBlock.class,"storage_block");
		blockMachine = GameRegistry.registerBlock(new BlockMachine(), ItemMachine.class,"machine_block");
		blockComputerOff = GameRegistry.registerBlock(new BlockComputerOff(),"computer_standard");
		blockComputerOn = GameRegistry.registerBlock(new BlockComputerOn(), null, "computer_standard_on");
		transportalizer = GameRegistry.registerBlock(new BlockTransportalizer(), "transportalizer");
		blockGoldSeeds = (BlockGoldSeeds) GameRegistry.registerBlock(new BlockGoldSeeds(), null, "gold_seeds");
		returnNode = GameRegistry.registerBlock(new BlockReturnNode(), null, "return_node");
		gate = GameRegistry.registerBlock(new BlockGate(), null, "gate");
		glowingMushroom = (BlockGlowingMushroom) GameRegistry.registerBlock(new BlockGlowingMushroom(), "glowing_mushroom");
		primedTnt = GameRegistry.registerBlock(new BlockTNTSpecial(true, false, false), "primed_tnt").setUnlocalizedName("primedTnt");
		unstableTnt = GameRegistry.registerBlock(new BlockTNTSpecial(false, true, false), "unstable_tnt").setUnlocalizedName("unstableTnt");
		instantTnt = GameRegistry.registerBlock(new BlockTNTSpecial(false, false, true), "instant_tnt").setUnlocalizedName("instantTnt");
		woodenExplosiveButton = GameRegistry.registerBlock(new BlockButtonSpecial(true, true), "wooden_button_explosive").setUnlocalizedName("buttonTnt");
		stoneExplosiveButton = GameRegistry.registerBlock(new BlockButtonSpecial(false, true), "stone_button_explosive").setUnlocalizedName("buttonTnt");
		//fluids
		fluidOil = new Fluid("Oil", new ResourceLocation("minestuck", "blocks/OilStill"), new ResourceLocation("minestuck", "blocks/OilFlowing"));
		FluidRegistry.registerFluid(fluidOil);
		fluidBlood = new Fluid("Blood", new ResourceLocation("minestuck", "blocks/BloodStill"), new ResourceLocation("minestuck", "blocks/BloodFlowing"));
		FluidRegistry.registerFluid(fluidBlood);
		fluidBrainJuice = new Fluid("BrainJuice", new ResourceLocation("minestuck", "blocks/BrainJuiceStill"), new ResourceLocation("minestuck", "blocks/BrainJuiceFlowing"));
		FluidRegistry.registerFluid(fluidBrainJuice);
		blockOil = GameRegistry.registerBlock(new BlockFluid(fluidOil, Material.water).setUnlocalizedName("oil"), null, "block_oil");
		blockBlood = GameRegistry.registerBlock(new BlockFluid(fluidBlood, Material.water).setUnlocalizedName("blood"), null, "block_blood");
		blockBrainJuice = GameRegistry.registerBlock(new BlockFluid(fluidBrainJuice, Material.water).setUnlocalizedName("brainJuice"), null, "block_brain_juice");
		
		fluidOil.setUnlocalizedName(blockOil.getUnlocalizedName());
		fluidBlood.setUnlocalizedName(blockBlood.getUnlocalizedName());
		fluidBrainJuice.setUnlocalizedName(blockBrainJuice.getUnlocalizedName());
	}
}