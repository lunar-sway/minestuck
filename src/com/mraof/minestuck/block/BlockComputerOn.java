package com.mraof.minestuck.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.ComputerProgram;

public class BlockComputerOn extends Block implements ITileEntityProvider
{
	
	public static final PropertyBool BSOD = PropertyBool.create("bsod");
	
	public BlockComputerOn()
	{
		super(Material.rock);
		
		setDefaultState(getDefaultState().withProperty(BSOD, false));
		setUnlocalizedName("sburbComputer");
		setHardness(4.0F);
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, BlockComputerOff.DIRECTION, BSOD);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean) state.getValue(BSOD) ? 1 : 0) + Minestuck.blockComputerOff.getMetaFromState(state)*2;	//I can't see why this wouldn't work
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BSOD, meta % 2 == 1).withProperty(BlockComputerOff.DIRECTION, EnumFacing.values()[(meta/2) + 2]);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityComputer tileEntity = (TileEntityComputer) world.getTileEntity(pos);
		ItemStack item = player.getCurrentEquippedItem();

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		int id = ComputerProgram.getProgramID(item);
		if(id != -2 && !tileEntity.hasProgram(id) && tileEntity.installedPrograms.size() < 2 && !tileEntity.hasProgram(-1)) 
		{
			if(world.isRemote)
				return true;
			player.destroyCurrentEquippedItem();
			if(id == -1) 
			{
				tileEntity.closeAll();
				world.setBlockState(pos, state.withProperty(BSOD, true), 2);
			}
			tileEntity.installedPrograms.put(id, true);
			world.markBlockForUpdate(pos);
			return true;
		}

		if(world.isRemote && SkaiaClient.requestData(tileEntity))
			player.openGui(Minestuck.instance, GuiHandler.GuiId.COMPUTER.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());

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
		list.add(new ItemStack(Minestuck.blockComputerOff));
		
		return list;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		dropItems(worldIn, pos.getX(), pos.getY(), pos.getZ());
		super.breakBlock(worldIn, pos, state);
	}
	
	private void dropItems(World world, int x, int y, int z)
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
			world.spawnEntityInWorld(entityItem);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(Minestuck.blockComputerOff);
	}
	
}
