package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.redstone.SummonerBlock;
import com.mraof.minestuck.block.redstone.TogglerBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.FrogTemplePiece;
import net.minecraft.block.Block;
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

import java.util.List;

public class TogglerTileEntity extends TileEntity implements ITickableTileEntity
{
	private int tickCycle;
	private boolean hasWiped = false;
	public final Structure<?>[] SOLVABLE_STRUCTURES = new Structure<?>[]{MSFeatures.FROG_TEMPLE};
	
	public TogglerTileEntity()
	{
		super(MSTileEntityTypes.TOGGLER.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null)
			return;
		
		if(tickCycle >= 100)
		{
			sendUpdate();
			tickCycle = 0;
		}
		tickCycle++;
	}
	
	private void sendUpdate()
	{
		if(level != null && !level.isClientSide && level.isAreaLoaded(getBlockPos(), 1))
		{
			if(getBlockState().getValue(TogglerBlock.POWERED) && !hasWiped)
			{
				StructureManager structureManager = ((ServerWorld) level).structureFeatureManager();
				if(isInStructureChunk(structureManager))
				{
					Debug.debugf("meets all conditions, wiping the slate clean");
					
					wipeSlate();
				}
			}
			
		}
	}
	
	public boolean isInStructureChunk(StructureManager structureManager)
	{
		for(Structure<?> structureListIterate : SOLVABLE_STRUCTURES)
		{
			Debug.debugf("%s", structureListIterate.getRegistryName());
			StructureStart<?> structureStart = structureManager.getStructureAt(getBlockPos(), true, structureListIterate);
			if(structureStart.isValid())
			{
				//compoundnbt.putString("id", Registry.STRUCTURE_FEATURE.getKey(this.getFeature()).toString());
				Debug.debugf("is in frog temple chunk");
				
				List<StructurePiece> structurePieceList = structureStart.getPieces();
				for(StructurePiece pieceIterate : structurePieceList)
				{
					if(pieceIterate instanceof FrogTemplePiece)
					{
						if(!((FrogTemplePiece) pieceIterate).hasBeenUsed())
						{
							((FrogTemplePiece) pieceIterate).nowUsed();
							return true;
						}
					}
				}
				
				Debug.debugf("has been used");
			}
		}
		
		return false;
	}
	
	private void wipeSlate()
	{
		if(level != null)
		{
			for(BlockPos iteratePos : BlockPos.betweenClosed(getBlockPos().offset(32, 32, 32), getBlockPos().offset(-32, -32, -32)))
			{
				BlockState iterateState = level.getBlockState(iteratePos);
				Block iterateBlock = iterateState.getBlock();
				
				//removing functionality from blocks that should have limited survival access
				for(Property<?> property : iterateState.getProperties())
				{
					if(property.equals(MSProperties.DISCHARGED) && !iterateState.getValue(MSProperties.DISCHARGED))
					{
						if(iterateState.getBlock() instanceof SummonerBlock)
						{
							level.setBlock(iteratePos, iterateState.setValue(MSProperties.DISCHARGED, false).setValue(SummonerBlock.TRIGGERED, true), Constants.BlockFlags.DEFAULT); //the boolean is inverted specifically with the summoner
						} else
							level.setBlock(iteratePos, iterateState.setValue(MSProperties.DISCHARGED, true), Constants.BlockFlags.DEFAULT);
						
						this.hasWiped = true;
						
						break;
					}
				}
			}
		}
	}
	
	public void setClockSpeed(int clockSpeed)
	{
		//this.clockSpeed = clockSpeed;
	}
	
	public int getClockSpeed()
	{
		return 0;
	}
	
	@Override
	public void load(BlockState state, CompoundNBT compound)
	{
		super.load(state, compound);
		tickCycle = compound.getInt("tickCycle");
		hasWiped = compound.getBoolean("hasWiped");
		//clockSpeed = compound.getInt("clockSpeed");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		
		compound.putInt("tickCycle", tickCycle);
		compound.putBoolean("hasWiped", hasWiped);
		//compound.putInt("clockSpeed", clockSpeed);
		
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