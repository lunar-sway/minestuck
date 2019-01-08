package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mraof.minestuck.block.BlockAspectSapling.BlockType;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckRandom;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMinestuckLeaves1 extends BlockMinestuckLeaves
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockType.class);
	
	public BlockMinestuckLeaves1()
	{
		super();
		setUnlocalizedName("leavesMinestuck");
		setDefaultState(blockState.getBaseState().withProperty(VARIANT, BlockType.VINE_OAK).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
//		for (BlockType blocktype : BlockType.values())		//keyword
		BlockType blocktype = BlockType.RAINBOW;
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
	
	public enum BlockType implements IStringSerializable
	{
		VINE_OAK("vine_oak", "vineOak", MapColor.WOOD, MapColor.OBSIDIAN),
		FLOWERY_VINE_OAK("flowery_vine_oak", "floweryVineOak", MapColor.WOOD, MapColor.OBSIDIAN),
		FROST("frost", "frost", MapColor.ICE, MapColor.ICE),
		RAINBOW("rainbow", "rainbow", MapColor.WOOD, MapColor.WOOD);
		
		private final String name;
		private final String unlocalizedName;
		private final MapColor topColor, sideColor;
		
		BlockType(String name, String unlocalizedName, MapColor topColor, MapColor sideColor)
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

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList out = new ArrayList<ItemStack>();
		out.add(new ItemStack(this));
		return out;
	}

	@Override
	protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance)
	{
		if(state.getValue(VARIANT)==BlockType.RAINBOW && worldIn.rand.nextInt(chance) == 0)
		{
			int i = worldIn.rand.nextInt(16);
			spawnAsEntity(worldIn, pos, new ItemStack(Items.DYE, 1, i));
		}
	}

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemStack.EMPTY.getItem();
    }
	
	@Override
    public int damageDropped(IBlockState state)
    {
		if(state.getValue(VARIANT)==BlockType.RAINBOW)
			return 0;
		else
			return BlockAspectSapling.BlockType.values()[getMetaFromState(state)].ordinal();
    }

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {DECAYABLE, CHECK_DECAY, VARIANT});
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
             return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
                        return Blocks.LEAVES.isOpaqueCube(state);
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
}