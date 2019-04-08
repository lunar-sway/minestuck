package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import java.util.Random;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.BlockVanityLaptopOff.BlockType;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVanityLaptopOn extends BlockComputerOff implements ITileEntityProvider
{
	public static final PropertyEnum<BlockType> VARIANT = BlockVanityLaptopOff.VARIANT;
	public static final PropertyDirection DIRECTION = BlockVanityLaptopOff.DIRECTION;
	public static final PropertyBool BSOD = BlockComputerOn.BSOD;
	
	protected static final AxisAlignedBB COMPUTER_AABB = new AxisAlignedBB(1/32D, 0.0D, 7/32D, 31/32D, 0.5/16D, 24.8/32D);
	protected static final AxisAlignedBB COMPUTER_SCREEN_AABB = new AxisAlignedBB(0.5/16D, 0.5D/16, 11.8/16D, 15.5/16D, 9.5/16D, 12.4/16D);
	
	public BlockVanityLaptopOn()
	{
		super();
		setDefaultState(getDefaultState().withProperty(BSOD, false));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((state.getValue(DIRECTION).getHorizontalIndex()) << 2) + ((state.getValue(VARIANT)).ordinal() % 4);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DIRECTION, EnumFacing.getHorizontal((meta >> 2) % 4)).withProperty(VARIANT, BlockType.values()[meta & 3]);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state.withProperty(BSOD, ((TileEntityComputer)worldIn.getTileEntity(pos)).hasProgram(-1))
				.withProperty(VARIANT, BlockVanityLaptopOff.BlockType.values()[state.getValue(VARIANT).ordinal()])
				.withProperty(DIRECTION, EnumFacing.values()[state.getValue(DIRECTION).ordinal()]);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DIRECTION, BSOD, VARIANT);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntityComputer tileEntity = (TileEntityComputer) worldIn.getTileEntity(pos);

		if (tileEntity == null || playerIn.isSneaking())
		{
			return false;
		}

		int id = ComputerProgram.getProgramID(playerIn.getHeldItem(hand));
		if(id != -2 && !tileEntity.hasProgram(id) && tileEntity.installedPrograms.size() < 2 && !tileEntity.hasProgram(-1)) 
		{
			if(worldIn.isRemote)
				return true;
			playerIn.setHeldItem(hand, ItemStack.EMPTY);
			if(id == -1) 
			{
				tileEntity.closeAll();
				worldIn.setBlockState(pos, state.withProperty(BSOD, true), 2);
				tileEntity.installedPrograms.put(id, true);
				tileEntity.markBlockForUpdate();
				System.out.println("A laptop has been BSOD'd!");
			}
			else tileEntity.installedPrograms.put(id, true);
			tileEntity.markDirty();
			worldIn.notifyBlockUpdate(pos, state, getActualState(state, worldIn, pos), 3);
			return true;
		}

		if(worldIn.isRemote && SkaiaClient.requestData(tileEntity))
			playerIn.openGui(Minestuck.instance, GuiHandler.GuiId.COMPUTER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileEntityComputer();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(new ItemStack(MinestuckBlocks.blockLaptopOff, 1, (state.getValue(VARIANT)).ordinal()));
		
		return list;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), state);
		super.breakBlock(worldIn, pos, state);
	}
	
	private void dropItems(World world, int x, int y, int z, IBlockState state)
	{
		Random rand = new Random();
		TileEntityComputer te = (TileEntityComputer) world.getTileEntity(new BlockPos(x, y, z));
		if (te == null) 
		{
			return;
		}
		te.closeAll();
		float factor = 0.05F;

		Iterator<Entry<Integer, Boolean>> it = te.installedPrograms.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Integer, Boolean> pairs = it.next();
			if(!pairs.getValue())
				continue;
			int program = pairs.getKey();

			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(program));
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		if(state.getValue(VARIANT)==BlockType.LUNCH_TOP)
		{
			return modifyAABBForDirection(state.getValue(DIRECTION), new AxisAlignedBB(5/16D, 0.0D, 5/16D, 11/16D, 3.5/16D, 10/16D));
		}
		return modifyAABBForDirection(state.getValue(DIRECTION), COMPUTER_AABB);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
		if(state.getValue(VARIANT)!=BlockType.LUNCH_TOP)
		{
			EnumFacing rotation = state.getValue(DIRECTION);
			AxisAlignedBB bb = modifyAABBForDirection(rotation, COMPUTER_SCREEN_AABB).offset(pos);
			if(entityBox.intersects(bb))
				collidingBoxes.add(bb);
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(MinestuckBlocks.blockLaptopOff, state.getValue(VARIANT).ordinal());
	}
}
