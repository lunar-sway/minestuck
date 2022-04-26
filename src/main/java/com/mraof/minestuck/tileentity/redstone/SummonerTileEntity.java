package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Optional;

public class SummonerTileEntity extends TileEntity implements ITickableTileEntity
{
	private EntityType<?> summonType;
	private int cooldownTimer;
	
	public static final String SUMMON_TYPE_CHANGE = "block.minestuck.summoner_block.summon_type_change";
	
	public SummonerTileEntity()
	{
		super(MSTileEntityTypes.SUMMONER.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null)
			return;
		
		if(cooldownTimer >= 200) //summoner has a cooldown of 10 seconds(10 sec * 20 tick) to prevent entity spamming
		{
			cooldownTimer = 0;
		}
		
		if(cooldownTimer != 0)
			cooldownTimer++;
	}
	
	public void summonEntity(World worldIn, BlockPos summonerBlockPos, EntityType<?> type, boolean triggerActivate, boolean playParticles)
	{
		if(type == null)
			throw new IllegalStateException("SummonerTileEntity unable to create a new entity. Entity factory returned null!");
		
		if(cooldownTimer == 0 && worldIn instanceof IServerWorld)
		{
			int iterateTracker = 0;
			for(int i = 0; i < 60; i++) //arbitrarily high
			{
				iterateTracker = i;
				double newPosX = summonerBlockPos.getX() + (worldIn.random.nextDouble() - 0.5D) * 16.0D;
				double newPosY = summonerBlockPos.getY() + (worldIn.random.nextDouble() - 0.5D) * 16.0D;
				double newPosZ = summonerBlockPos.getZ() + (worldIn.random.nextDouble() - 0.5D) * 16.0D;
				if(worldIn.noCollision(type.getAABB(newPosX, newPosY, newPosZ)) && //checks that entity wont suffocate //getAABB was getBoundingBoxWithSizeApplied
						EntitySpawnPlacementRegistry.checkSpawnRules(type, (IServerWorld) worldIn, SpawnReason.SPAWN_EGG, new BlockPos(newPosX, newPosY, newPosZ), worldIn.getRandom())) //helps spawn entity on a valid floor
				{
					BlockPos newBlockPos = new BlockPos(newPosX, newPosY, newPosZ);
					type.spawn((ServerWorld) worldIn, null, null, null, newBlockPos, SpawnReason.SPAWN_EGG, true, true); //TODO mob spawning conforms to light level/spawning surface/other conditions of normal generation which limits undead mob use
					
					if(playParticles)
					{
						//TODO caused a crash, bring back in later
						/*for(int particleIterate = 0; particleIterate < 5; particleIterate++)
						{
							worldIn.addParticle(ParticleTypes.POOF, true, newPosX, newPosY, newPosZ, 0.1, 0.1, 0.1);
						}*/
					}
					break;
				}
			}
			
			if(iterateTracker == 59)
				worldIn.getBlockTicks().scheduleTick(new BlockPos(summonerBlockPos), worldIn.getBlockState(summonerBlockPos).getBlock(), 30); //if a valid resting spot was not found in the 59 checks of the for loop then the block will be reset and will try again in 1.5 seconds
			else
			{
				if(triggerActivate)
					worldIn.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(SummonerBlock.TRIGGERED, true));
				
				cooldownTimer = 1;
			}
		}
	}
	
	public void setSummonedEntity(EntityType<?> entityTypeIn, @Nullable PlayerEntity playerEntityIn)
	{
		this.summonType = entityTypeIn;
		
		if(playerEntityIn != null)
			playerEntityIn.displayClientMessage(new TranslationTextComponent(SUMMON_TYPE_CHANGE, summonType.getRegistryName()), true);
	}
	
	public EntityType<?> getSummonedEntity()
	{
		if(summonType == null)
			summonType = MSEntityTypes.IMP;
		return this.summonType;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		
		cooldownTimer = compound.getInt("cooldownTimer");
		Optional<EntityType<?>> attemptedSummonType = EntityType.byString(compound.getString("summonType"));
		attemptedSummonType.ifPresent(entityType -> summonType = entityType);
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("cooldownTimer", cooldownTimer);
		compound.putString("summonType", EntityType.getKey(getSummonedEntity()).toString());
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
}