package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MinestuckBlockStateProvider extends BlockStateProvider
{
	public MinestuckBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels()
	{
		
		simpleBlockWithItem(MSBlocks.UNCARVED_WOOD.get());
		
		simpleBlockWithItem(MSBlocks.NATIVE_SULFUR.get());
	}
	
	private void simpleBlockWithItem(Block block)
	{
		simpleBlockWithItem(block, cubeAll(block));
	}
}
