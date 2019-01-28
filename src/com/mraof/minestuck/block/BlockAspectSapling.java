package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAspectSapling extends BlockBush implements IGrowable
{
	public static final PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
	
	protected BlockAspectSapling()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockType.ASPECT_BLOOD));
		this.setCreativeTab(MinestuckItems.tabMinestuck);
		this.setUnlocalizedName("aspectSapling");
		this.setSoundType(SoundType.PLANT);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return SAPLING_AABB;
	}
	
	/**
	 * Gets the localized name of this block. Used for the statistics page.
	 */
	@Override
	public String getLocalizedName()
	{
		return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockType.ASPECT_BLOOD.getUnlocalizedName() + ".name");
	}
	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return ((BlockType)state.getValue(VARIANT)).ordinal();
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for (BlockType blocktype : BlockType.values())
		{
			items.add(new ItemStack(this, 1, blocktype.ordinal()));
		}
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockType.values()[meta]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = ((BlockType)state.getValue(VARIANT)).ordinal();
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		
	}
	
	public enum BlockType implements IStringSerializable
	{
		ASPECT_BLOOD("aspect_blood", "aspectBlood", MapColor.FOLIAGE),
		ASPECT_BREATH("aspect_breath", "aspectBreath", MapColor.FOLIAGE),
		ASPECT_DOOM("aspect_doom", "aspectDoom", MapColor.FOLIAGE),
		ASPECT_HEART("aspect_heart", "aspectHeart", MapColor.FOLIAGE),
		ASPECT_HOPE("aspect_hope", "aspectHope", MapColor.FOLIAGE),
		ASPECT_LIFE("aspect_life", "aspectLife", MapColor.FOLIAGE),
		ASPECT_LIGHT("aspect_light", "aspectLight", MapColor.FOLIAGE),
		ASPECT_MIND("aspect_mind", "aspectMind", MapColor.FOLIAGE),
		ASPECT_RAGE("aspect_rage", "aspectRage", MapColor.FOLIAGE),
		ASPECT_SPACE("aspect_space", "aspectSpace", MapColor.FOLIAGE),
		ASPECT_TIME("aspect_time", "aspectTime", MapColor.FOLIAGE),
		ASPECT_VOID("aspect_void", "aspectVoid", MapColor.FOLIAGE);
		
		private final String name;
		private final String unlocalizedName;
		private final MapColor color;
		
		BlockType(String name, String unlocalizedName, MapColor color)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
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
	}
}