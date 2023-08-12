package com.mraof.minestuck.computer.editmode;

import com.google.common.collect.*;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
		
		//TODO validate
		locations.put(level, pos);
	}
	
	public void removeEntry(ResourceKey<Level> level, BlockPos pos)
	{
		if(level == null || pos == null)
			return;
		
		locations.remove(level, pos);
	}
	
	public static ListTag write(Multimap<ResourceKey<Level>, BlockPos> locations, ListTag listTag)
	{
		for(Map.Entry<ResourceKey<Level>, BlockPos> entry : locations.entries())
		{
			CompoundTag nbt = new CompoundTag();
			
			ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, entry.getKey().location()).resultOrPartial(LOGGER::error)
					.ifPresent(tag -> nbt.put("dim", tag));
			
			BlockPos pos = entry.getValue();
			
			nbt.putInt("x", pos.getX());
			nbt.putInt("y", pos.getY());
			nbt.putInt("z", pos.getZ());
			
			listTag.add(nbt);
		}
		
		return listTag;
	}
	
	public static EditmodeLocations read(CompoundTag nbt)
	{
		ResourceKey<Level> dimension = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get("dim")).resultOrPartial(LOGGER::error).orElse(null);
		
		int posX = nbt.getInt("x");
		int posY = nbt.getInt("y");
		int posZ = nbt.getInt("z");
		
		return new EditmodeLocations(dimension, new BlockPos(posX, posY, posZ));
	}
	
	private boolean isValidSource(Level level, BlockPos pos)
	{
		if(level.getBlockEntity(pos) instanceof ComputerBlockEntity computerBlockEntity)
		{
			return !computerBlockEntity.isBroken() && computerBlockEntity.hasProgram(0);
		}
		
		return false;
	}
	
	/**
	 * Takes in a player and looks through each source (such as a computer) providing editmode to see if the player resides within one of the spaces
	 *
	 * @param editPlayer Player in editmode
	 * @return Whether the player is standing in a supported region
	 */
	public boolean isValidLocation(Player editPlayer, double range)
	{
		Level editLevel = editPlayer.level();
		double editPosX = editPlayer.getX();
		double editPosY = editPlayer.getY();
		double editPosZ = editPlayer.getZ();
		
		//temp for testing
		if(!locations.containsValue(new BlockPos(18,71,-36)))
			locations.put(editLevel.dimension(), new BlockPos(18,71,-36));
		if(!locations.containsValue(new BlockPos(10,71,-36)))
			locations.put(editLevel.dimension(), new BlockPos(10,71,-36));
		if(!locations.containsValue(new BlockPos(10,80,-32)))
			locations.put(editLevel.dimension(), new BlockPos(10,80,-32));
		
		if(!locations.containsKey(editLevel.dimension()))
			return false;
		
		Collection<BlockPos> allLevelPos = locations.get(editLevel.dimension());
		
		boolean xMatches = false;
		boolean yMatches = false;
		boolean zMatches = false;
		boolean allMatch = false;
		
		for(BlockPos iteratePos : allLevelPos)
		{
			int centerX = iteratePos.getX();
			int centerY = iteratePos.getY();
			int centerZ = iteratePos.getZ();
			
			//if the range is 1 and the player pos isnt an exact match, dont bother
			//if(range == 1 && editPos != iteratePos)
			//	continue;
			
			boolean localXMatches = editPosX >= centerX - range && editPosX <= centerX + range;
			boolean localYMatches = editPosY >= centerY - range && editPosY <= centerY + range;
			boolean localZMatches = editPosZ >= centerZ - range && editPosZ <= centerZ + range;
			
			//TODO does not factor in player offset such as in ServerEditHandler
			if(localXMatches)
				xMatches = true;
			
			if(localYMatches)
				yMatches = true;
			
			if(localZMatches)
				zMatches = true;
			
			if(localXMatches && localYMatches && localZMatches)
				allMatch = true;
		}
		
		if(!xMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(0, 1, 1));
		
		if(!yMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 0, 1));
		
		if(!zMatches && !allMatch)
			editPlayer.setDeltaMovement(editPlayer.getDeltaMovement().multiply(1, 1, 0));
		
		if(!allMatch)
		{
			//TODO do not use "old" pos, player "sticks" to the edge of the zone
			if(editPlayer.level().isClientSide)
				editPlayer.setPos(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
			else editPlayer.teleportTo(editPlayer.xOld, editPlayer.yOld, editPlayer.zOld);
		}
		
		return false;
	}
}