package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class BlockComputerOn extends Block implements ITileEntityProvider
{
	protected static final AxisAlignedBB COMPUTER_AABB = new AxisAlignedBB(1/16D, 0.0D, 1/16D, 15/16D, 1/8D, 15/16D);
	protected static final AxisAlignedBB[] COMPUTER_SCREEN_AABB = {new AxisAlignedBB(0.5/16D, 0.0D, 6/16D, 15.5/16D, 13/16D, 7.2/16D), new AxisAlignedBB(8.8/16D, 0.0D, 0.5/16D, 10/16D, 13/16D, 15.5/16), new AxisAlignedBB(0.5/16D, 0.0D, 8.8/16D, 15.5/16D, 13/16D, 10/16D), new AxisAlignedBB(6/16D, 0.0D, 0.5/16D, 7.2/16D, 13/16D, 15.5/16D)};

	public static final PropertyBool BSOD = PropertyBool.create("bsod");
	
	public BlockComputerOn()
	{
		super(Material.ROCK);
		setDefaultState(getDefaultState().withProperty(BSOD, false));
		setUnlocalizedName("sburbComputer");
		setHardness(4.0F);
		setHarvestLevel("pickaxe", 0);
		lightOpacity = 1;
		this.translucent=true;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockComputerOff.DIRECTION, BSOD);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean) state.getValue(BSOD) ? 1 : 0) + MinestuckBlocks.blockComputerOff.getMetaFromState(state)*2;
			//TODO: Now that I know about block.getActualState, the bsod doesn't have to be part of the block.
			//Fix that when there is no need to worry about breaking existing save files
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BSOD, meta % 2 == 1).withProperty(BlockComputerOff.DIRECTION, EnumFacing.values()[(meta/2) + 2]);
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
			}
			else tileEntity.installedPrograms.put(id, true);
			tileEntity.markDirty();
			worldIn.notifyBlockUpdate(pos, state, state, 3);
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
		list.add(new ItemStack(MinestuckBlocks.blockComputerOff));
		
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
			int program = (Integer) pairs.getKey();

			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(program));
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
		}
		if((Boolean) state.getValue(BSOD))
		{
			float rx = rand.nextFloat() * 0.8F + 0.1F;
			float ry = rand.nextFloat() * 0.8F + 0.1F;
			float rz = rand.nextFloat() * 0.8F + 0.1F;
			EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, ComputerProgram.getItem(-1));
			entityItem.motionX = rand.nextGaussian() * factor;
			entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
			entityItem.motionZ = rand.nextGaussian() * factor;
			world.spawnEntity(entityItem);
		}
	}
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(MinestuckBlocks.blockComputerOff);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return COMPUTER_AABB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
	{
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
		EnumFacing roation = worldIn.getBlockState(pos).getValue(BlockComputerOff.DIRECTION);
		AxisAlignedBB bb = COMPUTER_SCREEN_AABB[roation.getHorizontalIndex()].offset(pos);
		if(entityBox.intersects(bb))
			collidingBoxes.add(bb);
	}
}
