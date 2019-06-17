package com.mraof.minestuck.entity.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityHangingArt<T extends EntityHangingArt.IArt> extends EntityHanging implements IEntityAdditionalSpawnData
{
	public T art;
	private static boolean random = true;
	
	public EntityHangingArt(EntityType<?> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public EntityHangingArt(EntityType<?> type, World worldIn, BlockPos pos, EnumFacing facing)
	{
		super(type, worldIn, pos);
		List<T> artList = Lists.newArrayList();
		
		for(T art : getArtSet())
		{
			this.art = art;
			this.updateFacingWithBoundingBox(facing);
			
			if(this.onValidSurface())
				artList.add(art);
		}
		
		if(!artList.isEmpty())
				this.art = artList.get(this.rand.nextInt(artList.size()));
		
		
		this.updateFacingWithBoundingBox(facing);
	}

	public EntityHangingArt(EntityType<?> type, World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta, boolean random)
	{
		super(type, worldIn, pos);
		List<T> artList = Lists.<T>newArrayList();
		
		setRandom(random);
		
		for(T art : getArtSet())
		{
			this.art = art;
			this.updateFacingWithBoundingBox(facing);
			
			if(this.onValidSurface())
				artList.add(art);
		}
		
		if(!artList.isEmpty())
		{
			if(random)
				this.art = artList.get(this.rand.nextInt(artList.size()));
			else
				this.art = artList.get(meta);
		}
		
		this.updateFacingWithBoundingBox(facing);
	}
	
	@OnlyIn(Dist.CLIENT)
	public EntityHangingArt(EntityType<?> type, World worldIn, BlockPos pos, EnumFacing facing, String title)
	{
		this(type, worldIn, pos, facing);
		
		for(T art : this.getArtSet())
		{
			if(art.getTitle().equals(title))
			{
				this.art = art;
				break;
			}
		}
		
		this.updateFacingWithBoundingBox(facing);
	}
	
	@Override
	public void writeAdditional(NBTTagCompound compound)
	{
		super.writeAdditional(compound);
		compound.setString("Motive", this.art.getTitle());
	}
	
	@Override
	public void readAdditional(NBTTagCompound compound)
	{
		super.readAdditional(compound);
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
			Debug.warnf("Could not load art %s for type %s, resorting to the default type.", s, this.getType().getTranslationKey());
		}
	}
	
	@Override
	public int getWidthPixels()
	{
		return art.getSizeX();
	}
	
	@Override
	public int getHeightPixels()
	{
		return art.getSizeY();
	}
	
	@Override
	public void onBroken(Entity brokenEntity)
	{
		if(this.world.getGameRules().getBoolean("doEntityDrops"))
		{
			this.playSound(SoundEvents.ENTITY_PAINTING_BREAK, 1.0F, 1.0F);
			
			if(brokenEntity instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer) brokenEntity;
				
				if(entityplayer.abilities.isCreativeMode)
					return;
			}
			//TODO
			
				this.entityDropItem(this.getStackDropped(), 0.0F);
		}
			
	}
	
	@Override
	public void playPlaceSound()
	{
		this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
	}
	
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		this.setPosition(x, y, z);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean teleport)
	{
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeByte(this.facingDirection.ordinal());
		
		buffer.writeInt(this.hangingPosition.getX());
		buffer.writeInt(this.hangingPosition.getY());
		buffer.writeInt(this.hangingPosition.getZ());
		
		String name = art.getTitle();
		buffer.writeInt(name.length());
		for(char c : name.toCharArray())
			buffer.writeChar(c);
	}
	
	@Override
	public void readSpawnData(PacketBuffer data)
	{
		EnumFacing facing = EnumFacing.values()[data.readByte()%EnumFacing.values().length];
		
		this.hangingPosition = new BlockPos(data.readInt(), data.readInt(), data.readInt());
		
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
		
		this.updateFacingWithBoundingBox(facing);
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
	
	public static void setRandom(boolean rand)
	{
		random = rand;
	}
	
	public boolean getRandom()
	{
		return random;
	}
}
