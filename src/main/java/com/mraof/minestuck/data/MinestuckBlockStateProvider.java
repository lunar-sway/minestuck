package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
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
		simpleBlockWithItem(MSBlocks.SWISS_CHEESE);
		simpleBlockWithItem(MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleBlockWithItem(MSBlocks.AMERICAN_CHEESE);
		simpleBlockWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE);
		
		simpleStairsWithItem(MSBlocks.SWISS_CHEESE_STAIRS, MSBlocks.SWISS_CHEESE);
		simpleStairsWithItem(MSBlocks.SMOOTH_SWISS_CHEESE_STAIRS, MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleStairsWithItem(MSBlocks.AMERICAN_CHEESE_STAIRS, MSBlocks.AMERICAN_CHEESE);
		simpleStairsWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE_STAIRS, MSBlocks.SMOOTH_AMERICAN_CHEESE);
		
		simpleSlabWithItem(MSBlocks.SWISS_CHEESE_SLAB, MSBlocks.SWISS_CHEESE);
		simpleSlabWithItem(MSBlocks.SMOOTH_SWISS_CHEESE_SLAB, MSBlocks.SMOOTH_SWISS_CHEESE);
		simpleSlabWithItem(MSBlocks.AMERICAN_CHEESE_SLAB, MSBlocks.AMERICAN_CHEESE);
		simpleSlabWithItem(MSBlocks.SMOOTH_AMERICAN_CHEESE_SLAB, MSBlocks.SMOOTH_AMERICAN_CHEESE);
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR);
	}
	
	private void simpleBlockWithItem(RegistryObject<Block> block)
	{
		simpleBlockWithItem(block.get(), cubeAll(block.get()));
	}
	
	private void simpleStairsWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		stairsBlock((StairBlock) block.get(), blockTexture(baseBlock.get()));
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
	}
	
	private void simpleSlabWithItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock)
	{
		slabBlock((SlabBlock) block.get(), blockTexture(baseBlock.get()), blockTexture(baseBlock.get()));
		simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("minestuck:block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
	}
}
