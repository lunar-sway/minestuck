package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMinestuckLog extends BlockLog
{
	public static final PropertyEnum<BlockType> VARIANT = PropertyEnum.create("variant", BlockType.class);
	
	public BlockMinestuckLog()
	{
		super();
		setCreativeTab(Minestuck.tabMinestuck);
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockType.VINE_OAK).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
		setUnlocalizedName("logMinestuck");
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT, LOG_AXIS});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockType.values()[meta&3]);
		iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.values()[(meta>>2)&3]);
		
		return iblockstate;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = state.getValue(VARIANT).ordinal();
		
		i |= state.getValue(LOG_AXIS).ordinal()<<2;
		
		return i;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		BlockType type = state.getValue(VARIANT);
		if(state.getValue(LOG_AXIS).equals(EnumAxis.Y))
			return type.topColor;
		else return type.sideColor;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		for(BlockType type : BlockType.values())
			list.add(new ItemStack(itemIn, 1, type.ordinal()));
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
	
	public static enum BlockType implements IStringSerializable
	{
		VINE_OAK("vine_oak", "vineOak", MapColor.WOOD, MapColor.OBSIDIAN),
		FLOWERY_VINE_OAK("flowery_vine_oak", "floweryVineOak", MapColor.WOOD, MapColor.OBSIDIAN);
		
		private final String name;
		private final String unlocalizedName;
		private final MapColor topColor, sideColor;
		
		private BlockType(String name, String unlocalizedName, MapColor topColor, MapColor sideColor)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.topColor = topColor;
			this.sideColor = sideColor;
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