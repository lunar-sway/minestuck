package com.mraof.minestuck.block;

import java.util.List;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
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
		return state.getValue(VARIANT).getMetadata();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, BlockType.getFromMeta(meta));
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
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
			list.add(new ItemStack(itemIn, 1, type.getMetadata()));
	}
	
	public static enum BlockType implements IStringSerializable
	{
		COARSE(0, "coarse_stone", "coarse", 2.0F, MapColor.STONE),
		//Space for decorative coarse stone
		SHADE_BRICK(2, "shade_brick", "shadeBrick", 1.5F, MapColor.BLUE),
		SHADE_SMOOTH(3, "shade_smooth", "shadeSmooth", 1.5F, MapColor.BLUE),
		FROST_BRICK(4, "frost_brick", "frostBrick", 1.5F, MapColor.ICE),
		FROST_TILE(5, "frost_tile", "frostTile", 1.5F, MapColor.ICE),
		FROST_CHISELED(6, "frost_chiseled", "frostChiseled", 1.5F, MapColor.ICE);
		
		private final int metadata;
		private final String name;
		private final String unlocalizedName;
		private final float hardness;
		private final MapColor color;
		
		private BlockType(int metadata, String name, String unlocalizedName, float hardness, MapColor color)
		{
			this.metadata = metadata;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.hardness = hardness;
			this.color = color;
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
		
		public int getMetadata()
		{
			return metadata;
		}
		
		public static BlockType getFromMeta(int metadata)
		{
			for(BlockType type : values())
				if(metadata == type.metadata)
					return type;
			return BlockType.COARSE;
		}
	}
}