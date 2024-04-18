package com.mraof.minestuck.entity.item;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class HangingArtEntity<T extends HangingArtEntity.IArt> extends HangingEntity implements IEntityWithComplexSpawn
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private T art;
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, Level level)
	{
		super(type, level);
	}
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, Level level, BlockPos pos, Direction direction)
	{
		super(type, level, pos);
		List<T> artList = Lists.newArrayList();
		int maxValidSize = 0;
		
		for(T art : getArtSet())
		{
			this.art = art;
			this.setDirection(direction);
			
			if(this.survives())
			{
				artList.add(art);
				maxValidSize = Math.max(maxValidSize, art.getSizeX() * art.getSizeY());
			}
		}
		
		//Makes sure that the art picked is the largest possible
		int maxSize = maxValidSize;
		artList.removeIf(art -> art.getSizeX() * art.getSizeY() < maxSize);
		
		if(!artList.isEmpty())
			this.art = artList.get(this.random.nextInt(artList.size()));
		
		
		this.setDirection(direction);
	}
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, Level level, BlockPos pos, Direction direction, T art)
	{
		this(type, level, pos, direction);
		
		this.art = art;
		
		this.setDirection(direction);
	}
	
	public T getArt()
	{
		return art;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putString("Motive", this.art.getTitle());
		compound.putByte("Facing", (byte) this.direction.get2DDataValue());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		String artTitle = compound.getString("Motive");
		
		Optional<T> optionalArt = this.getArtSet().stream().filter(art -> art.getTitle().equals(artTitle)).findAny();
		if(optionalArt.isPresent())
			this.art = optionalArt.get();
		else
		{
			this.art = this.getDefault();
			LOGGER.warn("Could not load art {} for type {}, resorting to the default type.", artTitle, this.getType().getDescriptionId());
		}
		super.readAdditionalSaveData(compound);
		this.setDirection(Direction.from2DDataValue(compound.getByte("Facing")));
	}
	
	@Override
	public int getWidth()
	{
		return art == null ? 1 : art.getSizeX();
	}
	
	@Override
	public int getHeight()
	{
		return art == null ? 1 : art.getSizeY();
	}
	
	@Override
	public void dropItem(Entity brokenEntity)
	{
		if(this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
		{
			this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
			
			if(brokenEntity instanceof Player player && player.getAbilities().instabuild)
				return;
			
			this.spawnAtLocation(this.getStackDropped());
		}
		
	}
	
	@Override
	public void playPlacementSound()
	{
		this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
	}
	
	@Override
	public void moveTo(double x, double y, double z, float yaw, float pitch)
	{
		this.setPos(x, y, z);
	}
	
	@Override
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements)
	{
		BlockPos blockpos = this.pos.offset(Mth.floor(x - this.getX()), Mth.floor(y - this.getY()), Mth.floor(z - this.getZ()));
		this.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeByte(this.direction.ordinal());
		
		buffer.writeInt(this.pos.getX());
		buffer.writeInt(this.pos.getY());
		buffer.writeInt(this.pos.getZ());
		
		String name = art.getTitle();
		buffer.writeInt(name.length());
		for(char c : name.toCharArray())
			buffer.writeChar(c);
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf data)
	{
		Direction facing = Direction.values()[data.readByte() % Direction.values().length];
		
		this.pos = new BlockPos(data.readInt(), data.readInt(), data.readInt());
		
		int length = data.readInt();
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < length; i++)
			str.append(data.readChar());
		
		String name = str.toString();
		this.art = this.getArtSet().stream().filter(art -> art.getTitle().equals(name)).findAny().orElseGet(this::getDefault);
		
		this.setDirection(facing);
	}
	
	public abstract Set<T> getArtSet();
	
	public abstract T getDefault();
	
	public abstract ItemStack getStackDropped();
	
	public interface IArt
	{
		String getTitle();
		
		int getSizeX();
		
		int getSizeY();
		
		int getOffsetX();
		
		int getOffsetY();
	}
}
