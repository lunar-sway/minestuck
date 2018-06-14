package com.mraof.minestuck.client.renderer.tileentity;

import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Map;

public class RenderAlchemiter extends TileEntitySpecialRenderer<TileEntityAlchemiter>
{
	@Override
	public void render(TileEntityAlchemiter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		//ModelLoaderRegistry.getModel()
	}
	
	public class AlchemiterStateMapper implements IStateMapper
	{
		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
		{
			return null;
		}
	}
}