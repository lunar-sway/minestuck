package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class MinestuckBlockStateProvider extends BlockStateProvider
{
	public MinestuckBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		
		blockWithItem(MSBlocks.UNCARVED_WOOD.get());
		
	}
	
	private void blockWithItem(Block block)
	{
		simpleBlock(block, new ConfiguredModel(cubeAll(block)));
		simpleBlockItem(block, cubeAll(block));
	}
	
	public void simpleBlockItem(Block block, ModelFile model)
	{
		itemModels().getBuilder(key(block).getPath()).parent(model);
	}
	
	public void simpleBlock(Block block, ConfiguredModel... models)
	{
		getVariantBuilder(block).partialState().setModels(models);
	}
	
	private ResourceLocation key(Block block)
	{
		return ForgeRegistries.BLOCKS.getKey(block);
	}
}
