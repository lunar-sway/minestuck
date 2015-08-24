package com.mraof.minestuck.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.tileentity.TileEntityGate;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockGate extends Block implements ITileEntityProvider
{
	
	public static PropertyBool isMainComponent = PropertyBool.create("mainComponent");
	
	public BlockGate()
	{
		super(Material.portal);
		setDefaultState(getDefaultState().withProperty(isMainComponent, false));
		setLightLevel(0.75F);
		setHardness(10.0F);
		this.setBlockBounds(0F, 0.45F, 0F, 1F, 0.55F, 1F);
	}
	
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer)
	{
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return null;
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isFullCube()
	{
		return false;
	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, isMainComponent);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean) state.getValue(isMainComponent)) ? 0 : 1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(isMainComponent, meta == 0 ? true : false);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityGate();
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return (Boolean) state.getValue(isMainComponent);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityPlayerMP && entityIn.ridingEntity == null && entityIn.riddenByEntity == null && entityIn.timeUntilPortal == 0)
		{
			BlockPos mainPos = pos;
			if(!(Boolean) state.getValue(isMainComponent))
				if(this != Minestuck.gate)
					mainPos = this.findMainComponent(pos, worldIn);
				else return;
			
			TileEntity te = worldIn.getTileEntity(mainPos);
			if(te instanceof TileEntityGate)
				((TileEntityGate) te).teleportEntity(worldIn, (EntityPlayerMP) entityIn, this);
		}
	}
	
	protected boolean isValid(BlockPos pos, World world, IBlockState state)
	{
		if((Boolean) state.getValue(isMainComponent))
			return isValid(pos, world);
		else
		{
			BlockPos mainPos = findMainComponent(pos, world);
			if(mainPos != null)
				return isValid(mainPos, world);
			else return false;
		}
	}
	
	protected boolean isValid(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() != this || (Boolean) block.getValue(isMainComponent))
						return false;
				}
		
		return true;
	}
	
	protected void removePortal(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(world.getBlockState(pos.add(x, 0, z)).getBlock() == this)
					world.setBlockToAir(pos.add(x, 0, z));
	}
	
	protected BlockPos findMainComponent(BlockPos pos, World world)
	{
		for(int x = -1; x <= 1; x++)
			for(int z = -1; z <= 1; z++)
				if(x != 0 || z != 0)
				{
					IBlockState block = world.getBlockState(pos.add(x, 0, z));
					if(block.getBlock() == this && (Boolean) block.getValue(isMainComponent))
						return pos.add(x, 0, z);
				}
		
		return null;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		if((Boolean) state.getValue(isMainComponent))
			removePortal(pos, worldIn);
		else
		{
			BlockPos mainPos = findMainComponent(pos, worldIn);
			if(mainPos != null)
				removePortal(mainPos, worldIn);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if(!this.isValid(pos, worldIn, state))
		{
			BlockPos mainPos = pos;
			if(!(Boolean) state.getValue(isMainComponent))
				mainPos = findMainComponent(pos, worldIn);
			
			if(mainPos == null)
				worldIn.setBlockToAir(pos);
			else removePortal(mainPos, worldIn);
		}
	}
	
	@Override
	public int getLightValue()
	{
		return super.getLightValue();
	}
	
}