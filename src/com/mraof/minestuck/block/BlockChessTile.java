package com.mraof.minestuck.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

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
		super(Material.ground);
		setHardness(0.5F);
		
		setUnlocalizedName("chessTile");
		setDefaultState(getDefaultState().withProperty(BLOCK_TYPE, BlockType.BLACK));
		this.setCreativeTab(Minestuck.tabMinestuck);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) 
	{
		for(int i = 0; i < BlockType.values().length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type)
	{
		return true;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		switch((BlockType) state.getValue(BLOCK_TYPE))
		{
		case WHITE: return MapColor.snowColor;
		case LIGHT_GREY: return MapColor.silverColor;
		case DARK_GREY: return MapColor.grayColor;
		case BLACK: return MapColor.blackColor;
		default: return super.getMapColor(state);
		}
	}
}