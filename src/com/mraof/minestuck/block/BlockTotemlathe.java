package com.mraof.minestuck.block;

import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockTotemLathe extends BlockLargeMachine
{
	
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.BOTTOM_LEFT, EnumParts.BOTTOM_MIDLEFT, EnumParts.BOTTOM_MIDRIGHT, EnumParts.BOTTOM_RIGHT, EnumParts.BOTTOM_LEFT_CARD_1, EnumParts.BOTTOM_LEFT_CARD_2);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.MID_LEFT, EnumParts.ROD_LEFT, EnumParts.ROD_RIGHT, EnumParts.MID_RIGHT, EnumParts.ROD_LEFT_ACTIVE, EnumParts.ROD_RIGHT_CARVED, EnumParts.MID_RIGHT_ACTIVE);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOP_LEFT, EnumParts.TOP_MIDLEFT, EnumParts.TOP_MIDRIGHT);
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	private int index;
	
	public BlockTotemLathe()
	{
		this(0, PART1);
	}
	
	public BlockTotemLathe(int index, PropertyEnum<EnumParts> part)
	{
		this.index = index;
		PART = part;
		setUnlocalizedName("totem_lathe");
		
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
		
		BlockPos mainPos = getMainPos(state, pos);
		TileEntity te = worldIn.getTileEntity(mainPos);
		if(te instanceof TileEntityTotemLathe)
			((TileEntityTotemLathe) te).onRightClick(playerIn, state);
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART).isBottomLeft() || state.getValue(PART).isRodRight();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta / 4 + index * 4 == EnumParts.BOTTOM_LEFT.ordinal())
			return new TileEntityTotemLathe();
		else if(meta / 4 + index * 4 == EnumParts.ROD_RIGHT.ordinal())
			return new TileEntityItemStack();
		else return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(!state.getValue(PART).isRodRight() && !state.getValue(PART).isBottomLeft())
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			IBlockState otherState = worldIn.getBlockState(mainPos);
			if(te instanceof TileEntityTotemLathe && otherState.getValue(DIRECTION) == state.getValue(DIRECTION))
			{
				((TileEntityTotemLathe) te).setBroken();
			}
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityTotemLathe)
			((TileEntityTotemLathe) te).checkStates();
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
		} else if(te instanceof TileEntityTotemLathe)
		{
			int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			List<ItemStack> items = new ArrayList<>();
			items.add(((TileEntityTotemLathe) te).getCard1());
			items.add(((TileEntityTotemLathe) te).getCard2());
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
		EnumParts part = EnumParts.values()[index * 4 + meta / 4];
		EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
		
		return defaultState.withProperty(PART, part).withProperty(DIRECTION, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumParts part = state.getValue(PART);
		if(part.isBottomLeft())
			part = EnumParts.BOTTOM_LEFT;
		else if(part.isRodLeft())
			part = EnumParts.ROD_LEFT;
		else if(part.isRodRight())
			part = EnumParts.ROD_RIGHT;
		else if(part.isMiddleRight())
			part = EnumParts.MID_RIGHT;
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		if(part.isBottomLeft())
		{
			BlockPos mainPos = getMainPos(state, pos);
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof TileEntityTotemLathe)
			{
				TileEntityTotemLathe lathe = (TileEntityTotemLathe) te;
				if(!lathe.getCard2().isEmpty())
					return state.withProperty(PART, EnumParts.BOTTOM_LEFT_CARD_2);
				else if(!lathe.getCard1().isEmpty())
					return state.withProperty(PART, EnumParts.BOTTOM_LEFT_CARD_1);
			}
		} else if(part.isRodLeft())
		{
			if(worldIn.getBlockState(pos.offset(facing.rotateYCCW())).equals(getState(EnumParts.ROD_RIGHT, facing)))
				return state.withProperty(PART, EnumParts.ROD_LEFT_ACTIVE);
		} else if(part.isMiddleRight())
		{
			if(worldIn.getBlockState(pos.offset(facing.rotateY())).equals(getState(EnumParts.ROD_RIGHT, facing)))
				return state.withProperty(PART, EnumParts.MID_RIGHT_ACTIVE);
		} else if(part.isRodRight())
		{
			TileEntity te = worldIn.getTileEntity(pos);
			if(te instanceof TileEntityItemStack && AlchemyRecipes.hasDecodedItem(((TileEntityItemStack) te).getStack()))
				return state.withProperty(PART, EnumParts.ROD_RIGHT_CARVED);
		}
		
		return state;
	}
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos)
	{
		EnumFacing facing = state.getValue(DIRECTION);
		switch(state.getValue(PART))
		{
		
			case BOTTOM_RIGHT:
				return pos.offset(facing.rotateY(),3);
			case BOTTOM_MIDRIGHT:
				return pos.offset(facing.rotateY(),2);
			case BOTTOM_MIDLEFT:
				return pos.offset(facing.rotateY(),1);
			case BOTTOM_LEFT: case BOTTOM_LEFT_CARD_1: case BOTTOM_LEFT_CARD_2:
				return pos;
			case MID_RIGHT: case MID_RIGHT_ACTIVE:
				return pos.down(1).offset(facing.rotateY(),3);
			case ROD_RIGHT: case ROD_RIGHT_CARVED:
				return pos.down(1).offset(facing.rotateY(),2);
			case ROD_LEFT: case ROD_LEFT_ACTIVE:
				return pos.down(1).offset(facing.rotateY(),1);
			case MID_LEFT:
				return pos.down(1);
			case TOP_MIDRIGHT:
				return pos.down(2).offset(facing.rotateY(),2);
			case TOP_MIDLEFT:
				return pos.down(2).offset(facing.rotateY(),1);
			case TOP_LEFT:
				return pos.down(2);
			}
			return pos;
	}
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockTotemLathe)
			return state.getValue(((BlockTotemLathe) state.getBlock()).PART);
		else return null;
	}
	
	public static IBlockState getState(EnumParts parts, EnumFacing facing)
	{
		BlockTotemLathe block = MinestuckBlocks.totemlathe[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : 2];
		return block.getDefaultState().withProperty(block.PART, parts).withProperty(DIRECTION, facing);
	}
	
	public enum EnumParts implements IStringSerializable
	{
		BOTTOM_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D)),
		BOTTOM_MIDLEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 12/16D)),
		BOTTOM_MIDRIGHT(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 12/16D, 12/16D)),
		BOTTOM_RIGHT(	new AxisAlignedBB(0.0D, 0.0D, 1/16D, 12/16D, 1.0D, 11/16D)),
		
		MID_LEFT(		new AxisAlignedBB(0.0D, 0.0D,  1/16D, 1.0D,  1.0D, 12/16D)),
		ROD_LEFT(		new AxisAlignedBB(0.0D, 4/16D,  3/16D, 1.0D,  10/16D, 9/16D)),
		ROD_RIGHT(		new AxisAlignedBB(0.0D, 4/16D, 3/16D, 1.0D,  10/16D, 9/16D)),
		MID_RIGHT(		new AxisAlignedBB(0.0D, 0.0D,  3/16D, 14/16D, 10/16D, 9/16D)),
		
		TOP_MIDRIGHT(	new AxisAlignedBB(0.0D, 0.0D, 1/16D, 10/16D,  1.0D, 11/16D)),
		TOP_MIDLEFT(	new AxisAlignedBB(0.0D, 3/16D, 1/16D, 1.0D,  1.0D, 11/16D)),
		TOP_LEFT(		new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D,  1.0D, 11/16D)),
		
		BOTTOM_LEFT_CARD_1(new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D)),
		BOTTOM_LEFT_CARD_2(new AxisAlignedBB(0.0D, 0.0D, 1/16D, 1.0D, 1.0D, 13/16D)),
		ROD_LEFT_ACTIVE(new AxisAlignedBB(0.0D, 4/16D,  3/16D, 1.0D,  10/16D, 9/16D)),
		ROD_RIGHT_CARVED(new AxisAlignedBB(0.0D, 4/16D, 3/16D, 1.0D,  10/16D, 9/16D)),
		MID_RIGHT_ACTIVE(new AxisAlignedBB(0.0D, 0.0D,  3/16D, 14/16D, 10/16D, 9/16D));
		
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
		
		public boolean isBottomLeft()
		{
			return this == BOTTOM_LEFT || this == BOTTOM_LEFT_CARD_1 || this == BOTTOM_LEFT_CARD_2;
		}
		
		public boolean isRodLeft()
		{
			return this == ROD_LEFT || this == ROD_LEFT_ACTIVE;
		}
		
		public boolean isRodRight()
		{
			return this == ROD_RIGHT || this == ROD_RIGHT_CARVED;
		}
		
		public boolean isMiddleRight()
		{
			return this == MID_RIGHT || this == MID_RIGHT_ACTIVE;
		}
	}
	
	public static BlockTotemLathe[] createBlocks()
	{
		return new BlockTotemLathe[] {new BlockTotemLathe(), new BlockTotemLathe2(), new BlockTotemLathe3()};
	}
	
	private static class BlockTotemLathe2 extends BlockTotemLathe
	{
		public BlockTotemLathe2()
		{
			super(1, PART2);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART2, DIRECTION);
		}
	}
	
	private static class BlockTotemLathe3 extends BlockTotemLathe
	{
		public BlockTotemLathe3()
		{
			super(2, PART3);
		}
		
		@Override
		protected BlockStateContainer createBlockState()
		{
			return new BlockStateContainer(this, PART3, DIRECTION);
		}
	}

	@Override
	public Item getItemFromMachine() {
		return new ItemStack(MinestuckBlocks.totemlathe[0]).getItem();
	}
}