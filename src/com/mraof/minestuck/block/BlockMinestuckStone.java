package com.mraof.minestuck.block;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMinestuckStone extends Block
{
	public static final PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);
	
	public BlockMinestuckStone()
	{
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockType.COARSE));
		setCreativeTab(Minestuck.tabMinestuck);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("stoneMinestuck");
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(VARIANT).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, BlockType.values()[meta%BlockType.values().length]);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return blockState.getValue(VARIANT).hardness;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for(BlockType type : BlockType.values())
			list.add(new ItemStack(itemIn, 1, type.ordinal()));
	}
	
	public static enum BlockType implements IStringSerializable
	{
		COARSE("coarse_stone", "coarse", 2.0F),
		SHADE("shade_brick", "shadeBrick", 1.5F);
		
		private final String name;
		private final String unlocalizedName;
		private final float hardness;
		private BlockType(String name, float hardness)
		{
			this(name, name, hardness);
		}
		
		private BlockType(String name, String unlocalizedName, float hardness)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.hardness = hardness;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		public String getUnlocalizedName()
		{
			return unlocalizedName;
		}
	}
}