package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock3;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockAlchemiterUpgrades extends BlockLargeMachine {
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.BASE_CORNER_LEFT, EnumParts.BASE_CORNER_RIGHT, EnumParts.BASE_SIDE, EnumParts.PLACEHOLDER);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.CHEST, EnumParts.HOPPER, EnumParts.CRAFTING, EnumParts.LIBRARY);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.GRISTWIDGET, EnumParts.CAPTCHA_CARD, EnumParts.DROPPER, EnumParts.BOONDOLLAR);
	public static final PropertyEnum<EnumParts> PART4 = PropertyEnum.create("part", EnumParts.class, EnumParts.BLENDER, EnumParts.PLACEHOLDER_0, EnumParts.PLACEHOLDER_1, EnumParts.PLACEHOLDER_2);
	
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	private int index;
	
	public BlockAlchemiterUpgrades()
	{
		this(0, PART1);
	}
	
	public BlockAlchemiterUpgrades(int index, PropertyEnum<EnumParts> part)
	{
		this.index = index;
		PART = part;
		setUnlocalizedName("jumper_block_extension");
		
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		EnumParts parts = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		return parts.BOUNDING_BOX[facing.getHorizontalIndex()];
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return true;
		if(index == 2||index == 3)
			return true;
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART).hasTileEntity();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		//if(meta / 4 + index * 4 == EnumParts.CABLE.ordinal())
		//	return new TileEntityJumperBlock();
		//else
			return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		/*if(!state.getValue(PART).isPlug() && !state.getValue(PART).isShunt() && !state.getValue(PART).isCable())
		{
			BlockPos mainPos = getMainPos(state, pos, worldIn);
			TileEntity te = worldIn.getTileEntity(mainPos);
			IBlockState otherState = worldIn.getBlockState(mainPos);
			if(te instanceof TileEntityJumperBlock && otherState.getValue(DIRECTION) == state.getValue(DIRECTION))
			{
				((TileEntityJumperBlock) te).setBroken();
			}
		}
		*/
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityJumperBlock)
			((TileEntityJumperBlock) te).checkStates();
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);
		
		if(te instanceof TileEntityItemStack)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityItemStack) te).getStack());
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		} else if(te instanceof TileEntityJumperBlock)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityJumperBlock) te).getUpgrade(0));
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, 1.0f, false, harvesters.get());
			
			for (ItemStack item : items)
			{
				spawnAsEntity(worldIn, pos, item);
			}
		}
	}
	
	//Block state handling
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, PART1, DIRECTION);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState defaultState = getDefaultState();
		EnumParts part = EnumParts.values()[ index * 4  + meta / 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		
		EnumParts part = state.getValue(PART);
		
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		state = state.withProperty(PART, part);
		return state;
		} 
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			return state.getValue(((BlockAlchemiterUpgrades) state.getBlock()).PART);
		else return null;
	}
	
	public static IBlockState getBlockState(EnumParts parts, EnumFacing direction)
	{
		BlockAlchemiterUpgrades block = MinestuckBlocks.alchemiterUpgrades[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : 3];
		IBlockState state = block.getDefaultState();
		return state.withProperty(block.PART, parts).withProperty(DIRECTION, direction);
	}
	
	public static IBlockState getState(EnumParts parts, EnumFacing facing)
    {
        BlockAlchemiterUpgrades block = MinestuckBlocks.alchemiterUpgrades[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : PART4.getAllowedValues().contains(parts) ? 3 : 4];
        return block.getDefaultState().withProperty(block.PART, parts).withProperty(DIRECTION, facing);
    }
	
	@Override
	public Item getItemFromMachine() 
	{
		return new ItemStack(MinestuckBlocks.alchemiter[0]).getItem();
	}
	
	
	 /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	
	
	public enum EnumParts implements IStringSerializable
	{
		BASE_CORNER_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE_CORNER_RIGHT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE_SIDE(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		CHEST(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		HOPPER(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CRAFTING(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		LIBRARY(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		CAPTCHA_CARD(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		GRISTWIDGET(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		DROPPER(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BOONDOLLAR(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BLENDER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER_0(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER_1(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		PLACEHOLDER_2(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D));
		
		private final AxisAlignedBB[] BOUNDING_BOX;
		
		EnumParts(AxisAlignedBB bb)
		{
			BOUNDING_BOX = new AxisAlignedBB[4];
			BOUNDING_BOX[0] = bb;
			BOUNDING_BOX[1] = new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
			BOUNDING_BOX[2] = new AxisAlignedBB(1 - bb.maxX, bb.minY, 1- bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
			BOUNDING_BOX[3] = new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);
			
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
		
		public boolean hasTileEntity()
		{
			return false;
		}
		
	}
	
	public static BlockAlchemiterUpgrades[] createBlocks()
	{
		return new BlockAlchemiterUpgrades[] {new BlockJumperBlock1(), new BlockJumperBlock2(), new BlockJumperBlock3(), new BlockJumperBlock4()};
	}
	
	private static class BlockJumperBlock1 extends BlockAlchemiterUpgrades
	{
		public BlockJumperBlock1()
		{
			super(0, PART1);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART1, DIRECTION);
		}
	}
	
	private static class BlockJumperBlock2 extends BlockAlchemiterUpgrades
	{
		public BlockJumperBlock2()
		{
			super(1, PART2);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART2, DIRECTION);
		}
	}
	
	private static class BlockJumperBlock3 extends BlockAlchemiterUpgrades
	{
		public BlockJumperBlock3()
		{
			super(2, PART3);
		}
		
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART3, DIRECTION);
		}
	}
	
	private static class BlockJumperBlock4 extends BlockAlchemiterUpgrades
	{
		public BlockJumperBlock4()
		{
			super(3, PART4);
		}
		
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART4, DIRECTION);
		}
	}
	

}
