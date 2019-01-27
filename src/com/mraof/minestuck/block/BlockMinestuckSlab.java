package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.block.MinestuckBlocks.EnumSlabStairMaterial;
import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockMinestuckSlab extends BlockSlab
{
	private boolean isDouble = false;
	private final IBlockState modelState;
	private final EnumSlabStairMaterial ssm;
	
	/**
	 * <code>getVariantProperty()</code> must return a non-null property found in this block's blockstates.
	 * As a result, this dummy property exists to be the return value of that method. It does nothing else.
	 */
	public final static PropertyInteger dummy = PropertyInteger.create("dummy", 0, 1);
	
	public BlockMinestuckSlab(IBlockState modelState, EnumSlabStairMaterial slabStairMaterial, boolean isDouble)
	{
		super(modelState.getMaterial());
		setCreativeTab(TabMinestuck.instance);
		this.modelState = modelState;
		this.isDouble = isDouble;
		this.useNeighborBrightness = true;
		this.ssm = slabStairMaterial;
		
		//TODO: Use the modelState's hardness.
		setHardness(modelState.getMaterial()==Material.WOOD ? 1.0F : 3.0F);
	}
	
	protected BlockStateContainer createBlockState()
	{
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] {dummy}) : new BlockStateContainer(this, new IProperty[] {HALF, dummy});
	}
	
	public IBlockState getModelState()
	{
		return modelState;
	}
	
	@Override
	public String getUnlocalizedName(int meta)
	{
		return super.getUnlocalizedName();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		if(isDouble)	return 0;
		return state.getValue(HALF).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if(isDouble)	return getDefaultState();
		return this.getDefaultState().withProperty(HALF, EnumBlockHalf.values()[meta % 2]); 
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ssm.getSlab());
	}
	
	@Override
	public boolean isDouble()
	{
		return isDouble;
	}
	
	@Override public IProperty<?> getVariantProperty() { return dummy; }
	@Override public Comparable<?> getTypeForItem(ItemStack stack) { return 0; }
}
