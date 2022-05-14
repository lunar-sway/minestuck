package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.CoreCompatabileScatteredStructurePiece;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.Property;
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
				Debug.debugf("meets all conditions, now doing action type function");
				if(actionType == ActionType.WRITE)
				{
					writeToStructure(structureStart);
				} else if(actionType == ActionType.READ_AND_WIPE)
				{
					wipeSlate();
				} else if(actionType == ActionType.READ_AND_REDSTONE)
				{
					CoreCompatabileScatteredStructurePiece piece = getStructurePiece(structureStart);
					
					if(piece != null && piece.hasBeenUsed())
					{
						if(!getBlockState().getValue(StructureCoreBlock.POWERED))
							level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StructureCoreBlock.POWERED, true));
					} else
					{
						level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(StructureCoreBlock.POWERED, false));
					}
				}
			}
		}
	}
	
	public StructureStart<?> getStructureStart(StructureManager structureManager)
	{
		for(Structure<?> structureListIterate : SOLVABLE_STRUCTURES)
		{
			Debug.debugf("%s", structureListIterate.getRegistryName());
			return structureManager.getStructureAt(getBlockPos(), true, structureListIterate);
		}
		
		return null;
	}
	
	private void writeToStructure(StructureStart<?> structureStart)
	{
		//compoundnbt.putString("id", Registry.STRUCTURE_FEATURE.getKey(this.getFeature()).toString());
		
		CoreCompatabileScatteredStructurePiece piece = getStructurePiece(structureStart);
		if(piece != null && !piece.hasBeenUsed())
			piece.nowUsed();
		if(piece != null && piece.hasBeenUsed())
			Debug.debugf("has already been used");
	}
	
	private CoreCompatabileScatteredStructurePiece getStructurePiece(StructureStart<?> structureStart)
	{
		List<StructurePiece> structurePieceList = structureStart.getPieces();
		for(StructurePiece pieceIterate : structurePieceList)
		{
			if(pieceIterate instanceof CoreCompatabileScatteredStructurePiece)
			{
				return (CoreCompatabileScatteredStructurePiece) pieceIterate;
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
				for(Property<?> property : iterateState.getProperties())
				{
					if(property.equals(MSProperties.SHUT_DOWN) && !iterateState.getValue(MSProperties.SHUT_DOWN))
					{
						if(iterateState.getBlock() instanceof SummonerBlock)
						{
							level.setBlock(iteratePos, iterateState.setValue(MSProperties.SHUT_DOWN, false).setValue(SummonerBlock.TRIGGERED, true), Constants.BlockFlags.DEFAULT); //the boolean is inverted specifically with the summoner
						} else
							level.setBlock(iteratePos, iterateState.setValue(MSProperties.SHUT_DOWN, true), Constants.BlockFlags.DEFAULT);
						
						this.hasWiped = true;
						
						break;
					}
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