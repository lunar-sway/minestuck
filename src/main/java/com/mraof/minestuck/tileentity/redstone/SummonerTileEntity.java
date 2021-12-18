package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class SummonerTileEntity extends TileEntity
{
	private EntityType<?> summonType;
	
	public static final String SUMMON_TYPE_CHANGE = "block.minestuck.summoner_block.summon_type_change";
	
	public SummonerTileEntity()
	{
		super(MSTileEntityTypes.SUMMONER.get());
	}
	
	public void summonEntity(World worldIn, BlockPos summonerBlockPos, EntityType<?> type, boolean triggerActivate, boolean playParticles)
	{
		if(type == null)
			throw new IllegalStateException("SummonerTileEntity unable to create a new entity. Entity factory returned null!");
		
		int iterateTracker = 0;
		for(int i = 0; i < 60; i++) //arbitrarily high
		{
			iterateTracker = i;
			double newPosX = summonerBlockPos.getX() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
			double newPosY = summonerBlockPos.getY() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
			double newPosZ = summonerBlockPos.getZ() + (worldIn.rand.nextDouble() - 0.5D) * 16.0D;
			if(worldIn.hasNoCollisions(type.getBoundingBoxWithSizeApplied(newPosX, newPosY, newPosZ)) && //checks that entity wont suffocate
					EntitySpawnPlacementRegistry.func_223515_a(type, worldIn, SpawnReason.TRIGGERED, new BlockPos(newPosX, newPosY, newPosZ), worldIn.getRandom())) //helps spawn entity on a valid floor
			{
				BlockPos newBlockPos = new BlockPos(newPosX, newPosY, newPosZ);
				type.spawn(worldIn, null, null, null, newBlockPos, SpawnReason.TRIGGERED, true, true);
				
				if(playParticles)
				{
					for(int particleIterate = 0; particleIterate < 5; particleIterate++)
					{
						worldIn.addParticle(ParticleTypes.POOF, true, newPosX, newPosY, newPosZ, 0.1, 0.1, 0.1);
					}
				}
				break;
			}
		}
		
		if(iterateTracker == 59)
			worldIn.getPendingBlockTicks().scheduleTick(new BlockPos(summonerBlockPos), worldIn.getBlockState(summonerBlockPos).getBlock(), 30); //if a valid resting spot was not found in the 59 checks of the for loop then the block will be reset and will try again in 1.5 seconds
		else if(triggerActivate)
			worldIn.setBlockState(summonerBlockPos, worldIn.getBlockState(summonerBlockPos).with(SummonerBlock.TRIGGERED, true), 4);
	}
	
	public void setSummonedEntity(EntityType<?> entityTypeIn, @Nullable PlayerEntity playerEntityIn)
	{
		this.summonType = entityTypeIn;
		
		if(playerEntityIn != null)
			playerEntityIn.sendStatusMessage(new TranslationTextComponent(SUMMON_TYPE_CHANGE, summonType.getRegistryName()), true);
	}
	
	public EntityType<?> getSummonedEntity()
	{
		if(summonType == null)
			summonType = MSEntityTypes.IMP;
		return this.summonType;
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		Optional<EntityType<?>> attemptedSummonType = EntityType.byKey(compound.getString("summonType"));
		attemptedSummonType.ifPresent(entityType -> summonType = entityType);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putString("summonType", EntityType.getKey(getSummonedEntity()).toString());
		
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