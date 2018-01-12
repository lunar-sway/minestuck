package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVanityLaptopOn extends BlockComputerOff implements ITileEntityProvider
{
	public static final PropertyEnum<BlockType> VARIANT = BlockVanityLaptopOff.VARIANT;
	public static final PropertyDirection DIRECTION = BlockVanityLaptopOff.DIRECTION;
	public static final PropertyBool BSOD = BlockComputerOn.BSOD;
	
	public BlockVanityLaptopOn()
	{
		super();
		setDefaultState(getDefaultState().withProperty(BSOD, false));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((((state.getValue(DIRECTION)).ordinal() - 2) % 4) << 2) + ((state.getValue(VARIANT)).ordinal() % 4);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(DIRECTION, EnumFacing.values()[(meta >> 2) + 2]).withProperty(VARIANT, BlockType.values()[meta & 3]);
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
				System.out.println("A laptop has been BSOD'd!");
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(MinestuckBlocks.blockLaptopOff, state.getValue(VARIANT).ordinal());
	}
}
