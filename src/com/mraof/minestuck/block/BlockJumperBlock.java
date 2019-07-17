package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
import com.mraof.minestuck.block.BlockJumperBlock.EnumParts;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock2;
//import com.mraof.minestuck.block.BlockJumperBlock.BlockJumperBlock3;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.item.TabMinestuck;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityHolopad;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityJumperBlock;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import com.mraof.minestuck.item.TabMinestuck;

public abstract class BlockJumperBlock extends BlockLargeMachine {
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOP_PLUG, EnumParts.BOTTOM_PLUG, EnumParts.TOP_SHUNT, EnumParts.BOTTOM_SHUNT);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.TOP_CORNER_PLUG, EnumParts.BOTTOM_CORNER_PLUG, EnumParts.BOTTOM_CORNER_SHUNT, EnumParts.TOP_CORNER_SHUNT);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.BORDER_LEFT, EnumParts.BORDER_SIDE, EnumParts.BORDER_RIGHT, EnumParts.SMALL_CORNER);
	public static final PropertyEnum<EnumParts> PART4 = PropertyEnum.create("part", EnumParts.class, EnumParts.BASE_SIDE, EnumParts.BASE_CORNER, EnumParts.CENTER, EnumParts.CABLE);
	protected static final AxisAlignedBB SHUNT_AABB = new AxisAlignedBB(-6/16D, 6/16D, -5/16D, 13/16D, 13/16D, 7/16D);
	protected static final AxisAlignedBB PLUG_AABB = new AxisAlignedBB(-2/16D, 6/16D, -3/16D, 9/16D, 7/16D, 2/16D);
	
	public final PropertyEnum<EnumParts> PART;
	public static final PropertyDirection DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	private int index;
	
	public BlockJumperBlock()
	{
		this(0, PART1);
	}
	
	public BlockJumperBlock(int index, PropertyEnum<EnumParts> part)
	{
		this.index = index;
		PART = part;
		setUnlocalizedName("jumper_block_extension");
		//setCreativeTab(null);
		
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
			return false;
		
		
		BlockPos mainPos = getMainPos(state, pos, worldIn);
		TileEntity te = worldIn.getTileEntity(mainPos);
		
		int id = getUpgradeId(state, pos, worldIn);
		if(te instanceof TileEntityJumperBlock)
			((TileEntityJumperBlock) te).onRightClick(playerIn, state, id);
		return true;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return state.getValue(PART).isCable();
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		if(meta / 4 + index * 4 == EnumParts.CABLE.ordinal())
			return new TileEntityJumperBlock();
		else
			return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
			BlockPos mainPos = getMainPos(state, pos, worldIn);
			TileEntity te = worldIn.getTileEntity(mainPos);
			IBlockState otherState = worldIn.getBlockState(mainPos);
			
			if(te instanceof TileEntityJumperBlock)
			{
				int id = getUpgradeId(state, pos, worldIn);
				TileEntityJumperBlock jbe = ((TileEntityJumperBlock) te);
				jbe.setBroken();
				if(mainPos == pos)
					for(int i = 0; i < 8; i++)
						if(!worldIn.isRemote)jbe.dropItem(true, pos, jbe.getShunt(i));
				
				else if(!worldIn.isRemote)jbe.dropItem(false, jbe.idToPos(i), jbe.getShunt(id));
			}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) 
	{
		TileEntityJumperBlock te = (TileEntityJumperBlock) worldIn.getTileEntity(pos);
		
		if(te != null && !worldIn.isRemote)
		{
			int id = getUpgradeId(state, pos, worldIn);
			te.dropItem(true, pos, te.getUpgrade(id));
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
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
		if(part.isCable())
			part = EnumParts.CABLE;
		else if(part == EnumParts.TOP_SHUNT || part == EnumParts.TOP_PLUG)
			part = EnumParts.TOP_PLUG;
		else if(part == EnumParts.BOTTOM_CORNER_SHUNT || part == EnumParts.BOTTOM_CORNER_PLUG)
			part = EnumParts.BOTTOM_CORNER_PLUG;
		else if(part == EnumParts.TOP_CORNER_SHUNT || part == EnumParts.TOP_CORNER_PLUG)
			part = EnumParts.TOP_CORNER_PLUG;
		else if(part == EnumParts.TOP_SHUNT || part == EnumParts.TOP_PLUG)
			part = EnumParts.TOP_PLUG;
		
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	//TODO
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumParts part = state.getValue(PART);
		
		int id = getUpgradeId(state, pos, worldIn);
		BlockPos mainPos = getMainPos(state, pos, worldIn);
		TileEntity te;
		if(mainPos == null)
			return state;
		else
			te = worldIn.getTileEntity(mainPos);
		
		if(te == null) 
			return state;
		
		if(worldIn.getTileEntity(mainPos) instanceof TileEntityJumperBlock)
		{
			if(part.isPlug())
			{
				
				TileEntityJumperBlock cable = (TileEntityJumperBlock) te;
				
				if(!cable.getUpgrade(id).isEmpty())
				{
					if(part == EnumParts.TOP_PLUG) part = EnumParts.TOP_SHUNT; 
					if(part == EnumParts.BOTTOM_PLUG) part = EnumParts.BOTTOM_SHUNT; 
					if(part == EnumParts.TOP_CORNER_PLUG) part = EnumParts.TOP_CORNER_SHUNT; 
					if(part == EnumParts.BOTTOM_CORNER_PLUG) part = EnumParts.BOTTOM_CORNER_SHUNT; 
				} 
				
			} else if(part.isShunt())
			{
				TileEntityJumperBlock cable = (TileEntityJumperBlock) te;
				
				
				if(cable.getUpgrade(id).isEmpty())
				{
					if(part == EnumParts.TOP_SHUNT) part = EnumParts.TOP_PLUG; 
					if(part == EnumParts.BOTTOM_SHUNT) part = EnumParts.BOTTOM_PLUG; 
					if(part == EnumParts.TOP_CORNER_SHUNT) part = EnumParts.TOP_CORNER_PLUG; 
					if(part == EnumParts.BOTTOM_CORNER_SHUNT) part = EnumParts.BOTTOM_CORNER_PLUG; 
				}
					
					
				}
		}
		state = state.withProperty(PART, part);
		return state;
		} 
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos, World worldIn)
	{
		
		BlockPos mainPos = pos;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		if(index == 3)
		{
			if(part == EnumParts.BASE_SIDE)
			{
				mainPos = pos.offset(facing).offset(facing.rotateY());
				if(!isCable(state, mainPos, worldIn)) 
					mainPos = pos.offset(facing).offset(facing.rotateY(), 2);
			}
			else if(part == EnumParts.BASE_CORNER)
				{
					mainPos = pos.offset(facing);
					if(!isCable(state, mainPos, worldIn)) 
						mainPos = pos.offset(facing, 3).offset(facing.rotateYCCW());
				}
			else if(part == EnumParts.CENTER) 
			{
				for(int offset = 3; offset >= 1; offset--)
				{
					if(isCable(state, pos.offset(facing, offset), worldIn))
					{
						mainPos = pos.offset(facing, offset);
						break;
					}
				}
			}
			else if(part == EnumParts.CABLE) mainPos = pos;
		}
		else if(index == 2)
		{
			if(part == EnumParts.BORDER_LEFT) mainPos = pos.offset(facing, 4);
			else if(part == EnumParts.BORDER_RIGHT) mainPos = pos.offset(facing, 4).offset(facing.rotateY(),2);
			else if(part == EnumParts.BORDER_SIDE) mainPos = pos.offset(facing, 4).offset(facing.rotateY());
			else if(part == EnumParts.SMALL_CORNER) mainPos = pos.offset(facing, 4).offset(facing.rotateYCCW());
		}
		else if(index == 1)
		{
			if((part== EnumParts.BOTTOM_CORNER_PLUG || part== EnumParts.BOTTOM_CORNER_SHUNT) && isCable(state, pos.offset(facing.rotateY(), 1).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 1);
			else if((part== EnumParts.TOP_CORNER_PLUG || part== EnumParts.TOP_CORNER_SHUNT) && isCable(state, pos.offset(facing.rotateY(), 2).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 2);
		}
		else if(index == 0)
			if((part== EnumParts.TOP_PLUG || part== EnumParts.TOP_SHUNT))
		{
			for(int offset = 3; offset >= 1; offset--)
			{
				if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 2), worldIn))
				{
					mainPos = pos.offset(facing, offset).offset(facing.rotateY(), 2);
					break;
				}
			}
		}
		else if((part == EnumParts.BOTTOM_PLUG || part== EnumParts.BOTTOM_SHUNT))
		{
			for(int offset = 3; offset >= 1; offset--)
			{
				if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 1), worldIn))
				{
					mainPos = pos.offset(facing, offset).offset(facing.rotateY(), 1);
					break;
				}
			}
		}
		else
			mainPos = pos;
		return mainPos;	
		
	}
	
	public BlockPos getMainPos(IBlockState state, BlockPos pos, IBlockAccess worldIn)
	{
		
		BlockPos mainPos = null;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		if(index == 1)
		{
			if((part== EnumParts.BOTTOM_CORNER_PLUG || part== EnumParts.BOTTOM_CORNER_SHUNT) && isCable(state, pos.offset(facing.rotateY(), 1).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 1);
			else if((part== EnumParts.TOP_CORNER_PLUG || part== EnumParts.TOP_CORNER_SHUNT) && isCable(state, pos.offset(facing.rotateY(), 2).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 2);
		}
		else if(index == 0)
			if((part== EnumParts.TOP_PLUG || part== EnumParts.TOP_SHUNT))
		{
			for(int offset = 3; offset >= 1; offset--)
			{
				if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 2), worldIn))
				{
					mainPos = pos.offset(facing, offset).offset(facing.rotateY(), 2);
					break;
				}
			}
		}
		else if((part == EnumParts.BOTTOM_PLUG || part== EnumParts.BOTTOM_SHUNT))
		{
			for(int offset = 3; offset >= 1; offset--)
			{
				if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 1), worldIn))
				{
					mainPos = pos.offset(facing, offset).offset(facing.rotateY(), 1);
					break;
				}
			}
		}
		else if(part == EnumParts.CABLE)
			mainPos = pos;
		return mainPos;	
		
	}
	
	
	public int getUpgradeId(IBlockState state, BlockPos pos, World worldIn)
	{
		int id = 8;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		
		if(index == 1)
		{
			if(part == EnumParts.BOTTOM_CORNER_PLUG || part== EnumParts.BOTTOM_CORNER_SHUNT) 
				id = 1;
			else if(part== EnumParts.TOP_CORNER_PLUG || part== EnumParts.TOP_CORNER_SHUNT) 
				id = 0;
		}
		else if(index == 0)
		{
			
			if((part== EnumParts.TOP_PLUG || part== EnumParts.TOP_SHUNT))
			{
				for(int offset = 3; offset >= 1; offset--)
				{
					
					if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 2), worldIn))
					{
						
						id = offset + 1;
						break;
					}
				}
			}
			else if((part== EnumParts.BOTTOM_PLUG || part== EnumParts.BOTTOM_SHUNT))
			{
				for(int offset = 3; offset >= 1; offset--)
				{
					
					if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 1), worldIn))
					{
						id = offset + 4;
						break;
					}
				}
			}
			
		}
		return id;
	}
	
	public int getUpgradeId(IBlockState state, BlockPos pos, IBlockAccess worldIn)
	{
		int id = 8;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		
		if(index == 1)
		{
			
			if(part == EnumParts.BOTTOM_CORNER_PLUG || part== EnumParts.BOTTOM_CORNER_SHUNT)
				id = 1;
			else if(part== EnumParts.TOP_CORNER_PLUG || part== EnumParts.TOP_CORNER_SHUNT)
				id = 0;
		}
		else if(index == 0)
		{
			
			if((part== EnumParts.TOP_PLUG || part== EnumParts.TOP_SHUNT))
			{
				for(int offset = 3; offset >= 1; offset--)
				{
					
					if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 2), worldIn))
					{
						
						id = offset + 1;
						break;
					}
				}
			}
			else if((part== EnumParts.BOTTOM_PLUG || part== EnumParts.BOTTOM_SHUNT))
			{
				for(int offset = 3; offset >= 1; offset--)
				{
					
					if(isCable(state, pos.offset(facing, offset).offset(facing.rotateY(), 1), worldIn))
					{
						id = offset + 4;
						break;
					}
				}
			}
			
		}
		return id;
	}
	
	public boolean isCable(IBlockState state, BlockPos pos, World worldIn)
	{
		return worldIn.getBlockState(pos).getBlock() == MinestuckBlocks.jumperBlockExtension[3] && 
				worldIn.getBlockState(pos).getBlock().hasTileEntity(worldIn.getBlockState(pos));
	}
	
	public boolean isCable(IBlockState state, BlockPos pos, IBlockAccess worldIn)
	{
		return worldIn.getBlockState(pos).getBlock() == MinestuckBlocks.jumperBlockExtension[3] && 
				worldIn.getBlockState(pos).getBlock().hasTileEntity(worldIn.getBlockState(pos));
	}
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockJumperBlock)
			return state.getValue(((BlockJumperBlock) state.getBlock()).PART);
		else return null;
	}
	
	public static IBlockState getState(EnumParts parts, EnumFacing facing)
    {
        BlockJumperBlock block = MinestuckBlocks.jumperBlockExtension[PART1.getAllowedValues().contains(parts) ? 0 : PART2.getAllowedValues().contains(parts) ? 1 : PART3.getAllowedValues().contains(parts) ? 2 : PART4.getAllowedValues().contains(parts) ? 3 : 4];
        return block.getDefaultState().withProperty(block.PART, parts).withProperty(DIRECTION, facing);
    }
	
	 /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
		if(state.getValue(PART).isShunt())
		{
			AxisAlignedBB bb = modifyAABBForDirection(state.getValue(DIRECTION), SHUNT_AABB).offset(pos);
			if(entityBox.intersects(bb))
				collidingBoxes.add(bb);
		}
		else if(state.getValue(PART).isPlug())
		{
			AxisAlignedBB bb = modifyAABBForDirection(state.getValue(DIRECTION), PLUG_AABB).offset(pos);
			if(entityBox.intersects(bb))
				collidingBoxes.add(bb);
		}
		
		
	}
	
	public AxisAlignedBB modifyAABBForDirection(EnumFacing facing, AxisAlignedBB bb)
	{
		AxisAlignedBB out = null;
		switch(facing.ordinal())
		{
		case 2:	//North
			out = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
			break;
		case 3:	//South
			out = new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ);
			break;
		case 4:	//West
			out = new AxisAlignedBB(bb.minZ, bb.minY, 1-bb.maxX, bb.maxZ, bb.maxY, 1-bb.minX);
			break;
		case 5:	//East
			out = new AxisAlignedBB(1-bb.maxZ, bb.minY, bb.minX, 1-bb.minZ, bb.maxY, bb.maxX);
			break;
		}
		return out;
	}
	
	@Override
	public Item getItemFromMachine() 
	{
		return new ItemStack(MinestuckBlocks.jumperBlockExtension[0]).getItem();
	}
	
	public enum EnumParts implements IStringSerializable
	{
		TOP_PLUG(				new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BOTTOM_PLUG(			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		TOP_SHUNT(				new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BOTTOM_SHUNT(			new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BOTTOM_CORNER_PLUG(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		TOP_CORNER_PLUG(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BOTTOM_CORNER_SHUNT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		TOP_CORNER_SHUNT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BORDER_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BORDER_SIDE(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BORDER_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		SMALL_CORNER(new AxisAlignedBB(6/16D, 0.0D, 4/16D, 1.0D, 0.5D, 1.0D)),
		
		BASE_SIDE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BASE_CORNER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CENTER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CABLE(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D));
		
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
		
		public boolean isPlug()
		{
			return this == TOP_PLUG || this == BOTTOM_PLUG || this == TOP_CORNER_PLUG || this == BOTTOM_CORNER_PLUG;
		}
		
		public boolean isShunt()
		{
			return this == TOP_SHUNT || this == BOTTOM_SHUNT || this == TOP_CORNER_SHUNT || this == BOTTOM_CORNER_SHUNT;
		}
		
		public boolean isCable()
		{
			return this == CABLE;
		}
		
		
	}
	
	public static BlockJumperBlock[] createBlocks()
	{
		return new BlockJumperBlock[] {new BlockJumperBlock1(), new BlockJumperBlock2(), new BlockJumperBlock3(), new BlockJumperBlock4()};
	}
	
	private static class BlockJumperBlock1 extends BlockJumperBlock
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
	
	private static class BlockJumperBlock2 extends BlockJumperBlock
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
	
	private static class BlockJumperBlock3 extends BlockJumperBlock
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
	
	private static class BlockJumperBlock4 extends BlockJumperBlock
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
