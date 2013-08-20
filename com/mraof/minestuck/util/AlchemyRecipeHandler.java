package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.LandAspectFrost;
import com.mraof.minestuck.world.gen.lands.LandAspectHeat;
import com.mraof.minestuck.world.gen.lands.LandHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class AlchemyRecipeHandler {
	
	private static HashMap recipeList;

	public static void registerVanillaRecipes() {
		
		//Set up Alchemiter recipes
		//Blocks
		GristRegistry.addGristConversion(new ItemStack(Block.stone), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.grass), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.dirt), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cobblestone), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.planks), false, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Block.sapling), false, new GristSet(new GristType[] {GristType.Build}, new int[] {16})); 
		GristRegistry.addGristConversion(new ItemStack(Block.bedrock), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Zillium}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.sand), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.gravel), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.gravel), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreGold), false, new GristSet(new GristType[] {GristType.Build, GristType.Gold}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreIron), false, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreCoal), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Block.leaves), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {1, 1}));		
		GristRegistry.addGristConversion(new ItemStack(Block.wood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Block.sponge), false, new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.glass), false, new GristSet(new GristType[] {GristType.Quartz}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreLapis), false, new GristSet(new GristType[] {GristType.Amethyst, GristType.Build}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.blockLapis), false, new GristSet(new GristType[] {GristType.Amethyst}, new int[] {36}));
		//GristRegistry.addGristConversion(new ItemStack(Block.dispenser), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Chalk}, new int[] {3, 4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.sandStone), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Block.music), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.railPowered), false, new GristSet(new GristType[] {GristType.Build, GristType.Gold}, new int[] {4, 32}));
		//GristRegistry.addGristConversion(new ItemStack(Block.railDetector), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {4, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pistonStickyBase), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Build, GristType.Garnet}, new int[] {4, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Block.web), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Block.tallGrass), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Block.deadBush), false, new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.pistonBase), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 1), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 2), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 3), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 4), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 5), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 6), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Amber}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 7), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 8), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 9), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 10), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 11), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 12), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 13), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 14), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet, GristType.Amber}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.cloth, 1, 15), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Block.plantYellow), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.plantRed), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Iodine}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.mushroomBrown), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Block.mushroomRed), false, new GristSet(new GristType[] {GristType.Ruby}, new int[] {2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 0), true, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 1), true, new GristSet(new GristType[] {GristType.Shale}, new int[] {2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 3), true, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 4), true, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {8, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 5), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 6), true, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {2, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneSingleSlab, 1, 7), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.woodSingleSlab), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.brick), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.bookShelf), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {16, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Block.tnt), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk, GristType.Shale}, new int[] {8, 8, 16}));
		GristRegistry.addGristConversion(new ItemStack(Block.cobblestoneMossy), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Build}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.obsidian), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Tar, GristType.Build}, new int[] {1, 1, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Block.torchWood), false, new GristSet(new GristType[] {GristType.Tar}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsWoodOak), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsCobblestone), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsStoneBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsNetherBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsSandStone), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsWoodSpruce), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsWoodBirch), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsWoodJungle), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stairsNetherQuartz), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.chest), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Block.workbench), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.furnaceIdle), false, new GristSet(new GristType[] {GristType.Tar, GristType.Build}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Block.ladder), false, new GristSet(new GristType[] {GristType.Build}, new int[] {7}));
		//GristRegistry.addGristConversion(new ItemStack(Block.rail), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {32, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.lever), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pressurePlateStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pressurePlatePlanks), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreRedstone), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.torchRedstoneIdle), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneButton), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 3}));
		GristRegistry.addGristConversion(new ItemStack(Block.snow), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Block.ice), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Block.blockSnow), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.cactus), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockClay), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {16}));
		//GristRegistry.addGristConversion(new ItemStack(Block.jukebox), false, new GristSet(new GristType[] {GristType.Build, GristType.Iodine, GristType.Diamond}, new int[] {4, 1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.fence), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Block.pumpkin), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.netherrack), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.slowSand), false, new GristSet(new GristType[] {GristType.Tar, GristType.Shale}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.glowStone), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {16, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pumpkinLantern), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Tar, GristType.Build}, new int[] {6, 2, 2, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.trapdoor), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.stoneBrick, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.stoneBrick, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {10, 6}));
		GristRegistry.addGristConversion(new ItemStack(Block.stoneBrick, 1, 2), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Iodine}, new int[] {10, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.stoneBrick, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {12, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.fenceIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Caulk}, new int[] {2, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.thinGlass), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Caulk}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.melon), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build}, new int[] {8, 8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.vine), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.fenceGate), false, new GristSet(new GristType[] {GristType.Build}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Block.mycelium), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby, GristType.Build}, new int[] {2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.waterlily), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.netherBrick), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Block.netherFence), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {12, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Block.enchantmentTable), false, new GristSet(new GristType[] {GristType.Shale, GristType.Chalk, GristType.Build, GristType.Tar}, new int[] {4, 4, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.whiteStone), false, new GristSet(new GristType[] {GristType.Shale, GristType.Chalk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.dragonEgg), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Zillium}, new int[] {64, 64, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.redstoneLampIdle), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk, GristType.Garnet}, new int[] {12, 12, 12}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreEmerald), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond, GristType.Build}, new int[] {8, 8, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.enderChest), false, new GristSet(new GristType[] {GristType.Build, GristType.Uranium, GristType.Tar}, new int[] {32, 32, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Block.tripWireSource), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockEmerald), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond}, new int[] {72, 72}));
		//GristRegistry.addGristConversion(new ItemStack(Block.beacon), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {256, 128, 128}));
		//GristRegistry.addGristConversion(new ItemStack(Block.cobblestoneWall, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {8, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.cobblestoneWall, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Iodine}, new int[] {8, 1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.woodenButton), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Block.anvil), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.chestTrapped), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pressurePlateGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.pressurePlateIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.daylightSensor), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber}, new int[] {4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockRedstone), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {36}));
		GristRegistry.addGristConversion(new ItemStack(Block.oreNetherQuartz), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble, GristType.Build}, new int[] {8, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.hopperBlock), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet, GristType.Build}, new int[] {6, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Block.blockNetherQuartz), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.railActivator), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk, GristType.Build}, new int[] {16, 16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Block.dropper), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Chalk}, new int[] {4, 4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Block.field_111039_cA), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {12, 8}));
		GristRegistry.addGristConversion(new ItemStack(Block.field_111038_cB), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {72}));
		GristRegistry.addGristConversion(new ItemStack(Block.field_111031_cC), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Block.field_111032_cD), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Block.field_111034_cE), false, new GristSet(new GristType[] {GristType.Tar}, new int[] {144}));
		GristRegistry.addGristConversion(new ItemStack(Block.silverfish, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.silverfish, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.silverfish, 1, 2), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Block.silverfish, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		
		//Items
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.flintAndSteel), false, new GristSet(new GristType[] {GristType.Rust, GristType.Shale}, new int[] {16, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.appleRed), false, new GristSet(new GristType[] {GristType.Amber, GristType.Shale}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.bow), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {3, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Item.arrow), false, new GristSet(new GristType[] {GristType.Shale, GristType.Build}, new int[] {1, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.coal, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Item.coal, 1, 1), true, new GristSet(new GristType[] {GristType.Tar, GristType.Amber}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.diamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Item.ingotIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Item.ingotGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {16}));
		//GristRegistry.addGristConversion(new ItemStack(Item.swordIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.swordWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.swordStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {1, 7}));
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.swordDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.stick), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bowlEmpty), false, new GristSet(new GristType[] {GristType.Build}, new int[] {3}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bowlSoup), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby, GristType.Build}, new int[] {2, 2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Item.swordGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.silk), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Item.feather), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Item.gunpowder), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.hoeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.hoeStone), false, new GristSet(new GristType[] {GristType.Shale, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.hoeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.hoeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.hoeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.seeds), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.wheat), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bread), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {10, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.helmetLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {10, 10}));
		//GristRegistry.addGristConversion(new ItemStack(Item.plateLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {16, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Item.legsLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {14, 14}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bootsLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Item.helmetChain), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {10, 10}));
		GristRegistry.addGristConversion(new ItemStack(Item.plateChain), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {16, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.legsChain), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {14, 14}));
		GristRegistry.addGristConversion(new ItemStack(Item.bootsChain), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {8, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.helmetIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Item.plateIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Item.legsIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bootsIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {64}));
		//GristRegistry.addGristConversion(new ItemStack(Item.helmetDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Item.plateDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Item.legsDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bootsDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {64}));
		//GristRegistry.addGristConversion(new ItemStack(Item.helmetGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Item.plateGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Item.legsGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bootsGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {64}));
		GristRegistry.addGristConversion(new ItemStack(Item.flint), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Item.porkRaw), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Item.porkCooked), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.painting), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.appleGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Amber, GristType.Shale}, new int[] {128, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.sign), false, new GristSet(new GristType[] {GristType.Build}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.doorWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {12}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bucketEmpty), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {48}));
		GristRegistry.addGristConversion(new ItemStack(Item.bucketWater), false, new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt}, new int[] {48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.bucketLava), false, new GristSet(new GristType[] {GristType.Rust, GristType.Tar}, new int[] {48, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Item.minecartEmpty), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Item.saddle), false, new GristSet(new GristType[] {GristType.Rust, GristType.Iodine, GristType.Chalk}, new int[] {16, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.doorIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {96}));
		GristRegistry.addGristConversion(new ItemStack(Item.redstone), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.snowball), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.boat), false, new GristSet(new GristType[] {GristType.Build}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Item.leather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.bucketMilk), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk}, new int[] {48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.brick), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.clay), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.reed), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.paper), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.book), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {14, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.slimeBall), false, new GristSet(new GristType[] {GristType.Caulk}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.minecartCrate), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {80, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.minecartPowered), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build, GristType.Shale}, new int[] {80, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.egg), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {2, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.compass), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet}, new int[] {64, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.fishingRod), false, new GristSet(new GristType[] {GristType.Build, GristType.Chalk}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pocketSundial), false, new GristSet(new GristType[] {GristType.Gold, GristType.Garnet}, new int[] {64, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.glowstone), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.fishRaw), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt}, new int[] {4, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.fishCooked), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Tar}, new int[] {4, 4, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 1), true, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 2), true, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 3), true, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 4), true, new GristSet(new GristType[] {GristType.Amethyst}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 5), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 6), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 7), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 8), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 9), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 10), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 11), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 12), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 13), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 14), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.dyePowder, 1, 15), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.bone), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {12}));
		//GristRegistry.addGristConversion(new ItemStack(Item.sugar), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.cake), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build, GristType.Ruby}, new int[] {4, 4, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.bed), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {12, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.redstoneRepeater), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.cookie), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {6, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Item.shears), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {32}));
		GristRegistry.addGristConversion(new ItemStack(Item.melon), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pumpkinSeeds), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.melonSeeds), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Item.beefRaw), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Item.beefCooked), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {12, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.chickenRaw), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Item.chickenCooked), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.rottenFlesh), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.enderPearl), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Diamond}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.blazeRod), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium}, new int[] {16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.ghastTear), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.goldNugget), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Item.netherStalkSeeds), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 0), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt}, new int[] {1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 1), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Chalk}, new int[] {1, 18, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 2), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine}, new int[] {1, 16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 3), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {1, 16, 8, 1, 8}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 4), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Iodine}, new int[] {1, 16, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 5), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Chalk}, new int[] {1, 16, 16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 6), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold}, new int[] {1, 16, 3, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 8), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 6, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 9), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Tar, GristType.Uranium}, new int[] {1, 8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 10), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine, GristType.Amber, GristType.Tar}, new int[] {1, 16, 8, 6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 12), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Iodine, GristType.Tar, GristType.Chalk}, new int[] {1, 16, 16, 7, 4, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 14), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 9, 1, 16, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 16), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.potion, 1, 32), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt}, new int[] {1, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Item.glassBottle), false, new GristSet(new GristType[] {GristType.Quartz}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Item.spiderEye), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.fermentedSpiderEye), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine, GristType.Tar}, new int[] {6, 4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.blazePowder), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.magmaCream), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {8, 1, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.brewingStand), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Build}, new int[] {16, 2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Item.cauldron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Item.eyeOfEnder), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Diamond}, new int[] {8, 9, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.speckledMelon), false, new GristSet(new GristType[] {GristType.Gold, GristType.Amber, GristType.Chalk}, new int[] {16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 61), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar, GristType.Uranium}, new int[] {2, 2, 2, 16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 59), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 50), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Sulfur, GristType.Chalk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 58), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Uranium, GristType.Diamond}, new int[] {2, 2, 2, 8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 56), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Chalk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 62), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {2, 2, 2, 8, 1, 8}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 60), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 51), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {2, 2, 2, 12, 62, 48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 55), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {2, 10, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 59), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 66), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Quartz, GristType.Cobalt}, new int[] {2, 2, 2, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 54), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Iodine}, new int[] {2, 2, 2, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 57), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Gold}, new int[] {2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 65), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {3, 3, 3}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 93), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 92), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 12}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 100), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 8, 6}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 96), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine, GristType.Ruby}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 98), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Caulk, GristType.Amber, GristType.Cobalt}, new int[] {2, 2, 2, 4, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 90), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 10}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 91), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 94), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar}, new int[] {2, 2, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 95), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 12}));
		GristRegistry.addGristConversion(new ItemStack(Item.monsterPlacer, 1, 120), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Ruby, GristType.Diamond}, new int[] {2, 2, 2, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Item.expBottle), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby}, new int[] {8, 1, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Item.fireballCharge), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Tar, GristType.Sulfur, GristType.Chalk}, new int[] {8, 1, 16, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.writableBook), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.emerald), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond}, new int[] {8, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Item.itemFrame), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {2, 10}));
		//GristRegistry.addGristConversion(new ItemStack(Item.flowerPot), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Item.carrot), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.potato), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.bakedPotato), false, new GristSet(new GristType[] {GristType.Amber, GristType.Tar}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.poisonousPotato), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.emptyMap), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk, GristType.Garnet}, new int[] {32, 10, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.goldenCarrot), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Gold}, new int[] {3, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.skull, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Item.skull, 1, 1), true, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {62, 48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Item.skull, 1, 2), true, new GristSet(new GristType[] {GristType.Cobalt, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.skull, 1, 3), true, new GristSet(new GristType[] {GristType.Artifact}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Item.skull, 1, 4), true, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.carrotOnAStick), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build}, new int[] {3, 1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.netherStar), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {256, 64, 64}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pumpkinPie), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk, GristType.Build, GristType.Iodine}, new int[] {8, 4, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.firework), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Item.fireworkCharge), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.enchantedBook), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby, GristType.Chalk, GristType.Iodine}, new int[] {8, 1, 4, 4, 16, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.comparator), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Build, GristType.Quartz, GristType.Marble}, new int[] {3, 6, 4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Item.netherrackBrick), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.netherQuartz), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Item.minecartTnt), false, new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur, GristType.Chalk, GristType.Shale}, new int[] {80, 8, 8, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Item.minecartHopper), false, new GristSet(new GristType[] {GristType.Rust, GristType.Rust, GristType.Garnet, GristType.Build}, new int[] {80, 6, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.field_111215_ce), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Item.field_111216_cf), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Item.field_111213_cg), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Item.field_111214_ch), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Caulk}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.field_111212_ci), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.record13), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordCat), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordBlocks), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordChirp), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordFar), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordMall), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordMellohi), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordStal), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordStrad), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordWard), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.record11), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Item.recordWait), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		
		
		//Set up Punch Designex recipes
		for(int metadata = 0; metadata < BlockSapling.WOOD_TYPES.length; metadata++)
		{
			CombinationRegistry.addCombination(new ItemStack(Block.sapling, 1, metadata), new ItemStack(Block.wood), true, true, false, new ItemStack(Block.wood, 1, metadata));
			CombinationRegistry.addCombination(new ItemStack(Block.wood, 1, metadata), new ItemStack(Block.sapling), false, true, false, new ItemStack(Block.sapling, 1, metadata));
		}
	}
	
	public static void registerMinestuckRecipes() {
		
		//set up vanilla recipes
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockStorage,1,0),new Object[]{ "XXX","XXX","XXX",'X',new ItemStack(Minestuck.rawCruxite, 1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.blankCard,8,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.rawCruxite, 1),'X',new ItemStack(Item.paper,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,0),new Object[]{ " X ","XYX"," X ",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Item.ingotIron,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,1),new Object[]{ "X X"," Y ","X X",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Item.ingotIron,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockComputerOff,1,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.blockStorage, 1, 0),'X',new ItemStack(Item.ingotIron,1)});
		
		
		//add grist conversions
		GristRegistry.addGristConversion(new ItemStack(Minestuck.blockStorage, 1, 1), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		//register land aspects
		LandHelper.registerLandAspect(new LandAspectFrost());
		LandHelper.registerLandAspect(new LandAspectHeat());
	}
	
	public static void registerModRecipes() 
	{
		try 
		{
			if(Loader.isModLoaded("IronChest"))
			{
				Block ironChest = ((Block) (Class.forName("cpw.mods.ironchest.IronChest").getField("ironChestBlock").get(null)));
				GristRegistry.addGristConversion(ironChest.blockID, 0, true, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {16, 128}));
				CombinationRegistry.addCombination(new ItemStack(Block.chest), new ItemStack(Item.ingotIron), true, new ItemStack(ironChest, 1, 0));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Given a punched card or a carved dowel, returns a new item that represents the encoded data.
	 * 
	 * @param card - The dowel or card with encoded data
	 * @return An item, or null if the data was invalid.
	 */
	public static ItemStack getDecodedItem(ItemStack card) {
		
		if (card == null) {return null;}
		//Debug.print("Looking for an ID of" +  Minestuck.cruxiteDowel.itemID + ". Got an ID of " + card.itemID);
		if (card.itemID == Minestuck.cruxiteDowel.itemID) {
			//Debug.print("Got a blank dowel as input. Returning a generic object");
			return new ItemStack(Minestuck.blockStorage,1,1); //return a Perfectly Generic Object if it's a blank dowel
		}
		
		//Debug.print("Got a carved dowel. Returning encoded object");
		NBTTagCompound tag = card.getTagCompound();
		
		if (tag == null || Item.itemsList[tag.getInteger("contentID")] == null) {return null;}
		ItemStack newItem = new ItemStack(tag.getInteger("contentID"),1,tag.getInteger("contentMeta"));
		
		return newItem;
		
	}
	
	/**
	 * Adds all the recipes that are based on the existing vanilla crafting registries, like grist conversions of items composed of oither things.
	 */
	public static void registerDynamicRecipes() {
		
		recipeList = new HashMap();
		
		Debug.print("PASS 1");
		for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
			if (recipe.getClass() == ShapedRecipes.class) {
				ShapedRecipes newRecipe = (ShapedRecipes) recipe;
				//Debug.print("Found the recipe for "+newRecipe.getRecipeOutput().getDisplayName()+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
				recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
			} else if (recipe.getClass() == ShapelessRecipes.class) {
				ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
				//Debug.print("Found the recipe for "+newRecipe.getRecipeOutput().getDisplayName()+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
				recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
			} else if (recipe.getClass() == ShapedOreRecipe.class) {
				ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
				//Debug.print("Found the recipe for "+newRecipe.getRecipeOutput().getDisplayName()+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
				recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
			} else if (recipe.getClass() == ShapelessOreRecipe.class) {
				ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
				//Debug.print("Found the recipe for "+newRecipe.getRecipeOutput().getDisplayName()+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
				recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().itemID,newRecipe.getRecipeOutput().getItemDamage()), recipe);
			} else {
				Debug.print("Found the recipe for unkown format: "+recipe.getClass());
			}
		}
		
		Debug.print("PASS 2");
	   	Iterator it = recipeList.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	//Debug.print("Getting recipe with key"+pairs.getKey()+" and value "+pairs.getValue());
        	getRecipe(pairs.getValue());
        }
	}
	
	private static boolean getRecipe(Object recipe) {
		if (recipe.getClass() == ShapedRecipes.class) {
			Debug.print("found shaped recipe. Output of "+((ShapedRecipes)recipe).getRecipeOutput().getDisplayName());
			ShapedRecipes newRecipe = (ShapedRecipes) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (ItemStack item : newRecipe.recipeItems) {
				if (GristRegistry.getGristConversion(item) != null) {
					Debug.print("	Adding compo: "+item.getDisplayName());
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					if (((Integer)item.getItemDamage()).equals(32767)) {item = item.copy(); item.setItemDamage(0);}
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						Debug.print("	Could not find "+item.getDisplayName()+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 Debug.print("	}");
						 } else {
							 Debug.print("	}");
							 return false;
						 }
					} else {
						Debug.print("	Could not find "+item.getDisplayName()+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe.getClass() == ShapelessRecipes.class) {
			Debug.print("found shapeless recipe. Output of "+((ShapelessRecipes)recipe).getRecipeOutput().getDisplayName());
			ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.recipeItems) {
				ItemStack item = (ItemStack) obj;
				if (GristRegistry.getGristConversion(item) != null) {
					Debug.print("	Adding compo: "+item.getDisplayName());
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					if (((Integer)item.getItemDamage()).equals(32767)) {item = item.copy(); item.setItemDamage(0);}
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						Debug.print("	Could not find "+item.getDisplayName()+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 Debug.print("	}");
						 } else {
							 Debug.print("	}");
							 return false;
						 }
					} else {
						Debug.print("	Could not find "+item.getDisplayName()+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe.getClass() == ShapedOreRecipe.class) {
			Debug.print("found shaped oredict recipe. Output of "+((ShapedOreRecipe)recipe).getRecipeOutput().getDisplayName());
			ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj.getClass() != ItemStack.class) {
					if (((ArrayList) obj).size() == 0) {
						Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					Debug.print("	Adding compo: "+item.getDisplayName());
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					if (((Integer)item.getItemDamage()).equals(32767)) {item = item.copy(); item.setItemDamage(0);}
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						Debug.print("	Could not find "+item.getDisplayName()+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 Debug.print("	}");
						 } else {
							 Debug.print("	}");
							 return false;
						 }
					} else {
						Debug.print("	Could not find "+item.getDisplayName()+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe.getClass() == ShapelessOreRecipe.class) {
			Debug.print("found shapeless oredict recipe. Output of "+((ShapelessOreRecipe)recipe).getRecipeOutput().getDisplayName());
			ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj.getClass() != ItemStack.class) {
					if (((ArrayList) obj).size() == 0) {
						Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					Debug.print("	Adding compo: "+item.getDisplayName());
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					if (((Integer)item.getItemDamage()).equals(32767)) {item = item.copy(); item.setItemDamage(0);}
					Object subrecipe = recipeList.get(Arrays.asList(item.itemID,item.getHasSubtypes() ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						Debug.print("	Could not find "+item.getDisplayName()+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 Debug.print("	}");
						 } else {
							 Debug.print("	}");
							 return false;
						 }
					} else {
						Debug.print("	Could not find "+item.getDisplayName()+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else {
			Debug.print("found other recipe class: "+recipe.getClass());
		}
		
		return true;
	}
	
}
