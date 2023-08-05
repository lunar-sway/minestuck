package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.ComputerBlock;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Describes a list of locations in which a player in editmode can move around. Should be stored for each Sburb Client
 */
public class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	//TODO switch to using ResourceKey<Level>
	private final Multimap<Level, BlockPos> locations = ArrayListMultimap.create();
	//ArrayListMultimap
	
	public EditmodeLocations(Level level, BlockPos pos)
	{
		addEntry(level, pos);
	}
	
	public void addEntry(Level level, BlockPos pos)
	{
		if(level == null || pos == null)
			return;
		
		//TODO validate the entry
		locations.put(level, pos);
	}
	
	/*private boolean validateSource(Level level, BlockPos pos)
	{
		BlockState blockState = level.getBlockState(pos);
		
		if(blockState.getBlock() instanceof ComputerBlock computerBlock)
		{
			//
		}
		
		boolean isComputer = blockState.getBlock() instanceof ComputerBlock computerBlock && ComputerBlock.;
		if( !=)
		
		return ;
	}*/
	
	/**
	 * Takes in a player and looks through each source (such as a computer) providing editmode to see if the player resides within one of the spaces
	 *
	 * @param editPlayer Player in editmode
	 * @return Whether the player is standing in a supported region
	 */
	public boolean isValidLocation(ServerPlayer editPlayer)
	{
		Level editLevel = editPlayer.level();
		BlockPos editPos = editPlayer.blockPosition();
		double editPosX = editPos.getX();
		double editPosZ = editPos.getZ();
		
		if(!locations.containsKey(editLevel))
			return false;
		
		//TODO Not a major deal but players who Enter in another persons Land are given more freedom of movement
		int range = MSDimensions.isLandDimension(editPlayer.server, editLevel.dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
		
		Collection<BlockPos> allLevelPos = locations.get(editLevel);
		
		for(BlockPos iteratePos : allLevelPos)
		{
			int centerX = iteratePos.getX();
			int centerZ = iteratePos.getZ();
			
			//if the range is 1 and the player pos isnt an exact match, dont bother
			if(range == 1 && editPos != iteratePos)
				continue;
			
			//TODO does not factor in player offset such as in ServerEditHandler
			boolean xMatches = editPosX >= centerX - range && editPosX <= centerX + range;
			boolean zMatches = editPosZ >= centerZ - range && editPosZ <= centerZ + range;
			
			if(xMatches && zMatches)
				return true;
		}
		
		return true;
	}
}