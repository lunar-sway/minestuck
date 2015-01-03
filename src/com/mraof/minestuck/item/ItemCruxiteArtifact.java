package com.mraof.minestuck.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.MinestuckAchievementHandler;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.gen.ChunkProviderLands;
import com.mraof.minestuck.world.gen.lands.LandHelper;

public class ItemCruxiteArtifact extends ItemFood implements ITeleporter
{
	//List<Block> commonBlocks = new ArrayList<Block>();
	
	public ItemCruxiteArtifact(int par2, boolean par3) 
	{
		super(1, par2, par3);
		this.setCreativeTab(Minestuck.tabMinestuck);
		setUnlocalizedName("cruxiteArtifact");
		//commonBlocks.add(Blocks.stone);
		//commonBlocks.add(Blocks.grass);
		//commonBlocks.add(Blocks.dirt);
		//commonBlocks.add(Blocks.sand);
		//commonBlocks.add(Blocks.sandstone);
		//commonBlocks.add(Blocks.water);
		//commonBlocks.add(Blocks.flowing_water);
	}
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

		return par1ItemStack;
	}
	@Override
	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer player) {
		if(!par2World.isRemote && player.worldObj.provider.getDimensionId() != -1) {
			
			int destinationId = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId") == 0 ? LandHelper.createLand(player) : player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId");
			if(player.worldObj.provider.getDimensionId() != destinationId) {
				player.triggerAchievement(MinestuckAchievementHandler.enterMedium);
				Teleport.teleportEntity(player, destinationId, this);
				SkaianetHandler.enterMedium((EntityPlayerMP)player, destinationId);
			}
		}
	}
	public void makeDestination(Entity entity, WorldServer worldserver0, WorldServer worldserver1)
	{
		if(entity instanceof EntityPlayerMP && entity.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getInteger("LandId") == worldserver1.provider.getDimensionId())
		{
			int x = (int) entity.posX;
			int y = (int) entity.posY;
			int z = (int) entity.posZ;
			
			for(int chunkX = (x - Minestuck.artifactRange) >> 4; chunkX <= (x + Minestuck.artifactRange) >> 4; chunkX++)	//Prevent anything to generate on the piece that we move
				for(int chunkZ = (z - Minestuck.artifactRange) >> 4; chunkZ <=(z + Minestuck.artifactRange) >> 4; chunkZ++)	//from the overworld.
					worldserver1.theChunkProviderServer.loadChunk(chunkX, chunkZ);
			
			List<?> list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand((double)Minestuck.artifactRange, Minestuck.artifactRange, (double)Minestuck.artifactRange));
			Iterator<?> iterator = list.iterator();

			while (iterator.hasNext())
			{
				Teleport.teleportEntity((Entity)iterator.next(), worldserver1.provider.getDimensionId(), this);
			}
			int nextWidth = 0;
			for(int blockX = x - Minestuck.artifactRange; blockX <= x + Minestuck.artifactRange; blockX++)
			{
				int zWidth = nextWidth;
				nextWidth = (int) Math.sqrt(Minestuck.artifactRange * Minestuck.artifactRange - (blockX - x + 1) * (blockX - x + 1));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
					int minY =  y - (int) (Math.sqrt(Minestuck.artifactRange * Minestuck.artifactRange - radius * radius));
					minY = minY < 0 ? 0 : minY;
					for(int blockY = minY; blockY < 256; blockY++)
					{
						BlockPos pos = new BlockPos(blockX, blockY, blockZ);
						IBlockState block = worldserver0.getBlockState(pos);
						TileEntity te = worldserver0.getTileEntity(pos);
						if(block != Blocks.bedrock)
							worldserver1.setBlockState(pos, block, 0);
						if((te) != null)
						{
							TileEntity te1 = null;
							try {
								te1 = te.getClass().newInstance();
							} catch (Exception e) {e.printStackTrace();	}
							NBTTagCompound nbt = new NBTTagCompound();
							te.writeToNBT(nbt);
							te1.readFromNBT(nbt);
							worldserver1.removeTileEntity(pos);
							worldserver1.setTileEntity(pos, te1);
						};
					}
				}
			}
			for(int blockX = x - Minestuck.artifactRange; blockX <= x + Minestuck.artifactRange; blockX++)
			{
				int zWidth = nextWidth;
				nextWidth = (int) Math.sqrt(Minestuck.artifactRange * Minestuck.artifactRange - (blockX - x + 1) * (blockX - x + 1));
				for(int blockZ = z - zWidth; blockZ <= z + zWidth; blockZ++)
				{
					double radius = Math.sqrt(((blockX - x) * (blockX - x) + (blockZ - z) * (blockZ - z)) / 2);
					int minY =  y - (int) (Math.sqrt(Minestuck.artifactRange * Minestuck.artifactRange - radius*radius));
					minY = minY < 0 ? 0 : minY;
					for(int blockY = minY; blockY < 256; blockY++)
					{
						Block block = worldserver0.getBlockState(new BlockPos(blockX, blockY, blockZ)).getBlock();
						if(block != Blocks.bedrock)
							worldserver0.setBlockState(new BlockPos(blockX, blockY, blockZ), Blocks.air.getDefaultState(), 2);
					}
				}
			}
			list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().expand((double)Minestuck.artifactRange, Minestuck.artifactRange, (double)Minestuck.artifactRange));
			iterator = list.iterator();

			while (iterator.hasNext())
			{
				((Entity)iterator.next()).setDead();
			}
			
			ChunkProviderLands chunkProvider = (ChunkProviderLands) worldserver1.provider.createChunkGenerator();
			chunkProvider.spawnX = x;
			chunkProvider.spawnY = y;
			chunkProvider.spawnZ = z;
			chunkProvider.saveData();
			
			Debug.printf("Respawn location being set to: %d, %d, %d", x, y, z);
		}
	}
	
}
