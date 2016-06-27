package com.mraof.minestuck.block;

import java.util.List;

import com.mraof.minestuck.tileentity.TileEntityGate;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGate extends Block
{
	
	protected static final AxisAlignedBB GATE_AABB = new AxisAlignedBB(0.0D, 0.45D, 0.0D, 1.0D, 0.55D, 1.0D);
	public static PropertyBool isMainComponent = PropertyBool.create("main_component");
	
	public BlockGate()
	{
		super(Material.PORTAL);
		setDefaultState(getDefaultState().withProperty(isMainComponent, false));
		setLightLevel(0.75F);
		setBlockUnbreakable();
		blockResistance = 75.0F;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return GATE_AABB;
	}
	
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		return true;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB boundingBox, List<AxisAlignedBB> list, Entity entity)
	{
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, isMainComponent);
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
	public TileEntity createTileEntity(World worldIn, IBlockState state)
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
		if(entityIn instanceof EntityPlayerMP && entityIn.getRidingEntity() == null && entityIn.getPassengers().isEmpty())
		{
			if(entityIn.timeUntilPortal != 0)
			{
				entityIn.timeUntilPortal = entityIn.getPortalCooldown();
				return;
			}
			
			BlockPos mainPos = pos;
			if(!(Boolean) state.getValue(isMainComponent))
				if(this != MinestuckBlocks.gate)
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
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
}