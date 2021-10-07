package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.BasiliskEntity;
import com.mraof.minestuck.entity.underling.ImpEntity;
import com.mraof.minestuck.entity.underling.LichEntity;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonerTileEntity extends TileEntity
{
	private SummonType summonType;
	
	public enum SummonType
	{
		IMP,
		OGRE,
		BASILISK,
		LICH;
		
		public static SummonerTileEntity.SummonType fromInt(int ordinal) //converts int back into enum
		{
			for(SummonerTileEntity.SummonType type : SummonerTileEntity.SummonType.values())
			{
				if(type.ordinal() == ordinal)
					return type;
			}
			return null;
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
		}
	}
	
	public SummonerTileEntity()
	{
		super(MSTileEntityTypes.SUMMONER.get());
	}
	
	public void summonEntity(World worldIn, BlockState summonerState, BlockPos summonerBlockPos, boolean playParticles)
	{
		BlockPos pickedBlockPos = summonerBlockPos.up(1);
		
		if(summonType == SummonType.IMP)
		{
			ImpEntity impEntity = MSEntityTypes.IMP.create(worldIn);
			if(impEntity == null)
				throw new IllegalStateException("Unable to create a new imp. Entity factory returned null!");
			impEntity.enablePersistence();
			impEntity.setLocationAndAngles(pickedBlockPos.getX() + 0.5, pickedBlockPos.getY(), pickedBlockPos.getZ() + 0.5, worldIn.rand.nextFloat() * 360F, 0);
			impEntity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pickedBlockPos), SpawnReason.TRIGGERED, null, null);
			impEntity.setHomePosAndDistance(pickedBlockPos, 10);
			worldIn.addEntity(impEntity);
			randomTeleport(worldIn, summonerBlockPos, impEntity);
		} else if(summonType == SummonType.OGRE)
		{
			OgreEntity ogreEntity = MSEntityTypes.OGRE.create(worldIn);
			if(ogreEntity == null)
				throw new IllegalStateException("Unable to create a new ogre. Entity factory returned null!");
			ogreEntity.enablePersistence();
			ogreEntity.setLocationAndAngles(pickedBlockPos.getX() + 0.5, pickedBlockPos.getY(), pickedBlockPos.getZ() + 0.5, worldIn.rand.nextFloat() * 360F, 0);
			ogreEntity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pickedBlockPos), SpawnReason.TRIGGERED, null, null);
			ogreEntity.setHomePosAndDistance(pickedBlockPos, 10);
			worldIn.addEntity(ogreEntity);
			randomTeleport(worldIn, summonerBlockPos, ogreEntity);
		} else if(summonType == SummonType.BASILISK)
		{
			BasiliskEntity basiliskEntity = MSEntityTypes.BASILISK.create(worldIn);
			if(basiliskEntity == null)
				throw new IllegalStateException("Unable to create a new basilisk. Entity factory returned null!");
			basiliskEntity.enablePersistence();
			basiliskEntity.setLocationAndAngles(pickedBlockPos.getX() + 0.5, pickedBlockPos.getY(), pickedBlockPos.getZ() + 0.5, worldIn.rand.nextFloat() * 360F, 0);
			basiliskEntity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pickedBlockPos), SpawnReason.TRIGGERED, null, null);
			basiliskEntity.setHomePosAndDistance(pickedBlockPos, 10);
			worldIn.addEntity(basiliskEntity);
			randomTeleport(worldIn, summonerBlockPos, basiliskEntity);
		} else if(summonType == SummonType.LICH)
		{
			LichEntity lichEntity = MSEntityTypes.LICH.create(worldIn);
			if(lichEntity == null)
				throw new IllegalStateException("Unable to create a new lich. Entity factory returned null!");
			lichEntity.enablePersistence();
			lichEntity.setLocationAndAngles(pickedBlockPos.getX() + 0.5, pickedBlockPos.getY(), pickedBlockPos.getZ() + 0.5, worldIn.rand.nextFloat() * 360F, 0);
			lichEntity.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pickedBlockPos), SpawnReason.TRIGGERED, null, null);
			lichEntity.setHomePosAndDistance(pickedBlockPos, 10);
			worldIn.addEntity(lichEntity);
			randomTeleport(worldIn, summonerBlockPos, lichEntity);
		}
		
		if(playParticles)
		{
			for(int i = 0; i < 5; i++)
			{
				worldIn.addParticle(ParticleTypes.POOF, true, pickedBlockPos.getX(),  pickedBlockPos.getY(), pickedBlockPos.getZ(), 0.1,0.1,0.1);
			}
		}
	}
	
	private void randomTeleport(World worldIn, BlockPos summonerBlockPos, LivingEntity summonedEntity)
	{
		if(worldIn != null)
		{
			for(int i = 0; i < 16; ++i)
			{
				double newPosX = summonerBlockPos.getX() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
				double newPosY = summonerBlockPos.getY() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
				double newPosZ = summonerBlockPos.getZ() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
				
				if(summonedEntity.attemptTeleport(newPosX, newPosY, newPosZ, true))
				{
					break;
				}
			}
		}
	}
	
	public void setSummonedEntity(SummonType summonTypeIn)
	{
		this.summonType = summonTypeIn;
	}
	
	public SummonType getSummonedEntity()
	{
		if(summonType == null)
			summonType = SummonType.IMP;
		return this.summonType;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		summonType = SummonType.fromInt(compound.getInt("summonType"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("summonType", getSummonedEntity().ordinal());
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}