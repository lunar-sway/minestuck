package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.gen.lands.LandHelper;

public class ItemCruxiteArtifact extends ItemFood implements ITeleporter
{
	int destinationDimension = Minestuck.landDimensionIdStart;
	public ItemCruxiteArtifact(int par1, int par2, boolean par3) 
	{
		super(par1, par2, par3);
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
    }
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(!par2World.isRemote)
		{
			Teleport.teleportEntity(par3EntityPlayer, LandHelper.createLand(), this);
		}
	}
	public void makeDestination(Entity entity, WorldServer worldserver0, WorldServer worldserver1)
	{
		int x = (int) entity.posX;
		int y = (int) entity.posY;
		int z = (int) entity.posZ;
		int width = 24;
		for(int blockX = x - width; blockX <= x + width; blockX++)
		{
			for(int blockZ = z - width; blockZ <= z + width; blockZ++)
			{
				double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
				for(int blockY = y - (int) (Math.sqrt(width*width - radius*radius)); blockY < y + width * 2 && blockY < 256; blockY++)
				{
					int blockId = worldserver0.getBlockId(blockX, blockY, blockZ);
					int metadata = worldserver0.getBlockMetadata(blockX, blockY, blockZ);
					TileEntity te = worldserver0.getBlockTileEntity(blockX, blockY, blockZ);
					worldserver1.setBlock(blockX, blockY, blockZ, blockId, metadata, 3);
					if((te) != null)
					{
						TileEntity te1 = null;
						try {
							te1 = te.getClass().newInstance();
						} catch (Exception e) {e.printStackTrace();	}
						NBTTagCompound nbt = new NBTTagCompound();
						te.writeToNBT(nbt);
						te1.readFromNBT(nbt);
						te1.yCoord++;//prevents TileEntity from being invalidated
						worldserver1.setBlockTileEntity(blockX, blockY, blockZ, te1);
					};
					if(blockId != Block.bedrock.blockID)
						worldserver0.setBlockToAir(blockX, blockY, blockZ);
				}
					
					
			}
		}
	}
	@Override
	public void registerIcons(IconRegister iconRegister) 
	{
		this.itemIcon = iconRegister.registerIcon("minestuck:CruxiteApple");
	}
	
}
