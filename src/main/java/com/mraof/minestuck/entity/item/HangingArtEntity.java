package com.mraof.minestuck.entity.item;

import com.google.common.collect.Lists;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.Set;

public abstract class HangingArtEntity<T extends HangingArtEntity.IArt> extends HangingEntity implements IEntityAdditionalSpawnData
{
	public T art;
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, World worldIn, BlockPos pos, Direction direction)
	{
		super(type, worldIn, pos);
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
	
	public HangingArtEntity(EntityType<? extends HangingArtEntity<T>> type, World worldIn, BlockPos pos, Direction direction, String title)
	{
		this(type, worldIn, pos, direction);
		
		for(T art : this.getArtSet())
		{
			if(art.getTitle().equals(title))
			{
				this.art = art;
				break;
			}
		}
		
		this.setDirection(direction);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putString("Motive", this.art.getTitle());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound)
	{
		String s = compound.getString("Motive");
		
		for(T art : this.getArtSet())
			if(art.getTitle().equals(s))
			{
				this.art = art;
				break;
			}
		
		if(this.art == null)
		{
			this.art = this.getDefault();
			Debug.warnf("Could not load art %s for type %s, resorting to the default type.", s, this.getType().getDescriptionId());
		}
		super.readAdditionalSaveData(compound);
		recalculateBoundingBox();	//Fixes a vanilla-related bug where pos and bb isn't updated when loaded from nbt
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
		if(this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
		{
			this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
			
			if(brokenEntity instanceof PlayerEntity)
			{
				PlayerEntity entityplayer = (PlayerEntity) brokenEntity;
				
				if(entityplayer.abilities.instabuild)
					return;
			}
			
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
	public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean teleport)
	{
		BlockPos blockpos = this.pos.offset(x - this.getX(), y - this.getY(), z - this.getZ());
		this.setPos((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
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
	public void readSpawnData(PacketBuffer data)
	{
		Direction facing = Direction.values()[data.readByte()%Direction.values().length];
		
		this.pos = new BlockPos(data.readInt(), data.readInt(), data.readInt());
		
		int length = data.readInt();
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < length; i++)
			str.append(data.readChar());
		
		String name = str.toString();
		for(T art : this.getArtSet())
			if(art.getTitle().equals(name))
			{
				this.art = art;
				break;
			}
		
		this.setDirection(facing);
	}
	
	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
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

	public Direction getFacingDirection() {return direction;}
}
