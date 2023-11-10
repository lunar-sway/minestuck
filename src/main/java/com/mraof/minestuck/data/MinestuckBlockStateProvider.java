package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
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
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD);
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
		
		simpleBlockWithItem(MSBlocks.SWISS_CHEESE);
		simpleBlockWithItem(MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleBlockWithItem(MSBlocks.AMERICAN_CHEESE);
		simpleBlockWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE);
		simpleBlockWithItem(MSBlocks.FETA_CHEESE);
		simpleBlockWithItem(MSBlocks.CHHURPI);
		simpleBlockWithItem(MSBlocks.CHHURPI_BRICKS);
		simpleBlockWithItem(MSBlocks.CHISELED_CHHURPI_BRICKS);
		simpleBlockWithItem(MSBlocks.CRACKED_CHHURPI_BRICKS);
		simpleBlockWithItem(MSBlocks.GILDED_CHHURPI_BRICKS);
		simpleBlockWithItem(MSBlocks.CHEESE_PATH);
		
		simpleAxisBlockWithItem(MSBlocks.CHHURPI_PILLAR);
		
		simpleStairsWithItem(MSBlocks.SWISS_CHEESE_STAIRS, MSBlocks.SWISS_CHEESE);
		simpleStairsWithItem(MSBlocks.SMOOTH_SWISS_CHEESE_STAIRS, MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleStairsWithItem(MSBlocks.AMERICAN_CHEESE_STAIRS, MSBlocks.AMERICAN_CHEESE);
		simpleStairsWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE_STAIRS, MSBlocks.SMOOTH_AMERICAN_CHEESE);
		simpleStairsWithItem(MSBlocks.CHHURPI_STAIRS, MSBlocks.CHHURPI);
		simpleStairsWithItem(MSBlocks.CHHURPI_BRICK_STAIRS, MSBlocks.CHHURPI_BRICKS);
		simpleStairsWithItem(MSBlocks.CHISELED_CHHURPI_BRICK_STAIRS, MSBlocks.CHISELED_CHHURPI_BRICKS);
		simpleStairsWithItem(MSBlocks.CRACKED_CHHURPI_BRICK_STAIRS, MSBlocks.CRACKED_CHHURPI_BRICKS);
		simpleStairsWithItem(MSBlocks.GILDED_CHHURPI_BRICK_STAIRS, MSBlocks.GILDED_CHHURPI_BRICKS);
		
		simpleSlabWithItem(MSBlocks.SWISS_CHEESE_SLAB, MSBlocks.SWISS_CHEESE);
		simpleSlabWithItem(MSBlocks.SMOOTH_SWISS_CHEESE_SLAB, MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleSlabWithItem(MSBlocks.AMERICAN_CHEESE_SLAB, MSBlocks.AMERICAN_CHEESE);
		simpleSlabWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE_SLAB, MSBlocks.SMOOTH_AMERICAN_CHEESE);
		simpleSlabWithItem(MSBlocks.CHHURPI_SLAB, MSBlocks.CHHURPI);
		simpleSlabWithItem(MSBlocks.CHHURPI_BRICK_SLAB, MSBlocks.CHHURPI_BRICKS);
		simpleSlabWithItem(MSBlocks.CHISELED_CHHURPI_BRICK_SLAB, MSBlocks.CHISELED_CHHURPI_BRICKS);
		simpleSlabWithItem(MSBlocks.CRACKED_CHHURPI_BRICK_SLAB, MSBlocks.CRACKED_CHHURPI_BRICKS);
		simpleSlabWithItem(MSBlocks.GILDED_CHHURPI_BRICK_SLAB, MSBlocks.GILDED_CHHURPI_BRICKS);
		
		simpleWallBlock(MSBlocks.CHHURPI_WALL, MSBlocks.CHHURPI);
		simpleWallBlock(MSBlocks.CHHURPI_BRICK_WALL, MSBlocks.CHHURPI_BRICKS);
		simpleWallBlock(MSBlocks.CHISELED_CHHURPI_BRICK_WALL, MSBlocks.CHISELED_CHHURPI_BRICKS);
		simpleWallBlock(MSBlocks.CRACKED_CHHURPI_BRICK_WALL, MSBlocks.CRACKED_CHHURPI_BRICKS);
		simpleWallBlock(MSBlocks.GILDED_CHHURPI_BRICK_WALL, MSBlocks.GILDED_CHHURPI_BRICKS);
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
	
	public void simpleAxisBlockWithItem(RegistryObject<Block> block)
	{
		axisBlock(((RotatedPillarBlock) block.get()), new ResourceLocation("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_side"), new ResourceLocation("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath() + "_top"));
		blockItem(block);
	}
	
	public void blockItem(RegistryObject<Block> block)
	{
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
	}
}
