package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.CoreCompatibleScatteredStructurePiece;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class StructureCoreTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	@Nonnull
	private ActionType actionType;
	private int shutdownRange = 32;
	private boolean hasWiped = false;
	public final Structure<?>[] SOLVABLE_STRUCTURES = new Structure<?>[]{MSFeatures.FROG_TEMPLE};
	
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
			for(ActionType type : ActionType.values())
			{
				if(type.ordinal() == ordinal)
					return type;
			}
			throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for structure core action type!");
		}
		
		public String getNameNoSpaces()
		{
			return name().replace('_', ' ');
		}
	}
	
	public StructureCoreTileEntity()
	{
		super(MSTileEntityTypes.STRUCTURE_CORE.get());
		actionType = ActionType.READ_AND_REDSTONE;
	}
	
	@Override
	public void tick()
	{
		if(level == null || level.isClientSide)
			return;
		
		int cycleRate = (hasWiped && actionType == ActionType.READ_AND_WIPE) ? 600 : 100;
		if(tickCycle >= cycleRate)
		{
			sendUpdate();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && level.isAreaLoaded(getBlockPos(), 1) && getBlockState().getValue(StructureCoreBlock.ACTIVE))
		{
			StructureManager structureManager = ((ServerWorld) level).structureFeatureManager();
			StructureStart<?> structureStart = getStructureStart(structureManager);
			if(structureStart != null && structureStart.isValid())
			{
				CoreCompatibleScatteredStructurePiece piece = getStructurePiece(structureStart);
				
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
						if(piece.hasBeenCompleted())
						{
							if(!getBlockState().getValue(StructureCoreBlock.POWERED))
								level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StructureCoreBlock.POWERED, true));
						} else
						{
							if(getBlockState().getValue(StructureCoreBlock.POWERED))
								level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StructureCoreBlock.POWERED, false));
						}
					}
				}
			}
		}
	}
	
	public StructureStart<?> getStructureStart(StructureManager structureManager)
	{
		for(Structure<?> structureListIterate : SOLVABLE_STRUCTURES)
		{
			return structureManager.getStructureAt(getBlockPos(), true, structureListIterate);
		}
		
		return null;
	}
	
	private void writeToStructure(CoreCompatibleScatteredStructurePiece piece)
	{
		//compoundnbt.putString("id", Registry.STRUCTURE_FEATURE.getKey(this.getFeature()).toString());
		
		if(!piece.hasBeenCompleted())
			piece.nowCompleted();
	}
	
	private CoreCompatibleScatteredStructurePiece getStructurePiece(StructureStart<?> structureStart)
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
					level.setBlock(iteratePos, iterateState.setValue(MSProperties.SHUT_DOWN, true), Constants.BlockFlags.DEFAULT);
					this.hasWiped = true;
				}
				
				if(iterateState.hasProperty(MSProperties.UNTRIGGERABLE) && iterateState.getValue(MSProperties.UNTRIGGERABLE))
				{
					if(iterateState.getBlock() instanceof SummonerBlock)
					{
						level.setBlock(iteratePos, iterateState.setValue(SummonerBlock.UNTRIGGERABLE, false).setValue(SummonerBlock.TRIGGERED, true), Constants.BlockFlags.DEFAULT);
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
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		tickCycle = compound.getInt("tickCycle");
		this.actionType = ActionType.fromInt(compound.getInt("actionTypeOrdinal"));
		hasWiped = compound.getBoolean("hasWiped");
		shutdownRange = compound.getInt("shutdownRange");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putInt("actionTypeOrdinal", getActionType().ordinal());
		compound.putBoolean("hasWiped", hasWiped);
		compound.putInt("shutdownRange", shutdownRange);
		
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