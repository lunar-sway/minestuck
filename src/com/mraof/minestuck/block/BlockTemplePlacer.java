package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.world.gen.structure.StructureTemple;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockTemplePlacer extends Block
{

	public BlockTemplePlacer() 
	{
		super(Material.BARRIER);
		setUnlocalizedName("templePlacer");
		//setCreativeTab(TabMinestuck.instance);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		if(!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			StructureTemple.generateStructure(worldIn, pos);
		}
		super.onBlockAdded(worldIn, pos, state);
	}
	
	
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) 
	{
		//if(new ChunkProviderClient(worldIn).getLoadedChunk(pos.getX() / 16, pos.getY() / 16) != null)
		{
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			StructureTemple.generateStructure(worldIn, pos);
		}
		super.updateTick(worldIn, pos, state, rand);
	}
}
