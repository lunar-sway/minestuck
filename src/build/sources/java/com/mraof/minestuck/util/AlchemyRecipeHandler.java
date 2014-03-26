package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import com.mraof.minestuck.world.gen.lands.LandAspectPulse;
import com.mraof.minestuck.world.gen.lands.LandAspectShade;
import com.mraof.minestuck.world.gen.lands.LandHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class AlchemyRecipeHandler {
	
	private static HashMap recipeList;
	private static HashMap lookedOver;
	private static int returned = 0;

	public static void registerVanillaRecipes() {
		
		//Set up Alchemiter recipes
		//Blocks
		GristRegistry.addGristConversion(new ItemStack(Blocks.stone), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.grass), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.dirt), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.cobblestone), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.planks), false, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.sapling), false, new GristSet(new GristType[] {GristType.Build}, new int[] {16})); 
		GristRegistry.addGristConversion(new ItemStack(Blocks.bedrock), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Zillium}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.sand), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.gravel), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.gold_ore), false, new GristSet(new GristType[] {GristType.Build, GristType.Gold}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.iron_ore), false, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.coal_ore), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {4, 16}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.leaves), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {1, 1}));		
		GristRegistry.addGristConversion(new ItemStack(Blocks.log), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.sponge), false, new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.glass), false, new GristSet(new GristType[] {GristType.Quartz}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.lapis_ore), false, new GristSet(new GristType[] {GristType.Amethyst, GristType.Build}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.lapis_block), false, new GristSet(new GristType[] {GristType.Amethyst}, new int[] {36}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.dispenser), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Chalk}, new int[] {3, 4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.sandstone), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.noteblock), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.railPowered), false, new GristSet(new GristType[] {GristType.Build, GristType.Gold}, new int[] {4, 32}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.railDetector), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {4, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pistonStickyBase), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Build, GristType.Garnet}, new int[] {4, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.web), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.tallgrass), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.deadbush), false, new GristSet(new GristType[] {GristType.Amber, GristType.Sulfur}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.piston), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 1), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 2), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 3), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 4), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 5), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 6), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Amber}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 7), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 8), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Tar}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 9), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 10), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 11), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amber}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 12), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst}, new int[] {6, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 13), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Amethyst, GristType.Garnet}, new int[] {4, 1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 14), true, new GristSet(new GristType[] {GristType.Chalk, GristType.Garnet, GristType.Amber}, new int[] {4, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wool, 1, 15), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.yellow_flower), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.red_flower), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Iodine}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.brown_mushroom), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.red_mushroom), false, new GristSet(new GristType[] {GristType.Ruby}, new int[] {2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 0), true, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 1), true, new GristSet(new GristType[] {GristType.Shale}, new int[] {2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 3), true, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 4), true, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {8, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 5), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 6), true, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {2, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneSingleSlab, 1, 7), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.wooden_slab), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.brick), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.bookShelf), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {16, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.tnt), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk, GristType.Shale}, new int[] {8, 8, 16}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.mossy_cobblestone), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Build}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.obsidian), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Tar, GristType.Build}, new int[] {1, 1, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.torchWood), false, new GristSet(new GristType[] {GristType.Tar}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsWoodOak), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsCobblestone), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsStoneBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsNetherBrick), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsSandStone), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsWoodSpruce), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsWoodBirch), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsWoodJungle), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stairsNetherQuartz), false, new GristSet(new GristType[] {GristType.Artifact, GristType.Build}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.chest), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.diamond_ore), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {144}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.workbench), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.furnace), false, new GristSet(new GristType[] {GristType.Tar, GristType.Build}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.ladder), false, new GristSet(new GristType[] {GristType.Build}, new int[] {7}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.rail), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {32, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.lever), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pressurePlateStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pressurePlatePlanks), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {3, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.redstone_ore), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.torchRedstoneIdle), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.stone_button), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 3}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.snow), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.ice), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.snow), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Build}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.cactus), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockClay), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {16}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.jukebox), false, new GristSet(new GristType[] {GristType.Build, GristType.Iodine, GristType.Diamond}, new int[] {4, 1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.fence), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.pumpkin), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.netherrack), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.soul_sand), false, new GristSet(new GristType[] {GristType.Tar, GristType.Shale}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.glowStone), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {16, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pumpkinLantern), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Tar, GristType.Build}, new int[] {6, 2, 2, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.trapdoor), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.stoneBrick, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.stonebrick, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {10, 6}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.stonebrick, 1, 2), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Iodine}, new int[] {10, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.stonebrick, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {12, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.fenceIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Caulk}, new int[] {2, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.thinGlass), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Caulk}, new int[] {2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.melon_block), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build}, new int[] {8, 8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.vine), false, new GristSet(new GristType[] {GristType.Build, GristType.Amber}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.fenceGate), false, new GristSet(new GristType[] {GristType.Build}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.mycelium), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby, GristType.Build}, new int[] {2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.waterlily), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.netherBrick), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.netherFence), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {12, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.enchantmentTable), false, new GristSet(new GristType[] {GristType.Shale, GristType.Chalk, GristType.Build, GristType.Tar}, new int[] {4, 4, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.end_stone), false, new GristSet(new GristType[] {GristType.Shale, GristType.Chalk}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.dragon_egg), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Zillium}, new int[] {64, 64, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.redstoneLampIdle), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk, GristType.Garnet}, new int[] {12, 12, 12}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.emerald_ore), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond, GristType.Build}, new int[] {8, 8, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.enderChest), false, new GristSet(new GristType[] {GristType.Build, GristType.Uranium, GristType.Tar}, new int[] {32, 32, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.tripWireSource), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockEmerald), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond}, new int[] {72, 72}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.beacon), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {256, 128, 128}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.cobblestoneWall, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {8, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.cobblestoneWall, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Shale, GristType.Iodine}, new int[] {8, 1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.woodenButton), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.anvil), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.chestTrapped), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pressurePlateGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.pressurePlateIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet}, new int[] {3, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.daylightSensor), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber}, new int[] {4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.blockRedstone), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {36}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.quartz_ore), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble, GristType.Build}, new int[] {8, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.hopperBlock), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet, GristType.Build}, new int[] {6, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.quartz_block), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.railActivator), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk, GristType.Build}, new int[] {16, 16, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Blocks.dropper), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet, GristType.Chalk}, new int[] {4, 4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.stained_hardened_clay), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {12, 8}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.hay_block), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {72}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.carpet), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.hardened_clay), false, new GristSet(new GristType[] {GristType.Shale, GristType.Marble}, new int[] {16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.coal_block), false, new GristSet(new GristType[] {GristType.Tar}, new int[] {144}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.monster_egg, 1, 0), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.monster_egg, 1, 1), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.monster_egg, 1, 2), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Blocks.monster_egg, 1, 3), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		
		//Items
		//GristRegistry.addGristConversion(new ItemStack(Item.shovelIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.pickaxeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.axeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Item.flintAndSteel), false, new GristSet(new GristType[] {GristType.Rust, GristType.Shale}, new int[] {16, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.apple), false, new GristSet(new GristType[] {GristType.Amber, GristType.Shale}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.bow), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {3, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Items.arrow), false, new GristSet(new GristType[] {GristType.Shale, GristType.Build}, new int[] {1, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.coal, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Items.coal, 1, 1), true, new GristSet(new GristType[] {GristType.Tar, GristType.Amber}, new int[] {12, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.diamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Items.iron_ingot), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {16}));
		GristRegistry.addGristConversion(new ItemStack(Items.gold_ingot), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {16}));
		//GristRegistry.addGristConversion(new ItemStack(Items.swordIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.swordWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.shovelWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pickaxeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.axeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.swordStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {1, 7}));
		//GristRegistry.addGristConversion(new ItemStack(Items.shovelStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pickaxeStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.axeStone), false, new GristSet(new GristType[] {GristType.Build, GristType.Shale}, new int[] {2, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.swordDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.shovelDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pickaxeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.axeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.stick), false, new GristSet(new GristType[] {GristType.Build}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bowlEmpty), false, new GristSet(new GristType[] {GristType.Build}, new int[] {3}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bowlSoup), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Ruby, GristType.Build}, new int[] {2, 2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Items.swordGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {7, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.shovelGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pickaxeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.axeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.string), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.feather), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.gunpowder), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.hoeWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.hoeStone), false, new GristSet(new GristType[] {GristType.Shale, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.hoeIron), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.hoeGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Build}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.hoeDiamond), false, new GristSet(new GristType[] {GristType.Diamond, GristType.Build}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.wheat_seeds), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.wheat), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bread), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {10, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.helmetLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {10, 10}));
		//GristRegistry.addGristConversion(new ItemStack(Items.plateLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {16, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Items.legsLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {14, 14}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bootsLeather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.chainmail_helmet), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {10, 10}));
		GristRegistry.addGristConversion(new ItemStack(Items.chainmail_chestplate), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {16, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.chainmail_leggings), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {14, 14}));
		GristRegistry.addGristConversion(new ItemStack(Items.chainmail_boots), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Tar}, new int[] {8, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.helmetIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Items.plateIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Items.legsIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bootsIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {64}));
		//GristRegistry.addGristConversion(new ItemStack(Items.helmetDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Items.plateDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Items.legsDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bootsDiamond), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {64}));
		//GristRegistry.addGristConversion(new ItemStack(Items.helmetGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {80}));
		//GristRegistry.addGristConversion(new ItemStack(Items.plateGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {128}));
		//GristRegistry.addGristConversion(new ItemStack(Items.legsGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bootsGold), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {64}));
		GristRegistry.addGristConversion(new ItemStack(Items.flint), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.porkchop), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.cooked_porkchop), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.painting), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {4, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.appleGold), false, new GristSet(new GristType[] {GristType.Gold, GristType.Amber, GristType.Shale}, new int[] {128, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.sign), false, new GristSet(new GristType[] {GristType.Build}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.doorWood), false, new GristSet(new GristType[] {GristType.Build}, new int[] {12}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bucketEmpty), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {48}));
		GristRegistry.addGristConversion(new ItemStack(Items.water_bucket), false, new GristSet(new GristType[] {GristType.Rust, GristType.Cobalt}, new int[] {48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.lava_bucket), false, new GristSet(new GristType[] {GristType.Rust, GristType.Tar}, new int[] {48, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Items.minecartEmpty), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Items.saddle), false, new GristSet(new GristType[] {GristType.Rust, GristType.Iodine, GristType.Chalk}, new int[] {16, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.doorIron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {96}));
		GristRegistry.addGristConversion(new ItemStack(Items.redstone), false, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.snowball), false, new GristSet(new GristType[] {GristType.Cobalt}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.boat), false, new GristSet(new GristType[] {GristType.Build}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.leather), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.milk_bucket), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk}, new int[] {48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.brick), false, new GristSet(new GristType[] {GristType.Shale, GristType.Tar}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.clay_ball), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.reeds), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.paper), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.book), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {14, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.slime_ball), false, new GristSet(new GristType[] {GristType.Caulk}, new int[] {8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.minecartCrate), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build}, new int[] {80, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.minecartPowered), false, new GristSet(new GristType[] {GristType.Rust, GristType.Build, GristType.Shale}, new int[] {80, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.egg), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {2, 2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.compass), false, new GristSet(new GristType[] {GristType.Rust, GristType.Garnet}, new int[] {64, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.fishingRod), false, new GristSet(new GristType[] {GristType.Build, GristType.Chalk}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pocketSundial), false, new GristSet(new GristType[] {GristType.Gold, GristType.Garnet}, new int[] {64, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.glowstone_dust), false, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.fish), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt}, new int[] {4, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.cooked_fished), false, new GristSet(new GristType[] {GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Tar}, new int[] {4, 4, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 0), true, new GristSet(new GristType[] {GristType.Tar}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 1), true, new GristSet(new GristType[] {GristType.Garnet}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 2), true, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 3), true, new GristSet(new GristType[] {GristType.Iodine, GristType.Amber}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 4), true, new GristSet(new GristType[] {GristType.Amethyst}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 5), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 6), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 7), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 8), true, new GristSet(new GristType[] {GristType.Tar, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 9), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 10), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {3}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 11), true, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 12), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 13), true, new GristSet(new GristType[] {GristType.Amethyst, GristType.Garnet}, new int[] {1, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 14), true, new GristSet(new GristType[] {GristType.Garnet, GristType.Amber}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.dye, 1, 15), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.bone), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {12}));
		//GristRegistry.addGristConversion(new ItemStack(Items.sugar), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.cake), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build, GristType.Ruby}, new int[] {4, 4, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.bed), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {12, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.redstoneRepeater), false, new GristSet(new GristType[] {GristType.Build, GristType.Garnet}, new int[] {6, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.cookie), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {6, 6}));
		//GristRegistry.addGristConversion(new ItemStack(Items.shears), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {32}));
		GristRegistry.addGristConversion(new ItemStack(Items.melon), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk}, new int[] {1, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pumpkinSeeds), false, new GristSet(new GristType[] {GristType.Chalk}, new int[] {1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.melonSeeds), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.beef), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Items.cooked_beef), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {12, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.chicken), false, new GristSet(new GristType[] {GristType.Iodine}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.cooked_chicken), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {10, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.rotten_flesh), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ender_pearl), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Diamond}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.blaze_rod), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium}, new int[] {16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.ghast_tear), false, new GristSet(new GristType[] {GristType.Cobalt, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.goldNugget), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Items.nether_wart), false, new GristSet(new GristType[] {GristType.Iodine, GristType.Tar}, new int[] {4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 0), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt}, new int[] {1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 1), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Chalk}, new int[] {1, 18, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 2), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine}, new int[] {1, 16, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 3), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {1, 16, 8, 1, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 4), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Iodine}, new int[] {1, 16, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 5), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Chalk}, new int[] {1, 16, 16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 6), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold}, new int[] {1, 16, 3, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 8), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 6, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 9), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Tar, GristType.Uranium}, new int[] {1, 8, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 10), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine, GristType.Amber, GristType.Tar}, new int[] {1, 16, 8, 6, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 12), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Gold, GristType.Amber, GristType.Iodine, GristType.Tar, GristType.Chalk}, new int[] {1, 16, 16, 7, 4, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 14), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Amber, GristType.Chalk, GristType.Gold, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 9, 1, 16, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 16), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt, GristType.Iodine, GristType.Tar}, new int[] {1, 16, 4, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.potionitem, 1, 32), true, new GristSet(new GristType[] {GristType.Quartz, GristType.Cobalt}, new int[] {1, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Items.glassBottle), false, new GristSet(new GristType[] {GristType.Quartz}, new int[] {1}));
		GristRegistry.addGristConversion(new ItemStack(Items.spider_eye), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.fermentedSpiderEye), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine, GristType.Tar}, new int[] {6, 4, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.blazePowder), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium}, new int[] {8, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.magmaCream), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {8, 1, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.brewingStand), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Build}, new int[] {16, 2, 3}));
		//GristRegistry.addGristConversion(new ItemStack(Items.cauldron), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {112}));
		//GristRegistry.addGristConversion(new ItemStack(Items.eyeOfEnder), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Diamond}, new int[] {8, 9, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.speckledMelon), false, new GristSet(new GristType[] {GristType.Gold, GristType.Amber, GristType.Chalk}, new int[] {16, 1, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 61), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar, GristType.Uranium}, new int[] {2, 2, 2, 16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 59), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 50), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Sulfur, GristType.Chalk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 58), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Uranium, GristType.Diamond}, new int[] {2, 2, 2, 8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 56), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Chalk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 62), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar, GristType.Uranium, GristType.Caulk}, new int[] {2, 2, 2, 8, 1, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 60), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 51), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {2, 2, 2, 12, 62, 48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 55), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {2, 10, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 59), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 66), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Quartz, GristType.Cobalt}, new int[] {2, 2, 2, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 54), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Cobalt, GristType.Iodine}, new int[] {2, 2, 2, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 57), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Gold}, new int[] {2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 65), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber}, new int[] {3, 3, 3}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 93), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 92), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 12}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 100), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 8, 6}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 96), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine, GristType.Ruby}, new int[] {2, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 98), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Caulk, GristType.Amber, GristType.Cobalt}, new int[] {2, 2, 2, 4, 4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 90), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Iodine}, new int[] {2, 2, 2, 10}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 91), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 94), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Tar}, new int[] {2, 2, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 95), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk}, new int[] {2, 2, 2, 12}));
		GristRegistry.addGristConversion(new ItemStack(Items.spawn_egg, 1, 120), true, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Ruby, GristType.Diamond}, new int[] {2, 2, 2, 8, 8}));
		GristRegistry.addGristConversion(new ItemStack(Items.experience_bottle), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby}, new int[] {8, 1, 4, 4}));
		//GristRegistry.addGristConversion(new ItemStack(Items.fireballCharge), false, new GristSet(new GristType[] {GristType.Tar, GristType.Uranium, GristType.Tar, GristType.Sulfur, GristType.Chalk}, new int[] {8, 1, 16, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.writable_book), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Iodine}, new int[] {16, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.emerald), false, new GristSet(new GristType[] {GristType.Ruby, GristType.Diamond}, new int[] {8, 8}));
		//GristRegistry.addGristConversion(new ItemStack(Items.itemFrame), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Build}, new int[] {2, 10}));
		//GristRegistry.addGristConversion(new ItemStack(Items.flowerPot), false, new GristSet(new GristType[] {GristType.Shale}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Items.carrot), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk}, new int[] {3, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.potato), false, new GristSet(new GristType[] {GristType.Amber}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.baked_potato), false, new GristSet(new GristType[] {GristType.Amber, GristType.Tar}, new int[] {4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.poisonous_potato), false, new GristSet(new GristType[] {GristType.Amber, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.map), false, new GristSet(new GristType[] {GristType.Rust, GristType.Chalk, GristType.Garnet}, new int[] {32, 10, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.goldenCarrot), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Gold}, new int[] {3, 1, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.skull, 1, 0), true, new GristSet(new GristType[] {GristType.Chalk}, new int[] {12}));
		GristRegistry.addGristConversion(new ItemStack(Items.skull, 1, 1), true, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {62, 48, 16}));
		GristRegistry.addGristConversion(new ItemStack(Items.skull, 1, 2), true, new GristSet(new GristType[] {GristType.Cobalt, GristType.Iodine}, new int[] {4, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.skull, 1, 3), true, new GristSet(new GristType[] {GristType.Artifact}, new int[] {4}));
		GristRegistry.addGristConversion(new ItemStack(Items.skull, 1, 4), true, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.carrotOnAStick), false, new GristSet(new GristType[] {GristType.Amber, GristType.Chalk, GristType.Build}, new int[] {3, 1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.nether_star), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Tar, GristType.Diamond}, new int[] {256, 64, 64}));
		//GristRegistry.addGristConversion(new ItemStack(Items.pumpkinPie), false, new GristSet(new GristType[] {GristType.Amber, GristType.Caulk, GristType.Build, GristType.Iodine}, new int[] {8, 4, 2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.fireworks), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 4}));
		GristRegistry.addGristConversion(new ItemStack(Items.firework_charge), false, new GristSet(new GristType[] {GristType.Sulfur, GristType.Chalk}, new int[] {2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.enchanted_book), false, new GristSet(new GristType[] {GristType.Uranium, GristType.Quartz, GristType.Diamond, GristType.Ruby, GristType.Chalk, GristType.Iodine}, new int[] {8, 1, 4, 4, 16, 2}));
		//GristRegistry.addGristConversion(new ItemStack(Items.comparator), false, new GristSet(new GristType[] {GristType.Garnet, GristType.Build, GristType.Quartz, GristType.Marble}, new int[] {3, 6, 4, 1}));
		GristRegistry.addGristConversion(new ItemStack(Items.netherbrick), false, new GristSet(new GristType[] {GristType.Build, GristType.Tar}, new int[] {1, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.quartz), false, new GristSet(new GristType[] {GristType.Quartz, GristType.Marble}, new int[] {4, 1}));
		//GristRegistry.addGristConversion(new ItemStack(Items.minecartTnt), false, new GristSet(new GristType[] {GristType.Rust, GristType.Sulfur, GristType.Chalk, GristType.Shale}, new int[] {80, 8, 8, 16}));
		//GristRegistry.addGristConversion(new ItemStack(Items.minecartHopper), false, new GristSet(new GristType[] {GristType.Rust, GristType.Rust, GristType.Garnet, GristType.Build}, new int[] {80, 6, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.iron_horse_armor), false, new GristSet(new GristType[] {GristType.Rust}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Items.golden_horse_armor), false, new GristSet(new GristType[] {GristType.Gold}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Items.diamond_horse_armor), false, new GristSet(new GristType[] {GristType.Diamond}, new int[] {80}));
		GristRegistry.addGristConversion(new ItemStack(Items.lead), false, new GristSet(new GristType[] {GristType.Chalk, GristType.Caulk}, new int[] {8, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.name_tag), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_13), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_cat), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_blocks), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_chirp), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_far), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_mall), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_mellohi), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_stal), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_strad), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_ward), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_11), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		GristRegistry.addGristConversion(new ItemStack(Items.record_wait), false, new GristSet(new GristType[] {GristType.Build, GristType.Caulk, GristType.Amber, GristType.Chalk, GristType.Caulk}, new int[] {3, 2, 2, 2, 2}));
		
		
		//Set up Punch Designex recipes
		
		//metadata-based
		for (int meta = 0; meta < BlockSapling.field_149882_a.length; meta++) {
			CombinationRegistry.addCombination(new ItemStack(Blocks.sapling, 1, meta), new ItemStack(Blocks.log), CombinationRegistry.MODE_AND, true, false, new ItemStack(Blocks.log, 1, meta));
			CombinationRegistry.addCombination(new ItemStack(Blocks.log, 1, meta), new ItemStack(Blocks.sapling), CombinationRegistry.MODE_OR, true, false, new ItemStack(Blocks.sapling, 1, meta));
		}
		for (int meta = 0;meta <= 15;meta++) {
			CombinationRegistry.addCombination(new ItemStack(Items.dye,1,meta),new ItemStack(Blocks.wool),CombinationRegistry.MODE_AND, true,false, new ItemStack(Blocks.wool,1,meta));
			CombinationRegistry.addCombination(new ItemStack(Items.dye),new ItemStack(Blocks.wool,1,meta),CombinationRegistry.MODE_OR, false,true, new ItemStack(Items.dye,1,meta));
		}
		
		//ore related
		CombinationRegistry.addCombination(new ItemStack(Items.iron_ingot),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.iron_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.gold_ingot),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.gold_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.coal),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.coal_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.diamond),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.diamond_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.emerald),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.emerald_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.redstone),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.redstone_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.dye,1,8),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Blocks.lapis_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.quartz),new ItemStack(Blocks.netherrack),CombinationRegistry.MODE_AND, new ItemStack(Blocks.quartz_ore));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_ingot),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.iron_block));
		CombinationRegistry.addCombination(new ItemStack(Items.gold_ingot),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.gold_block));
		CombinationRegistry.addCombination(new ItemStack(Items.coal),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.coal_block));
		CombinationRegistry.addCombination(new ItemStack(Items.diamond),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.diamond_block));
		CombinationRegistry.addCombination(new ItemStack(Items.emerald),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.emerald_block));
		CombinationRegistry.addCombination(new ItemStack(Items.redstone),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.redstone_block));
		CombinationRegistry.addCombination(new ItemStack(Items.dye,1,8),new ItemStack(Blocks.stone),CombinationRegistry.MODE_OR, new ItemStack(Blocks.lapis_block));
		CombinationRegistry.addCombination(new ItemStack(Items.quartz),new ItemStack(Blocks.netherrack),CombinationRegistry.MODE_OR, new ItemStack(Blocks.quartz_block));
		
		//misc
		CombinationRegistry.addCombination(new ItemStack(Blocks.dirt),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_AND, new ItemStack(Blocks.grass));
		CombinationRegistry.addCombination(new ItemStack(Blocks.dirt),new ItemStack(Blocks.tallgrass),CombinationRegistry.MODE_OR, new ItemStack(Blocks.grass));
		CombinationRegistry.addCombination(new ItemStack(Blocks.ladder),new ItemStack(Items.iron_ingot),CombinationRegistry.MODE_AND, new ItemStack(Blocks.rail));
		CombinationRegistry.addCombination(new ItemStack(Blocks.rail),new ItemStack(Blocks.planks),CombinationRegistry.MODE_AND, new ItemStack(Blocks.ladder));
		CombinationRegistry.addCombination(new ItemStack(Blocks.grass),new ItemStack(Blocks.red_mushroom),CombinationRegistry.MODE_AND, new ItemStack(Blocks.mycelium));
		CombinationRegistry.addCombination(new ItemStack(Blocks.grass),new ItemStack(Blocks.brown_mushroom),CombinationRegistry.MODE_AND, new ItemStack(Blocks.mycelium));
		CombinationRegistry.addCombination(new ItemStack(Blocks.fence),new ItemStack(Blocks.nether_brick),CombinationRegistry.MODE_AND, new ItemStack(Blocks.nether_brick_fence));
		CombinationRegistry.addCombination(new ItemStack(Blocks.nether_brick_fence),new ItemStack(Blocks.planks),CombinationRegistry.MODE_AND, new ItemStack(Blocks.fence));
		CombinationRegistry.addCombination(new ItemStack(Items.wooden_door),new ItemStack(Items.iron_ingot),CombinationRegistry.MODE_AND, new ItemStack(Items.iron_door));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_door),new ItemStack(Blocks.planks),CombinationRegistry.MODE_AND, new ItemStack(Items.wooden_door));
		CombinationRegistry.addCombination(new ItemStack(Items.rotten_flesh),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_OR, new ItemStack(Items.chicken));
		CombinationRegistry.addCombination(new ItemStack(Items.rotten_flesh),new ItemStack(Items.carrot),CombinationRegistry.MODE_OR, new ItemStack(Items.porkchop));
		CombinationRegistry.addCombination(new ItemStack(Items.rotten_flesh),new ItemStack(Items.wheat),CombinationRegistry.MODE_OR, new ItemStack(Items.beef));
		CombinationRegistry.addCombination(new ItemStack(Blocks.noteblock),new ItemStack(Items.diamond),CombinationRegistry.MODE_AND, new ItemStack(Blocks.jukebox));
		CombinationRegistry.addCombination(new ItemStack(Blocks.cobblestone),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_OR, new ItemStack(Blocks.mossy_cobblestone));
		CombinationRegistry.addCombination(new ItemStack(Blocks.stonebrick),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_OR, new ItemStack(Blocks.stonebrick,1,2));
		CombinationRegistry.addCombination(new ItemStack(Blocks.cobblestone_wall),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_OR, new ItemStack(Blocks.cobblestone_wall,1,1));
		CombinationRegistry.addCombination(new ItemStack(Items.stick),new ItemStack(Items.lava_bucket),CombinationRegistry.MODE_AND, new ItemStack(Items.blaze_rod));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_ingot),new ItemStack(Items.saddle),CombinationRegistry.MODE_AND, new ItemStack(Items.iron_horse_armor));
		CombinationRegistry.addCombination(new ItemStack(Items.gold_ingot),new ItemStack(Items.saddle),CombinationRegistry.MODE_AND, new ItemStack(Items.golden_horse_armor));
		CombinationRegistry.addCombination(new ItemStack(Items.diamond),new ItemStack(Items.saddle),CombinationRegistry.MODE_AND, new ItemStack(Items.diamond_horse_armor));
		CombinationRegistry.addCombination(new ItemStack(Items.string),new ItemStack(Items.leather),CombinationRegistry.MODE_AND, new ItemStack(Items.saddle));
		CombinationRegistry.addCombination(new ItemStack(Items.ender_pearl),new ItemStack(Items.blaze_powder),CombinationRegistry.MODE_AND, new ItemStack(Items.ender_eye));
		CombinationRegistry.addCombination(new ItemStack(Blocks.netherrack),new ItemStack(Blocks.brick_block),CombinationRegistry.MODE_AND, new ItemStack(Items.netherbrick));
		CombinationRegistry.addCombination(new ItemStack(Blocks.netherrack),new ItemStack(Blocks.brick_block),CombinationRegistry.MODE_OR, new ItemStack(Blocks.nether_brick));
		CombinationRegistry.addCombination(new ItemStack(Blocks.netherrack),new ItemStack(Items.glowstone_dust),CombinationRegistry.MODE_AND, new ItemStack(Blocks.glowstone));
		CombinationRegistry.addCombination(new ItemStack(Blocks.stone),new ItemStack(Items.ender_pearl),CombinationRegistry.MODE_AND, new ItemStack(Blocks.end_stone));
		CombinationRegistry.addCombination(new ItemStack(Blocks.cobblestone),new ItemStack(Items.coal),CombinationRegistry.MODE_AND, new ItemStack(Blocks.furnace));
		CombinationRegistry.addCombination(new ItemStack(Blocks.gravel),new ItemStack(Blocks.stone),CombinationRegistry.MODE_AND, new ItemStack(Items.flint));
		CombinationRegistry.addCombination(new ItemStack(Items.compass),new ItemStack(Items.gold_ingot),CombinationRegistry.MODE_AND, new ItemStack(Items.clock));
		CombinationRegistry.addCombination(new ItemStack(Items.redstone),new ItemStack(Items.gold_ingot),CombinationRegistry.MODE_OR, new ItemStack(Items.clock));
		CombinationRegistry.addCombination(new ItemStack(Items.clock),new ItemStack(Items.iron_ingot),CombinationRegistry.MODE_AND, new ItemStack(Items.compass));
		CombinationRegistry.addCombination(new ItemStack(Items.redstone),new ItemStack(Items.iron_ingot),CombinationRegistry.MODE_OR, new ItemStack(Items.compass));
		CombinationRegistry.addCombination(new ItemStack(Items.water_bucket),new ItemStack(Items.lava_bucket),CombinationRegistry.MODE_AND, new ItemStack(Blocks.obsidian));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_ingot),new ItemStack(Blocks.tallgrass),CombinationRegistry.MODE_AND, new ItemStack(Items.shears));
		CombinationRegistry.addCombination(new ItemStack(Blocks.sapling),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_AND,true,false, new ItemStack(Items.apple));
		CombinationRegistry.addCombination(new ItemStack(Items.apple),new ItemStack(Items.gold_ingot),CombinationRegistry.MODE_AND, new ItemStack(Items.golden_apple));
		CombinationRegistry.addCombination(new ItemStack(Items.carrot),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_AND, new ItemStack(Items.potato));
		CombinationRegistry.addCombination(new ItemStack(Items.potato),new ItemStack(Items.wheat_seeds),CombinationRegistry.MODE_AND, new ItemStack(Items.carrot));
		CombinationRegistry.addCombination(new ItemStack(Items.gunpowder),new ItemStack(Blocks.sand),CombinationRegistry.MODE_AND, new ItemStack(Blocks.tnt));
		CombinationRegistry.addCombination(new ItemStack(Items.rotten_flesh),new ItemStack(Items.water_bucket),CombinationRegistry.MODE_OR, new ItemStack(Items.leather));
		CombinationRegistry.addCombination(new ItemStack(Items.slime_ball),new ItemStack(Items.blaze_powder),CombinationRegistry.MODE_AND, new ItemStack(Items.magma_cream));
		CombinationRegistry.addCombination(new ItemStack(Items.ender_eye),new ItemStack(Items.egg),CombinationRegistry.MODE_AND, new ItemStack(Blocks.dragon_egg));
		
	}
	
	public static void registerMinestuckRecipes() {
		
		 //set up vanilla recipes
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockStorage,1,0),new Object[]{ "XXX","XXX","XXX",'X',new ItemStack(Minestuck.rawCruxite, 1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.captchaCard,8,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.rawCruxite, 1),'X',new ItemStack(Items.paper,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,0),new Object[]{ " X ","XYX"," X ",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Items.iron_ingot,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.disk,1,1),new Object[]{ "X X"," Y ","X X",'X',new ItemStack(Minestuck.rawCruxite, 1),'Y',new ItemStack(Items.iron_ingot,1)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.blockComputerOff,1,0),new Object[]{ "XXX","XYX","XXX",'Y',new ItemStack(Minestuck.blockStorage, 1, 0),'X',new ItemStack(Items.iron_ingot,1)});
		GameRegistry.addShapelessRecipe(new ItemStack(Minestuck.rawCruxite, 9),new  ItemStack(Minestuck.blockStorage,1,0)); 
		GameRegistry.addRecipe(new ItemStack(Minestuck.clawHammer),new Object[]{ " XX","XY "," Y ",'X',new ItemStack(Items.iron_ingot),'Y',new ItemStack(Items.stick)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.component,1,0),new Object[]{ " X "," Y "," Y ",'X',new ItemStack(Items.bowl),'Y',new ItemStack(Items.stick)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.component,1,2),new Object[]{ "XYX","YXY","XYX",'X',new ItemStack(Blocks.stained_hardened_clay,1,0),'Y',new ItemStack(Blocks.stained_hardened_clay,1,15)});
		GameRegistry.addRecipe(new ItemStack(Minestuck.component,1,2),new Object[]{ "XYX","YXY","XYX",'Y',new ItemStack(Blocks.stained_hardened_clay,1,0),'X',new ItemStack(Blocks.stained_hardened_clay,1,15)});
		
		//add grist conversions
		GristRegistry.addGristConversion(new ItemStack(Minestuck.blockStorage, 1, 1), true, new GristSet(new GristType[] {GristType.Build}, new int[] {2}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.cruxiteArtifact,1),false,new GristSet());
		GristRegistry.addGristConversion(new ItemStack(Minestuck.sledgeHammer), false, new GristSet(new GristType[] {GristType.Build,GristType.Shale}, new int[] {10,2}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.sickle), false, new GristSet(new GristType[] {GristType.Build}, new int[] {10}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.ninjaSword), false, new GristSet(new GristType[] {GristType.Build,GristType.Quartz}, new int[] {10,5}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.crockerSpork), false, new GristSet(new GristType[] {GristType.Build,GristType.Iodine,GristType.Chalk,GristType.Ruby}, new int[] {100,48,48,12}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.pogoHammer), false, new GristSet(new GristType[] {GristType.Build,GristType.Shale}, new int[] {20,16}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.regisword), false, new GristSet(new GristType[] {GristType.Build,GristType.Tar,GristType.Gold}, new int[] {64,20,6}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.component,1,1), true, new GristSet(new GristType[] {GristType.Build,GristType.Mercury}, new int[] {3,9}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.component,1,1), true, new GristSet(new GristType[] {GristType.Shale,GristType.Marble}, new int[] {10,20}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.regiSickle), false, new GristSet(new GristType[] {GristType.Build,GristType.Tar,GristType.Gold}, new int[] {60,15,8}));
		GristRegistry.addGristConversion(new ItemStack(Minestuck.blockMachine,1,4), true, new GristSet(new GristType[] {GristType.Build,GristType.Garnet,GristType.Ruby}, new int[] {100,20,5}));
		
		//add Designex combinations
		CombinationRegistry.addCombination(new ItemStack(Items.iron_hoe),new ItemStack(Items.wheat),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.sickle));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_sword),new ItemStack(Items.rotten_flesh),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.ninjaSword));
		CombinationRegistry.addCombination(new ItemStack(Minestuck.clawHammer),new ItemStack(Blocks.brick_block),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.sledgeHammer));
		CombinationRegistry.addCombination(new ItemStack(Minestuck.component,1,0),new ItemStack(Items.iron_ingot),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.component,1,1));
		CombinationRegistry.addCombination(new ItemStack(Minestuck.component,1,1),new ItemStack(Items.cake),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.crockerSpork));
		CombinationRegistry.addCombination(new ItemStack(Items.slime_ball),new ItemStack(Minestuck.sledgeHammer),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.pogoHammer));
		CombinationRegistry.addCombination(new ItemStack(Items.iron_sword),new ItemStack(Minestuck.component,1,2),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.regisword));
		CombinationRegistry.addCombination(new ItemStack(Minestuck.sickle),new ItemStack(Minestuck.component,1,2),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.regiSickle));
		CombinationRegistry.addCombination(new ItemStack(Minestuck.crockerSpork),new ItemStack(Minestuck.captchaCard),CombinationRegistry.MODE_AND, new ItemStack(Minestuck.blockMachine,1,4));
		
		//register land aspects
		LandHelper.registerLandAspect(new LandAspectFrost());
		LandHelper.registerLandAspect(new LandAspectHeat());
		LandHelper.registerLandAspect(new LandAspectShade());
		LandHelper.registerLandAspect(new LandAspectPulse());
	}
	
	public static void registerModRecipes() 
	{
		try 
		{
			if(Loader.isModLoaded("IronChest"))
			{
				Block ironChest = ((Block) (Class.forName("cpw.mods.ironchest.IronChest").getField("ironChestBlock").get(null)));
				GristRegistry.addGristConversion(ironChest, 0, true, new GristSet(new GristType[] {GristType.Build, GristType.Rust}, new int[] {16, 128}));
				CombinationRegistry.addCombination(new ItemStack(Blocks.chest), new ItemStack(Items.iron_ingot), true, new ItemStack(ironChest, 1, 0));
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
		
		NBTTagCompound tag = card.getTagCompound();
		
		if (tag == null || !tag.hasKey("contentID")) {
			return new ItemStack(Minestuck.blockStorage,1,1);
		}
		
		if (!Item.itemRegistry.containsKey(tag.getString("contentID"))) {return null;}
		ItemStack newItem = new ItemStack((Item)Item.itemRegistry.getObject(tag.getString(("contentID"))), 1, tag.getInteger("contentMeta"));
		
		return newItem;
		
	}
	
	/**
	 * Given a punched card or a carved dowel, returns a new item that represents the encoded data. If allowDirect is true,
	 * it jst gives you the item directly if it's not a card.
	 */
	public static ItemStack getDecodedItem(ItemStack card,boolean allowDirect) {
		
		if (card == null) {return null;}
		
		if (!card.getItem().equals(Minestuck.captchaCard) && Minestuck.easyDesignex) {
			return card;
		} else {
			return getDecodedItem(card);
		}
	}
	
	public static ItemStack createEncodedItem(ItemStack item, boolean registerToCard) {
		NBTTagCompound nbt = null;
		if(item != null) {
			nbt = new NBTTagCompound();
			nbt.setString("contentID", Item.itemRegistry.getNameForObject(item.getItem()));
			nbt.setInteger("contentMeta", item.getItemDamage());
		}
		ItemStack stack = new ItemStack(registerToCard?Minestuck.captchaCard:Minestuck.cruxiteDowel);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static ItemStack createCard(ItemStack item, boolean punched) {
		ItemStack stack = createEncodedItem(item, true);
		if(stack.hasTagCompound())
			stack.getTagCompound().setBoolean("punched", punched);
		return stack;
	}
	
	/**
	 * Adds all the recipes that are based on the existing vanilla crafting registries, like grist conversions of items composed of oither things.
	 */
	public static void registerDynamicRecipes() {
		
		recipeList = new HashMap();
		int invalid = 0;
		
		Debug.print("Looking for dynamic grist conversions...");
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			try
			{
				if (recipe instanceof ShapedRecipes) {
					ShapedRecipes newRecipe = (ShapedRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessRecipes) {
					ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapedOreRecipe) {
					ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else if (recipe instanceof ShapelessOreRecipe) {
					ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
					//Debug.print("Found the recipe for "+"ITEM"+", id "+newRecipe.getRecipeOutput().itemID+":"+newRecipe.getRecipeOutput().getItemDamage());
					recipeList.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getItemDamage()), recipe);
				} else {
					//Debug.print("Found the recipe for unkown format: "+recipe.getClass());
					invalid++;
				}
			}
			catch (NullPointerException e)
			{
				Debug.printf("a null pointer exception was thrown for %s", recipe);
			}
		}
		Debug.print("Found "+recipeList.size()+" valid recipes, and "+invalid+" unknown ones.");
		
		Debug.print("Calculating grist conversion...");
	   	Iterator it = recipeList.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry pairs = (Map.Entry)it.next();
        	//Debug.print("Getting recipe with key"+pairs.getKey()+" and value "+pairs.getValue());
        	lookedOver = new HashMap();
        	getRecipe(pairs.getValue());
        }
        
        Debug.print("Done. Added "+returned+" grist conversions.");
	}
	
	private static boolean getRecipe(Object recipe) {
		if (recipe instanceof ShapedRecipes) {
			////Debug.print("found shaped recipe. Output of "+"ITEM");
			ShapedRecipes newRecipe = (ShapedRecipes) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				////Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (ItemStack item : newRecipe.recipeItems) {
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapelessRecipes) {
			//Debug.print("found shapeless recipe. Output of "+"ITEM");
			ShapelessRecipes newRecipe = (ShapelessRecipes) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.recipeItems) {
				ItemStack item = (ItemStack) obj;
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapedOreRecipe) {
			//Debug.print("found shaped oredict recipe. Output of "+"ITEM");
			ShapedOreRecipe newRecipe = (ShapedOreRecipe) recipe;
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj instanceof ArrayList) {
					if (((ArrayList) obj).size() == 0) {
						//Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else if (recipe instanceof ShapelessOreRecipe) {
			//Debug.print("found shapeless oredict recipe. Output of "+"ITEM");
			ShapelessOreRecipe newRecipe = (ShapelessOreRecipe) recipe;
			if (lookedOver.get(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0)) != null) {
				//Debug.print("	Recursive recipe! Recipe failed.");
				return false;
			} else {
				lookedOver.put(Arrays.asList(newRecipe.getRecipeOutput().getItem(),newRecipe.getRecipeOutput().getHasSubtypes() ? newRecipe.getRecipeOutput().getItemDamage() : 0),true);
			}
			if (GristRegistry.getGristConversion(newRecipe.getRecipeOutput()) != null) {return false;};
			GristSet set = new GristSet();
			for (Object obj : newRecipe.getInput()) {
				ItemStack item = null;
				if (obj == null) {break;}
				if (obj instanceof ArrayList) {
					if (((ArrayList) obj).size() == 0) {
						//Debug.print("	Input list was empty!");
						break;
					}
					item = (ItemStack) ((ArrayList) obj).get(0);
				} else {
					item = (ItemStack) obj;
				}
				if (GristRegistry.getGristConversion(item) != null) {
					//Debug.print("	Adding compo: "+"ITEM");
					set.addGrist(GristRegistry.getGristConversion(item));
				} else if (item != null) {
					Object subrecipe = recipeList.get(Arrays.asList(item.getItem(),item.getHasSubtypes() && !((Integer)item.getItemDamage()).equals(32767) ? item.getItemDamage() : 0));
					if (subrecipe != null) {
						//Debug.print("	Could not find "+"ITEM"+". Looking up subrecipe... {");
						 if (getRecipe(subrecipe)) {
							 if (GristRegistry.getGristConversion(item) == null) {
								 //Debug.print("	} Recipe failure! getRecipe did not return expeted boolean value.");
								 return false;
							 }
							 set.addGrist(GristRegistry.getGristConversion(item));
							 //Debug.print("	}");
						 } else {
							 //Debug.print("	}");
							 return false;
						 }
					} else {
						//Debug.print("	Could not find "+"ITEM"+" ("+item.itemID+":"+item.getItemDamage()+"). Recipe failed!");
						return false;
					}
				}
				set.scaleGrist(1/(float)newRecipe.getRecipeOutput().stackSize);
				GristRegistry.addGristConversion(newRecipe.getRecipeOutput(),newRecipe.getRecipeOutput().getHasSubtypes(),set);
			}
		} else {
			//Debug.print("found other recipe class: "+recipe.getClass());
		}
		
		returned ++;
		return true;
	}
	
}
