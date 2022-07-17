package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.world.gen.feature.structure.CoreCompatibleScatteredStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.MSStructureFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructureCoreTileEntity extends BlockEntity
{
	private int tickCycle;
	@Nonnull
	private ActionType actionType;
	private int shutdownRange = 32;
	private boolean hasWiped = false;
	public final ConfiguredStructureFeature<?, ?>[] SOLVABLE_STRUCTURES = new ConfiguredStructureFeature<?, ?>[]{MSStructureFeatures.FROG_TEMPLE.get()};
	
	/**
	 * READ AND WIPE checks to see if the structure piece hasBeenCompleted variable is true, and then prevents puzzle blocks not meant to be utilized for survival players from working
	 * READ AND REDSTONE checks to see if the structure piece hasBeenCompleted variable is true, and then gives off a redstone signal if so
	 * WRITE turns the structure piece hasBeenCompleted variable to true if it is not yet
	 */
	public enum ActionType
	{
		READ_AND_WIPE,
		READ_AND_REDSTONE,
		WRITE;
		
		public static ActionType fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < ActionType.values().length)
				return ActionType.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for structure core action type!");
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
		}
	}
	
	public StructureCoreTileEntity(BlockPos pos, BlockState state)
	{
		super(MSTileEntityTypes.STRUCTURE_CORE.get(), pos, state);
		actionType = ActionType.READ_AND_REDSTONE;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, StructureCoreTileEntity blockEntity)
	{
		int cycleRate = (blockEntity.hasWiped && blockEntity.actionType == ActionType.READ_AND_WIPE) ? 600 : 100;
		if(blockEntity.tickCycle >= cycleRate)
		{
			blockEntity.sendUpdate();
			blockEntity.tickCycle = 0;
		}
		blockEntity.tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && level.isAreaLoaded(getBlockPos(), 1) && getBlockState().getValue(StructureCoreBlock.ACTIVE))
		{
			StructureFeatureManager structureManager = ((ServerLevel) level).structureFeatureManager();
			List<StructureStart> structureStarts = getStructureStarts(structureManager);
			
			for(StructureStart structureStartIterate : structureStarts)
			{
				CoreCompatibleScatteredStructurePiece piece = getStructurePiece(structureStartIterate);
				
				if(piece != null)
				{
					if(actionType == ActionType.WRITE)
					{
						writeToStructure(piece);
					} else if(actionType == ActionType.READ_AND_WIPE)
					{
						if(piece.hasBeenCompleted())
						{
							wipeSlate();
						}
					} else if(actionType == ActionType.READ_AND_REDSTONE)
					{
						BlockState newState = getBlockState().setValue(StructureCoreBlock.POWERED, piece.hasBeenCompleted());
						if(newState != getBlockState())
							level.setBlockAndUpdate(getBlockPos(), newState); //TODO this results in the block getting powered based on the state of the last structure piece, find out a more optimal set of mechanics
					}
				}
			}
		}
	}
	
	public List<StructureStart> getStructureStarts(StructureFeatureManager structureManager)
	{
		List<StructureStart> structureStartList = new ArrayList<>();
		for(ConfiguredStructureFeature<?, ?> structureListIterate : SOLVABLE_STRUCTURES)
		{
			StructureStart potentialStructureStart = structureManager.getStructureAt(getBlockPos(), structureListIterate);
			
			if(potentialStructureStart.isValid())
				structureStartList.add(potentialStructureStart);
		}
		
		return structureStartList;
	}
	
	private void writeToStructure(CoreCompatibleScatteredStructurePiece piece)
	{
		if(!piece.hasBeenCompleted())
			piece.nowCompleted();
	}
	
	private CoreCompatibleScatteredStructurePiece getStructurePiece(StructureStart structureStart)
	{
		List<StructurePiece> structurePieceList = structureStart.getPieces();
		for(StructurePiece pieceIterate : structurePieceList)
		{
			if(pieceIterate instanceof CoreCompatibleScatteredStructurePiece)
			{
				return (CoreCompatibleScatteredStructurePiece) pieceIterate;
			}
		}
		
		return null;
	}
	
	private void wipeSlate()
	{
		if(level != null)
		{
			for(BlockPos iteratePos : BlockPos.betweenClosed(getBlockPos().offset(shutdownRange, shutdownRange, shutdownRange), getBlockPos().offset(-shutdownRange, -shutdownRange, -shutdownRange)))
			{
				BlockState iterateState = level.getBlockState(iteratePos);
				
				//removing functionality from blocks that should have limited survival access
				
				if(iterateState.hasProperty(MSProperties.SHUT_DOWN) && !iterateState.getValue(MSProperties.SHUT_DOWN))
				{
					level.setBlock(iteratePos, iterateState.setValue(MSProperties.SHUT_DOWN, true), Block.UPDATE_ALL);
					this.hasWiped = true;
				}
				
				if(iterateState.hasProperty(MSProperties.UNTRIGGERABLE) && iterateState.getValue(MSProperties.UNTRIGGERABLE))
				{
					if(iterateState.getBlock() instanceof SummonerBlock)
					{
						level.setBlock(iteratePos, iterateState.setValue(SummonerBlock.UNTRIGGERABLE, false).setValue(SummonerBlock.TRIGGERED, true), Block.UPDATE_ALL);
					}
					
					this.hasWiped = true;
				}
			}
		}
	}
	
	public void setShutdownRange(int shutdownRange)
	{
		this.shutdownRange = shutdownRange;
	}
	
	public int getShutdownRange()
	{
		return shutdownRange;
	}
	
	public void setHasWiped(boolean hasWiped)
	{
		this.hasWiped = hasWiped;
	}
	
	public boolean getHasWiped()
	{
		return hasWiped;
	}
	
	public ActionType getActionType()
	{
		return this.actionType;
	}
	
	public void setActionType(ActionType actionTypeIn)
	{
		actionType = Objects.requireNonNull(actionTypeIn);
	}
	
	public void prepForUpdate()
	{
		this.tickCycle = 600;
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		tickCycle = compound.getInt("tickCycle");
		this.actionType = ActionType.fromInt(compound.getInt("actionTypeOrdinal"));
		hasWiped = compound.getBoolean("hasWiped");
		shutdownRange = compound.getInt("shutdownRange");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("actionTypeOrdinal", getActionType().ordinal());
		compound.putBoolean("hasWiped", hasWiped);
		compound.putInt("shutdownRange", shutdownRange);
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