package com.mraof.minestuck.block;

import com.mraof.minestuck.item.TabMinestuck;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMinestuckStone extends Block
{
	public static final int COARSE_META = BlockType.COARSE.getMetadata();
	public static final int COARSE_CHISELED_META = BlockType.COARSE_CHISELED.getMetadata();
	public static final int SHADE_META = BlockType.SHADE_BRICK.getMetadata();
	public static final int SHADE_SMOOTH_META = BlockType.SHADE_SMOOTH.getMetadata();
	public static final int FROST_META = BlockType.FROST_BRICK.getMetadata();
	public static final int FROST_TILE_META = BlockType.FROST_TILE.getMetadata();
	public static final int FROST_CHISELED_META = BlockType.FROST_CHISELED.getMetadata();
	public static final int CAST_META = BlockType.CAST_IRON.getMetadata();
	public static final int CAST_CHISELED_META = BlockType.CAST_IRON_CHISELED.getMetadata();
	
	public static final PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);
	
	public BlockMinestuckStone()
	{
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockType.COARSE));
		setCreativeTab(TabMinestuck.instance);
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
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockType type : BlockType.values())
			items.add(new ItemStack(this, 1, type.getMetadata()));
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state.getValue(VARIANT).color;
	}
	
	public enum BlockType implements IStringSerializable
	{
		COARSE(0, "coarse_stone", "coarse", 2.0F, MapColor.STONE),
		COARSE_CHISELED(1, "coarse_chiseled", "coarseChiseled", 2.0F, MapColor.STONE),
		SHADE_BRICK(2, "shade_brick", "shadeBrick", 1.5F, MapColor.BLUE),
		SHADE_SMOOTH(3, "shade_smooth", "shadeSmooth", 1.5F, MapColor.BLUE),
		FROST_BRICK(4, "frost_brick", "frostBrick", 1.5F, MapColor.ICE),
		FROST_TILE(5, "frost_tile", "frostTile", 1.5F, MapColor.ICE),
		FROST_CHISELED(6, "frost_chiseled", "frostChiseled", 1.5F, MapColor.ICE),
		CAST_IRON(7, "cast_iron", "castIron", 3.0F, MapColor.IRON),
		CAST_IRON_CHISELED(8, "cast_iron_chiseled", "castIronChiseled", 3.0F, MapColor.IRON),
		BLACK_STONE(9, "black_stone", "blackStone", 2.5F, MapColor.BLACK),
		MYCELIUM_BRICK(10, "mycelium_brick", "myceliumBrick", 1.5F, MapColor.MAGENTA);
		
		private final int metadata;
		private final String name;
		private final String unlocalizedName;
		private final float hardness;
		private final MapColor color;
		
		BlockType(int metadata, String name, String unlocalizedName, float hardness, MapColor color)
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