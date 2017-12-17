package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockChessTile extends Block
{
	public static enum BlockType implements IStringSerializable
	{
		BLACK("black"),
		WHITE("white"),
		DARK_GREY("dark_grey"),
		LIGHT_GREY("light_grey");
		public final String name;
		BlockType(String resource)
		{
			this.name = resource;
		}
		@Override
		public String getName()
		{
			return name;
		}
	}
	
	public static final PropertyEnum BLOCK_TYPE = PropertyEnum.create("block_type", BlockType.class);
	
	public BlockChessTile()
	{
		super(Material.GROUND);
		setHardness(0.5F);
		
		setUnlocalizedName("chessTile");
		setDefaultState(getDefaultState().withProperty(BLOCK_TYPE, BlockType.BLACK));
		this.setCreativeTab(MinestuckItems.tabMinestuck);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BLOCK_TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BLOCK_TYPE, BlockType.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockType) state.getValue(BLOCK_TYPE)).ordinal();
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(int i = 0; i < BlockType.values().length; i++)
			items.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return true;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		switch((BlockType) state.getValue(BLOCK_TYPE))
		{
		case WHITE: return MapColor.SNOW;
		case LIGHT_GREY: return MapColor.SILVER;
		case DARK_GREY: return MapColor.GRAY;
		case BLACK: return MapColor.BLACK;
		default: return super.getMapColor(state, worldIn, pos);
		}
	}
}