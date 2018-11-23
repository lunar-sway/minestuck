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
	public static final PropertyEnum<EnumParts> PART1 = PropertyEnum.create("part", EnumParts.class, EnumParts.CHEST_LEFT, EnumParts.HOPPER_LEFT, EnumParts.CHEST_RIGHT, EnumParts.HOPPER_RIGHT);
	public static final PropertyEnum<EnumParts> PART2 = PropertyEnum.create("part", EnumParts.class, EnumParts.LIBRARY_LEFT, EnumParts.CRAFTING_LEFT, EnumParts.CRAFTING_RIGHT, EnumParts.LIBRARY_RIGHT);
	public static final PropertyEnum<EnumParts> PART3 = PropertyEnum.create("part", EnumParts.class, EnumParts.BORDER_LEFT, EnumParts.BORDER_SIDE, EnumParts.BORDER_RIGHT, EnumParts.SMALL_CORNER);
	public static final PropertyEnum<EnumParts> PART4 = PropertyEnum.create("part", EnumParts.class, EnumParts.BLENDER, EnumParts.BASE_CORNER, EnumParts.CENTER, EnumParts.CABLE);
	
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
		if(!state.getValue(PART).isPlug() && !state.getValue(PART).isShunt() && !state.getValue(PART).isCable())
		{
			BlockPos mainPos = getMainPos(state, pos, worldIn);
			TileEntity te = worldIn.getTileEntity(mainPos);
			IBlockState otherState = worldIn.getBlockState(mainPos);
			if(te instanceof TileEntityJumperBlock && otherState.getValue(DIRECTION) == state.getValue(DIRECTION))
			{
				((TileEntityJumperBlock) te).setBroken();
			}
		}
		
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
		if(part.isCable())
			part = EnumParts.CABLE;
		else if(part == EnumParts.CHEST_RIGHT || part == EnumParts.CHEST_LEFT)
			part = EnumParts.CHEST_LEFT;
		else if(part == EnumParts.CRAFTING_RIGHT || part == EnumParts.CRAFTING_LEFT)
			part = EnumParts.CRAFTING_LEFT;
		else if(part == EnumParts.LIBRARY_RIGHT || part == EnumParts.LIBRARY_LEFT)
			part = EnumParts.LIBRARY_LEFT;
		else if(part == EnumParts.CHEST_RIGHT || part == EnumParts.CHEST_LEFT)
			part = EnumParts.CHEST_LEFT;
		
		EnumFacing facing = state.getValue(DIRECTION);
		return (part.ordinal() % 4) * 4 + facing.getHorizontalIndex();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		int id = getUpgradeId(state, pos, worldIn);
		BlockPos mainPos = getMainPos(state, pos, worldIn);
		
		/*if(worldIn.getTileEntity(mainPos) instanceof TileEntityJumperBlock)
		{
			if(part.isPlug())
			{
				
				TileEntity te = worldIn.getTileEntity(mainPos);
				System.out.println("beep: " + te);
				TileEntityJumperBlock cable = (TileEntityJumperBlock) te;
				System.out.println("boop: " + cable);
				
				if(!cable.getUpgrade(id).isEmpty())
				{
					if(part == EnumParts.CHEST_LEFT) part = EnumParts.CHEST_RIGHT; 
					if(part == EnumParts.HOPPER_LEFT) part = EnumParts.HOPPER_RIGHT; 
					if(part == EnumParts.LIBRARY_LEFT) part = EnumParts.LIBRARY_RIGHT; 
					if(part == EnumParts.CRAFTING_LEFT) part = EnumParts.CRAFTING_RIGHT; 
				} 
				
			} else if(part.isShunt())
			{
				TileEntity te = worldIn.getTileEntity(mainPos);
				TileEntityJumperBlock cable = (TileEntityJumperBlock) te;
				
				
				if(cable.getUpgrade(id).isEmpty())
				{
					if(part == EnumParts.CHEST_RIGHT) part = EnumParts.CHEST_LEFT; 
					if(part == EnumParts.HOPPER_RIGHT) part = EnumParts.HOPPER_LEFT; 
					if(part == EnumParts.LIBRARY_RIGHT) part = EnumParts.LIBRARY_LEFT; 
					if(part == EnumParts.CRAFTING_RIGHT) part = EnumParts.CRAFTING_LEFT; 
				}
					
					
				}
		}*/
		state = state.withProperty(PART, part);
		return state;
		} 
	
    /**
     *returns the block position of the "Main" block
     *aka the block with the TileEntity for the machine
     */
	public BlockPos getMainPos(IBlockState state, BlockPos pos, World worldIn)
	{
		
		BlockPos mainPos = null;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		if(index == 1)
		{
			if((part== EnumParts.CRAFTING_LEFT || part== EnumParts.CRAFTING_RIGHT) && isCable(state, pos.offset(facing.rotateY(), 1).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 1);
			else if((part== EnumParts.LIBRARY_LEFT || part== EnumParts.LIBRARY_RIGHT) && isCable(state, pos.offset(facing.rotateY(), 2).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 2);
		}
		else if(index == 0)
			if((part== EnumParts.CHEST_LEFT || part== EnumParts.CHEST_RIGHT))
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
		else if((part == EnumParts.HOPPER_LEFT || part== EnumParts.HOPPER_RIGHT))
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
	
	public BlockPos getMainPos(IBlockState state, BlockPos pos, IBlockAccess worldIn)
	{
		
		BlockPos mainPos = null;
		
		EnumParts part = state.getValue(PART);
		EnumFacing facing = state.getValue(DIRECTION);
		
		if(index == 1)
		{
			if((part== EnumParts.CRAFTING_LEFT || part== EnumParts.CRAFTING_RIGHT) && isCable(state, pos.offset(facing.rotateY(), 1).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 1);
			else if((part== EnumParts.LIBRARY_LEFT || part== EnumParts.LIBRARY_RIGHT) && isCable(state, pos.offset(facing.rotateY(), 2).offset(facing.rotateY(), 0), worldIn))
				mainPos = pos.offset(facing.rotateY(), 2);
		}
		else if(index == 0)
			if((part== EnumParts.CHEST_LEFT || part== EnumParts.CHEST_RIGHT))
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
		else if((part == EnumParts.HOPPER_LEFT || part== EnumParts.HOPPER_RIGHT))
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
			if(part == EnumParts.CRAFTING_LEFT || part== EnumParts.CRAFTING_RIGHT) 
				id = 1;
			else if(part== EnumParts.LIBRARY_LEFT || part== EnumParts.LIBRARY_RIGHT) 
				id = 0;
		}
		else if(index == 0)
		{
			
			if((part== EnumParts.CHEST_LEFT || part== EnumParts.CHEST_RIGHT))
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
			else if((part== EnumParts.HOPPER_LEFT || part== EnumParts.HOPPER_RIGHT))
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
			
			if(part == EnumParts.CRAFTING_LEFT || part== EnumParts.CRAFTING_RIGHT)
				id = 1;
			else if(part== EnumParts.LIBRARY_LEFT || part== EnumParts.LIBRARY_RIGHT)
				id = 0;
		}
		else if(index == 0)
		{
			
			if((part== EnumParts.CHEST_LEFT || part== EnumParts.CHEST_RIGHT))
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
			else if((part== EnumParts.HOPPER_LEFT || part== EnumParts.HOPPER_RIGHT))
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
		return worldIn.getBlockState(pos).getBlock() == MinestuckBlocks.alchemiterUpgrades[3];
	}
	
	public boolean isCable(IBlockState state, BlockPos pos, IBlockAccess worldIn)
	{
		return worldIn.getBlockState(pos).getBlock() == MinestuckBlocks.alchemiterUpgrades[3];
	}
	
	public static EnumParts getPart(IBlockState state)
	{
		if(state.getBlock() instanceof BlockAlchemiterUpgrades)
			return state.getValue(((BlockAlchemiterUpgrades) state.getBlock()).PART);
		else return null;
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
		CHEST_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		HOPPER_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CHEST_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		HOPPER_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		CRAFTING_LEFT(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		LIBRARY_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		CRAFTING_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		LIBRARY_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BORDER_LEFT(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BORDER_SIDE(	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		BORDER_RIGHT(		new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		SMALL_CORNER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
		
		BLENDER(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D)),
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
			return this == CHEST_LEFT || this == HOPPER_LEFT || this == LIBRARY_LEFT || this == CRAFTING_LEFT;
		}
		
		public boolean isShunt()
		{
			return this == CHEST_RIGHT || this == HOPPER_RIGHT || this == LIBRARY_RIGHT || this == CRAFTING_RIGHT;
		}
		
		public boolean isCable()
		{
			return this == CABLE;
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
