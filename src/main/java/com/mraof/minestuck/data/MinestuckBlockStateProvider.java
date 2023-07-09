package com.mraof.minestuck.data;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class MinestuckBlockStateProvider extends BlockStateProvider {
	public MinestuckBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Minestuck.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		
		simpleBlock(MSBlocks.UNCARVED_WOOD.get());
		
	}
}
