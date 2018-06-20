package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.item.TabMinestuck;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMinestuckPlanks extends Block
{
	public static final PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);
	
	public BlockMinestuckPlanks()
	{
		super(Material.WOOD);
		setCreativeTab(TabMinestuck.instance);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockType.RAINBOW));
		setUnlocalizedName("planksMinestuck");
		this.setHardness(2.0F);
		this.setSoundType(SoundType.WOOD);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockType.values()[Math.min(meta, 12)]);
		return iblockstate;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = state.getValue(VARIANT).ordinal();
		return i;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		BlockType type = state.getValue(VARIANT);
		return type.color;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(BlockType type : BlockType.values())
		{
			items.add(new ItemStack(this, 1, type.ordinal()));
		}
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).ordinal());
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(VARIANT).ordinal();
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 5;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 5;
	}
	
	public enum BlockType implements IStringSerializable
	{
		ASPECT_BLOOD("aspect_blood", "aspectBlood", MapColor.WOOD),
		ASPECT_BREATH("aspect_breath", "aspectBreath", MapColor.WOOD),
		ASPECT_DOOM("aspect_doom", "aspectDoom", MapColor.WOOD),
		ASPECT_HEART("aspect_heart", "aspectHeart", MapColor.WOOD),
		ASPECT_HOPE("aspect_hope", "aspectHope", MapColor.WOOD),
		ASPECT_LIFE("aspect_life", "aspectLife", MapColor.WOOD),
		ASPECT_LIGHT("aspect_light", "aspectLight", MapColor.WOOD),
		ASPECT_MIND("aspect_mind", "aspectMind", MapColor.WOOD),
		ASPECT_RAGE("aspect_rage", "aspectRage", MapColor.WOOD),
		ASPECT_SPACE("aspect_space", "aspectSpace", MapColor.WOOD),
		ASPECT_TIME("aspect_time", "aspectTime", MapColor.WOOD),
		ASPECT_VOID("aspect_void", "aspectVoid", MapColor.WOOD),
		RAINBOW("rainbow", "rainbow", MapColor.WOOD);
		
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