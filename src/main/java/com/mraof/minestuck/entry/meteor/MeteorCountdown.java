package com.mraof.minestuck.entry.meteor;

import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nullable;
public class MeteorCountdown
{
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final PlayerIdentifier owner;
	private final BlockPos cruxtruderPos;
	private final ResourceKey<Level> levelKey;
	private final float meteorSize;
	private final String playerKey;
	
	private int ticksElapsed = 0;
	private boolean musicTriggered = false;
	private int meteorEntityId = -1;
	
	public MeteorCountdown(PlayerIdentifier owner, BlockPos pos, ResourceKey<Level> levelKey, int enteredPlayers)
	{
		this.owner = owner;
		this.cruxtruderPos = pos;
		this.levelKey = levelKey;
		this.playerKey = owner.getCommandString();
		// Size: 1.0 for 0 entered, +0.2 per additional player
		this.meteorSize = 1.0f + Math.min(enteredPlayers, 10) * 0.2f;
	}
	
	private MeteorCountdown(PlayerIdentifier owner, BlockPos pos, ResourceKey<Level> levelKey, float size, int ticksElapsed, boolean musicTriggered, int entityId)
	{
		this.owner = owner;
		this.cruxtruderPos = pos;
		this.levelKey = levelKey;
		this.playerKey = owner.getCommandString();
		this.meteorSize = size;
		this.ticksElapsed = ticksElapsed;
		this.musicTriggered = musicTriggered;
		this.meteorEntityId = entityId;
	}
	
	public void tick()
	{
		ticksElapsed++;
	}
	
	public boolean isExpired()
	{
		return ticksElapsed >= MeteorManager.TOTAL_TICKS;
	}
	
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		owner.saveToNBT(tag, "owner");
		tag.putInt("x", cruxtruderPos.getX());
		tag.putInt("y", cruxtruderPos.getY());
		tag.putInt("z", cruxtruderPos.getZ());
		tag.putFloat("size", meteorSize);
		tag.putInt("ticks", ticksElapsed);
		tag.putBoolean("music", musicTriggered);
		tag.putInt("entityId", meteorEntityId);
		Level.RESOURCE_KEY_CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, levelKey).resultOrPartial(LOGGER::error).ifPresent(t -> tag.put("level", t));
		return tag;
	}
	
	@Nullable
	public static MeteorCountdown read(CompoundTag tag)
	{
		PlayerIdentifier owner = IdentifierHandler.load(tag, "owner").result().orElse(null);
		if(owner == null) return null;
		
		BlockPos pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
		float size = tag.getFloat("size");
		int ticks = tag.getInt("ticks");
		boolean music = tag.getBoolean("music");
		int entityId = tag.getInt("entityId");
		
		ResourceKey<Level> levelKey = Level.RESOURCE_KEY_CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, tag.get("level")).resultOrPartial(LOGGER::error).orElse(Level.OVERWORLD);
		
		return new MeteorCountdown(owner, pos, levelKey, size, ticks, music, entityId);
	}
	
	public PlayerIdentifier getOwner()
	{
		return owner;
	}
	
	public BlockPos getCruxtruderPos()
	{
		return cruxtruderPos;
	}
	
	public ResourceKey<Level> getLevelKey()
	{
		return levelKey;
	}
	
	public float getMeteorSize()
	{
		return meteorSize;
	}
	
	public String getPlayerKey()
	{
		return playerKey;
	}
	
	public int getTicksElapsed()
	{
		return ticksElapsed;
	}
	
	public boolean isMusicTriggered()
	{
		return musicTriggered;
	}
	
	public void setMusicTriggered(boolean v)
	{
		musicTriggered = v;
	}
	
	public int getMeteorEntityId()
	{
		return meteorEntityId;
	}
	
	public void setMeteorEntityId(int id)
	{
		meteorEntityId = id;
	}
}