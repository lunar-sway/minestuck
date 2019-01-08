package com.mraof.minestuck.entity.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityHangingArt<T extends EntityHangingArt.IArt> extends EntityHanging implements IEntityAdditionalSpawnData
{
	public T art;
	private static boolean random = true;
	
	private int meta = 1;
	
	public EntityHangingArt(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityHangingArt(World worldIn, BlockPos pos, EnumFacing facing)
	{
		super(worldIn, pos);
		List<T> artList = Lists.<T>newArrayList();
		
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

	public EntityHangingArt(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta, boolean random)
	{
		super(worldIn, pos);
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
	
	@SideOnly(Side.CLIENT)
	public EntityHangingArt(World worldIn, BlockPos pos, EnumFacing facing, String title)
	{
		this(worldIn, pos, facing);
		
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
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("Motive", this.art.getTitle());
		super.writeEntityToNBT(compound);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		String s = compound.getString("Motive");
		
		for(T art : this.getArtSet())
			if(art.getTitle().equals(s))
			{
				this.art = art;
				break;
			}
		
		if(this.art == null)
			this.art = this.getDefault();
		
		super.readEntityFromNBT(compound);
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
				
				if(entityplayer.capabilities.isCreativeMode)
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
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
			boolean teleport)
	{
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
	}
	
	@Override
	public void writeSpawnData(ByteBuf data)
	{
		data.writeByte(this.facingDirection.ordinal());
		
		data.writeInt(this.hangingPosition.getX());
		data.writeInt(this.hangingPosition.getY());
		data.writeInt(this.hangingPosition.getZ());
		
		String name = art.getTitle();
		data.writeInt(name.length());
		for(char c : name.toCharArray())
			data.writeChar(c);
	}
	
	@Override
	public void readSpawnData(ByteBuf data)
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
	
	public ItemStack getStackDroppedMeta(int meta)
	{
		Item stack = getStackDropped().getItem();
		return new ItemStack(stack, 1, meta);
	}
	
	public abstract Set<T> getArtSet();
	
	public abstract T getDefault();
	
	public abstract ItemStack getStackDropped();
	
	public static interface IArt
	{
		public String getTitle();
		
		public int getSizeX();
		
		public int getSizeY();
		
		public int getOffsetX();
		
		public int getOffsetY();
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
