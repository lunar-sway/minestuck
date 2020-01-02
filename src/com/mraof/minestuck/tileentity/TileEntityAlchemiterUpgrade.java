package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.util.AlchemiterUpgrades;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAlchemiterUpgrade extends TileEntity implements ITickableTileEntity
{
	protected AlchemiterUpgrades upgrade;
	
	public TileEntityAlchemiterUpgrade(AlchemiterUpgrades upg) 
	{
		super(MSTileEntityTypes.ALCHEMITER_UPGRADE);
		//setUpgrade(upg);
	}
	
	public TileEntityAlchemiterUpgrade()
	{
		this(null);
	}
	
	@Override
	public void tick()
	{
		if(world != null && !world.isRemote)
		{
			/*EnumFacing facing = world.getBlockState(pos).getValue(BlockAlchemiterUpgrades.DIRECTION);
			
			if(upgrade.equals(AlchemiterUpgrades.hopper))
				updateHopper(facing);*/
		}
	}
	/* TODO Update once the blocks are sorted out
	private void updateHopper(EnumFacing facing) 
	{
		
		TileEntity te = world.getTileEntity(((BlockAlchemiterUpgrades)world.getBlockState(pos).getBlock()).getAlchemiterPos(world, pos.offset(facing)));
		
		if(!(te instanceof TileEntityAlchemiter)) return;
		TileEntityAlchemiter alchemiter = (TileEntityAlchemiter) te;
		
		//TODO dont forget about checking the pad usability thing!
		
		
		if(alchemiter.getDowel().isEmpty())
		{
			for(EntityItem itemEntity : getCaptureItems(world, pos.getX(), pos.getY(), pos.getZ()))
			{
				if(itemEntity == null) continue;
				ItemStack itemstack = itemEntity.getItem().copy();
				ItemStack itemstack1 = itemstack.copy();
				itemstack1.shrink(1);
				
				if(!itemstack.getItem().equals(MinestuckItems.cruxiteDowel)) continue;
				if(itemstack1.isEmpty())
					itemEntity.setDead();
				else
					itemEntity.setItem(itemstack1);
				
				alchemiter.setDowel(itemstack);
			}
		}
	}

	public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z)
    {
        return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x, y, z, x + 1D, y + 1.5D, z + 1D), EntitySelectors.IS_ALIVE);
    }
	
	public AlchemiterUpgrades getUpgrade()
	{
		return upgrade;
	}
	
	public void setUpgrade(AlchemiterUpgrades upg)
	{
		upgrade = upg;
	}*/
}
