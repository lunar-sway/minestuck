package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class MinestuckBlockStateProvider extends BlockStateProvider
{
	public MinestuckBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		ModelFile file;
		
		//Skaia
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_DIRT);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_DIRT);
		
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICKS);
		simpleBlockWithItem(MSBlocks.BLACK_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.DARK_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.LIGHT_GRAY_CHESS_BRICK_SMOOTH);
		simpleBlockWithItem(MSBlocks.WHITE_CHESS_BRICK_SMOOTH);
		
		simpleBlockWithItem(MSBlocks.STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SANDSTONE_CRUXITE_ORE.get(), models().getExistingFile(MSBlocks.SANDSTONE_CRUXITE_ORE.getId()));
		simpleBlockWithItem(MSBlocks.RED_SANDSTONE_CRUXITE_ORE.get(), models().getExistingFile(MSBlocks.RED_SANDSTONE_CRUXITE_ORE.getId()));
		simpleBlockWithItem(MSBlocks.END_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_CRUXITE_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_CRUXITE_ORE);
		
		simpleBlockWithItem(MSBlocks.STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.DEEPSLATE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.NETHERRACK_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.COBBLESTONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.END_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_URANIUM_ORE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE_URANIUM_ORE);
		
		simpleBlockWithItem(MSBlocks.NETHERRACK_COAL_ORE);
		simpleBlockWithItem(MSBlocks.SHADE_STONE_COAL_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_COAL_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_IRON_ORE);
		
		simpleBlockWithItem(MSBlocks.SHADE_STONE_GOLD_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_GOLD_ORE);
		
		simpleBlockWithItem(MSBlocks.END_STONE_REDSTONE_ORE);
		simpleBlockWithItem(MSBlocks.STONE_QUARTZ_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_LAPIS_ORE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_DIAMOND_ORE);
		
		//Resource Blocks
		simpleBlockWithItem(MSBlocks.CRUXITE_BLOCK);
		simpleBlockWithItem(MSBlocks.URANIUM_BLOCK);
		simpleBlockWithItem(MSBlocks.GENERIC_OBJECT);
		
		//Land Environment
		simpleBlockWithItem(MSBlocks.THOUGHT_DIRT);
		
		simpleBlockWithItem(MSBlocks.COARSE_STONE);
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE);
		simpleBlockWithItem(MSBlocks.COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_COARSE_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_COARSE_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SMOOTH_SHADE_STONE);
		simpleBlockWithItem(MSBlocks.SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.BLOOD_SHADE_BRICKS);
		simpleBlockWithItem(MSBlocks.TAR_SHADE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.FROST_TILE);
		simpleBlockWithItem(MSBlocks.CHISELED_FROST_TILE);
		simpleBlockWithItem(MSBlocks.FROST_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_FROST_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_FROST_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CAST_IRON);
		simpleBlockWithItem(MSBlocks.CHISELED_CAST_IRON);
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
		
		simpleBlockWithItem(MSBlocks.MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_MYCELIUM_STONE);
		simpleBlockWithItem(MSBlocks.MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.SUSPICIOUS_CHISELED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_MYCELIUM_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_MYCELIUM_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BLACK_SAND);
		simpleBlockWithItem(MSBlocks.BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_COBBLESTONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BLACK_STONE);
		simpleBlockWithItem(MSBlocks.BLACK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_BLACK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BLACK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.CHALK);
		simpleBlockWithItem(MSBlocks.POLISHED_CHALK);
		simpleBlockWithItem(MSBlocks.CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_CHALK_BRICKS);
		simpleBlockWithItem(MSBlocks.FLOWERY_CHALK_BRICKS);
		
		simpleBlockWithItem(MSBlocks.PINK_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_PINK_STONE);
		simpleBlockWithItem(MSBlocks.PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_PINK_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.MOSSY_PINK_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.BROWN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_BROWN_STONE);
		simpleBlockWithItem(MSBlocks.BROWN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_BROWN_STONE_BRICKS);
		
		simpleBlockWithItem(MSBlocks.GREEN_STONE);
		simpleBlockWithItem(MSBlocks.POLISHED_GREEN_STONE);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.HORIZONTAL_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.VERTICAL_GREEN_STONE_BRICKS);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_FROG);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_LOTUS);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_SKAIA);
		simpleBlockWithItem(MSBlocks.GREEN_STONE_BRICK_TURTLE);
		
		file = models().cubeColumn(
				MSBlocks.CHISELED_SANDSTONE_COLUMN.getId().getPath(),
				texture(MSBlocks.CHISELED_SANDSTONE_COLUMN),
				texture("sandstone_column_end"));
		directionalBlock(MSBlocks.CHISELED_SANDSTONE_COLUMN.get(), file);
		simpleBlockItem(MSBlocks.CHISELED_SANDSTONE_COLUMN.get(), file);
		file = models().cubeColumn(
				MSBlocks.CHISELED_RED_SANDSTONE_COLUMN.getId().getPath(),
				texture(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN),
				texture("red_sandstone_column_end"));
		directionalBlock(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN.get(), file);
		simpleBlockItem(MSBlocks.CHISELED_RED_SANDSTONE_COLUMN.get(), file);
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CHIPBOARD);
		simpleBlockWithItem(MSBlocks.WOOD_SHAVINGS);
		simpleBlockWithItem(MSBlocks.CARVED_HEAVY_PLANKS);
		simpleBlockWithItem(MSBlocks.CARVED_PLANKS);
		simpleBlockWithItem(MSBlocks.POLISHED_UNCARVED_WOOD);
		simpleBlockWithItem(MSBlocks.CARVED_KNOTTED_WOOD);
		
		simpleBlockWithItem(MSBlocks.DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.BRIGHT_DENSE_CLOUD);
		simpleBlockWithItem(MSBlocks.SUGAR_CUBE);
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
	}
	
	private void simpleStairsWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		stairsBlock((StairBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	private void simpleSlabWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		slabBlock((SlabBlock) block.get(), blockTexture(baseBlock.get()), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	private void simpleCrossBlock(RegistryObject<Block> block)
	{
		simpleBlock(block.get(), models().cross(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(), blockTexture(block.get())).renderType("cutout"));
	}
	
	private void trapdoorBlockItem(RegistryObject<Block> block)
	{
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_bottom"));
	}
	
	private void cutoutBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), models().cubeAll(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(), blockTexture(block.get())).renderType("cutout"));
	}
	
	public void simpleCutoutDoorBlock(RegistryObject<Block> block)
	{
		String id = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath();
		doorBlockWithRenderType((DoorBlock) block.get(), modLoc("block/" + id + "_bottom"), modLoc("block/" + id + "_top"), "cutout");
	}
	
	public void simpleButtonBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		buttonBlock((ButtonBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simplePressurePlateBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		pressurePlateBlock((PressurePlateBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simpleFenceBlock(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		fenceBlock((FenceBlock) block.get(), blockTexture(baseBlock.get()));
	}
	
	public void simpleWallBlock(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		wallBlock((WallBlock) block.get(), blockTexture(baseBlock.get()));
	}
	
	public void simpleFenceGateBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		fenceGateBlock((FenceGateBlock) block.get(), blockTexture(baseBlock.get()));
		blockItem(block);
	}
	
	public void simpleLogBlockWithItem(RegistryObject<Block> block) {
		logBlock((RotatedPillarBlock) block.get());
		blockItem(block);
	}
	
	public void simpleAxisBlockWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock1, RegistryObject<Block> baseBlock2)
	{
		axisBlock(((RotatedPillarBlock) block.get()), blockTexture(baseBlock1.get()), blockTexture(baseBlock2.get()));
		blockItem(block);
	}
	
	public void blockItem(RegistryObject<Block> block)
	{
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
	}
	
	public static ResourceLocation texture(RegistryObject<? extends Block> block)
	{
		return block.getId().withPrefix(ModelProvider.BLOCK_FOLDER + "/");
	}
	
	public static ResourceLocation texture(String path)
	{
		return new ResourceLocation(Minestuck.MOD_ID, ModelProvider.BLOCK_FOLDER + "/" + path);
	}
}
