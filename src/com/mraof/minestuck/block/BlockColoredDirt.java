package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class BlockColoredDirt extends Block
{
	
	public static enum BlockType implements IStringSerializable
	{
		BLUE("blue"),
		THOUGHT("thought");
		
		public final String name;
		BlockType(String name)
		{
			this.name = name;
		}
		@Override
		public String getName()
		{
			return name;
		}
		
	}
	
	public static final PropertyEnum BLOCK_TYPE = PropertyEnum.create("block_type", BlockType.class);
	
	public BlockColoredDirt()
	{
		super(Material.GROUND);
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		setSoundType(SoundType.GROUND);
		setDefaultState(getBlockState().getBaseState().withProperty(BLOCK_TYPE, BlockType.BLUE));
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
		case BLUE: return MapColor.BLUE;
		case THOUGHT: return MapColor.LIME;
		default: return super.getMapColor(state, worldIn, pos);
		}
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
	{
		return plantable == Blocks.SAPLING || super.canSustainPlant(state, world, pos, direction, plantable);
	}
	
}