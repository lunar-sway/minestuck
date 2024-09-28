package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.network.block.SummonerSettingsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SummonerBlockEntity extends BlockEntity
{
	private EntityType<?> summonType;
	private int cooldownTimer;
	private int summonRange = 8; //default is 8, but can be set(via gui) between 1 and 64
	
	public SummonerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.SUMMONER.get(), pos, state);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, SummonerBlockEntity blockEntity)
	{
		if(level == null)
			return;
		
		if(blockEntity.cooldownTimer >= 200) //summoner has a cooldown of 10 seconds(10 sec * 20 tick) to prevent entity spamming
		{
			blockEntity.cooldownTimer = 0;
		}
		
		if(blockEntity.cooldownTimer != 0)
			blockEntity.cooldownTimer++;
	}
	
	public void summonEntity(Level levelIn, BlockPos summonerBlockPos, EntityType<?> type, boolean triggerActivate, boolean playParticles)
	{
		if(type == null)
			throw new IllegalStateException("SummonerBlockEntity unable to create a new entity. Entity factory returned null!");
		
		if(cooldownTimer == 0 && levelIn instanceof ServerLevel level)
		{
			int iterateTracker = 0;
			for(int i = 0; i < 60; i++) //arbitrarily high
			{
				iterateTracker = i;
				double newPosX = summonerBlockPos.getX() + (level.random.nextDouble() - 0.5D) * summonRange;
				double newPosY = summonerBlockPos.getY() + (level.random.nextDouble() - 0.5D) * summonRange;
				double newPosZ = summonerBlockPos.getZ() + (level.random.nextDouble() - 0.5D) * summonRange;
				if(level.noCollision(type.getAABB(newPosX, newPosY, newPosZ)) && //checks that entity wont suffocate
						SpawnPlacements.Type.ON_GROUND.canSpawnAt(level, BlockPos.containing(newPosX, newPosY, newPosZ), type)) //helps spawn entity on a valid floor
				{
					BlockPos newBlockPos = BlockPos.containing(newPosX, newPosY, newPosZ);
					type.spawn(level, (CompoundTag) null, null, newBlockPos, MobSpawnType.MOB_SUMMONED, true, true);
					
					if(playParticles)
					{
						//TODO caused a crash, bring back in later
						/*for(int particleIterate = 0; particleIterate < 5; particleIterate++)
						{
							level.addParticle(ParticleTypes.POOF, true, newPosX, newPosY, newPosZ, 0.1, 0.1, 0.1);
						}*/
					}
					break;
				}
			}
			
			if(iterateTracker == 59)
				level.scheduleTick(new BlockPos(summonerBlockPos), getBlockState().getBlock(), 30); //if a valid resting spot was not found in the 59 checks of the for loop then the block will be reset and will try again in 1.5 seconds
			else
			{
				if(triggerActivate)
					level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(SummonerBlock.TRIGGERED, true));
				
				cooldownTimer = 1;
			}
		}
	}
	
	public void setSummonedEntity(EntityType<?> entityTypeIn)
	{
		this.summonType = entityTypeIn;
		setChanged();
		if(getLevel() instanceof ServerLevel serverLevel)
			serverLevel.getChunkSource().blockChanged(getBlockPos());
	}
	
	public EntityType<?> getSummonedEntity()
	{
		if(summonType == null)
			summonType = MSEntityTypes.IMP.get();
		return this.summonType;
	}
	
	public void setSummonRange(int rangeIn)
	{
		this.summonRange = Mth.clamp(rangeIn, 1, 64);
	}
	
	public int getSummonRange()
	{
		return summonRange;
	}
	
	public void handleSettingsPacket(SummonerSettingsPacket packet)
	{
		if(packet.entityType() != null)
			setSummonedEntity(packet.entityType());
		
		setSummonRange(packet.summonRange());
		setChanged();
		
		getLevel().setBlock(getBlockPos(), getBlockState().setValue(SummonerBlock.UNTRIGGERABLE, packet.isUntriggerable()), Block.UPDATE_ALL);
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		cooldownTimer = compound.getInt("cooldownTimer");
		if(compound.contains("summonRange", Tag.TAG_ANY_NUMERIC))
			this.setSummonRange(compound.getInt("summonRange"));
		else this.setSummonRange(16); //16 was previously the default
		Optional<EntityType<?>> attemptedSummonType = EntityType.byString(compound.getString("summonType"));
		attemptedSummonType.ifPresent(entityType -> summonType = entityType);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("cooldownTimer", cooldownTimer);
		compound.putInt("summonRange", summonRange);
		compound.putString("summonType", EntityType.getKey(getSummonedEntity()).toString());
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
