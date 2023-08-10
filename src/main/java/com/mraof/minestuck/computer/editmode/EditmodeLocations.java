package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Describes a list of locations in which a player in editmode can move around. Should be stored for each Sburb Client
 */
public class EditmodeLocations
{
	//TODO prevent going to non Medium dimensions post Entry, delete maps to the Overworld
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Multimap<ResourceKey<Level>, BlockPos> locations = ArrayListMultimap.create();
	
	public EditmodeLocations(ResourceKey<Level> level, BlockPos pos)
	{
		addEntry(level, pos);
	}
	
	public EditmodeLocations()
	{
	}
	
	public Multimap<ResourceKey<Level>, BlockPos> getLocations()
	{
		return locations;
	}
	
	public void addEntry(ResourceKey<Level> level, BlockPos pos)
	{
		if(level == null || pos == null)
			return;
		
		//TODO validate the entry
		locations.put(level, pos);
	}
	
	public void removeEntry(ResourceKey<Level> level, BlockPos pos)
	{
		if(level == null || pos == null)
			return;
		
		locations.remove(level, pos);
	}
	
	public static CompoundTag write(Multimap<ResourceKey<Level>, BlockPos> locations, CompoundTag nbt)
	{
		for(Map.Entry<ResourceKey<Level>, BlockPos> entry : locations.entries())
		{
			ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, entry.getKey().location()).resultOrPartial(LOGGER::error)
					.ifPresent(tag -> nbt.put("dim", tag));
			
			BlockPos pos = entry.getValue();
			
			nbt.putInt("x", pos.getX());
			nbt.putInt("y", pos.getY());
			nbt.putInt("z", pos.getZ());
		}
		
		return nbt;
	}
	
	public static EditmodeLocations read(CompoundTag nbt)
	{
		EditmodeLocations editmodeLocations = new EditmodeLocations();
		
		ListTag locationsTag = nbt.getList("editmode_location", Tag.TAG_COMPOUND);
		for(int i = 0; i < locationsTag.size(); i++)
		{
			CompoundTag dataTag = locationsTag.getCompound(i);
			
			ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, dataTag.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
			
			int posX = dataTag.getInt("x");
			int posY = dataTag.getInt("y");
			int posZ = dataTag.getInt("z");
			
			editmodeLocations.addEntry(dimension, new BlockPos(posX, posY, posZ));
		}
		
		return editmodeLocations;
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
		
		//temp for testing
		locations.put(editLevel.dimension(), new BlockPos(18,71,-36));
		
		if(!locations.containsKey(editLevel.dimension()))
			return false;
		
		//TODO Not a major deal but players who Enter in another persons Land are given more freedom of movement
		int range = MSDimensions.isLandDimension(editPlayer.server, editLevel.dimension()) ? MinestuckConfig.SERVER.landEditRange.get() : MinestuckConfig.SERVER.overworldEditRange.get();
		range -= 6; //temp for testing
		
		Collection<BlockPos> allLevelPos = locations.get(editLevel.dimension());
		
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
		
		return false;
	}
}