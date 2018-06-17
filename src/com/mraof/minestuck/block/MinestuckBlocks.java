package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
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
	public static Block chessTile = new BlockChessTile();
	public static Block skaiaPortal = new BlockSkaiaPortal(Material.PORTAL);
	
	public static Block coloredDirt = new BlockColoredDirt().setUnlocalizedName("coloredDirt").setHardness(0.5F);
	public static Block cruxiteBlock = new Block(Material.ROCK, MapColor.LIGHT_BLUE).setUnlocalizedName("cruxiteBlock").setHardness(3.0F).setCreativeTab(TabMinestuck.instance);
	public static Block genericObject = new BlockCustom(Material.GOURD, MapColor.LIME, SoundType.WOOD).setUnlocalizedName("genericObject").setHardness(1.0F).setCreativeTab(TabMinestuck.instance);
	public static Block sburbMachine = new BlockSburbMachine();
	public static Block crockerMachine = new BlockCrockerMachine();
	public static Block blockComputerOff = new BlockComputerOff();
	public static Block blockComputerOn = new BlockComputerOn();
	public static Block transportalizer = new BlockTransportalizer();
	
	public static Block punchDesignix = new BlockPunchDesignix();
	public static BlockTotemLathe totemlathe[] = BlockTotemLathe.createBlocks();
	public static BlockAlchemiter[] alchemiter = BlockAlchemiter.createBlocks();
	public static Block cruxtruder = new BlockCruxtruder();
	public static Block cruxtruderLid = new BlockCruxtruderLid();
	
	public static Block blockCruxiteDowel = new BlockCruxtiteDowel();
	public static Block blockGoldSeeds = new BlockGoldSeeds();
	public static Block returnNode = new BlockReturnNode();
	public static Block gate = new BlockGate();
	public static Block glowingMushroom = new BlockGlowingMushroom();
	public static Block glowingLog = new BlockGlowingLog();
	public static Block glowingPlanks = new BlockCustom(Material.WOOD, MapColor.LIGHT_BLUE, SoundType.WOOD).setFireInfo(5, 20).setUnlocalizedName("glowingPlanks").setLightLevel(0.5F).setHardness(2.0F).setResistance(5.0F).setCreativeTab(TabMinestuck.instance);
	public static Block stone = new BlockMinestuckStone();
	public static Block coarseStoneStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.COARSE)).setUnlocalizedName("stairsMinestuck.coarse");
	public static Block shadeBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.SHADE_BRICK)).setUnlocalizedName("stairsMinestuck.shadeBrick");
	public static Block frostBrickStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.FROST_BRICK)).setUnlocalizedName("stairsMinestuck.frostBrick");
	public static Block castIronStairs = new BlockMinestuckStairs(stone.getDefaultState().withProperty(BlockMinestuckStone.VARIANT, BlockMinestuckStone.BlockType.CAST_IRON)).setUnlocalizedName("stairsMinestuck.castIron");
	public static Block log = new BlockMinestuckLog();
	public static Block woodenCactus = new BlockCactusSpecial(SoundType.WOOD, "axe").setHardness(1.0F).setResistance(2.5F).setUnlocalizedName("woodenCactus");
	public static Block sugarCube = new BlockCustom(Material.SAND, MapColor.SNOW, SoundType.SAND).setUnlocalizedName("sugarCube").setHardness(0.4F).setCreativeTab(TabMinestuck.instance);
	public static Block rabbitSpawner = new BlockMobSpawner().setUnlocalizedName("rabbitSpawner");
	public static Block appleCake = new BlockSimpleCake(2, 0.5F, null).setUnlocalizedName("appleCake");
	public static Block blueCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 150, 0))).setUnlocalizedName("blueCake");
	public static Block coldCake = new BlockSimpleCake(2, 0.3F, (EntityPlayer player) -> {player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 200, 1));}).setUnlocalizedName("coldCake");
	public static Block redCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.heal(1)).setUnlocalizedName("redCake");
	public static Block hotCake = new BlockSimpleCake(2, 0.1F, (EntityPlayer player) -> player.setFire(4)).setUnlocalizedName("hotCake");
	public static Block reverseCake = new BlockSimpleCake(2, 0.1F, null).setUnlocalizedName("cake");
	
	//Ores
	public static Block oreCruxite = new BlockCruxiteOre();
	public static BlockUraniumOre oreUranium = (BlockUraniumOre) new BlockUraniumOre().setUnlocalizedName("oreUranium");
	public static Block coalOreNetherrack = new BlockVanillaOre(BlockVanillaOre.OreType.COAL).setUnlocalizedName("oreCoal");
	public static Block ironOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron");
	public static Block ironOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.IRON).setUnlocalizedName("oreIron");
	public static Block goldOreSandstone = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold");
	public static Block goldOreSandstoneRed = new BlockVanillaOre(BlockVanillaOre.OreType.GOLD).setUnlocalizedName("oreGold");
	
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
	
	public static Block blockOil = new BlockFluidClassic(fluidOil, Material.WATER).setUnlocalizedName("oil");
	public static Block blockBlood = new BlockFluidClassic(fluidBlood, Material.WATER).setUnlocalizedName("blood");
	public static Block blockBrainJuice = new BlockFluidClassic(fluidBrainJuice, Material.WATER).setUnlocalizedName("brainJuice");

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
		registry.register(glowingLog.setRegistryName("glowing_log"));
		registry.register(glowingPlanks.setRegistryName("glowing_planks"));
		registry.register(glowingMushroom.setRegistryName("glowing_mushroom"));
		registry.register(woodenCactus.setRegistryName("wooden_cactus"));
		
		registry.register(oreCruxite.setRegistryName("ore_cruxite"));
		registry.register(oreUranium.setRegistryName("ore_uranium"));
		registry.register(coalOreNetherrack.setRegistryName("coal_ore_netherrack"));
		registry.register(ironOreSandstone.setRegistryName("iron_ore_sandstone"));
		registry.register(ironOreSandstoneRed.setRegistryName("iron_ore_sandstone_red"));
		registry.register(goldOreSandstone.setRegistryName("gold_ore_sandstone"));
		registry.register(goldOreSandstoneRed.setRegistryName("gold_ore_sandstone_red"));
		
		registry.register(cruxiteBlock.setRegistryName("cruxite_block"));
		registry.register(genericObject.setRegistryName("generic_object"));
		registry.register(blockCruxiteDowel.setRegistryName("cruxite_dowel"));
		
		registry.register(coarseStoneStairs.setRegistryName("coarse_stone_stairs"));
		registry.register(shadeBrickStairs.setRegistryName("shade_brick_stairs"));
		registry.register(frostBrickStairs.setRegistryName("frost_brick_stairs"));
		registry.register(castIronStairs.setRegistryName("cast_iron_stairs"));
		
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
		
		registry.register(blockComputerOff.setRegistryName("computer_standard"));
		registry.register(blockComputerOn.setRegistryName("computer_standard_on"));
		
		registry.register(blockGoldSeeds.setRegistryName("gold_seeds"));
		registry.register(glowystoneWire.setRegistryName("glowystone_wire"));
		
		registry.register(appleCake.setRegistryName("apple_cake"));
		registry.register(blueCake.setRegistryName("blue_cake"));
		registry.register(coldCake.setRegistryName("cold_cake"));
		registry.register(redCake.setRegistryName("red_cake"));
		registry.register(hotCake.setRegistryName("hot_cake"));
		registry.register(reverseCake.setRegistryName("reverse_cake"));
		
		registry.register(primedTnt.setRegistryName("primed_tnt"));
		registry.register(unstableTnt.setRegistryName("unstable_tnt"));
		registry.register(instantTnt.setRegistryName("instant_tnt"));
		registry.register(woodenExplosiveButton.setRegistryName("wooden_button_explosive"));
		registry.register(stoneExplosiveButton.setRegistryName("stone_button_explosive"));
		
		registry.register(blockOil.setRegistryName("block_oil"));
		registry.register(blockBlood.setRegistryName("block_blood"));
		registry.register(blockBrainJuice.setRegistryName("block_brain_juice"));
		
		registry.register(rabbitSpawner.setRegistryName("rabbit_spawner"));
		
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